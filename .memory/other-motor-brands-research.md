---
name: other-motor-brands-research
description: 2026-11-08 research on controlling Minn Kota and MotorGuide GPS trolling motors from the app; no public API, ranked options
metadata:
  type: reference
---

On 2026-11-08 the owner asked whether the app could also control Minn Kota and MotorGuide GPS motors.
Web research found no public API, protocol spec or open-source reverse engineering for either brand.

- **Minn Kota Bluetooth i-Pilot / i-Pilot Link (2017–2023): most feasible.** The official i-Pilot Link
  phone app controls speed, steering, Spot-Lock and AutoPilot over Bluetooth, so its protocol could be
  learned from the Android app, as Haswing's was from Helmsman ([[helmsman-source-as-reference]]).
  A reviewer says app pairing disturbs phone calls, which hints at classic Bluetooth rather than BLE
  (unconfirmed).
- **Minn Kota QUEST / Advanced GPS Navigation (2023+): possible, riskier.** The One-Boat Network /
  Minn Kota Fishing app connects over Bluetooth ("Minn Kota Controller 4.0"); Wi-Fi is for updates only.
  The app needs an account sign-in, so authentication is likely. The controller pairs with one phone.
- **MotorGuide Xi3 / Xi5 / Tour Pro (Pinpoint GPS): hardest.** No phone app; the remote uses
  undocumented 2.4 GHz RF. Chartplotter control goes over NMEA 2000 with proprietary PGNs. Xi3/Xi5 need
  the Pinpoint GPS Gateway Kit; the Tour Pro connects directly. It would need an N2K gateway (WiFi/BLE,
  or ESP32 + CAN) plus captures from a Lowrance display controlling the motor.

Any brand needs a common motor interface first, as `haswing` is Haswing-specific. The command gate,
no replay after a reconnect, and STOP must be rebuilt per protocol, with a simulator and field tests.
The owner hasn't said which motors they can test with; nothing was started.

**How to apply:** if the owner comes back to this, start from the ranking above and ask which motor
they have access to. Without a real motor the work stays at research.
