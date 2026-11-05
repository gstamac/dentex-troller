---
name: hardware-verification-status
description: Which behaviour is covered only by unit tests and still needs checking on the real remote and motor
metadata:
  type: project
---

On 2026-11-02 these fixes were made and covered by unit tests (59 tests passing), but not yet
tried on the real Haswing remote and motor:

- The 5 s link watchdog, which sets the command gate to `LINK_LOST`.
- The heartbeat now starts as soon as the BLE link is Ready, before the motor's first heartbeat.
- The route upload no longer repeats MISSION_COUNT once requests start, and it now fails on
  timeouts. The route download now stops its requests when it is cancelled or fails.
- `AndroidBleTransport.write` now throws when the link is not Ready or the queue is full.
- The GATT client is now closed on a remote disconnect, on a discovery failure and in `onCleared`.

On 2026-11-04 the owner saw "GPS signal 250" and coordinates 0, 0 on the Dashboard with the real
motor (probably indoors). The app now treats a 0/0 position as no fix and labels the GPS value with
Helmsman's bands (lower is better, 250 = no fix). Still to confirm outdoors: the value drops below
171 and real coordinates arrive once the motor has a fix.

On 2026-11-05 "Move boat here" was added (long press on the chart): a one-waypoint upload, then
navigation mode (15) once, confirmed by the mode the motor reports in `ATTITUDE.pitchspeed`
(0 manual, 1 anchor, 2 navigation, 3 cruise, 4 record, from Helmsman's `boatModelState`).

Owner's first test, 2026-11-05: navigation turned the motor towards the target and the go-to was
confirmed. It runs at the speed the motor had last (at 0 it only turns, and after a STOP it starts
again at the earlier speed). Speed +/- work in navigation. Manual (13) left the propeller running;
only STOP (11) stopped it. So the go-to now sends one Speed up when the speed is 0, and ends with
Manual then STOP. Still unknown: what the motor does on reaching the target (the app stops it
unless it reports Anchor).

The `simulator` module (sim build, 2026-11-05) models these observations. When a field test
shows different motor behaviour, change `SimulatedMotor` and its tests to match, so land tests stay
honest.

**Why:** the motor behaviour is inferred from the decompiled app ([[helmsman-source-as-reference]]),
not from captured traffic.
**How to apply:** when the owner reports a field test, update this list. Remove the items that
were confirmed, and treat failures here as the first suspects.
