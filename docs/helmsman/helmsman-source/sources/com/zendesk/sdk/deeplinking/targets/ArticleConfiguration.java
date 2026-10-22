package com.zendesk.sdk.deeplinking.targets;

import android.content.Intent;
import com.zendesk.sdk.model.helpcenter.Article;
import com.zendesk.sdk.model.helpcenter.SimpleArticle;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes2.dex */
public class ArticleConfiguration extends TargetConfiguration {
    private Article mArticle;
    private SimpleArticle mSimpleArticle;

    @Override // com.zendesk.sdk.deeplinking.targets.TargetConfiguration
    public /* bridge */ /* synthetic */ ArrayList getBackStackActivities() {
        return super.getBackStackActivities();
    }

    @Override // com.zendesk.sdk.deeplinking.targets.TargetConfiguration
    public /* bridge */ /* synthetic */ DeepLinkType getDeepLinkType() {
        return super.getDeepLinkType();
    }

    @Override // com.zendesk.sdk.deeplinking.targets.TargetConfiguration
    public /* bridge */ /* synthetic */ Intent getFallbackActivity() {
        return super.getFallbackActivity();
    }

    public ArticleConfiguration(Article article, ArrayList<Intent> arrayList, Intent intent) {
        super(DeepLinkType.Article, arrayList, intent);
        this.mArticle = article;
    }

    public ArticleConfiguration(SimpleArticle simpleArticle, ArrayList<Intent> arrayList, Intent intent) {
        super(DeepLinkType.Article, arrayList, intent);
        this.mSimpleArticle = simpleArticle;
    }

    public Article getArticle() {
        return this.mArticle;
    }

    public SimpleArticle getSimpleArticle() {
        return this.mSimpleArticle;
    }
}
