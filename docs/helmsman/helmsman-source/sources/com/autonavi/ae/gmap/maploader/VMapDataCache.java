package com.autonavi.ae.gmap.maploader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class VMapDataCache {
    private static final int MAXSIZE = 400;
    private static VMapDataCache mMapDataCacheInstance;
    private Map<String, VMapDataRecoder> mMapDataHs = new HashMap();
    private List<String> mMapDataList = new ArrayList();
    private Map<String, VMapDataRecoder> mCancelMapDataHs = new HashMap();
    private List<String> mCancelMapDataList = new ArrayList();

    public static VMapDataCache getInstance() {
        if (mMapDataCacheInstance == null) {
            mMapDataCacheInstance = new VMapDataCache();
        }
        return mMapDataCacheInstance;
    }

    public synchronized void reset() {
        this.mMapDataHs.clear();
        this.mMapDataList.clear();
        this.mCancelMapDataHs.clear();
        this.mCancelMapDataList.clear();
    }

    public int getSize() {
        return this.mMapDataHs.size();
    }

    private String getKey(String str, int i) {
        return str + "-" + i;
    }

    public synchronized VMapDataRecoder getRecoder(String str, int i) {
        VMapDataRecoder vMapDataRecoder;
        vMapDataRecoder = this.mMapDataHs.get(getKey(str, i));
        if (vMapDataRecoder != null) {
            vMapDataRecoder.mTimes++;
        }
        return vMapDataRecoder;
    }

    public synchronized VMapDataRecoder getCancelRecoder(String str, int i) {
        VMapDataRecoder vMapDataRecoder = this.mCancelMapDataHs.get(getKey(str, i));
        if (vMapDataRecoder != null) {
            if ((System.currentTimeMillis() / 1000) - ((long) vMapDataRecoder.mCreateTime) > 10) {
                return null;
            }
        }
        return vMapDataRecoder;
    }

    public synchronized VMapDataRecoder putRecoder(byte[] bArr, String str, int i) {
        VMapDataRecoder vMapDataRecoder = new VMapDataRecoder(str, i);
        if (vMapDataRecoder.mGridName == null) {
            return null;
        }
        if (this.mMapDataHs.size() > 400) {
            this.mMapDataHs.remove(this.mMapDataList.get(0));
            this.mMapDataList.remove(0);
        }
        this.mMapDataHs.put(getKey(str, i), vMapDataRecoder);
        this.mMapDataList.add(getKey(str, i));
        return vMapDataRecoder;
    }

    public synchronized VMapDataRecoder putCancelRecoder(byte[] bArr, String str, int i) {
        if (getRecoder(str, i) != null) {
            return null;
        }
        VMapDataRecoder vMapDataRecoder = new VMapDataRecoder(str, i);
        if (vMapDataRecoder.mGridName == null) {
            return null;
        }
        if (this.mCancelMapDataHs.size() > 400) {
            this.mCancelMapDataHs.remove(this.mMapDataList.get(0));
            this.mCancelMapDataList.remove(0);
        }
        this.mCancelMapDataHs.put(getKey(str, i), vMapDataRecoder);
        this.mCancelMapDataList.add(getKey(str, i));
        return vMapDataRecoder;
    }
}
