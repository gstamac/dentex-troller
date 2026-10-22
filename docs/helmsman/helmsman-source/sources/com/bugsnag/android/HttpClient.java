package com.bugsnag.android;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
class HttpClient {
    HttpClient() {
    }

    static class BadResponseException extends Exception {
        public BadResponseException(String str, int i) {
            super(String.format(Locale.US, "Got non-200 response code (%d) from %s", Integer.valueOf(i), str));
        }
    }

    static class NetworkException extends IOException {
        public NetworkException(String str, Exception exc) {
            super(String.format("Network error when posting to %s", str));
            initCause(exc);
        }
    }

    static void post(String str, JsonStream.Streamable streamable) throws Throwable {
        HttpURLConnection httpURLConnection = null;
        OutputStream outputStream = null;
        try {
            try {
                HttpURLConnection httpURLConnection2 = (HttpURLConnection) new URL(str).openConnection();
                try {
                    httpURLConnection2.setDoOutput(true);
                    httpURLConnection2.setChunkedStreamingMode(0);
                    httpURLConnection2.addRequestProperty(com.qiniu.android.http.Client.ContentTypeHeader, "application/json");
                    try {
                        outputStream = httpURLConnection2.getOutputStream();
                        JsonStream jsonStream = new JsonStream(new OutputStreamWriter(outputStream));
                        streamable.toStream(jsonStream);
                        jsonStream.close();
                        IOUtils.closeQuietly(outputStream);
                        int responseCode = httpURLConnection2.getResponseCode();
                        if (responseCode / 100 != 2) {
                            throw new BadResponseException(str, responseCode);
                        }
                        IOUtils.close(httpURLConnection2);
                    } catch (Throwable th) {
                        IOUtils.closeQuietly(outputStream);
                        throw th;
                    }
                } catch (IOException e) {
                    e = e;
                    throw new NetworkException(str, e);
                } catch (Throwable th2) {
                    th = th2;
                    httpURLConnection = httpURLConnection2;
                    IOUtils.close(httpURLConnection);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
            }
        } catch (IOException e2) {
            e = e2;
        }
    }
}
