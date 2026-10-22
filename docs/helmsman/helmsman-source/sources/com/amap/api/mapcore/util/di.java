package com.amap.api.mapcore.util;

import android.graphics.Color;
import android.opengl.GLES20;
import android.util.Log;
import android.view.SurfaceHolder;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

/* JADX INFO: compiled from: GLESUtility.java */
/* JADX INFO: loaded from: classes.dex */
public class di {
    public static void a(String str) {
        int iGlGetError = GLES20.glGetError();
        if (iGlGetError == 0) {
            return;
        }
        Log.e("amap", str + ": glError " + iGlGetError);
        throw new RuntimeException(str + ": glError " + iGlGetError);
    }

    public static void a(cw.d dVar, int i, int i2, FloatBuffer floatBuffer, float f, FloatBuffer floatBuffer2, int i3, int i4, float[] fArr) {
        a(dVar, 4, i, floatBuffer2, 1.0f, i4, fArr);
        a(dVar, 2, i2, floatBuffer, f, i3, fArr);
    }

    public static void a(cw.d dVar, int i, FloatBuffer floatBuffer, float f, int i2, float[] fArr) {
        a(dVar, 3, i, floatBuffer, f, i2, fArr);
    }

    public static void a(cw.d dVar, int i, int i2, FloatBuffer floatBuffer, float f, int i3, float[] fArr, int i4, int i5) {
        a(dVar, 6, i, floatBuffer, 1.0f, i3, fArr);
        a(dVar, 2, i2, floatBuffer, f, 1, i3 - 1, fArr);
    }

    private static void a(cw.d dVar, int i, int i2, FloatBuffer floatBuffer, float f, int i3, float[] fArr) {
        a(dVar, i, i2, floatBuffer, f, 0, i3, fArr);
    }

    private static void a(cw.d dVar, int i, int i2, FloatBuffer floatBuffer, float f, int i3, int i4, float[] fArr) {
        if (f == 0.0f || dVar == null) {
            return;
        }
        dVar.a();
        GLES20.glEnable(3042);
        GLES20.glDisable(2929);
        GLES20.glBlendFunc(770, 771);
        float[] fArr2 = {Color.red(i2) / 255.0f, Color.green(i2) / 255.0f, Color.blue(i2) / 255.0f, Color.alpha(i2) / 255.0f};
        GLES20.glLineWidth(f);
        GLES20.glEnableVertexAttribArray(dVar.b);
        GLES20.glVertexAttribPointer(dVar.b, 3, 5126, false, 0, (Buffer) floatBuffer);
        GLES20.glUniform4fv(dVar.c, 1, fArr2, 0);
        GLES20.glUniformMatrix4fv(dVar.a, 1, false, fArr, 0);
        GLES20.glDrawArrays(i, i3, i4);
        GLES20.glDisableVertexAttribArray(dVar.b);
        GLES20.glDisable(3042);
        GLES20.glUseProgram(0);
    }

    public static void a(l lVar, int i, int i2, int i3, int i4, int i5, int i6) {
        SurfaceHolder holder;
        if (i4 > 0 && (holder = lVar.getHolder()) != null) {
            holder.setFormat(-3);
        }
        lVar.a(new b());
        lVar.a(new a(i, i2, i3, i4, i5, i6));
    }

    /* JADX INFO: compiled from: GLESUtility.java */
    public static class b extends dk {
        private static int a = 12440;

        @Override // com.amap.api.mapcore.util.dk, android.opengl.GLSurfaceView.EGLContextFactory, com.amap.api.mapcore.util.j.f
        public EGLContext createContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig) {
            try {
                return egl10.eglCreateContext(eGLDisplay, eGLConfig, EGL10.EGL_NO_CONTEXT, new int[]{a, 2, 12344});
            } catch (Throwable th) {
                th.printStackTrace();
                return null;
            }
        }

