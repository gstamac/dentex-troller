---
name: osm-land-download
description: OSM land polygons: osmdata.openstreetmap.de was down 2026-11-06/07, back 2026-11-13; the zip is now in tools/charts/build/data
metadata:
  type: reference
---

On 2026-11-06 and 2026-11-07, osmdata.openstreetmap.de (139.17.3.227) refused or timed out every
connection. It answered again on 2026-11-13, and the full land polygon zip (930 MB) is now in
`tools/charts/build/data`, so builds use it again without `LAND=`.

The Docker download of it broke at 500 MB when Docker Desktop stopped. `curl -C -` resumed it
outside Docker; check it with `unzip -tq` before renaming the `.part` file.

The older fallback `data/land.geojsons` covers only the test area ([[chart-test-area]]). Building
the land from Geofabrik with osmcoastline was proposed as a lasting fallback; no decision.

**How to apply:** if a chart build fails at the land download, keep the existing zip, check
whether the host is reachable, and resume rather than restart.
