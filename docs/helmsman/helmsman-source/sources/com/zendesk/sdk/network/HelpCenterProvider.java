package com.zendesk.sdk.network;

import com.zendesk.sdk.model.helpcenter.Article;
import com.zendesk.sdk.model.helpcenter.ArticleVote;
import com.zendesk.sdk.model.helpcenter.Attachment;
import com.zendesk.sdk.model.helpcenter.AttachmentType;
import com.zendesk.sdk.model.helpcenter.Category;
import com.zendesk.sdk.model.helpcenter.FlatArticle;
import com.zendesk.sdk.model.helpcenter.HelpCenterSearch;
import com.zendesk.sdk.model.helpcenter.ListArticleQuery;
import com.zendesk.sdk.model.helpcenter.SearchArticle;
import com.zendesk.sdk.model.helpcenter.Section;
import com.zendesk.sdk.model.helpcenter.SuggestedArticleResponse;
import com.zendesk.sdk.model.helpcenter.SuggestedArticleSearch;
import com.zendesk.sdk.model.helpcenter.help.HelpItem;
import com.zendesk.sdk.model.helpcenter.help.HelpRequest;
import com.zendesk.service.ZendeskCallback;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
public interface HelpCenterProvider {
    void deleteVote(Long l, ZendeskCallback<Void> zendeskCallback);

    void downvoteArticle(Long l, ZendeskCallback<ArticleVote> zendeskCallback);

    void getArticle(Long l, ZendeskCallback<Article> zendeskCallback);

    void getArticles(Long l, ZendeskCallback<List<Article>> zendeskCallback);

    void getAttachments(Long l, AttachmentType attachmentType, ZendeskCallback<List<Attachment>> zendeskCallback);

    void getCategories(ZendeskCallback<List<Category>> zendeskCallback);

    void getCategory(Long l, ZendeskCallback<Category> zendeskCallback);

    void getHelp(HelpRequest helpRequest, ZendeskCallback<List<HelpItem>> zendeskCallback);

    void getSection(Long l, ZendeskCallback<Section> zendeskCallback);

    void getSections(Long l, ZendeskCallback<List<Section>> zendeskCallback);

    void getSuggestedArticles(SuggestedArticleSearch suggestedArticleSearch, ZendeskCallback<SuggestedArticleResponse> zendeskCallback);

    void listArticles(ListArticleQuery listArticleQuery, ZendeskCallback<List<SearchArticle>> zendeskCallback);

    void listArticlesFlat(ListArticleQuery listArticleQuery, ZendeskCallback<List<FlatArticle>> zendeskCallback);

    void searchArticles(HelpCenterSearch helpCenterSearch, ZendeskCallback<List<SearchArticle>> zendeskCallback);

    void submitRecordArticleView(Long l, Locale locale, ZendeskCallback<Void> zendeskCallback);

    void upvoteArticle(Long l, ZendeskCallback<ArticleVote> zendeskCallback);
}
