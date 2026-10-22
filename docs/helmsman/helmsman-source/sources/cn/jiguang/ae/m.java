package cn.jiguang.ae;

import android.content.Context;
import java.io.File;
import java.util.Set;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
final class m implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ Set b;
    final /* synthetic */ JSONObject c;
    final /* synthetic */ File d;

    m(Context context, Set set, JSONObject jSONObject, File file) {
        this.a = context;
        this.b = set;
        this.c = jSONObject;
        this.d = file;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            k.a(this.a, this.b, this.c, this.d, null);
        } catch (Throwable unused) {
        }
    }
}
