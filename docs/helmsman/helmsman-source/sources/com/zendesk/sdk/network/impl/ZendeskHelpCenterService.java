package com.zendesk.sdk.network.impl;

import com.zendesk.logger.Logger;
import com.zendesk.sdk.model.helpcenter.Article;
import com.zendesk.sdk.model.helpcenter.ArticleResponse;
import com.zendesk.sdk.model.helpcenter.ArticleVoteResponse;
import com.zendesk.sdk.model.helpcenter.ArticlesListResponse;
import com.zendesk.sdk.model.helpcenter.ArticlesSearchResponse;
import com.zendesk.sdk.model.helpcenter.Attachment;
import com.zendesk.sdk.model.helpcenter.AttachmentResponse;
import com.zendesk.sdk.model.helpcenter.AttachmentType;
import com.zendesk.sdk.model.helpcenter.CategoriesResponse;
import com.zendesk.sdk.model.helpcenter.Category;
import com.zendesk.sdk.model.helpcenter.CategoryResponse;
import com.zendesk.sdk.model.helpcenter.RecordArticleViewRequest;
import com.zendesk.sdk.model.helpcenter.Section;
import com.zendesk.sdk.model.helpcenter.SectionResponse;
import com.zendesk.sdk.model.helpcenter.SectionsResponse;
import com.zendesk.sdk.model.helpcenter.SortBy;
import com.zendesk.sdk.model.helpcenter.SortOrder;
import com.zendesk.sdk.model.helpcenter.SuggestedArticleResponse;
import com.zendesk.sdk.model.helpcenter.User;
import com.zendesk.sdk.model.helpcenter.help.HelpResponse;
import com.zendesk.sdk.network.HelpCenterService;
import com.zendesk.service.ErrorResponseAdapter;
import com.zendesk.service.RetrofitZendeskCallbackAdapter;
import com.zendesk.service.ZendeskCallback;
import com.zendesk.util.CollectionUtils;
import com.zendesk.util.LocaleUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
class ZendeskHelpCenterService {
    private static final String LOG_TAG = "ZendeskHelpCenterService";
    private static final int NUMBER_PER_PAGE = 1000;
    private final HelpCenterService helpCenterService;

    ZendeskHelpCenterService(HelpCenterService helpCenterService) {
        this.helpCenterService = helpCenterService;
    }

