package com.qiniu.android.storage;

import android.util.Log;
import cn.jpush.android.api.JThirdPlatFormInterface;
import com.qiniu.android.http.Client;
import com.qiniu.android.http.CompletionHandler;
import com.qiniu.android.http.PostArgs;
import com.qiniu.android.http.ProgressHandler;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.utils.AndroidNetwork;
import com.qiniu.android.utils.Crc32;
import com.qiniu.android.utils.StringMap;
import java.io.File;
import java.io.IOException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
final class FormUploader {
    FormUploader() {
    }

    static void upload(Client client, Configuration configuration, byte[] bArr, String str, UpToken upToken, UpCompletionHandler upCompletionHandler, UploadOptions uploadOptions) {
        post(bArr, null, str, upToken, upCompletionHandler, uploadOptions, client, configuration);
    }

    static void upload(Client client, Configuration configuration, File file, String str, UpToken upToken, UpCompletionHandler upCompletionHandler, UploadOptions uploadOptions) {
        post(null, file, str, upToken, upCompletionHandler, uploadOptions, client, configuration);
    }

    private static void post(byte[] bArr, File file, final String str, UpToken upToken, UpCompletionHandler upCompletionHandler, UploadOptions uploadOptions, Client client, Configuration configuration) {
        long jFile;
        StringMap stringMap = new StringMap();
        PostArgs postArgs = new PostArgs();
        if (str != null) {
            stringMap.put("key", str);
            postArgs.fileName = str;
        } else {
            postArgs.fileName = "?";
        }
        if (file != null) {
            postArgs.fileName = file.getName();
        }
        stringMap.put(JThirdPlatFormInterface.KEY_TOKEN, upToken.token);
        final UploadOptions uploadOptionsDefaultOptions = uploadOptions != null ? uploadOptions : UploadOptions.defaultOptions();
        stringMap.putFileds(uploadOptionsDefaultOptions.params);
        if (file != null) {
            try {
                jFile = Crc32.file(file);
            } catch (IOException e) {
                e.printStackTrace();
                jFile = 0;
            }
        } else {
            jFile = Crc32.bytes(bArr);
        }
        stringMap.put("crc32", "" + jFile);
        ProgressHandler progressHandler = new ProgressHandler() { // from class: com.qiniu.android.storage.FormUploader.1
            @Override // com.qiniu.android.http.ProgressHandler
            public void onProgress(long j, long j2) {
                double d = j / j2;
                if (d > 0.95d) {
                    d = 0.95d;
                }
                uploadOptionsDefaultOptions.progressHandler.progress(str, d);
            }
        };
        postArgs.data = bArr;
        postArgs.file = file;
        postArgs.mimeType = uploadOptionsDefaultOptions.mimeType;
        postArgs.params = stringMap;
        String strUpHost = configuration.zone.upHost(upToken.token, configuration.useHttps, (String) null);
        Log.d("Qiniu.FormUploader", "upload use up host " + strUpHost);
        client.asyncMultipartPost(strUpHost, postArgs, upToken, progressHandler, new AnonymousClass2(uploadOptionsDefaultOptions, upCompletionHandler, str, configuration, upToken, strUpHost, client, postArgs, progressHandler), uploadOptionsDefaultOptions.cancellationSignal);
    }

    /* JADX INFO: renamed from: com.qiniu.android.storage.FormUploader$2, reason: invalid class name */
    static class AnonymousClass2 implements CompletionHandler {
        final /* synthetic */ PostArgs val$args;
        final /* synthetic */ Client val$client;
        final /* synthetic */ UpCompletionHandler val$completionHandler;
        final /* synthetic */ Configuration val$config;
        final /* synthetic */ String val$key;
        final /* synthetic */ UploadOptions val$options;
        final /* synthetic */ ProgressHandler val$progress;
        final /* synthetic */ UpToken val$token;
        final /* synthetic */ String val$upHost;

