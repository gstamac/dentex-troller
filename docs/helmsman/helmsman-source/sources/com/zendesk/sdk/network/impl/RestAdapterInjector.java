package com.zendesk.sdk.network.impl;

import com.zendesk.sdk.util.BaseInjector;
import com.zendesk.sdk.util.LibraryInjector;
import com.zendesk.sdk.util.ModuleInjector;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/* JADX INFO: loaded from: classes2.dex */
public class RestAdapterInjector {
    static Retrofit injectCachedRestAdapter(ApplicationScope applicationScope) {
        return ModuleInjector.injectCachedRestAdapterModule(applicationScope).getRetrofit();
    }

    public static Retrofit injectRestAdapter(ApplicationScope applicationScope) {
        return new Retrofit.Builder().baseUrl(BaseInjector.injectUrl(applicationScope)).addConverterFactory(injectGsonConverterFactory(applicationScope)).client(LibraryInjector.injectOkHttpClient(applicationScope)).build();
    }

    private static GsonConverterFactory injectGsonConverterFactory(ApplicationScope applicationScope) {
        return GsonConverterFactory.create(LibraryInjector.injectCachedGson(applicationScope));
    }
}
