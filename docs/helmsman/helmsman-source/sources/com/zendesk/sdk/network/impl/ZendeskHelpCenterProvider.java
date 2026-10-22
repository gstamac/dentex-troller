package com.zendesk.sdk.network.impl;

import com.zendesk.logger.Logger;
import com.zendesk.sdk.model.SdkConfiguration;
import com.zendesk.sdk.model.helpcenter.Article;
import com.zendesk.sdk.model.helpcenter.ArticleVote;
import com.zendesk.sdk.model.helpcenter.ArticleVoteResponse;
import com.zendesk.sdk.model.helpcenter.ArticlesListResponse;
import com.zendesk.sdk.model.helpcenter.ArticlesResponse;
import com.zendesk.sdk.model.helpcenter.ArticlesSearchResponse;
import com.zendesk.sdk.model.helpcenter.Attachment;
import com.zendesk.sdk.model.helpcenter.AttachmentType;
import com.zendesk.sdk.model.helpcenter.Category;
import com.zendesk.sdk.model.helpcenter.FlatArticle;
import com.zendesk.sdk.model.helpcenter.HelpCenterSearch;
import com.zendesk.sdk.model.helpcenter.ListArticleQuery;
import com.zendesk.sdk.model.helpcenter.RecordArticleViewRequest;
import com.zendesk.sdk.model.helpcenter.SearchArticle;
import com.zendesk.sdk.model.helpcenter.Section;
import com.zendesk.sdk.model.helpcenter.SortBy;
import com.zendesk.sdk.model.helpcenter.SortOrder;
import com.zendesk.sdk.model.helpcenter.SuggestedArticleResponse;
import com.zendesk.sdk.model.helpcenter.SuggestedArticleSearch;
import com.zendesk.sdk.model.helpcenter.User;
import com.zendesk.sdk.model.helpcenter.help.HelpItem;
import com.zendesk.sdk.model.helpcenter.help.HelpRequest;
import com.zendesk.sdk.model.helpcenter.help.HelpResponse;
import com.zendesk.sdk.model.settings.SafeMobileSettings;
import com.zendesk.sdk.network.BaseProvider;
import com.zendesk.sdk.network.HelpCenterProvider;
import com.zendesk.sdk.storage.HelpCenterSessionCache;
import com.zendesk.service.ErrorResponseAdapter;
import com.zendesk.service.ZendeskCallback;
import com.zendesk.util.CollectionUtils;
import com.zendesk.util.LocaleUtil;
import com.zendesk.util.StringUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
class ZendeskHelpCenterProvider implements HelpCenterProvider {
    private static final String EMPTY_JSON_BODY = "{}";
    private static final String LOG_TAG = "ZendeskHelpCenterProvider";
    private final BaseProvider baseProvider;
    private final ZendeskHelpCenterService helpCenterService;
    private final HelpCenterSessionCache helpCenterSessionCache;

    ZendeskHelpCenterProvider(BaseProvider baseProvider, ZendeskHelpCenterService zendeskHelpCenterService, HelpCenterSessionCache helpCenterSessionCache) {
        this.baseProvider = baseProvider;
        this.helpCenterService = zendeskHelpCenterService;
        this.helpCenterSessionCache = helpCenterSessionCache;
    }

