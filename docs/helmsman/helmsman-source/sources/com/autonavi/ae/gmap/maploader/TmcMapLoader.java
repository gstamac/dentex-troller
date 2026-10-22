package com.autonavi.ae.gmap.maploader;

import com.autonavi.ae.gmap.GLMapEngine;
import com.qiniu.android.common.Constants;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class TmcMapLoader extends NormalMapLoader {
    private Map<String, Object> mMd5Key;

    public TmcMapLoader(int i, GLMapEngine gLMapEngine, int i2) {
        super(i, gLMapEngine, i2);
        this.mMd5Key = new HashMap();
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    public void addRequestTiles(MapSourceGridData mapSourceGridData) {
        if (this.mMapTiles != null) {
            if (mapSourceGridData.mObj != null) {
                this.mMd5Key.put(mapSourceGridData.getGridName(), mapSourceGridData.mObj);
            }
            this.mMapTiles.add(mapSourceGridData);
        }
    }

    @Override // com.autonavi.ae.gmap.maploader.NormalMapLoader
    protected void processReceivedTileDataVTmc(byte[] bArr, int i, int i2) {
        int i3 = i + 4;
        int i4 = i3 + 1;
        try {
            int i5 = bArr[i3];
            int i6 = i4 + i5;
            if (i6 <= bArr.length) {
                if (i4 <= bArr.length - 1 && i5 >= 0) {
                    String str = new String(bArr, i4, i5, Constants.UTF_8);
                    byte b = bArr[i6];
                    if (this.mGLMapEngine.isMapEngineValid() && i2 > i) {
                        int i7 = i2 - i;
                        byte[] bArr2 = new byte[i7];
                        System.arraycopy(bArr, i, bArr2, 0, i7);
                        this.mGLMapEngine.putMapTMCData(this.mEngineID, this.mDataSource, bArr2, str, this.mMd5Key.containsKey(str));
                        if (this.mGLMapEngine.GetCurrentGrideNameLen(this.mEngineID) >= str.length() ? true ^ this.mGLMapEngine.isGridsInScreen(this.mEngineID, this.mMapTiles, this.mDataSource) : true) {
                            super.doCancel();
                        }
                    }
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
