package com.autonavi.ae.gmap.maploader;

import android.text.TextUtils;
import com.autonavi.ae.gmap.GLMapEngine;
import com.autonavi.ae.gmap.utils.GLConvertUtil;
import com.autonavi.ae.gmap.utils.GLMapServerUtil;
import com.autonavi.ae.gmap.utils.GLMapUtil;
import com.qiniu.android.common.Constants;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

/* JADX INFO: loaded from: classes.dex */
public class GuideMapLoader extends BaseMapLoader {
    protected String mScenicAoiID;

    private String getScenicWidgetRequestParams() {
        return "";
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected void addRequestHeader(HttpURLConnection httpURLConnection) {
    }

    public GuideMapLoader(int i, GLMapEngine gLMapEngine, int i2) {
        super(i);
        this.mScenicAoiID = "";
        this.mGLMapEngine = gLMapEngine;
        this.mDataSource = i2;
        this.mCreateTime = System.currentTimeMillis();
    }

    public GuideMapLoader(int i, GLMapEngine gLMapEngine, int i2, String str) {
        super(i);
        this.mScenicAoiID = "";
        this.mGLMapEngine = gLMapEngine;
        this.mDataSource = i2;
        this.mCreateTime = System.currentTimeMillis();
        this.mScenicAoiID = str;
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected String getMapAddress() {
        return this.mDataSource == 101 ? GLMapServerUtil.BASE_AOS_URL_DATA_SERVER_PUBLIC : this.mGLMapEngine.getMapSvrAddress();
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected String getMapServerPath() {
        int i = this.mDataSource;
        if (i == 11) {
            return "/ws/mps/spot?";
        }
        if (i != 101) {
            return null;
        }
        return "/ws/valueadded/scenic/scenic_widget/?";
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected String getGridParams() {
        String gridParmaV4;
        if (this.mDataSource == 101) {
            gridParmaV4 = getScenicWidgetRequestParams();
        } else {
            gridParmaV4 = getGridParmaV4();
        }
        return gridParmaV4 + "&channel=amapapi";
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected boolean processReceivedDataHeader() {
        try {
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
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected void processReceivedDataByType() {
        try {
            super.processReceivedData();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected void processReceivedVersion() {
        try {
            if (this.mDataSource == 9) {
                super.processReceivedVersionData(this.mReceivedDataBuffer, 0, this.mReceivedDataSize);
            } else if (this.mDataSource == 101) {
                processScenicWidgetData(this.mReceivedDataBuffer, 0, this.mReceivedDataSize);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected void processReceivedTileData(byte[] bArr, int i, int i2) {
        try {
            if (i != 0 && this.mDataSource == 11) {
                processReceivedTileDataBmp(bArr, i, i2);
            } else {
                super.processReceivedTileData(bArr, i, i2);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    public boolean isRequestValid() {
        if (this.mDataSource == 101) {
            return true;
        }
        return this.mGLMapEngine.isGridsInScreen(this.mEngineID, this.mMapTiles, this.mDataSource);
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected boolean isNeedReturn() {
        return this.mDataSource == 101 || this.mDataSource == 9;
    }

    protected void processReceivedTileDataBmp(byte[] bArr, int i, int i2) throws UnsupportedEncodingException {
        int i3 = i + 4;
        int i4 = i3 + 1;
        int i5 = bArr[i3];
        String str = (i5 <= 0 || (i4 + i5) - 1 >= i2) ? "" : new String(bArr, i4, i5, Constants.UTF_8);
        if (!this.mGLMapEngine.isMapEngineValid() || i2 <= i) {
            return;
        }
        this.mGLMapEngine.putMapData(this.mEngineID, bArr, i, i2 - i, this.mDataSource, 0);
        if (this.mGLMapEngine.GetCurrentGrideNameLen(this.mEngineID) == str.length() ? true ^ this.mGLMapEngine.isGridsInScreen(this.mEngineID, this.mMapTiles, this.mDataSource) : true) {
            super.doCancel();
        }
    }

    private String getGridParmaV4() {
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
        if (string.length() > 0 && this.mDataSource == 11) {
            return "mesh=" + string;
        }
        return null;
    }

    private void processScenicWidgetData(byte[] bArr, int i, int i2) {
        if (bArr == null || i2 <= 0 || i2 > bArr.length) {
            return;
        }
        this.mGLMapEngine.putScenicData(this.mEngineID, bArr, i2, this.mScenicAoiID);
    }
}
