package im.helmsman.helmsmanandroid.inet.model;

/* JADX INFO: loaded from: classes2.dex */
public class WaypointToJson {
    private String geohash;
    private int id;
    private int index;
    private float latitude;
    private float longitude;
    private int route_id;
    private String timestamp;

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public int getRoute_id() {
        return this.route_id;
    }

    public void setRoute_id(int i) {
        this.route_id = i;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int i) {
        this.index = i;
    }

    public float getLongitude() {
        return this.longitude;
    }

    public void setLongitude(float f) {
        this.longitude = f;
    }

    public float getLatitude() {
        return this.latitude;
    }

    public void setLatitude(float f) {
        this.latitude = f;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String str) {
        this.timestamp = str;
    }

    public String getGeohash() {
        return this.geohash;
    }

    public void setGeohash(String str) {
        this.geohash = str;
    }

    public String toString() {
        return "WaypointToJson{id=" + this.id + ", route_id=" + this.route_id + ", index=" + this.index + ", longitude=" + this.longitude + ", latitude=" + this.latitude + ", timestamp='" + this.timestamp + "', geohash='" + this.geohash + "'}";
    }
}
