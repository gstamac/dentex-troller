package com.qiniu.android.storage;

import com.qiniu.android.collect.Config;
import com.qiniu.android.collect.UploadInfoCollector;
import com.qiniu.android.common.Zone;
import com.qiniu.android.http.Client;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.utils.AsyncRun;
import com.qiniu.android.utils.StringUtils;
import java.io.File;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public final class UploadManager {
    private final Client client;
    private final Configuration config;

    public UploadManager() {
        this(new Configuration.Builder().build());
    }

    public UploadManager(Configuration configuration) {
        this.config = configuration;
        this.client = new Client(configuration.proxy, configuration.connectTimeout, configuration.responseTimeout, configuration.urlConverter, configuration.dns);
    }

    public UploadManager(Recorder recorder, KeyGenerator keyGenerator) {
        this(new Configuration.Builder().recorder(recorder, keyGenerator).build());
    }

    public UploadManager(Recorder recorder) {
        this(recorder, null);
    }

    private static boolean areInvalidArg(String str, byte[] bArr, File file, String str2, UpToken upToken, UpCompletionHandler upCompletionHandler) {
        String str3;
        ResponseInfo responseInfoInvalidToken;
        if (upCompletionHandler == null) {
            throw new IllegalArgumentException("no UpCompletionHandler");
        }
        if (file == null && bArr == null) {
            str3 = "no input data";
        } else {
            str3 = (str2 == null || str2.equals("")) ? "no token" : null;
        }
        if (str3 != null) {
            responseInfoInvalidToken = ResponseInfo.invalidArgument(str3, upToken);
        } else if (upToken == UpToken.NULL || upToken == null) {
            responseInfoInvalidToken = ResponseInfo.invalidToken("invalid token");
        } else {
            responseInfoInvalidToken = ((file == null || file.length() != 0) && (bArr == null || bArr.length != 0)) ? null : ResponseInfo.zeroSize(upToken);
        }
        if (responseInfoInvalidToken == null) {
            return false;
        }
        upCompletionHandler.complete(str, responseInfoInvalidToken, null);
        return true;
    }

    private static ResponseInfo areInvalidArg(String str, byte[] bArr, File file, String str2, UpToken upToken) {
        String str3;
        if (file == null && bArr == null) {
            str3 = "no input data";
        } else {
            str3 = (str2 == null || str2.equals("")) ? "no token" : null;
        }
        if (str3 != null) {
            return ResponseInfo.invalidArgument(str3, upToken);
        }
        if (upToken == UpToken.NULL || upToken == null) {
            return ResponseInfo.invalidToken("invalid token");
        }
        if ((file == null || file.length() != 0) && (bArr == null || bArr.length != 0)) {
            return null;
        }
        return ResponseInfo.zeroSize(upToken);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static WarpHandler warpHandler(UpCompletionHandler upCompletionHandler, long j) {
        return new WarpHandler(upCompletionHandler, j);
    }

    public void put(final byte[] bArr, final String str, String str2, final UpCompletionHandler upCompletionHandler, final UploadOptions uploadOptions) {
        final UpToken upToken = UpToken.parse(str2);
        if (areInvalidArg(str, bArr, null, str2, upToken, upCompletionHandler)) {
            return;
        }
        this.config.zone.preQuery(str2, new Zone.QueryHandler() { // from class: com.qiniu.android.storage.UploadManager.1
            @Override // com.qiniu.android.common.Zone.QueryHandler
            public void onSuccess() {
                FormUploader.upload(UploadManager.this.client, UploadManager.this.config, bArr, str, upToken, upCompletionHandler, uploadOptions);
            }

            @Override // com.qiniu.android.common.Zone.QueryHandler
            public void onFailure(int i) {
                ResponseInfo responseInfoInvalidToken;
                if (ResponseInfo.isStatusCodeForBrokenNetwork(i)) {
                    responseInfoInvalidToken = ResponseInfo.networkError(i, upToken);
                } else {
                    responseInfoInvalidToken = ResponseInfo.invalidToken("invalid token");
                }
                upCompletionHandler.complete(str, responseInfoInvalidToken, null);
            }
        });
    }

    public void put(String str, String str2, String str3, UpCompletionHandler upCompletionHandler, UploadOptions uploadOptions) {
        put(new File(str), str2, str3, upCompletionHandler, uploadOptions);
    }

    public void put(final File file, final String str, String str2, final UpCompletionHandler upCompletionHandler, final UploadOptions uploadOptions) {
        final UpToken upToken = UpToken.parse(str2);
        if (areInvalidArg(str, null, file, str2, upToken, upCompletionHandler)) {
            return;
        }
        this.config.zone.preQuery(str2, new Zone.QueryHandler() { // from class: com.qiniu.android.storage.UploadManager.2
            @Override // com.qiniu.android.common.Zone.QueryHandler
            public void onSuccess() {
                if (file.length() <= UploadManager.this.config.putThreshold) {
                    FormUploader.upload(UploadManager.this.client, UploadManager.this.config, file, str, upToken, upCompletionHandler, uploadOptions);
                    return;
                }
                String strGen = UploadManager.this.config.keyGen.gen(str, file);
                UpCompletionHandler upCompletionHandler2 = upCompletionHandler;
                File file2 = file;
                AsyncRun.runInMain(new ResumeUploader(UploadManager.this.client, UploadManager.this.config, file, str, upToken, UploadManager.warpHandler(upCompletionHandler2, file2 != null ? file2.length() : 0L), uploadOptions, strGen));
            }

            @Override // com.qiniu.android.common.Zone.QueryHandler
            public void onFailure(int i) {
                ResponseInfo responseInfoInvalidToken;
                if (ResponseInfo.isStatusCodeForBrokenNetwork(i)) {
                    responseInfoInvalidToken = ResponseInfo.networkError(i, upToken);
                } else {
                    responseInfoInvalidToken = ResponseInfo.invalidToken("invalid token");
                }
                upCompletionHandler.complete(str, responseInfoInvalidToken, null);
            }
        });
    }

    public ResponseInfo syncPut(byte[] bArr, String str, String str2, UploadOptions uploadOptions) {
        UpToken upToken = UpToken.parse(str2);
        ResponseInfo responseInfoAreInvalidArg = areInvalidArg(str, bArr, null, str2, upToken);
        return responseInfoAreInvalidArg != null ? responseInfoAreInvalidArg : FormUploader.syncUpload(this.client, this.config, bArr, str, upToken, uploadOptions);
    }

    public ResponseInfo syncPut(File file, String str, String str2, UploadOptions uploadOptions) {
        UpToken upToken = UpToken.parse(str2);
        ResponseInfo responseInfoAreInvalidArg = areInvalidArg(str, null, file, str2, upToken);
        return responseInfoAreInvalidArg != null ? responseInfoAreInvalidArg : FormUploader.syncUpload(this.client, this.config, file, str, upToken, uploadOptions);
    }

    public ResponseInfo syncPut(String str, String str2, String str3, UploadOptions uploadOptions) {
        return syncPut(new File(str), str2, str3, uploadOptions);
    }

    private static class WarpHandler implements UpCompletionHandler {
        final long before = System.currentTimeMillis();
        final UpCompletionHandler complete;
        final long size;

        WarpHandler(UpCompletionHandler upCompletionHandler, long j) {
            this.complete = upCompletionHandler;
            this.size = j;
        }

        @Override // com.qiniu.android.storage.UpCompletionHandler
        public void complete(final String str, final ResponseInfo responseInfo, final JSONObject jSONObject) {
            if (Config.isRecord) {
                final long jCurrentTimeMillis = System.currentTimeMillis();
                UploadInfoCollector.handleUpload(responseInfo.upToken, new UploadInfoCollector.RecordMsg() { // from class: com.qiniu.android.storage.UploadManager.WarpHandler.1
                    @Override // com.qiniu.android.collect.UploadInfoCollector.RecordMsg
                    public String toRecordMsg() {
                        return StringUtils.join(new String[]{responseInfo.statusCode + "", responseInfo.reqId, responseInfo.host, responseInfo.ip, responseInfo.port + "", (jCurrentTimeMillis - WarpHandler.this.before) + "", responseInfo.timeStamp + "", WarpHandler.this.size + "", "block", WarpHandler.this.size + ""}, ",");
                    }
                });
            }
            AsyncRun.runInMain(new Runnable() { // from class: com.qiniu.android.storage.UploadManager.WarpHandler.2
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        WarpHandler.this.complete.complete(str, responseInfo, jSONObject);
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            });
        }
    }
}
