package com.zendesk.sdk.model.helpcenter;

import com.zendesk.util.CollectionUtils;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class ArticlesListResponse implements ArticlesResponse {
    private List<Article> articles;
    private List<Category> categories;
    private String nextPage;
    private String previousPage;
    private List<Section> sections;
    private List<User> users;

    @Override // com.zendesk.sdk.model.helpcenter.ArticlesResponse
    public List<Article> getArticles() {
        return CollectionUtils.copyOf(this.articles);
    }

    @Override // com.zendesk.sdk.model.helpcenter.ArticlesResponse
    public List<Category> getCategories() {
        return CollectionUtils.copyOf(this.categories);
    }

    @Override // com.zendesk.sdk.model.helpcenter.ArticlesResponse
    public List<Section> getSections() {
        return CollectionUtils.copyOf(this.sections);
    }

    @Override // com.zendesk.sdk.model.helpcenter.ArticlesResponse
    public List<User> getUsers() {
        return CollectionUtils.copyOf(this.users);
    }

    public String getNextPage() {
        return this.nextPage;
    }

    public String getPreviousPage() {
        return this.previousPage;
    }
}
