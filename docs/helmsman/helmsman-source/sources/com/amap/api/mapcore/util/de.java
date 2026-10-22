package com.amap.api.mapcore.util;

import android.text.TextUtils;
import com.amap.api.maps.model.Tile;
import com.amap.api.maps.model.TileProvider;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* JADX INFO: compiled from: BaseTileProvider.java */
/* JADX INFO: loaded from: classes.dex */
public abstract class de implements TileProvider {
    private final int a;
    hb b;
    private final int c;

    public abstract String a(int i, int i2, int i3);

    public de(int i, int i2) {
        this.a = i;
        this.c = i2;
    }

    @Override // com.amap.api.maps.model.TileProvider
    public final Tile getTile(int i, int i2, int i3) {
        String strA = a(i, i2, i3);
        if (TextUtils.isEmpty(strA)) {
            return NO_TILE;
        }
        try {
            return Tile.obtain(this.a, this.c, a(strA));
        } catch (IOException unused) {
            return NO_TILE;
        }
    }

    private byte[] a(String str) throws IOException {
        try {
            a aVar = new a(str);
            hb hbVarA = hb.a(false);
            this.b = hbVarA;
            return hbVarA.d(aVar);
        } catch (Throwable unused) {
            return null;
        }
    }

    @Override // com.amap.api.maps.model.TileProvider
    public int getTileWidth() {
        return this.a;
    }

    @Override // com.amap.api.maps.model.TileProvider
    public int getTileHeight() {
        return this.c;
    }

    /* JADX INFO: compiled from: BaseTileProvider.java */
    class a extends hc {
        private String b;

        @Override // com.amap.api.mapcore.util.hc
        public Map<String, String> b() {
            return null;
        }

        public a(String str) {
            this.b = str;
            a(fd.a(o.a));
            a(BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT);
            b(50000);
        }

        @Override // com.amap.api.mapcore.util.hc
        public Map<String, String> a() {
            HashMap map = new HashMap();
            map.put("User-Agent", g.d);
            map.put("Accept-Encoding", "gzip");
            map.put("platinfo", String.format(Locale.US, "platform=Android&sdkversion=%s&product=%s", "5.4.0", "3dmap"));
            map.put("X-INFO", ez.a(o.a));
            map.put("key", ew.f(o.a));
            map.put("logversion", "2.1");
            return map;
        }

        @Override // com.amap.api.mapcore.util.hc
        public String c() {
            return this.b;
        }
    }
}
