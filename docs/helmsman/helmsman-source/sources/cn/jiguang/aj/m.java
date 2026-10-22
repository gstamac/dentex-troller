package cn.jiguang.aj;

import android.content.Context;
import android.os.Message;
import cn.jiguang.api.JCoreManager;
import cn.jiguang.internal.JConstants;

/* JADX INFO: loaded from: classes.dex */
final class m extends cn.jiguang.an.a implements Runnable {
    private Context a;
    private int d;
    private final Object e;
    private int f;

    m(Context context, int i, Object obj) {
        this.a = context;
        this.d = i;
        this.e = obj;
    }

    m(Context context, Object obj) {
        this.a = context;
        this.e = obj;
    }

    @Override // cn.jiguang.an.a
    public final void a(Message message) {
        if (message.what >= 60000) {
            if (this.e instanceof cn.jiguang.ah.h) {
                cn.jiguang.ad.d.b("TcpReporter", "time to idle=" + this.e);
                l.a().a(this.a, (cn.jiguang.ah.h) this.e, 0);
                return;
            }
            return;
        }
        if (message.what >= 50000) {
            Object obj = this.e;
            if (obj instanceof n) {
                n nVar = (n) obj;
                cn.jiguang.ad.d.b("TcpReporter", "onTimeout=" + nVar);
                l.a().a(nVar.g, nVar.f);
                l.a().a(this.a, nVar);
            }
        }
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            int i = this.d;
            if (i != 1) {
                if (i != 2) {
                    if (i == 3) {
                        Object obj = this.e;
                        if (obj instanceof n) {
                            n nVar = (n) obj;
                            cn.jiguang.ad.d.b("TcpReporter", "onResult, data=" + nVar + " code=" + this.f);
                            JCoreManager.onEvent(this.a, JConstants.SDK_TYPE, 61, "", null, Integer.valueOf(this.f), nVar.b, nVar.c, Integer.valueOf(nVar.a), nVar.d, nVar.e, nVar.j);
                        }
                    }
                } else if (this.e instanceof cn.jiguang.ah.h) {
                    l.b(l.a(), this.a, (cn.jiguang.ah.h) this.e);
                }
            } else if (this.e instanceof cn.jiguang.ah.h) {
                l.a(l.a(), this.a, (cn.jiguang.ah.h) this.e);
            }
        } catch (Throwable unused) {
        }
    }
}
