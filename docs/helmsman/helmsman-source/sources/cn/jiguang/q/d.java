package cn.jiguang.q;

import android.content.Context;
import com.bun.miitmdid.core.MdidSdkHelper;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;

/* JADX INFO: loaded from: classes.dex */
public final class d implements Callable<String> {
    public final LinkedBlockingQueue<String> a = new LinkedBlockingQueue<>(1);
    private Context b;

    public d(Context context) {
        this.b = context;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Override // java.util.concurrent.Callable
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public String call() {
        try {
            int iInitSdk = MdidSdkHelper.InitSdk(this.b, true, new e(this));
            cn.jiguang.w.a.a("MittCall", "nres:" + iInitSdk);
            return (iInitSdk == 0 || iInitSdk == 1008614) ? this.a.take() : "";
        } catch (Throwable th) {
            cn.jiguang.w.a.e("MittCall", "call ids failed:" + th.getMessage());
            return "";
        }
    }
}
