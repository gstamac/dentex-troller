# Dentex Troller

Dentex Troller is an Android app built with Jetpack Compose and Material 3 for controlling the
Haswing Cayman-B GPS trolling motor over Bluetooth Low Energy.

The application talks to the motor directly with the MAVLink dialect that the original Helmsman
application uses. It replaces the vendor application without any additional hardware.

> **Safety first.** A motor command can move the boat immediately. Test every command with the
> propeller removed, the motor secured and an independent power cut at hand. Never assume that a
> disconnect stops the propeller.

## Architecture overview

- UI: Compose + Material 3 single-activity app

The code is split into modules so that the protocol layers stay testable on the JVM:

| Module | Package | Responsibility |
|---|---|---|
| `mavlink` | `com.dentex.troller.mavlink` | MAVLink v1 framing, checksum, dialect table, message model |
| `ble` | `com.dentex.troller.ble` | BLE scan, GATT session, queued and chunked writes |
| `haswing` | `com.dentex.troller.haswing` | Motor profiles, commands, telemetry decoding |
| `navigation` | `com.dentex.troller.navigation` | Route model and the mission transfer state machine |
| `logging` | `com.dentex.troller.logging` | Bounded diagnostic log, hex dump, redaction |
| `app` | `com.dentex.troller` | Compose UI, permissions, lifecycle, view model |

### Protocol facts

The wire format was recovered from the Helmsman application and is reproduced exactly:

- GATT service `0000ffe0-…`, write characteristic `0000ffe1-…`, notify characteristic `0000ffe4-…`.
- MAVLink v1 frames, `STX = 0xFE`, little endian payload, checksum written low byte first.
- X.25 checksum with the per message CRC-extra byte of the Helmsman dialect.
- Outgoing frames use `sysid = 255` and `compid = 190`. The route download request is the only
  exception and uses `sysid = 106`, `compid = 200`.
- The type 110 motor profile uses `COMMAND_LONG` (message id 76) for propulsion and for the
  operating modes.

The motor reuses standard MAVLink messages for non standard data. `RAW_IMU` carries the GPS signal
and the motor state, `ATTITUDE` carries the heading, the battery level and the boat model, and
`GLOBAL_POSITION_INT` carries the position and the speed. Values that the motor has not sent are
shown as unknown instead of being invented.

## Build requirements

- JDK 17
- Android SDK Platform 36
- Android SDK Build-Tools 36
- Android Studio latest stable or Android command-line tools

## Exact toolchain used

- AGP: 9.4.0
- Gradle wrapper: 9.6.1
- Kotlin: 2.4.20
- compileSdk / targetSdk: 36
- minSdk: 26
- Java: 17

AGP 9 compiles the Kotlin sources of the Android modules with its built-in Kotlin support, so the
`org.jetbrains.kotlin.android` plugin is not applied. The plain Kotlin modules use the
`org.jetbrains.kotlin.jvm` plugin.

## How to run

