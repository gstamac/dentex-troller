package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.RemoteException;
import com.amap.api.maps.model.TileOverlay;
import com.amap.api.maps.model.TileOverlayOptions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* JADX INFO: compiled from: TileOverlayView.java */
/* JADX INFO: loaded from: classes.dex */
public class u {
    cu d;
    private k f;
    private Context g;
    List<cm> a = new ArrayList();
    a b = new a();
    List<Integer> c = new ArrayList();
    float[] e = new float[16];

    /* JADX INFO: compiled from: TileOverlayView.java */
    static class a implements Serializable, Comparator<Object> {
        a() {
        }

        @Override // java.util.Comparator
        public int compare(Object obj, Object obj2) {
            cm cmVar = (cm) obj;
            cm cmVar2 = (cm) obj2;
            if (cmVar == null || cmVar2 == null) {
                return 0;
            }
            try {
                if (cmVar.getZIndex() > cmVar2.getZIndex()) {
                    return 1;
                }
                return cmVar.getZIndex() < cmVar2.getZIndex() ? -1 : 0;
            } catch (Throwable th) {
                fn.b(th, "TileOverlayView", "compare");
                th.printStackTrace();
                return 0;
            }
        }
    }

    public u(Context context, k kVar) {
        this.d = null;
        this.f = kVar;
        this.g = context;
        int i = 256;
        TileOverlayOptions tileOverlayOptionsTileProvider = new TileOverlayOptions().tileProvider(new de(i, i) { // from class: com.amap.api.mapcore.util.u.1
            @Override // com.amap.api.mapcore.util.de
            public String a(int i2, int i3, int i4) {
                try {
                    return String.format(Locale.US, "http://grid.amap.com/grid/%d/%d/%d?dpiType=webrd&lang=zh_cn&pack=%s&ds=0", Integer.valueOf(i4), Integer.valueOf(i2), Integer.valueOf(i3), g.b);
                } catch (Throwable unused) {
                    return null;
                }
            }
        });
        tileOverlayOptionsTileProvider.memCacheSize(10485760);
        tileOverlayOptionsTileProvider.diskCacheSize(20480);
        this.d = new cu(tileOverlayOptionsTileProvider, this, true);
    }

    public k a() {
        return this.f;
    }

