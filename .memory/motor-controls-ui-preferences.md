---
name: motor-controls-ui-preferences
description: How the owner wants the chart's motor controls and go-tos to look and behave (no dialogs, no panels, icons always shown)
metadata:
  type: feedback
---

These preferences come from the owner's requests on 2026-11-06:

- Motor controls are round icon buttons floating over the chart, not a panel and not text.
- Buttons stay visible in every mode and are only disabled where they can't be used. The owner asked for this for speed, turn, anchor and navigate.
- The action order is anchor, then start/stop, then navigate.
- A go-to starts as soon as navigate is pressed. There is no confirmation dialog and no go-to info panel. A failure is shown in a snackbar.
- There are no "Move boat here" or "Follow route" buttons in the info sheet. The navigate button takes the selection, or continues a go-to that anchoring interrupted.
- The selection (pin, route) must survive starting, stopping and anchoring a go-to.
- The top-right badge only shows status and doesn't stop the motor. Manual is drawn as a propeller.
- The chart screen has no bottom menu. A three-dot button under the track button opens Settings, the first tab (2026-11-09). The other screens keep the bottom menu, but without a Chart entry (removed
  2026-11-09). They slide in over the chart from the right, like Google Maps settings, and an X at
  the top right (or Back) slides them away (an X, not a back arrow: Google Maps settings use an X too, owner 2026-11-10); the chart stays alive underneath.
- Connect is a popup, not a screen (2026-11-09): the chart's status badge opens it, and so does
  Change next to the saved remote on Settings. It closes itself once a new link is ready.
- STOP is only on Controls and on the chart's motor controls. On 2026-11-09 the owner had the STOP bar
  removed from Connect, Dashboard, Log and Settings, after being told it goes against "STOP always
  available". Don't put it back on those screens; never remove it from Controls or the chart.
- The track record button is a dashed winding trail ending in a red dot; while recording it is red
  with a solid trail and a stop square. The owner asked for no blinking (2026-11-09).
- The Route screen was removed on 2026-11-06, because routes are edited on the map. The owner doesn't want route upload or download back: following a route uploads it.
- Follow route starts at the tapped waypoint, or at the first point of a tapped section. The owner rejected distance-based joining twice, so don't bring it back.

- Landscape (owner's design, 2026-11-11): motor controls bottom left, both groups side by side;
  the info sheet along the bottom to their right, allowed to cover the right-hand buttons when
  pulled up. Portrait and route mode keep the full-width layout.
- App icon: the owner tried docs/icon2.png as the launcher icon on 2026-11-10 and had it reverted;
  keep the current launcher icon unless asked.

**Why:** the owner uses it one-handed on a boat. Controls that move or vanish between modes, and popups, get in the way.

**How to apply:** for new chart controls, use icons, keep the button positions fixed, disable rather than hide, and add no confirmation steps. The STOP-always rule from CLAUDE.md still applies: the stop is never disabled.
