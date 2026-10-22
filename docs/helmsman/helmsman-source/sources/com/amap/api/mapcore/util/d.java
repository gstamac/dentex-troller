package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import com.autonavi.ae.gmap.GLMapRender;

/* JADX INFO: compiled from: AMapGLTextureView.java */
/* JADX INFO: loaded from: classes.dex */
public class d extends j implements l {
    protected boolean a;
    private k b;
    private GLMapRender c;

    @Override // com.amap.api.mapcore.util.l
    public SurfaceHolder getHolder() {
        return null;
    }

    public d(Context context) {
        this(context, null);
    }

    public d(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.b = null;
        this.c = null;
        this.a = false;
        di.a(this, 5, 6, 5, 0, 16, 8);
        this.b = new a(this, context, attributeSet);
    }

    public k a() {
        return this.b;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        try {
            return this.b.onTouchEvent(motionEvent);
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    @Override // com.amap.api.mapcore.util.l
    public void a(dj djVar) {
        super.a((j.e) djVar);
    }

    @Override // com.amap.api.mapcore.util.l
    public void a(dk dkVar) {
        super.a((j.f) dkVar);
    }

    @Override // com.amap.api.mapcore.util.j, com.amap.api.mapcore.util.l
    public void setRenderer(GLSurfaceView.Renderer renderer) {
        this.c = (GLMapRender) renderer;
        super.setRenderer(renderer);
    }

    @Override // com.amap.api.mapcore.util.j
    public void b() {
        if (!this.c.mSurfacedestoryed) {
            queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.d.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        if (d.this.c != null) {
                            d.this.c.onSurfaceDestory();
                        }
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            });
            int i = 0;
            while (!this.c.mSurfacedestoryed) {
                int i2 = i + 1;
                if (i >= 20) {
                    break;
                }
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException unused) {
                }
                i = i2;
            }
        }
        super.b();
    }

    @Override // com.amap.api.mapcore.util.j
    public void c() {
        super.c();
    }

    @Override // com.amap.api.mapcore.util.j, android.view.TextureView.SurfaceTextureListener
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        requestRender();
        try {
            Thread.sleep(100L);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return super.onSurfaceTextureDestroyed(surfaceTexture);
    }

    @Override // android.view.View
    protected void onWindowVisibilityChanged(int i) {
        GLMapRender gLMapRender;
        super.onWindowVisibilityChanged(i);
        try {
            if (i != 8 && i != 4) {
                if (i != 0 || (gLMapRender = this.c) == null) {
                    return;
                }
                gLMapRender.renderResume();
                return;
            }
            GLMapRender gLMapRender2 = this.c;
            if (gLMapRender2 != null) {
                gLMapRender2.renderPause();
                this.a = false;
            }
            requestRender();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.amap.api.mapcore.util.j, android.view.TextureView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            GLMapRender gLMapRender = this.c;
            if (gLMapRender != null) {
                gLMapRender.onAttachedToWindow();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        c();
    }

    @Override // com.amap.api.mapcore.util.j, android.view.View
    protected void onDetachedFromWindow() {
        b();
        try {
            GLMapRender gLMapRender = this.c;
            if (gLMapRender != null) {
                gLMapRender.onDetachedFromWindow();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        super.onDetachedFromWindow();
    }
}
