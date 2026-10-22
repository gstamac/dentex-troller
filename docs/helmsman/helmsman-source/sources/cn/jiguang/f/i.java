package cn.jiguang.f;

import android.text.TextUtils;
import com.facebook.appevents.AppEventsConstants;
import com.qiniu.android.common.Constants;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Pattern;
import kotlin.UByte;

/* JADX INFO: loaded from: classes.dex */
public final class i {
    private static Pattern a = Pattern.compile("((2[0-4]\\d|25[0-5]|[01]?\\d{1,2})\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d{1,2})");
    private static SimpleDateFormat b = new SimpleDateFormat("HHH:mm:ss:SSS", Locale.ENGLISH);

    public static String a(String str, int i) {
        int length = str.length();
        if (length >= i) {
            return str;
        }
        for (int i2 = 0; i2 < i - length; i2++) {
            str = str + " ";
        }
        return str;
    }

    public static String a(byte[] bArr) {
        try {
            byte[] bArrDigest = MessageDigest.getInstance("SHA1").digest(bArr);
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b2 : bArrDigest) {
                int i = b2 & UByte.MAX_VALUE;
                if (i < 16) {
                    stringBuffer.append(AppEventsConstants.EVENT_PARAM_VALUE_NO);
                }
                stringBuffer.append(Integer.toHexString(i));
            }
            return stringBuffer.toString();
        } catch (Throwable unused) {
            cn.jiguang.ad.d.b("StringUtils", "Get SHA1 error");
            return "";
        }
    }

    public static boolean a(String str) {
        return str == null || str.length() == 0 || str.trim().length() == 0;
    }

    public static boolean a(String str, String str2) {
        if (str.length() < str2.length() + 0) {
            return false;
        }
        for (int i = 0; i < str2.length(); i++) {
            char cCharAt = str.charAt(i + 0);
            char cCharAt2 = str2.charAt(i);
            if (cCharAt != cCharAt2 && Character.toLowerCase(cCharAt) != Character.toLowerCase(cCharAt2)) {
                return false;
            }
        }
        return true;
    }

    public static String b(String str) {
        return a(str) ? "" : Pattern.compile("[^\\w#$@\\-一-龥]+").matcher(str).replaceAll("");
    }

    public static byte[] c(String str) {
        try {
            char[] charArray = str.toCharArray();
            byte[] bArr = new byte[charArray.length];
            for (int i = 0; i < charArray.length; i++) {
                bArr[i] = (byte) charArray[i];
            }
            return MessageDigest.getInstance("MD5").digest(bArr);
        } catch (Exception unused) {
            return null;
        }
    }

    public static String d(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            char[] charArray = str.toCharArray();
            byte[] bArr = new byte[charArray.length];
            for (int i = 0; i < charArray.length; i++) {
                bArr[i] = (byte) charArray[i];
            }
            byte[] bArrDigest = messageDigest.digest(bArr);
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b2 : bArrDigest) {
                int i2 = b2 & UByte.MAX_VALUE;
                if (i2 < 16) {
                    stringBuffer.append(AppEventsConstants.EVENT_PARAM_VALUE_NO);
                }
                stringBuffer.append(Integer.toHexString(i2));
            }
            return stringBuffer.toString();
        } catch (Exception unused) {
            return "";
        }
    }

    public static String e(String str) {
        try {
            byte[] bArrDigest = MessageDigest.getInstance("MD5").digest(str.getBytes(Constants.UTF_8));
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b2 : bArrDigest) {
                int i = b2 & UByte.MAX_VALUE;
                if (i < 16) {
                    stringBuffer.append(AppEventsConstants.EVENT_PARAM_VALUE_NO);
                }
                stringBuffer.append(Integer.toHexString(i));
            }
            return stringBuffer.toString();
        } catch (Throwable unused) {
            cn.jiguang.ad.d.b("StringUtils", "Get MD5 error");
            return "";
        }
    }

    public static boolean f(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        int iIndexOf = str.indexOf(":");
        if (iIndexOf >= 0) {
            if (iIndexOf != str.lastIndexOf(":")) {
                return false;
            }
            str = str.substring(0, iIndexOf);
        }
        return a.matcher(str).matches();
    }

    public static boolean g(String str) {
        int iIndexOf;
        return (TextUtils.isEmpty(str) || (iIndexOf = str.indexOf(":")) == -1 || str.lastIndexOf(":") == iIndexOf) ? false : true;
    }

    public static boolean h(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        try {
            return Pattern.compile("[\\x20-\\x7E]+").matcher(str).matches();
        } catch (Throwable unused) {
            return true;
        }
    }

    public static byte[] i(String str) {
        if (TextUtils.isEmpty(str)) {
            return new byte[0];
        }
        try {
            return str.getBytes("UTF-8");
        } catch (Throwable th) {
            cn.jiguang.ad.d.i("StringUtils", "stringToUtf8Bytes error:" + th.getMessage());
            return str.getBytes();
        }
    }

    public static String j(String str) {
        if (str != null && !"".equals(str)) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.update(str.getBytes());
                byte[] bArrDigest = messageDigest.digest();
                if (bArrDigest == null) {
                    return "";
                }
                StringBuffer stringBuffer = new StringBuffer(bArrDigest.length * 2);
                for (byte b2 : bArrDigest) {
                    stringBuffer.append("0123456789ABCDEF".charAt((b2 >> 4) & 15)).append("0123456789ABCDEF".charAt(b2 & 15));
                }
                return stringBuffer.toString();
            } catch (NoSuchAlgorithmException unused) {
            }
        }
        return null;
    }

    public static String k(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        try {
            return a(str.getBytes(Constants.UTF_8));
        } catch (Throwable th) {
            th.printStackTrace();
            return str;
        }
    }
}