    @Override // com.zendesk.sdk.network.HelpCenterProvider
    public void getHelp(final HelpRequest helpRequest, final ZendeskCallback<List<HelpItem>> zendeskCallback) {
        this.baseProvider.configureSdk(new PassThroughErrorZendeskCallback<SdkConfiguration>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskHelpCenterProvider.1
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(SdkConfiguration sdkConfiguration) {
                if (ZendeskHelpCenterProvider.this.sanityCheckHelpCenterSettings(zendeskCallback, sdkConfiguration.getMobileSettings())) {
                    return;
                }
                ZendeskHelpCenterProvider.this.helpCenterService.getHelp(sdkConfiguration.getBearerAuthorizationHeader(), ZendeskHelpCenterProvider.this.getBestLocale(sdkConfiguration.getMobileSettings()), helpRequest.getCategoryIds(), helpRequest.getSectionIds(), helpRequest.getIncludes(), helpRequest.getArticlesPerPageLimit(), StringUtils.toCsvString(helpRequest.getLabelNames()), new PassThroughErrorZendeskCallback<HelpResponse>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskHelpCenterProvider.1.1
                    @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
                    public void onSuccess(HelpResponse helpResponse) {
                        ZendeskConfig.INSTANCE.getTracker().helpCenterLoaded();
                        if (zendeskCallback != null) {
                            zendeskCallback.onSuccess(new HelpResponseConverter(helpResponse).convert());
                        }
                    }
                });
            }
        });
    }

    @Override // com.zendesk.sdk.network.HelpCenterProvider
    public void getCategories(final ZendeskCallback<List<Category>> zendeskCallback) {
        if (sanityCheck(zendeskCallback, new Object[0])) {
            return;
        }
        this.baseProvider.configureSdk(new PassThroughErrorZendeskCallback<SdkConfiguration>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskHelpCenterProvider.2
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(SdkConfiguration sdkConfiguration) {
                if (ZendeskHelpCenterProvider.this.sanityCheckHelpCenterSettings(zendeskCallback, sdkConfiguration.getMobileSettings())) {
                    return;
                }
                ZendeskHelpCenterProvider.this.helpCenterService.getCategories(sdkConfiguration.getBearerAuthorizationHeader(), ZendeskHelpCenterProvider.this.getBestLocale(sdkConfiguration.getMobileSettings()), zendeskCallback);
            }
        });
    }

    @Override // com.zendesk.sdk.network.HelpCenterProvider
    public void getSections(final Long l, final ZendeskCallback<List<Section>> zendeskCallback) {
        if (sanityCheck(zendeskCallback, l)) {
            return;
        }
        this.baseProvider.configureSdk(new PassThroughErrorZendeskCallback<SdkConfiguration>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskHelpCenterProvider.3
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(SdkConfiguration sdkConfiguration) {
                if (ZendeskHelpCenterProvider.this.sanityCheckHelpCenterSettings(zendeskCallback, sdkConfiguration.getMobileSettings())) {
                    return;
                }
                ZendeskHelpCenterProvider.this.helpCenterService.getSectionsForCategory(sdkConfiguration.getBearerAuthorizationHeader(), l, ZendeskHelpCenterProvider.this.getBestLocale(sdkConfiguration.getMobileSettings()), zendeskCallback);
            }
        });
    }

    @Override // com.zendesk.sdk.network.HelpCenterProvider
    public void getArticles(final Long l, final ZendeskCallback<List<Article>> zendeskCallback) {
        if (sanityCheck(zendeskCallback, l)) {
            return;
        }
        this.baseProvider.configureSdk(new PassThroughErrorZendeskCallback<SdkConfiguration>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskHelpCenterProvider.4
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(SdkConfiguration sdkConfiguration) {
                if (ZendeskHelpCenterProvider.this.sanityCheckHelpCenterSettings(zendeskCallback, sdkConfiguration.getMobileSettings())) {
                    return;
                }
                ZendeskHelpCenterProvider.this.helpCenterService.getArticlesForSection(sdkConfiguration.getBearerAuthorizationHeader(), l, ZendeskHelpCenterProvider.this.getBestLocale(sdkConfiguration.getMobileSettings()), "users", zendeskCallback);
            }
        });
    }

    @Override // com.zendesk.sdk.network.HelpCenterProvider
    public void listArticles(final ListArticleQuery listArticleQuery, final ZendeskCallback<List<SearchArticle>> zendeskCallback) {
        if (sanityCheck(zendeskCallback, listArticleQuery)) {
            return;
        }
        this.baseProvider.configureSdk(new PassThroughErrorZendeskCallback<SdkConfiguration>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskHelpCenterProvider.5
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(SdkConfiguration sdkConfiguration) {
                String include;
                if (ZendeskHelpCenterProvider.this.sanityCheckHelpCenterSettings(zendeskCallback, sdkConfiguration.getMobileSettings())) {
                    return;
                }
                if (listArticleQuery.getInclude() == null) {
                    include = StringUtils.toCsvString("categories", "sections", "users");
                } else {
                    include = listArticleQuery.getInclude();
                }
                String str = include;
                ZendeskHelpCenterProvider.this.helpCenterService.listArticles(sdkConfiguration.getBearerAuthorizationHeader(), StringUtils.toCsvString(listArticleQuery.getLabelNames()), listArticleQuery.getLocale() == null ? ZendeskHelpCenterProvider.this.getBestLocale(sdkConfiguration.getMobileSettings()) : listArticleQuery.getLocale(), str, (listArticleQuery.getSortBy() == null ? SortBy.CREATED_AT : listArticleQuery.getSortBy()).getApiValue(), (listArticleQuery.getSortOrder() == null ? SortOrder.DESCENDING : listArticleQuery.getSortOrder()).getApiValue(), listArticleQuery.getPage(), listArticleQuery.getResultsPerPage(), new PassThroughErrorZendeskCallback<ArticlesListResponse>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskHelpCenterProvider.5.1
                    @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
                    public void onSuccess(ArticlesListResponse articlesListResponse) {
                        List<SearchArticle> listAsSearchArticleList = ZendeskHelpCenterProvider.this.asSearchArticleList(articlesListResponse);
                        if (zendeskCallback != null) {
                            zendeskCallback.onSuccess(listAsSearchArticleList);
                        }
                    }
                });
            }
        });
    }

    @Override // com.zendesk.sdk.network.HelpCenterProvider
    public void listArticlesFlat(final ListArticleQuery listArticleQuery, final ZendeskCallback<List<FlatArticle>> zendeskCallback) {
        if (sanityCheck(zendeskCallback, listArticleQuery)) {
            return;
        }
        this.baseProvider.configureSdk(new PassThroughErrorZendeskCallback<SdkConfiguration>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskHelpCenterProvider.6
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(SdkConfiguration sdkConfiguration) {
                if (ZendeskHelpCenterProvider.this.sanityCheckHelpCenterSettings(zendeskCallback, sdkConfiguration.getMobileSettings())) {
                    return;
                }
                ZendeskHelpCenterProvider.this.helpCenterService.listArticles(sdkConfiguration.getBearerAuthorizationHeader(), StringUtils.toCsvString(listArticleQuery.getLabelNames()), listArticleQuery.getLocale() == null ? ZendeskHelpCenterProvider.this.getBestLocale(sdkConfiguration.getMobileSettings()) : listArticleQuery.getLocale(), "categories,sections", (listArticleQuery.getSortBy() == null ? SortBy.CREATED_AT : listArticleQuery.getSortBy()).getApiValue(), (listArticleQuery.getSortOrder() == null ? SortOrder.DESCENDING : listArticleQuery.getSortOrder()).getApiValue(), listArticleQuery.getPage(), listArticleQuery.getResultsPerPage(), new PassThroughErrorZendeskCallback<ArticlesListResponse>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskHelpCenterProvider.6.1
                    @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
                    public void onSuccess(ArticlesListResponse articlesListResponse) {
                        List<FlatArticle> listAsFlatArticleList = ZendeskHelpCenterProvider.this.asFlatArticleList(articlesListResponse);
                        if (zendeskCallback != null) {
                            zendeskCallback.onSuccess(listAsFlatArticleList);
                        }
                    }
                });
            }
        });
    }

    @Override // com.zendesk.sdk.network.HelpCenterProvider
    public void searchArticles(final HelpCenterSearch helpCenterSearch, final ZendeskCallback<List<SearchArticle>> zendeskCallback) {
        if (sanityCheck(zendeskCallback, helpCenterSearch)) {
            return;
        }
        this.baseProvider.configureSdk(new PassThroughErrorZendeskCallback<SdkConfiguration>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskHelpCenterProvider.7
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(SdkConfiguration sdkConfiguration) {
                String csvString;
                Locale locale;
                if (ZendeskHelpCenterProvider.this.sanityCheckHelpCenterSettings(zendeskCallback, sdkConfiguration.getMobileSettings())) {
                    return;
                }
                if (StringUtils.isEmpty(helpCenterSearch.getInclude())) {
                    csvString = StringUtils.toCsvString("categories", "sections", "users");
                } else {
                    csvString = StringUtils.toCsvString(helpCenterSearch.getInclude());
                }
                String str = csvString;
                String csvString2 = StringUtils.isEmpty(helpCenterSearch.getLabelNames()) ? null : StringUtils.toCsvString(helpCenterSearch.getLabelNames());
                if (helpCenterSearch.getLocale() == null) {
                    locale = ZendeskHelpCenterProvider.this.getBestLocale(sdkConfiguration.getMobileSettings());
                } else {
                    locale = helpCenterSearch.getLocale();
                }
                ZendeskHelpCenterProvider.this.helpCenterService.searchArticles(sdkConfiguration.getBearerAuthorizationHeader(), helpCenterSearch.getQuery(), locale, str, csvString2, helpCenterSearch.getCategoryIds(), helpCenterSearch.getSectionIds(), helpCenterSearch.getPage(), helpCenterSearch.getPerPage(), new PassThroughErrorZendeskCallback<ArticlesSearchResponse>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskHelpCenterProvider.7.1
                    @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
                    public void onSuccess(ArticlesSearchResponse articlesSearchResponse) {
                        ZendeskConfig.INSTANCE.getTracker().helpCenterSearched(helpCenterSearch.getQuery());
                        ZendeskHelpCenterProvider.this.helpCenterSessionCache.setLastSearch(helpCenterSearch.getQuery(), (articlesSearchResponse == null || !CollectionUtils.isNotEmpty(articlesSearchResponse.getArticles())) ? 0 : articlesSearchResponse.getArticles().size());
                        List<SearchArticle> listAsSearchArticleList = ZendeskHelpCenterProvider.this.asSearchArticleList(articlesSearchResponse);
                        if (zendeskCallback != null) {
                            zendeskCallback.onSuccess(listAsSearchArticleList);
                        }
                    }
                });
            }
        });
    }

    @Override // com.zendesk.sdk.network.HelpCenterProvider
    public void getArticle(final Long l, final ZendeskCallback<Article> zendeskCallback) {
        if (sanityCheck(zendeskCallback, l)) {
            return;
        }
        this.baseProvider.configureSdk(new PassThroughErrorZendeskCallback<SdkConfiguration>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskHelpCenterProvider.8
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(SdkConfiguration sdkConfiguration) {
                if (ZendeskHelpCenterProvider.this.sanityCheckHelpCenterSettings(zendeskCallback, sdkConfiguration.getMobileSettings())) {
                    return;
                }
                ZendeskHelpCenterProvider.this.helpCenterService.getArticle(sdkConfiguration.getBearerAuthorizationHeader(), l, ZendeskHelpCenterProvider.this.getBestLocale(sdkConfiguration.getMobileSettings()), "users", zendeskCallback);
            }
        });
    }

    @Override // com.zendesk.sdk.network.HelpCenterProvider
    public void getSection(final Long l, final ZendeskCallback<Section> zendeskCallback) {
        if (sanityCheck(zendeskCallback, l)) {
            return;
        }
        this.baseProvider.configureSdk(new PassThroughErrorZendeskCallback<SdkConfiguration>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskHelpCenterProvider.9
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(SdkConfiguration sdkConfiguration) {
                if (ZendeskHelpCenterProvider.this.sanityCheckHelpCenterSettings(zendeskCallback, sdkConfiguration.getMobileSettings())) {
                    return;
                }
                ZendeskHelpCenterProvider.this.helpCenterService.getSectionById(sdkConfiguration.getBearerAuthorizationHeader(), l, ZendeskHelpCenterProvider.this.getBestLocale(sdkConfiguration.getMobileSettings()), zendeskCallback);
            }
        });
    }

    @Override // com.zendesk.sdk.network.HelpCenterProvider
    public void getCategory(final Long l, final ZendeskCallback<Category> zendeskCallback) {
        if (sanityCheck(zendeskCallback, l)) {
            return;
        }
        this.baseProvider.configureSdk(new PassThroughErrorZendeskCallback<SdkConfiguration>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskHelpCenterProvider.10
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(SdkConfiguration sdkConfiguration) {
                if (ZendeskHelpCenterProvider.this.sanityCheckHelpCenterSettings(zendeskCallback, sdkConfiguration.getMobileSettings())) {
                    return;
                }
                ZendeskHelpCenterProvider.this.helpCenterService.getCategoryById(sdkConfiguration.getBearerAuthorizationHeader(), l, ZendeskHelpCenterProvider.this.getBestLocale(sdkConfiguration.getMobileSettings()), zendeskCallback);
            }
        });
    }

    @Override // com.zendesk.sdk.network.HelpCenterProvider
    public void getAttachments(final Long l, final AttachmentType attachmentType, final ZendeskCallback<List<Attachment>> zendeskCallback) {
        if (sanityCheck(zendeskCallback, l, attachmentType)) {
            return;
        }
        this.baseProvider.configureSdk(new PassThroughErrorZendeskCallback<SdkConfiguration>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskHelpCenterProvider.11
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(SdkConfiguration sdkConfiguration) {
                if (ZendeskHelpCenterProvider.this.sanityCheckHelpCenterSettings(zendeskCallback, sdkConfiguration.getMobileSettings())) {
                    return;
                }
                ZendeskHelpCenterProvider.this.helpCenterService.getAttachments(sdkConfiguration.getBearerAuthorizationHeader(), ZendeskHelpCenterProvider.this.getBestLocale(sdkConfiguration.getMobileSettings()), l, attachmentType, zendeskCallback);
            }
        });
    }

    @Override // com.zendesk.sdk.network.HelpCenterProvider
    public void upvoteArticle(final Long l, final ZendeskCallback<ArticleVote> zendeskCallback) {
        if (sanityCheck(zendeskCallback, l)) {
            return;
        }
        this.baseProvider.configureSdk(new PassThroughErrorZendeskCallback<SdkConfiguration>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskHelpCenterProvider.12
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(SdkConfiguration sdkConfiguration) {
                if (ZendeskHelpCenterProvider.this.sanityCheckHelpCenterSettings(zendeskCallback, sdkConfiguration.getMobileSettings())) {
                    return;
                }
                ZendeskHelpCenterProvider.this.helpCenterService.upvoteArticle(sdkConfiguration.getBearerAuthorizationHeader(), l, ZendeskHelpCenterProvider.EMPTY_JSON_BODY, new PassThroughErrorZendeskCallback<ArticleVoteResponse>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskHelpCenterProvider.12.1
                    @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
                    public void onSuccess(ArticleVoteResponse articleVoteResponse) {
                        if (zendeskCallback != null) {
                            zendeskCallback.onSuccess(articleVoteResponse.getVote());
                        }
                    }
                });
            }
        });
    }

    @Override // com.zendesk.sdk.network.HelpCenterProvider
    public void downvoteArticle(final Long l, final ZendeskCallback<ArticleVote> zendeskCallback) {
        if (sanityCheck(zendeskCallback, l)) {
            return;
        }
        this.baseProvider.configureSdk(new PassThroughErrorZendeskCallback<SdkConfiguration>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskHelpCenterProvider.13
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(SdkConfiguration sdkConfiguration) {
                if (ZendeskHelpCenterProvider.this.sanityCheckHelpCenterSettings(zendeskCallback, sdkConfiguration.getMobileSettings())) {
                    return;
                }
                ZendeskHelpCenterProvider.this.helpCenterService.downvoteArticle(sdkConfiguration.getBearerAuthorizationHeader(), l, ZendeskHelpCenterProvider.EMPTY_JSON_BODY, new PassThroughErrorZendeskCallback<ArticleVoteResponse>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskHelpCenterProvider.13.1
                    @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
                    public void onSuccess(ArticleVoteResponse articleVoteResponse) {
                        if (zendeskCallback != null) {
                            zendeskCallback.onSuccess(articleVoteResponse.getVote());
                        }
                    }
                });
            }
        });
    }

    @Override // com.zendesk.sdk.network.HelpCenterProvider
    public void deleteVote(final Long l, final ZendeskCallback<Void> zendeskCallback) {
        if (sanityCheck(zendeskCallback, l)) {
            return;
        }
        this.baseProvider.configureSdk(new PassThroughErrorZendeskCallback<SdkConfiguration>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskHelpCenterProvider.14
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(SdkConfiguration sdkConfiguration) {
                if (ZendeskHelpCenterProvider.this.sanityCheckHelpCenterSettings(zendeskCallback, sdkConfiguration.getMobileSettings())) {
                    return;
                }
                ZendeskHelpCenterProvider.this.helpCenterService.deleteVote(sdkConfiguration.getBearerAuthorizationHeader(), l, new PassThroughErrorZendeskCallback<Void>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskHelpCenterProvider.14.1
                    @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
                    public void onSuccess(Void r2) {
                        if (zendeskCallback != null) {
                            zendeskCallback.onSuccess(r2);
                        }
                    }
                });
            }
        });
    }

    @Override // com.zendesk.sdk.network.HelpCenterProvider
    public void getSuggestedArticles(final SuggestedArticleSearch suggestedArticleSearch, final ZendeskCallback<SuggestedArticleResponse> zendeskCallback) {
        if (sanityCheck(zendeskCallback, suggestedArticleSearch)) {
            return;
        }
        this.baseProvider.configureSdk(new PassThroughErrorZendeskCallback<SdkConfiguration>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskHelpCenterProvider.15
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(SdkConfiguration sdkConfiguration) {
                if (ZendeskHelpCenterProvider.this.sanityCheckHelpCenterSettings(zendeskCallback, sdkConfiguration.getMobileSettings())) {
                    return;
                }
                ZendeskHelpCenterProvider.this.helpCenterService.getSuggestedArticles(sdkConfiguration.getBearerAuthorizationHeader(), suggestedArticleSearch.getQuery(), suggestedArticleSearch.getLocale() == null ? ZendeskHelpCenterProvider.this.getBestLocale(sdkConfiguration.getMobileSettings()) : suggestedArticleSearch.getLocale(), StringUtils.isEmpty(suggestedArticleSearch.getLabelNames()) ? null : StringUtils.toCsvString(suggestedArticleSearch.getLabelNames()), suggestedArticleSearch.getCategoryId(), suggestedArticleSearch.getSectionId(), zendeskCallback);
            }
        });
    }

    @Override // com.zendesk.sdk.network.HelpCenterProvider
    public void submitRecordArticleView(final Long l, final Locale locale, final ZendeskCallback<Void> zendeskCallback) {
        this.baseProvider.configureSdk(new PassThroughErrorZendeskCallback<SdkConfiguration>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskHelpCenterProvider.16
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(SdkConfiguration sdkConfiguration) {
                if (ZendeskHelpCenterProvider.this.sanityCheckHelpCenterSettings(zendeskCallback, sdkConfiguration.getMobileSettings())) {
                    return;
                }
                ZendeskHelpCenterProvider.this.helpCenterService.submitRecordArticleView(sdkConfiguration.getBearerAuthorizationHeader(), l, locale, new RecordArticleViewRequest(ZendeskHelpCenterProvider.this.helpCenterSessionCache.getLastSearch(), ZendeskHelpCenterProvider.this.helpCenterSessionCache.isUniqueSearchResultClick()), new PassThroughErrorZendeskCallback<Void>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskHelpCenterProvider.16.1
                    @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
                    public void onSuccess(Void r2) {
                        ZendeskHelpCenterProvider.this.helpCenterSessionCache.unsetUniqueSearchResultClick();
                        if (zendeskCallback != null) {
                            zendeskCallback.onSuccess(r2);
                        }
                    }
                });
            }
        });
    }

    Locale getBestLocale(SafeMobileSettings safeMobileSettings) {
        String helpCenterLocale = safeMobileSettings != null ? safeMobileSettings.getHelpCenterLocale() : "";
        return StringUtils.isEmpty(helpCenterLocale) ? Locale.getDefault() : LocaleUtil.forLanguageTag(helpCenterLocale);
    }

    boolean sanityCheck(ZendeskCallback<?> zendeskCallback, Object... objArr) {
        if (objArr != null) {
            boolean z = true;
            for (Object obj : objArr) {
                if (obj == null) {
                    z = false;
                }
            }
            if (!z) {
                Logger.e(LOG_TAG, "One or more provided parameters are null.", new Object[0]);
                if (zendeskCallback != null) {
                    zendeskCallback.onError(new ErrorResponseAdapter("One or more provided parameters are null."));
                }
                return true;
            }
        }
        return false;
    }

    boolean sanityCheckHelpCenterSettings(ZendeskCallback<?> zendeskCallback, SafeMobileSettings safeMobileSettings) {
        if (!safeMobileSettings.hasHelpCenterSettings()) {
            Logger.e(LOG_TAG, "Help Center settings are null. Can not continue with the call", new Object[0]);
            if (zendeskCallback != null) {
                zendeskCallback.onError(new ErrorResponseAdapter("Help Center settings are null. Can not continue with the call"));
            }
            return true;
        }
        if (safeMobileSettings.isHelpCenterEnabled()) {
            return false;
        }
        Logger.e(LOG_TAG, "Help Center is disabled in your app's settings. Can not continue with the call", new Object[0]);
        if (zendeskCallback != null) {
            zendeskCallback.onError(new ErrorResponseAdapter("Help Center is disabled in your app's settings. Can not continue with the call"));
        }
        return true;
    }

    List<SearchArticle> asSearchArticleList(ArticlesResponse articlesResponse) {
        Section section;
        ArrayList arrayList = new ArrayList();
        if (articlesResponse == null) {
            return arrayList;
        }
        HashMap map = new HashMap();
        HashMap map2 = new HashMap();
        HashMap map3 = new HashMap();
        List<Article> listEnsureEmpty = CollectionUtils.ensureEmpty(articlesResponse.getArticles());
        List<Section> listEnsureEmpty2 = CollectionUtils.ensureEmpty(articlesResponse.getSections());
        List<Category> listEnsureEmpty3 = CollectionUtils.ensureEmpty(articlesResponse.getCategories());
        List<User> listEnsureEmpty4 = CollectionUtils.ensureEmpty(articlesResponse.getUsers());
        for (Section section2 : listEnsureEmpty2) {
            if (section2.getId() != null) {
                map.put(section2.getId(), section2);
            }
        }
        for (Category category : listEnsureEmpty3) {
            if (category.getId() != null) {
                map2.put(category.getId(), category);
            }
        }
        for (User user : listEnsureEmpty4) {
            if (user.getId() != null) {
                map3.put(user.getId(), user);
            }
        }
        for (Article article : listEnsureEmpty) {
            Category category2 = null;
            if (article.getSectionId() != null) {
                section = (Section) map.get(article.getSectionId());
            } else {
                Logger.w(LOG_TAG, "Unable to determine section as section id was null.", new Object[0]);
                section = null;
            }
            if (section != null && section.getCategoryId() != null) {
                category2 = (Category) map2.get(section.getCategoryId());
            } else {
                Logger.w(LOG_TAG, "Unable to determine category as section was null.", new Object[0]);
            }
            if (article.getAuthorId() != null) {
                article.setAuthor((User) map3.get(article.getAuthorId()));
            } else {
                Logger.w(LOG_TAG, "Unable to determine author as author id was null.", new Object[0]);
            }
            arrayList.add(new SearchArticle(article, section, category2));
        }
        return arrayList;
    }

    List<FlatArticle> asFlatArticleList(ArticlesResponse articlesResponse) {
        if (articlesResponse == null) {
            return new ArrayList();
        }
        List<Category> categories = articlesResponse.getCategories();
        List<Section> sections = articlesResponse.getSections();
        List<Article> articles = articlesResponse.getArticles();
        HashMap map = new HashMap();
        HashMap map2 = new HashMap();
        List<FlatArticle> arrayList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(articles)) {
            for (Category category : categories) {
                map.put(category.getId(), category);
            }
            for (Section section : sections) {
                map2.put(section.getId(), section);
            }
            for (Article article : articles) {
                Section section2 = (Section) map2.get(article.getSectionId());
                arrayList.add(new FlatArticle((Category) map.get(section2.getCategoryId()), section2, article));
            }
        } else {
            Logger.d(LOG_TAG, "There are no articles contained in this account", new Object[0]);
            arrayList = Collections.emptyList();
        }
        Collections.sort(arrayList);
        return arrayList;
    }
}
