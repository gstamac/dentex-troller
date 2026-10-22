package com.zendesk.sdk.model.helpcenter;

import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public interface ArticlesResponse {
    List<Article> getArticles();

    List<Category> getCategories();

    List<Section> getSections();

    List<User> getUsers();
}
