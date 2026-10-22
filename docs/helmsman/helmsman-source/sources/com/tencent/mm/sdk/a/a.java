package com.tencent.mm.sdk.a;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.tencent.mm.sdk.b.b;
import com.tencent.mm.sdk.b.h;
import com.tencent.mm.sdk.constants.ConstantsAPI;

/* JADX INFO: loaded from: classes2.dex */
public final class a {

    /* JADX INFO: renamed from: com.tencent.mm.sdk.a.a$a, reason: collision with other inner class name */
    public static class C0028a {
        public String W;
        public String X;
        public String Y;
        public Bundle Z;
        public int flags = -1;

        public final String toString() {
            return "targetPkgName:" + this.W + ", targetClassName:" + this.X + ", content:" + this.Y + ", flags:" + this.flags + ", bundle:" + this.Z;
        }
    }

    public static boolean a(Context context, C0028a c0028a) {
        if (context == null) {
            b.b("MicroMsg.SDK.MMessageAct", "send fail, invalid argument");
            return false;
        }
        if (h.h(c0028a.W)) {
            b.b("MicroMsg.SDK.MMessageAct", "send fail, invalid targetPkgName, targetPkgName = " + c0028a.W);
            return false;
        }
        if (h.h(c0028a.X)) {
            c0028a.X = c0028a.W + ".wxapi.WXEntryActivity";
        }
        b.e("MicroMsg.SDK.MMessageAct", "send, targetPkgName = " + c0028a.W + ", targetClassName = " + c0028a.X);
        Intent intent = new Intent();
        intent.setClassName(c0028a.W, c0028a.X);
        if (c0028a.Z != null) {
            intent.putExtras(c0028a.Z);
        }
        String packageName = context.getPackageName();
        intent.putExtra(ConstantsAPI.SDK_VERSION, 587268097);
        intent.putExtra(ConstantsAPI.APP_PACKAGE, packageName);
        intent.putExtra(ConstantsAPI.CONTENT, c0028a.Y);
        intent.putExtra(ConstantsAPI.CHECK_SUM, com.tencent.mm.sdk.a.a.b.a(c0028a.Y, 587268097, packageName));
        if (c0028a.flags == -1) {
            intent.addFlags(268435456).addFlags(134217728);
        } else {
            intent.setFlags(c0028a.flags);
        }
        try {
            context.startActivity(intent);
            b.e("MicroMsg.SDK.MMessageAct", "send mm message, intent=" + intent);
            return true;
        } catch (Exception e) {
            b.a("MicroMsg.SDK.MMessageAct", "send fail, ex = %s", e.getMessage());
            return false;
        }
    }
}
