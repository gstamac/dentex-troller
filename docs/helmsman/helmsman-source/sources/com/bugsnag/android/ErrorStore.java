package com.bugsnag.android;

import android.content.Context;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
class ErrorStore {
    private static final int MAX_STORED_ERRORS = 100;
    private static final String UNSENT_ERROR_PATH = "/bugsnag-errors/";
    final Configuration config;
    final String path;

    ErrorStore(Configuration configuration, Context context) {
        this.config = configuration;
        String str = null;
        try {
            String str2 = context.getCacheDir().getAbsolutePath() + UNSENT_ERROR_PATH;
            File file = new File(str2);
            file.mkdirs();
            if (file.exists()) {
                str = str2;
            } else {
                Logger.warn("Could not prepare error storage directory");
            }
        } catch (Exception e) {
            Logger.warn("Could not prepare error storage directory", e);
        }
        this.path = str;
    }

    void flush() {
        if (this.path == null) {
            return;
        }
        Async.run(new Runnable() { // from class: com.bugsnag.android.ErrorStore.1
            @Override // java.lang.Runnable
            public void run() throws Throwable {
                File[] fileArrListFiles;
                File file = new File(ErrorStore.this.path);
                if (!file.exists() || !file.isDirectory() || (fileArrListFiles = file.listFiles()) == null || fileArrListFiles.length <= 0) {
                    return;
                }
                Logger.info(String.format(Locale.US, "Sending %d saved error(s) to Bugsnag", Integer.valueOf(fileArrListFiles.length)));
                for (File file2 : fileArrListFiles) {
                    try {
                        HttpClient.post(ErrorStore.this.config.getEndpoint(), new Report(ErrorStore.this.config.getApiKey(), file2));
                        Logger.info("Deleting sent error file " + file2.getName());
                        if (!file2.delete()) {
                            file2.deleteOnExit();
                        }
                    } catch (HttpClient.NetworkException e) {
                        Logger.warn("Could not send previously saved error(s) to Bugsnag, will try again later", e);
                    } catch (Exception e2) {
                        Logger.warn("Problem sending unsent error from disk", e2);
                        if (!file2.delete()) {
                            file2.deleteOnExit();
                        }
                    }
                }
            }
        });
    }

    void write(Error error) {
        File[] fileArrListFiles;
        if (this.path == null) {
            return;
        }
        File file = new File(this.path);
        if (file.isDirectory() && (fileArrListFiles = file.listFiles()) != null && fileArrListFiles.length >= 100) {
            Arrays.sort(fileArrListFiles);
            Logger.warn(String.format("Discarding oldest error as stored error limit reached (%s)", fileArrListFiles[0].getPath()));
            if (!fileArrListFiles[0].delete()) {
                fileArrListFiles[0].deleteOnExit();
            }
        }
        String str = String.format(Locale.US, "%s%d.json", this.path, Long.valueOf(System.currentTimeMillis()));
        FileWriter fileWriter = null;
        try {
            try {
                FileWriter fileWriter2 = new FileWriter(str);
                try {
                    JsonStream jsonStream = new JsonStream(fileWriter2);
                    jsonStream.value(error);
                    jsonStream.close();
                    Logger.info(String.format("Saved unsent error to disk (%s) ", str));
                    IOUtils.closeQuietly(fileWriter2);
                } catch (Exception e) {
                    e = e;
                    fileWriter = fileWriter2;
                    Logger.warn(String.format("Couldn't save unsent error to disk (%s) ", str), e);
                    IOUtils.closeQuietly(fileWriter);
                } catch (Throwable th) {
                    th = th;
                    fileWriter = fileWriter2;
                    IOUtils.closeQuietly(fileWriter);
                    throw th;
                }
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }
}
