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
| `simulator` | `com.dentex.troller.simulator` | Simulated remote and motor for the `sim` build |
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
adb connect 192.168.88.101:44829
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
adb -s $device install 'C:\Work\github\dentex-troller\app\build\outputs\apk\sim\app-sim.apk'
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

## Simulator build

The `sim` build (`./gradlew :app:assembleSim`) replaces Bluetooth with a simulated remote and
motor, for testing on land. It cannot reach the real remote, so no command can move the boat, and
the chart shows a **SIMULATOR** badge. It installs over the debug app and keeps its charts, routes
and pins; it remembers its simulated remote apart from the real one.

The simulator (module `simulator`) talks MAVLink bytes, so the app's decoding, command gate,
watchdog, route upload and go-to run as on the water. It sends the motor's telemetry once a
second and follows what was seen on the motor: navigation steers to the stored route at the last
speed (at speed 0 it only turns), manual mode leaves the propeller running, STOP stops it, and
anchor mode holds the spot. Without a GPS fix it refuses navigation and anchor mode. The boat
starts at the test position 44.216687, 14.793422.

The **Log** screen has buttons for the common cases. Everything can also be set with adb:

located in sim-messages.md

Each command prints the simulator state in the `data=` of the broadcast result and logs it on
the Log screen. The model is only as good as what is known about the motor: confirm anything new
on the water and update the simulator when the motor behaves differently.

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

1. **Connect** — the app opens on the chart and, without a link, looks for the remote it was connected to last and connects to it. It does the same whenever the link is lost, scanning for 30 seconds at a time with a short pause in between, until the remote is back; the chart's status badge then shows a turning arc. After a manual Disconnect it waits for you to connect again. Otherwise grant the Bluetooth permissions, scan for the remote and connect to it. The app
   opens the GATT service, subscribes to the notify characteristic and starts a MAVLink parser.
   The scan runs without a service filter, because the remote does not necessarily advertise the
   GATT service in its advertisement packet.
2. **Dashboard** — shows the motor profile, the telemetry and the age of the last update. The
   profile is only reported after a heartbeat with a valid checksum was received.
3. **Chart** — an offline depth chart of the Adriatic with the boat position and heading from the
   motor telemetry. Tap the chart for the depth, a seamark, a pin or the selected route: a pull-up panel shows its title, and pulled up (drag or tap the title) the details and the distance and bearing from the boat; for a route, to the marked waypoint and its nearest point. A tapped depth or seamark is marked with a ring. A long press always shows the spot itself (its depth or land), ignoring pins, routes and seamarks close to it. The panel runs along the bottom edge and is always there: with nothing tapped it shows the boat (position source, position, heading, speed); dragging it down from its small state goes back to that. The **route** button under the layers button switches route mode: tap the chart to add a waypoint, tap the line between two waypoints to insert one there, tap a waypoint to select it, then drag it to move it or delete it, and tap another route to select it; **Routes** lists every saved route to pick, rename or delete, and **New route** starts one. Routes are saved on the phone automatically. **Pins** in the layers menu shows or hides marked spots, and **Import GPX…** adds the waypoints of a GPX file as pins (for example the markers exported from the Navionics Boating app; ones already there are skipped) and its routes as saved routes. The pin's panel can also delete it. While the motor is connected and ready, the **navigate** button of the motor controls (the blue navigation arrow) moves the boat to the selected pin, seamark or tapped spot, or follows the selected route from its marked waypoint (a tap on a waypoint marks that waypoint and a tap on a section marks the first waypoint of the section, and the waypoints before it are left out). It starts at once, without a confirmation, and the selection stays. While the go-to runs the chart draws a dashed line from the boat to the target, or to the waypoint the motor heads for; there is no go-to panel, and a go-to that fails says why in a snackbar. The
   **layers** button under the status badge picks the chart layer: Default or Rainbow depths. The boat is green when
   the position comes from the motor and light blue when the motor has no recent fix and the
   phone's own GPS is used instead (**Use phone GPS** asks for the permission); it turns grey when
   neither has a recent fix. The phone position is only displayed. Navigating to a pin, a seamark or a
   tapped spot uploads the spot as a
   one-waypoint route (replacing the route stored on the motor) and, a second after the motor acknowledges it,
   switches the motor to navigation mode once, as Helmsman starts a route. The go-to only counts as under way
   when the motor reports navigation mode; otherwise it fails and nothing is sent again. Navigation runs at the
   speed the motor had last, so at speed 0 one speed step is sent. **Stop** on the motor controls sends manual mode and then
   STOP, because manual mode leaves the propeller running; the same stop is sent when the motor leaves navigation
   by itself, unless it reports anchor mode. **Anchor** switches from navigation to anchor mode once, so the
   motor holds the boat where it is, without the stop. A lost link stops a go-to that has not started, and nothing is resent after a reconnect.
   While the link to the remote is up, **motor controls** float over the bottom of the chart as round icon
   buttons and follow the mode the motor reports. On the left is the speed, a rabbit (faster) above a turtle
   (slower), with turn left and right beside it; they are always there and disabled where the mode has no use
   for them (steering outside manual, both in anchor mode or before the motor answers). On the right are the actions, with the symbols
   of the status badge, always in the same order: the anchor, then Start or Stop, then the navigation arrow
   (navigate). Start (the propeller) shows in manual while the propeller is off and the red Stop square
   otherwise; Stop ends navigation with manual mode and STOP, and in anchor and other modes sends manual mode,
   then STOP. The anchor is disabled in anchor mode, and in navigation until navigation mode was sent. The
   navigation arrow is disabled while the motor navigates; otherwise it goes to the selection. When
   anchoring interrupted a go-to of this app and nothing new was selected since it started, it continues the
   rest of it instead, from the waypoint the motor headed for, with a new upload. With nothing to go to it is
   disabled. Until the motor answers only Stop is
   enabled. Each control sends its command once. Apart from these and STOP, the chart sends nothing to the motor. It needs the chart file, see [Offline chart](#offline-chart).
4. **Controls** — propulsion commands stay disabled until a type 110 motor was confirmed. The stop
   command is always available: a STOP button at the top of every screen, and on the chart a Stop on the motor controls whenever the link to the remote is up, also before the motor answers. The **status badge** at the top right of the chart only shows the state and opens the connect screen: no connection (grey, crossed out; a turning arc while connecting), the remote connected but no motor (amber, with a question mark), or the motor's state (stopped, manual, anchored, navigation) with its speed as a ring of 10 dashes, as Helmsman counts speed steps. Switching to a mode asks for a confirmation.
5. **Route** — uploads the selected route to the motor and downloads the one stored on it, which can be saved as a new route. Uploading a route never starts the motor.
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
  the chart, and **Rainbow depths** in the layers menu for the rainbow file. The app tells the files apart by their
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
