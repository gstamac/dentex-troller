package com.bugsnag.android;

import com.facebook.internal.ServerProtocol;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
class Notifier implements JsonStream.Streamable {
    static final String NOTIFIER_NAME = "Android Bugsnag Notifier";
    static final String NOTIFIER_URL = "https://bugsnag.com";
    static final String NOTIFIER_VERSION = "3.9.0";
    private static final Notifier instance = new Notifier();
    private String name = NOTIFIER_NAME;
    private String version = NOTIFIER_VERSION;
    private String url = NOTIFIER_URL;

    public static Notifier getInstance() {
        return instance;
    }

    Notifier() {
    }

    @Override // com.bugsnag.android.JsonStream.Streamable
    public void toStream(JsonStream jsonStream) throws IOException {
        jsonStream.beginObject();
        jsonStream.name("name").value(this.name);
        jsonStream.name(ServerProtocol.FALLBACK_DIALOG_PARAM_VERSION).value(this.version);
        jsonStream.name("url").value(this.url);
        jsonStream.endObject();
    }

    public void setVersion(String str) {
        this.version = str;
    }

    public void setURL(String str) {
        this.url = str;
    }

    public void setName(String str) {
        this.name = str;
    }
}
