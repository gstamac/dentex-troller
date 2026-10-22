package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.amap.api.maps.model.CameraPosition;

/* JADX INFO: compiled from: CompassView.java */
/* JADX INFO: loaded from: classes.dex */
public class eg extends LinearLayout {
    Bitmap a;
    Bitmap b;
    Bitmap c;
    ImageView d;
    k e;
    Matrix f;

    public void a() {
        try {
            removeAllViews();
            Bitmap bitmap = this.a;
            if (bitmap != null) {
                bitmap.recycle();
            }
            Bitmap bitmap2 = this.b;
            if (bitmap2 != null) {
                bitmap2.recycle();
            }
            Bitmap bitmap3 = this.c;
            if (bitmap3 != null) {
                bitmap3.recycle();
            }
            Matrix matrix = this.f;
            if (matrix != null) {
                matrix.reset();
                this.f = null;
            }
            this.c = null;
            this.a = null;
            this.b = null;
        } catch (Throwable th) {
            fn.b(th, "CompassView", "destroy");
            th.printStackTrace();
        }
    }

    public eg(Context context, k kVar) {
        super(context);
        this.f = new Matrix();
        this.e = kVar;
        try {
            Bitmap bitmapA = dw.a(context, "maps_dav_compass_needle_large.png");
            this.c = bitmapA;
            this.b = dw.a(bitmapA, g.a * 0.8f);
            Bitmap bitmapA2 = dw.a(this.c, g.a * 0.7f);
            this.c = bitmapA2;
            Bitmap bitmap = this.b;
            if (bitmap == null && bitmapA2 == null) {
                return;
            }
            this.a = Bitmap.createBitmap(bitmap.getWidth(), this.b.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(this.a);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            canvas.drawBitmap(this.c, (this.b.getWidth() - this.c.getWidth()) / 2.0f, (this.b.getHeight() - this.c.getHeight()) / 2.0f, paint);
            ImageView imageView = new ImageView(context);
            this.d = imageView;
            imageView.setScaleType(ImageView.ScaleType.MATRIX);
            this.d.setImageBitmap(this.a);
            this.d.setClickable(true);
            b();
            this.d.setOnTouchListener(new View.OnTouchListener() { // from class: com.amap.api.mapcore.util.eg.1
                @Override // android.view.View.OnTouchListener
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    try {
                        if (!eg.this.e.isMaploaded()) {
                            return false;
                        }
                        if (motionEvent.getAction() == 0) {
                            eg.this.d.setImageBitmap(eg.this.b);
                        } else if (motionEvent.getAction() == 1) {
                            eg.this.d.setImageBitmap(eg.this.a);
                            CameraPosition cameraPosition = eg.this.e.getCameraPosition();
                            eg.this.e.b(z.a(new CameraPosition(cameraPosition.target, cameraPosition.zoom, 0.0f, 0.0f)));
                        }
                    } catch (Throwable th) {
                        fn.b(th, "CompassView", "onTouch");
                        th.printStackTrace();
                    }
                    return false;
                }
            });
            addView(this.d);
        } catch (Throwable th) {
            fn.b(th, "CompassView", "create");
            th.printStackTrace();
        }
    }

    public void b() {
        try {
            k kVar = this.e;
            if (kVar == null || this.d == null) {
                return;
            }
            float fO = kVar.o(1);
            float fM = this.e.m(1);
            if (this.f == null) {
                this.f = new Matrix();
            }
            this.f.reset();
            this.f.postRotate(-fM, this.d.getDrawable().getBounds().width() / 2.0f, this.d.getDrawable().getBounds().height() / 2.0f);
            this.f.postScale(1.0f, (float) Math.cos((((double) fO) * 3.141592653589793d) / 180.0d), this.d.getDrawable().getBounds().width() / 2.0f, this.d.getDrawable().getBounds().height() / 2.0f);
            this.d.setImageMatrix(this.f);
        } catch (Throwable th) {
            fn.b(th, "CompassView", "invalidateAngle");
            th.printStackTrace();
        }
    }

    public void a(boolean z) {
        if (z) {
            setVisibility(0);
            b();
        } else {
            setVisibility(8);
        }
    }
}
