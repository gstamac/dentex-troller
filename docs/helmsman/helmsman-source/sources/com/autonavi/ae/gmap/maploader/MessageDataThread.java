package com.autonavi.ae.gmap.maploader;

import com.autonavi.ae.gmap.GLMapEngine;

/* JADX INFO: loaded from: classes.dex */
public class MessageDataThread extends Thread {
    private int mDataType;
    private int mDelMode;
    private int mEngineID;
    private GLMapEngine mGLMapEngine;
    private int mRequestMode;
    private int mType;

    public MessageDataThread(int i, GLMapEngine gLMapEngine, int i2, int i3, int i4, int i5) {
        this.mEngineID = i;
        this.mGLMapEngine = gLMapEngine;
        this.mType = i2;
        this.mDelMode = i3;
        this.mRequestMode = i4;
        this.mDataType = i5;
    }

    /* JADX WARN: Code restructure failed: missing block: B:37:0x00bd, code lost:
    
        if (r2 != 0) goto L33;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v10 */
    /* JADX WARN: Type inference failed for: r2v12 */
    /* JADX WARN: Type inference failed for: r2v13 */
    /* JADX WARN: Type inference failed for: r2v14 */
    /* JADX WARN: Type inference failed for: r2v16 */
    /* JADX WARN: Type inference failed for: r2v25 */
    /* JADX WARN: Type inference failed for: r2v26 */
    /* JADX WARN: Type inference failed for: r2v27 */
    /* JADX WARN: Type inference failed for: r2v28 */
    /* JADX WARN: Type inference failed for: r2v29 */
    /* JADX WARN: Type inference failed for: r2v3 */
    /* JADX WARN: Type inference failed for: r2v30 */
    /* JADX WARN: Type inference failed for: r2v31 */
    /* JADX WARN: Type inference failed for: r2v32 */
    /* JADX WARN: Type inference failed for: r2v33 */
    /* JADX WARN: Type inference failed for: r2v34 */
    /* JADX WARN: Type inference failed for: r2v4 */
    /* JADX WARN: Type inference failed for: r2v5, types: [java.net.HttpURLConnection] */
    /* JADX WARN: Type inference failed for: r2v6 */
    /* JADX WARN: Type inference failed for: r2v7, types: [java.net.HttpURLConnection] */
    /* JADX WARN: Type inference failed for: r2v8 */
    /* JADX WARN: Type inference failed for: r2v9 */
    @Override // java.lang.Thread, java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void run() throws java.lang.Throwable {
        /*
            r10 = this;
            java.lang.String r0 = "utf-8"
            com.autonavi.ae.gmap.GLMapEngine r1 = r10.mGLMapEngine
            java.lang.String r1 = r1.getMapSvrAddress()
            boolean r1 = android.text.TextUtils.isEmpty(r1)
            if (r1 == 0) goto Lf
            return
        Lf:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            com.autonavi.ae.gmap.GLMapEngine r2 = r10.mGLMapEngine
            java.lang.String r2 = r2.getMapSvrAddress()
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r2 = "/ws/mps/bmcache/?"
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            r2 = 0
            java.net.URL r3 = new java.net.URL     // Catch: java.lang.Throwable -> Lb1 java.lang.IndexOutOfBoundsException -> Lb3 java.io.IOException -> Lb9
            r3.<init>(r1)     // Catch: java.lang.Throwable -> Lb1 java.lang.IndexOutOfBoundsException -> Lb3 java.io.IOException -> Lb9
            java.net.URLConnection r1 = r3.openConnection()     // Catch: java.lang.Throwable -> Lb1 java.lang.IndexOutOfBoundsException -> Lb3 java.io.IOException -> Lb9
            java.net.HttpURLConnection r1 = (java.net.HttpURLConnection) r1     // Catch: java.lang.Throwable -> Lb1 java.lang.IndexOutOfBoundsException -> Lb3 java.io.IOException -> Lb9
            r2 = 20000(0x4e20, float:2.8026E-41)
            r1.setConnectTimeout(r2)     // Catch: java.lang.Throwable -> La9 java.lang.IndexOutOfBoundsException -> Lac java.io.IOException -> Lae
            r1.setReadTimeout(r2)     // Catch: java.lang.Throwable -> La9 java.lang.IndexOutOfBoundsException -> Lac java.io.IOException -> Lae
            r1.connect()     // Catch: java.lang.Throwable -> La9 java.lang.IndexOutOfBoundsException -> Lac java.io.IOException -> Lae
            int r2 = r1.getResponseCode()     // Catch: java.lang.Throwable -> La9 java.lang.IndexOutOfBoundsException -> Lac java.io.IOException -> Lae
            r3 = 200(0xc8, float:2.8E-43)
            if (r2 != r3) goto La3
            java.io.InputStream r2 = r1.getInputStream()     // Catch: java.lang.Throwable -> La9 java.lang.IndexOutOfBoundsException -> Lac java.io.IOException -> Lae
            java.io.ByteArrayOutputStream r3 = new java.io.ByteArrayOutputStream     // Catch: java.lang.Throwable -> La9 java.lang.IndexOutOfBoundsException -> Lac java.io.IOException -> Lae
            r3.<init>()     // Catch: java.lang.Throwable -> La9 java.lang.IndexOutOfBoundsException -> Lac java.io.IOException -> Lae
            r4 = 512(0x200, float:7.175E-43)
            byte[] r4 = new byte[r4]     // Catch: java.lang.Throwable -> La9 java.lang.IndexOutOfBoundsException -> Lac java.io.IOException -> Lae
        L54:
            int r5 = r2.read(r4)     // Catch: java.lang.Throwable -> La9 java.lang.IndexOutOfBoundsException -> Lac java.io.IOException -> Lae
            if (r5 <= 0) goto L5f
            r6 = 0
            r3.write(r4, r6, r5)     // Catch: java.lang.Throwable -> La9 java.lang.IndexOutOfBoundsException -> Lac java.io.IOException -> Lae
            goto L54
        L5f:
            r2.close()     // Catch: java.lang.Throwable -> La9 java.lang.IndexOutOfBoundsException -> Lac java.io.IOException -> Lae
            r3.flush()     // Catch: java.lang.Throwable -> La9 java.lang.IndexOutOfBoundsException -> Lac java.io.IOException -> Lae
            r3.close()     // Catch: java.lang.Throwable -> La9 java.lang.IndexOutOfBoundsException -> Lac java.io.IOException -> Lae
            java.lang.String r2 = new java.lang.String     // Catch: java.lang.Throwable -> La9 java.lang.IndexOutOfBoundsException -> Lac java.io.IOException -> Lae
            byte[] r3 = r3.toByteArray()     // Catch: java.lang.Throwable -> La9 java.lang.IndexOutOfBoundsException -> Lac java.io.IOException -> Lae
            r2.<init>(r3, r0)     // Catch: java.lang.Throwable -> La9 java.lang.IndexOutOfBoundsException -> Lac java.io.IOException -> Lae
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> La9 java.lang.IndexOutOfBoundsException -> Lac java.io.IOException -> Lae
            r3.<init>(r2)     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> La9 java.lang.IndexOutOfBoundsException -> Lac java.io.IOException -> Lae
            java.lang.String r2 = "code"
            int r2 = r3.optInt(r2)     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> La9 java.lang.IndexOutOfBoundsException -> Lac java.io.IOException -> Lae
            r4 = 1
            if (r2 != r4) goto La3
            java.lang.String r2 = "data"
            org.json.JSONObject r2 = r3.optJSONObject(r2)     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> La9 java.lang.IndexOutOfBoundsException -> Lac java.io.IOException -> Lae
            if (r2 == 0) goto La3
            com.autonavi.ae.gmap.GLMapEngine r3 = r10.mGLMapEngine     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> La9 java.lang.IndexOutOfBoundsException -> Lac java.io.IOException -> Lae
            int r4 = r10.mEngineID     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> La9 java.lang.IndexOutOfBoundsException -> Lac java.io.IOException -> Lae
            int r5 = r10.mType     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> La9 java.lang.IndexOutOfBoundsException -> Lac java.io.IOException -> Lae
            int r6 = r10.mDelMode     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> La9 java.lang.IndexOutOfBoundsException -> Lac java.io.IOException -> Lae
            int r7 = r10.mRequestMode     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> La9 java.lang.IndexOutOfBoundsException -> Lac java.io.IOException -> Lae
            int r8 = r10.mDataType     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> La9 java.lang.IndexOutOfBoundsException -> Lac java.io.IOException -> Lae
            java.lang.String r2 = r2.toString()     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> La9 java.lang.IndexOutOfBoundsException -> Lac java.io.IOException -> Lae
            byte[] r9 = r2.getBytes(r0)     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> La9 java.lang.IndexOutOfBoundsException -> Lac java.io.IOException -> Lae
            r3.setMapDataConrol(r4, r5, r6, r7, r8, r9)     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> La9 java.lang.IndexOutOfBoundsException -> Lac java.io.IOException -> Lae
            goto La3
        L9f:
            r0 = move-exception
            r0.printStackTrace()     // Catch: java.lang.Throwable -> La9 java.lang.IndexOutOfBoundsException -> Lac java.io.IOException -> Lae
        La3:
            if (r1 == 0) goto Lc0
            r1.disconnect()     // Catch: java.lang.Exception -> Lc0
            goto Lc0
        La9:
            r0 = move-exception
            r2 = r1
            goto Lc1
        Lac:
            r2 = r1
            goto Lb3
        Lae:
            r0 = move-exception
            r2 = r1
            goto Lba
        Lb1:
            r0 = move-exception
            goto Lc1
        Lb3:
            if (r2 == 0) goto Lc0
        Lb5:
            r2.disconnect()     // Catch: java.lang.Exception -> Lc0
            goto Lc0
        Lb9:
            r0 = move-exception
        Lba:
            r0.printStackTrace()     // Catch: java.lang.Throwable -> Lb1
            if (r2 == 0) goto Lc0
            goto Lb5
        Lc0:
            return
        Lc1:
            if (r2 == 0) goto Lc6
            r2.disconnect()     // Catch: java.lang.Exception -> Lc6
        Lc6:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.autonavi.ae.gmap.maploader.MessageDataThread.run():void");
    }
}
