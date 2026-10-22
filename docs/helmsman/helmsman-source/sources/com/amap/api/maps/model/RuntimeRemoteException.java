package com.amap.api.maps.model;

import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public final class RuntimeRemoteException extends RuntimeException {
    public RuntimeRemoteException(String str) {
        super(str);
    }

    public RuntimeRemoteException(RemoteException remoteException) {
        super(remoteException);
    }
}