    public void b() {
        cu cuVar;
        try {
            Iterator<Integer> it = this.c.iterator();
            while (it.hasNext()) {
                dw.b(it.next().intValue());
            }
            this.c.clear();
            if (g.c == 0 && (cuVar = this.d) != null) {
                cuVar.a();
            }
            synchronized (this.a) {
                try {
                    int size = this.a.size();
                    for (int i = 0; i < size; i++) {
                        cm cmVar = this.a.get(i);
                        if (cmVar.isVisible()) {
                            cmVar.a();
                        }
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        } catch (Throwable unused) {
        }
    }

    public void c() {
        synchronized (this.a) {
            int size = this.a.size();
            for (int i = 0; i < size; i++) {
                cm cmVar = this.a.get(i);
                if (cmVar != null) {
                    cmVar.destroy(false);
                }
            }
            this.a.clear();
        }
    }

    public void d() {
        synchronized (this.a) {
            Collections.sort(this.a, this.b);
        }
    }

    public TileOverlay a(TileOverlayOptions tileOverlayOptions) throws RemoteException {
        if (tileOverlayOptions != null && tileOverlayOptions.getTileProvider() != null) {
            try {
                cu cuVar = new cu(tileOverlayOptions, this);
                a(cuVar);
                cuVar.a(true);
                this.f.setRunLowFrame(false);
                return new TileOverlay(cuVar);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        return null;
    }

    public void a(cm cmVar) {
        synchronized (this.a) {
            b(cmVar);
            this.a.add(cmVar);
        }
        d();
    }

    public boolean b(cm cmVar) {
        boolean zRemove;
        synchronized (this.a) {
            zRemove = this.a.remove(cmVar);
        }
        return zRemove;
    }

    /* JADX WARN: Bottom block not found for handler: all -> 0x0057 */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x005f, code lost:
    
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void a(boolean r6) {
        /*
            r5 = this;
            int r0 = com.amap.api.mapcore.util.g.c     // Catch: java.lang.Throwable -> L57
            if (r0 != 0) goto L30
            com.amap.api.mapcore.util.k r0 = r5.f     // Catch: java.lang.Throwable -> L57
            com.amap.api.maps.model.CameraPosition r0 = r0.getCameraPosition()     // Catch: java.lang.Throwable -> L57
            if (r0 == 0) goto L29
            float r1 = r0.zoom     // Catch: java.lang.Throwable -> L57
            r2 = 1092616192(0x41200000, float:10.0)
            int r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1))
            if (r1 <= 0) goto L29
            boolean r0 = r0.isAbroad     // Catch: java.lang.Throwable -> L57
            if (r0 == 0) goto L29
            com.amap.api.mapcore.util.k r0 = r5.f     // Catch: java.lang.Throwable -> L57
            int r0 = r0.getMapType()     // Catch: java.lang.Throwable -> L57
            r1 = 1
            if (r0 != r1) goto L29
            com.amap.api.mapcore.util.cu r0 = r5.d     // Catch: java.lang.Throwable -> L57
            if (r0 == 0) goto L30
            r0.a(r6)     // Catch: java.lang.Throwable -> L57
            goto L30
        L29:
            com.amap.api.mapcore.util.cu r0 = r5.d     // Catch: java.lang.Throwable -> L57
            if (r0 == 0) goto L30
            r0.b()     // Catch: java.lang.Throwable -> L57
        L30:
            java.util.List<com.amap.api.mapcore.util.cm> r0 = r5.a     // Catch: java.lang.Throwable -> L57
            monitor-enter(r0)     // Catch: java.lang.Throwable -> L57
            java.util.List<com.amap.api.mapcore.util.cm> r1 = r5.a     // Catch: java.lang.Throwable -> L54
            int r1 = r1.size()     // Catch: java.lang.Throwable -> L54
            r2 = 0
        L3a:
            if (r2 >= r1) goto L52
            java.util.List<com.amap.api.mapcore.util.cm> r3 = r5.a     // Catch: java.lang.Throwable -> L54
            java.lang.Object r3 = r3.get(r2)     // Catch: java.lang.Throwable -> L54
            com.amap.api.mapcore.util.cm r3 = (com.amap.api.mapcore.util.cm) r3     // Catch: java.lang.Throwable -> L54
            if (r3 == 0) goto L4f
            boolean r4 = r3.isVisible()     // Catch: java.lang.Throwable -> L54
            if (r4 == 0) goto L4f
            r3.a(r6)     // Catch: java.lang.Throwable -> L54
        L4f:
            int r2 = r2 + 1
            goto L3a
        L52:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L54
            goto L5f
        L54:
            r6 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L54
            throw r6     // Catch: java.lang.Throwable -> L57
        L57:
            r6 = move-exception
            java.lang.String r0 = "TileOverlayView"
            java.lang.String r1 = "refresh"
            com.amap.api.mapcore.util.fn.b(r6, r0, r1)
        L5f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.u.a(boolean):void");
    }

    public void b(boolean z) {
        cu cuVar = this.d;
        if (cuVar != null) {
            cuVar.b(z);
        }
        synchronized (this.a) {
            int size = this.a.size();
            for (int i = 0; i < size; i++) {
                cm cmVar = this.a.get(i);
                if (cmVar != null) {
                    cmVar.b(z);
                }
            }
        }
    }

    public Context e() {
        return this.g;
    }

    public void a(int i) {
        this.c.add(Integer.valueOf(i));
    }

    public void f() {
        c();
        cu cuVar = this.d;
        if (cuVar != null) {
            cuVar.remove();
        }
        this.d = null;
    }

    public float[] g() {
        k kVar = this.f;
        if (kVar != null) {
            return kVar.v();
        }
        return this.e;
    }
}
