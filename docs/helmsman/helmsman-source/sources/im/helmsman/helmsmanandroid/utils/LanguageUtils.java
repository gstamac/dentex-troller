package im.helmsman.helmsmanandroid.utils;

import im.helmsman.helmsmanandroid.MyApplication;

/* JADX INFO: loaded from: classes2.dex */
public class LanguageUtils {
    public static boolean isZh() {
        return MyApplication.getInstance().getResources().getConfiguration().locale.getLanguage().endsWith("zh");
    }
}
