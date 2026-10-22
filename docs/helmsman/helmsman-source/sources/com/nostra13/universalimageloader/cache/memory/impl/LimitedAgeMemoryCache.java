package com.nostra13.universalimageloader.cache.memory.impl;

import android.graphics.Bitmap;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class LimitedAgeMemoryCache implements MemoryCache {
    private final MemoryCache cache;
    private final Map<String, Long> loadingDates = Collections.synchronizedMap(new HashMap());
    private final long maxAge;

    public LimitedAgeMemoryCache(MemoryCache memoryCache, long j) {
        this.cache = memoryCache;
        this.maxAge = j * 1000;
    }

    @Override // com.nostra13.universalimageloader.cache.memory.MemoryCache
    public boolean put(String str, Bitmap bitmap) {
        boolean zPut = this.cache.put(str, bitmap);
        if (zPut) {
            this.loadingDates.put(str, Long.valueOf(System.currentTimeMillis()));
        }
        return zPut;
    }

    @Override // com.nostra13.universalimageloader.cache.memory.MemoryCache
    public Bitmap get(String str) {
        Long l = this.loadingDates.get(str);
        if (l != null && System.currentTimeMillis() - l.longValue() > this.maxAge) {
            this.cache.remove(str);
            this.loadingDates.remove(str);
        }
        return this.cache.get(str);
    }

    @Override // com.nostra13.universalimageloader.cache.memory.MemoryCache
    public Bitmap remove(String str) {
        this.loadingDates.remove(str);
        return this.cache.remove(str);
    }

    @Override // com.nostra13.universalimageloader.cache.memory.MemoryCache
    public Collection<String> keys() {
        return this.cache.keys();
    }

    @Override // com.nostra13.universalimageloader.cache.memory.MemoryCache
    public void clear() {
        this.cache.clear();
        this.loadingDates.clear();
    }
}
