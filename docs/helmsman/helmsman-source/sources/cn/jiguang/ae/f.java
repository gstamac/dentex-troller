package cn.jiguang.ae;

import android.content.Context;
import java.io.File;

/* JADX INFO: loaded from: classes.dex */
final class f implements Runnable {
    final /* synthetic */ Context a;

    f(Context context) {
        this.a = context;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            d.d.set(true);
            File[] fileArrA = cn.jiguang.f.e.a(d.e(this.a), cn.jiguang.f.f.b);
            if (fileArrA != null) {
                for (File file : fileArrA) {
                    d.a(this.a, file);
                }
            } else {
                cn.jiguang.ad.d.b("ReportHistory", "no history, no report");
            }
        } catch (Throwable th) {
            try {
                cn.jiguang.ad.d.h("ReportHistory", "run upload exception=" + th);
            } finally {
                d.d.set(false);
            }
        }
    }
}
