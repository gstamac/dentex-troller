package com.bugsnag.android;

import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public enum Severity implements JsonStream.Streamable {
    ERROR("error"),
    WARNING("warning"),
    INFO("info");

    private final String name;

    Severity(String str) {
        this.name = str;
    }

    @Override // com.bugsnag.android.JsonStream.Streamable
    public void toStream(JsonStream jsonStream) throws IOException {
        jsonStream.value(this.name);
    }
}
