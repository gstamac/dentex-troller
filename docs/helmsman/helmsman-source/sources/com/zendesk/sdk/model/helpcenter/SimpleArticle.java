package com.zendesk.sdk.model.helpcenter;

import java.io.Serializable;

/* JADX INFO: loaded from: classes2.dex */
public class SimpleArticle implements Serializable {
    private Long id;
    private String title;

    public SimpleArticle(Long l, String str) {
        this.id = l;
        this.title = str;
    }

    public Long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }
}
