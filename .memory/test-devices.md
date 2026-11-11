---
name: test-devices
description: The owner's two adb devices (Xiaomi phone, iPlay 40H tablet) and why chart pushes to Android/data fail on the tablet
metadata:
  type: reference
---

- **Xiaomi 23090RA98G (`zircon`), Android 16:** the phone. `adb push` into
  `/sdcard/Android/data/com.dentex.troller/files/charts/` works, as in the readme.
- **iPlay 40H tablet (`T1020H`), Android 11:** added 2026-11-11. The adb shell gets "Permission
  denied" inside `Android/data/<app>/`, and `run-as com.dentex.troller` can't reach
  `/sdcard` or `/storage/emulated` either. So adb can't write charts into the app folder there.
  Push to `/sdcard/Download/` instead, then use Settings, Charts, Import chart file… (added
  2026-11-11 because the chart screen only offers import while a chart is missing).

Both are often connected at once, so `adb` needs `-s <serial>`; check `adb devices -l` each session
([[dev-environment]]).
