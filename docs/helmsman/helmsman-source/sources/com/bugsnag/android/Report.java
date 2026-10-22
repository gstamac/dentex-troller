package com.bugsnag.android;

import java.io.File;

/* JADX INFO: loaded from: classes.dex */
public class Report implements JsonStream.Streamable {
    private String apiKey;
    private Error error;
    private final File errorFile;
    private Notifier notifier;

    Report(String str, File file) {
        this.apiKey = str;
        this.error = null;
        this.errorFile = file;
        this.notifier = Notifier.getInstance();
    }

    Report(String str, Error error) {
        this.apiKey = str;
        this.error = error;
        this.errorFile = null;
        this.notifier = Notifier.getInstance();
    }

    @Override // com.bugsnag.android.JsonStream.Streamable
    public void toStream(JsonStream jsonStream) throws Throwable {
        jsonStream.beginObject();
        jsonStream.name("apiKey").value(this.apiKey);
        jsonStream.name("notifier").value(this.notifier);
        jsonStream.name("events").beginArray();
        Error error = this.error;
        if (error != null) {
            jsonStream.value(error);
        }
        File file = this.errorFile;
        if (file != null) {
            jsonStream.value(file);
        }
        jsonStream.endArray();
        jsonStream.endObject();
    }

    public Error getError() {
        return this.error;
    }

    public void setApiKey(String str) {
        this.apiKey = str;
    }

    public void setNotifierVersion(String str) {
        this.notifier.setVersion(str);
    }

    public void setNotifierName(String str) {
        this.notifier.setName(str);
    }

    public void setNotifierURL(String str) {
        this.notifier.setURL(str);
    }
}
