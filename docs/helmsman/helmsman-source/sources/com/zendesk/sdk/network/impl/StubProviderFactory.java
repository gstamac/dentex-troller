package com.zendesk.sdk.network.impl;

import com.zendesk.logger.Logger;
import com.zendesk.service.ErrorResponseAdapter;
import com.zendesk.service.ZendeskCallback;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
class StubProviderFactory {
    private static final String LOG_TAG = "StubProviderFactory";

    StubProviderFactory() {
    }

    static <E> E getStubProvider(Class<E> cls) {
        try {
            return (E) Proxy.getProxyClass(cls.getClassLoader(), cls).getConstructor(InvocationHandler.class).newInstance(new ZendeskCallbackInvocationHandler(cls));
        } catch (Exception e) {
            Logger.w(LOG_TAG, "Unable to create stub provider. Error: %s", e.getMessage());
            return null;
        }
    }

    private static class ZendeskCallbackInvocationHandler implements InvocationHandler {
        private Class clazz;

        ZendeskCallbackInvocationHandler(Class cls) {
            this.clazz = cls;
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
            ZendeskCallback zendeskCallback;
            int length = objArr.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    zendeskCallback = null;
                    break;
                }
                Object obj2 = objArr[i];
                if (obj2 instanceof ZendeskCallback) {
                    zendeskCallback = (ZendeskCallback) obj2;
                    break;
                }
                i++;
            }
            String simpleName = this.clazz.getSimpleName();
            String str = String.format(Locale.US, "Zendesk not initialised! You tried to call: %s#%s(...)", simpleName, method.getName());
            Logger.d(simpleName, str, new Object[0]);
            if (zendeskCallback != null) {
                zendeskCallback.onError(new ErrorResponseAdapter(str));
            }
            return null;
        }
    }
}
