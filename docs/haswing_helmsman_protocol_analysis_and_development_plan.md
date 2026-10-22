# Haswing Cayman-B GPS 80 lb / 24 V / 60 in — Helmsman protocol analysis and Android development plan

**Date:** 2026-10-21  
**Purpose:** Reference for building a Kotlin Android application that communicates directly with the motor over Bluetooth Low Energy (BLE), without extra hardware.  
**Evidence:** User-supplied `base.apk`, split APKs, and JADX-exported `helmsman-source.zip`. Decompiled source is not an official protocol specification; all on-device behavior requires validation.

## 1. Executive summary

The Helmsman application contains BLE GATT transport and MAVLink serialization code, including Haswing-specific message types. A likely motor implementation for this project is `HMMotor50736` (the app's **type 110** profile). The physical remote has corresponding functions, but that alone does **not** establish the motor's heartbeat type; confirm it from a received MAVLink HEARTBEAT before enabling commands.

The motor already performs GPS spot lock without the phone. The replacement application should send high-level mode/control/route commands and read motor telemetry; it should not try to implement a real-time GPS autopilot on the phone.

**Recommended first deliverable:** a read-only BLE + MAVLink diagnostic Android app. Add active controls only after observing the motor's heartbeat, validating framing/checksums, and confirming safe stop behavior.

## 2. Source-code map

Paths relative to `helmsman-source/sources/`:

| File | Relevance |
|---|---|
| `im/helmsman/helmsmanandroid/controller/BleBlueBoatDataIOController.java` | BLE connection, characteristics, outgoing byte queue, fragmentation |
| `im/helmsman/lib/product/HMMotor50736.java` | Candidate type-110 motor control, modes, waypoint methods, telemetry |
| `im/helmsman/lib/product/HMMotorH1.java` | Alternative motor profile (type 11) |
| `im/helmsman/helmsmanandroid/utils/MavlinkUtil.java` | App-level MAVLink handling |
| `im/helmsman/lib/util/MavlinkUtil.java` | SDK MAVLink utility |
| `com/MAVLink/MAVLinkPacket.java` | Packet serialization/framing |
| `com/MAVLink/Parser.java` | Stream parser |
| `com/MAVLink/helmsman/msg_command_short.java` | Custom MAVLink message 183 for alternative profile |
| `com/MAVLink/common/msg_command_long.java` | MAVLink COMMAND_LONG message 76 |

## 3. BLE transport findings

| Parameter | Value / observation |
|---|---|
| GATT service | `0000ffe0-0000-1000-8000-00805f9b34fb` |
| Candidate outgoing characteristic | `0000ffe1-0000-1000-8000-00805f9b34fb` |
| Candidate incoming characteristic | `0000ffe4-0000-1000-8000-00805f9b34fb` |
| Outgoing queue capacity | 20 byte-array messages (`LinkedBlockingQueue<byte[]>(20)`) |
| Write chunk size | 20 bytes |
| Inter-chunk delay in original code | 15 ms |
| BLE connection API | `BluetoothDevice.connectGatt(...)` |

The BLE controller's write loop slices queued byte arrays into 20-byte chunks and writes them to the GATT characteristic. It does not add an obvious per-chunk application header in that loop. Verify notification subscription, CCCD setup, write type, characteristic permissions and connection initialization directly in the callback implementation before copying them. BLE notifications can split or combine MAVLink frames; implement a continuous streaming parser.

### Android implementation notes

- Scan using `BluetoothLeScanner`, filtering by service UUID where supported; fall back to device selection if advertising omits the service.
- Request the relevant Android Bluetooth runtime permissions (`BLUETOOTH_SCAN`, `BLUETOOTH_CONNECT` on Android 12+) and account for OS-version differences.
- Connect with `connectGatt`, discover services, locate `FFE1` and `FFE4`, and enable notifications via the Client Characteristic Configuration Descriptor where required.
- Serialize all GATT operations. Do not issue overlapping writes; respect callbacks, API-level write behavior and error results.
- Use conservative 20-byte chunks initially, with logs for writes, errors, disconnects and incoming notifications.
- Maintain a byte-stream buffer and feed it into a MAVLink parser, not a packet-per-notification decoder.

## 4. MAVLink framing

The decompiled code contains MAVLink v1-style packets. The expected frame layout is:

```
FE | payload_length | sequence | system_id | component_id | message_id | payload | CRC_low | CRC_high
```

Payload fields are generally little-endian. MAVLink uses an X.25-style checksum with a message-specific CRC-extra byte. **Do not hardcode CRC-extra values from generic MAVLink libraries without checking the dialect embedded in this APK.** Confirm exact CRC extras and message layouts from the bundled MAVLink classes.

The source contains standard message classes and a custom `com.MAVLink.helmsman` dialect. The existence of a message class is not proof that this motor implements it.

## 5. Motor profiles and identification

The application includes at least two different command implementations:

- **Type 110:** `HMMotor50736`, generally uses `COMMAND_LONG` (MAVLink message ID 76) for direct motor commands.
- **Type 11:** `HMMotorH1`, uses custom `COMMAND_SHORT` (message ID 183) for several controls.

The motor's incoming `HEARTBEAT` (`msg_heartbeat`, message ID 0) includes a `type` field used by the application to identify its profile. `HMMotor50736` also reads `custom_mode` as firmware-version information and `base_mode` for disconnect protection. **Confirm `type == 110` from the actual motor before transmitting type-110 commands.**

## 6. Type-110 motor controls (source-verified assignments)

`HMMotor50736.java` constructs `msg_command_long` messages with these `command` values:

| Action | `COMMAND_LONG.command` | Source behavior |
|---|---:|---|
| Turn right | 6 | `turnRight()`, may set `param1` to PWM |
| Turn left | 7 | `turnLeft()`, may set `param1` to PWM |
| Speed up | 8 | Increment speed |
| Speed down | 9 | Decrement speed |
| Motor on | 10 | Enable propulsion |
| Motor off | 11 | Disable propulsion |
| Full speed | 3 | Full-speed control |

The values above are app-generated protocol commands, **not** a guarantee that any connected motor will accept them. Inspect the full `COMMAND_LONG` field initialization (including `param1`, target IDs, and `compid`) and compare with real app traffic before transmitting. Steering behavior may depend on press/release or repeat timing, so never assume a single command safely stops turning.

### Operating modes

`HMMotor50736` defines these constants:

| Mode | Value |
|---|---:|
| Anchor / spot lock | 12 |
| Manual | 13 |
| Cruise | 14 |
| Navigation | 15 |
| Record waypoint | 181 |

Its `changeModel(int)` method assigns the argument to `msg_command_long.command` and sends it. These values therefore have direct source-code support as mode-change command identifiers. The mode switch's preconditions, confirmation and interaction with active propulsion must be tested.

### Heartbeat behavior

`HMMotor50736` has a `sendHeartBeat()` method constructing an outgoing heartbeat with `type = 106` and `mavlink_version = 3`. **Do not confuse this outgoing app heartbeat type with the expected incoming motor profile type 110.** Inspect timer scheduling and disconnect protection to determine required heartbeat frequency.

## 7. Alternate type-11 profile (for completeness)

`HMMotorH1` sends custom `msg_command_short` with these commands:

| Action | `COMMAND_SHORT.command` |
|---|---:|
| Turn left | 0 |
| Turn right | 1 |
| Speed up | 2 |
| Speed down | 3 |

`msg_command_short` has message ID **183** and payload length **4**:

```
uint16 command (little-endian)
uint8  target_system
uint8  target_component
```

Its `pack()` sets packet `sysid = 255` and `compid = 190`. These are verified specifically for `COMMAND_SHORT`; do **not** assume the same sender identifiers for every `COMMAND_LONG` packet without checking its packer. This profile is retained as a diagnostic fallback, not the initial target.

## 8. GPS telemetry and route protocol

The app contains handlers/classes for the following MAVLink messages:

| ID | Message | Intended investigation |
|---|---|---|
| 0 | HEARTBEAT | Identify profile, firmware, disconnect protection |
| 27 | RAW_IMU | Check Haswing-specific reinterpretation of fields |
| 30 | ATTITUDE | Heading / mode / battery interpretation in motor class |
| 33 | GLOBAL_POSITION_INT | Latitude, longitude, velocity and GPS status interpretation |
| 44 | MISSION_COUNT | Begin waypoint upload |
| 39 | MISSION_ITEM | Send route waypoint |
| 40 | MISSION_REQUEST | Waypoint request/handshake |
| 43 | MISSION_REQUEST_LIST | Begin route download |
| 45 | MISSION_CLEAR_ALL | Clear stored mission |
| 47 | MISSION_ACK | Mission transfer acknowledgement |
| 76 | COMMAND_LONG | Type-110 motor/mode commands |

**Source-confirmed waypoint methods in `HMMotor50736`:**

- `uploadWaypointRequest(count)` sends `msg_mission_count` with the requested count.
- `upLoadWayPount(lat, lon, seq)` constructs `msg_mission_item`, sets `x = lat`, `y = lon`, `target_system = 106`, and `seq = seq`.
- `requestDownLoadRoute()` packs `msg_mission_request_list`, then sets packet `compid = 200` and `sysid = 106` before sending.
- `sendClearCacheCommand()` sends `msg_mission_clear_all`.

The full mission upload/download state machine, retry policy, route coordinate units, sequencing and acknowledgements need a separate caller-level trace. Avoid assuming ordinary MAVLink autopilot semantics for fields that Haswing may repurpose.

## 9. Application-level command identifiers

The APK contains string identifiers including:

```
MB/Command/Send/BC/Mode/Anchor
MB/Command/Send/BC/Mode/Cruise
MB/Command/Send/BC/Mode/Manual
MB/Command/Send/BC/Mode/Navigation
MB/Command/Send/BC/Route/Upload
MB/Command/Send/BC/Route/Download
MB/Command/Send/TM/Start
MB/Command/Send/TM/Stop
MB/Command/Send/TM/Speed/Up
MB/Command/Send/TM/Speed/Down
```

Treat these as **application event or command names**, not literal BLE payload strings, until their callers and serialization paths are traced.

## 10. Risks, gaps and confidence

| Finding | Confidence | Outstanding validation |
|---|---|---|
| BLE service and UUID constants | High (source) | Confirm against actual motor GATT discovery |
| 20-byte chunking, 15-ms delays | High (source) | Test write pacing on modern Android |
| Type-110 command numbers | High (source) | Confirm incoming heartbeat type and real responses |
| Mode constants and `changeModel` behavior | High (source) | Confirm safe transitions and acknowledgements |
| MAVLink v1 framing | High (source) | Confirm wire captures and CRC extras |
| GPS/telemetry field meanings | Medium | Verify actual units and firmware variants |
| Full mission upload/download | Partial | Trace callbacks, request order, retries and ACKs |
| Activation/authentication | Unknown | Trace activation/network calls and offline behavior |
| Failsafe on disconnect | Unknown | Observe safely; never assume propulsion stops |

The APK is a decompiled implementation, not a public API contract. Firmware revisions may differ. A motor command may cause immediate physical motion; validate all behavior with propeller removed, motor secured, independent power cutoff and original remote available.

## 11. Kotlin Android development plan

### Phase 0 — Repository and architecture

**Deliverable:** Compilable Android Studio project with a clean modular design.

Suggested modules/packages:

```
app/                         UI, permissions, lifecycle
ble/                         scanning, GATT session, queued writes
mavlink/                     frame parser, CRC, packet codec, dialect
haswing/                     motor profiles, commands, telemetry
navigation/                  route models and mission state machine
logging/                     raw hex logs, diagnostics export
```

Use Kotlin, Coroutines/Flow, Jetpack Compose and current stable Android Gradle Plugin/Kotlin/SDK versions **checked at implementation time**. Do not invent version numbers in the specification.

### Phase 1 — Read-only BLE diagnostic (first milestone)

**Implement:**

1. Scan and select a device; show MAC/address where Android permits.
2. Connect, discover `FFE0`, inspect `FFE1`/`FFE4` properties, subscribe to notifications.
3. Log timestamped raw incoming bytes and connection events.
4. Build an incremental MAVLink v1 parser with CRC checking.
5. Decode heartbeat; show `type`, `custom_mode`, `base_mode`, system/component IDs.
6. Export a redacted diagnostic log for comparison with Helmsman.

**Acceptance criteria:** Stable connection; at least one valid CRC-checked heartbeat; motor profile detected from actual data; no propulsion commands sent.

### Phase 2 — Telemetry dashboard

Decode `GLOBAL_POSITION_INT`, `ATTITUDE`, `RAW_IMU` and other observed messages. Show coordinates, heading, GPS quality, operating mode, battery and speed only after verifying Haswing-specific field interpretation. Display freshness timestamps and unknown/stale states rather than invented values.

**Acceptance criteria:** Telemetry agrees with motor/remote indicators and known GPS location; disconnections and stale data are obvious.

### Phase 3 — Safe command validation

1. Confirm `type == 110` and packet dialect.
2. Compare generated packets with Helmsman's captured BLE traffic.
3. Confirm heartbeat requirements and any initialization/activation exchange.
4. Add a prominent STOP command with explicit result/telemetry verification.
5. Add motor on/off and incremental speed with deliberate user interaction and safety interlocks.
6. Add steering only after determining press/release semantics and timeout behavior.

**Acceptance criteria:** Commands work consistently in a secured bench test; no uncontrolled repeat, stuck steering or restart after reconnect; STOP/failsafe behavior documented.

### Phase 4 — GPS modes

Implement read-only current mode first, then mode switching: manual, anchor, cruise and navigation. Verify motor-generated acknowledgements/status changes. GPS anchoring is performed by motor firmware; the phone requests mode changes rather than calculating steering corrections.

**Acceptance criteria:** Mode transitions agree with remote and motor telemetry; failed transitions produce actionable errors.

### Phase 5 — Route upload/download

Trace mission-manager callers and callbacks from the APK. Implement a state machine for `MISSION_COUNT`, requests, `MISSION_ITEM`, ACK, timeouts, retries and cancellation. Download and compare a known route before attempting custom uploads. Validate coordinate order, precision, waypoint limit, target IDs and mission sequencing. Never begin propulsion automatically after uploading a route.

**Acceptance criteria:** Round-trip upload/download of a small test route matches expected coordinates and sequence; malformed or interrupted transfers fail safely.

### Phase 6 — Production hardening

- Background/foreground lifecycle and Android permission handling.
- Reconnection policy that never replays a previous motor-on command.
- Explicit operator confirmation for navigation and high thrust.
- Watchdog, stale-telemetry indication and emergency stop UI.
- Persistent structured logs without unnecessary location history.
- On-water testing only after controlled bench tests, with independent control available.

## 12. Suggested core Kotlin interfaces

```kotlin
interface BleTransport {
    val incomingBytes: kotlinx.coroutines.flow.Flow<ByteArray>
    suspend fun connect(deviceId: String)
    suspend fun write(bytes: ByteArray)
    suspend fun disconnect()
}

interface MavlinkCodec {
    fun consume(bytes: ByteArray): List<MavlinkFrame>
    fun encode(message: MavlinkMessage): ByteArray
}

data class MotorIdentity(
    val heartbeatType: Int,
    val firmwareCustomMode: Long,
    val systemId: Int,
    val componentId: Int
)

interface HaswingMotor {
    val identity: kotlinx.coroutines.flow.StateFlow<MotorIdentity?>
    val telemetry: kotlinx.coroutines.flow.StateFlow<MotorTelemetry>
    suspend fun requestStop(): CommandResult
    // Add propulsion and navigation methods only after Phase 3 validation.
}
```

These interfaces are design proposals, **not extracted API signatures**. Define `MavlinkFrame`, `MavlinkMessage`, `MotorTelemetry` and `CommandResult` in the implementation.

## 13. Immediate next tasks

1. Inspect the complete BLE GATT callback: CCCD, write type, connection setup, and notification path.
2. Trace `manager.sendData(...)` to `MAVLinkPacket.encodePacket()` and check CRC-extra implementation.
3. Recover exact `COMMAND_LONG` packet fields and all sender/target identifiers.
4. Trace motor-profile factory selection from heartbeat type.
5. Trace mission upload/download state machine and mode-change callers.
6. Build the Phase-1 Android diagnostic app and obtain a **read-only** real-device capture.
7. Use that capture to verify the assumed type-110 profile and wire-level details.

## 14. Scope exclusions

- No RS-485, wired foot-pedal interface, or extra motor hardware.
- No replacement GPS navigation algorithm on the Android phone.
- No firmware modification or firmware-update command testing.
- No bypass of device activation or security mechanisms.
- No active motor-control packets before heartbeat/profile verification.

---

**Bottom line:** The APK provides a substantial starting point for a direct-BLE Kotlin controller. The type-110 commands and mission interfaces are visible in decompiled code, but real-device read-only verification is the essential next step before a safe, functional replacement app can be built.
