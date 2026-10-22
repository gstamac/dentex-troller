package com.autonavi.ae.gmap.maploader;

import com.autonavi.ae.gmap.GLMapEngine;
import com.autonavi.ae.gmap.utils.GLConvertUtil;
import java.net.HttpURLConnection;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class OpenLayerMapLoader extends BaseMapLoader {
    static final int DATA_OPENLAYER_TYPEGEOMETRY = 17;
    private static ArrayList<DownTilesInfor> g_arrLayerDownBuffer = new ArrayList<>();
    private byte[] mArrblayerid;
    private byte[] mArrbsublayerid;
    private int mLayerid;
    private String mStrGridParams;
    private String mStrPath;
    private String mStrSeverAdd;
    private int mSubLayerid;

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected void addRequestHeader(HttpURLConnection httpURLConnection) {
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected boolean isNeedReturn() {
        return false;
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    public boolean isRequestValid() {
        return true;
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected void processReceivedVersion() {
    }

    protected class DownTilesInfor {
        String mStrTile;
        int mSubLayerID;

        protected DownTilesInfor() {
        }
    }

    public OpenLayerMapLoader(int i, GLMapEngine gLMapEngine, String str, int i2, int i3, int i4) {
        super(i);
        this.mGLMapEngine = gLMapEngine;
        int iIndexOf = str.indexOf("://");
        if (iIndexOf < 0) {
            return;
        }
        this.mDataSource = i2;
        int iIndexOf2 = str.indexOf("/", iIndexOf + 4) + 1;
        this.mStrSeverAdd = str.substring(0, iIndexOf2);
        String strSubstring = str.substring(iIndexOf2);
        this.mStrPath = strSubstring;
        if (strSubstring.indexOf(63) < 0) {
            this.mStrPath += '?';
        }
        this.mLayerid = i3;
        this.mArrblayerid = GLConvertUtil.convertInt(i3);
        this.mSubLayerid = i4;
        this.mArrbsublayerid = GLConvertUtil.convertInt(i4);
    }

    protected boolean isExistAndAdd(int i, String str) {
        synchronized (g_arrLayerDownBuffer) {
            for (int i2 = 0; i2 < g_arrLayerDownBuffer.size(); i2++) {
                if (g_arrLayerDownBuffer.get(i2).mSubLayerID == i && str.compareTo(g_arrLayerDownBuffer.get(i2).mStrTile) == 0) {
                    return true;
                }
            }
            DownTilesInfor downTilesInfor = new DownTilesInfor();
            downTilesInfor.mSubLayerID = i;
            downTilesInfor.mStrTile = str;
            g_arrLayerDownBuffer.add(downTilesInfor);
            return false;
        }
    }

    public static void removeDownTile(int i, String str) {
        synchronized (g_arrLayerDownBuffer) {
            for (int i2 = 0; i2 < g_arrLayerDownBuffer.size(); i2++) {
                if (g_arrLayerDownBuffer.get(i2).mSubLayerID == i && str.compareTo(g_arrLayerDownBuffer.get(i2).mStrTile) == 0) {
                    g_arrLayerDownBuffer.remove(i2);
                    return;
                }
            }
        }
    }

    public boolean createDownUrl(String[] strArr) {
        this.mStrGridParams = String.format("type=50&mapdataver=6&aetraffic=8&lyrtype=%d&id=%d&mesh=", Integer.valueOf(17 == this.mDataSource ? 4 : 1), Integer.valueOf(this.mSubLayerid));
        String str = "";
        for (int i = 0; i < strArr.length; i++) {
            if (!isExistAndAdd(this.mSubLayerid, strArr[i])) {
                MapSourceGridData mapSourceGridDataObtain = MapSourceGridData.obtain();
                mapSourceGridDataObtain.setGridData(strArr[i], this.mDataSource, 0, 0);
                this.mMapTiles.add(mapSourceGridDataObtain);
                str = (str + strArr[i]) + ';';
            }
        }
        if (str.length() > 1) {
            this.mStrGridParams += str.substring(0, str.length() - 1);
        }
        return str.length() > 1;
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected String getMapAddress() {
        return this.mStrSeverAdd;
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected String getMapServerPath() {
        return this.mStrPath;
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected String getGridParams() {
        String str = this.mStrGridParams + "&channel=amapapi";
        this.mStrGridParams = str;
        return str;
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
        super.processReceivedDataV4();
        return true;
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected void putMapData(byte[] bArr, int i, int i2, int i3, int i4, String str) {
        int i5 = i2 + 8;
        byte[] bArr2 = new byte[i5];
        System.arraycopy(this.mArrblayerid, 0, bArr2, 0, 4);
        System.arraycopy(this.mArrbsublayerid, 0, bArr2, 4, 4);
        System.arraycopy(bArr, i, bArr2, 8, i2);
        if (this.mGLMapEngine != null) {
            this.mGLMapEngine.putMapData(this.mEngineID, bArr2, 0, i5, i3, i4);
            this.mGLMapEngine.resetRenderTime(this.mEngineID);
        }
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected void downLoadOver(int i) {
        int size = this.mMapTiles.size();
        for (int i2 = 0; i2 < size; i2++) {
            removeDownTile(this.mSubLayerid, this.mMapTiles.get(i2).getGridName());
        }
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected void processReceivedDataByType() {
        super.processReceivedDataV4();
    }
}
