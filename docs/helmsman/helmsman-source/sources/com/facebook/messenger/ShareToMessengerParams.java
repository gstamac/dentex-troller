package com.facebook.messenger;

import android.net.Uri;
import im.helmsman.helmsmanandroid.factory.OkHttpFactory;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class ShareToMessengerParams {
    public static final Set<String> VALID_EXTERNAL_URI_SCHEMES;
    public static final Set<String> VALID_MIME_TYPES;
    public static final Set<String> VALID_URI_SCHEMES;
    public final Uri externalUri;
    public final String metaData;
    public final String mimeType;
    public final Uri uri;

    static {
        HashSet hashSet = new HashSet();
        hashSet.add("image/*");
        hashSet.add("image/jpeg");
        hashSet.add("image/png");
        hashSet.add("image/gif");
        hashSet.add("image/webp");
        hashSet.add("video/*");
        hashSet.add("video/mp4");
        hashSet.add("audio/*");
        hashSet.add("audio/mpeg");
        VALID_MIME_TYPES = Collections.unmodifiableSet(hashSet);
        HashSet hashSet2 = new HashSet();
        hashSet2.add("content");
        hashSet2.add("android.resource");
        hashSet2.add("file");
        VALID_URI_SCHEMES = Collections.unmodifiableSet(hashSet2);
        HashSet hashSet3 = new HashSet();
        hashSet3.add(OkHttpFactory.TYPE_HTTP);
        hashSet3.add(OkHttpFactory.TYPE_HTTPS);
        VALID_EXTERNAL_URI_SCHEMES = Collections.unmodifiableSet(hashSet3);
    }

    ShareToMessengerParams(ShareToMessengerParamsBuilder shareToMessengerParamsBuilder) {
        Uri uri = shareToMessengerParamsBuilder.getUri();
        this.uri = uri;
        String mimeType = shareToMessengerParamsBuilder.getMimeType();
        this.mimeType = mimeType;
        this.metaData = shareToMessengerParamsBuilder.getMetaData();
        Uri externalUri = shareToMessengerParamsBuilder.getExternalUri();
        this.externalUri = externalUri;
        if (uri == null) {
            throw new NullPointerException("Must provide non-null uri");
        }
        if (mimeType == null) {
            throw new NullPointerException("Must provide mimeType");
        }
        if (!VALID_URI_SCHEMES.contains(uri.getScheme())) {
            throw new IllegalArgumentException("Unsupported URI scheme: " + uri.getScheme());
        }
        if (!VALID_MIME_TYPES.contains(mimeType)) {
            throw new IllegalArgumentException("Unsupported mime-type: " + mimeType);
        }
        if (externalUri != null && !VALID_EXTERNAL_URI_SCHEMES.contains(externalUri.getScheme())) {
            throw new IllegalArgumentException("Unsupported external uri scheme: " + externalUri.getScheme());
        }
    }

    public static ShareToMessengerParamsBuilder newBuilder(Uri uri, String str) {
        return new ShareToMessengerParamsBuilder(uri, str);
    }
}
