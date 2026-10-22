package com.amap.api.mapcore.util;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.autonavi.ae.gmap.GLMapRender;

/* JADX INFO: compiled from: AMapGLSurfaceView.java */
/* JADX INFO: loaded from: classes.dex */
public class c extends GLSurfaceView implements l {
    protected boolean a;
    private k b;
    private GLMapRender c;

    public c(Context context) {
        this(context, null);
    }

    public c(Context context, AttributeSet attributeSet) {
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

    @Override // android.opengl.GLSurfaceView, com.amap.api.mapcore.util.l
    public void setRenderer(GLSurfaceView.Renderer renderer) {
        this.c = (GLMapRender) renderer;
        super.setRenderer(renderer);
    }

    @Override // com.amap.api.mapcore.util.l
    public void a(dj djVar) {
        super.setEGLConfigChooser(djVar);
    }

    @Override // com.amap.api.mapcore.util.l
    public void a(dk dkVar) {
        super.setEGLContextFactory(dkVar);
    }

    @Override // android.opengl.GLSurfaceView
    public void onPause() {
        if (!this.c.mSurfacedestoryed) {
            queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.c.1
                @Override // java.lang.Runnable
                public void run() {
                    if (c.this.c != null) {
                        try {
                            c.this.c.onSurfaceDestory();
                        } catch (Throwable th) {
                            th.printStackTrace();
                        }
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
        super.onPause();
    }

    @Override // android.opengl.GLSurfaceView
    public void onResume() {
        super.onResume();
    }

    @Override // android.view.SurfaceView, android.view.View
    protected void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        try {
            if (i == 8 || i == 4) {
                GLMapRender gLMapRender = this.c;
                if (gLMapRender != null) {
                    gLMapRender.renderPause();
                    this.a = false;
                }
            } else {
                if (i != 0) {
                    return;
                }
                GLMapRender gLMapRender2 = this.c;
                if (gLMapRender2 != null) {
                    gLMapRender2.renderResume();
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // android.opengl.GLSurfaceView, android.view.SurfaceView, android.view.View
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
        onResume();
    }

    @Override // android.opengl.GLSurfaceView, android.view.SurfaceView, android.view.View
    protected void onDetachedFromWindow() {
        onPause();
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
