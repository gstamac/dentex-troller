---
name: track-recording-decisions
description: How the owner wants the boat track recorded and drawn (2026-11-08), learned from their sim tests
metadata:
  type: feedback
---

The owner asked for track recording on 2026-11-08 and tested it in the sim build. Their corrections:

- The line is plain yellow, the same 2.5 px as route lines, with no darker edge ("too thick", "no border").
- The track starts at the boat's position when record is pressed, not after the first move.
- Simulator jumps of a few hundred metres (`sendsim ... position`) must be joined like real moves.
- A jump no boat could make (their test: 44.2180 to 40.2180) must be ignored as a bad fix: no point,
  no new segment, and the next good position joins on from the last good one. Starting a new segment
  at the bad spot was "not ok".
- A boat sitting still (on anchor) must not break the line when it moves again.
- Switching between motor and phone GPS starts a new segment. When the phone was unlocked, the stale
  sim motor fell back to the phone's real fix 205 km away, and a line was drawn to it.

Background recording: the owner asked for it on 2026-11-10. A location foreground service
(`TrackService`) with a notification keeps it recording; not yet tested on the water.

**Why:** the owner tests with the sim, whose teleports look like bad fixes to a speed rule alone.
**How to apply:** keep the thresholds in `TrackRecorder` (1 km jump allowance, 50 kn, 2 min) unless
the owner asks; test track changes against the cases above, which are in `TrackTest`.
