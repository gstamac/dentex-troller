package com.amap.api.mapcore.util;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.os.RemoteException;
import android.util.Log;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.amap.mapcore.DPoint;
import com.autonavi.amap.mapcore.FPointBounds;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.interfaces.IOverlay;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: compiled from: GroundOverlayDelegateImp.java */
/* JADX INFO: loaded from: classes.dex */
public class cb implements ce {
    cw.b d;
    private k e;
    private BitmapDescriptor f;
    private LatLng g;
    private float h;
    private float i;
    private LatLngBounds j;
    private float k;
    private float l;
    private String r;
    private FloatBuffer t;
    private int u;
    private boolean m = true;
    private float n = 0.0f;
    private float o = 1.0f;
    private float p = 0.5f;
    private float q = 0.5f;
    private FloatBuffer s = null;
    private boolean v = false;
    private boolean w = false;
    FPointBounds a = null;
    private List<Float> x = new ArrayList();
    private List<Float> y = new ArrayList();
    float[] b = null;
    int c = 10000;

    @Override // com.amap.api.mapcore.util.ch
    public boolean a() {
        return true;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean isAboveMaskLayer() {
        return false;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setAboveMaskLayer(boolean z) {
    }

    public cb(k kVar) {
        this.e = kVar;
        try {
            this.r = getId();
        } catch (RemoteException e) {
            fn.b(e, "GroundOverlayDelegateImp", "create");
            e.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void remove() throws RemoteException {
        this.e.a(getId());
        this.e.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public String getId() throws RemoteException {
        if (this.r == null) {
            this.r = this.e.c("GroundOverlay");
        }
        return this.r;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setZIndex(float f) throws RemoteException {
        this.l = f;
        this.e.f();
        this.e.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public float getZIndex() throws RemoteException {
        return this.l;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setVisible(boolean z) throws RemoteException {
        this.m = z;
        this.e.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean isVisible() throws RemoteException {
        return this.m;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean equalsRemote(IOverlay iOverlay) throws RemoteException {
        return equals(iOverlay) || iOverlay.getId().equals(getId());
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public int hashCodeRemote() throws RemoteException {
        return super.hashCode();
    }

    public boolean b() throws RemoteException {
        this.w = false;
        if (this.g == null) {
            f();
            return true;
        }
        if (this.j == null) {
            e();
            return true;
        }
        g();
        return true;
    }

    private void e() {
        LatLng latLng = this.g;
        if (latLng == null) {
            return;
        }
        double dCos = ((double) this.h) / ((Math.cos(latLng.latitude * 0.01745329251994329d) * 6371000.79d) * 0.01745329251994329d);
        double d = ((double) this.i) / 111194.94043265979d;
        try {
            this.j = new LatLngBounds(new LatLng(this.g.latitude - (((double) (1.0f - this.q)) * d), this.g.longitude - (((double) this.p) * dCos)), new LatLng(this.g.latitude + (((double) this.q) * d), this.g.longitude + (((double) (1.0f - this.p)) * dCos)));
        } catch (Throwable th) {
            th.printStackTrace();
        }
        g();
    }

    private void f() {
        LatLngBounds latLngBounds = this.j;
        if (latLngBounds == null) {
            return;
        }
        LatLng latLng = latLngBounds.southwest;
        LatLng latLng2 = this.j.northeast;
        LatLng latLng3 = new LatLng(latLng.latitude + (((double) (1.0f - this.q)) * (latLng2.latitude - latLng.latitude)), latLng.longitude + (((double) this.p) * (latLng2.longitude - latLng.longitude)));
        this.g = latLng3;
        this.h = (float) (Math.cos(latLng3.latitude * 0.01745329251994329d) * 6371000.79d * (latLng2.longitude - latLng.longitude) * 0.01745329251994329d);
        this.i = (float) ((latLng2.latitude - latLng.latitude) * 6371000.79d * 0.01745329251994329d);
        g();
    }

    private synchronized void g() {
        if (this.j == null) {
            return;
        }
        this.b = new float[16];
        IPoint iPointObtain = IPoint.obtain();
        IPoint iPointObtain2 = IPoint.obtain();
        IPoint iPointObtain3 = IPoint.obtain();
        IPoint iPointObtain4 = IPoint.obtain();
        GLMapState.lonlat2Geo(this.j.southwest.longitude, this.j.southwest.latitude, iPointObtain);
        GLMapState.lonlat2Geo(this.j.northeast.longitude, this.j.southwest.latitude, iPointObtain2);
        GLMapState.lonlat2Geo(this.j.northeast.longitude, this.j.northeast.latitude, iPointObtain3);
        GLMapState.lonlat2Geo(this.j.southwest.longitude, this.j.northeast.latitude, iPointObtain4);
        if (this.k != 0.0f) {
            double d = iPointObtain2.x - iPointObtain.x;
            double d2 = iPointObtain2.y - iPointObtain3.y;
            DPoint dPointObtain = DPoint.obtain();
            dPointObtain.x = ((double) iPointObtain.x) + (((double) this.p) * d);
            dPointObtain.y = ((double) iPointObtain.y) - (((double) (1.0f - this.q)) * d2);
            a(dPointObtain, 0.0d, 0.0d, d, d2, iPointObtain);
            a(dPointObtain, d, 0.0d, d, d2, iPointObtain2);
            a(dPointObtain, d, d2, d, d2, iPointObtain3);
            a(dPointObtain, 0.0d, d2, d, d2, iPointObtain4);
            dPointObtain.recycle();
        }
        this.b[0] = iPointObtain.x / this.c;
        this.b[1] = iPointObtain.y / this.c;
        this.b[2] = iPointObtain.x % this.c;
        this.b[3] = iPointObtain.y % this.c;
        this.b[4] = iPointObtain2.x / this.c;
        this.b[5] = iPointObtain2.y / this.c;
        this.b[6] = iPointObtain2.x % this.c;
        this.b[7] = iPointObtain2.y % this.c;
        this.b[8] = iPointObtain3.x / this.c;
        this.b[9] = iPointObtain3.y / this.c;
        this.b[10] = iPointObtain3.x % this.c;
        this.b[11] = iPointObtain3.y % this.c;
        this.b[12] = iPointObtain4.x / this.c;
        this.b[13] = iPointObtain4.y / this.c;
        this.b[14] = iPointObtain4.x % this.c;
        this.b[15] = iPointObtain4.y % this.c;
        FloatBuffer floatBuffer = this.s;
        if (floatBuffer == null) {
            this.s = dw.a(this.b);
        } else {
            this.s = dw.a(this.b, floatBuffer);
        }
        iPointObtain4.recycle();
        iPointObtain.recycle();
        iPointObtain2.recycle();
        iPointObtain3.recycle();
    }

    private void a(DPoint dPoint, double d, double d2, double d3, double d4, IPoint iPoint) {
        double d5 = d - (d3 * ((double) this.p));
        double d6 = (d4 * ((double) (1.0f - this.q))) - d2;
        double d7 = ((double) (-this.k)) * 0.01745329251994329d;
        iPoint.x = (int) (dPoint.x + (Math.cos(d7) * d5) + (Math.sin(d7) * d6));
        iPoint.y = (int) (dPoint.y + ((d6 * Math.cos(d7)) - (d5 * Math.sin(d7))));
    }

    private void h() {
        BitmapDescriptor bitmapDescriptor = this.f;
        if (bitmapDescriptor == null && (bitmapDescriptor == null || bitmapDescriptor.getBitmap() == null)) {
            return;
        }
        int width = this.f.getWidth();
        int height = this.f.getHeight();
        int height2 = this.f.getBitmap().getHeight();
        float width2 = width / this.f.getBitmap().getWidth();
        float f = height / height2;
        this.t = dw.a(new float[]{0.0f, f, width2, f, width2, 0.0f, 0.0f, 0.0f});
    }

    @Override // com.amap.api.mapcore.util.ch
    public void c() throws RemoteException {
        if (this.m) {
            if ((this.g == null && this.j == null) || this.f == null) {
                return;
            }
            if (this.b == null) {
                b();
            }
            if (!this.v) {
                Bitmap bitmap = this.f.getBitmap();
                if (bitmap != null && !bitmap.isRecycled()) {
                    int i = this.u;
                    if (i == 0) {
                        int[] iArr = {0};
                        GLES20.glGenTextures(1, iArr, 0);
                        this.u = iArr[0];
                    } else {
                        GLES20.glDeleteTextures(1, new int[]{i}, 0);
                    }
                    dw.b(this.u, bitmap, false);
                }
                this.v = true;
            }
            if (this.h == 0.0f && this.i == 0.0f) {
                return;
            }
            a(this.u, this.s, this.t);
            this.w = true;
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void destroy() {
        Bitmap bitmap;
        try {
            remove();
            BitmapDescriptor bitmapDescriptor = this.f;
            if (bitmapDescriptor != null && (bitmap = bitmapDescriptor.getBitmap()) != null) {
                bitmap.recycle();
                this.f = null;
            }
            FloatBuffer floatBuffer = this.t;
            if (floatBuffer != null) {
                floatBuffer.clear();
                this.t = null;
            }
            FloatBuffer floatBuffer2 = this.s;
            if (floatBuffer2 != null) {
                floatBuffer2.clear();
                this.s = null;
            }
            this.g = null;
            this.j = null;
        } catch (Throwable th) {
            fn.b(th, "GroundOverlayDelegateImp", "destroy");
            th.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IGroundOverlay
    public void setPosition(LatLng latLng) throws RemoteException {
        this.g = latLng;
        e();
        this.e.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IGroundOverlay
    public LatLng getPosition() throws RemoteException {
        return this.g;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IGroundOverlay
    public void setDimensions(float f) throws RemoteException {
        if (f <= 0.0f) {
            Log.w("GroundOverlayDelegateImp", "Width must be non-negative");
        }
        if (this.v && this.h != f) {
            this.h = f;
            this.i = f;
            e();
        } else {
            this.h = f;
            this.i = f;
        }
        this.e.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IGroundOverlay
    public void setDimensions(float f, float f2) throws RemoteException {
        if (f <= 0.0f || f2 <= 0.0f) {
            Log.w("GroundOverlayDelegateImp", "Width and Height must be non-negative");
        }
        if (this.v && this.h != f && this.i != f2) {
            this.h = f;
            this.i = f2;
            e();
        } else {
            this.h = f;
            this.i = f2;
        }
        this.e.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IGroundOverlay
    public float getWidth() throws RemoteException {
        return this.h;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IGroundOverlay
    public float getHeight() throws RemoteException {
        return this.i;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IGroundOverlay
    public void setPositionFromBounds(LatLngBounds latLngBounds) throws RemoteException {
        this.j = latLngBounds;
        f();
        this.e.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IGroundOverlay
    public LatLngBounds getBounds() throws RemoteException {
        return this.j;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IGroundOverlay
    public void setBearing(float f) throws RemoteException {
        float f2 = ((f % 360.0f) + 360.0f) % 360.0f;
        if (Math.abs(this.k - f2) > 1.0E-7d) {
            this.k = f2;
            g();
        }
        this.e.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IGroundOverlay
    public float getBearing() throws RemoteException {
        return this.k;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IGroundOverlay
    public void setTransparency(float f) throws RemoteException {
        this.n = (float) Math.min(1.0d, Math.max(0.0d, f));
        this.o = 1.0f - f;
        this.e.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IGroundOverlay
    public float getTransparency() throws RemoteException {
        return this.n;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IGroundOverlay
    public void setImage(BitmapDescriptor bitmapDescriptor) throws RemoteException {
        if (bitmapDescriptor == null || bitmapDescriptor.getBitmap() == null || bitmapDescriptor.getBitmap().isRecycled()) {
            return;
        }
        this.f = bitmapDescriptor;
        h();
        if (this.v) {
            this.v = false;
        }
        this.e.setRunLowFrame(false);
    }

    public void a(float f, float f2) throws RemoteException {
        this.p = f;
        this.q = f2;
        this.e.setRunLowFrame(false);
    }

    @Override // com.amap.api.mapcore.util.ch
    public boolean d() {
        return this.w;
    }

    private void i() {
        k kVar = this.e;
        if (kVar != null) {
            this.d = (cw.b) kVar.s(2);
        }
    }

    private void a(int i, FloatBuffer floatBuffer, FloatBuffer floatBuffer2) {
        if (this.b == null || floatBuffer == null || floatBuffer2 == null) {
            return;
        }
        cw.b bVar = this.d;
        if (bVar == null || bVar.c()) {
            i();
        }
        this.d.a();
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(1, 771);
        float f = this.o;
        GLES20.glBlendColor(f * 1.0f, f * 1.0f, f * 1.0f, f);
        GLES20.glBindTexture(3553, i);
        GLES20.glEnableVertexAttribArray(this.d.b);
        GLES20.glVertexAttribPointer(this.d.b, 4, 5126, false, 16, (Buffer) floatBuffer);
        GLES20.glEnableVertexAttribArray(this.d.c);
        GLES20.glVertexAttribPointer(this.d.c, 2, 5126, false, 8, (Buffer) floatBuffer2);
        GLES20.glUniform4f(this.d.g, this.e.getMapConfig().getS_x() / this.c, this.e.getMapConfig().getS_y() / this.c, this.e.getMapConfig().getS_x() % this.c, this.e.getMapConfig().getS_y() % this.c);
        int i2 = this.d.h;
        float f2 = this.o;
        GLES20.glUniform4f(i2, f2 * 1.0f, f2 * 1.0f, 1.0f * f2, f2);
        GLES20.glUniformMatrix4fv(this.d.a, 1, false, this.e.v(), 0);
        GLES20.glDrawArrays(6, 0, 4);
        GLES20.glBindTexture(3553, 0);
        GLES20.glDisableVertexAttribArray(this.d.b);
        GLES20.glDisableVertexAttribArray(this.d.c);
        GLES20.glDisable(3042);
        GLES20.glUseProgram(0);
    }
}
