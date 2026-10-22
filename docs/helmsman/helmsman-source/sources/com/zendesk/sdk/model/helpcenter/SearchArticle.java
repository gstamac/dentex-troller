package com.zendesk.sdk.model.helpcenter;

import java.io.Serializable;

/* JADX INFO: loaded from: classes2.dex */
public class SearchArticle implements Serializable {
    private final Article mArticle;
    private final Category mCategory;
    private final Section mSection;

    public SearchArticle(Article article, Section section, Category category) {
        this.mArticle = article;
        this.mSection = section;
        this.mCategory = category;
    }

    public Article getArticle() {
        return this.mArticle;
    }

    public Section getSection() {
        return this.mSection;
    }

    public Category getCategory() {
        return this.mCategory;
    }
}
