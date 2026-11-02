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

**Why:** the motor behaviour is inferred from the decompiled app ([[helmsman-source-as-reference]]),
not from captured traffic.
**How to apply:** when the owner reports a field test, update this list. Remove the items that
were confirmed, and treat failures here as the first suspects.
