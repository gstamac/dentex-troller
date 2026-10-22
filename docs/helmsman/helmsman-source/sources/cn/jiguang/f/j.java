package cn.jiguang.f;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.facebook.places.model.PlaceFields;
import java.lang.reflect.InvocationTargetException;

/* JADX INFO: loaded from: classes.dex */
public final class j {
    public static int a(Context context) {
        String strC = c(context);
        if (TextUtils.isEmpty(strC)) {
            return 0;
        }
        if ("wifi".equals(strC)) {
            return 1;
        }
        if ("2g".equals(strC)) {
            return 2;
        }
        if ("3g".equals(strC)) {
            return 3;
        }
        if ("4g".equals(strC)) {
            return 4;
        }
        return "5g".equals(strC) ? 5 : 0;
    }

    public static String a(Context context, int i) {
        String strC = c(context);
        cn.jiguang.ad.d.a("TeleonyManagerUtils", "getCurrentNetType - type:" + strC);
        if (TextUtils.isEmpty(strC)) {
            try {
                Object objA = h.a((Class<?>) TelephonyManager.class, "getNetworkClass", new Object[]{Integer.valueOf(i)}, new Class[]{Integer.TYPE});
                if (((Integer) objA).intValue() == 0) {
                    strC = "unknown";
                } else if (((Integer) objA).intValue() == 1) {
                    strC = "2g";
                } else if (((Integer) objA).intValue() == 2) {
                    strC = "3g";
                } else if (((Integer) objA).intValue() == 3) {
                    strC = "4g";
                }
                cn.jiguang.ad.d.b("TeleonyManagerUtils", "step2 - type:" + strC);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | Exception unused) {
            }
        }
        return TextUtils.isEmpty(strC) ? "unknown" : strC;
    }

    public static String b(Context context) {
        try {
            return ((TelephonyManager) context.getSystemService(PlaceFields.PHONE)).getNetworkOperator();
        } catch (Exception unused) {
            return "";
        }
    }

    /* JADX WARN: Code duplicated, block: B:15:0x0045  */
    /* JADX WARN: Code duplicated, block: B:16:0x0047  */
    /* JADX WARN: Code duplicated, block: B:17:0x0049  */
    public static String c(Context context) {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo == null) {
                return "unknown";
            }
            if (activeNetworkInfo.getType() == 1) {
                return "wifi";
            }
            if (activeNetworkInfo.getType() != 0) {
                return "unknown";
            }
            int subtype = activeNetworkInfo.getSubtype();
            cn.jiguang.ad.d.a("TeleonyManagerUtils", "getNetworkClass networkType:" + subtype);
            switch (subtype) {
                case 1:
                case 2:
                case 4:
                case 7:
                case 11:
                    return "2g";
                case 3:
                case 5:
                case 6:
                case 8:
                case 9:
                case 10:
                case 12:
                case 14:
                case 15:
                    return "3g";
                case 13:
                    return "4g";
                default:
                    switch (subtype) {
                        case 16:
                            return "2g";
                        case 17:
                            return "3g";
                        case 18:
                        case 19:
                            return "4g";
                        case 20:
                            return "5g";
                        default:
                            return "unknown";
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "unknown";
        }
    }
}
