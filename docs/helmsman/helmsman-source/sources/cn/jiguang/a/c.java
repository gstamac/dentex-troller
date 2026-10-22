package cn.jiguang.a;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import cn.jiguang.ad.d;
import cn.jiguang.internal.JConstants;
import com.MAVLink.enums.MAV_CMD;
import com.autonavi.ae.gmap.utils.GLMapStaticValue;
import com.facebook.GraphResponse;

/* JADX INFO: loaded from: classes.dex */
final class c implements Runnable {
    Context a;
    boolean b;
    String c;
    Bundle d;

    c(Context context, boolean z, String str, Bundle bundle) {
        this.a = context;
        this.b = z;
        this.c = str;
        this.d = bundle;
    }

    @Override // java.lang.Runnable
    public final void run() {
        String str;
        try {
            if (!this.b) {
                if (a.a(this.a)) {
                    cn.jiguang.ac.c.a();
                    cn.jiguang.ac.c.a(this.a, this.c, this.d);
                    return;
                }
                return;
            }
            if (this.c.equals("a5")) {
                cn.jiguang.aj.b.a().a(this.a, MAV_CMD.MAV_CMD_IMAGE_STOP_CAPTURE, 1, "");
                cn.jiguang.g.b.a(this.a, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.n().a(true)});
                String string = this.d.getString("appkey");
                if (TextUtils.isEmpty(string)) {
                    cn.jiguang.aj.b.a().a(this.a, 0, 10003, "appkey is empty");
                    return;
                }
                String strB = cn.jiguang.ac.a.b(this.a);
                if (!TextUtils.isEmpty(strB) && !strB.equals(string)) {
                    cn.jiguang.aj.b.a().a(this.a, 0, GLMapStaticValue.AM_CALLBACK_NEED_NEXTFRAME, "appkey not same with meta appkey");
                    return;
                }
                cn.jiguang.f.a.g(this.a);
                JConstants.APP_KEY = string;
                if (cn.jiguang.aj.c.a(this.a)) {
                    str = "tcp_a24";
                } else {
                    String strB2 = cn.jiguang.d.a.b(this.a);
                    if (!TextUtils.isEmpty(strB2)) {
                        cn.jiguang.aj.b.a().a(this.a, MAV_CMD.MAV_CMD_IMAGE_STOP_CAPTURE, 0, strB2);
                    }
                    str = "tcp_a1";
                }
                this.c = str;
            } else if (this.c.equals("tcp_a23")) {
                cn.jiguang.aj.b.a().a(this.a, MAV_CMD.MAV_CMD_IMAGE_START_CAPTURE, 0, GraphResponse.SUCCESS_KEY);
            }
            if (a.b(this.a)) {
                cn.jiguang.ac.c.a().b(this.a, this.c, this.d);
            }
        } catch (Throwable th) {
            d.h("JCoreGobal", "do action error:" + th.getMessage());
        }
    }
}
