package com.zhy.autolayout.utils;

import android.view.View;
import com.zhy.autolayout.AutoLayoutInfo;
import com.zhy.autolayout.R;
import com.zhy.autolayout.config.AutoLayoutConifg;

/* JADX INFO: loaded from: classes2.dex */
public class AutoUtils {
    public static void auto(View view) {
        autoSize(view);
        autoPadding(view);
        autoMargin(view);
        autoTextSize(view, 3);
    }

    public static void auto(View view, int i, int i2) {
        AutoLayoutInfo attrFromView = AutoLayoutInfo.getAttrFromView(view, i, i2);
        if (attrFromView != null) {
            attrFromView.fillAttrs(view);
        }
    }

    public static void autoTextSize(View view) {
        auto(view, 4, 3);
    }

    public static void autoTextSize(View view, int i) {
        auto(view, 4, i);
    }

    public static void autoMargin(View view) {
        auto(view, 16, 3);
    }

    public static void autoMargin(View view, int i) {
        auto(view, 16, i);
    }

    public static void autoPadding(View view) {
        auto(view, 8, 3);
    }

    public static void autoPadding(View view, int i) {
        auto(view, 8, i);
    }

    public static void autoSize(View view) {
        auto(view, 3, 3);
    }

    public static void autoSize(View view, int i) {
        auto(view, 3, i);
    }

    public static boolean autoed(View view) {
        if (view.getTag(R.id.id_tag_autolayout_size) != null) {
            return true;
        }
        view.setTag(R.id.id_tag_autolayout_size, "Just Identify");
        return false;
    }

    public static float getPercentWidth1px() {
        return (AutoLayoutConifg.getInstance().getScreenWidth() * 1.0f) / AutoLayoutConifg.getInstance().getDesignWidth();
    }

    public static float getPercentHeight1px() {
        return (AutoLayoutConifg.getInstance().getScreenHeight() * 1.0f) / AutoLayoutConifg.getInstance().getDesignHeight();
    }

    public static int getPercentWidthSize(int i) {
        return (int) (((i * 1.0f) / AutoLayoutConifg.getInstance().getDesignWidth()) * AutoLayoutConifg.getInstance().getScreenWidth());
    }

    public static int getPercentWidthSizeBigger(int i) {
        int screenWidth = AutoLayoutConifg.getInstance().getScreenWidth();
        int designWidth = AutoLayoutConifg.getInstance().getDesignWidth();
        int i2 = i * screenWidth;
        if (i2 % designWidth == 0) {
            return i2 / designWidth;
        }
        return (i2 / designWidth) + 1;
    }

    public static int getPercentHeightSizeBigger(int i) {
        int screenHeight = AutoLayoutConifg.getInstance().getScreenHeight();
        int designHeight = AutoLayoutConifg.getInstance().getDesignHeight();
        int i2 = i * screenHeight;
        if (i2 % designHeight == 0) {
            return i2 / designHeight;
        }
        return (i2 / designHeight) + 1;
    }

    public static int getPercentHeightSize(int i) {
        return (int) (((i * 1.0f) / AutoLayoutConifg.getInstance().getDesignHeight()) * AutoLayoutConifg.getInstance().getScreenHeight());
    }
}
