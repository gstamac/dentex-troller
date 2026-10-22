package com.amap.api.mapcore.util;

import android.content.Context;
import dalvik.system.DexFile;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: compiled from: BaseLoader.java */
/* JADX INFO: loaded from: classes.dex */
abstract class gj extends ClassLoader {
    protected final Context a;
    protected final Map<String, Class<?>> b;
    protected DexFile c;
    volatile boolean d;
    protected ff e;
    protected String f;

    public gj(Context context, ff ffVar, boolean z) {
        super(context.getClassLoader());
        this.b = new HashMap();
        this.c = null;
        this.d = true;
        this.a = context;
        this.e = ffVar;
    }

    public boolean a() {
        return this.c != null;
    }

    protected void b() {
        try {
            synchronized (this.b) {
                this.b.clear();
            }
            DexFile dexFile = this.c;
            if (dexFile != null) {
                dexFile.close();
            }
        } catch (Throwable th) {
            gr.a(th, "BaseLoader", "releaseDexFile()");
        }
    }
}
