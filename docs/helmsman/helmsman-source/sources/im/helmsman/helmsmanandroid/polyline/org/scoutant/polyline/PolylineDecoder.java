package im.helmsman.helmsmanandroid.polyline.org.scoutant.polyline;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class PolylineDecoder {
    private static final double DEFAULT_PRECISION = 100000.0d;

    public List<Point> decode(String str) {
        return decode(str, DEFAULT_PRECISION);
    }

    public List<Point> decode(String str, double d) {
        int i;
        int i2;
        ArrayList arrayList = new ArrayList();
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        while (i3 < str.length()) {
            int i6 = 0;
            int i7 = 0;
            while (true) {
                i = i3 + 1;
                int iCharAt = str.charAt(i3) - '?';
                i6 |= (iCharAt & 31) << i7;
                i7 += 5;
                if (iCharAt < 32) {
                    break;
                }
                i3 = i;
            }
            int i8 = ((i6 & 1) != 0 ? ~(i6 >> 1) : i6 >> 1) + i4;
            int i9 = 0;
            int i10 = 0;
            while (true) {
                i2 = i + 1;
                int iCharAt2 = str.charAt(i) - '?';
                i9 |= (iCharAt2 & 31) << i10;
                i10 += 5;
                if (iCharAt2 < 32) {
                    break;
                }
                i = i2;
            }
            int i11 = i9 & 1;
            int i12 = i9 >> 1;
            if (i11 != 0) {
                i12 = ~i12;
            }
            i5 += i12;
            arrayList.add(new Point(((double) i8) / d, ((double) i5) / d));
            i4 = i8;
            i3 = i2;
        }
        return arrayList;
    }
}
