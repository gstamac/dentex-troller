package com.qiniu.android.storage;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import com.qiniu.android.http.Client;
import com.qiniu.android.http.CompletionHandler;
import com.qiniu.android.http.ProgressHandler;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.utils.AndroidNetwork;
import com.qiniu.android.utils.Crc32;
import com.qiniu.android.utils.StringMap;
import com.qiniu.android.utils.StringUtils;
import com.qiniu.android.utils.UrlSafeBase64;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
final class ResumeUploader implements Runnable {
    private final byte[] chunkBuffer;
    private final Client client;
    private final UpCompletionHandler completionHandler;
    private final Configuration config;
    private final String[] contexts;
    private long crc32;
    private File f;
    private RandomAccessFile file;
    private final StringMap headers;
    private final String key;
    private final long modifyTime;
    private final UploadOptions options;
    private final String recorderKey;
    private UpToken token;
    private final long totalSize;

    ResumeUploader(Client client, Configuration configuration, File file, String str, UpToken upToken, final UpCompletionHandler upCompletionHandler, UploadOptions uploadOptions, String str2) {
        this.client = client;
        this.config = configuration;
        this.f = file;
        this.recorderKey = str2;
        long length = file.length();
        this.totalSize = length;
        this.key = str;
        this.headers = new StringMap().put("Authorization", "UpToken " + upToken.token);
        this.file = null;
        this.completionHandler = new UpCompletionHandler() { // from class: com.qiniu.android.storage.ResumeUploader.1
            @Override // com.qiniu.android.storage.UpCompletionHandler
            public void complete(String str3, ResponseInfo responseInfo, JSONObject jSONObject) {
                if (ResumeUploader.this.file != null) {
                    try {
                        ResumeUploader.this.file.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                upCompletionHandler.complete(str3, responseInfo, jSONObject);
            }
        };
        this.options = uploadOptions == null ? UploadOptions.defaultOptions() : uploadOptions;
        this.chunkBuffer = new byte[configuration.chunkSize];
        this.contexts = new String[(int) (((length + 4194304) - 1) / 4194304)];
        this.modifyTime = file.lastModified();
        this.token = upToken;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isChunkOK(ResponseInfo responseInfo, JSONObject jSONObject) {
        return responseInfo.statusCode == 200 && responseInfo.error == null && (responseInfo.hasReqId() || isChunkResOK(jSONObject));
    }

    private static boolean isChunkResOK(JSONObject jSONObject) {
        try {
            jSONObject.getString("ctx");
            jSONObject.getLong("crc32");
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isNotChunkToQiniu(ResponseInfo responseInfo, JSONObject jSONObject) {
        return responseInfo.statusCode < 500 && responseInfo.statusCode >= 200 && !responseInfo.hasReqId() && !isChunkResOK(jSONObject);
    }

    @Override // java.lang.Runnable
    public void run() {
        long jRecoveryFromRecord = recoveryFromRecord();
        try {
            this.file = new RandomAccessFile(this.f, "r");
            nextTask(jRecoveryFromRecord, 0, this.config.zone.upHost(this.token.token, this.config.useHttps, (String) null));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            this.completionHandler.complete(this.key, ResponseInfo.fileError(e, this.token), null);
        }
    }

    private void makeBlock(String str, long j, int i, int i2, ProgressHandler progressHandler, CompletionHandler completionHandler, UpCancellationSignal upCancellationSignal) {
        String str2 = String.format(Locale.ENGLISH, "/mkblk/%d", Integer.valueOf(i));
        try {
            this.file.seek(j);
            this.file.read(this.chunkBuffer, 0, i2);
            this.crc32 = Crc32.bytes(this.chunkBuffer, 0, i2);
            post(String.format("%s%s", str, str2), this.chunkBuffer, 0, i2, progressHandler, completionHandler, upCancellationSignal);
        } catch (IOException e) {
            this.completionHandler.complete(this.key, ResponseInfo.fileError(e, this.token), null);
        }
    }

    private void putChunk(String str, long j, int i, String str2, ProgressHandler progressHandler, CompletionHandler completionHandler, UpCancellationSignal upCancellationSignal) {
        String str3 = String.format(Locale.ENGLISH, "/bput/%s/%d", str2, Integer.valueOf((int) (j % 4194304)));
        try {
            this.file.seek(j);
            this.file.read(this.chunkBuffer, 0, i);
            this.crc32 = Crc32.bytes(this.chunkBuffer, 0, i);
            post(String.format("%s%s", str, str3), this.chunkBuffer, 0, i, progressHandler, completionHandler, upCancellationSignal);
        } catch (IOException e) {
            this.completionHandler.complete(this.key, ResponseInfo.fileError(e, this.token), null);
        }
    }

    private void makeFile(String str, CompletionHandler completionHandler, UpCancellationSignal upCancellationSignal) {
        String str2 = String.format(Locale.ENGLISH, "/mimeType/%s/fname/%s", UrlSafeBase64.encodeToString(this.options.mimeType), UrlSafeBase64.encodeToString(this.f.getName()));
        String str3 = this.key;
        String str4 = "";
        String str5 = str3 != null ? String.format("/key/%s", UrlSafeBase64.encodeToString(str3)) : "";
        if (this.options.params.size() != 0) {
            String[] strArr = new String[this.options.params.size()];
            int i = 0;
            for (Map.Entry<String, String> entry : this.options.params.entrySet()) {
                strArr[i] = String.format(Locale.ENGLISH, "%s/%s", entry.getKey(), UrlSafeBase64.encodeToString(entry.getValue()));
                i++;
            }
            str4 = "/" + StringUtils.join(strArr, "/");
        }
        String str6 = String.format(Locale.ENGLISH, "/mkfile/%d%s%s%s", Long.valueOf(this.totalSize), str2, str5, str4);
        byte[] bytes = StringUtils.join(this.contexts, ",").getBytes();
        post(String.format("%s%s", str, str6), bytes, 0, bytes.length, null, completionHandler, upCancellationSignal);
    }

    private void post(String str, byte[] bArr, int i, int i2, ProgressHandler progressHandler, CompletionHandler completionHandler, UpCancellationSignal upCancellationSignal) {
        this.client.asyncPost(str, bArr, i, i2, this.headers, this.token, this.totalSize, progressHandler, completionHandler, upCancellationSignal);
    }

    private long calcPutSize(long j) {
        long j2 = this.totalSize - j;
        return j2 < ((long) this.config.chunkSize) ? j2 : this.config.chunkSize;
    }

    private long calcBlockSize(long j) {
        long j2 = this.totalSize - j;
        if (j2 < 4194304) {
            return j2;
        }
        return 4194304L;
    }

    private boolean isCancelled() {
        return this.options.cancellationSignal.isCancelled();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void nextTask(final long j, final int i, final String str) {
        if (isCancelled()) {
            this.completionHandler.complete(this.key, ResponseInfo.cancelled(this.token), null);
        } else {
            if (j == this.totalSize) {
                makeFile(str, new CompletionHandler() { // from class: com.qiniu.android.storage.ResumeUploader.2
                    @Override // com.qiniu.android.http.CompletionHandler
                    public void complete(ResponseInfo responseInfo, JSONObject jSONObject) {
                        String strUpHost;
                        if (responseInfo.isNetworkBroken() && !AndroidNetwork.isNetWorkReady()) {
                            ResumeUploader.this.options.netReadyHandler.waitReady();
                            if (!AndroidNetwork.isNetWorkReady()) {
                                ResumeUploader.this.completionHandler.complete(ResumeUploader.this.key, responseInfo, jSONObject);
                                return;
                            }
                        }
                        if (responseInfo.isOK()) {
                            ResumeUploader.this.removeRecord();
                            ResumeUploader.this.options.progressHandler.progress(ResumeUploader.this.key, 1.0d);
                            ResumeUploader.this.completionHandler.complete(ResumeUploader.this.key, responseInfo, jSONObject);
                        } else if (!responseInfo.needRetry() || i >= ResumeUploader.this.config.retryMax + 1 || (strUpHost = ResumeUploader.this.config.zone.upHost(ResumeUploader.this.token.token, ResumeUploader.this.config.useHttps, str)) == null) {
                            ResumeUploader.this.completionHandler.complete(ResumeUploader.this.key, responseInfo, jSONObject);
                        } else {
                            ResumeUploader.this.nextTask(j, i + 1, strUpHost);
                        }
                    }
                }, this.options.cancellationSignal);
                return;
            }
            final int iCalcPutSize = (int) calcPutSize(j);
            ProgressHandler progressHandler = new ProgressHandler() { // from class: com.qiniu.android.storage.ResumeUploader.3
                @Override // com.qiniu.android.http.ProgressHandler
                public void onProgress(long j2, long j3) {
                    double d = (j + j2) / j3;
                    if (d > 0.95d) {
                        d = 0.95d;
                    }
                    ResumeUploader.this.options.progressHandler.progress(ResumeUploader.this.key, d);
                }
            };
            CompletionHandler completionHandler = new CompletionHandler() { // from class: com.qiniu.android.storage.ResumeUploader.4
                @Override // com.qiniu.android.http.CompletionHandler
                public void complete(ResponseInfo responseInfo, JSONObject jSONObject) {
                    long j2;
                    if (responseInfo.isNetworkBroken() && !AndroidNetwork.isNetWorkReady()) {
                        ResumeUploader.this.options.netReadyHandler.waitReady();
                        if (!AndroidNetwork.isNetWorkReady()) {
                            ResumeUploader.this.completionHandler.complete(ResumeUploader.this.key, responseInfo, jSONObject);
                            return;
                        }
                    }
                    if (responseInfo.isCancelled()) {
                        ResumeUploader.this.completionHandler.complete(ResumeUploader.this.key, responseInfo, jSONObject);
                        return;
                    }
                    if (!ResumeUploader.isChunkOK(responseInfo, jSONObject)) {
                        String strUpHost = ResumeUploader.this.config.zone.upHost(ResumeUploader.this.token.token, ResumeUploader.this.config.useHttps, str);
                        if (responseInfo.statusCode != 701 || i >= ResumeUploader.this.config.retryMax) {
                            if (strUpHost == null || ((!ResumeUploader.isNotChunkToQiniu(responseInfo, jSONObject) && !responseInfo.needRetry()) || i >= ResumeUploader.this.config.retryMax)) {
                                ResumeUploader.this.completionHandler.complete(ResumeUploader.this.key, responseInfo, jSONObject);
                                return;
                            } else {
                                ResumeUploader.this.nextTask(j, i + 1, strUpHost);
                                return;
                            }
                        }
                        ResumeUploader.this.nextTask((j / 4194304) * 4194304, i + 1, str);
                        return;
                    }
                    if (jSONObject == null && i < ResumeUploader.this.config.retryMax) {
                        ResumeUploader.this.nextTask(j, i + 1, ResumeUploader.this.config.zone.upHost(ResumeUploader.this.token.token, ResumeUploader.this.config.useHttps, str));
                        return;
                    }
                    String string = null;
                    try {
                        string = jSONObject.getString("ctx");
                        j2 = jSONObject.getLong("crc32");
                    } catch (Exception e) {
                        e.printStackTrace();
                        j2 = 0;
                    }
                    if ((string == null || j2 != ResumeUploader.this.crc32) && i < ResumeUploader.this.config.retryMax) {
                        ResumeUploader.this.nextTask(j, i + 1, ResumeUploader.this.config.zone.upHost(ResumeUploader.this.token.token, ResumeUploader.this.config.useHttps, str));
                        return;
                    }
                    String[] strArr = ResumeUploader.this.contexts;
                    long j3 = j;
                    strArr[(int) (j3 / 4194304)] = string;
                    ResumeUploader.this.record(j3 + ((long) iCalcPutSize));
                    ResumeUploader.this.nextTask(j + ((long) iCalcPutSize), i, str);
                }
            };
            if (j % 4194304 == 0) {
                makeBlock(str, j, (int) calcBlockSize(j), iCalcPutSize, progressHandler, completionHandler, this.options.cancellationSignal);
            } else {
                putChunk(str, j, iCalcPutSize, this.contexts[(int) (j / 4194304)], progressHandler, completionHandler, this.options.cancellationSignal);
            }
        }
    }

    private long recoveryFromRecord() {
        byte[] bArr;
        if (this.config.recorder == null || (bArr = this.config.recorder.get(this.recorderKey)) == null) {
            return 0L;
        }
        try {
            JSONObject jSONObject = new JSONObject(new String(bArr));
            long jOptLong = jSONObject.optLong(TypedValues.CycleType.S_WAVE_OFFSET, 0L);
            long jOptLong2 = jSONObject.optLong("modify_time", 0L);
            long jOptLong3 = jSONObject.optLong("size", 0L);
            JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("contexts");
            if (jOptLong == 0 || jOptLong2 != this.modifyTime || jOptLong3 != this.totalSize || jSONArrayOptJSONArray == null || jSONArrayOptJSONArray.length() == 0) {
                return 0L;
            }
            for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                this.contexts[i] = jSONArrayOptJSONArray.optString(i);
            }
            return jOptLong;
        } catch (JSONException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeRecord() {
        if (this.config.recorder != null) {
            this.config.recorder.del(this.recorderKey);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void record(long j) {
        if (this.config.recorder == null || j == 0) {
            return;
        }
        this.config.recorder.set(this.recorderKey, String.format(Locale.ENGLISH, "{\"size\":%d,\"offset\":%d, \"modify_time\":%d, \"contexts\":[%s]}", Long.valueOf(this.totalSize), Long.valueOf(j), Long.valueOf(this.modifyTime), StringUtils.jsonJoin(this.contexts)).getBytes());
    }

    private URI newURI(URI uri, String str) {
        try {
            return new URI(uri.getScheme(), null, uri.getHost(), uri.getPort(), str, null, null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return uri;
        }
    }
}
