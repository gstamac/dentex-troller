package com.zendesk.sdk.support;

import android.os.Bundle;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.R;
import com.zendesk.sdk.model.helpcenter.SearchArticle;
import com.zendesk.sdk.model.settings.SafeMobileSettings;
import com.zendesk.sdk.network.NetworkAware;
import com.zendesk.sdk.network.NetworkInfoProvider;
import com.zendesk.sdk.network.RetryAction;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zendesk.service.ErrorResponse;
import com.zendesk.service.ZendeskCallback;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* JADX INFO: loaded from: classes2.dex */
class SupportPresenter implements SupportMvp.Presenter, NetworkAware {
    private static final String LOG_TAG = "SupportPresenter";
    private static final Integer NETWORK_AWARE_ID = 31;
    private static final Integer RETRY_ACTION_ID = 8642;
    private SupportUiConfig config;
    private Set<RetryAction> internalRetryActions = new HashSet();
    private SafeMobileSettings mobileSettings = ZendeskConfig.INSTANCE.getMobileSettings();
    private SupportMvp.Model model;
    private NetworkInfoProvider networkInfoProvider;
    private boolean networkPreviouslyUnavailable;
    private SupportMvp.View view;

    SupportPresenter(SupportMvp.View view, SupportMvp.Model model, NetworkInfoProvider networkInfoProvider) {
        this.view = view;
        this.model = model;
        this.networkInfoProvider = networkInfoProvider;
    }

    private void extractDataFromBundle(Bundle bundle) {
        this.config = builderWithHelpCenterIdsFromBundle(bundle).withLabelNames(bundle.getStringArray("extra_label_names")).withShowContactUsButton(bundle.getBoolean("extra_show_contact_us_button", true)).withAddListPaddingBottom(bundle.getBoolean("extra_show_contact_us_button", true)).withCollapseCategories(bundle.getBoolean("extra_categories_collapsed", false)).withShowConversationsMenuButton(bundle.getBoolean("extra_show_conversations_menu_button", true)).build();
    }

    private SupportUiConfig.Builder builderWithHelpCenterIdsFromBundle(Bundle bundle) {
        boolean z;
        SupportUiConfig.Builder builder = new SupportUiConfig.Builder();
        boolean z2 = true;
        if (bundle.getLongArray("extra_category_ids") != null) {
            builder = builder.withCategoryIds(convertToLongList(bundle.getLongArray("extra_category_ids")));
            z = true;
        } else {
            z = false;
        }
        if (bundle.getLongArray("extra_section_ids") != null) {
            builder = builder.withSectionIds(convertToLongList(bundle.getLongArray("extra_section_ids")));
        } else {
            z2 = z;
        }
        if (!z2) {
            logNoCategoriesOrSectionsSet();
        }
        return builder;
    }

    private void logNoCategoriesOrSectionsSet() {
        Logger.d(LOG_TAG, "No category or section IDs have been set.", new Object[0]);
    }

    @Override // com.zendesk.sdk.support.SupportMvp.Presenter
    public void onResume(SupportMvp.View view) {
        this.view = view;
        this.networkInfoProvider.addNetworkAwareListener(NETWORK_AWARE_ID, this);
        this.networkInfoProvider.register();
        if (!this.networkInfoProvider.isNetworkAvailable()) {
            view.showError(R.string.network_activity_no_connectivity);
            view.hideLoadingState();
            this.networkPreviouslyUnavailable = true;
        }
        invokeRetryActions();
    }

    private void invokeRetryActions() {
        Iterator<RetryAction> it = this.internalRetryActions.iterator();
        while (it.hasNext()) {
            it.next().onRetry();
        }
        this.internalRetryActions.clear();
    }

    @Override // com.zendesk.sdk.support.SupportMvp.Presenter
    public void onPause() {
        this.view = null;
        this.networkInfoProvider.removeNetworkAwareListener(NETWORK_AWARE_ID);
        this.networkInfoProvider.removeRetryAction(RETRY_ACTION_ID);
        this.networkInfoProvider.unregister();
    }

