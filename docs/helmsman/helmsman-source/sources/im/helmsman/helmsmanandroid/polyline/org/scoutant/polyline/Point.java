package im.helmsman.helmsmanandroid.polyline.org.scoutant.polyline;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class Point implements Serializable {
    private static final long serialVersionUID = 1;
    private final double lat;
    private final double lng;

    public Point(double d, double d2) {
        this.lat = d;
        this.lng = d2;
    }

    public double getLat() {
        return this.lat;
    }

    public double getLng() {
        return this.lng;
    }

    public String toString() {
        return "(" + this.lat + ", " + this.lng + ")";
    }

    public static String toGeoJSON(List<Point> list) {
        StringBuilder sb = new StringBuilder("[");
        Iterator<Point> it = list.iterator();
        while (it.hasNext()) {
            sb.append(toGeoJSON(it.next()));
            if (it.hasNext()) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static String toGeoJSON(Point point) {
        return "[" + point.getLng() + "," + point.getLat() + "]";
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Point)) {
            return false;
        }
        Point point = (Point) obj;
        return Math.abs(point.getLat() - this.lat) <= 0.001d && Math.abs(point.getLng() - this.lng) <= 0.001d;
    }

    public int hashCode() {
        return ((185 + ((int) (Double.doubleToLongBits(this.lat) ^ (Double.doubleToLongBits(this.lat) >>> 32)))) * 37) + ((int) (Double.doubleToLongBits(this.lng) ^ (Double.doubleToLongBits(this.lng) >>> 32)));
    }
}
