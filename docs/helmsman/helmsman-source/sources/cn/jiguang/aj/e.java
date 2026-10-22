package cn.jiguang.aj;

import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
public final class e {
    public static Object a(Context context, String str, Object obj) {
        try {
            return cn.jiguang.h.a.a(context, str, obj);
        } catch (Throwable th) {
            cn.jiguang.ad.d.g("JCommonPresenter", "jcommon call failed:" + th.getMessage());
            return null;
        }
    }
}