        AnonymousClass2(UploadOptions uploadOptions, UpCompletionHandler upCompletionHandler, String str, Configuration configuration, UpToken upToken, String str2, Client client, PostArgs postArgs, ProgressHandler progressHandler) {
            this.val$options = uploadOptions;
            this.val$completionHandler = upCompletionHandler;
            this.val$key = str;
            this.val$config = configuration;
            this.val$token = upToken;
            this.val$upHost = str2;
            this.val$client = client;
            this.val$args = postArgs;
            this.val$progress = progressHandler;
        }

        @Override // com.qiniu.android.http.CompletionHandler
        public void complete(ResponseInfo responseInfo, JSONObject jSONObject) {
            if (responseInfo.isNetworkBroken() && !AndroidNetwork.isNetWorkReady()) {
                this.val$options.netReadyHandler.waitReady();
                if (!AndroidNetwork.isNetWorkReady()) {
                    this.val$completionHandler.complete(this.val$key, responseInfo, jSONObject);
                    return;
                }
            }
            if (responseInfo.isOK()) {
                this.val$options.progressHandler.progress(this.val$key, 1.0d);
                this.val$completionHandler.complete(this.val$key, responseInfo, jSONObject);
            } else {
                if (responseInfo.needRetry()) {
                    final String strUpHost = this.val$config.zone.upHost(this.val$token.token, this.val$config.useHttps, this.val$upHost);
                    Log.d("Qiniu.FormUploader", "retry upload first time use up host " + strUpHost);
                    this.val$client.asyncMultipartPost(strUpHost, this.val$args, this.val$token, this.val$progress, new CompletionHandler() { // from class: com.qiniu.android.storage.FormUploader.2.1
                        @Override // com.qiniu.android.http.CompletionHandler
                        public void complete(ResponseInfo responseInfo2, JSONObject jSONObject2) {
                            if (responseInfo2.isOK()) {
                                AnonymousClass2.this.val$options.progressHandler.progress(AnonymousClass2.this.val$key, 1.0d);
                                AnonymousClass2.this.val$completionHandler.complete(AnonymousClass2.this.val$key, responseInfo2, jSONObject2);
                            } else {
                                if (responseInfo2.needRetry()) {
                                    final String strUpHost2 = AnonymousClass2.this.val$config.zone.upHost(AnonymousClass2.this.val$token.token, AnonymousClass2.this.val$config.useHttps, strUpHost);
                                    Log.d("Qiniu.FormUploader", "retry upload second time use up host " + strUpHost2);
                                    AnonymousClass2.this.val$client.asyncMultipartPost(strUpHost2, AnonymousClass2.this.val$args, AnonymousClass2.this.val$token, AnonymousClass2.this.val$progress, new CompletionHandler() { // from class: com.qiniu.android.storage.FormUploader.2.1.1
                                        @Override // com.qiniu.android.http.CompletionHandler
                                        public void complete(ResponseInfo responseInfo3, JSONObject jSONObject3) {
                                            if (responseInfo3.isOK()) {
                                                AnonymousClass2.this.val$options.progressHandler.progress(AnonymousClass2.this.val$key, 1.0d);
                                            } else if (responseInfo3.needRetry()) {
                                                AnonymousClass2.this.val$config.zone.frozenDomain(strUpHost2);
                                            }
                                            AnonymousClass2.this.val$completionHandler.complete(AnonymousClass2.this.val$key, responseInfo3, jSONObject3);
                                        }
                                    }, AnonymousClass2.this.val$options.cancellationSignal);
                                    return;
                                }
                                AnonymousClass2.this.val$completionHandler.complete(AnonymousClass2.this.val$key, responseInfo2, jSONObject2);
                            }
                        }
                    }, this.val$options.cancellationSignal);
                    return;
                }
                this.val$completionHandler.complete(this.val$key, responseInfo, jSONObject);
            }
        }
    }

    public static ResponseInfo syncUpload(Client client, Configuration configuration, byte[] bArr, String str, UpToken upToken, UploadOptions uploadOptions) {
        try {
            return syncUpload0(client, configuration, bArr, null, str, upToken, uploadOptions);
        } catch (Exception e) {
            return ResponseInfo.create(null, 0, "", "", "", "", "", "", 0, 0L, 0L, e.getMessage(), upToken, bArr != null ? bArr.length : 0L);
        }
    }

