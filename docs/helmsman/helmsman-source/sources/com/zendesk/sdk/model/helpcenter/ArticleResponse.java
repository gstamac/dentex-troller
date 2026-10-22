package com.zendesk.sdk.model.helpcenter;

import com.zendesk.util.CollectionUtils;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class ArticleResponse {
    private Article article;
    private List<User> users;

    public Article getArticle() {
        return this.article;
    }

    public List<User> getUsers() {
        return CollectionUtils.copyOf(this.users);
    }
}
