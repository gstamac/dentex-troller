package com.zendesk.sdk.network.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.UrlConnectionDownloader;
import com.zendesk.logger.Logger;
import com.zendesk.util.StringUtils;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes2.dex */
public class ZendeskPicassoProvider {
    private static final String HTTPS_SCHEME = "https";
    private static final String HTTP_SCHEME = "http";
    private static final String LOG_TAG = "ZendeskPicassoProvider";
    private static Picasso singleton;

    private ZendeskPicassoProvider() {
    }

    public static Picasso getInstance(Context context) {
        if (singleton == null) {
            synchronized (ZendeskPicassoProvider.class) {
                if (singleton == null) {
                    singleton = new Picasso.Builder(context).executor(Executors.newFixedThreadPool(2)).defaultBitmapConfig(Bitmap.Config.RGB_565).downloader(new UrlConnectionDownloader(context) { // from class: com.zendesk.sdk.network.impl.ZendeskPicassoProvider.1
                        @Override // com.squareup.picasso.UrlConnectionDownloader
                        protected HttpURLConnection openConnection(Uri uri) throws IOException {
                            String scheme = uri.getScheme();
                            if (StringUtils.hasLength(scheme) && "http".equals(scheme)) {
                                Logger.d(ZendeskPicassoProvider.LOG_TAG, String.format("Loading image: %s - http scheme detected, enforcing https", uri.toString()), new Object[0]);
                                uri = uri.buildUpon().scheme("https").build();
                            }
                            HttpURLConnection httpURLConnectionOpenConnection = super.openConnection(uri);
                            String storedAccessTokenAsBearerToken = ZendeskConfig.INSTANCE.storage().identityStorage().getStoredAccessTokenAsBearerToken();
                            if (storedAccessTokenAsBearerToken != null) {
                                httpURLConnectionOpenConnection.addRequestProperty("Authorization", storedAccessTokenAsBearerToken);
                            }
                            return httpURLConnectionOpenConnection;
                        }
                    }).build();
                }
            }
        }
        return singleton;
    }
}
