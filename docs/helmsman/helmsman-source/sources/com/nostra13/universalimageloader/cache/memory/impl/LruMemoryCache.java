package com.nostra13.universalimageloader.cache.memory.impl;

import android.graphics.Bitmap;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class LruMemoryCache implements MemoryCache {
    private final LinkedHashMap<String, Bitmap> map;
    private final int maxSize;
    private int size;

    public LruMemoryCache(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.maxSize = i;
        this.map = new LinkedHashMap<>(0, 0.75f, true);
    }

    @Override // com.nostra13.universalimageloader.cache.memory.MemoryCache
    public final Bitmap get(String str) {
        Bitmap bitmap;
        if (str == null) {
            throw new NullPointerException("key == null");
        }
        synchronized (this) {
            bitmap = this.map.get(str);
        }
        return bitmap;
    }

    @Override // com.nostra13.universalimageloader.cache.memory.MemoryCache
    public final boolean put(String str, Bitmap bitmap) {
        if (str == null || bitmap == null) {
            throw new NullPointerException("key == null || value == null");
        }
        synchronized (this) {
            this.size += sizeOf(str, bitmap);
            Bitmap bitmapPut = this.map.put(str, bitmap);
            if (bitmapPut != null) {
                this.size -= sizeOf(str, bitmapPut);
            }
        }
        trimToSize(this.maxSize);
        return true;
    }

    private void trimToSize(int i) {
        while (true) {
            synchronized (this) {
                if (this.size < 0 || (this.map.isEmpty() && this.size != 0)) {
                    break;
                }
                if (this.size > i && !this.map.isEmpty()) {
                    Map.Entry<String, Bitmap> next = this.map.entrySet().iterator().next();
                    if (next == null) {
                        return;
                    }
                    String key = next.getKey();
                    Bitmap value = next.getValue();
                    this.map.remove(key);
                    this.size -= sizeOf(key, value);
                }
                return;
            }
        }
        throw new IllegalStateException(getClass().getName() + ".sizeOf() is reporting inconsistent results!");
    }

    @Override // com.nostra13.universalimageloader.cache.memory.MemoryCache
    public final Bitmap remove(String str) {
        Bitmap bitmapRemove;
        if (str == null) {
            throw new NullPointerException("key == null");
        }
        synchronized (this) {
            bitmapRemove = this.map.remove(str);
            if (bitmapRemove != null) {
                this.size -= sizeOf(str, bitmapRemove);
            }
        }
        return bitmapRemove;
    }

    @Override // com.nostra13.universalimageloader.cache.memory.MemoryCache
    public Collection<String> keys() {
        HashSet hashSet;
        synchronized (this) {
            hashSet = new HashSet(this.map.keySet());
        }
        return hashSet;
    }

    @Override // com.nostra13.universalimageloader.cache.memory.MemoryCache
    public void clear() {
        trimToSize(-1);
    }

    private int sizeOf(String str, Bitmap bitmap) {
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    public final synchronized String toString() {
        return String.format("LruCache[maxSize=%d]", Integer.valueOf(this.maxSize));
    }
}
