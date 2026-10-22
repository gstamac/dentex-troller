package cn.jiguang.l;

import android.content.Context;
import android.os.Bundle;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public abstract class a {
    static /* synthetic */ void a(a aVar, Context context, String str) {
        boolean zA = aVar.a(context, str);
        cn.jiguang.w.a.a("JCommon", str + " isBusinessEnable:" + zA);
        if (zA) {
            aVar.b(context, str);
        }
        boolean zC = aVar.c(context, str);
        cn.jiguang.w.a.a("JCommon", str + " isReportEnable:" + zC);
        if (zC) {
            aVar.d(context, str);
        }
    }

    static /* synthetic */ void a(a aVar, Context context, String str, Bundle bundle) {
        aVar.a(str, bundle);
        boolean zD = aVar.d();
        cn.jiguang.w.a.a("JCommon", str + " isActionBundleEnable:" + zD);
        if (zD) {
            aVar.b(context, str);
            aVar.d(context, str);
        }
    }

    static /* synthetic */ void a(a aVar, Context context, String str, JSONObject jSONObject) {
        aVar.a(jSONObject);
        if (jSONObject.optInt("cmd") != 45) {
            boolean zB = aVar.b();
            cn.jiguang.w.a.a("JCommon", str + " isActionCommandEnable:" + zB);
            if (zB) {
                aVar.b(context, str);
                aVar.d(context, str);
            }
        }
    }

    private boolean e(Context context, String str) {
        boolean zC = c();
        boolean zB = b();
        boolean zD = d(context);
        boolean z = zC && zB && zD;
        cn.jiguang.w.a.a("JCommon", str + " isActionEnable:" + z + ",actionUserEnable:" + zC + ",actionCommandEnable:" + zB + ",actionUidEnable:" + zD);
        return z;
    }

    protected abstract String a(Context context);

    public final void a(Context context, Bundle bundle) {
        String strA = a(context);
        cn.jiguang.w.a.a("JCommon", "executeBundleAction: [" + strA + "] from bundle");
        boolean zC = c();
        cn.jiguang.w.a.a("JCommon", strA + " isActionUserEnable:" + zC);
        if (zC) {
            cn.jiguang.af.b.a("JCommon", new b(this, context, strA, bundle));
        }
    }

    public final void a(Context context, JSONObject jSONObject) {
        String strA = a(context);
        cn.jiguang.w.a.a("JCommon", "executeCommandActionSingle: [" + strA + "] from cmd");
        boolean zC = c();
        cn.jiguang.w.a.a("JCommon", strA + " isActionUserEnable:" + zC);
        if (zC) {
            cn.jiguang.af.b.a(strA, new d(this, context, strA, jSONObject));
        }
    }

    protected void a(String str, Bundle bundle) {
        if (bundle != null) {
            cn.jiguang.w.a.a("JCommon", str + " parseJson:" + bundle.toString());
        }
    }

    protected void a(JSONObject jSONObject) {
    }

    protected boolean a(Context context, String str) {
        return e.b(context, str);
    }

    public final void b(Context context) {
        String strA = a(context);
        cn.jiguang.w.a.a("JCommon", "executeAction: [" + strA + "] from heartBeat");
        if (e(context, strA)) {
            cn.jiguang.af.b.a("JCommon", new c(this, context, strA));
        }
    }

    protected void b(Context context, String str) {
        e.c(context, str);
    }

    protected boolean b() {
        return true;
    }

    public final void c(Context context) {
        String strA = a(context);
        cn.jiguang.w.a.a("JCommon", "executeActionSingle: [" + strA + "] from heartBeat");
        if (e(context, strA)) {
            cn.jiguang.af.b.a(strA, new c(this, context, strA));
        }
    }

    protected boolean c() {
        return true;
    }

    protected boolean c(Context context, String str) {
        return e.b(context, str);
    }

    protected void d(Context context, String str) {
        e.e(context, str);
    }

    protected boolean d() {
        return true;
    }

    protected boolean d(Context context) {
        return cn.jiguang.d.a.d(context) > 0;
    }

    public Object e(Context context) {
        return null;
    }
}
