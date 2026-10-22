package com.tencent.mm.sdk.diffdev.a;

import com.tencent.mm.sdk.diffdev.OAuthListener;
import java.util.ArrayList;
import java.util.Iterator;

/* JADX INFO: loaded from: classes2.dex */
final class c implements Runnable {
    final /* synthetic */ b ah;

    c(b bVar) {
        this.ah = bVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.ah.ag.ad);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ((OAuthListener) it.next()).onQrcodeScanned();
        }
    }
}
