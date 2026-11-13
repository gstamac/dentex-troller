---
name: chart-test-area
description: Build charts only for the test area around 44.2202 N 14.7955 E unless the owner asks for the full Adriatic
metadata:
  type: feedback
---

On 2026-11-04 the owner asked to limit chart builds to the section around their test point
(44.22023319537834, 14.795524186248247) from then on. Build with
`docker run -e BBOX="14.68 44.14 14.91 44.30" ...` (about 18 x 18 km).

**Why:** a full Adriatic build took about 35 minutes with the old coarse bands; with the 1 m bands
(since 2026-11-07) it took about 3 hours on 2026-11-13 (depth bands 42 min, tiles 2 h, plus the
rainbow). The test area takes about 5 minutes, mostly reading the land-polygon zip. Since
2026-11-13 the land zip and all eight Geofabrik extracts are in `tools/charts/build/data`.

**How to apply:** pass that BBOX on every chart build. Build the whole Adriatic only when the owner
asks for it explicitly. The rainbow file of the test area only has zooms 10 and 11.
