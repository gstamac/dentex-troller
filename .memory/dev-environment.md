---
name: dev-environment
description: How to build and test on the owner's Windows machine (JDK path, no Python 3)
metadata:
  type: reference
---

- Gradle needs JDK 17. In PowerShell:
  `$env:JAVA_HOME = 'C:\Tools\jdk-17\jdk-17.0.20.1+1'; $env:Path = "$env:JAVA_HOME\bin;$env:Path"`
  then `.\gradlew.bat test assembleDebug`. A full run takes a few minutes.
- The JUnit result XML files are in `*/build/test-results/test*/`. Check them to confirm the tests
  ran rather than being skipped as up to date.
- Only Python 2.7 is installed (`python`); there is no `python3` or `py`. Use the editor tools for
  file edits, or a Python 2 script working on bytes (`io.open(p, 'rb')`, b'' strings) when an edit
  must keep the line endings.
- Many sources and `readme.md` use CRLF line endings. In Git Bash, `sed -i` turns them into LF and
  the diff then covers the whole file. Check with `file <path>` after an edit.
- No PIL or ImageMagick: draw image mock-ups and resize icons with System.Drawing in PowerShell.
- The phone's wireless adb port changes between sessions, and the phone is often locked
  (screenshots then show the lock screen). Look it up with `adb devices` each time.
- `du` over `docs/helmsman` is very slow, so avoid it.
- The phone is installed over wireless adb; the commands are in the readme.
