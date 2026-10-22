package com.autonavi.ae.gmap.maploader;

import android.text.TextUtils;
import com.autonavi.ae.gmap.GLMapEngine;
import com.autonavi.ae.gmap.utils.GLConvertUtil;
import com.autonavi.ae.gmap.utils.GLMapUtil;
import com.qiniu.android.common.Constants;
import java.net.HttpURLConnection;

/* JADX INFO: loaded from: classes.dex */
public class HeatMapLoader extends BaseMapLoader {
    private String mMapHeatPoiId;

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected void addRequestHeader(HttpURLConnection httpURLConnection) {
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected String getMapServerPath() {
        return "/ws/mps/hot/?";
    }

    public void setMapHeatPoiId(String str) {
        this.mMapHeatPoiId = str;
    }

    public HeatMapLoader(int i, GLMapEngine gLMapEngine, int i2) {
        super(i);
        this.mMapHeatPoiId = null;
        this.mGLMapEngine = gLMapEngine;
        this.mDataSource = i2;
        this.mCreateTime = System.currentTimeMillis();
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected String getMapAddress() {
        return this.mGLMapEngine.getMapSvrAddress();
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected String getGridParams() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < this.mMapTiles.size(); i++) {
            String gridName = this.mMapTiles.get(i).getGridName();
            if (!TextUtils.isEmpty(gridName) && !super.isContainIllegal(gridName) && GLMapUtil.isAssic(gridName)) {
                stringBuffer.append(gridName + ";");
            }
        }
        String string = stringBuffer.toString();
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        while (true) {
            if (!string.endsWith(";") && !string.endsWith(" ")) {
                break;
            }
            string = stringBuffer.substring(0, string.length() - 1);
        }
        if (string.length() <= 0) {
            return null;
        }
        return "cp=1&mesh=" + (string + "&channel=amapapi") + "&poiid=" + this.mMapHeatPoiId;
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected boolean processReceivedDataHeader() {
        if (this.mReceivedDataSize <= 7) {
            return false;
        }
        if (GLConvertUtil.getInt(this.mReceivedDataBuffer, 0) != 0) {
            doCancel();
            return false;
        }
        this.mReceivedDataSize -= 8;
        if (this.mReceivedDataSize > 0) {
            GLConvertUtil.moveArray(this.mReceivedDataBuffer, 8, this.mReceivedDataBuffer, 0, this.mReceivedDataSize);
        }
        this.mNextImgDataLength = 0;
        this.isReceivedHeader = true;
        super.processReceivedData();
        return true;
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected void processReceivedDataByType() {
        super.processReceivedData();
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected void processReceivedVersion() {
        if (this.mDataSource == 9) {
            processReceivedVersionData(this.mReceivedDataBuffer, 0, this.mReceivedDataSize);
        }
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected void processReceivedTileData(byte[] bArr, int i, int i2) {
        if (i == 0) {
            super.processReceivedTileData(bArr, i, i2);
        } else {
            processReceivedTileDataBmp(bArr, i, i2);
        }
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    public boolean isRequestValid() {
        return this.mGLMapEngine.isGridsInScreen(this.mEngineID, this.mMapTiles, this.mDataSource);
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected boolean isNeedReturn() {
        return this.mDataSource == 9;
    }

    protected void processReceivedTileDataBmp(byte[] bArr, int i, int i2) {
        int i3 = i + 4;
        int i4 = i3 + 1;
        try {
            int i5 = bArr[i3];
            String str = "";
            if (i5 > 0 && (i4 + i5) - 1 < i2) {
                str = new String(bArr, i4, i5, Constants.UTF_8);
            }
            if (this.mGLMapEngine.isMapEngineValid() && i2 > i) {
                this.mGLMapEngine.putMapHeatData(this.mEngineID, bArr, i, i2 - i, 0, this.mMapHeatPoiId);
                if (this.mGLMapEngine.GetCurrentGrideNameLen(this.mEngineID) == str.length() ? true ^ this.mGLMapEngine.isGridsInScreen(this.mEngineID, this.mMapTiles, this.mDataSource) : true) {
                    super.doCancel();
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
