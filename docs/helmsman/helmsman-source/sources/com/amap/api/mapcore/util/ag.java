package com.amap.api.mapcore.util;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;
import com.amap.api.maps.model.BitmapDescriptor;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/* JADX INFO: compiled from: FakeInstanceMultiPoint.java */
/* JADX INFO: loaded from: classes.dex */
public class ag {
    public static int a = 200;
    float[] b;
    private BitmapDescriptor h;
    private FloatBuffer i;
    private FloatBuffer j;
    private ShortBuffer k;
    private cw.a m;
    private cw n;
    private int e = 6;
    private boolean f = false;
    private boolean g = false;
    private int l = 0;
    int c = 0;
    int d = 0;

    public ag(float[] fArr, ai aiVar) {
        this.b = fArr;
    }

    private void a(float[] fArr) {
        if (fArr == null) {
            return;
        }
        if (this.i == null) {
            ByteBuffer byteBufferAllocateDirect = ByteBuffer.allocateDirect(fArr.length * a * 4);
            byteBufferAllocateDirect.order(ByteOrder.nativeOrder());
            this.i = byteBufferAllocateDirect.asFloatBuffer();
        }
        this.i.clear();
        for (int i = 0; i < a; i++) {
            int i2 = 0;
            for (float f : fArr) {
                if (i2 % this.e == 3) {
                    this.i.put(i);
                } else {
                    this.i.put(f);
                }
                i2++;
            }
        }
        this.i.position(0);
        if (this.k == null) {
            ByteBuffer byteBufferAllocateDirect2 = ByteBuffer.allocateDirect(a * 6 * 2);
            byteBufferAllocateDirect2.order(ByteOrder.nativeOrder());
            this.k = byteBufferAllocateDirect2.asShortBuffer();
            short[] sArr = new short[a * 6];
            for (int i3 = 0; i3 < a; i3++) {
                int i4 = i3 * 6;
                int i5 = i3 * 4;
                short s = (short) (i5 + 0);
                sArr[i4 + 0] = s;
                sArr[i4 + 1] = (short) (i5 + 1);
                short s2 = (short) (i5 + 2);
                sArr[i4 + 2] = s2;
                sArr[i4 + 3] = s;
                sArr[i4 + 4] = s2;
                sArr[i4 + 5] = (short) (i5 + 3);
            }
            this.k.put(sArr);
            this.k.flip();
        }
        this.f = true;
    }

    public void a() {
        float[] fArr = this.b;
        if (fArr == null || this.f) {
            return;
        }
        a(fArr);
    }

    public void a(BitmapDescriptor bitmapDescriptor) {
        this.h = bitmapDescriptor;
    }

    private void a(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        if (this.l == 0) {
            int[] iArr = new int[1];
            GLES20.glGenTextures(1, iArr, 0);
            this.l = iArr[0];
        }
        int i = this.l;
        if (i == 0) {
            return;
        }
        GLES20.glBindTexture(3553, i);
        GLES20.glTexParameterf(3553, 10241, 9728.0f);
        GLES20.glTexParameterf(3553, 10240, 9729.0f);
        GLES20.glTexParameterf(3553, 10242, 33071.0f);
        GLES20.glTexParameterf(3553, 10243, 33071.0f);
        GLUtils.texImage2D(3553, 0, bitmap, 0);
        if (this.l != 0) {
            this.g = true;
        }
    }

    public boolean b() {
        return this.f;
    }

    public void a(float[] fArr, float[] fArr2, float[] fArr3, float f, float f2, float f3, float f4, int i) {
        BitmapDescriptor bitmapDescriptor;
        if (!this.g && (bitmapDescriptor = this.h) != null) {
            a(bitmapDescriptor.getBitmap());
        }
        if (this.l == 0) {
            return;
        }
        cw.a aVar = this.m;
        if (aVar == null || aVar.c()) {
            e();
        }
        d();
        GLES20.glUseProgram(this.m.d);
        GLES20.glUniform4f(this.m.j, f, f2, f3, f4);
        GLES20.glUniform3fv(this.m.i, i, fArr3, 0);
        GLES20.glDisable(2929);
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(770, 771);
        GLES20.glBlendColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glBindTexture(3553, this.l);
        GLES20.glEnableVertexAttribArray(this.m.c);
        GLES20.glBindBuffer(34962, this.c);
        GLES20.glVertexAttribPointer(this.m.c, 4, 5126, false, this.e * 4, 0);
        GLES20.glEnableVertexAttribArray(this.m.h);
        GLES20.glVertexAttribPointer(this.m.h, 2, 5126, false, this.e * 4, 16);
        GLES20.glUniformMatrix4fv(this.m.g, 1, false, fArr, 0);
        GLES20.glUniformMatrix4fv(this.m.k, 1, false, fArr2, 0);
        GLES20.glBindBuffer(34963, this.d);
        GLES20.glDrawElements(4, i * 6, 5123, 0);
        GLES20.glBindTexture(3553, 0);
        GLES20.glBindBuffer(34962, 0);
        GLES20.glDisableVertexAttribArray(this.m.c);
        GLES20.glDisableVertexAttribArray(this.m.h);
        GLES20.glUseProgram(0);
    }

    private void d() {
        if (this.c == 0) {
            int[] iArr = new int[2];
            GLES20.glGenBuffers(2, iArr, 0);
            int i = iArr[0];
            this.c = i;
            this.d = iArr[1];
            GLES20.glBindBuffer(34962, i);
            GLES20.glBufferData(34962, this.i.limit() * 4, this.i, 35044);
            GLES20.glBindBuffer(34963, this.d);
            GLES20.glBufferData(34963, a * 6 * 2, this.k, 35044);
            a("bindVbo");
            this.i.clear();
            this.i = null;
        }
    }

    public void c() {
        FloatBuffer floatBuffer = this.i;
        if (floatBuffer != null) {
            floatBuffer.clear();
        }
        FloatBuffer floatBuffer2 = this.j;
        if (floatBuffer2 != null) {
            floatBuffer2.clear();
        }
        ShortBuffer shortBuffer = this.k;
        if (shortBuffer != null) {
            shortBuffer.clear();
        }
        BitmapDescriptor bitmapDescriptor = this.h;
        if (bitmapDescriptor != null) {
            bitmapDescriptor.recycle();
        }
        GLES20.glDeleteBuffers(2, new int[]{this.c, this.d}, 0);
        int i = this.l;
        if (i != 0) {
            GLES20.glDeleteTextures(1, new int[]{i}, 0);
        }
        this.b = null;
        this.f = false;
        this.g = false;
        this.c = 0;
        this.d = 0;
        this.n = null;
    }

    public void a(cw cwVar) {
        this.n = cwVar;
    }

    private void e() {
        try {
            cw cwVar = this.n;
            if (cwVar != null) {
                this.m = (cw.a) cwVar.a(4);
            }
        } catch (Throwable unused) {
            a = 1;
            cw cwVar2 = this.n;
            if (cwVar2 != null) {
                this.m = (cw.a) cwVar2.a(4);
            }
        }
    }

    public static synchronized void a(String str) {
        int iGlGetError = GLES20.glGetError();
        if (iGlGetError != 0) {
            Log.e("amap", str + ": glError " + iGlGetError);
            throw new RuntimeException(str + ": glError " + iGlGetError);
        }
    }
}
