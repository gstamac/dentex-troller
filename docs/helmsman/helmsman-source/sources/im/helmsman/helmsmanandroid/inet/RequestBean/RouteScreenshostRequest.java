package im.helmsman.helmsmanandroid.inet.RequestBean;

import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class RouteScreenshostRequest {
    private String encoded_polyline;
    private List<WaypointsBean> waypoints;

    public String getEncoded_polyline() {
        return this.encoded_polyline;
    }

    public void setEncoded_polyline(String str) {
        this.encoded_polyline = str;
    }

    public List<WaypointsBean> getWaypoints() {
        return this.waypoints;
    }

    public void setWaypoints(List<WaypointsBean> list) {
        this.waypoints = list;
    }

    public static class WaypointsBean {
        private double latitude;
        private double longitude;
        private int sequence;

        public int getSequence() {
            return this.sequence;
        }

        public void setSequence(int i) {
            this.sequence = i;
        }

        public double getLatitude() {
            return this.latitude;
        }

        public void setLatitude(double d) {
            this.latitude = d;
        }

        public double getLongitude() {
            return this.longitude;
        }

        public void setLongitude(double d) {
            this.longitude = d;
        }
    }
}
