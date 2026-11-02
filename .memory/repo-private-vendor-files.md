---
name: repo-private-vendor-files
description: The repo is private; the decompiled Helmsman APK and sources under docs/helmsman stay in git
metadata:
  type: project
---

The GitHub repo (gstamac/dentex-troller) is private and will stay private. The vendor material in
`docs/helmsman/` (`base.apk` and the full decompiled `helmsman-source/`, about 10,000 files) is kept
in git on purpose.

**Why:** the owner decided this on 2026-11-02 after the redistribution risk was raised.
**How to apply:** do not suggest removing these files again. Use them as the reference when you
verify protocol behaviour (see [[helmsman-source-as-reference]]).
