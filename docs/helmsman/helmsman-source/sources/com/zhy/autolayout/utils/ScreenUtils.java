package com.zhy.autolayout.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import com.facebook.appevents.codeless.internal.Constants;

/* JADX INFO: loaded from: classes2.dex */
public class ScreenUtils {
    public static int getStatusBarHeight(Context context) {
        try {
            int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", Constants.PLATFORM);
            if (identifier > 0) {
                return context.getResources().getDimensionPixelSize(identifier);
            }
            return 0;
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int[] getScreenSize(Context context, boolean z) {
        int[] iArr = new int[2];
        Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        int i = displayMetrics.widthPixels;
        int i2 = displayMetrics.heightPixels;
        if (!z) {
            iArr[0] = i;
            iArr[1] = i2 - getStatusBarHeight(context);
            return iArr;
        }
        try {
            Point point = new Point();
            Display.class.getMethod("getRealSize", Point.class).invoke(defaultDisplay, point);
            i = point.x;
            i2 = point.y;
        } catch (Exception unused) {
        }
        iArr[0] = i;
        iArr[1] = i2;
        return iArr;
    }
}
