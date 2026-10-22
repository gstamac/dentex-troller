package cn.jpush.android.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import cn.jiguang.z.a;

/* JADX INFO: loaded from: classes.dex */
public class DaemonService extends Service {
    private static final String TAG = "DaemonService";

    public class MyBinder extends Binder {
        public MyBinder() {
        }

        public DaemonService getService() {
            return DaemonService.this;
        }
    }

    @Override // android.app.Service
    public final IBinder onBind(Intent intent) {
        a.a(getApplicationContext(), 2);
        return new MyBinder();
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        cn.jiguang.w.a.a(TAG, "action onCreate");
    }

    @Override // android.app.Service
    public void onDestroy() {
        cn.jiguang.w.a.a(TAG, "action onDestroy");
        super.onDestroy();
    }

    @Override // android.app.Service
    public final int onStartCommand(Intent intent, int i, int i2) {
        a.a(getApplicationContext(), 1);
        return super.onStartCommand(intent, i, i2);
    }
}
