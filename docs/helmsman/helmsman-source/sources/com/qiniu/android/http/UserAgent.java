package com.qiniu.android.http;

import android.os.Build;
import android.text.TextUtils;
import com.autonavi.ae.gmap.utils.GLMapStaticValue;
import com.qiniu.android.common.Constants;
import com.qiniu.android.utils.StringUtils;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import org.bouncycastle.i18n.LocalizedMessage;

/* JADX INFO: loaded from: classes2.dex */
public final class UserAgent {
    private static UserAgent _instance = new UserAgent();
    public final String id;
    public final String ua;

    private UserAgent() {
        String strGenId = genId();
        this.id = strGenId;
        this.ua = getUserAgent(strGenId);
    }

    public static UserAgent instance() {
        return _instance;
    }

    private static String genId() {
        return System.currentTimeMillis() + "" + new Random().nextInt(GLMapStaticValue.MAPRENDER_CAN_STOP_AND_FULLSCREEN_RENDEROVER);
    }

    static String getUserAgent(String str) {
        return String.format("QiniuAndroid/%s (%s; %s; %s", Constants.VERSION, osVersion(), device(), str);
    }

    private static String osVersion() {
        try {
            String str = Build.VERSION.RELEASE;
            return str == null ? "-" : StringUtils.strip(str.trim());
        } catch (Throwable unused) {
            return "-";
        }
    }

    private static String device() {
        try {
            String strTrim = Build.MODEL.trim();
            String strDeviceName = deviceName(Build.MANUFACTURER.trim(), strTrim);
            if (TextUtils.isEmpty(strDeviceName)) {
                strDeviceName = deviceName(Build.BRAND.trim(), strTrim);
            }
            StringBuilder sb = new StringBuilder();
            if (strDeviceName == null) {
                strDeviceName = "-";
            }
            return StringUtils.strip(sb.append(strDeviceName).append("-").append(strTrim).toString());
        } catch (Throwable unused) {
            return "-";
        }
    }

    private static String deviceName(String str, String str2) {
        String lowerCase = str.toLowerCase(Locale.getDefault());
        if (lowerCase.startsWith("unknown") || lowerCase.startsWith("alps") || lowerCase.startsWith(com.facebook.appevents.codeless.internal.Constants.PLATFORM) || lowerCase.startsWith("sprd") || lowerCase.startsWith("spreadtrum") || lowerCase.startsWith("rockchip") || lowerCase.startsWith("wondermedia") || lowerCase.startsWith("mtk") || lowerCase.startsWith("mt65") || lowerCase.startsWith("nvidia") || lowerCase.startsWith("brcm") || lowerCase.startsWith("marvell") || str2.toLowerCase(Locale.getDefault()).contains(lowerCase)) {
            return null;
        }
        return str;
    }

    public String getUa(String str) {
        String strTrim = ("" + str).trim();
        return new String((this.ua + "; " + strTrim.substring(0, Math.min(16, strTrim.length())) + ")").getBytes(Charset.forName(LocalizedMessage.DEFAULT_ENCODING)));
    }
}
