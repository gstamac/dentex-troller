package com.autonavi.ae.gmap.maploader;

import android.text.TextUtils;
import com.amap.api.mapcore.util.dg;
import com.amap.api.mapcore.util.o;
import com.autonavi.ae.gmap.GLMapEngine;
import com.autonavi.ae.gmap.utils.GLConvertUtil;
import com.autonavi.ae.gmap.utils.GLMapUtil;
import com.qiniu.android.common.Constants;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

/* JADX INFO: loaded from: classes.dex */
public class NormalMapLoader extends BaseMapLoader {
    private int mMapLevel;
    private int mMapMode;
    private int mMapModeState;

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected void addRequestHeader(HttpURLConnection httpURLConnection) {
    }

    public NormalMapLoader(int i, GLMapEngine gLMapEngine, int i2) {
        super(i);
        this.mMapLevel = 0;
        this.mMapMode = 0;
        this.mMapModeState = 0;
        this.mGLMapEngine = gLMapEngine;
        this.mDataSource = i2;
        this.mCreateTime = System.currentTimeMillis();
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected String getMapAddress() {
        return this.mGLMapEngine.getMapSvrAddress();
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected String getMapServerPath() {
        int i = this.mDataSource;
        if (i != 0 && i != 1) {
            if (i != 2) {
                if (i == 3) {
                    return "/ws/mps/smap?";
                }
                if (i == 4) {
                    return "/ws/mps/rtt?";
                }
                if (i != 15) {
                    switch (i) {
                        case 6:
                            break;
                        case 7:
                        case 8:
                        case 9:
                            break;
                        default:
                            return null;
                    }
                }
            }
            return "/amapsrv/MPS?";
        }
        return "/ws/mps/vmap?";
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected String getGridParams() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < this.mMapTiles.size(); i++) {
            String gridName = this.mMapTiles.get(i).getGridName();
            if (!TextUtils.isEmpty(gridName) && !super.isContainIllegal(gridName) && GLMapUtil.isAssic(gridName)) {
                if (this.mDataSource == 4 && this.mMapTiles.get(i).mObj != null) {
                    try {
                        gridName = gridName + "-" + URLEncoder.encode((String) this.mMapTiles.get(i).mObj, Constants.UTF_8);
                        stringBuffer.append(gridName + ";");
                    } catch (UnsupportedEncodingException unused) {
                    }
                } else {
                    stringBuffer.append(gridName + ";");
                }
            }
        }
        String string = stringBuffer.toString();
        String str = null;
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        while (true) {
            if (!string.endsWith(";") && !string.endsWith(" ")) {
                break;
            }
            string = string.substring(0, string.length() - 1);
        }
        int i2 = this.mDataSource;
        if (i2 != 0) {
            if (i2 != 1) {
                if (i2 != 2) {
                    if (i2 != 3) {
                        if (i2 != 4) {
                            if (i2 == 15) {
                                str = "type=2&mesh=" + string + getMapParams();
                            } else {
                                switch (i2) {
                                    case 6:
                                        str = "t=VMMV3&type=mod&cp=0&mid=" + string;
                                        break;
                                    case 7:
                                        str = "type=1&mesh=" + string + getMapParams();
                                        break;
                                    case 8:
                                        str = "type=4&mesh=" + string + getMapParams();
                                        break;
                                    case 9:
                                        str = "type=40&mesh=" + string + getMapParams();
                                        break;
                                }
                            }
                        } else {
                            str = "v=6.0.0&bver=2&mesh=" + string + "&diu=" + dg.a(o.a);
                        }
                    } else {
                        str = "mesh=" + string + getMapParams();
                    }
                } else {
                    str = "t=BMPBM&mesh=" + string;
                }
            } else {
                str = "type=11&mesh=" + string + getMapParams();
            }
        } else {
            str = "type=20&mesh=" + string + getMapParams();
        }
        return str + "&channel=amapapi";
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
            if (this.mDataSource == 0 || this.mDataSource == 1 || this.mDataSource == 8 || this.mDataSource == 7 || this.mDataSource == 15) {
                super.processReceivedDataV4();
            } else {
                super.processReceivedData();
            }
            return true;
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected void processReceivedDataByType() {
        try {
            if (this.mDataSource == 0 || this.mDataSource == 1 || this.mDataSource == 8 || this.mDataSource == 7 || this.mDataSource == 15) {
                super.processReceivedDataV4();
            } else {
                super.processReceivedData();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected void processReceivedVersion() {
        try {
            if (this.mDataSource == 9) {
                super.processReceivedVersionData(this.mReceivedDataBuffer, 0, this.mReceivedDataSize);
            }
        } catch (Throwable th) {
            th.printStackTrace();
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

    @Override // com.autonavi.ae.gmap.maploader.BaseMapLoader
    protected void processReceivedTileData(byte[] bArr, int i, int i2) {
        try {
            if (i == 0) {
                super.processReceivedTileData(bArr, i, i2);
            } else if (this.mDataSource == 2 || this.mDataSource == 3) {
                processReceivedTileDataBmp(bArr, i, i2);
            } else if (this.mDataSource == 4) {
                processReceivedTileDataVTmc(bArr, i, i2);
            } else if (this.mDataSource == 6) {
                processReceivedModels(bArr, i, i2);
            } else {
                super.processReceivedTileData(bArr, i, i2);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
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
                this.mGLMapEngine.putMapData(this.mEngineID, bArr, i, i2 - i, this.mDataSource, 0);
                if (this.mGLMapEngine.GetCurrentGrideNameLen(this.mEngineID) >= str.length() ? true ^ this.mGLMapEngine.isGridsInScreen(this.mEngineID, this.mMapTiles, this.mDataSource) : true) {
                    super.doCancel();
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

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
                        this.mGLMapEngine.putMapTMCData(this.mEngineID, this.mDataSource, bArr2, str, false);
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

    protected void processReceivedModels(byte[] bArr, int i, int i2) {
        int i3 = i + 1;
        try {
            byte b = bArr[i];
            if (b < 0) {
                return;
            }
            String str = new String(bArr, i3, b, Constants.UTF_8);
            if (this.mGLMapEngine.isMapEngineValid() && i2 > i) {
                this.mGLMapEngine.putMapData(this.mEngineID, bArr, i, i2 - i, this.mDataSource, 0);
                if (this.mGLMapEngine.GetCurrentGrideNameLen(this.mEngineID) >= str.length() ? true ^ this.mGLMapEngine.isGridsInScreen(this.mEngineID, this.mMapTiles, this.mDataSource) : true) {
                    super.doCancel();
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setMapParams(int i, int i2, int i3) {
        this.mMapLevel = i;
        this.mMapMode = i2;
        this.mMapModeState = i3;
    }

    public String getMapParams() {
        return "&zoom=" + this.mMapLevel + "&mode=" + this.mMapMode + "&state=" + this.mMapModeState;
    }
}
