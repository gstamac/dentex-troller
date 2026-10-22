package com.autonavi.ae.gmap.maploader;

/* JADX INFO: loaded from: classes.dex */
public class ProcessingTile {
    private static final Pools.SynchronizedPool<ProcessingTile> mPool = new Pools.SynchronizedPool<>(30);
    public long mCreateTime = 0;
    public String mKeyName;

    public static ProcessingTile obtain(String str) {
        ProcessingTile processingTileAcquire = mPool.acquire();
        if (processingTileAcquire != null) {
            processingTileAcquire.setParams(str);
            return processingTileAcquire;
        }
        return new ProcessingTile(str);
    }

    public void recycle() {
        this.mKeyName = null;
        this.mCreateTime = 0L;
        mPool.release(this);
    }

    public ProcessingTile(String str) {
        setParams(str);
    }

    private void setParams(String str) {
        this.mKeyName = str;
        this.mCreateTime = System.currentTimeMillis() / 1000;
    }
}
