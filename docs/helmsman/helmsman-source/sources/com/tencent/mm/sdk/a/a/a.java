package com.tencent.mm.sdk.a.a;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.tencent.mm.sdk.b.h;
import com.tencent.mm.sdk.constants.ConstantsAPI;

/* JADX INFO: loaded from: classes2.dex */
public final class a {

    /* JADX INFO: renamed from: com.tencent.mm.sdk.a.a.a$a, reason: collision with other inner class name */
    public static class C0029a {
        public String Y;
        public Bundle Z;
        public String aa;
        public String ab;
    }

    public static boolean a(Context context, C0029a c0029a) {
        String str;
        if (context == null) {
            str = "send fail, invalid argument";
        } else {
            if (!h.h(c0029a.ab)) {
                String str2 = !h.h(c0029a.aa) ? c0029a.aa + ".permission.MM_MESSAGE" : null;
                Intent intent = new Intent(c0029a.ab);
                if (c0029a.Z != null) {
                    intent.putExtras(c0029a.Z);
                }
                String packageName = context.getPackageName();
                intent.putExtra(ConstantsAPI.SDK_VERSION, 587268097);
                intent.putExtra(ConstantsAPI.APP_PACKAGE, packageName);
                intent.putExtra(ConstantsAPI.CONTENT, c0029a.Y);
                intent.putExtra(ConstantsAPI.CHECK_SUM, b.a(c0029a.Y, 587268097, packageName));
                context.sendBroadcast(intent, str2);
                com.tencent.mm.sdk.b.b.e("MicroMsg.SDK.MMessage", "send mm message, intent=" + intent + ", perm=" + str2);
                return true;
            }
            str = "send fail, action is null";
        }
        com.tencent.mm.sdk.b.b.b("MicroMsg.SDK.MMessage", str);
        return false;
    }
}
