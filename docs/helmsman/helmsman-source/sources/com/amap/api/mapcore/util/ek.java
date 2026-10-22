package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import androidx.core.view.ViewCompat;
import com.MAVLink.enums.MAV_CMD;
import com.autonavi.amap.mapcore.DPoint;
import com.autonavi.amap.mapcore.VirtualEarthProjection;
import com.google.android.gms.common.util.GmsVersion;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import kotlin.time.DurationKt;

/* JADX INFO: compiled from: ScaleView.java */
/* JADX INFO: loaded from: classes.dex */
public class ek extends View {
    private String a;
    private int b;
    private k c;
    private Paint d;
    private Paint e;
    private Rect f;
    private Point g;
    private float h;
    private final int[] i;

    public void a() {
        this.d = null;
        this.e = null;
        this.f = null;
        this.a = null;
    }

    public ek(Context context, k kVar) {
        super(context);
        this.a = "";
        this.b = 0;
        this.h = 0.0f;
        this.i = new int[]{10000000, GmsVersion.VERSION_LONGHORN, 2000000, DurationKt.NANOS_IN_MILLIS, 500000, 200000, 100000, 50000, 30000, 20000, 10000, BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT, MAV_CMD.MAV_CMD_IMAGE_START_CAPTURE, 1000, 500, 200, 100, 50, 25, 10, 5};
        this.c = kVar;
        this.d = new Paint();
        this.f = new Rect();
        this.d.setAntiAlias(true);
        this.d.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.d.setStrokeWidth(g.a * 2.0f);
        this.d.setStyle(Paint.Style.STROKE);
        Paint paint = new Paint();
        this.e = paint;
        paint.setAntiAlias(true);
        this.e.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.e.setTextSize(g.a * 20.0f);
        this.h = ds.a(context, 1.0f);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        Point pointK;
        String str = this.a;
        if (str == null || str.equals("") || this.b == 0 || (pointK = this.c.k()) == null) {
            return;
        }
        Paint paint = this.e;
        String str2 = this.a;
        paint.getTextBounds(str2, 0, str2.length(), this.f);
        int i = pointK.x;
        int iHeight = (pointK.y - this.f.height()) + 5;
        canvas.drawText(this.a, ((this.b - this.f.width()) / 2) + i, iHeight, this.e);
        float f = i;
        float fHeight = iHeight + (this.f.height() - 5);
        canvas.drawLine(f, fHeight - (this.h * 2.0f), f, fHeight + g.a, this.d);
        canvas.drawLine(f, fHeight, this.b + i, fHeight, this.d);
        int i2 = this.b;
        canvas.drawLine(i + i2, fHeight - (this.h * 2.0f), i + i2, fHeight + g.a, this.d);
    }

    public void a(String str) {
        this.a = str;
    }

    public void a(int i) {
        this.b = i;
    }

    public void a(boolean z) {
        if (z) {
            setVisibility(0);
            b();
        } else {
            a("");
            a(0);
            setVisibility(8);
        }
    }

    public void b() {
        k kVar = this.c;
        if (kVar == null) {
            return;
        }
        try {
            float fA = kVar.a(1);
            Point pointN = this.c.n(1);
            this.g = pointN;
            if (pointN == null) {
                return;
            }
            DPoint dPointPixelsToLatLong = VirtualEarthProjection.PixelsToLatLong(pointN.x, this.g.y, 20);
            float fS = this.c.s();
            double dCos = (float) ((((Math.cos((dPointPixelsToLatLong.y * 3.141592653589793d) / 180.0d) * 2.0d) * 3.141592653589793d) * 6378137.0d) / (Math.pow(2.0d, fA) * 256.0d));
            int i = this.i[(int) fA];
            int i2 = (int) (((double) i) / (dCos * ((double) fS)));
            String strA = dw.a(i);
            a(i2);
            a(strA);
            dPointPixelsToLatLong.recycle();
            invalidate();
        } catch (Throwable th) {
            fn.b(th, "AMapDelegateImpGLSurfaceView", "changeScaleState");
            th.printStackTrace();
        }
    }
}
