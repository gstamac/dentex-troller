package cn.jpush.android.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import cn.jiguang.z.a;

/* JADX INFO: loaded from: classes.dex */
public class DActivity extends Activity {
    private static final String TAG = "DActivity";

    private void handleStart() {
        try {
            a.a(getApplicationContext(), 8);
        } catch (Throwable th) {
            cn.jiguang.w.a.a(TAG, "handle start error#" + th);
        }
        try {
            finish();
        } catch (Throwable th2) {
            cn.jiguang.w.a.a(TAG, "finish error#" + th2);
        }
    }

    @Override // android.app.Activity
    protected final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        cn.jiguang.w.a.a(TAG, "DActivity oncreate");
        handleStart();
    }

    @Override // android.app.Activity
    protected final void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        cn.jiguang.w.a.a(TAG, "DActivity onNewIntent");
        handleStart();
    }
}
