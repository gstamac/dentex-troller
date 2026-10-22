package cn.jiguang.q;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import java.util.concurrent.Callable;

/* JADX INFO: loaded from: classes.dex */
final class c implements Callable<String> {
    private Context a;

    public c(Context context) {
        this.a = context;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Override // java.util.concurrent.Callable
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public String call() {
        try {
            String str = Build.MANUFACTURER;
            if (TextUtils.isEmpty(str)) {
                return "";
            }
            if ("huawei".equals(str.toLowerCase())) {
                return a.h(this.a);
            }
            cn.jiguang.w.a.d("IdHelper", "not supported this device:" + str);
            return "";
        } catch (Throwable th) {
            cn.jiguang.w.a.e("IdHelper", "[LocalIdCall] failed:" + th.getMessage());
            return "";
        }
    }
}
