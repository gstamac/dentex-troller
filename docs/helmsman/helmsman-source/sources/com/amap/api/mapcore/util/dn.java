package com.amap.api.mapcore.util;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/* JADX INFO: compiled from: LruCache.java */
/* JADX INFO: loaded from: classes.dex */
public class dn<K, V> {
    private final LinkedHashMap<K, V> a;
    private int b;
    private int c;
    private int d;
    private int e;
    private int f;
    private int g;
    private int h;

    protected void a(boolean z, K k, V v, V v2) {
    }

    protected int b(K k, V v) {
        return 1;
    }

    protected V b(K k) {
        return null;
    }

    public dn(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.c = i;
        this.a = new LinkedHashMap<>(0, 0.75f, true);
    }

    public final V a(K k) {
        V vPut;
        if (k == null) {
            throw new NullPointerException("key == null");
        }
        synchronized (this) {
            V v = this.a.get(k);
            if (v != null) {
                this.g++;
                return v;
            }
            this.h++;
            V vB = b(k);
            if (vB == null) {
                return null;
            }
            synchronized (this) {
                this.e++;
                vPut = this.a.put(k, vB);
                if (vPut != null) {
                    this.a.put(k, vPut);
                } else {
                    this.b += c(k, vB);
                }
            }
            if (vPut != null) {
                a(false, k, vB, vPut);
                return vPut;
            }
            a(this.c);
            return vB;
        }
    }

    public final V a(K k, V v) {
        V vPut;
        if (k == null || v == null) {
            throw new NullPointerException("key == null || value == null");
        }
        synchronized (this) {
            this.d++;
            this.b += c(k, v);
            vPut = this.a.put(k, v);
            if (vPut != null) {
                this.b -= c(k, vPut);
            }
        }
        if (vPut != null) {
            a(false, k, vPut, v);
        }
        a(this.c);
        return vPut;
    }

    private void a(int i) {
        K key;
        V value;
        while (true) {
            synchronized (this) {
                if (this.b >= 0) {
                    this.a.isEmpty();
                }
                if (this.b <= i) {
                    return;
                }
                Iterator<Map.Entry<K, V>> it = this.a.entrySet().iterator();
                Map.Entry<K, V> next = null;
                while (it.hasNext()) {
                    next = it.next();
                }
                if (next == null) {
                    return;
                }
                key = next.getKey();
                value = next.getValue();
                this.a.remove(key);
                this.b -= c(key, value);
                this.f++;
            }
            a(true, key, value, null);
        }
    }

    private int c(K k, V v) {
        int iB = b(k, v);
        if (iB >= 0) {
            return iB;
        }
        throw new IllegalStateException("Negative size: " + k + "=" + v);
    }

    public final void a() {
        a(-1);
    }

    public final synchronized String toString() {
        int i;
        int i2;
        i = this.g;
        i2 = this.h + i;
        return String.format(Locale.US, "LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", Integer.valueOf(this.c), Integer.valueOf(this.g), Integer.valueOf(this.h), Integer.valueOf(i2 != 0 ? (i * 100) / i2 : 0));
    }
}
