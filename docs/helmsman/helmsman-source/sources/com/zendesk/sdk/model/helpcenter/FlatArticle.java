package com.zendesk.sdk.model.helpcenter;

/* JADX INFO: loaded from: classes2.dex */
public class FlatArticle implements Comparable<FlatArticle> {
    private Article article;
    private Category category;
    private Section section;

    public FlatArticle(Category category, Section section, Article article) {
        this.category = category == null ? new Category() : category;
        this.section = section == null ? new Section() : section;
        this.article = article == null ? new Article() : article;
    }

    public Category getCategory() {
        return this.category;
    }

    public Section getSection() {
        return this.section;
    }

    public Article getArticle() {
        return this.article;
    }

    public String toString() {
        return this.category.getName() + ", " + this.section.getName() + ", " + this.article.getTitle();
    }

    @Override // java.lang.Comparable
    public int compareTo(FlatArticle flatArticle) {
        if (flatArticle == null) {
            return -1;
        }
        return toString().compareTo(flatArticle.toString());
    }
}
