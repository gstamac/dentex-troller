package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLDebugHelper;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

/* JADX INFO: compiled from: GLTextureView.java */
/* JADX INFO: loaded from: classes.dex */
public class j extends TextureView implements TextureView.SurfaceTextureListener {
    private static final C0017j a = new C0017j();
    private final WeakReference<j> b;
    private i c;
    private GLSurfaceView.Renderer d;
    private boolean e;
    private e f;
    private f g;
    private g h;
    private k i;
    private int j;
    private int k;
    private boolean l;

    /* JADX INFO: compiled from: GLTextureView.java */
    public interface e {
        EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay);
    }

    /* JADX INFO: compiled from: GLTextureView.java */
    public interface f {
        EGLContext createContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig);

        void destroyContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLContext eGLContext);
    }

    /* JADX INFO: compiled from: GLTextureView.java */
    public interface g {
        EGLSurface a(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, Object obj);

        void a(EGL10 egl10, EGLDisplay eGLDisplay, EGLSurface eGLSurface);
    }

    /* JADX INFO: compiled from: GLTextureView.java */
    public interface k {
        GL a(GL gl);
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    public j(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.b = new WeakReference<>(this);
        a();
    }

    protected void finalize() throws Throwable {
        try {
            i iVar = this.c;
            if (iVar != null) {
                iVar.h();
            }
        } finally {
            super.finalize();
        }
    }

    private void a() {
        setSurfaceTextureListener(this);
    }

    public void setRenderer(GLSurfaceView.Renderer renderer) {
        e();
        if (this.f == null) {
            this.f = new m(true);
        }
        if (this.g == null) {
            this.g = new c();
        }
        if (this.h == null) {
            this.h = new d();
        }
        this.d = renderer;
        i iVar = new i(this.b);
        this.c = iVar;
        iVar.start();
    }

    public void a(f fVar) {
        e();
        this.g = fVar;
    }

    public void a(e eVar) {
        e();
        this.f = eVar;
    }

    public void setRenderMode(int i2) {
        this.c.a(i2);
    }

    public void requestRender() {
        this.c.c();
    }

    public int getRenderMode() {
        return this.c.b();
    }

    public void b() {
        this.c.f();
    }

    public void c() {
        this.c.g();
    }

    public void queueEvent(Runnable runnable) {
        this.c.a(runnable);
    }

    @Override // android.view.TextureView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.e && this.d != null) {
            i iVar = this.c;
            int iB = iVar != null ? iVar.b() : 1;
            i iVar2 = new i(this.b);
            this.c = iVar2;
            if (iB != 1) {
                iVar2.a(iB);
            }
            this.c.start();
        }
        this.e = false;
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        i iVar = this.c;
        if (iVar != null) {
            iVar.h();
        }
        this.e = true;
        super.onDetachedFromWindow();
    }

    /* JADX INFO: compiled from: GLTextureView.java */
    private class c implements f {
        private int b;

        private c() {
            this.b = 12440;
        }

        @Override // com.amap.api.mapcore.util.j.f
        public EGLContext createContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig) {
            int[] iArr = {this.b, j.this.k, 12344};
            EGLContext eGLContext = EGL10.EGL_NO_CONTEXT;
            if (j.this.k == 0) {
                iArr = null;
            }
            return egl10.eglCreateContext(eGLDisplay, eGLConfig, eGLContext, iArr);
        }

        @Override // com.amap.api.mapcore.util.j.f
        public void destroyContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLContext eGLContext) {
            if (egl10.eglDestroyContext(eGLDisplay, eGLContext)) {
                return;
            }
            Log.e("DefaultContextFactory", "display:" + eGLDisplay + " context: " + eGLContext);
            h.a("eglDestroyContex", egl10.eglGetError());
        }
    }

    /* JADX INFO: compiled from: GLTextureView.java */
    private static class d implements g {
        private d() {
        }

        @Override // com.amap.api.mapcore.util.j.g
        public EGLSurface a(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, Object obj) {
            try {
                return egl10.eglCreateWindowSurface(eGLDisplay, eGLConfig, obj, null);
            } catch (IllegalArgumentException e) {
                Log.e("GLSurfaceView", "eglCreateWindowSurface", e);
                return null;
            }
        }

        @Override // com.amap.api.mapcore.util.j.g
        public void a(EGL10 egl10, EGLDisplay eGLDisplay, EGLSurface eGLSurface) {
            egl10.eglDestroySurface(eGLDisplay, eGLSurface);
        }
    }

    /* JADX INFO: compiled from: GLTextureView.java */
    private abstract class a implements e {
        protected int[] a;

        abstract EGLConfig a(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig[] eGLConfigArr);

        public a(int[] iArr) {
            this.a = a(iArr);
        }

        @Override // com.amap.api.mapcore.util.j.e
        public EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay) {
            int[] iArr = new int[1];
            if (!egl10.eglChooseConfig(eGLDisplay, this.a, null, 0, iArr)) {
                throw new IllegalArgumentException("eglChooseConfig failed");
            }
            int i = iArr[0];
            if (i <= 0) {
                throw new IllegalArgumentException("No configs match configSpec");
            }
            EGLConfig[] eGLConfigArr = new EGLConfig[i];
            if (!egl10.eglChooseConfig(eGLDisplay, this.a, eGLConfigArr, i, iArr)) {
                throw new IllegalArgumentException("eglChooseConfig#2 failed");
            }
            EGLConfig eGLConfigA = a(egl10, eGLDisplay, eGLConfigArr);
            if (eGLConfigA != null) {
                return eGLConfigA;
            }
            throw new IllegalArgumentException("No config chosen");
        }

        private int[] a(int[] iArr) {
            if (j.this.k != 2 && j.this.k != 3) {
                return iArr;
            }
            int length = iArr.length;
            int[] iArr2 = new int[length + 2];
            int i = length - 1;
            System.arraycopy(iArr, 0, iArr2, 0, i);
            iArr2[i] = 12352;
            if (j.this.k == 2) {
                iArr2[length] = 4;
            } else {
                iArr2[length] = 64;
            }
            iArr2[length + 1] = 12344;
            return iArr2;
        }
    }

    /* JADX INFO: compiled from: GLTextureView.java */
    private class b extends a {
        protected int c;
        protected int d;
        protected int e;
        protected int f;
        protected int g;
        protected int h;
        private int[] j;

        public b(int i, int i2, int i3, int i4, int i5, int i6) {
            super(new int[]{12324, i, 12323, i2, 12322, i3, 12321, i4, 12325, i5, 12326, i6, 12344});
            this.j = new int[1];
            this.c = i;
            this.d = i2;
            this.e = i3;
            this.f = i4;
            this.g = i5;
            this.h = i6;
        }

        @Override // com.amap.api.mapcore.util.j.a
        public EGLConfig a(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig[] eGLConfigArr) {
            for (EGLConfig eGLConfig : eGLConfigArr) {
                int iA = a(egl10, eGLDisplay, eGLConfig, 12325, 0);
                int iA2 = a(egl10, eGLDisplay, eGLConfig, 12326, 0);
                if (iA >= this.g && iA2 >= this.h) {
                    int iA3 = a(egl10, eGLDisplay, eGLConfig, 12324, 0);
                    int iA4 = a(egl10, eGLDisplay, eGLConfig, 12323, 0);
                    int iA5 = a(egl10, eGLDisplay, eGLConfig, 12322, 0);
                    int iA6 = a(egl10, eGLDisplay, eGLConfig, 12321, 0);
                    if (iA3 == this.c && iA4 == this.d && iA5 == this.e && iA6 == this.f) {
                        return eGLConfig;
                    }
                }
            }
            return null;
        }

        private int a(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, int i, int i2) {
            return egl10.eglGetConfigAttrib(eGLDisplay, eGLConfig, i, this.j) ? this.j[0] : i2;
        }
    }

    /* JADX INFO: compiled from: GLTextureView.java */
    private class m extends b {
        public m(boolean z) {
            super(8, 8, 8, 0, z ? 16 : 0, 0);
        }
    }

    /* JADX INFO: compiled from: GLTextureView.java */
    private static class h {
        EGL10 a;
        EGLDisplay b;
        EGLSurface c;
        EGLConfig d;
        EGLContext e;
        private WeakReference<j> f;

        public h(WeakReference<j> weakReference) {
            this.f = weakReference;
        }

        public void a() {
            EGL10 egl10 = (EGL10) EGLContext.getEGL();
            this.a = egl10;
            EGLDisplay eGLDisplayEglGetDisplay = egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
            this.b = eGLDisplayEglGetDisplay;
            if (eGLDisplayEglGetDisplay == EGL10.EGL_NO_DISPLAY) {
                throw new RuntimeException("eglGetDisplay failed");
            }
            if (!this.a.eglInitialize(this.b, new int[2])) {
                throw new RuntimeException("eglInitialize failed");
            }
            j jVar = this.f.get();
            if (jVar != null) {
                this.d = jVar.f.chooseConfig(this.a, this.b);
                this.e = jVar.g.createContext(this.a, this.b, this.d);
            } else {
                this.d = null;
                this.e = null;
            }
            EGLContext eGLContext = this.e;
            if (eGLContext == null || eGLContext == EGL10.EGL_NO_CONTEXT) {
                this.e = null;
                a("createContext");
            }
            this.c = null;
        }

        public boolean b() {
            if (this.a == null) {
                throw new RuntimeException("egl not initialized");
            }
            if (this.b == null) {
                throw new RuntimeException("eglDisplay not initialized");
            }
            if (this.d == null) {
                throw new RuntimeException("mEglConfig not initialized");
            }
            g();
            j jVar = this.f.get();
            if (jVar != null) {
                this.c = jVar.h.a(this.a, this.b, this.d, jVar.getSurfaceTexture());
            } else {
                this.c = null;
            }
            EGLSurface eGLSurface = this.c;
            if (eGLSurface == null || eGLSurface == EGL10.EGL_NO_SURFACE) {
                if (this.a.eglGetError() == 12299) {
                    Log.e("EglHelper", "createWindowSurface returned EGL_BAD_NATIVE_WINDOW.");
                }
                return false;
            }
            EGL10 egl10 = this.a;
            EGLDisplay eGLDisplay = this.b;
            EGLSurface eGLSurface2 = this.c;
            if (egl10.eglMakeCurrent(eGLDisplay, eGLSurface2, eGLSurface2, this.e)) {
                return true;
            }
            a("EGLHelper", "eglMakeCurrent", this.a.eglGetError());
            return false;
        }

        GL c() {
            GL gl = this.e.getGL();
            j jVar = this.f.get();
            if (jVar == null) {
                return gl;
            }
            if (jVar.i != null) {
                gl = jVar.i.a(gl);
            }
            if ((jVar.j & 3) != 0) {
                return GLDebugHelper.wrap(gl, (jVar.j & 1) == 0 ? 0 : 1, (jVar.j & 2) != 0 ? new l() : null);
            }
            return gl;
        }

        public int d() {
            if (this.a.eglSwapBuffers(this.b, this.c)) {
                return 12288;
            }
            return this.a.eglGetError();
        }

        public void e() {
            g();
        }

        private void g() {
            EGLSurface eGLSurface = this.c;
            if (eGLSurface == null || eGLSurface == EGL10.EGL_NO_SURFACE) {
                return;
            }
            this.a.eglMakeCurrent(this.b, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
            j jVar = this.f.get();
            if (jVar != null) {
                jVar.h.a(this.a, this.b, this.c);
            }
            this.c = null;
        }

        public void f() {
            if (this.e != null) {
                j jVar = this.f.get();
                if (jVar != null) {
                    jVar.g.destroyContext(this.a, this.b, this.e);
                }
                this.e = null;
            }
            EGLDisplay eGLDisplay = this.b;
            if (eGLDisplay != null) {
                this.a.eglTerminate(eGLDisplay);
                this.b = null;
            }
        }

        private void a(String str) {
            a(str, this.a.eglGetError());
        }

        public static void a(String str, int i) {
            throw new RuntimeException(b(str, i));
        }

        public static void a(String str, String str2, int i) {
            Log.w(str, b(str2, i));
        }

        public static String b(String str, int i) {
            return str + " failed: " + i;
        }
    }

    /* JADX INFO: compiled from: GLTextureView.java */
    static class i extends Thread {
        private boolean a;
        private boolean b;
        private boolean c;
        private boolean d;
        private boolean e;
        private boolean f;
        private boolean g;
        private boolean h;
        private boolean i;
        private boolean j;
        private boolean k;
        private boolean p;
        private h s;
        private WeakReference<j> t;
        private ArrayList<Runnable> q = new ArrayList<>();
        private boolean r = true;
        private int l = 0;
        private int m = 0;
        private boolean o = true;
        private int n = 1;

        i(WeakReference<j> weakReference) {
            this.t = weakReference;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            setName("GLThread " + getId());
            try {
                l();
            } catch (InterruptedException unused) {
            } finally {
                j.a.a(this);
            }
        }

        private void j() {
            if (this.i) {
                this.i = false;
                this.s.e();
            }
        }

        private void k() {
            if (this.h) {
                this.s.f();
                this.h = false;
                j.a.c(this);
            }
        }

        /* JADX WARN: Code duplicated, block: B:170:0x0222 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        private void l() throws InterruptedException {
            boolean z;
            boolean z2;
            this.s = new h(this.t);
            this.h = false;
            this.i = false;
            boolean z3 = false;
            boolean z4 = false;
            boolean z5 = false;
            boolean z6 = false;
            boolean z7 = false;
            boolean z8 = false;
            boolean z9 = false;
            int i = 0;
            int i2 = 0;
            boolean z10 = false;
            GL10 gl10 = null;
            while (true) {
                Runnable runnableRemove = null;
                while (true) {
                    try {
                        synchronized (j.a) {
                            while (true) {
                                if (this.a) {
                                    synchronized (j.a) {
                                        j();
                                        k();
                                    }
                                    return;
                                }
                                if (!this.q.isEmpty()) {
                                    runnableRemove = this.q.remove(0);
                                    z = false;
                                    break;
                                }
                                boolean z11 = this.d;
                                boolean z12 = this.c;
                                if (z11 != z12) {
                                    this.d = z12;
                                    j.a.notifyAll();
                                } else {
                                    z12 = false;
                                }
                                if (this.k) {
                                    j();
                                    k();
                                    this.k = false;
                                    z5 = true;
                                }
                                if (z3) {
                                    j();
                                    k();
                                    z3 = false;
                                }
                                if (z12 && this.i) {
                                    j();
                                }
                                if (z12 && this.h) {
                                    j jVar = this.t.get();
                                    if (!(jVar == null ? false : jVar.l) || j.a.a()) {
                                        k();
                                    }
                                }
                                if (z12 && j.a.b()) {
                                    this.s.f();
                                }
                                if (!this.e && !this.g) {
                                    if (this.i) {
                                        j();
                                    }
                                    this.g = true;
                                    this.f = false;
                                    j.a.notifyAll();
                                }
                                if (this.e && this.g) {
                                    this.g = false;
                                    j.a.notifyAll();
                                }
                                if (z4) {
                                    this.p = true;
                                    j.a.notifyAll();
                                    z4 = false;
                                    z10 = false;
                                }
                                if (m()) {
                                    if (!this.h) {
                                        if (z5) {
                                            z5 = false;
                                        } else if (j.a.b(this)) {
                                            try {
                                                this.s.a();
                                                this.h = true;
                                                j.a.notifyAll();
                                                z6 = true;
                                            } catch (RuntimeException e) {
                                                j.a.c(this);
                                                throw e;
                                            }
                                        }
                                    }
                                    if (this.h && !this.i) {
                                        this.i = true;
                                        z7 = true;
                                        z8 = true;
                                        z9 = true;
                                    }
                                    if (this.i) {
                                        if (this.r) {
                                            int i3 = this.l;
                                            int i4 = this.m;
                                            this.r = false;
                                            i = i3;
                                            i2 = i4;
                                            z = false;
                                            z7 = true;
                                            z9 = true;
                                            z10 = true;
                                        } else {
                                            z = false;
                                        }
                                        this.o = z;
                                        j.a.notifyAll();
                                        break;
                                    }
                                }
                                j.a.wait();
                            }
                        }
                        if (runnableRemove != null) {
                            break;
                        }
                        if (z7) {
                            if (this.s.b()) {
                                synchronized (j.a) {
                                    this.j = true;
                                    j.a.notifyAll();
                                }
                                z7 = z;
                            } else {
                                synchronized (j.a) {
                                    this.j = true;
                                    this.f = true;
                                    j.a.notifyAll();
                                }
                            }
                            synchronized (j.a) {
                                j();
                                k();
                                throw th;
                            }
                        }
                        if (z8) {
                            gl10 = (GL10) this.s.c();
                            j.a.a(gl10);
                            z8 = z;
                        }
                        if (z6) {
                            j jVar2 = this.t.get();
                            if (jVar2 != null) {
                                jVar2.d.onSurfaceCreated(gl10, this.s.d);
                            }
                            z6 = z;
                        }
                        if (z9) {
                            j jVar3 = this.t.get();
                            if (jVar3 != null) {
                                jVar3.d.onSurfaceChanged(gl10, i, i2);
                            }
                            z9 = z;
                        }
                        j jVar4 = this.t.get();
                        if (jVar4 != null) {
                            jVar4.d.onDrawFrame(gl10);
                        }
                        int iD = this.s.d();
                        if (iD == 12288) {
                            z2 = true;
                        } else if (iD != 12302) {
                            h.a("GLThread", "eglSwapBuffers", iD);
                            synchronized (j.a) {
                                z2 = true;
                                this.f = true;
                                j.a.notifyAll();
                            }
                        } else {
                            z2 = true;
                            z3 = true;
                        }
                        if (z10) {
                            z4 = z2;
                        }
                    } catch (Throwable th) {
                        synchronized (j.a) {
                            j();
                            k();
                            throw th;
                        }
                    }
                }
                runnableRemove.run();
            }
        }

        public boolean a() {
            return this.h && this.i && m();
        }

        private boolean m() {
            return !this.d && this.e && !this.f && this.l > 0 && this.m > 0 && (this.o || this.n == 1);
        }

        public void a(int i) {
            if (i >= 0 && i <= 1) {
                synchronized (j.a) {
                    this.n = i;
                    j.a.notifyAll();
                }
                return;
            }
            throw new IllegalArgumentException("renderMode");
        }

        public int b() {
            int i;
            synchronized (j.a) {
                i = this.n;
            }
            return i;
        }

        public void c() {
            synchronized (j.a) {
                this.o = true;
                j.a.notifyAll();
            }
        }

        public void d() {
            synchronized (j.a) {
                this.e = true;
                this.j = false;
                j.a.notifyAll();
                while (this.g && !this.j && !this.b) {
                    try {
                        j.a.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void e() {
            synchronized (j.a) {
                this.e = false;
                j.a.notifyAll();
                while (!this.g && !this.b) {
                    try {
                        j.a.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void f() {
            synchronized (j.a) {
                this.c = true;
                j.a.notifyAll();
                while (!this.b && !this.d) {
                    try {
                        j.a.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void g() {
            synchronized (j.a) {
                this.c = false;
                this.o = true;
                this.p = false;
                j.a.notifyAll();
                while (!this.b && this.d && !this.p) {
                    try {
                        j.a.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void a(int i, int i2) {
            synchronized (j.a) {
                this.l = i;
                this.m = i2;
                this.r = true;
                this.o = true;
                this.p = false;
                j.a.notifyAll();
                while (!this.b && !this.d && !this.p && a()) {
                    try {
                        j.a.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void h() {
            synchronized (j.a) {
                this.a = true;
                j.a.notifyAll();
                while (!this.b) {
                    try {
                        j.a.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void i() {
            this.k = true;
            j.a.notifyAll();
        }

        public void a(Runnable runnable) {
            if (runnable != null) {
                synchronized (j.a) {
                    this.q.add(runnable);
                    j.a.notifyAll();
                }
                return;
            }
            throw new IllegalArgumentException("r must not be null");
        }
    }

    /* JADX INFO: compiled from: GLTextureView.java */
    static class l extends Writer {
        private StringBuilder a = new StringBuilder();

        l() {
        }

        @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            a();
        }

        @Override // java.io.Writer, java.io.Flushable
        public void flush() {
            a();
        }

        @Override // java.io.Writer
        public void write(char[] cArr, int i, int i2) {
            for (int i3 = 0; i3 < i2; i3++) {
                char c = cArr[i + i3];
                if (c == '\n') {
                    a();
                } else {
                    this.a.append(c);
                }
            }
        }

        private void a() {
            if (this.a.length() > 0) {
                Log.v("GLSurfaceView", this.a.toString());
                StringBuilder sb = this.a;
                sb.delete(0, sb.length());
            }
        }
    }

    private void e() {
        if (this.c != null) {
            throw new IllegalStateException("setRenderer has already been called for this instance.");
        }
    }

    /* JADX INFO: renamed from: com.amap.api.mapcore.util.j$j, reason: collision with other inner class name */
    /* JADX INFO: compiled from: GLTextureView.java */
    private static class C0017j {
        private static String a = "GLThreadManager";
        private boolean b;
        private int c;
        private boolean d;
        private boolean e;
        private boolean f;
        private i g;

        private C0017j() {
        }

        public synchronized void a(i iVar) {
            iVar.b = true;
            if (this.g == iVar) {
                this.g = null;
            }
            notifyAll();
        }

        public boolean b(i iVar) {
            i iVar2 = this.g;
            if (iVar2 == iVar || iVar2 == null) {
                this.g = iVar;
                notifyAll();
                return true;
            }
            c();
            if (this.e) {
                return true;
            }
            i iVar3 = this.g;
            if (iVar3 == null) {
                return false;
            }
            iVar3.i();
            return false;
        }

        public void c(i iVar) {
            if (this.g == iVar) {
                this.g = null;
            }
            notifyAll();
        }

        public synchronized boolean a() {
            return this.f;
        }

        public synchronized boolean b() {
            c();
            return !this.e;
        }

        public synchronized void a(GL10 gl10) {
            if (!this.d && gl10 != null) {
                c();
                String strGlGetString = gl10.glGetString(7937);
                if (this.c < 131072) {
                    this.e = !strGlGetString.startsWith("Q3Dimension MSM7500 ");
                    notifyAll();
                }
                this.f = this.e ? false : true;
                this.d = true;
            }
        }

        private void c() {
            if (this.b) {
                return;
            }
            this.c = 131072;
            this.e = true;
            this.b = true;
        }
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i2, int i3) {
        this.c.d();
        onSurfaceTextureSizeChanged(surfaceTexture, i2, i3);
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        this.c.e();
        return true;
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i2, int i3) {
        this.c.a(i2, i3);
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i2, int i3, int i4, int i5) {
        onSurfaceTextureSizeChanged(getSurfaceTexture(), i4 - i2, i5 - i3);
        super.onLayout(z, i2, i3, i4, i5);
    }
}
