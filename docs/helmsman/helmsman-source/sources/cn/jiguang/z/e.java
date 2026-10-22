package cn.jiguang.z;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

/* JADX INFO: loaded from: classes.dex */
final class e implements ServiceConnection {
    final /* synthetic */ a a;

    private e(a aVar) {
        this.a = aVar;
    }

    /* synthetic */ e(a aVar, byte b) {
        this(aVar);
    }

    @Override // android.content.ServiceConnection
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        try {
            cn.jiguang.w.a.a("JWake", "unbind wake ServiceConnection");
            this.a.b.getApplicationContext().unbindService(this);
        } catch (Throwable th) {
            cn.jiguang.w.a.d("JWake", "onServiceConnected throwable" + th.getMessage());
        }
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName componentName) {
    }
}
