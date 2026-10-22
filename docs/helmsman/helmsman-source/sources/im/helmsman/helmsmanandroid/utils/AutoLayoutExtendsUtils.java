package im.helmsman.helmsmanandroid.utils;

import com.zhy.autolayout.config.AutoLayoutConifg;

/* JADX INFO: loaded from: classes2.dex */
public class AutoLayoutExtendsUtils {
    public static int widthConvert(int i) {
        return (int) ((AutoLayoutConifg.getInstance().getScreenWidth() / AutoLayoutConifg.getInstance().getDesignWidth()) * i);
    }

    public static int heightConvert(int i) {
        return (int) ((AutoLayoutConifg.getInstance().getScreenHeight() / AutoLayoutConifg.getInstance().getDesignHeight()) * i);
    }
}