    private List<Long> convertToLongList(long[] jArr) {
        if (jArr == null) {
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList(jArr.length);
        for (long j : jArr) {
            arrayList.add(Long.valueOf(j));
        }
        return arrayList;
    }

    @Override // com.zendesk.sdk.support.SupportMvp.Presenter
    public void onSearchSubmit(final String str) {
        if (this.networkInfoProvider.isNetworkAvailable()) {
            this.view.dismissError();
            this.view.showLoadingState();
            this.view.clearSearchResults();
            this.model.search(this.config.getCategoryIds(), this.config.getSectionIds(), str, this.config.getLabelNames(), new ViewSafeRetryZendeskCallback(str));
            return;
        }
        this.networkInfoProvider.addRetryAction(RETRY_ACTION_ID, new RetryAction() { // from class: com.zendesk.sdk.support.SupportPresenter.1
            @Override // com.zendesk.sdk.network.RetryAction
            public void onRetry() {
                SupportPresenter.this.onSearchSubmit(str);
            }
        });
    }

    class ViewSafeRetryZendeskCallback extends ZendeskCallback<List<SearchArticle>> {
        private String query;

        ViewSafeRetryZendeskCallback(String str) {
            this.query = str;
        }

        @Override // com.zendesk.service.ZendeskCallback
        public void onSuccess(final List<SearchArticle> list) {
            if (SupportPresenter.this.view != null) {
                SupportPresenter.this.view.hideLoadingState();
                SupportPresenter.this.view.showSearchResults(list, this.query);
                if (SupportPresenter.this.config.isShowContactUsButton()) {
                    SupportPresenter.this.view.showContactUsButton();
                    return;
                }
                return;
            }
            SupportPresenter.this.internalRetryActions.add(new RetryAction() { // from class: com.zendesk.sdk.support.SupportPresenter.ViewSafeRetryZendeskCallback.1
                @Override // com.zendesk.sdk.network.RetryAction
                public void onRetry() {
                    ViewSafeRetryZendeskCallback.this.onSuccess(list);
                }
            });
        }

        @Override // com.zendesk.service.ZendeskCallback
        public void onError(final ErrorResponse errorResponse) {
            if (SupportPresenter.this.view != null) {
                SupportPresenter.this.view.hideLoadingState();
                SupportPresenter.this.view.showErrorWithRetry(SupportMvp.ErrorType.ARTICLES_LOAD, new RetryAction() { // from class: com.zendesk.sdk.support.SupportPresenter.ViewSafeRetryZendeskCallback.2
                    @Override // com.zendesk.sdk.network.RetryAction
                    public void onRetry() {
                        SupportPresenter.this.onSearchSubmit(ViewSafeRetryZendeskCallback.this.query);
                    }
                });
            } else {
                SupportPresenter.this.internalRetryActions.add(new RetryAction() { // from class: com.zendesk.sdk.support.SupportPresenter.ViewSafeRetryZendeskCallback.3
                    @Override // com.zendesk.sdk.network.RetryAction
                    public void onRetry() {
                        ViewSafeRetryZendeskCallback.this.onError(errorResponse);
                    }
                });
            }
        }
    }

    @Override // com.zendesk.sdk.support.SupportMvp.Presenter
    public void onLoad() {
        if (this.config.isShowContactUsButton()) {
            SupportMvp.View view = this.view;
            if (view != null) {
                view.showContactUsButton();
            } else {
                this.internalRetryActions.add(new RetryAction() { // from class: com.zendesk.sdk.support.SupportPresenter.2
                    @Override // com.zendesk.sdk.network.RetryAction
                    public void onRetry() {
                        SupportPresenter.this.view.showContactUsButton();
                    }
                });
            }
        }
    }

    @Override // com.zendesk.sdk.support.SupportMvp.Presenter
    public void onErrorWithRetry(final SupportMvp.ErrorType errorType, final RetryAction retryAction) {
        SupportMvp.View view = this.view;
        if (view != null) {
            if (view.isShowingHelp()) {
                this.view.hideLoadingState();
                this.view.showErrorWithRetry(errorType, retryAction);
                return;
            }
            return;
        }
        this.internalRetryActions.add(new RetryAction() { // from class: com.zendesk.sdk.support.SupportPresenter.3
            @Override // com.zendesk.sdk.network.RetryAction
            public void onRetry() {
                if (SupportPresenter.this.view == null || !SupportPresenter.this.view.isShowingHelp()) {
                    return;
                }
                SupportPresenter.this.view.hideLoadingState();
                SupportPresenter.this.view.showErrorWithRetry(errorType, retryAction);
            }
        });
    }

    @Override // com.zendesk.sdk.network.NetworkAware
    public void onNetworkAvailable() {
        Logger.d(LOG_TAG, "Network is available.", new Object[0]);
        if (!this.networkPreviouslyUnavailable) {
            Logger.d(LOG_TAG, "Network was not previously unavailable, no need to dismiss Snackbar", new Object[0]);
            return;
        }
        this.networkPreviouslyUnavailable = false;
        SupportMvp.View view = this.view;
        if (view != null) {
            view.dismissError();
        } else {
            this.internalRetryActions.add(new RetryAction() { // from class: com.zendesk.sdk.support.SupportPresenter.4
                @Override // com.zendesk.sdk.network.RetryAction
                public void onRetry() {
                    SupportPresenter.this.view.dismissError();
                }
            });
        }
    }

    @Override // com.zendesk.sdk.network.NetworkAware
    public void onNetworkUnavailable() {
        Logger.d(LOG_TAG, "Network is unavailable.", new Object[0]);
        this.networkPreviouslyUnavailable = true;
        SupportMvp.View view = this.view;
        if (view != null) {
            view.showError(R.string.network_activity_no_connectivity);
            this.view.hideLoadingState();
        }
    }

    @Override // com.zendesk.sdk.support.SupportMvp.Presenter
    public void initWithBundle(final Bundle bundle) {
        if (bundle != null) {
            extractDataFromBundle(bundle);
        } else {
            this.config = new SupportUiConfig.Builder().build();
            logNoCategoriesOrSectionsSet();
        }
        this.view.showLoadingState();
        this.model.getSettings(new ZendeskCallback<SafeMobileSettings>() { // from class: com.zendesk.sdk.support.SupportPresenter.5
            @Override // com.zendesk.service.ZendeskCallback
            public void onSuccess(SafeMobileSettings safeMobileSettings) {
                if (SupportPresenter.this.view != null) {
                    SupportPresenter.this.view.hideLoadingState();
                } else {
                    SupportPresenter.this.internalRetryActions.add(new RetryAction() { // from class: com.zendesk.sdk.support.SupportPresenter.5.1
                        @Override // com.zendesk.sdk.network.RetryAction
                        public void onRetry() {
                            SupportPresenter.this.view.hideLoadingState();
                        }
                    });
                }
                SupportPresenter.this.mobileSettings = safeMobileSettings;
                if (safeMobileSettings.isHelpCenterEnabled()) {
                    Logger.d(SupportPresenter.LOG_TAG, "Help center is enabled. starting with Help Center", new Object[0]);
                    if (SupportPresenter.this.view != null) {
                        SupportPresenter.this.view.showHelp(SupportPresenter.this.config);
                    } else {
                        SupportPresenter.this.internalRetryActions.add(new RetryAction() { // from class: com.zendesk.sdk.support.SupportPresenter.5.2
                            @Override // com.zendesk.sdk.network.RetryAction
                            public void onRetry() {
                                SupportPresenter.this.view.showHelp(SupportPresenter.this.config);
                            }
                        });
                    }
                    Bundle bundle2 = bundle;
                    if (bundle2 == null || !bundle2.getBoolean("fab_visibility")) {
                        return;
                    }
                    Logger.d(SupportPresenter.LOG_TAG, "Saved instance states that we should show the contact FAB", new Object[0]);
                    if (SupportPresenter.this.view != null) {
                        SupportPresenter.this.view.showContactUsButton();
                        return;
                    } else {
                        SupportPresenter.this.internalRetryActions.add(new RetryAction() { // from class: com.zendesk.sdk.support.SupportPresenter.5.3
                            @Override // com.zendesk.sdk.network.RetryAction
                            public void onRetry() {
                                SupportPresenter.this.view.showContactUsButton();
                            }
                        });
                        return;
                    }
                }
                Logger.d(SupportPresenter.LOG_TAG, "Help center is disabled", new Object[0]);
                if (safeMobileSettings.isConversationsEnabled()) {
                    Logger.d(SupportPresenter.LOG_TAG, "Starting with conversations", new Object[0]);
                    if (SupportPresenter.this.view != null) {
                        SupportPresenter.this.view.showRequestList();
                        SupportPresenter.this.view.exitActivity();
                        return;
                    } else {
                        SupportPresenter.this.internalRetryActions.add(new RetryAction() { // from class: com.zendesk.sdk.support.SupportPresenter.5.4
                            @Override // com.zendesk.sdk.network.RetryAction
                            public void onRetry() {
                                SupportPresenter.this.view.showRequestList();
                                SupportPresenter.this.view.exitActivity();
                            }
                        });
                        return;
                    }
                }
                Logger.d(SupportPresenter.LOG_TAG, "Starting with contact", new Object[0]);
                if (SupportPresenter.this.view != null) {
                    SupportPresenter.this.view.showContactZendesk();
                    SupportPresenter.this.view.exitActivity();
                } else {
                    SupportPresenter.this.internalRetryActions.add(new RetryAction() { // from class: com.zendesk.sdk.support.SupportPresenter.5.5
                        @Override // com.zendesk.sdk.network.RetryAction
                        public void onRetry() {
                            SupportPresenter.this.view.showContactZendesk();
                            SupportPresenter.this.view.exitActivity();
                        }
                    });
                }
            }

            @Override // com.zendesk.service.ZendeskCallback
            public void onError(ErrorResponse errorResponse) {
                Logger.e(SupportPresenter.LOG_TAG, "Failed to get mobile settings. Cannot determine start screen.", new Object[0]);
                Logger.e(SupportPresenter.LOG_TAG, errorResponse);
                if (SupportPresenter.this.view != null) {
                    SupportPresenter.this.view.hideLoadingState();
                    SupportPresenter.this.view.exitActivity();
                } else {
                    SupportPresenter.this.internalRetryActions.add(new RetryAction() { // from class: com.zendesk.sdk.support.SupportPresenter.5.6
                        @Override // com.zendesk.sdk.network.RetryAction
                        public void onRetry() {
                            SupportPresenter.this.view.hideLoadingState();
                            SupportPresenter.this.view.exitActivity();
                        }
                    });
                }
            }
        });
    }

    @Override // com.zendesk.sdk.support.SupportMvp.Presenter
    public boolean shouldShowConversationsMenuItem() {
        return this.mobileSettings.isConversationsEnabled() && this.config.isShowConversationsMenuButton();
    }

    @Override // com.zendesk.sdk.support.SupportMvp.Presenter
    public boolean shouldShowSearchMenuItem() {
        return this.mobileSettings.hasHelpCenterSettings();
    }
}
