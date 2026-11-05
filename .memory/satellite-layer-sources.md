---
name: satellite-layer-sources
description: Satellite layer for the chart was evaluated on 2026-11-05 and dropped; which imagery sources were ruled out and why
metadata:
  type: project
---

On 2026-11-05 the owner asked for a satellite option in the chart's layers menu. No source fit, so
the menu only has Default and Rainbow depths. `ChartLayer` in `ChartStore.kt` is where a third
layer would go.

Sources ruled out:
- **EOX Sentinel-2 cloudless** (free WMTS, CC BY-NC-SA for non-commercial use): the owner rejected
  it as too coarse at about 10 m per pixel.
- **Croatian DGU orthophoto DOF5 2021/22** (0.5 m, `geoportal.dgu.hr` WMS, open licence of the
  Republic of Croatia): land only, so the sea comes back blank.
- **Esri, Google and Bing imagery**: their terms don't allow bulk downloading tiles into our own
  offline file.
- **Mapbox Satellite, or Esri through an ArcGIS account**: offline only inside their own SDKs and
  within limits, which would mean replacing MapLibre.

**Why:** imagery has to work offline at sea, and has to cover the water itself, not only the land.

**How to apply:** don't propose these sources again unless their terms or coverage change. A
satellite layer is worth adding only if a source covers the sea in detail and allows offline use.
See [[offline-chart-decisions]].
