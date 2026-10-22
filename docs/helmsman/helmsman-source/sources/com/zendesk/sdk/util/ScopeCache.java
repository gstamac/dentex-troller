package com.zendesk.sdk.util;

import com.zendesk.logger.Logger;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
public class ScopeCache<T> {
    private static final String LOG_TAG = "ScopeCache";
    private boolean provided = false;
    private T cache = null;

    public synchronized T get(DependencyProvider<? extends T> dependencyProvider) {
        if (!this.provided) {
            this.cache = dependencyProvider.provideDependency();
            this.provided = true;
            Logger.d(String.format(Locale.US, "%s - %s", LOG_TAG, this.cache.getClass().getSimpleName()), "Creating new object", new Object[0]);
        }
        return this.cache;
    }
}
