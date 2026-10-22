package com.autonavi.ae.gmap.maploader;

import android.text.TextUtils;
import cn.jpush.android.api.JThirdPlatFormInterface;
import com.autonavi.ae.gmap.GLMapEngine;
import com.autonavi.ae.gmap.utils.GLConvertUtil;
import com.autonavi.ae.gmap.utils.GLMD5Util;
import com.autonavi.ae.gmap.utils.GLMapStaticValue;
import com.autonavi.ae.gmap.utils.GLMapUtil;
import com.qiniu.android.common.Constants;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

/* JADX INFO: loaded from: classes.dex */
public class IndoorMapLoader extends BaseMapLoader {
    private static final int AM_INVALID_FLOOR = -9999;
    private static final String INDOOR_CHANNEL = "amap7";
    private static final String INDOOR_SIGN_KEY = "@1071a2a4e3gte2Uc32cY3a98Tf33H1c4Gc23f";

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected String getMapServerPath() {
        return "/ws/transfer/auth/map/indoor_maps/?";
    }

    public IndoorMapLoader(int i, GLMapEngine gLMapEngine, int i2) {
        super(i);
        this.mGLMapEngine = gLMapEngine;
        this.mDataSource = i2;
        this.mCreateTime = System.currentTimeMillis();
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected String getMapAddress() {
        return this.mGLMapEngine.getMapIndoorAddress();
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected String getGridParams() {
        return getIndoorRequestParams();
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected void addRequestHeader(HttpURLConnection httpURLConnection) {
        httpURLConnection.addRequestProperty(JThirdPlatFormInterface.KEY_PLATFORM, "Android");
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected boolean processReceivedDataHeader() {
        if (this.mReceivedDataSize <= 5) {
            return false;
        }
        this.mReceivedDataSize -= 6;
        if (this.mReceivedDataSize > 0) {
            GLConvertUtil.moveArray(this.mReceivedDataBuffer, 6, this.mReceivedDataBuffer, 0, this.mReceivedDataSize);
        }
        this.mNextImgDataLength = 0;
        this.isReceivedHeader = true;
        processReceivedIndoorData();
        return true;
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected void processReceivedDataByType() {
        processReceivedIndoorData();
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected void processReceivedVersion() {
        if (this.mDataSource == 9) {
            super.processReceivedVersionData(this.mReceivedDataBuffer, 0, this.mReceivedDataSize);
        }
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    public boolean isRequestValid() {
        return this.mGLMapEngine.isIndoorGridsInScreen(this.mMapTiles, this.mDataSource);
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected boolean isNeedReturn() {
        return this.mDataSource == 9;
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected boolean processHttpResponse(HttpURLConnection httpURLConnection) {
        String contentType = httpURLConnection.getContentType();
        if (TextUtils.isEmpty(contentType)) {
            return false;
        }
        if (!contentType.contains("application/json") && !contentType.contains("application/xml")) {
            return false;
        }
        this.mGLMapEngine.setParamater(this.mEngineID, GLMapStaticValue.AM_PARAMETERNAME_PROCESS_INDOOR, 0, 0, 0, 0);
        return true;
    }

    private void processReceivedIndoorData() {
        if (this.mNextImgDataLength == 0) {
            if (this.mReceivedDataSize >= 6) {
                this.mNextImgDataLength = GLConvertUtil.getInt2(this.mReceivedDataBuffer, 0);
                processReceivedIndoorData();
                return;
            }
            return;
        }
        if (this.mReceivedDataSize >= this.mNextImgDataLength) {
            processReceivedIndoorTileDataV4(this.mReceivedDataBuffer, 0, this.mNextImgDataLength);
            if (this.mReceivedDataBuffer != null) {
                GLConvertUtil.moveArray(this.mReceivedDataBuffer, this.mNextImgDataLength, this.mReceivedDataBuffer, 0, this.mReceivedDataSize - this.mNextImgDataLength);
                this.mReceivedDataSize -= this.mNextImgDataLength;
                this.mNextImgDataLength = 0;
                processReceivedIndoorData();
            }
        }
    }

    protected void processReceivedIndoorTileDataV4(byte[] bArr, int i, int i2) {
        int i3 = i + 4;
        int i4 = i3 + 1;
        try {
            int i5 = bArr[i3];
            if (i5 > 10) {
                return;
            }
            String str = "";
            if (i5 > 0 && (i4 + i5) - 1 < i2) {
                str = new String(bArr, i4, i5, Constants.UTF_8);
            }
            String str2 = str;
            int i6 = i4 + i5;
            if (this.mGLMapEngine.isMapEngineValid() && i2 > i3) {
                short short2 = GLConvertUtil.getShort2(bArr, i6);
                if (this.mGLMapEngine.putMapData(this.mEngineID, bArr, i3, i2 - i3, this.mDataSource, 0)) {
                    str2 = str2 + "-" + ((int) short2);
                }
                if (!this.mGLMapEngine.isIndoorGridInScreen(this.mDataSource, str2, short2)) {
                    super.doCancel();
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private String getIndoorRequestParams() {
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        StringBuffer stringBuffer3 = new StringBuffer();
        for (int i = 0; i < this.mMapTiles.size(); i++) {
            String gridName = this.mMapTiles.get(i).getGridName();
            int i2 = this.mMapTiles.get(i).mIndoorIndex;
            int i3 = this.mMapTiles.get(i).mIndoorVersion;
            if (!TextUtils.isEmpty(gridName) && !super.isContainIllegal(gridName) && GLMapUtil.isAssic(gridName)) {
                stringBuffer.append(gridName + ";");
                stringBuffer2.append(i2 + ";");
                stringBuffer3.append(i3 + ";");
            }
        }
        String string = stringBuffer.toString();
        if (!TextUtils.isEmpty(string) && (string.endsWith(";") || string.endsWith(" "))) {
            string = string.substring(0, string.length() - 1);
        }
        String string2 = stringBuffer2.toString();
        if (!TextUtils.isEmpty(string2) && (string2.endsWith(";") || string2.endsWith(" "))) {
            string2 = string2.substring(0, string2.length() - 1);
        }
        String string3 = stringBuffer3.toString();
        if (!TextUtils.isEmpty(string3) && (string3.endsWith(";") || string3.endsWith(" "))) {
            string3 = string3.substring(0, string3.length() - 1);
        }
        StringBuffer stringBuffer4 = new StringBuffer();
        try {
            stringBuffer4.append("from=AMAP_ENGINE_INDOOR_V4&").append("poiid=" + URLEncoder.encode(string, "UTF-8") + "&").append("floor=" + URLEncoder.encode(string2, "UTF-8") + "&").append("version=" + URLEncoder.encode(string3, "UTF-8") + "&").append("sign=" + getIndoorMD5Params(string) + "&").append("language=zh_CN&output=bin&channel=amap7&servicetype=unify&").append("zoomlevel=" + ((int) this.mGLMapEngine.getMapZoomer(this.mEngineID)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringBuffer4.toString();
    }

    private String getIndoorMD5Params(String str) {
        return GLMD5Util.getStringMD5(INDOOR_CHANNEL + str + INDOOR_SIGN_KEY).toUpperCase();
    }

    public boolean switchFloorFailed() {
        if (this.mMapTiles != null && this.mMapTiles.size() > 0) {
            int size = this.mMapTiles.size();
            for (int i = 0; i < size; i++) {
                MapSourceGridData mapSourceGridData = this.mMapTiles.get(i);
                if (mapSourceGridData != null && mapSourceGridData.mIndoorVersion == 0 && mapSourceGridData.mIndoorIndex != -9999) {
                    return true;
                }
            }
        }
        return false;
    }
}
