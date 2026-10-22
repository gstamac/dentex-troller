package com.amap.api.mapcore.util;

import android.text.TextUtils;
import com.google.android.gms.common.internal.ImagesContract;

/* JADX INFO: compiled from: StyleItemAdaptor.java */
/* JADX INFO: loaded from: classes.dex */
public class cz {
    public static final int[][] a = {new int[]{0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}, new int[]{12}, new int[]{1}, new int[]{13}, new int[]{14}, new int[]{15, 16}, new int[]{17, 18, 19, 20, 21, 26, 27, 28}, new int[]{22, 23}, new int[]{24, 25}, new int[]{39, 40, 41}, new int[]{29, 30, 31}, new int[]{32, 33, 34, 35, 36, 37, 38}};
    public static final String[] b = {"land", "water", "green", "building", "highway", "arterial", ImagesContract.LOCAL, "railway", "subway", "boundary", "poilabel", "districtlable"};

    public static int[] a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        int i = 0;
        while (true) {
            String[] strArr = b;
            if (i >= strArr.length) {
                i = -1;
                break;
            }
            if (strArr[i].equals(str)) {
                break;
            }
            i++;
        }
        if (i >= 0) {
            return a[i];
        }
        return null;
    }
}
