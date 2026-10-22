package com.tencent.mm.sdk.diffdev.a;

import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import com.tencent.mm.sdk.diffdev.IDiffDevOAuth;
import com.tencent.mm.sdk.diffdev.OAuthListener;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public final class a implements IDiffDevOAuth {
    private d ae;
    private com.tencent.mm.sdk.b.d ac = null;
    private List<OAuthListener> ad = new ArrayList();
    private OAuthListener af = new b(this);

    static /* synthetic */ d c(a aVar) {
        aVar.ae = null;
        return null;
    }

    @Override // com.tencent.mm.sdk.diffdev.IDiffDevOAuth
    public final void addListener(OAuthListener oAuthListener) {
        if (this.ad.contains(oAuthListener)) {
            return;
        }
        this.ad.add(oAuthListener);
    }

    @Override // com.tencent.mm.sdk.diffdev.IDiffDevOAuth
    public final boolean auth(String str, String str2, String str3, String str4, String str5, OAuthListener oAuthListener) {
        Log.i("MicroMsg.SDK.DiffDevOAuth", "start auth, appId = " + str);
        if (str == null || str.length() <= 0 || str2 == null || str2.length() <= 0) {
            Log.d("MicroMsg.SDK.DiffDevOAuth", String.format("auth fail, invalid argument, appId = %s, scope = %s", str, str2));
            return false;
        }
        if (this.ac == null) {
            this.ac = new com.tencent.mm.sdk.b.d(Looper.getMainLooper());
        }
        addListener(oAuthListener);
        if (this.ae != null) {
            Log.d("MicroMsg.SDK.DiffDevOAuth", "auth, already running, no need to start auth again");
            return true;
        }
        d dVar = new d(str, str2, str3, str4, str5, this.af);
        this.ae = dVar;
        dVar.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        return true;
    }

    @Override // com.tencent.mm.sdk.diffdev.IDiffDevOAuth
    public final void detach() {
        Log.i("MicroMsg.SDK.DiffDevOAuth", "detach");
        this.ad.clear();
        stopAuth();
    }

    @Override // com.tencent.mm.sdk.diffdev.IDiffDevOAuth
    public final void removeAllListeners() {
        this.ad.clear();
    }

    @Override // com.tencent.mm.sdk.diffdev.IDiffDevOAuth
    public final void removeListener(OAuthListener oAuthListener) {
        this.ad.remove(oAuthListener);
    }

    @Override // com.tencent.mm.sdk.diffdev.IDiffDevOAuth
    public final boolean stopAuth() {
        boolean zQ;
        Log.i("MicroMsg.SDK.DiffDevOAuth", "stopAuth");
        try {
            d dVar = this.ae;
            zQ = dVar == null ? true : dVar.q();
        } catch (Exception e) {
            Log.w("MicroMsg.SDK.DiffDevOAuth", "stopAuth fail, ex = " + e.getMessage());
            zQ = false;
        }
        this.ae = null;
        return zQ;
    }
}
