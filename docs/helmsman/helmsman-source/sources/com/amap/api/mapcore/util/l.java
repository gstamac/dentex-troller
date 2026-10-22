package com.amap.api.mapcore.util;

import android.opengl.GLSurfaceView;
import android.view.SurfaceHolder;

/* JADX INFO: compiled from: IGLSurfaceView.java */
/* JADX INFO: loaded from: classes.dex */
public interface l {
    void a(dj djVar);

    void a(dk dkVar);

    int getHeight();

    SurfaceHolder getHolder();

    int getRenderMode();

    int getWidth();

    boolean isEnabled();

    boolean post(Runnable runnable);

    boolean postDelayed(Runnable runnable, long j);

    void queueEvent(Runnable runnable);

    void requestRender();

    void setRenderMode(int i);

    void setRenderer(GLSurfaceView.Renderer renderer);

    void setVisibility(int i);
}
