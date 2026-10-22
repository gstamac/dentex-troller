package com.zendesk.sdk.model.helpcenter.help;

import com.zendesk.util.CollectionUtils;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class HelpResponse {
    private List<ArticleItem> articles;
    private List<CategoryItem> categories;
    private List<SectionItem> sections;

    public List<CategoryItem> getCategories() {
        return CollectionUtils.copyOf(this.categories);
    }

    public List<SectionItem> getSections() {
        return CollectionUtils.copyOf(this.sections);
    }

    public List<ArticleItem> getArticles() {
        return CollectionUtils.copyOf(this.articles);
    }
}