1. Install the Android SDK and accept the licenses.
2. Ensure `local.properties` points to your SDK directory.
3. Configure the Gradle runtime to use JDK 17 or later (see [Gradle JVM troubleshooting](#gradle-jvm-troubleshooting)).
4. Run:

```bash
./gradlew test
```
```bash
./gradlew assembleDebug
```

5. Open the generated APK in a device/emulator or run from Android Studio.

## How to install it on the phone

```bash
adb pair 192.168.88.101:33977
```
```bash
adb connect 192.168.88.101:34387
```

```bash
adb devices
```
```bash
$device = adb devices | Select-String '^\d{1,3}(\.\d{1,3}){3}:\d+\s+device$' | ForEach-Object { ($_.Line -split '\s+')[0] } | Select-Object -First 1
```
```bash
adb disconnect
```
```bash
adb -s $device install 'C:\Work\github\dentex-troller\app\build\outputs\apk\debug\app-debug.apk'
```
```bash
Get-ChildItem -Path 'tools/charts/build' -File | ForEach-Object { adb -s $device push $_.FullName "/sdcard/Android/data/com.dentex.troller/files/charts/" }
```
```bash
adb -s $device push tools/charts/build/adriatic.pmtiles /sdcard/Android/data/com.dentex.troller/files/charts/adriatic.pmtiles
```
```bash
adb -s $device push tools/charts/build/adriatic-rainbow.pmtiles /sdcard/Android/data/com.dentex.troller/files/charts/adriatic-rainbow.pmtiles
```

## Verification

The protocol layer is verified against the wire format of the original application, not only
against itself:

- The Helmsman application contains a hard coded frame in its GATT write callback. That frame is
  used as a reference vector in `MavlinkCodecTest`, which pins the framing, the checksum
  algorithm, the CRC-extra table and the low byte first checksum ordering at the same time.
- `MavlinkDialectTest` guards the CRC-extra table against accidental replacement with the stock
  MAVLink values.
- The command builder, the telemetry mapping, the command gate, the mission handshake and the
  write chunking each have unit tests.

```bash
./gradlew test
```

## Using the application

The BLE peer is the **remote control** of the motor, not the motor itself. The phone talks to the
remote over Bluetooth and the remote talks to the motor, so the remote has to be switched on and
within range before it answers.

1. **Connect** — grant the Bluetooth permissions, scan for the remote and connect to it. The app
   opens the GATT service, subscribes to the notify characteristic and starts a MAVLink parser.
   The scan runs without a service filter, because the remote does not necessarily advertise the
   GATT service in its advertisement packet.
2. **Dashboard** — shows the motor profile, the telemetry and the age of the last update. The
   profile is only reported after a heartbeat with a valid checksum was received.
3. **Chart** — an offline depth chart of the Adriatic with the boat position and heading from the
   motor telemetry. Tap the chart for the depth or a seamark, long-press to add a waypoint to the route. The
   **Rainbow** chip switches the optional rainbow depth layer on and off. The boat is green when
   the position comes from the motor and light blue when the motor has no recent fix and the
   phone's own GPS is used instead (**Use phone GPS** asks for the permission); it turns grey when
   neither has a recent fix. The phone position is only displayed. The chart never sends
   anything to the motor. It needs the chart file, see [Offline chart](#offline-chart).
4. **Controls** — propulsion commands stay disabled until a type 110 motor was confirmed. The stop
   command is always available from the top bar. Switching to a mode asks for a confirmation.
5. **Route** — uploads and downloads waypoints. Uploading a route never starts the motor.
6. **Log** — shows the raw traffic in both directions and exports it as text so that it can be
   compared with a capture from the Helmsman application. Every device seen during a scan is
   logged there as well, with its name, address, signal strength and advertised services.

## Offline chart

The chart combines open data sets and is **not for navigation**:

- Depths: [EMODnet Bathymetry DTM 2024](https://emodnet.ec.europa.eu/en/bathymetry), CC BY 4.0.
  The grid is 1/960° (about 115 m), so near-shore contours are approximate.
- Shoreline: [OpenStreetMap land polygons](https://osmdata.openstreetmap.de/data/land-polygons.html),
  © OpenStreetMap contributors, ODbL.
- Seamarks: harbours, marinas, slipways, fuel, lights, buoys, beacons, rocks, wrecks, obstructions,
  anchorages and moorings from [OpenStreetMap](https://www.openstreetmap.org/) and
  [OpenSeaMap](https://www.openseamap.org/), through the [Geofabrik](https://download.geofabrik.de/)
  country extracts, © OpenStreetMap contributors, ODbL. They are only as complete as the
  community mapping.

The chart is made of two files that are not part of the APK:

- `adriatic.pmtiles`: land, depth bands, contours and seamarks, as vector tiles. This file is
  required.
- `adriatic-rainbow.pmtiles`: optional. The same depths as a smooth colour ramp, matching the
  "Mean depth rainbow colour (no land)" layer of the EMODnet map viewer, as raster tiles. It is
  about 115 m per grid cell, like the depth bands.

Build both with Docker:

```bash
docker build -t dentex/chart-builder tools/charts
docker run --rm -v "$PWD/tools/charts/build/data:/data" -v "$PWD/tools/charts/build:/out" dentex/chart-builder
```

Add `chart` or `rainbow` after the image name to build only one of the files. For a quick test
build of a small area, add `-e BBOX="14.68 44.14 14.91 44.30"` (west south east north) to
`docker run`; that takes about five minutes instead of half an hour. The first run
downloads about 1.1 GB (the EMODnet grid and the land polygons) into `tools/charts/build/data` and
reuses it afterwards. For the seamarks it also downloads the OpenStreetMap extracts of the
countries that overlap the area: about 190 MB for Croatia, and about 2.1 GB for the whole
Adriatic. Delete `tools/charts/build/data/osm` to fetch fresh ones. The results are written to `tools/charts/build/`. Install them on the phone
in one of two ways:

- Copy them with adb:

  ```bash
  adb push tools/charts/build/adriatic.pmtiles /sdcard/Android/data/com.dentex.troller/files/charts/adriatic.pmtiles
  adb push tools/charts/build/adriatic-rainbow.pmtiles /sdcard/Android/data/com.dentex.troller/files/charts/adriatic-rainbow.pmtiles
  ```

- Copy them to the phone in any way. Then open the **Chart** tab and tap **Import chart file** for
  the chart, and **Add rainbow layer** for the rainbow file. The app tells the files apart by their
  tile type.

The area, the contour depths, the zoom levels and the rainbow colours are set in
[build-chart.sh](tools/charts/build-chart.sh). The depths must match `ChartStyle.DEPTHS` in the
app, and the rainbow colours must match `ChartStyle.RAINBOW`, which draws the legend.

## Troubleshooting

**No device is found.**

- The remote control has to be switched on. The motor being on is not enough.
- The scan must run without a filter. The default is "Filter: off"; the service filter only
  matches peers that advertise `FFE0` in their advertisement packet, which the remote does not
  necessarily do.
- The app needs the `BLUETOOTH_SCAN` and `BLUETOOTH_CONNECT` permissions. The location permission
  is requested as well, because some vendor builds refuse to deliver scan results without it even
  though the manifest asserts `neverForLocation`.
- The phone's Bluetooth has to be switched on. The connect screen says so explicitly.
- The **Log** tab records every device the radio reports. If it stays empty, the scan itself is
  blocked; if it lists devices, the problem is the connection instead.

**The connection fails with "the expected characteristics were not found".**

The message lists the services and characteristics that the remote really exposes. Compare them
with `BleUuids` and adjust the UUIDs if the remote uses a different service.

**Commands are refused.**

The command gate stays closed until a heartbeat with a valid checksum was received from a type 110
motor. The **Log** tab shows the raw frames, so a checksum failure is visible there.

The gate also closes when the motor has sent nothing (apart from `SYS_STATUS`) for 5 seconds,
the same rule the Helmsman application uses to decide that the motor is offline. It reopens with
the next heartbeat. STOP is always sent regardless of the gate, but no command is sent while the
Bluetooth link itself is down; the app reports that instead of pretending the command went out.

## Gradle JVM troubleshooting

Gradle 9.6.1 must itself run on JDK 17 or later. Setting `sourceCompatibility` and
`targetCompatibility` in the app build file does not select the JVM used to run
Gradle.

For a terminal build on Windows, set `JAVA_HOME` to your JDK 17 installation and
put its `bin` directory first on `PATH` in the same PowerShell window where you
run Gradle. This machine's JDK installation is:

```powershell
$env:JAVA_HOME = 'C:\Tools\jdk-17\jdk-17.0.20.1+1'
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat --version
.\gradlew.bat test
.\gradlew.bat assembleDebug
```

The environment variable assignments only apply to that PowerShell window. Check
the `JVM:` line from `.\gradlew.bat --version`; it must show 17 or later. If it
still shows Java 8, stop existing Gradle daemons with `.\gradlew.bat --stop`, then
run the version check again in the same window.