    void getHelp(String str, Locale locale, String str2, String str3, String str4, int i, String str5, ZendeskCallback<HelpResponse> zendeskCallback) {
        this.helpCenterService.getHelp(str, LocaleUtil.toLanguageTag(locale), str2, str3, str4, i, str5, 1000, SortBy.CREATED_AT.getApiValue(), SortOrder.DESCENDING.getApiValue()).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback));
    }

    public void getCategories(String str, Locale locale, ZendeskCallback<List<Category>> zendeskCallback) {
        this.helpCenterService.getCategories(str, LocaleUtil.toLanguageTag(locale)).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback, new RetrofitZendeskCallbackAdapter.RequestExtractor<CategoriesResponse, List<Category>>() { // from class: com.zendesk.sdk.network.impl.ZendeskHelpCenterService.1
            @Override // com.zendesk.service.RetrofitZendeskCallbackAdapter.RequestExtractor
            public List<Category> extract(CategoriesResponse categoriesResponse) {
                return categoriesResponse.getCategories();
            }
        }));
    }

    public void getSectionsForCategory(String str, Long l, Locale locale, ZendeskCallback<List<Section>> zendeskCallback) {
        this.helpCenterService.getSections(str, LocaleUtil.toLanguageTag(locale), l, 1000).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback, new RetrofitZendeskCallbackAdapter.RequestExtractor<SectionsResponse, List<Section>>() { // from class: com.zendesk.sdk.network.impl.ZendeskHelpCenterService.2
            @Override // com.zendesk.service.RetrofitZendeskCallbackAdapter.RequestExtractor
            public List<Section> extract(SectionsResponse sectionsResponse) {
                return sectionsResponse.getSections();
            }
        }));
    }

    public void getArticlesForSection(String str, Long l, Locale locale, String str2, ZendeskCallback<List<Article>> zendeskCallback) {
        this.helpCenterService.getArticles(str, LocaleUtil.toLanguageTag(locale), l, str2, 1000).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback, new RetrofitZendeskCallbackAdapter.RequestExtractor<ArticlesListResponse, List<Article>>() { // from class: com.zendesk.sdk.network.impl.ZendeskHelpCenterService.3
            @Override // com.zendesk.service.RetrofitZendeskCallbackAdapter.RequestExtractor
            public List<Article> extract(ArticlesListResponse articlesListResponse) {
                return ZendeskHelpCenterService.this.matchArticlesWithUsers(articlesListResponse.getUsers(), articlesListResponse.getArticles());
            }
        }));
    }

    List<Article> matchArticlesWithUsers(List<User> list, List<Article> list2) {
        HashMap map = new HashMap();
        for (User user : list) {
            map.put(user.getId(), user);
        }
        ArrayList arrayList = new ArrayList();
        for (Article article : list2) {
            User user2 = (User) map.get(article.getAuthorId());
            if (user2 != null) {
                article.setAuthor(user2);
            }
            arrayList.add(article);
        }
        return arrayList;
    }

    public void listArticles(String str, String str2, Locale locale, String str3, String str4, String str5, Integer num, Integer num2, ZendeskCallback<ArticlesListResponse> zendeskCallback) {
        this.helpCenterService.listArticles(str, LocaleUtil.toLanguageTag(locale), str2, str3, str4, str5, num, num2).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback));
    }

    public void searchArticles(String str, String str2, Locale locale, String str3, String str4, String str5, String str6, Integer num, Integer num2, ZendeskCallback<ArticlesSearchResponse> zendeskCallback) {
        this.helpCenterService.searchArticles(str, str2, LocaleUtil.toLanguageTag(locale), str3, str4, str5, str6, num, num2).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback));
    }

    public void getArticle(String str, Long l, Locale locale, String str2, ZendeskCallback<Article> zendeskCallback) {
        this.helpCenterService.getArticle(str, LocaleUtil.toLanguageTag(locale), l, str2).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback, new RetrofitZendeskCallbackAdapter.RequestExtractor<ArticleResponse, Article>() { // from class: com.zendesk.sdk.network.impl.ZendeskHelpCenterService.4
            @Override // com.zendesk.service.RetrofitZendeskCallbackAdapter.RequestExtractor
            public Article extract(ArticleResponse articleResponse) {
                return ZendeskHelpCenterService.this.matchArticleWithUsers(articleResponse.getArticle(), CollectionUtils.ensureEmpty(articleResponse.getUsers()));
            }
        }));
    }

    Article matchArticleWithUsers(Article article, List<User> list) {
        if (article == null) {
            return new Article();
        }
        for (User user : list) {
            if (user.getId() != null && user.getId().equals(article.getAuthorId())) {
                article.setAuthor(user);
                break;
            }
        }
        return article;
    }

    public void getSectionById(String str, Long l, Locale locale, ZendeskCallback<Section> zendeskCallback) {
        this.helpCenterService.getSectionById(str, LocaleUtil.toLanguageTag(locale), l).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback, new RetrofitZendeskCallbackAdapter.RequestExtractor<SectionResponse, Section>() { // from class: com.zendesk.sdk.network.impl.ZendeskHelpCenterService.5
            @Override // com.zendesk.service.RetrofitZendeskCallbackAdapter.RequestExtractor
            public Section extract(SectionResponse sectionResponse) {
                return sectionResponse.getSection();
            }
        }));
    }

    public void getCategoryById(String str, Long l, Locale locale, ZendeskCallback<Category> zendeskCallback) {
        this.helpCenterService.getCategoryById(str, LocaleUtil.toLanguageTag(locale), l).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback, new RetrofitZendeskCallbackAdapter.RequestExtractor<CategoryResponse, Category>() { // from class: com.zendesk.sdk.network.impl.ZendeskHelpCenterService.6
            @Override // com.zendesk.service.RetrofitZendeskCallbackAdapter.RequestExtractor
            public Category extract(CategoryResponse categoryResponse) {
                return categoryResponse.getCategory();
            }
        }));
    }

    public void getAttachments(String str, Locale locale, Long l, AttachmentType attachmentType, ZendeskCallback<List<Attachment>> zendeskCallback) {
        if (attachmentType == null) {
            Logger.e(LOG_TAG, "getAttachments() was called with null attachment type", new Object[0]);
            if (zendeskCallback != null) {
                zendeskCallback.onError(new ErrorResponseAdapter("getAttachments() was called with null attachment type"));
                return;
            }
            return;
        }
        this.helpCenterService.getAttachments(str, LocaleUtil.toLanguageTag(locale), l, attachmentType.getAttachmentType()).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback, new RetrofitZendeskCallbackAdapter.RequestExtractor<AttachmentResponse, List<Attachment>>() { // from class: com.zendesk.sdk.network.impl.ZendeskHelpCenterService.7
            @Override // com.zendesk.service.RetrofitZendeskCallbackAdapter.RequestExtractor
            public List<Attachment> extract(AttachmentResponse attachmentResponse) {
                return attachmentResponse.getArticleAttachments();
            }
        }));
    }

    public void upvoteArticle(String str, Long l, String str2, ZendeskCallback<ArticleVoteResponse> zendeskCallback) {
        if (l == null) {
            Logger.e(LOG_TAG, "The article id was null, can not create up vote", new Object[0]);
            if (zendeskCallback != null) {
                zendeskCallback.onError(new ErrorResponseAdapter("The article id was null, can not create up vote"));
                return;
            }
            return;
        }
        this.helpCenterService.upvoteArticle(str, l, str2).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback));
    }

    public void downvoteArticle(String str, Long l, String str2, ZendeskCallback<ArticleVoteResponse> zendeskCallback) {
        if (l == null) {
            Logger.e(LOG_TAG, "The article id was null, can not create down vote", new Object[0]);
            if (zendeskCallback != null) {
                zendeskCallback.onError(new ErrorResponseAdapter("The article id was null, can not create down vote"));
                return;
            }
            return;
        }
        this.helpCenterService.downvoteArticle(str, l, str2).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback));
    }

    public void deleteVote(String str, Long l, ZendeskCallback<Void> zendeskCallback) {
        if (l == null) {
            Logger.e(LOG_TAG, "The vote id was null, can not delete the vote", new Object[0]);
            if (zendeskCallback != null) {
                zendeskCallback.onError(new ErrorResponseAdapter("The vote id was null, can not delete the vote"));
                return;
            }
            return;
        }
        this.helpCenterService.deleteVote(str, l).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback));
    }

    public void getSuggestedArticles(String str, String str2, Locale locale, String str3, Long l, Long l2, ZendeskCallback<SuggestedArticleResponse> zendeskCallback) {
        this.helpCenterService.getSuggestedArticles(str, str2, LocaleUtil.toLanguageTag(locale), str3, l, l2).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback));
    }

    public void submitRecordArticleView(String str, Long l, Locale locale, RecordArticleViewRequest recordArticleViewRequest, ZendeskCallback<Void> zendeskCallback) {
        this.helpCenterService.submitRecordArticleView(str, l, LocaleUtil.toLanguageTag(locale), recordArticleViewRequest).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback));
    }
}
