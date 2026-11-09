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
- The chart screen has no bottom menu. A three-dot button under the route button opens the Dashboard. The other screens keep the bottom menu, but without a Chart entry (removed
  2026-11-09); an X at the top right, beside STOP, closes them back to the chart.
- The track record button is a dashed winding trail ending in a red dot; while recording it is red
  with a solid trail and a stop square. The owner asked for no blinking (2026-11-09).
- The Route screen was removed on 2026-11-06, because routes are edited on the map. The owner doesn't want route upload or download back: following a route uploads it.
- Follow route starts at the tapped waypoint, or at the first point of a tapped section. The owner rejected distance-based joining twice, so don't bring it back.

**Why:** the owner uses it one-handed on a boat. Controls that move or vanish between modes, and popups, get in the way.

**How to apply:** for new chart controls, use icons, keep the button positions fixed, disable rather than hide, and add no confirmation steps. The STOP-always rule from CLAUDE.md still applies: the stop is never disabled.
