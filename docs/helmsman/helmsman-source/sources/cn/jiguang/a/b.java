package cn.jiguang.a;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import cn.jiguang.ad.d;
import cn.jiguang.android.IDataShare;
import cn.jiguang.api.JCoreManager;
import cn.jiguang.internal.JConstants;
import cn.jpush.android.service.DataShare;

/* JADX INFO: loaded from: classes.dex */
final class b implements ServiceConnection {
    b() {
    }

    @Override // android.content.ServiceConnection
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        d.b("JCoreGobal", "action - onServiceConnected, ComponentName:" + componentName);
        d.d("JCoreGobal", "Remote Service bind success.");
        try {
            DataShare.init(IDataShare.Stub.asInterface(iBinder), cn.jiguang.ac.c.a(JConstants.getAppContext(null)));
            if (JConstants.mApplicationContext != null) {
                JCoreManager.init(JConstants.mApplicationContext);
            }
        } catch (Throwable th) {
            d.h("JCoreGobal", "onServiceConnected e:" + th);
        }
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName componentName) {
        d.b("JCoreGobal", "action - onServiceDisconnected, ComponentName:" + componentName);
    }
}
