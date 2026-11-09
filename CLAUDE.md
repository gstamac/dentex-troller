# Dentex Troller

An Android app (Kotlin, Compose) that controls a Haswing GPS trolling motor over BLE and MAVLink.
See [readme.md](readme.md) for the architecture, protocol facts and build steps.

## Project memory

Persistent project memory lives in [.memory/](.memory/), which is versioned with the repo.

- At the start of every session, read [.memory/MEMORY.md](.memory/MEMORY.md), then open the
  memory files that matter for the task.
- Save new durable facts there: owner decisions, corrections to how you work, hardware test
  results, and external references. Use one fact per file, with `name`, `description` and
  `metadata.type` frontmatter, and add a one-line entry to `.memory/MEMORY.md`.
- Update an existing file rather than adding a duplicate, and delete memories that turn out to be
  wrong. Don't store what the code, the readme or git history already record.
- Use this folder instead of the per-user memory directory, so the memory travels with the repo.

## Working rules

- Safety first: a motor command can move the boat. Never weaken the command gate, never replay
  commands after a reconnect, and keep STOP always available. STOP stays on Controls and on the
  chart's motor controls; by the owner's choice (2026-11-09) the other screens don't have it.
- Run `.\gradlew.bat test` with JDK 17 (see [.memory/dev-environment.md](.memory/dev-environment.md))
  after changes to `mavlink`, `haswing`, `navigation`, `logging`, `simulator` or `ble`.
