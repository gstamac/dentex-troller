---
name: osm-land-download
description: osmdata.openstreetmap.de (OSM land polygons) was unreachable 2026-11-06/07; the LAND fallback and its limits
metadata:
  type: reference
---

On 2026-11-06 and 2026-11-07, osmdata.openstreetmap.de (139.17.3.227) refused or timed out every
connection: HTTPS, HTTP and ping, from the owner's PC and from outside their network. Other OSM
hosts, such as openstreetmap.org and download.geofabrik.de, were fine.

The land polygon zip has never been downloaded to `tools/charts/build/data`. Builds use
`LAND=/data/land.geojsons` (or `-Land land.geojsons` with the wrapper) instead. That file is a copy of
an earlier build's land and covers only the test area ([[chart-test-area]]).

Claude proposed building the land from the Geofabrik extract with osmcoastline as a lasting
fallback. The owner hasn't decided on it yet.

**How to apply:** if a chart build fails at the land download, check whether this host is
reachable again before anything else.
