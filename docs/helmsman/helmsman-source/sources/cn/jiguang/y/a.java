package cn.jiguang.y;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import cn.jiguang.d.b;
import cn.jiguang.l.e;
import cn.jiguang.l.h;
import com.facebook.internal.ServerProtocol;
import io.reactivex.annotations.SchedulerSupport;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class a extends cn.jiguang.l.a {
    private static volatile a b;
    private Context a;
    private Bundle c;
    private String d = "";
    private int e = 0;
    private int f = 0;
    private int g = 0;

    public static a a() {
        if (b == null) {
            synchronized (a.class) {
                b = new a();
            }
        }
        return b;
    }

    private JSONObject a(String str, int i, int i2) {
        try {
            JSONObject jSONObject = new JSONObject();
            JSONArray jSONArray = new JSONArray();
            JSONObject jSONObject2 = new JSONObject();
            jSONArray.put(i);
            jSONArray.put(i2);
            jSONObject2.put(str, jSONArray);
            jSONObject.put("type", "sdk_type");
            jSONObject.put("itime", b.b(this.a));
            jSONObject.put(ServerProtocol.DIALOG_PARAM_SDK_VERSION, jSONObject2);
            return jSONObject;
        } catch (JSONException e) {
            cn.jiguang.w.a.d("JType", "package json exception: " + e.getMessage());
            return null;
        }
    }

    @Override // cn.jiguang.l.a
    protected final String a(Context context) {
        this.a = context;
        return "JType";
    }

    @Override // cn.jiguang.l.a
    protected final void a(String str, Bundle bundle) {
        this.c = bundle;
    }

    @Override // cn.jiguang.l.a
    protected final void b(Context context, String str) {
    }

    @Override // cn.jiguang.l.a
    protected final void d(Context context, String str) {
        JSONObject jSONObjectA = a(this.d, this.e, this.f);
        if (jSONObjectA == null) {
            cn.jiguang.w.a.d("JType", "there are no data to report");
        } else {
            h.a(context, (Object) jSONObjectA);
        }
    }

    @Override // cn.jiguang.l.a
    protected final boolean d() {
        StringBuilder sb;
        Bundle bundle = this.c;
        boolean z = false;
        if (bundle == null) {
            return false;
        }
        this.d = bundle.getString("name");
        this.e = this.c.getInt(SchedulerSupport.CUSTOM, 0);
        this.f = this.c.getInt("dynamic", 0);
        this.g = this.c.getInt("sdk_v", 0);
        cn.jiguang.w.a.a("JType", "parseBundle type:" + this.d + ",custom:" + this.e + ",dynamic:" + this.f + ",sdkVersion:" + this.g);
        Context context = this.a;
        String str = this.d;
        int i = this.e;
        int i2 = this.f;
        int i3 = this.g;
        if (!TextUtils.isEmpty(str) && i >= 0 && i2 >= 0) {
            int iK = e.k(context, str);
            cn.jiguang.w.a.a("JType", "[isTypeReportEnable],lastversion:" + iK + ",curversion:" + i3 + ",type:" + str);
            if (iK != i3 || !e.j(context, str).equals(i + "," + i2)) {
                z = true;
            }
        }
        if (z) {
            sb = new StringBuilder();
            String string = sb.append(this.e).append(",").append(this.f).toString();
            e.a(this.a, this.d, this.g);
            e.a(this.a, this.d, string);
        } else {
            sb = new StringBuilder("type [");
            cn.jiguang.w.a.a("JType", sb.append(this.d).append("] data not change").toString());
        }
        return z;
    }
}
