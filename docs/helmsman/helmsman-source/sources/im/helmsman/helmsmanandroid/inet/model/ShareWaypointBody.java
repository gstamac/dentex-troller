package im.helmsman.helmsmanandroid.inet.model;

import im.helmsman.helmsmanandroid.dao.WayPoint;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class ShareWaypointBody {
    private String description;
    private String identifier;
    private String name;
    private List<WayPoint> waypoints;

    public ShareWaypointBody(String str, String str2, List<WayPoint> list) {
        this.description = str2;
        this.name = str;
        this.waypoints = list;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public List<WayPoint> getWaypoints() {
        return this.waypoints;
    }

    public void setWaypoints(List<WayPoint> list) {
        this.waypoints = list;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String str) {
        this.identifier = str;
    }
}
