package cn.jiguang.aj;

import android.content.Context;
import android.text.TextUtils;
import cn.jiguang.api.JDispatchAction;
import cn.jiguang.internal.JConstants;
import com.zhy.http.okhttp.OkHttpUtils;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class q {
    private static volatile q a;
    private static final Object b = new Object();
    private Map<Long, String> c = new HashMap();

    public static q a() {
        if (a == null) {
            synchronized (b) {
                if (a == null) {
                    a = new q();
                }
            }
        }
        return a;
    }

    private static String a(Context context, String str, String str2) {
        String str3 = (String) cn.jiguang.g.b.a(context, cn.jiguang.g.a.a(str));
        if (!TextUtils.isEmpty(str2) && !str2.equals(str3)) {
            return str2;
        }
        StringBuilder sbAppend = new StringBuilder("need not ").append(str).append(" userctrl,newest version:");
        if (TextUtils.isEmpty(str2)) {
            str2 = "null";
        }
        cn.jiguang.ad.d.a("UserCtrlHelper", sbAppend.append(str2).toString());
        return "";
    }

    public final void a(long j) {
        cn.jiguang.ad.d.b("UserCtrlHelper", "onUserCtrlTimeout rid:" + j);
        this.c.remove(Long.valueOf(j));
    }

    public final void a(long j, int i) {
        String strRemove = this.c.remove(Long.valueOf(j));
        if (TextUtils.isEmpty(strRemove)) {
            cn.jiguang.ad.d.b("UserCtrlHelper", "onUserCtrlFailed but not found rid:" + j);
            return;
        }
        cn.jiguang.ad.d.b("UserCtrlHelper", "onUserCtrlFailed rid:" + j + ",sdkType:" + strRemove + ",errorCode:" + i);
        b.a();
        if (TextUtils.isEmpty(b.a(strRemove, ""))) {
            cn.jiguang.ad.d.b("UserCtrlHelper", "onUserCtrlFailed but not found sdkversion by sdkType:" + strRemove);
        }
    }

    public final void a(Context context) {
        if (context == null) {
            cn.jiguang.ad.d.f("UserCtrlHelper", "handleUserCtrl failed,context is null");
            return;
        }
        HashMap<String, JDispatchAction> map = b.a;
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, JDispatchAction> entry : map.entrySet()) {
                JDispatchAction value = entry.getValue();
                if (value != null) {
                    String strA = a(context, entry.getKey(), value.getSdkVersion(entry.getKey()));
                    if (!TextUtils.isEmpty(strA)) {
                        short userCtrlProperty = value.getUserCtrlProperty(entry.getKey());
                        String key = entry.getKey();
                        cn.jiguang.ad.d.b("UserCtrlHelper", "sendUserCtrlInfo sdkType:" + key + ",property:" + ((int) userCtrlProperty) + ",verInfo:" + strA);
                        long jB = o.b();
                        cn.jiguang.af.b.a(context, JConstants.SDK_TYPE, 26, 0, jB, OkHttpUtils.DEFAULT_MILLISECONDS, cn.jiguang.al.b.a(userCtrlProperty, (short) 1, strA));
                        this.c.put(Long.valueOf(jB), key);
                    }
                }
            }
        }
    }

    public final void a(Context context, long j) {
        StringBuilder sbAppend;
        Map<Long, String> map;
        String strRemove = this.c.remove(Long.valueOf(j));
        if (!TextUtils.isEmpty(strRemove)) {
            cn.jiguang.ad.d.b("UserCtrlHelper", "userCtrlSuccess rid:" + j + ",sdkType:" + strRemove);
            b.a();
            String strA = b.a(strRemove, "");
            if (TextUtils.isEmpty(strA)) {
                sbAppend = new StringBuilder("userCtrlSuccess but not found sdkversion by sdkType:").append(strRemove);
            } else {
                cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.a(strRemove).a(strA)});
            }
            map = this.c;
            if (map == null && map.isEmpty() && !cn.jiguang.ac.b.a().d()) {
                i.a().a(context, "tcp_a21", null);
                return;
            }
            return;
        }
        sbAppend = new StringBuilder("userCtrlSuccess but not found rid:").append(j);
        cn.jiguang.ad.d.b("UserCtrlHelper", sbAppend.toString());
        map = this.c;
        if (map == null) {
        }
    }

    public final boolean b(Context context) {
        if (context == null) {
            cn.jiguang.ad.d.g("UserCtrlHelper", "get isNeedUserCtrl failed,context is null");
            return false;
        }
        HashMap<String, JDispatchAction> map = b.a;
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, JDispatchAction> entry : map.entrySet()) {
                JDispatchAction value = entry.getValue();
                if (value != null && !TextUtils.isEmpty(a(context, entry.getKey(), value.getSdkVersion(entry.getKey())))) {
                    return true;
                }
            }
        }
        return false;
    }
}
