package com.amap.api.mapcore.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import java.lang.ref.WeakReference;

/* JADX INFO: compiled from: ImageWorker.java */
/* JADX INFO: loaded from: classes.dex */
public abstract class ea {
    private dx a;
    private dx.a b;
    protected Resources d;
    private boolean e = false;
    protected boolean c = false;
    private final Object f = new Object();

    protected abstract Bitmap a(Object obj);

    protected ea(Context context) {
        this.d = context.getResources();
    }

    public void a(boolean z, cu.a aVar) {
        Bitmap bitmapA;
        if (aVar == null) {
            return;
        }
        try {
            if (this.a != null) {
                bitmapA = this.a.a(aVar.a + "-" + aVar.b + "-" + aVar.c);
            } else {
                bitmapA = null;
            }
            if (bitmapA != null) {
                aVar.a(bitmapA);
                return;
            }
            a aVar2 = new a(aVar);
            aVar.j = aVar2;
            aVar2.a(dd.c, Boolean.valueOf(z));
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void a(dx.a aVar) {
        this.b = aVar;
        this.a = dx.a(aVar);
        new b().c(1);
    }

    protected dx a() {
        return this.a;
    }

    public static void a(cu.a aVar) {
        a aVarC = c(aVar);
        if (aVarC != null) {
            aVarC.a(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static a c(cu.a aVar) {
        if (aVar != null) {
            return aVar.j;
        }
        return null;
    }

    /* JADX INFO: compiled from: ImageWorker.java */
    public class a extends dd<Boolean, Void, Bitmap> {
        private final WeakReference<cu.a> e;

        public a(cu.a aVar) {
            this.e = new WeakReference<>(aVar);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.amap.api.mapcore.util.dd
        public Bitmap a(Boolean... boolArr) {
            try {
                boolean zBooleanValue = boolArr[0].booleanValue();
                cu.a aVar = this.e.get();
                if (aVar == null) {
                    return null;
                }
                String str = aVar.a + "-" + aVar.b + "-" + aVar.c;
                synchronized (ea.this.f) {
                    while (ea.this.c && !d()) {
                        ea.this.f.wait();
                    }
                }
                Bitmap bitmapB = (ea.this.a == null || d() || e() == null || ea.this.e) ? null : ea.this.a.b(str);
                if (zBooleanValue && bitmapB == null && !d() && e() != null && !ea.this.e) {
                    bitmapB = ea.this.a((Object) aVar);
                }
                if (bitmapB != null && ea.this.a != null) {
                    ea.this.a.a(str, bitmapB);
                }
                return bitmapB;
            } catch (Throwable th) {
                th.printStackTrace();
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.amap.api.mapcore.util.dd
        public void a(Bitmap bitmap) {
            try {
                if (d() || ea.this.e) {
                    bitmap = null;
                }
                cu.a aVarE = e();
                if (bitmap == null || bitmap.isRecycled() || aVarE == null) {
                    return;
                }
                aVarE.a(bitmap);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.amap.api.mapcore.util.dd
        public void b(Bitmap bitmap) {
            super.b(bitmap);
            synchronized (ea.this.f) {
                try {
                    ea.this.f.notifyAll();
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        }

        private cu.a e() {
            cu.a aVar = this.e.get();
            if (this == ea.c(aVar)) {
                return aVar;
            }
            return null;
        }
    }

    public void a(boolean z) {
        synchronized (this.f) {
            this.c = z;
            if (!z) {
                try {
                    this.f.notifyAll();
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        }
    }

    /* JADX INFO: compiled from: ImageWorker.java */
    protected class b extends dd<Object, Void, Void> {
        protected b() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.amap.api.mapcore.util.dd
        /* JADX INFO: renamed from: d, reason: merged with bridge method [inline-methods] */
        public Void a(Object... objArr) {
            try {
                int iIntValue = ((Integer) objArr[0]).intValue();
                if (iIntValue == 0) {
                    ea.this.c();
                } else if (iIntValue == 1) {
                    ea.this.b();
                } else if (iIntValue == 2) {
                    ea.this.d();
                } else if (iIntValue == 3) {
                    ea.this.e();
                }
                return null;
            } catch (Throwable th) {
                th.printStackTrace();
                return null;
            }
        }
    }

    protected void b() {
        dx dxVar = this.a;
        if (dxVar != null) {
            dxVar.a();
        }
    }

    protected void c() {
        dx dxVar = this.a;
        if (dxVar != null) {
            dxVar.b();
        }
    }

    protected void d() {
        dx dxVar = this.a;
        if (dxVar != null) {
            dxVar.c();
        }
    }

    protected void e() {
        dx dxVar = this.a;
        if (dxVar != null) {
            dxVar.d();
            this.a = null;
        }
    }

    public void f() {
        new b().c(0);
    }

    public void g() {
        new b().c(3);
    }
}