    public static ResponseInfo syncUpload(Client client, Configuration configuration, File file, String str, UpToken upToken, UploadOptions uploadOptions) {
        try {
            return syncUpload0(client, configuration, null, file, str, upToken, uploadOptions);
        } catch (Exception e) {
            return ResponseInfo.create(null, 0, "", "", "", "", "", "", 0, 0L, 0L, e.getMessage(), upToken, file != null ? file.length() : 0L);
        }
    }

    private static ResponseInfo syncUpload0(Client client, Configuration configuration, byte[] bArr, File file, String str, UpToken upToken, UploadOptions uploadOptions) {
        long jFile;
        StringMap stringMap = new StringMap();
        PostArgs postArgs = new PostArgs();
        if (str != null) {
            stringMap.put("key", str);
            postArgs.fileName = str;
        } else {
            postArgs.fileName = "?";
        }
        if (file != null) {
            postArgs.fileName = file.getName();
        }
        stringMap.put(JThirdPlatFormInterface.KEY_TOKEN, upToken.token);
        if (uploadOptions == null) {
            uploadOptions = UploadOptions.defaultOptions();
        }
        stringMap.putFileds(uploadOptions.params);
        if (file != null) {
            try {
                jFile = Crc32.file(file);
            } catch (IOException e) {
                e.printStackTrace();
                jFile = 0;
            }
        } else {
            jFile = Crc32.bytes(bArr);
        }
        stringMap.put("crc32", "" + jFile);
        postArgs.data = bArr;
        postArgs.file = file;
        postArgs.mimeType = uploadOptions.mimeType;
        postArgs.params = stringMap;
        if (!configuration.zone.preQuery(upToken.token)) {
            return ResponseInfo.invalidToken("failed to get up host");
        }
        String strUpHost = configuration.zone.upHost(upToken.token, configuration.useHttps, (String) null);
        Log.d("Qiniu.FormUploader", "sync upload use up host " + strUpHost);
        ResponseInfo responseInfoSyncMultipartPost = client.syncMultipartPost(strUpHost, postArgs, upToken);
        if (responseInfoSyncMultipartPost.isOK()) {
            return responseInfoSyncMultipartPost;
        }
        if (responseInfoSyncMultipartPost.needRetry()) {
            if (responseInfoSyncMultipartPost.isNetworkBroken() && !AndroidNetwork.isNetWorkReady()) {
                uploadOptions.netReadyHandler.waitReady();
                if (!AndroidNetwork.isNetWorkReady()) {
                    return responseInfoSyncMultipartPost;
                }
            }
            String strUpHost2 = configuration.zone.upHost(upToken.token, configuration.useHttps, strUpHost);
            Log.d("Qiniu.FormUploader", "sync upload retry first time use up host " + strUpHost2);
            responseInfoSyncMultipartPost = client.syncMultipartPost(strUpHost2, postArgs, upToken);
            if (responseInfoSyncMultipartPost.needRetry()) {
                if (responseInfoSyncMultipartPost.isNetworkBroken() && !AndroidNetwork.isNetWorkReady()) {
                    uploadOptions.netReadyHandler.waitReady();
                    if (!AndroidNetwork.isNetWorkReady()) {
                        return responseInfoSyncMultipartPost;
                    }
                }
                String strUpHost3 = configuration.zone.upHost(upToken.token, configuration.useHttps, strUpHost2);
                Log.d("Qiniu.FormUploader", "sync upload retry second time use up host " + strUpHost3);
                responseInfoSyncMultipartPost = client.syncMultipartPost(strUpHost3, postArgs, upToken);
                if (responseInfoSyncMultipartPost.needRetry()) {
                    configuration.zone.frozenDomain(strUpHost3);
                }
            }
        }
        return responseInfoSyncMultipartPost;
    }
}
