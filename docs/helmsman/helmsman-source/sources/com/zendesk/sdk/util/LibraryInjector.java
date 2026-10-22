package com.zendesk.sdk.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.network.impl.ApplicationScope;
import com.zendesk.sdk.network.impl.DefaultZendeskUnauthorizedInterceptor;
import com.zendesk.sdk.network.impl.ZendeskRequestInterceptor;
import com.zendesk.sdk.storage.StorageInjector;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/* JADX INFO: loaded from: classes2.dex */
public class LibraryInjector {
    public static Gson injectCachedGson(ApplicationScope applicationScope) {
        return ModuleInjector.injectCachedLibraryModule(applicationScope).getGson();
    }

    public static Gson injectGson(ApplicationScope applicationScope) {
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).excludeFieldsWithModifiers(128, 8).create();
    }

    public static OkHttpClient injectOkHttpClient(ApplicationScope applicationScope) {
        return new OkHttpClient.Builder().addInterceptor(injectDefaultZendeskUnauthorizedInterceptor(applicationScope)).addInterceptor(injectZendeskRequestInterceptor(applicationScope)).addInterceptor(injectHttpLoggingInterceptor(applicationScope)).connectTimeout(30L, TimeUnit.SECONDS).readTimeout(30L, TimeUnit.SECONDS).writeTimeout(30L, TimeUnit.SECONDS).connectionSpecs(BaseInjector.injectConnectionSpec(applicationScope)).build();
    }

    private static Interceptor injectHttpLoggingInterceptor(ApplicationScope applicationScope) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(Logger.isLoggable() ? HttpLoggingInterceptor.Level.HEADERS : HttpLoggingInterceptor.Level.NONE);
        return httpLoggingInterceptor;
    }

    private static Interceptor injectZendeskRequestInterceptor(ApplicationScope applicationScope) {
        return new ZendeskRequestInterceptor(BaseInjector.injectOAuthToken(applicationScope), BaseInjector.injectUserAgentHeader(applicationScope));
    }

    private static Interceptor injectDefaultZendeskUnauthorizedInterceptor(ApplicationScope applicationScope) {
        return new DefaultZendeskUnauthorizedInterceptor(StorageInjector.injectCachedSdkStorage(applicationScope));
    }
}
