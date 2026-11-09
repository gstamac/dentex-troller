# Memory index

- [Repo private, vendor files stay](repo-private-vendor-files.md): keep `docs/helmsman`; don't suggest removing it again
- [Helmsman source as reference](helmsman-source-as-reference.md): which decompiled files define the protocol behaviour that is copied
- [Dev environment](dev-environment.md): JDK 17 path, Gradle commands, Python 2 only, CRLF files, adb port changes
- [Hardware verification status](hardware-verification-status.md): untested 2026-11-02 fixes; go-to tested 2026-11-05; 2026-11-06 chart controls not yet seen on the phone
- [Offline chart decisions](offline-chart-decisions.md): chart is EMODnet+OSM PMTiles installed outside the APK
- [Chart test area](chart-test-area.md): build charts with BBOX="14.68 44.14 14.91 44.30" unless the full Adriatic is asked for
- [Satellite layer sources](satellite-layer-sources.md): satellite layer dropped 2026-11-05; Sentinel-2 too coarse, DGU land only, Esri/Google/Bing not offline
- [Motor controls UI preferences](motor-controls-ui-preferences.md): icon buttons always shown, disabled not hidden; no go-to dialog or panel; chart has no bottom menu, other screens close with X, STOP only on Controls and chart; no Route screen; no distance-based route joining
- [Files never to commit](untracked-files.md): fishing-spot GPX; docs/csv and docs/gpkg gitignored; SonarChart pmtiles and their readme lines
- [Other motor brands research](other-motor-brands-research.md): Minn Kota BT i-Pilot Link most feasible, QUEST riskier, MotorGuide needs N2K hardware; nothing started
- [Track recording decisions](track-recording-decisions.md): yellow 2.5 px line; ignore impossible jumps, join sim jumps, no break when still; background recording undecided
- [Soundings charts](soundings-charts.md): CSV soundings tools; rainbow stays from soundings; shading interpolated since 2026-11-08; still open: no-depth colour, shades past 30 m
- [OSM land download](osm-land-download.md): osmdata.openstreetmap.de down 2026-11-06/07; LAND fallback covers only the test area
