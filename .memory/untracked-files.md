---
name: untracked-files
description: Local files in the repo that must never be committed (fishing spots GPX, depth CSVs, contour GeoPackages, SonarChart pmtiles)
metadata:
  type: project
---

Never stage these files, even with `git add -A`:

- `docs/Navionics_archive_export.gpx`: it holds the owner's fishing spots.
- `docs/csv/`: depth CSV files. It is gitignored since 2026-11-06.
- `docs/gpkg/`: depth contour GeoPackages (about 900 MB, Navionics-derived). It is gitignored since 2026-11-07.
- `docs/molat_sonarchart.pmtiles` and `docs/adriatic_sonarchart.pmtiles`: Navionics SonarChart
  builds (their metadata says so). The readme's adb push lines for them are the owner's local edit
  and stay out of commits too.

On 2026-11-08 the phone had the Adriatic SonarChart file installed as `adriatic.pmtiles` (563 MB).
tippecanoe dropped about 1–3 million of its depth polygons, so the background shows through in
patches. That is not an app bug, and Claude doesn't troubleshoot that file.

**Why:** these are private data, or research and vendor material the owner doesn't want in the repo.

**How to apply:** when committing, add paths explicitly (for example `git add app/src readme.md`) and check that `git status` still lists these as untracked.
