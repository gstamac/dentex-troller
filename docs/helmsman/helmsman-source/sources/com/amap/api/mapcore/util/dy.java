package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.amap.api.maps.model.Tile;
import com.amap.api.maps.model.TileProvider;

/* JADX INFO: compiled from: ImageFetcher.java */
/* JADX INFO: loaded from: classes.dex */
public class dy extends dz {
    private TileProvider e;

    public dy(Context context, int i, int i2) {
        super(context, i, i2);
        this.e = null;
        a(context);
    }

    private void a(Context context) {
        b(context);
    }

    public void a(TileProvider tileProvider) {
        this.e = tileProvider;
    }

    private void b(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            activeNetworkInfo.isConnectedOrConnecting();
        }
    }

    private Bitmap c(cu.a aVar) {
        try {
            Tile tile = this.e.getTile(aVar.a, aVar.b, aVar.c);
            if (tile == null || tile == TileProvider.NO_TILE) {
                return null;
            }
            return BitmapFactory.decodeByteArray(tile.data, 0, tile.data.length);
        } catch (Throwable unused) {
            return null;
        }
    }

    @Override // com.amap.api.mapcore.util.dz, com.amap.api.mapcore.util.ea
    protected Bitmap a(Object obj) {
        return c((cu.a) obj);
    }
}
