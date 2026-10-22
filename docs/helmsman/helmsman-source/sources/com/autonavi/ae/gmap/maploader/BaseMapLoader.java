package com.autonavi.ae.gmap.maploader;

import android.text.TextUtils;
import com.autonavi.ae.gmap.GLMapEngine;
import com.autonavi.ae.gmap.utils.GLConvertUtil;
import com.autonavi.ae.gmap.utils.GLMapStaticValue;
import com.qiniu.android.common.Constants;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseMapLoader {
    private static final int CONNECTION_TIMEOUT = 20000;
    protected long mCreateTime;
    protected int mEngineID;
    protected GLMapEngine mGLMapEngine;
    protected byte[] mReceivedDataBuffer;
    public List<MapSourceGridData> mMapTiles = new ArrayList();
    private HttpURLConnection mURLConnection = null;
    private boolean isGetRequestMethod = true;
    protected int mDataSource = 0;
    protected int mReceivedDataSize = 0;
    protected int mNextImgDataLength = 0;
    protected int mCapaticy = 30720;
    protected int mCapaticyExt = 10240;
    protected boolean isReceivedHeader = false;
    protected volatile boolean isInRequest = false;
    protected volatile boolean isFinished = false;
    protected volatile boolean isCanceled = false;

    protected abstract void addRequestHeader(HttpURLConnection httpURLConnection);

    protected void downLoadOver(int i) {
    }

    protected abstract String getGridParams();

    protected abstract String getMapAddress();

    protected abstract String getMapServerPath();

    protected abstract boolean isNeedReturn();

    public abstract boolean isRequestValid();

    protected boolean processHttpResponse(HttpURLConnection httpURLConnection) {
        return false;
    }

    protected abstract void processReceivedDataByType();

    protected abstract boolean processReceivedDataHeader();

    protected abstract void processReceivedVersion();

    public BaseMapLoader(int i) {
        this.mEngineID = i;
    }

    public void setGetRequestMethod(boolean z) {
        this.isGetRequestMethod = z;
    }

    public void addRequestTiles(MapSourceGridData mapSourceGridData) {
        List<MapSourceGridData> list = this.mMapTiles;
        if (list != null) {
            list.add(mapSourceGridData);
        }
    }

    protected String getRequestParams(String str) {
        return ("mapdataver=6&" + str) + "&aetraffic=8";
    }

    protected void processReceivedDataV4() {
        try {
            int i = this.mNextImgDataLength;
            if (i == 0) {
                if (this.mReceivedDataSize >= 8) {
                    this.mNextImgDataLength = GLConvertUtil.getInt(this.mReceivedDataBuffer, 0) + 8;
                    processReceivedDataV4();
                }
            } else if (this.mReceivedDataSize >= i) {
                processReceivedTileDataV4(this.mReceivedDataBuffer, 8, i);
                byte[] bArr = this.mReceivedDataBuffer;
                if (bArr != null) {
                    int i2 = this.mNextImgDataLength;
                    GLConvertUtil.moveArray(bArr, i2, bArr, 0, this.mReceivedDataSize - i2);
                    this.mReceivedDataSize -= this.mNextImgDataLength;
                    this.mNextImgDataLength = 0;
                    processReceivedDataV4();
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    protected void processReceivedTileDataV4(byte[] bArr, int i, int i2) throws UnsupportedEncodingException {
        int i3 = i + 4;
        int i4 = i3 + 1;
        int i5 = bArr[i3];
        String str = (i5 <= 0 || (i4 + i5) - 1 >= i2) ? "" : new String(bArr, i4, i5, Constants.UTF_8);
        if (this.mGLMapEngine.isMapEngineValid() && i2 > i) {
            putMapData(bArr, i, i2 - i, this.mDataSource, 0, str);
            if (this.mGLMapEngine.GetCurrentGrideNameLen(this.mEngineID) >= str.length() ? true ^ this.mGLMapEngine.isGridsInScreen(this.mEngineID, this.mMapTiles, this.mDataSource) : true) {
                doCancel();
            }
        }
    }

    protected void putMapData(byte[] bArr, int i, int i2, int i3, int i4, String str) {
        this.mGLMapEngine.putMapData(this.mEngineID, bArr, i, i2, i3, i4);
    }

    protected void processReceivedData() {
        byte[] bArr;
        try {
            int i = this.mNextImgDataLength;
            if (i == 0) {
                if (this.mReceivedDataSize >= 8) {
                    this.mNextImgDataLength = GLConvertUtil.getInt(this.mReceivedDataBuffer, 0) + 8;
                    processReceivedData();
                    return;
                }
                return;
            }
            if (this.mReceivedDataSize >= i) {
                int i2 = GLConvertUtil.getInt(this.mReceivedDataBuffer, 0);
                int i3 = GLConvertUtil.getInt(this.mReceivedDataBuffer, 4);
                if (i3 == 0) {
                    processReceivedTileData(this.mReceivedDataBuffer, 8, i2 + 8);
                } else {
                    try {
                        GZIPInputStream gZIPInputStream = new GZIPInputStream(new ByteArrayInputStream(this.mReceivedDataBuffer, 8, i2));
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        byte[] bArr2 = new byte[128];
                        while (true) {
                            int i4 = gZIPInputStream.read(bArr2);
                            if (i4 <= -1) {
                                break;
                            } else {
                                byteArrayOutputStream.write(bArr2, 0, i4);
                            }
                        }
                        processReceivedTileData(byteArrayOutputStream.toByteArray(), 0, i3);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                int i5 = this.mNextImgDataLength;
                if (i5 <= 0 || (bArr = this.mReceivedDataBuffer) == null) {
                    return;
                }
                GLConvertUtil.moveArray(bArr, i5, bArr, 0, this.mReceivedDataSize - i5);
                this.mReceivedDataSize -= this.mNextImgDataLength;
                this.mNextImgDataLength = 0;
                processReceivedData();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    protected void processReceivedTileData(byte[] bArr, int i, int i2) {
        int i3 = i + 2 + 2 + 4;
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
                    doCancel();
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    protected void processReceivedVersionData(byte[] bArr, int i, int i2) {
        int i3;
        if (i2 > 0) {
            try {
                if (i2 <= bArr.length) {
                    boolean z = false;
                    if (GLConvertUtil.getInt(bArr, 0) == 0 && GLConvertUtil.getInt(bArr, 4) == 0) {
                        int i4 = GLConvertUtil.getInt(bArr, 8);
                        ArrayList arrayList = new ArrayList();
                        int i5 = 12;
                        int i6 = 0;
                        while (true) {
                            if (i6 >= i4) {
                                z = true;
                                break;
                            }
                            if (i5 >= i2) {
                                break;
                            }
                            int i7 = i5 + 1;
                            int i8 = bArr[i5];
                            if (i8 <= 0 || (i3 = i7 + i8) >= i2) {
                                break;
                            }
                            arrayList.add(new String(bArr, i7, i8, Constants.UTF_8));
                            i5 = i3 + 4;
                            i6++;
                        }
                        if (z) {
                            this.mGLMapEngine.putMapData(this.mEngineID, bArr, 0, i2, this.mDataSource, 0);
                        }
                    }
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    protected boolean isContainIllegal(String str) {
        return str.contains("<") || str.contains("[");
    }

    public synchronized boolean hasFinished() {
        return this.isCanceled || this.isFinished;
    }

    public void onException(int i) {
        int i2 = this.mDataSource;
        if (i2 != 6 && i2 != 4 && i2 != 1) {
            if (this.isCanceled) {
                this.mGLMapEngine.onException(this.mEngineID, 1004);
            } else if (this.mDataSource == 10) {
                this.mGLMapEngine.onException(this.mEngineID, i);
            } else {
                this.mGLMapEngine.onException(this.mEngineID, i);
                this.mGLMapEngine.setParamater(this.mEngineID, GLMapStaticValue.AM_PARAMETERNAME_NETWORK, i, 0, 0, 0);
            }
        }
        downLoadOver(i);
        this.isFinished = true;
    }

    private void onConnectionReceivedData(byte[] bArr, int i) {
        int i2 = this.mCapaticy;
        int i3 = this.mReceivedDataSize;
        if (i2 < i3 + i) {
            try {
                int i4 = i2 + this.mCapaticyExt;
                this.mCapaticy = i4;
                byte[] bArr2 = new byte[i4];
                System.arraycopy(this.mReceivedDataBuffer, 0, bArr2, 0, i3);
                this.mReceivedDataBuffer = bArr2;
            } catch (ArrayIndexOutOfBoundsException unused) {
                doCancel();
                return;
            } catch (OutOfMemoryError unused2) {
                doCancel();
                return;
            }
        }
        try {
            System.arraycopy(bArr, 0, this.mReceivedDataBuffer, this.mReceivedDataSize, i);
            this.mReceivedDataSize += i;
            if (isNeedReturn()) {
                return;
            }
            if (this.isReceivedHeader || processReceivedDataHeader()) {
                processReceivedDataByType();
            }
        } catch (ArrayIndexOutOfBoundsException unused3) {
            doCancel();
        } catch (Exception unused4) {
            doCancel();
        }
    }

    protected void onConnectionOpened() {
        this.mReceivedDataBuffer = new byte[this.mCapaticy];
        this.mNextImgDataLength = 0;
        this.mReceivedDataSize = 0;
        this.isReceivedHeader = false;
    }

    private synchronized void onConnectionOver() {
        if (this.mURLConnection != null && this.isInRequest) {
            this.isInRequest = false;
            try {
                this.mURLConnection.disconnect();
            } catch (Exception unused) {
            }
        }
        processReceivedVersion();
        this.mReceivedDataBuffer = null;
        this.mNextImgDataLength = 0;
        this.mReceivedDataSize = 0;
        for (int i = 0; i < this.mMapTiles.size(); i++) {
            try {
                this.mGLMapEngine.mTileProcessCtrl.removeTile(this.mMapTiles.get(i).getKeyGridName());
            } catch (Exception unused2) {
            }
        }
        downLoadOver(0);
        this.isFinished = true;
    }

    public synchronized void doCancel() {
        if (!this.isCanceled && !this.isFinished) {
            this.isCanceled = true;
            onConnectionOver();
        }
    }

    public void doRequest() {
        String str;
        if (this.isCanceled || this.isFinished) {
            return;
        }
        if (!isRequestValid()) {
            doCancel();
            return;
        }
        String mapAddress = getMapAddress();
        String mapServerPath = getMapServerPath();
        String gridParams = getGridParams();
        if (TextUtils.isEmpty(mapAddress) || TextUtils.isEmpty(mapServerPath) || TextUtils.isEmpty(gridParams)) {
            doCancel();
            return;
        }
        String str2 = mapAddress + mapServerPath;
        String requestParams = getRequestParams(gridParams);
        if (this.isGetRequestMethod) {
            str = (str2 + requestParams) + "&csid=" + UUID.randomUUID().toString();
        } else {
            str = str2 + "csid=" + UUID.randomUUID().toString();
        }
        InputStream inputStream = null;
        try {
            try {
                try {
                    URL url = new URL(str);
                    if (this.mURLConnection == null) {
                        this.mURLConnection = (HttpURLConnection) url.openConnection();
                    }
                    this.mURLConnection.setConnectTimeout(20000);
                    this.mURLConnection.setReadTimeout(20000);
                    addRequestHeader(this.mURLConnection);
                    if (this.isGetRequestMethod) {
                        this.mURLConnection.setRequestMethod("GET");
                    } else {
                        this.mURLConnection.setRequestMethod("POST");
                        this.mURLConnection.setDoInput(true);
                        this.mURLConnection.setDoOutput(true);
                        this.mURLConnection.setUseCaches(false);
                        this.mURLConnection.setRequestProperty("User-Agent", this.mGLMapEngine.mUserAgent);
                        byte[] bytes = requestParams.getBytes(Constants.UTF_8);
                        OutputStream outputStream = this.mURLConnection.getOutputStream();
                        outputStream.write(bytes);
                        outputStream.flush();
                        outputStream.close();
                    }
                    this.isInRequest = true;
                    this.mURLConnection.connect();
                    if (processHttpResponse(this.mURLConnection)) {
                        onConnectionOver();
                        return;
                    }
                    if (this.mURLConnection.getResponseCode() == 200) {
                        onConnectionOpened();
                        inputStream = this.mURLConnection.getInputStream();
                        byte[] bArr = new byte[512];
                        while (true) {
                            int i = inputStream.read(bArr);
                            if (i <= -1 || this.isCanceled) {
                                break;
                                break;
                            }
                            onConnectionReceivedData(bArr, i);
                        }
                    } else if (this.mDataSource != 10) {
                        onException(1002);
                    } else if (((IndoorMapLoader) this).switchFloorFailed()) {
                        onException(1007);
                    }
                    onConnectionOver();
                    if (inputStream == null || this.isCanceled) {
                        return;
                    }
                    try {
                        inputStream.close();
                    } catch (IOException unused) {
                        onException(1005);
                    } catch (ArrayIndexOutOfBoundsException | NullPointerException unused2) {
                    }
                } catch (Throwable th) {
                    onConnectionOver();
                    if (0 != 0 && !this.isCanceled) {
                        try {
                            inputStream.close();
                        } catch (IOException unused3) {
                            onException(1005);
                        } catch (ArrayIndexOutOfBoundsException | NullPointerException unused4) {
                        }
                    }
                    throw th;
                }
            } catch (SocketTimeoutException unused5) {
                if (this.mDataSource == 10 && ((IndoorMapLoader) this).switchFloorFailed()) {
                    onException(1007);
                }
                onConnectionOver();
                if (0 == 0 || this.isCanceled) {
                }
            } catch (Exception unused6) {
                onConnectionOver();
                if (0 == 0 || this.isCanceled) {
                }
            }
        } catch (IOException unused7) {
            onException(1005);
            onConnectionOver();
            if (0 == 0 || this.isCanceled) {
            }
        } catch (IllegalStateException unused8) {
            onConnectionOver();
            if (0 == 0 || this.isCanceled) {
            }
        } catch (IndexOutOfBoundsException unused9) {
            onConnectionOver();
            if (0 == 0 || this.isCanceled) {
            }
        } catch (NullPointerException unused10) {
            onConnectionOver();
            if (0 == 0 || this.isCanceled) {
            }
        } catch (NumberFormatException unused11) {
            onConnectionOver();
            if (0 == 0 || this.isCanceled) {
            }
        } catch (OutOfMemoryError unused12) {
            onConnectionOver();
            if (0 == 0 || this.isCanceled) {
            }
        }
    }
}
