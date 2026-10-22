package com.amap.api.mapcore.util;

import javax.microedition.khronos.opengles.GL10;

/* JADX INFO: compiled from: GLOverlay.java */
/* JADX INFO: loaded from: classes.dex */
public abstract class ca {
    private k a;

    public void destroy() {
    }

    public abstract int getZIndex();

    public abstract void onDrawFrame(GL10 gl10);
}
