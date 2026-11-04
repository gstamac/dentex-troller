---
name: chart-test-area
description: Build charts only for the test area around 44.2202 N 14.7955 E unless the owner asks for the full Adriatic
metadata:
  type: feedback
---

On 2026-11-04 the owner asked to limit chart builds to the section around their test point
(44.22023319537834, 14.795524186248247) from then on. Build with
`docker run -e BBOX="14.68 44.14 14.91 44.30" ...` (about 18 x 18 km).

**Why:** a full Adriatic build takes about 35 minutes; the test area takes about 5, mostly reading
the land-polygon zip.

**How to apply:** pass that BBOX on every chart build. Build the whole Adriatic only when the owner
asks for it explicitly. The rainbow file of the test area only has zooms 10 and 11.
