package cn.jiguang.ac;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.os.Bundle;
import android.text.TextUtils;
import cn.jiguang.internal.JConstants;
import cn.jpush.android.service.JCommonService;
import com.facebook.share.internal.ShareConstants;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class c {
    private static final Object a = new Object();
    private static volatile c b;
    private static String c;
    private static String d;

    public static c a() {
        if (b == null) {
            synchronized (a) {
                if (b == null) {
                    b = new c();
                }
            }
        }
        return b;
    }

    public static String a(Context context) {
        String str = d;
        if (str != null) {
            return str;
        }
        String strB = b(context);
        if (TextUtils.isEmpty(strB)) {
            d = "";
            return "";
        }
        d = cn.jiguang.f.a.a(context, strB);
        cn.jiguang.ad.d.b("JCommonServiceHelper", "user serviceProcess is:" + d);
        return d;
    }

    public static void a(Context context, String str, Bundle bundle) {
        try {
            cn.jiguang.ad.d.b("JCommonServiceHelper", "onAction action:" + str + " bundle:" + (bundle == null ? "null" : bundle.toString()));
            String strB = b(context);
            if (TextUtils.isEmpty(strB)) {
                cn.jiguang.a.a.b(context, str, bundle);
            } else {
                e.a().a(context, strB, str, bundle);
            }
        } catch (Throwable th) {
            cn.jiguang.ad.d.d("JCommonServiceHelper", "onAction failed", th);
        }
    }

    public static String b(Context context) {
        ComponentInfo componentInfoA;
        try {
            String str = c;
            if (str != null) {
                return str;
            }
            Intent intent = new Intent();
            intent.setAction(JConstants.USER_SERVICE_ACTION);
            intent.setPackage(context.getPackageName());
            List<String> listA = cn.jiguang.f.a.a(context, intent, "");
            if (listA.size() > 0 && JCommonService.class.isAssignableFrom(Class.forName(listA.get(0)))) {
                c = listA.get(0);
                cn.jiguang.ad.d.e("JCommonServiceHelper", "found userServiceClass :" + c + " by getCommonServiceNames");
            }
        } catch (Throwable th) {
            cn.jiguang.ad.d.b("JCommonServiceHelper", "getUserServiceClass failed:" + th);
        }
        if (TextUtils.isEmpty(c) && (componentInfoA = cn.jiguang.f.a.a(context, context.getPackageName(), (Class<?>) JCommonService.class)) != null) {
            c = componentInfoA.name;
            cn.jiguang.ad.d.e("JCommonServiceHelper", "found userServiceClass :" + c + " by getComponentInfo");
        }
        if (TextUtils.isEmpty(c)) {
            c = "";
        }
        return c;
    }

    public final void b(Context context, String str, Bundle bundle) {
        try {
            cn.jiguang.ad.d.d("JCommonServiceHelper", "callAction action:" + str + " bundle:" + (bundle == null ? "null" : bundle.toString()));
            cn.jiguang.aq.a.a(ShareConstants.ACTION, new d(this, JConstants.getAppContext(context), str, bundle));
        } catch (Throwable th) {
            cn.jiguang.ad.d.d("JCommonServiceHelper", "callAction failed", th);
        }
    }
}
