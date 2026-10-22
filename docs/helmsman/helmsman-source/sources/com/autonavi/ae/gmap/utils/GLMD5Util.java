package com.autonavi.ae.gmap.utils;

import com.facebook.appevents.AppEventsConstants;
import java.security.MessageDigest;
import kotlin.UByte;

/* JADX INFO: loaded from: classes.dex */
public class GLMD5Util {
    public static String getStringMD5(String str) {
        return getCharArrayMD5(str.toCharArray());
    }

    public static String getCharArrayMD5(char[] cArr) {
        byte[] bArr = new byte[cArr.length];
        for (int i = 0; i < cArr.length; i++) {
            bArr[i] = (byte) cArr[i];
        }
        return getByteArrayMD5(bArr);
    }

    public static String getByteArrayMD5(byte[] bArr) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(bArr);
            byte[] bArrDigest = messageDigest.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : bArrDigest) {
                int i = b & UByte.MAX_VALUE;
                if (i < 16) {
                    stringBuffer.append(AppEventsConstants.EVENT_PARAM_VALUE_NO);
                }
                stringBuffer.append(Integer.toHexString(i));
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
