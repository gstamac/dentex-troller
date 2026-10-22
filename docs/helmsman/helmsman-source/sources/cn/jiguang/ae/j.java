package cn.jiguang.ae;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import java.util.LinkedHashSet;

/* JADX INFO: loaded from: classes.dex */
final class j implements Runnable {
    private final Context a;

    private j(Context context) {
        this.a = context;
    }

    /* synthetic */ j(Context context, byte b) {
        this(context);
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            String strC = i.c(this.a);
            LinkedHashSet<String> linkedHashSetD = i.d(this.a);
            linkedHashSetD.addAll(i.a());
            cn.jiguang.ad.d.b("ReportSis", "sis urls=" + linkedHashSetD.toString() + " post json=" + strC);
            if (!cn.jiguang.f.a.d(this.a)) {
                cn.jiguang.ad.d.f("ReportSis", "give up sis, because network is not connected");
                return;
            }
            for (String str : linkedHashSetD) {
                if (!TextUtils.isEmpty(str)) {
                    if (Build.VERSION.SDK_INT >= 28 && !str.startsWith("https://")) {
                        cn.jiguang.ad.d.f("ReportSis", "won't use http at device since 28");
                    } else if (i.b(this.a, str, strC)) {
                        return;
                    }
                }
            }
        } catch (Throwable unused) {
        }
    }
}
