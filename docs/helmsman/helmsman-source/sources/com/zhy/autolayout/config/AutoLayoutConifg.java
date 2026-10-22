package com.zhy.autolayout.config;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import com.zhy.autolayout.utils.L;
import com.zhy.autolayout.utils.ScreenUtils;

/* JADX INFO: loaded from: classes2.dex */
public class AutoLayoutConifg {
    private static final String KEY_DESIGN_HEIGHT = "design_height";
    private static final String KEY_DESIGN_WIDTH = "design_width";
    private static AutoLayoutConifg sIntance = new AutoLayoutConifg();
    private int mDesignHeight;
    private int mDesignWidth;
    private int mScreenHeight;
    private int mScreenWidth;
    private boolean useDeviceSize;

    private AutoLayoutConifg() {
    }

    public void checkParams() {
        if (this.mDesignHeight <= 0 || this.mDesignWidth <= 0) {
            throw new RuntimeException("you must set design_width and design_height  in your manifest file.");
        }
    }

    public AutoLayoutConifg useDeviceSize() {
        this.useDeviceSize = true;
        return this;
    }

    public static AutoLayoutConifg getInstance() {
        return sIntance;
    }

    public int getScreenWidth() {
        return this.mScreenWidth;
    }

    public int getScreenHeight() {
        return this.mScreenHeight;
    }

    public int getDesignWidth() {
        return this.mDesignWidth;
    }

    public int getDesignHeight() {
        return this.mDesignHeight;
    }

    public void init(Context context) {
        getMetaData(context);
        int[] screenSize = ScreenUtils.getScreenSize(context, this.useDeviceSize);
        this.mScreenWidth = screenSize[0];
        this.mScreenHeight = screenSize[1];
        L.e(" screenWidth =" + this.mScreenWidth + " ,screenHeight = " + this.mScreenHeight);
    }

    private void getMetaData(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                this.mDesignWidth = ((Integer) applicationInfo.metaData.get(KEY_DESIGN_WIDTH)).intValue();
                this.mDesignHeight = ((Integer) applicationInfo.metaData.get(KEY_DESIGN_HEIGHT)).intValue();
            }
            L.e(" designWidth =" + this.mDesignWidth + " , designHeight = " + this.mDesignHeight);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("you must set design_width and design_height  in your manifest file.", e);
        }
    }
}
