package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;
import androidx.core.view.ViewCompat;
import java.io.InputStream;

/* JADX INFO: compiled from: WaterMarkerView.java */
/* JADX INFO: loaded from: classes.dex */
public class el extends View {
    private Bitmap a;
    private Bitmap b;
    private Bitmap c;
    private Bitmap d;
    private Paint e;
    private boolean f;
    private int g;
    private int h;
    private k i;
    private int j;
    private int k;
    private int l;
    private int m;
    private int n;
    private int o;
    private int p;
    private boolean q;
    private float r;
    private float s;
    private boolean t;

    public void a() {
        try {
            Bitmap bitmap = this.a;
            if (bitmap != null) {
                bitmap.recycle();
            }
            Bitmap bitmap2 = this.b;
            if (bitmap2 != null) {
                bitmap2.recycle();
            }
            this.a = null;
            this.b = null;
            Bitmap bitmap3 = this.c;
            if (bitmap3 != null) {
                bitmap3.recycle();
                this.c = null;
            }
            Bitmap bitmap4 = this.d;
            if (bitmap4 != null) {
                bitmap4.recycle();
                this.d = null;
            }
            this.e = null;
        } catch (Throwable th) {
            fn.b(th, "WaterMarkerView", "destory");
            th.printStackTrace();
        }
    }

    public el(Context context, k kVar) {
        InputStream inputStream;
        super(context);
        this.e = new Paint();
        this.f = false;
        this.g = 0;
        this.h = 0;
        this.j = 0;
        this.k = 10;
        this.l = 0;
        this.m = 0;
        this.n = 10;
        this.o = 8;
        this.p = 0;
        this.q = false;
        this.r = 0.0f;
        this.s = 0.0f;
        this.t = true;
        this.i = kVar;
        InputStream inputStreamOpen = null;
        try {
            try {
                InputStream inputStreamOpen2 = ds.a(context).open("ap.data");
                try {
                    Bitmap bitmapDecodeStream = BitmapFactory.decodeStream(inputStreamOpen2);
                    this.c = bitmapDecodeStream;
                    this.a = dw.a(bitmapDecodeStream, g.a);
                    inputStreamOpen2.close();
                    inputStreamOpen = ds.a(context).open("ap1.data");
                    Bitmap bitmapDecodeStream2 = BitmapFactory.decodeStream(inputStreamOpen);
                    this.d = bitmapDecodeStream2;
                    this.b = dw.a(bitmapDecodeStream2, g.a);
                    inputStreamOpen.close();
                    this.h = this.b.getWidth();
                    this.g = this.b.getHeight();
                    this.e.setAntiAlias(true);
                    this.e.setColor(ViewCompat.MEASURED_STATE_MASK);
                    this.e.setStyle(Paint.Style.STROKE);
                    if (inputStreamOpen2 != null) {
                        try {
                            inputStreamOpen2.close();
                        } catch (Throwable th) {
                            th.printStackTrace();
                        }
                    }
                    if (inputStreamOpen != null) {
                        inputStreamOpen.close();
                    }
                } catch (Throwable th2) {
                    th = th2;
                    inputStream = inputStreamOpen;
                    inputStreamOpen = inputStreamOpen2;
                    try {
                        fn.b(th, "WaterMarkerView", "create");
                        th.printStackTrace();
                        if (inputStreamOpen != null) {
                            try {
                                inputStreamOpen.close();
                            } catch (Throwable th3) {
                                th3.printStackTrace();
                            }
                        }
                        if (inputStream == null) {
                        }
                    } finally {
                        if (inputStreamOpen != null) {
                            try {
                                inputStreamOpen.close();
                            } catch (Throwable th4) {
                                th4.printStackTrace();
                            }
                        }
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (Throwable th5) {
                                th5.printStackTrace();
                            }
                        }
                    }
                }
            } catch (Throwable th6) {
            }
        } catch (Throwable th7) {
            th = th7;
            inputStream = null;
        }
    }

    public Bitmap b() {
        if (this.f) {
            return this.b;
        }
        return this.a;
    }

    public Point c() {
        return new Point(this.k, this.l - 2);
    }

    public void a(int i) {
        this.m = 0;
        this.j = i;
        d();
    }

    public void b(int i) {
        this.m = 1;
        this.o = i;
        d();
    }

    public void c(int i) {
        this.m = 1;
        this.n = i;
        d();
    }

    public float d(int i) {
        float f;
        if (i == 0) {
            return this.r;
        }
        if (i == 1) {
            f = this.r;
        } else {
            if (i != 2) {
                return 0.0f;
            }
            f = this.s;
        }
        return 1.0f - f;
    }

    public void a(int i, float f) {
        this.m = 2;
        this.p = i;
        float fMax = Math.max(0.0f, Math.min(f, 1.0f));
        if (i == 0) {
            this.r = fMax;
            this.t = true;
        } else if (i == 1) {
            this.r = 1.0f - fMax;
            this.t = false;
        } else if (i == 2) {
            this.s = 1.0f - fMax;
        }
        d();
    }

    public void d() {
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        e();
        postInvalidate();
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        try {
            if (getWidth() == 0 || getHeight() == 0 || this.b == null) {
                return;
            }
            if (!this.q) {
                e();
                this.q = true;
            }
            canvas.drawBitmap(b(), this.k, this.l, this.e);
        } catch (Throwable th) {
            fn.b(th, "WaterMarkerView", "onDraw");
            th.printStackTrace();
        }
    }

    private void e() {
        int i = this.m;
        if (i == 0) {
            g();
        } else if (i == 2) {
            f();
        }
        this.k = this.n;
        int height = (getHeight() - this.o) - this.g;
        this.l = height;
        if (this.k < 0) {
            this.k = 0;
        }
        if (height < 0) {
            this.l = 0;
        }
    }

    private void f() {
        if (this.t) {
            this.n = (int) (getWidth() * this.r);
        } else {
            this.n = (int) ((getWidth() * this.r) - this.h);
        }
        this.o = (int) (getHeight() * this.s);
    }

    private void g() {
        int i = this.j;
        if (i == 1) {
            this.n = (getWidth() - this.h) / 2;
        } else if (i == 2) {
            this.n = (getWidth() - this.h) - 10;
        } else {
            this.n = 10;
        }
        this.o = 8;
    }
}
