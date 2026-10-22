package com.qiniu.android.collect;

import com.qiniu.android.http.UserAgent;
import com.qiniu.android.storage.UpToken;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/* JADX INFO: loaded from: classes2.dex */
public final class UploadInfoCollector {
    private static OkHttpClient httpClient;
    private static UploadInfoCollector httpCollector;
    private static ExecutorService singleServer;
    private long lastUpload;
    private File recordFile = null;
    private final String recordFileName;
    private final String serverURL;

    public static abstract class RecordMsg {
        public abstract String toRecordMsg();
    }

    private UploadInfoCollector(String str, String str2) {
        this.recordFileName = str;
        this.serverURL = str2;
        try {
            reset0();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static UploadInfoCollector getHttpCollector() {
        if (httpCollector == null) {
            httpCollector = new UploadInfoCollector("_qiniu_record_file_hs5z9lo7anx03", Config.serverURL);
        }
        return httpCollector;
    }

    public static void clean() {
        try {
            ExecutorService executorService = singleServer;
            if (executorService != null) {
                executorService.shutdown();
            }
        } catch (Exception unused) {
        }
        singleServer = null;
        httpClient = null;
        try {
            getHttpCollector().clean0();
        } catch (Exception e) {
            e.printStackTrace();
        }
        httpCollector = null;
    }

    public static void reset() {
        try {
            getHttpCollector().reset0();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handleHttp(UpToken upToken, RecordMsg recordMsg) {
        try {
            if (Config.isRecord) {
                getHttpCollector().handle0(upToken, recordMsg);
            }
        } catch (Throwable unused) {
        }
    }

    public static void handleUpload(UpToken upToken, RecordMsg recordMsg) {
        handleHttp(upToken, recordMsg);
    }

    /* JADX WARN: Code duplicated, block: B:18:0x002b A[Catch: IOException -> 0x0036, PHI: r0
  0x002b: PHI (r0v4 java.io.FileOutputStream) = (r0v3 java.io.FileOutputStream), (r0v5 java.io.FileOutputStream) binds: [B:17:0x0029, B:22:0x0033] A[DONT_GENERATE, DONT_INLINE], TRY_ENTER, TRY_LEAVE, TryCatch #4 {IOException -> 0x0036, blocks: (B:5:0x0016, B:18:0x002b), top: B:34:0x0001 }] */
    private static void writeToFile(File file, String str, boolean z) throws Throwable {
        FileOutputStream fileOutputStream = null;
        try {
            try {
                try {
                    FileOutputStream fileOutputStream2 = new FileOutputStream(file, z);
                    try {
                        fileOutputStream2.write(str.getBytes(Charset.forName("UTF-8")));
                        fileOutputStream2.flush();
                        fileOutputStream2.close();
                    } catch (FileNotFoundException e) {
                        e = e;
                        fileOutputStream = fileOutputStream2;
                        e.printStackTrace();
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                    } catch (IOException e2) {
                        e = e2;
                        fileOutputStream = fileOutputStream2;
                        e.printStackTrace();
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                    } catch (Throwable th) {
                        th = th;
                        fileOutputStream = fileOutputStream2;
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (IOException unused) {
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (FileNotFoundException e3) {
                e = e3;
            } catch (IOException e4) {
                e = e4;
            }
        } catch (IOException unused2) {
        }
    }

    private static OkHttpClient getHttpClient() {
        if (httpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(10L, TimeUnit.SECONDS);
            builder.readTimeout(15L, TimeUnit.SECONDS);
            builder.writeTimeout((((Config.interval / 2) + 1) * 60) - 10, TimeUnit.SECONDS);
            httpClient = builder.build();
        }
        return httpClient;
    }

    private void clean0() {
        try {
            File file = this.recordFile;
            if (file != null) {
                file.delete();
            } else {
                new File(getRecordDir(Config.recordDir), this.recordFileName).delete();
            }
        } catch (Exception unused) {
        }
        this.recordFile = null;
    }

    private void reset0() throws IOException {
        ExecutorService executorService;
        if (Config.isRecord) {
            initRecordFile(getRecordDir(Config.recordDir));
        }
        if (!Config.isRecord && (executorService = singleServer) != null) {
            executorService.shutdown();
        }
        if (Config.isRecord) {
            ExecutorService executorService2 = singleServer;
            if (executorService2 == null || executorService2.isShutdown()) {
                singleServer = Executors.newSingleThreadExecutor();
            }
        }
    }

    private File getRecordDir(String str) {
        return new File(str);
    }

    private void initRecordFile(File file) throws IOException {
        if (file == null) {
            throw new IOException("record's dir is not setted");
        }
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new IOException("mkdir failed: " + file.getAbsolutePath());
            }
        } else {
            if (!file.isDirectory()) {
                throw new IOException(file.getAbsolutePath() + " is not a dir");
            }
            this.recordFile = new File(file, this.recordFileName);
        }
    }

    private void handle0(final UpToken upToken, final RecordMsg recordMsg) {
        ExecutorService executorService = singleServer;
        if (executorService == null || executorService.isShutdown()) {
            return;
        }
        singleServer.submit(new Runnable() { // from class: com.qiniu.android.collect.UploadInfoCollector.1
            @Override // java.lang.Runnable
            public void run() {
                if (Config.isRecord) {
                    try {
                        UploadInfoCollector.this.tryRecode(recordMsg.toRecordMsg(), UploadInfoCollector.this.recordFile);
                    } catch (Throwable unused) {
                    }
                }
            }
        });
        if (!Config.isUpload || upToken == UpToken.NULL) {
            return;
        }
        singleServer.submit(new Runnable() { // from class: com.qiniu.android.collect.UploadInfoCollector.2
            @Override // java.lang.Runnable
            public void run() {
                if (Config.isRecord && Config.isUpload) {
                    try {
                        UploadInfoCollector uploadInfoCollector = UploadInfoCollector.this;
                        uploadInfoCollector.tryUploadAndClean(upToken, uploadInfoCollector.recordFile);
                    } catch (Throwable unused) {
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryRecode(String str, File file) throws Throwable {
        if (!Config.isRecord || file.length() >= Config.maxRecordFileSize) {
            return;
        }
        writeToFile(file, str + "\n", true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryUploadAndClean(UpToken upToken, File file) throws Throwable {
        if (!Config.isUpload || file.length() <= Config.uploadThreshold) {
            return;
        }
        long time = new Date().getTime();
        if (time > this.lastUpload + ((long) (Config.interval * 60 * 1000))) {
            this.lastUpload = time;
            if (upload(upToken, file)) {
                writeToFile(file, "", false);
                writeToFile(file, "", false);
            }
        }
    }

    private boolean upload(UpToken upToken, File file) {
        try {
            Response responseExecute = getHttpClient().newCall(new Request.Builder().url(this.serverURL).addHeader("Authorization", "UpToken " + upToken.token).addHeader("User-Agent", UserAgent.instance().getUa(upToken.accessKey)).post(RequestBody.create(MediaType.parse("text/plain"), file)).build()).execute();
            try {
                return isOk(responseExecute);
            } finally {
                try {
                    responseExecute.body().close();
                } catch (Exception unused) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isOk(Response response) {
        return response.isSuccessful() && response.header("X-Reqid") != null;
    }
}
