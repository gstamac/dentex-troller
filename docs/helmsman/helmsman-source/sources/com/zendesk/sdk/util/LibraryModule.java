package com.zendesk.sdk.util;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;

/* JADX INFO: loaded from: classes2.dex */
public class LibraryModule {
    private final Gson gson;
    private final OkHttpClient okHttpClient;

    LibraryModule(Gson gson, OkHttpClient okHttpClient) {
        this.gson = gson;
        this.okHttpClient = okHttpClient;
    }

    public OkHttpClient getOkHttpClient() {
        return this.okHttpClient;
    }

    public Gson getGson() {
        return this.gson;
    }
}
