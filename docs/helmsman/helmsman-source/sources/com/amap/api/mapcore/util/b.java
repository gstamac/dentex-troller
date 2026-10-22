package com.amap.api.mapcore.util;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

/* JADX INFO: compiled from: AMapGLRenderer.java */
/* JADX INFO: loaded from: classes.dex */
public class b implements l {
    protected boolean a;
    private k b;

    @Override // com.amap.api.mapcore.util.l
    public void a(dj djVar) {
    }

    @Override // com.amap.api.mapcore.util.l
    public void a(dk dkVar) {
    }

    @Override // com.amap.api.mapcore.util.l
    public int getHeight() {
        return 0;
    }

    @Override // com.amap.api.mapcore.util.l
    public SurfaceHolder getHolder() {
        return null;
    }

    @Override // com.amap.api.mapcore.util.l
    public int getRenderMode() {
        return 0;
    }

    @Override // com.amap.api.mapcore.util.l
    public int getWidth() {
        return 0;
    }

    @Override // com.amap.api.mapcore.util.l
    public boolean post(Runnable runnable) {
        return false;
    }

    @Override // com.amap.api.mapcore.util.l
    public boolean postDelayed(Runnable runnable, long j) {
        return false;
    }

    @Override // com.amap.api.mapcore.util.l
    public void requestRender() {
    }

    @Override // com.amap.api.mapcore.util.l
    public void setRenderMode(int i) {
    }

    @Override // com.amap.api.mapcore.util.l
    public void setRenderer(GLSurfaceView.Renderer renderer) {
    }

    @Override // com.amap.api.mapcore.util.l
    public void setVisibility(int i) {
    }

    public b(Context context) {
        this(context, null);
    }

    public b(Context context, AttributeSet attributeSet) {
        this.b = null;
        this.a = false;
        this.b = new a(this, context, attributeSet);
    }

    public k a() {
        return this.b;
    }

    @Override // com.amap.api.mapcore.util.l
    public void queueEvent(Runnable runnable) {
        if (runnable != null) {
            runnable.run();
        }
    }

    @Override // com.amap.api.mapcore.util.l
    public boolean isEnabled() {
        return this.b != null;
    }
}
