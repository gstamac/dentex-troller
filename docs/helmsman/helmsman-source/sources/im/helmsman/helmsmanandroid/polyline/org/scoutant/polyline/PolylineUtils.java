package im.helmsman.helmsmanandroid.polyline.org.scoutant.polyline;

import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class PolylineUtils {
    public static String toString(List<Point> list) {
        Iterator<Point> it = list.iterator();
        String str = "[ ";
        while (it.hasNext()) {
            str = str + it.next();
        }
        return str + " ]";
    }

    public static String toMarkers(List<Point> list) {
        String str = "";
        for (Point point : list) {
            str = str + "|" + point.getLat() + "," + point.getLng();
        }
        return str.substring(1, str.length());
    }
}
