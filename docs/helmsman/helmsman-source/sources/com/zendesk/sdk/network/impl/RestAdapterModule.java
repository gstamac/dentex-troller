package com.zendesk.sdk.network.impl;

import retrofit2.Retrofit;

/* JADX INFO: loaded from: classes2.dex */
public class RestAdapterModule {
    private final Retrofit retrofit;

    public RestAdapterModule(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public Retrofit getRetrofit() {
        return this.retrofit;
    }
}
