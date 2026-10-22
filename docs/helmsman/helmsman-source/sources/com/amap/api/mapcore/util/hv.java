package com.amap.api.mapcore.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.autonavi.amap.mapcore.Inner_3dMap_location;

/* JADX INFO: compiled from: MapLocationManagerResultHandler.java */
/* JADX INFO: loaded from: classes.dex */
public final class hv extends Handler {
    ht a;

    public hv(Looper looper, ht htVar) {
        super(looper);
        this.a = htVar;
    }

    public hv(ht htVar) {
        this.a = htVar;
    }

    @Override // android.os.Handler
    public final void handleMessage(Message message) {
        if (message.what != 1) {
            return;
        }
        try {
            ht htVar = this.a;
            if (htVar != null) {
                htVar.a((Inner_3dMap_location) message.obj);
            }
        } catch (Throwable th) {
            ij.a(th, "ClientResultHandler", "RESULT_LOCATION_FINISH");
        }
    }
}
