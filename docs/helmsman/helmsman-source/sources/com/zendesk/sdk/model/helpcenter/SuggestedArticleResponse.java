package com.zendesk.sdk.model.helpcenter;

import com.zendesk.util.CollectionUtils;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class SuggestedArticleResponse {
    private List<SimpleArticle> results;

    public List<SimpleArticle> getResults() {
        return CollectionUtils.copyOf(this.results);
    }
}
