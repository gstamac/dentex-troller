package com.bugsnag.android;

import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
class User implements JsonStream.Streamable {
    private String email;
    private String id;
    private String name;

    User() {
    }

    User(String str, String str2, String str3) {
        this.id = str;
        this.email = str2;
        this.name = str3;
    }

    User(User user) {
        this(user.id, user.email, user.name);
    }

    @Override // com.bugsnag.android.JsonStream.Streamable
    public void toStream(JsonStream jsonStream) throws IOException {
        jsonStream.beginObject();
        jsonStream.name("id").value(this.id);
        jsonStream.name("email").value(this.email);
        jsonStream.name("name").value(this.name);
        jsonStream.endObject();
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }
}
