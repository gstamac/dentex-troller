package im.helmsman.lib.mission;

import im.helmsman.lib.mission.common.HMMission;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class HMWayPointMission extends HMMission {
    private List<HMWayPoint> hmWayPoints;

    public HMWayPointMission() {
        super(HMMission.MissionType.WAYPOINT_TYPE);
        this.hmWayPoints = new ArrayList();
    }

    public void addWayPoint(HMWayPoint hMWayPoint) {
        this.hmWayPoints.add(hMWayPoint);
    }

    public void addWayPoints(List<HMWayPoint> list) {
        if (list != null) {
            this.hmWayPoints.addAll(list);
        }
    }

    public int getWayPointCount() {
        return this.hmWayPoints.size();
    }

    public void removeAllWayPoint() {
        this.hmWayPoints.clear();
    }

    public void removeWayPoint(HMWayPoint hMWayPoint) {
        this.hmWayPoints.remove(hMWayPoint);
    }

    public List<HMWayPoint> getHmWayPoints() {
        return this.hmWayPoints;
    }
}
