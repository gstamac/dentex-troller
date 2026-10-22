package com.amap.api.mapcore.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/* JADX INFO: renamed from: com.amap.api.mapcore.util.do, reason: invalid class name */
/* JADX INFO: compiled from: NativeBufferPool.java */
/* JADX INFO: loaded from: classes.dex */
public class Cdo extends ee<a> {
    private a b;

    /* JADX INFO: renamed from: com.amap.api.mapcore.util.do$a */
    /* JADX INFO: compiled from: NativeBufferPool.java */
    static final class a extends ed<a> {
        ByteBuffer a;
        ShortBuffer b;
        FloatBuffer c;
        IntBuffer d;
        int e;

        a() {
        }

        void a(int i) {
            if (i < 32768) {
                i = 32768;
            }
            this.a = ByteBuffer.allocateDirect(i).order(ByteOrder.nativeOrder());
            this.e = i;
            this.b = null;
            this.d = null;
            this.c = null;
        }
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [T extends com.amap.api.mapcore.util.ed<?>, T extends com.amap.api.mapcore.util.ed<T>] */
    public a a(int i) {
        a aVar = (a) this.a;
        if (aVar == null) {
            aVar = new a();
        } else {
            this.a = aVar.f;
            aVar.f = null;
        }
        if (aVar.e < i) {
            aVar.a(i);
        }
        this.b = (a) ed.a(this.b, aVar);
        return aVar;
    }

    public void a() {
        this.b = b(this.b);
    }

    public ShortBuffer b(int i) {
        a aVarA = a(i * 2);
        if (aVarA.b == null) {
            aVarA.a.clear();
            aVarA.b = aVarA.a.asShortBuffer();
        } else {
            aVarA.b.clear();
        }
        return aVarA.b;
    }

    public FloatBuffer c(int i) {
        a aVarA = a(i * 4);
        if (aVarA.c == null) {
            aVarA.a.clear();
            aVarA.c = aVarA.a.asFloatBuffer();
        } else {
            aVarA.c.clear();
        }
        aVarA.c.clear();
        return aVarA.c;
    }
}
