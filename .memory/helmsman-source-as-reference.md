---
name: helmsman-source-as-reference
description: Where in the decompiled Helmsman source the protocol behaviour this app copies is defined
metadata:
  type: reference
---

Protocol decisions are checked against the decompiled vendor app in
`docs/helmsman/helmsman-source/sources/im/helmsman/`:

- `lib/product/HMMotor50736.java`: the type 110 motor profile. Its `IsConnectedTimer` treats the
  motor as offline after 5000 ms without a message other than msgid 1 (SYS_STATUS). The app's
  `LINK_TIMEOUT_MILLIS` copies this.
- `lib/mission/HMMissionManager.java`: route upload and download. It stops the MISSION_COUNT retry
  as soon as the first MISSION_REQUEST arrives and fails the upload once the retries run out. It
  polls the route request every 150 ms with a 25 s timeout.
- `helmsmanandroid/controller/BaseIoController.java`: the steering repeat every 66 ms and the
  remote-controller heartbeat.
- `helmsmanandroid/presenter/MainPresenter.java`: the foreground-only heartbeat timer.

Search these with Grep before you assume ordinary MAVLink semantics. See [[repo-private-vendor-files]].
