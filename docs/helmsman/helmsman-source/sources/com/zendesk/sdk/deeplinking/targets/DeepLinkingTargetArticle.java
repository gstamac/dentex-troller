package com.zendesk.sdk.deeplinking.targets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.TaskStackBuilder;
import com.zendesk.sdk.model.helpcenter.Article;
import com.zendesk.sdk.model.helpcenter.SimpleArticle;
import com.zendesk.sdk.support.ViewArticleActivity;

/* JADX INFO: loaded from: classes2.dex */
public class DeepLinkingTargetArticle extends DeepLinkingTarget<ArticleConfiguration> {
    public static String EXTRA_ARTICLE = "extra_article";
    public static String EXTRA_ARTICLE_SIMPLE = "extra_article_simple";

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.zendesk.sdk.deeplinking.targets.DeepLinkingTarget
    public void openTargetActivity(Context context, ArticleConfiguration articleConfiguration) {
        Intent intent = new Intent(context, (Class<?>) ViewArticleActivity.class);
        if (articleConfiguration.getArticle() != null) {
            intent.putExtra(ViewArticleActivity.EXTRA_ARTICLE, articleConfiguration.getArticle());
        } else if (articleConfiguration.getSimpleArticle() == null) {
            return;
        } else {
            intent.putExtra(ViewArticleActivity.EXTRA_SIMPLE_ARTICLE, articleConfiguration.getSimpleArticle());
        }
        TaskStackBuilder taskStackBuilder = getTaskStackBuilder(context, articleConfiguration.getBackStackActivities());
        taskStackBuilder.addParentStack(ViewArticleActivity.class);
        taskStackBuilder.addNextIntent(intent);
        taskStackBuilder.startActivities();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.zendesk.sdk.deeplinking.targets.DeepLinkingTarget
    public ArticleConfiguration extractConfiguration(Bundle bundle, TargetConfiguration targetConfiguration) {
        if (bundle.containsKey(EXTRA_ARTICLE) && (bundle.getSerializable(EXTRA_ARTICLE) instanceof Article)) {
            return new ArticleConfiguration((Article) bundle.getSerializable(EXTRA_ARTICLE), targetConfiguration.getBackStackActivities(), targetConfiguration.getFallbackActivity());
        }
        if (bundle.containsKey(EXTRA_ARTICLE_SIMPLE) && (bundle.getSerializable(EXTRA_ARTICLE_SIMPLE) instanceof SimpleArticle)) {
            return new ArticleConfiguration((SimpleArticle) bundle.getSerializable(EXTRA_ARTICLE_SIMPLE), targetConfiguration.getBackStackActivities(), targetConfiguration.getFallbackActivity());
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.zendesk.sdk.deeplinking.targets.DeepLinkingTarget
    public Bundle getExtra(ArticleConfiguration articleConfiguration) {
        Bundle bundle = new Bundle();
        if (articleConfiguration.getArticle() != null) {
            bundle.putSerializable(EXTRA_ARTICLE, articleConfiguration.getArticle());
        } else if (articleConfiguration.getSimpleArticle() != null) {
            bundle.putSerializable(EXTRA_ARTICLE_SIMPLE, articleConfiguration.getSimpleArticle());
        }
        return bundle;
    }
}
