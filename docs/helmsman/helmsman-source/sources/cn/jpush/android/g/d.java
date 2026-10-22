package cn.jpush.android.g;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import cn.asus.push.DataBuffer;
import cn.asus.push.IAIDLInvoke;

/* JADX INFO: loaded from: classes.dex */
public final class d extends a {
    private IAIDLInvoke b = null;

    static /* synthetic */ IAIDLInvoke a(d dVar, IAIDLInvoke iAIDLInvoke) {
        dVar.b = null;
        return null;
    }

    @Override // cn.jpush.android.g.a
    public final void a(String str, String str2) {
        String str3;
        if (this.b != null) {
            try {
                Bundle bundle = new Bundle();
                bundle.putString("JPUSH_CONTENT", str2);
                this.b.asyncCall(new DataBuffer(str, bundle), new b());
                return;
            } catch (Throwable th) {
                str3 = "send data fail:" + th;
            }
        } else {
            str3 = "send data fail, please init first.";
        }
        cn.jpush.android.i.b.g("ServiceConnection", str3);
    }

    @Override // cn.jpush.android.g.a
    public final boolean a() {
        byte b = 0;
        if (a.get()) {
            cn.jpush.android.i.b.b("ServiceConnection", "already connecting.");
            return false;
        }
        a.set(true);
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(cn.jpush.android.f.c.e, "cn.jpush.android.service.PushManagerService"));
            e eVar = new e(this, b);
            boolean zBindService = cn.jpush.android.f.c.a.bindService(intent, eVar, 1);
            cn.jpush.android.i.b.b("ServiceConnection", "connect--" + zBindService);
            if (zBindService) {
                this.b = IAIDLInvoke.Stub.asInterface(eVar.a.take());
            } else {
                a.set(false);
            }
            return zBindService;
        } catch (Throwable th) {
            cn.jpush.android.i.b.g("ServiceConnection", "init fail:" + th);
            a.set(false);
            return false;
        }
    }

    @Override // cn.jpush.android.g.a
    public final boolean b() {
        IAIDLInvoke iAIDLInvoke = this.b;
        if (iAIDLInvoke == null) {
            return false;
        }
        return iAIDLInvoke.asBinder().isBinderAlive();
    }
}
