package cn.jiguang.aj;

import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
public final class c {
    public static boolean a(Context context) {
        String str = (String) cn.jiguang.g.b.a(context, cn.jiguang.g.a.C());
        String strA = cn.jiguang.ac.a.a(context);
        cn.jiguang.ad.d.b("InitHelper", "appkey=" + strA + " last=" + str);
        if (!cn.jiguang.f.i.a(str) && !"null".equals(str) && str.equalsIgnoreCase(strA)) {
            return false;
        }
        cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.C().a(strA)});
        cn.jiguang.ad.d.c("InitHelper", "We found the appKey is changed or register appkey is empty. Will re-register.");
        cn.jiguang.af.b.a(context);
        return true;
    }
}
