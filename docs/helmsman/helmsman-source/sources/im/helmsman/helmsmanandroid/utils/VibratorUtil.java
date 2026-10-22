package im.helmsman.helmsmanandroid.utils;

import android.content.Context;
import android.os.Vibrator;
import im.helmsman.helmsmanandroid.config.Config;

/* JADX INFO: loaded from: classes2.dex */
public class VibratorUtil {
    public static void vibrator(Context context, long[] jArr) {
        vibrator(context, jArr, 1);
    }

    public static void vibrator(Context context, long[] jArr, int i) {
        if (Config.isVibration) {
            ((Vibrator) context.getSystemService("vibrator")).vibrate(jArr, i);
        }
    }
}
