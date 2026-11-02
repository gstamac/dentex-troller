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
- Only Python 2.7 is installed (`python`); there is no `python3` or `py`. Use the editor tools or
  shell tools for file edits, not Python scripts.
- `du` over `docs/helmsman` is very slow, so avoid it.
- The phone is installed over wireless adb; the commands are in the readme.
