package com.facebook.internal;

import android.net.Uri;
import com.facebook.LoggingBehavior;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes.dex */
class UrlRedirectCache {
    private static final String REDIRECT_CONTENT_TAG = "UrlRedirectCache_Redirect";
    static final String TAG = "UrlRedirectCache";
    private static FileLruCache urlRedirectCache;

    UrlRedirectCache() {
    }

    static synchronized FileLruCache getCache() throws IOException {
        if (urlRedirectCache == null) {
            urlRedirectCache = new FileLruCache(TAG, new FileLruCache.Limits());
        }
        return urlRedirectCache;
    }

    static Uri getRedirectedUri(Uri uri) throws Throwable {
        Throwable th;
        InputStreamReader inputStreamReader;
        InputStreamReader inputStreamReader2;
        if (uri == null) {
            return null;
        }
        String string = uri.toString();
        try {
            FileLruCache cache = getCache();
            inputStreamReader2 = null;
            boolean z = false;
            while (true) {
                try {
                    InputStream inputStream = cache.get(string, REDIRECT_CONTENT_TAG);
                    if (inputStream == null) {
                        break;
                    }
                    inputStreamReader = new InputStreamReader(inputStream);
                    try {
                        char[] cArr = new char[128];
                        StringBuilder sb = new StringBuilder();
                        while (true) {
                            int i = inputStreamReader.read(cArr, 0, 128);
                            if (i <= 0) {
                                break;
                            }
                            sb.append(cArr, 0, i);
                        }
                        Utility.closeQuietly(inputStreamReader);
                        inputStreamReader2 = inputStreamReader;
                        string = sb.toString();
                        z = true;
                    } catch (IOException unused) {
                        inputStreamReader2 = inputStreamReader;
                    } catch (Throwable th2) {
                        th = th2;
                        Utility.closeQuietly(inputStreamReader);
                        throw th;
                    }
                } catch (IOException unused2) {
                } catch (Throwable th3) {
                    th = th3;
                    inputStreamReader = inputStreamReader2;
                }
                Utility.closeQuietly(inputStreamReader2);
                return null;
            }
            if (z) {
                Uri uri2 = Uri.parse(string);
                Utility.closeQuietly(inputStreamReader2);
                return uri2;
            }
        } catch (IOException unused3) {
            inputStreamReader2 = null;
        } catch (Throwable th4) {
            th = th4;
            inputStreamReader = null;
        }
        Utility.closeQuietly(inputStreamReader2);
        return null;
    }

    static void cacheUriRedirect(Uri uri, Uri uri2) {
        if (uri == null || uri2 == null) {
            return;
        }
        OutputStream outputStreamOpenPutStream = null;
        try {
            outputStreamOpenPutStream = getCache().openPutStream(uri.toString(), REDIRECT_CONTENT_TAG);
            outputStreamOpenPutStream.write(uri2.toString().getBytes());
        } catch (IOException unused) {
        } finally {
            Utility.closeQuietly(outputStreamOpenPutStream);
        }
    }

    static void clearCache() {
        try {
            getCache().clearCache();
        } catch (IOException e) {
            Logger.log(LoggingBehavior.CACHE, 5, TAG, "clearCache failed " + e.getMessage());
        }
    }
}
