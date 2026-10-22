package com.autonavi.amap.mapcore.interfaces;

import com.autonavi.ae.gmap.GLMapState;

/* JADX INFO: loaded from: classes.dex */
public interface IAMapListener {
    void afterDrawFrame(int i, GLMapState gLMapState);

    void afterDrawLabel(int i, GLMapState gLMapState);

    void beforeDrawLabel(int i, GLMapState gLMapState);
}
