package com.tencent.mm.sdk.diffdev.a;

import android.util.Log;
import com.tencent.mm.sdk.diffdev.OAuthErrCode;
import com.tencent.mm.sdk.diffdev.OAuthListener;
import java.util.ArrayList;
import java.util.Iterator;

/* JADX INFO: loaded from: classes2.dex */
final class b implements OAuthListener {
    final /* synthetic */ a ag;

    b(a aVar) {
        this.ag = aVar;
    }

    @Override // com.tencent.mm.sdk.diffdev.OAuthListener
    public final void onAuthFinish(OAuthErrCode oAuthErrCode, String str) {
        Log.d("MicroMsg.SDK.ListenerWrapper", String.format("onAuthFinish, errCode = %s, authCode = %s", oAuthErrCode.toString(), str));
        a.c(this.ag);
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.ag.ad);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ((OAuthListener) it.next()).onAuthFinish(oAuthErrCode, str);
        }
    }

    @Override // com.tencent.mm.sdk.diffdev.OAuthListener
    public final void onAuthGotQrcode(String str, byte[] bArr) {
        Log.d("MicroMsg.SDK.ListenerWrapper", "onAuthGotQrcode, qrcodeImgPath = " + str);
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.ag.ad);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ((OAuthListener) it.next()).onAuthGotQrcode(str, bArr);
        }
    }

    @Override // com.tencent.mm.sdk.diffdev.OAuthListener
    public final void onQrcodeScanned() {
        Log.d("MicroMsg.SDK.ListenerWrapper", "onQrcodeScanned");
        if (this.ag.ac != null) {
            this.ag.ac.post(new c(this));
        }
    }
}
