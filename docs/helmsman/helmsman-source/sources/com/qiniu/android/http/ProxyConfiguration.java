package com.qiniu.android.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/* JADX INFO: loaded from: classes2.dex */
public final class ProxyConfiguration {
    public final String hostAddress;
    public final String password;
    public final int port;
    public final Proxy.Type type;
    public final String user;

    public ProxyConfiguration(String str, int i, String str2, String str3, Proxy.Type type) {
        this.hostAddress = str;
        this.port = i;
        this.user = str2;
        this.password = str3;
        this.type = type;
    }

    public ProxyConfiguration(String str, int i) {
        this(str, i, null, null, Proxy.Type.HTTP);
    }

    Proxy proxy() {
        return new Proxy(this.type, new InetSocketAddress(this.hostAddress, this.port));
    }

    Authenticator authenticator() {
        return new Authenticator() { // from class: com.qiniu.android.http.ProxyConfiguration.1
            @Override // okhttp3.Authenticator
            public Request authenticate(Route route, Response response) throws IOException {
                return response.request().newBuilder().header("Proxy-Authorization", Credentials.basic(ProxyConfiguration.this.user, ProxyConfiguration.this.password)).header("Proxy-Connection", "Keep-Alive").build();
            }
        };
    }
}
