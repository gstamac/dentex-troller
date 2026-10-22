package com.zhy.autolayout.utils;

import android.util.TypedValue;

/* JADX INFO: loaded from: classes2.dex */
public class DimenUtils {
    private static int getComplexUnit(int i) {
        return (i >> 0) & 15;
    }

    public static boolean isPxVal(TypedValue typedValue) {
        return typedValue != null && typedValue.type == 5 && getComplexUnit(typedValue.data) == 0;
    }
}
