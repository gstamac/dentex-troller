package com.qiniu.android.storage;

import com.qiniu.android.common.AutoZone;
import com.qiniu.android.common.Zone;
import com.qiniu.android.http.Dns;
import com.qiniu.android.http.ProxyConfiguration;
import com.qiniu.android.http.UrlConverter;
import java.io.File;

/* JADX INFO: loaded from: classes2.dex */
public final class Configuration {
    public static final int BLOCK_SIZE = 4194304;
    public final int chunkSize;
    public final int connectTimeout;
    public Dns dns;
    public final KeyGenerator keyGen;
    public final ProxyConfiguration proxy;
    public final int putThreshold;
    public final Recorder recorder;
    public final int responseTimeout;
    public final int retryMax;
    public UrlConverter urlConverter;
    public boolean useHttps;
    public Zone zone;

    private Configuration(Builder builder) {
        this.useHttps = builder.useHttps;
        this.chunkSize = builder.chunkSize;
        this.putThreshold = builder.putThreshold;
        this.connectTimeout = builder.connectTimeout;
        this.responseTimeout = builder.responseTimeout;
        this.recorder = builder.recorder;
        this.keyGen = getKeyGen(builder.keyGen);
        this.retryMax = builder.retryMax;
        this.proxy = builder.proxy;
        this.urlConverter = builder.urlConverter;
        this.zone = builder.zone == null ? AutoZone.autoZone : builder.zone;
        this.dns = builder.dns;
    }

    private KeyGenerator getKeyGen(KeyGenerator keyGenerator) {
        return keyGenerator == null ? new KeyGenerator() { // from class: com.qiniu.android.storage.Configuration.1
            @Override // com.qiniu.android.storage.KeyGenerator
            public String gen(String str, File file) {
                return str + "_._" + ((Object) new StringBuffer(file.getAbsolutePath()).reverse());
            }
        } : keyGenerator;
    }

    public static class Builder {
        private Zone zone = null;
        private Recorder recorder = null;
        private KeyGenerator keyGen = null;
        private ProxyConfiguration proxy = null;
        private boolean useHttps = false;
        private int chunkSize = 2097152;
        private int putThreshold = 4194304;
        private int connectTimeout = 10;
        private int responseTimeout = 60;
        private int retryMax = 3;
        private UrlConverter urlConverter = null;
        private Dns dns = null;

        public Builder zone(Zone zone) {
            this.zone = zone;
            return this;
        }

        public Builder recorder(Recorder recorder) {
            this.recorder = recorder;
            return this;
        }

        public Builder recorder(Recorder recorder, KeyGenerator keyGenerator) {
            this.recorder = recorder;
            this.keyGen = keyGenerator;
            return this;
        }

        public Builder proxy(ProxyConfiguration proxyConfiguration) {
            this.proxy = proxyConfiguration;
            return this;
        }

        public Builder chunkSize(int i) {
            this.chunkSize = i;
            return this;
        }

        public Builder putThreshhold(int i) {
            this.putThreshold = i;
            return this;
        }

        public Builder connectTimeout(int i) {
            this.connectTimeout = i;
            return this;
        }

        public Builder responseTimeout(int i) {
            this.responseTimeout = i;
            return this;
        }

        public Builder retryMax(int i) {
            this.retryMax = i;
            return this;
        }

        public Builder urlConverter(UrlConverter urlConverter) {
            this.urlConverter = urlConverter;
            return this;
        }

        public Builder dns(Dns dns) {
            this.dns = dns;
            return this;
        }

        public Builder useHttps(boolean z) {
            this.useHttps = z;
            return this;
        }

        public Configuration build() {
            return new Configuration(this);
        }
    }
}
