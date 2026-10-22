package com.autonavi.ae.gmap.maploader;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class TilesProcessingCtrl {
    private static final int EXPIRED_MAX_TIME = 60;
    private Hashtable<String, ProcessingTile> mProcessingTiles = new Hashtable<>();
    private int mRequireSize = 0;

    public synchronized void removeTile(String str) {
        this.mProcessingTiles.remove(str).recycle();
    }

    public synchronized boolean isProcessing(String str) {
        return this.mProcessingTiles.get(str) != null;
    }

    public synchronized void addProcessingTile(String str) {
        this.mProcessingTiles.put(str, ProcessingTile.obtain(str));
    }

    public synchronized void clearAll() {
        try {
            Iterator<Map.Entry<String, ProcessingTile>> it = this.mProcessingTiles.entrySet().iterator();
            while (it.hasNext()) {
                it.next().getValue().recycle();
            }
            this.mProcessingTiles.clear();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public synchronized int getSize() {
        return this.mProcessingTiles.size();
    }
}
