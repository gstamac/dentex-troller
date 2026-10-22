package im.helmsman.helmsmanandroid.inet.model;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes2.dex */
public class UploadRouteJson<UploadWaypointJson> {
    private String name;
    private ArrayList<UploadWaypointJson> waypoints;

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public ArrayList<UploadWaypointJson> getWaypoints() {
        return this.waypoints;
    }

    public void setWaypoints(ArrayList<UploadWaypointJson> arrayList) {
        this.waypoints = arrayList;
    }

    public String toString() {
        return "UploadRouteJson{name='" + this.name + "', waypoints=" + this.waypoints + '}';
    }
}
