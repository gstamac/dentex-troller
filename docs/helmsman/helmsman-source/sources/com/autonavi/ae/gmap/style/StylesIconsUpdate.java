package com.autonavi.ae.gmap.style;

import android.text.TextUtils;
import com.autonavi.ae.gmap.GLMapEngine;
import com.autonavi.ae.gmap.utils.GLConvertUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/* JADX INFO: loaded from: classes.dex */
public class StylesIconsUpdate extends Thread {
    private int mEngineID;
    GLMapEngine mMapEngine;
    MapTilsCacheAndResManager.RetStyleIconsFile mRetFileRecoder;
    private String url;
    ByteArrayOutputStream netWorkData = null;
    int mReceiveDataLen = 0;
    private volatile boolean canceled = false;
    private boolean successed = false;
    private final int CONNECTION_TIMEOUT = 10000;

    public StylesIconsUpdate(int i, GLMapEngine gLMapEngine, MapTilsCacheAndResManager.RetStyleIconsFile retStyleIconsFile) {
        this.mEngineID = i;
        this.mRetFileRecoder = retStyleIconsFile;
        this.mMapEngine = gLMapEngine;
    }

    public void cancel() {
        this.canceled = true;
    }

    public synchronized void start(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.url = new String(str);
        super.start();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v10, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r0v12 */
    /* JADX WARN: Type inference failed for: r0v13 */
    /* JADX WARN: Type inference failed for: r0v14 */
    /* JADX WARN: Type inference failed for: r0v15 */
    /* JADX WARN: Type inference failed for: r0v16, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r0v18, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r0v19 */
    /* JADX WARN: Type inference failed for: r0v20, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r0v23 */
    /* JADX WARN: Type inference failed for: r0v24 */
    /* JADX WARN: Type inference failed for: r0v25 */
    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws Throwable {
        if (TextUtils.isEmpty(this.url)) {
            putErrordata(this.mMapEngine);
            return;
        }
        ?? r0 = this.url + ("mapdataver=6&type=30&name=" + this.mRetFileRecoder.name + "&cv=" + this.mRetFileRecoder.clientVersion + "&sv=" + this.mRetFileRecoder.serverVersion);
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        httpURLConnection = null;
        httpURLConnection = null;
        httpURLConnection = null;
        try {
            try {
                try {
                    HttpURLConnection httpURLConnection2 = (HttpURLConnection) new URL(r0).openConnection();
                    try {
                        httpURLConnection2.setConnectTimeout(10000);
                        httpURLConnection2.setReadTimeout(10000);
                        httpURLConnection2.connect();
                        if (httpURLConnection2.getResponseCode() == 200) {
                            inputStream = httpURLConnection2.getInputStream();
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(inputStream.available() + 100);
                            this.netWorkData = byteArrayOutputStream;
                            getResourceHeaderData(byteArrayOutputStream);
                            byte[] bArr = new byte[512];
                            while (true) {
                                int i = inputStream.read(bArr);
                                if (i <= -1 || this.canceled) {
                                    break;
                                }
                                this.netWorkData.write(bArr, 0, i);
                                this.mReceiveDataLen += i;
                            }
                            if (this.canceled || this.mReceiveDataLen <= 11) {
                                putErrordata(this.mMapEngine);
                            } else {
                                GLMapEngine gLMapEngine = this.mMapEngine;
                                if (gLMapEngine != null) {
                                    gLMapEngine.PutResourceData(this.mEngineID, this.netWorkData.toByteArray());
                                }
                            }
                        } else {
                            putErrordata(this.mMapEngine);
                        }
                        if (httpURLConnection2 != null) {
                            try {
                                httpURLConnection2.disconnect();
                            } catch (Exception unused) {
                            }
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } catch (IOException e) {
                        e = e;
                        httpURLConnection = httpURLConnection2;
                        r0 = 0;
                        putErrordata(this.mMapEngine);
                        e.printStackTrace();
                        if (httpURLConnection != null) {
                            try {
                                httpURLConnection.disconnect();
                            } catch (Exception unused2) {
                            }
                        }
                        if (r0 != 0) {
                            r0.close();
                        }
                    } catch (IndexOutOfBoundsException unused3) {
                        httpURLConnection = httpURLConnection2;
                        r0 = 0;
                        putErrordata(this.mMapEngine);
                        if (httpURLConnection != null) {
                            try {
                                httpURLConnection.disconnect();
                            } catch (Exception unused4) {
                            }
                        }
                        if (r0 != 0) {
                            r0.close();
                        }
                    } catch (Throwable th) {
                        th = th;
                        httpURLConnection = httpURLConnection2;
                        r0 = 0;
                        if (httpURLConnection != null) {
                            try {
                                httpURLConnection.disconnect();
                            } catch (Exception unused5) {
                            }
                        }
                        if (r0 == 0) {
                            throw th;
                        }
                        try {
                            r0.close();
                            throw th;
                        } catch (IOException e2) {
                            e2.printStackTrace();
                            throw th;
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (IOException e3) {
                e = e3;
                r0 = 0;
            } catch (IndexOutOfBoundsException unused6) {
                r0 = 0;
            } catch (Throwable th3) {
                th = th3;
                r0 = 0;
            }
        } catch (IOException e4) {
            e4.printStackTrace();
        }
    }

    private int getResourceHeaderData(ByteArrayOutputStream byteArrayOutputStream) {
        int i = this.mRetFileRecoder.type;
        int i2 = Integer.parseInt(this.mRetFileRecoder.fullName.split("_|\\.")[1]);
        byte[] bArrConvertInt = GLConvertUtil.convertInt(i);
        byteArrayOutputStream.write(bArrConvertInt, 0, bArrConvertInt.length);
        byte[] bArrConvertInt2 = GLConvertUtil.convertInt(i2);
        byteArrayOutputStream.write(bArrConvertInt2, 0, bArrConvertInt2.length);
        byte[] bArrConvertInt3 = GLConvertUtil.convertInt(this.mRetFileRecoder.clientVersion);
        byteArrayOutputStream.write(bArrConvertInt3, 0, bArrConvertInt3.length);
        byte[] bArrConvertInt4 = GLConvertUtil.convertInt(this.mRetFileRecoder.serverVersion);
        byteArrayOutputStream.write(bArrConvertInt4, 0, bArrConvertInt4.length);
        return byteArrayOutputStream.size();
    }

    private void putErrordata(GLMapEngine gLMapEngine) {
        if (gLMapEngine != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(100);
            getResourceHeaderData(byteArrayOutputStream);
            byte[] bArrConvertInt = GLConvertUtil.convertInt(-1);
            byteArrayOutputStream.write(bArrConvertInt, 0, bArrConvertInt.length);
            gLMapEngine.PutResourceData(this.mEngineID, byteArrayOutputStream.toByteArray());
        }
    }
}
