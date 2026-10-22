package cn.jpush.android.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import cn.jiguang.ad.d;
import cn.jiguang.d.a;

/* JADX INFO: loaded from: classes.dex */
public class DownloadActivity extends Activity {
    private static final String TAG = "DownloadActivity";

    @Override // android.app.Activity
    protected final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        d.b(TAG, "DownloadActivity onCreate");
        try {
            a.a(getApplicationContext(), getIntent());
        } catch (Throwable unused) {
        }
        finish();
    }

    @Override // android.app.Activity
    protected final void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        d.b(TAG, "DownloadActivity onNewIntent");
        try {
            a.a(getApplicationContext(), intent);
        } catch (Throwable unused) {
        }
        finish();
    }

    @Override // android.app.Activity
    protected final void onRestart() {
        super.onRestart();
    }

    @Override // android.app.Activity
    protected final void onResume() {
        super.onResume();
    }

    @Override // android.app.Activity
    protected final void onStart() {
        super.onStart();
    }
}
