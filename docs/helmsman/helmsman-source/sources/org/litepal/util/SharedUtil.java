package org.litepal.util;

import android.content.SharedPreferences;
import android.text.TextUtils;
import org.litepal.LitePalApplication;

/* JADX INFO: loaded from: classes2.dex */
public class SharedUtil {
    private static final String LITEPAL_PREPS = "litepal_prefs";
    private static final String VERSION = "litepal_version";

    public static void updateVersion(String str, int i) {
        SharedPreferences.Editor editorEdit = LitePalApplication.getContext().getSharedPreferences(LITEPAL_PREPS, 0).edit();
        if (TextUtils.isEmpty(str)) {
            editorEdit.putInt(VERSION, i);
        } else {
            if (str.endsWith(Const.Config.DB_NAME_SUFFIX)) {
                str = str.replace(Const.Config.DB_NAME_SUFFIX, "");
            }
            editorEdit.putInt("litepal_version_" + str, i);
        }
        editorEdit.apply();
    }

    public static int getLastVersion(String str) {
        SharedPreferences sharedPreferences = LitePalApplication.getContext().getSharedPreferences(LITEPAL_PREPS, 0);
        if (TextUtils.isEmpty(str)) {
            return sharedPreferences.getInt(VERSION, 0);
        }
        if (str.endsWith(Const.Config.DB_NAME_SUFFIX)) {
            str = str.replace(Const.Config.DB_NAME_SUFFIX, "");
        }
        return sharedPreferences.getInt("litepal_version_" + str, 0);
    }

    public static void removeVersion(String str) {
        SharedPreferences.Editor editorEdit = LitePalApplication.getContext().getSharedPreferences(LITEPAL_PREPS, 0).edit();
        if (TextUtils.isEmpty(str)) {
            editorEdit.remove(VERSION);
        } else {
            if (str.endsWith(Const.Config.DB_NAME_SUFFIX)) {
                str = str.replace(Const.Config.DB_NAME_SUFFIX, "");
            }
            editorEdit.remove("litepal_version_" + str);
        }
        editorEdit.apply();
    }
}
