---
name: untracked-files
description: Local files in the repo that must never be committed (fishing spots GPX, depth CSVs)
metadata:
  type: project
---

Never stage these files, even with `git add -A`:

- `docs/Navionics_archive_export.gpx`: it holds the owner's fishing spots.
- `docs/csv/`: depth CSV files. It is gitignored since 2026-11-06.

**Why:** these are private data, or research and vendor material the owner doesn't want in the repo.

**How to apply:** when committing, add paths explicitly (for example `git add app/src readme.md`) and check that `git status` still lists these as untracked.
