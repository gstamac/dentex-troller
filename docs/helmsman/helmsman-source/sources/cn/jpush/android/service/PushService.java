package cn.jpush.android.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/* JADX INFO: loaded from: classes.dex */
public class PushService extends Service {
    private static final String TAG = "PushService";

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }
}
