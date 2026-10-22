package com.qiniu.android.http;

import com.qiniu.android.common.Constants;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpToken;
import com.qiniu.android.utils.AsyncRun;
import com.qiniu.android.utils.StringMap;
import com.qiniu.android.utils.StringUtils;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public final class Client {
    public static final String ContentTypeHeader = "Content-Type";
    public static final String DefaultMime = "application/octet-stream";
    public static final String FormMime = "application/x-www-form-urlencoded";
    public static final String JsonMime = "application/json";
    private final UrlConverter converter;
    private OkHttpClient httpClient;

    public Client() {
        this(null, 10, 30, null, null);
    }

    public Client(ProxyConfiguration proxyConfiguration, int i, int i2, UrlConverter urlConverter, final Dns dns) {
        this.converter = urlConverter;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (proxyConfiguration != null) {
            builder.proxy(proxyConfiguration.proxy());
            if (proxyConfiguration.user != null && proxyConfiguration.password != null) {
                builder.proxyAuthenticator(proxyConfiguration.authenticator());
            }
        }
        if (dns != null) {
            builder.dns(new okhttp3.Dns() { // from class: com.qiniu.android.http.Client.1
                @Override // okhttp3.Dns
                public List<InetAddress> lookup(String str) throws UnknownHostException {
                    try {
                        return dns.lookup(str);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return okhttp3.Dns.SYSTEM.lookup(str);
                    }
                }
            });
        }
        builder.networkInterceptors().add(new Interceptor() { // from class: com.qiniu.android.http.Client.2
            @Override // okhttp3.Interceptor
            public Response intercept(Interceptor.Chain chain) throws IOException {
                String string;
                Request request = chain.request();
                long jCurrentTimeMillis = System.currentTimeMillis();
                Response responseProceed = chain.proceed(request);
                long jCurrentTimeMillis2 = System.currentTimeMillis();
                ResponseTag responseTag = (ResponseTag) request.tag();
                try {
                    string = chain.connection().socket().getRemoteSocketAddress().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                    string = "";
                }
                responseTag.ip = string;
                responseTag.duration = jCurrentTimeMillis2 - jCurrentTimeMillis;
                return responseProceed;
            }
        });
        builder.connectTimeout(i, TimeUnit.SECONDS);
        builder.readTimeout(i2, TimeUnit.SECONDS);
        builder.writeTimeout(0L, TimeUnit.SECONDS);
        this.httpClient = builder.build();
    }

    private static String via(Response response) {
        String strHeader = response.header("X-Via", "");
        if (!strHeader.equals("")) {
            return strHeader;
        }
        String strHeader2 = response.header("X-Px", "");
        if (!strHeader2.equals("")) {
            return strHeader2;
        }
        String strHeader3 = response.header("Fw-Via", "");
        strHeader3.equals("");
        return strHeader3;
    }

    private static String ctype(Response response) {
        MediaType mediaTypeContentType = response.body().get$contentType();
        return mediaTypeContentType == null ? "" : mediaTypeContentType.type() + "/" + mediaTypeContentType.subtype();
    }

    private static JSONObject buildJsonResp(byte[] bArr) throws Exception {
        String str = new String(bArr, Constants.UTF_8);
        if (StringUtils.isNullOrEmpty(str)) {
            return new JSONObject();
        }
        return new JSONObject(str);
    }

    private static ResponseInfo buildResponseInfo(Response response, String str, long j, UpToken upToken, long j2) {
        String message;
        byte[] bArrBytes;
        String str2;
        int iCode = response.code();
        String strHeader = response.header("X-Reqid");
        JSONObject jSONObjectBuildJsonResp = null;
        String str3 = strHeader == null ? null : strHeader.trim().split(",")[0];
        try {
            bArrBytes = response.body().bytes();
            message = null;
        } catch (IOException e) {
            message = e.getMessage();
            bArrBytes = null;
        }
        if (ctype(response).equals("application/json") && bArrBytes != null) {
            try {
                jSONObjectBuildJsonResp = buildJsonResp(bArrBytes);
                if (response.code() != 200) {
                    message = jSONObjectBuildJsonResp.optString("error", new String(bArrBytes, Constants.UTF_8));
                }
            } catch (Exception e2) {
                if (response.code() < 300) {
                    message = e2.getMessage();
                }
            }
            str2 = message;
        } else {
            str2 = bArrBytes == null ? "null body" : new String(bArrBytes);
        }
        HttpUrl httpUrlUrl = response.request().url();
        return ResponseInfo.create(jSONObjectBuildJsonResp, iCode, str3, response.header("X-Log"), via(response), httpUrlUrl.host(), httpUrlUrl.encodedPath(), str, httpUrlUrl.port(), j, getContentLength(response), str2, upToken, j2);
    }

    private static long getContentLength(Response response) {
        try {
            RequestBody requestBodyBody = response.request().body();
            if (requestBodyBody == null) {
                return 0L;
            }
            return requestBodyBody.contentLength();
        } catch (Throwable unused) {
            return -1L;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void onRet(Response response, String str, long j, UpToken upToken, long j2, final CompletionHandler completionHandler) {
        final ResponseInfo responseInfoBuildResponseInfo = buildResponseInfo(response, str, j, upToken, j2);
        AsyncRun.runInMain(new Runnable() { // from class: com.qiniu.android.http.Client.3
            @Override // java.lang.Runnable
            public void run() {
                CompletionHandler completionHandler2 = completionHandler;
                ResponseInfo responseInfo = responseInfoBuildResponseInfo;
                completionHandler2.complete(responseInfo, responseInfo.response);
            }
        });
    }

    public void asyncSend(final Request.Builder builder, StringMap stringMap, final UpToken upToken, final long j, final CompletionHandler completionHandler) {
        if (stringMap != null) {
            stringMap.forEach(new StringMap.Consumer() { // from class: com.qiniu.android.http.Client.4
                @Override // com.qiniu.android.utils.StringMap.Consumer
                public void accept(String str, Object obj) {
                    builder.header(str, obj.toString());
                }
            });
        }
        if (upToken != null) {
            builder.header("User-Agent", UserAgent.instance().getUa(upToken.accessKey));
        } else {
            builder.header("User-Agent", UserAgent.instance().getUa("pandora"));
        }
        final ResponseTag responseTag = new ResponseTag();
        this.httpClient.newCall(builder.tag(responseTag).build()).enqueue(new Callback() { // from class: com.qiniu.android.http.Client.5
            @Override // okhttp3.Callback
            public void onFailure(Call call, IOException iOException) {
                int i;
                iOException.printStackTrace();
                String message = iOException.getMessage();
                if (iOException instanceof CancellationHandler.CancellationException) {
                    i = -2;
                } else if (iOException instanceof UnknownHostException) {
                    i = ResponseInfo.UnknownHost;
                } else if (message != null && message.indexOf("Broken pipe") == 0) {
                    i = ResponseInfo.NetworkConnectionLost;
                } else if (iOException instanceof SocketTimeoutException) {
                    i = ResponseInfo.TimedOut;
                } else {
                    i = iOException instanceof ConnectException ? ResponseInfo.CannotConnectToHost : -1;
                }
                int i2 = i;
                HttpUrl httpUrlUrl = call.request().url();
                completionHandler.complete(ResponseInfo.create(null, i2, "", "", "", httpUrlUrl.host(), httpUrlUrl.encodedPath(), "", httpUrlUrl.port(), responseTag.duration, -1L, iOException.getMessage(), upToken, j), null);
            }

            @Override // okhttp3.Callback
            public void onResponse(Call call, Response response) throws IOException {
                ResponseTag responseTag2 = (ResponseTag) response.request().tag();
                Client.onRet(response, responseTag2.ip, responseTag2.duration, upToken, j, completionHandler);
            }
        });
    }

    public void asyncPost(String str, byte[] bArr, StringMap stringMap, UpToken upToken, long j, ProgressHandler progressHandler, CompletionHandler completionHandler, UpCancellationSignal upCancellationSignal) {
        asyncPost(str, bArr, 0, bArr.length, stringMap, upToken, j, progressHandler, completionHandler, upCancellationSignal);
    }

    public void asyncPost(String str, byte[] bArr, int i, int i2, StringMap stringMap, UpToken upToken, long j, ProgressHandler progressHandler, CompletionHandler completionHandler, CancellationHandler cancellationHandler) {
        RequestBody requestBodyCreate;
        Object obj;
        UrlConverter urlConverter = this.converter;
        String strConvert = urlConverter != null ? urlConverter.convert(str) : str;
        if (bArr != null && bArr.length > 0) {
            MediaType mediaType = MediaType.parse(DefaultMime);
            if (stringMap != null && (obj = stringMap.get(ContentTypeHeader)) != null) {
                mediaType = MediaType.parse(obj.toString());
            }
            requestBodyCreate = RequestBody.create(mediaType, bArr, i, i2);
        } else {
            requestBodyCreate = RequestBody.create((MediaType) null, new byte[0]);
        }
        RequestBody countingRequestBody = requestBodyCreate;
        if (progressHandler != null || cancellationHandler != null) {
            countingRequestBody = new CountingRequestBody(countingRequestBody, progressHandler, j, cancellationHandler);
        }
        asyncSend(new Request.Builder().url(strConvert).post(countingRequestBody), stringMap, upToken, j, completionHandler);
    }

    public void asyncMultipartPost(String str, PostArgs postArgs, UpToken upToken, ProgressHandler progressHandler, CompletionHandler completionHandler, CancellationHandler cancellationHandler) {
        RequestBody requestBodyCreate;
        long length;
        if (postArgs.file != null) {
            requestBodyCreate = RequestBody.create(MediaType.parse(postArgs.mimeType), postArgs.file);
            length = postArgs.file.length();
        } else {
            requestBodyCreate = RequestBody.create(MediaType.parse(postArgs.mimeType), postArgs.data);
            length = postArgs.data.length;
        }
        asyncMultipartPost(str, postArgs.params, upToken, length, progressHandler, postArgs.fileName, requestBodyCreate, completionHandler, cancellationHandler);
    }

    private void asyncMultipartPost(String str, StringMap stringMap, UpToken upToken, long j, ProgressHandler progressHandler, String str2, RequestBody requestBody, CompletionHandler completionHandler, CancellationHandler cancellationHandler) {
        UrlConverter urlConverter = this.converter;
        String strConvert = urlConverter != null ? urlConverter.convert(str) : str;
        final MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("file", str2, requestBody);
        stringMap.forEach(new StringMap.Consumer() { // from class: com.qiniu.android.http.Client.6
            @Override // com.qiniu.android.utils.StringMap.Consumer
            public void accept(String str3, Object obj) {
                builder.addFormDataPart(str3, obj.toString());
            }
        });
        builder.setType(MediaType.parse("multipart/form-data"));
        RequestBody requestBodyBuild = builder.build();
        if (progressHandler != null || cancellationHandler != null) {
            requestBodyBuild = new CountingRequestBody(requestBodyBuild, progressHandler, j, cancellationHandler);
        }
        asyncSend(new Request.Builder().url(strConvert).post(requestBodyBuild), null, upToken, j, completionHandler);
    }

    public void asyncGet(String str, StringMap stringMap, UpToken upToken, CompletionHandler completionHandler) {
        asyncSend(new Request.Builder().get().url(str), stringMap, upToken, 0L, completionHandler);
    }

    public ResponseInfo syncGet(String str, StringMap stringMap) {
        return send(new Request.Builder().get().url(str), stringMap);
    }

    private ResponseInfo send(final Request.Builder builder, StringMap stringMap) {
        if (stringMap != null) {
            stringMap.forEach(new StringMap.Consumer() { // from class: com.qiniu.android.http.Client.7
                @Override // com.qiniu.android.utils.StringMap.Consumer
                public void accept(String str, Object obj) {
                    builder.header(str, obj.toString());
                }
            });
        }
        builder.header("User-Agent", UserAgent.instance().getUa(""));
        System.currentTimeMillis();
        ResponseTag responseTag = new ResponseTag();
        Request requestBuild = builder.tag(responseTag).build();
        try {
            return buildResponseInfo(this.httpClient.newCall(requestBuild).execute(), responseTag.ip, responseTag.duration, UpToken.NULL, 0L);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseInfo.create(null, -1, "", "", "", requestBuild.url().host(), requestBuild.url().encodedPath(), responseTag.ip, requestBuild.url().port(), responseTag.duration, -1L, e.getMessage(), UpToken.NULL, 0L);
        }
    }

    public ResponseInfo syncMultipartPost(String str, PostArgs postArgs, UpToken upToken) {
        RequestBody requestBodyCreate;
        long length;
        if (postArgs.file != null) {
            requestBodyCreate = RequestBody.create(MediaType.parse(postArgs.mimeType), postArgs.file);
            length = postArgs.file.length();
        } else {
            requestBodyCreate = RequestBody.create(MediaType.parse(postArgs.mimeType), postArgs.data);
            length = postArgs.data.length;
        }
        return syncMultipartPost(str, postArgs.params, upToken, length, postArgs.fileName, requestBodyCreate);
    }

    private ResponseInfo syncMultipartPost(String str, StringMap stringMap, UpToken upToken, long j, String str2, RequestBody requestBody) {
        final MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("file", str2, requestBody);
        stringMap.forEach(new StringMap.Consumer() { // from class: com.qiniu.android.http.Client.8
            @Override // com.qiniu.android.utils.StringMap.Consumer
            public void accept(String str3, Object obj) {
                builder.addFormDataPart(str3, obj.toString());
            }
        });
        builder.setType(MediaType.parse("multipart/form-data"));
        return syncSend(new Request.Builder().url(str).post(builder.build()), null, upToken, j);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ResponseInfo syncSend(final Request.Builder builder, StringMap stringMap, UpToken upToken, long j) {
        int i;
        if (stringMap != null) {
            stringMap.forEach(new StringMap.Consumer() { // from class: com.qiniu.android.http.Client.9
                @Override // com.qiniu.android.utils.StringMap.Consumer
                public void accept(String str, Object obj) {
                    builder.header(str, obj.toString());
                }
            });
        }
        builder.header("User-Agent", UserAgent.instance().getUa(upToken.accessKey));
        Request request = null;
        ResponseTag responseTag = new ResponseTag();
        try {
            Request requestBuild = builder.tag(responseTag).build();
            try {
                return buildResponseInfo(this.httpClient.newCall(requestBuild).execute(), responseTag.ip, responseTag.duration, upToken, j);
            } catch (Exception e) {
                e = e;
                request = requestBuild;
                e.printStackTrace();
                String message = e.getMessage();
                if (e instanceof UnknownHostException) {
                    i = ResponseInfo.UnknownHost;
                } else if (message != null && message.indexOf("Broken pipe") == 0) {
                    i = ResponseInfo.NetworkConnectionLost;
                } else if (e instanceof SocketTimeoutException) {
                    i = ResponseInfo.TimedOut;
                } else {
                    i = e instanceof ConnectException ? ResponseInfo.CannotConnectToHost : -1;
                }
                int i2 = i;
                HttpUrl httpUrlUrl = request.url();
                return ResponseInfo.create(null, i2, "", "", "", httpUrlUrl.host(), httpUrlUrl.encodedPath(), "", httpUrlUrl.port(), 0L, 0L, e.getMessage(), upToken, j);
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    private static class ResponseTag {
        public long duration;
        public String ip;

        private ResponseTag() {
            this.ip = "";
            this.duration = -1L;
        }
    }
}
