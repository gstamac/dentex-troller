package com.amap.api.mapcore.util;

import android.opengl.GLSurfaceView;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

/* JADX INFO: compiled from: GLMapSurfaceEGLContextFactory.java */
/* JADX INFO: loaded from: classes.dex */
public class dk implements GLSurfaceView.EGLContextFactory, j.f {
    @Override // android.opengl.GLSurfaceView.EGLContextFactory, com.amap.api.mapcore.util.j.f
    public EGLContext createContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig) {
        return null;
    }

    @Override // android.opengl.GLSurfaceView.EGLContextFactory, com.amap.api.mapcore.util.j.f
    public void destroyContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLContext eGLContext) {
    }
}
