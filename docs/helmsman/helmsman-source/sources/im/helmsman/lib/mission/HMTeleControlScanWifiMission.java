package im.helmsman.lib.mission;

import im.helmsman.lib.mission.common.HMMission;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class HMTeleControlScanWifiMission extends HMMission {
    private List<String> ssids;

    public HMTeleControlScanWifiMission() {
        super(HMMission.MissionType.TELECONTROL_SCANWIFI_TYPE);
        this.ssids = new ArrayList();
    }

    public int getWayPointCount() {
        return this.ssids.size();
    }

    public void addSsid(String str) {
        if (this.ssids.contains(str)) {
            return;
        }
        this.ssids.add(str);
    }

    public List<String> getHmWayPoints() {
        return this.ssids;
    }
}
