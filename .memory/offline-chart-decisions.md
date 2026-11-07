---
name: offline-chart-decisions
description: Why the chart is EMODnet + OSM PMTiles outside the APK
metadata:
  type: project
---

On 2026-11-03 the owner asked for an Adriatic depth chart in the app. It is built from EMODnet DTM 2024
depths and OSM land polygons with `tools/charts`, and installed as a separate `adriatic.pmtiles` file
(adb push or in-app import), not bundled in the APK.

On 2026-11-04 the owner asked for EMODnet's "Mean depth rainbow colour (no land)" WMS layer and chose to
render it offline (`adriatic-rainbow.pmtiles`, built from the same grid) over loading the WMS live,
because there may be no mobile data at sea.

**Why:**
- The file is too large for git (GitHub refuses files over 100 MB).
- MapLibre reads PMTiles only from a real file path, not from assets.

**How to apply:** don't suggest bundling the chart in the APK. Since 2026-11-06 `tools/charts` can
also build from CSV soundings ([[soundings-charts]]).
Near-shore depth detail is limited by the roughly 115 m EMODnet grid; the SDK is the upgrade path.
