package com.zendesk.util;

import android.graphics.Color;
import com.zendesk.logger.Logger;

/* JADX INFO: loaded from: classes2.dex */
public class ColorUtils {
    public static final String LOG_TAG = "ColorUtils";

    private ColorUtils() {
    }

    public static Integer apiColorToAndroidColor(String str) {
        if (StringUtils.isEmpty(str)) {
            Logger.e(LOG_TAG, "The supplied hex value is null or empty, returning null", new Object[0]);
            return null;
        }
        if (!str.startsWith("#")) {
            str = "#" + str;
        }
        if (str.length() != 7) {
            Logger.e(LOG_TAG, "The hex value is malformed, returning null for input: " + str, new Object[0]);
            return null;
        }
        try {
            return Integer.valueOf(Color.parseColor(str));
        } catch (IllegalArgumentException e) {
            Logger.e(LOG_TAG, e.getMessage(), e, new Object[0]);
            return null;
        }
    }

    public static java.awt.Color apiColorToJavaColor(String str) {
        if (StringUtils.isEmpty(str)) {
            Logger.e(LOG_TAG, "The supplied hex value is null or empty, returning null", new Object[0]);
            return null;
        }
        if (!str.startsWith("#")) {
            str = "#" + str;
        }
        if (str.length() != 7) {
            Logger.e(LOG_TAG, "The hex value is malformed, returning null for input: " + str, new Object[0]);
            return null;
        }
        try {
            return java.awt.Color.decode(str);
        } catch (NumberFormatException e) {
            Logger.e(LOG_TAG, e.getMessage(), e, new Object[0]);
            return null;
        }
    }
}
