---
name: offline-chart-decisions
description: Why the chart is EMODnet + OSM PMTiles outside the APK, and why Navionics data is off the table
metadata:
  type: project
---

On 2026-11-03 the owner asked for an Adriatic depth chart in the app. It is built from EMODnet DTM 2024
depths and OSM land polygons with `tools/charts`, and installed as a separate `adriatic.pmtiles` file
(adb push or in-app import), not bundled in the APK.

**Why:**
- The file is too large for git (GitHub refuses files over 100 MB).
- MapLibre reads PMTiles only from a real file path, not from assets.

**Navionics:** the owner asked about pulling tiles out of the installed Navionics app. That was refused:
the charts are encrypted and the licence forbids extraction. The legitimate path is the Garmin Navionics
Mobile SDK, which needs a developer request; the owner had not applied as of 2026-11-03.

**How to apply:** don't suggest bundling the chart in the APK, and don't revisit Navionics extraction.
Near-shore depth detail is limited by the roughly 115 m EMODnet grid; the SDK is the upgrade path.
