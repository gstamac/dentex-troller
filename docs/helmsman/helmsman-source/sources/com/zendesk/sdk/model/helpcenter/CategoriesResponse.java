package com.zendesk.sdk.model.helpcenter;

import com.zendesk.util.CollectionUtils;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class CategoriesResponse {
    private List<Category> categories;

    public List<Category> getCategories() {
        return CollectionUtils.copyOf(this.categories);
    }
}
