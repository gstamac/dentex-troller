package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.RemoteException;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/* JADX INFO: compiled from: ZoomControllerView.java */
/* JADX INFO: loaded from: classes.dex */
class em extends LinearLayout {
    private Bitmap a;
    private Bitmap b;
    private Bitmap c;
    private Bitmap d;
    private Bitmap e;
    private Bitmap f;
    private Bitmap g;
    private Bitmap h;
    private Bitmap i;
    private Bitmap j;
    private Bitmap k;
    private Bitmap l;
    private ImageView m;
    private ImageView n;
    private k o;

    public void a() {
        try {
            removeAllViews();
            this.a.recycle();
            this.b.recycle();
            this.c.recycle();
            this.d.recycle();
            this.e.recycle();
            this.f.recycle();
            this.a = null;
            this.b = null;
            this.c = null;
            this.d = null;
            this.e = null;
            this.f = null;
            Bitmap bitmap = this.g;
            if (bitmap != null) {
                bitmap.recycle();
                this.g = null;
            }
            Bitmap bitmap2 = this.h;
            if (bitmap2 != null) {
                bitmap2.recycle();
                this.h = null;
            }
            Bitmap bitmap3 = this.i;
            if (bitmap3 != null) {
                bitmap3.recycle();
                this.i = null;
            }
            Bitmap bitmap4 = this.j;
            if (bitmap4 != null) {
                bitmap4.recycle();
                this.g = null;
            }
            Bitmap bitmap5 = this.k;
            if (bitmap5 != null) {
                bitmap5.recycle();
                this.k = null;
            }
            Bitmap bitmap6 = this.l;
            if (bitmap6 != null) {
                bitmap6.recycle();
                this.l = null;
            }
            this.m = null;
            this.n = null;
        } catch (Throwable th) {
            fn.b(th, "ZoomControllerView", "destory");
            th.printStackTrace();
        }
    }

    public em(Context context, k kVar) {
        super(context);
        this.o = kVar;
        try {
            Bitmap bitmapA = dw.a(context, "zoomin_selected.png");
            this.g = bitmapA;
            this.a = dw.a(bitmapA, g.a);
            Bitmap bitmapA2 = dw.a(context, "zoomin_unselected.png");
            this.h = bitmapA2;
            this.b = dw.a(bitmapA2, g.a);
            Bitmap bitmapA3 = dw.a(context, "zoomout_selected.png");
            this.i = bitmapA3;
            this.c = dw.a(bitmapA3, g.a);
            Bitmap bitmapA4 = dw.a(context, "zoomout_unselected.png");
            this.j = bitmapA4;
            this.d = dw.a(bitmapA4, g.a);
            Bitmap bitmapA5 = dw.a(context, "zoomin_pressed.png");
            this.k = bitmapA5;
            this.e = dw.a(bitmapA5, g.a);
            Bitmap bitmapA6 = dw.a(context, "zoomout_pressed.png");
            this.l = bitmapA6;
            this.f = dw.a(bitmapA6, g.a);
            ImageView imageView = new ImageView(context);
            this.m = imageView;
            imageView.setImageBitmap(this.a);
            this.m.setClickable(true);
            ImageView imageView2 = new ImageView(context);
            this.n = imageView2;
            imageView2.setImageBitmap(this.c);
            this.n.setClickable(true);
            this.m.setOnTouchListener(new View.OnTouchListener() { // from class: com.amap.api.mapcore.util.em.1
                @Override // android.view.View.OnTouchListener
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    try {
                        if (em.this.o.g() < em.this.o.getMaxZoomLevel() && em.this.o.isMaploaded()) {
                            if (motionEvent.getAction() == 0) {
                                em.this.m.setImageBitmap(em.this.e);
                            } else if (motionEvent.getAction() == 1) {
                                em.this.m.setImageBitmap(em.this.a);
                                try {
                                    em.this.o.b(z.a());
                                } catch (RemoteException e) {
                                    fn.b(e, "ZoomControllerView", "zoomin ontouch");
                                    e.printStackTrace();
                                }
                            }
                            return false;
                        }
                        return false;
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            });
            this.n.setOnTouchListener(new View.OnTouchListener() { // from class: com.amap.api.mapcore.util.em.2
                @Override // android.view.View.OnTouchListener
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    try {
                        if (em.this.o.g() > em.this.o.getMinZoomLevel() && em.this.o.isMaploaded()) {
                            if (motionEvent.getAction() == 0) {
                                em.this.n.setImageBitmap(em.this.f);
                            } else if (motionEvent.getAction() == 1) {
                                em.this.n.setImageBitmap(em.this.c);
                                em.this.o.b(z.b());
                            }
                            return false;
                        }
                        return false;
                    } catch (Throwable th) {
                        fn.b(th, "ZoomControllerView", "zoomout ontouch");
                        th.printStackTrace();
                    }
                }
            });
            this.m.setPadding(0, 0, 20, -2);
            this.n.setPadding(0, 0, 20, 20);
            setOrientation(1);
            addView(this.m);
            addView(this.n);
        } catch (Throwable th) {
            fn.b(th, "ZoomControllerView", "create");
            th.printStackTrace();
        }
    }

    public void a(float f) {
        try {
            if (f < this.o.getMaxZoomLevel() && f > this.o.getMinZoomLevel()) {
                this.m.setImageBitmap(this.a);
                this.n.setImageBitmap(this.c);
            } else if (f == this.o.getMinZoomLevel()) {
                this.n.setImageBitmap(this.d);
                this.m.setImageBitmap(this.a);
            } else if (f == this.o.getMaxZoomLevel()) {
                this.m.setImageBitmap(this.b);
                this.n.setImageBitmap(this.c);
            }
        } catch (Throwable th) {
            fn.b(th, "ZoomControllerView", "setZoomBitmap");
            th.printStackTrace();
        }
    }

    public void a(int i) {
        try {
            ej.a aVar = (ej.a) getLayoutParams();
            if (i == 1) {
                aVar.d = 16;
            } else if (i == 2) {
                aVar.d = 80;
            }
            setLayoutParams(aVar);
        } catch (Throwable th) {
            fn.b(th, "ZoomControllerView", "setZoomPosition");
            th.printStackTrace();
        }
    }

    public void a(boolean z) {
        if (z) {
            setVisibility(0);
        } else {
            setVisibility(8);
        }
    }
}