        @Override // com.amap.api.mapcore.util.dk, android.opengl.GLSurfaceView.EGLContextFactory, com.amap.api.mapcore.util.j.f
        public void destroyContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLContext eGLContext) {
            egl10.eglDestroyContext(eGLDisplay, eGLContext);
        }
    }

    /* JADX INFO: compiled from: GLESUtility.java */
    private static class c {
        public int[] a;
        public int[] b;

        private c() {
            this.a = null;
            this.b = new int[1];
        }
    }

    /* JADX INFO: compiled from: GLESUtility.java */
    public static class a extends dj {
        private static int g = 4;
        protected int a;
        protected int b;
        protected int c;
        protected int d;
        protected int e;
        protected int f;
        private int[] h = new int[1];

        public a(int i, int i2, int i3, int i4, int i5, int i6) {
            this.a = i;
            this.b = i2;
            this.c = i3;
            this.d = i4;
            this.e = i5;
            this.f = i6;
        }

        private int[] a(int i, boolean z) {
            if (i == 2) {
                return new int[]{12324, this.a, 12323, this.b, 12322, this.c, 12321, this.d, 12325, this.e, 12326, this.f, 12338, z ? 1 : 0, 12352, g, 12344};
            }
            return z ? new int[]{12324, this.a, 12323, this.b, 12322, this.c, 12338, 1, 12344} : new int[]{12324, this.a, 12323, this.b, 12322, this.c, 12344};
        }

        private c a(EGL10 egl10, EGLDisplay eGLDisplay) {
            c cVar = new c();
            cVar.a = a(2, true);
            egl10.eglChooseConfig(eGLDisplay, cVar.a, null, 0, cVar.b);
            if (cVar.b[0] <= 0) {
                cVar.a = a(2, false);
                egl10.eglChooseConfig(eGLDisplay, cVar.a, null, 0, cVar.b);
                if (cVar.b[0] <= 0) {
                    return null;
                }
            }
            return cVar;
        }

        @Override // com.amap.api.mapcore.util.dj, android.opengl.GLSurfaceView.EGLConfigChooser, com.amap.api.mapcore.util.j.e
        public EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay) {
            c cVarA = a(egl10, eGLDisplay);
            if (cVarA == null || cVarA.a == null) {
                return null;
            }
            EGLConfig[] eGLConfigArr = new EGLConfig[cVarA.b[0]];
            egl10.eglChooseConfig(eGLDisplay, cVarA.a, eGLConfigArr, cVarA.b[0], cVarA.b);
            EGLConfig eGLConfigA = a(egl10, eGLDisplay, eGLConfigArr);
            if (eGLConfigA != null) {
                return eGLConfigA;
            }
            this.a = 8;
            this.b = 8;
            this.c = 8;
            c cVarA2 = a(egl10, eGLDisplay);
            if (cVarA2 == null || cVarA2.a == null) {
                return eGLConfigA;
            }
            EGLConfig[] eGLConfigArr2 = new EGLConfig[cVarA2.b[0]];
            egl10.eglChooseConfig(eGLDisplay, cVarA2.a, eGLConfigArr2, cVarA2.b[0], cVarA2.b);
            return a(egl10, eGLDisplay, eGLConfigArr2);
        }

        public EGLConfig a(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig[] eGLConfigArr) {
            for (EGLConfig eGLConfig : eGLConfigArr) {
                int iA = a(egl10, eGLDisplay, eGLConfig, 12325, 0);
                int iA2 = a(egl10, eGLDisplay, eGLConfig, 12326, 0);
                if (iA >= this.e && iA2 >= this.f) {
                    int iA3 = a(egl10, eGLDisplay, eGLConfig, 12324, 0);
                    int iA4 = a(egl10, eGLDisplay, eGLConfig, 12323, 0);
                    int iA5 = a(egl10, eGLDisplay, eGLConfig, 12322, 0);
                    int iA6 = a(egl10, eGLDisplay, eGLConfig, 12321, 0);
                    if (iA3 == this.a && iA4 == this.b && iA5 == this.c && iA6 == this.d) {
                        return eGLConfig;
                    }
                }
            }
            return null;
        }

        private int a(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, int i, int i2) {
            return egl10.eglGetConfigAttrib(eGLDisplay, eGLConfig, i, this.h) ? this.h[0] : i2;
        }
    }
}
