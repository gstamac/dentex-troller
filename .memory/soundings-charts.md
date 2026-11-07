---
name: soundings-charts
description: Charts from the owner's CSV soundings: what the owner wants, the open chart-style problems found 2026-11-07, and which CSV files Claude won't process
metadata:
  type: project
---

On 2026-11-06 and 2026-11-07 the owner asked for a chart built from CSV soundings (sonar logs) on a
fine grid (they find 20 m too coarse). `tools/charts` now has:

- `SOUNDINGS=...`, which lays soundings over EMODnet.
- `DEPTH_SOURCE=soundings`, which uses soundings only for depths, with land and shoreline from OSM.
- `build-from-csv.ps1`, a one-command wrapper that reads `docs/csv` (gitignored).

The owner wants the rainbow tiles built from the soundings too. On 2026-11-07 they asked for an
EMODnet-only rainbow, then had it reverted.

**Open problem (2026-11-07):** after a soundings-only build, the owner said only the rainbow showed
the right depths. The vector tiles were checked against the source grid and match it (98% of
pixels). The cause is the app's chart style ([[offline-chart-decisions]]):

- Water with no depth band is painted with the background colour, which is the 0–2 m shade. So the
  gaps between survey tracks look shallow.
- `ChartStyle` has only 8 water shades (0 to 30 m). Every band deeper than 30 m gets the same
  near-white `deep` colour.
- The owner's tracks are wider apart than `SOUNDINGS_FILL` × cell (8 × 2 m), which leaves the gaps.

Claude proposed three fixes, and the owner hasn't decided yet:

1. Coarser cells with more fill, for example `-Cell 0.00005 -Fill 10`.
2. A distinct "no depth" colour.
3. More shades past 30 m.

**How to apply:** test the tools with generated samples, never with those two files. Ask before
changing the app colours.
