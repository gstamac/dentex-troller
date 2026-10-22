package com.zendesk.sdk.util;

import com.zendesk.sdk.network.impl.ApplicationScope;
import com.zendesk.sdk.network.impl.RestAdapterInjector;
import com.zendesk.sdk.network.impl.RestAdapterModule;

/* JADX INFO: loaded from: classes2.dex */
public class ModuleInjector {
    public static LibraryModule injectCachedLibraryModule(final ApplicationScope applicationScope) {
        return injectLibraryModuleCache(applicationScope).get(new DependencyProvider<LibraryModule>() { // from class: com.zendesk.sdk.util.ModuleInjector.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.zendesk.sdk.util.DependencyProvider
            public LibraryModule provideDependency() {
                return ModuleInjector.injectLibraryModule(applicationScope);
            }
        });
    }

    public static RestAdapterModule injectCachedRestAdapterModule(final ApplicationScope applicationScope) {
        return injectRestAdapterModuleCache(applicationScope).get(new DependencyProvider<RestAdapterModule>() { // from class: com.zendesk.sdk.util.ModuleInjector.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.zendesk.sdk.util.DependencyProvider
            public RestAdapterModule provideDependency() {
                return ModuleInjector.injectRestAdapterModule(applicationScope);
            }
        });
    }

    private static ScopeCache<LibraryModule> injectLibraryModuleCache(ApplicationScope applicationScope) {
        return applicationScope.getLibraryModuleCache();
    }

    private static ScopeCache<RestAdapterModule> injectRestAdapterModuleCache(ApplicationScope applicationScope) {
        return applicationScope.getRestAdapterCache();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static RestAdapterModule injectRestAdapterModule(ApplicationScope applicationScope) {
        return new RestAdapterModule(RestAdapterInjector.injectRestAdapter(applicationScope));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static LibraryModule injectLibraryModule(ApplicationScope applicationScope) {
        return new LibraryModule(LibraryInjector.injectGson(applicationScope), LibraryInjector.injectOkHttpClient(applicationScope));
    }
}
