package im.helmsman.lib.util;

/* JADX INFO: loaded from: classes2.dex */
public class PositionUtils {
    public static int floatToInt(float f) {
        return (int) (f * (f >= 100.0f ? 100000.0f : 1000000.0f));
    }
}
