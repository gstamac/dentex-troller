package com.zendesk.sdk.support.help;

import com.zendesk.logger.Logger;
import com.zendesk.sdk.model.helpcenter.help.CategoryItem;
import com.zendesk.sdk.model.helpcenter.help.HelpItem;
import com.zendesk.sdk.model.helpcenter.help.SectionItem;
import com.zendesk.sdk.model.helpcenter.help.SeeAllArticlesItem;
import com.zendesk.sdk.network.NetworkInfoProvider;
import com.zendesk.sdk.network.RetryAction;
import com.zendesk.sdk.support.SupportMvp;
import com.zendesk.sdk.support.SupportUiConfig;
import com.zendesk.service.ErrorResponse;
import com.zendesk.service.ZendeskCallback;
import com.zendesk.util.CollectionUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
class HelpAdapterPresenter implements HelpMvp.Presenter {
    private static final String LOG_TAG = "HelpAdapterPresenter";
    private static final Integer RETRY_ACTION_ID = 5;
    private static final String TAG = "HelpAdapterPresenter";
    private SupportMvp.Presenter contentPresenter;
    private boolean hasError;
    private HelpMvp.Model model;
    private NetworkInfoProvider networkInfoProvider;
    private boolean noResults;
    private RetryAction retryAction;
    private SupportUiConfig supportUiConfig;
    private HelpMvp.View view;
    private List<HelpItem> helpItems = new ArrayList();
    private List<HelpItem> filteredItems = new ArrayList();
    private ZendeskCallback<List<HelpItem>> callback = new ZendeskCallback<List<HelpItem>>() { // from class: com.zendesk.sdk.support.help.HelpAdapterPresenter.2
        @Override // com.zendesk.service.ZendeskCallback
        public void onSuccess(List<HelpItem> list) {
            HelpAdapterPresenter.this.hasError = false;
            HelpAdapterPresenter.this.helpItems = CollectionUtils.copyOf(list);
            if (HelpAdapterPresenter.this.supportUiConfig.isCollapseCategories()) {
                HelpAdapterPresenter helpAdapterPresenter = HelpAdapterPresenter.this;
                helpAdapterPresenter.filteredItems = helpAdapterPresenter.getCollapsedCategories(helpAdapterPresenter.helpItems);
            } else {
                HelpAdapterPresenter helpAdapterPresenter2 = HelpAdapterPresenter.this;
                helpAdapterPresenter2.filteredItems = CollectionUtils.copyOf(helpAdapterPresenter2.helpItems);
            }
            HelpAdapterPresenter helpAdapterPresenter3 = HelpAdapterPresenter.this;
            helpAdapterPresenter3.noResults = CollectionUtils.isEmpty(helpAdapterPresenter3.filteredItems);
            HelpAdapterPresenter.this.view.showItems(HelpAdapterPresenter.this.filteredItems);
            HelpAdapterPresenter.this.contentPresenter.onLoad();
        }

        @Override // com.zendesk.service.ZendeskCallback
        public void onError(ErrorResponse errorResponse) {
            SupportMvp.ErrorType errorType;
            if (!CollectionUtils.isNotEmpty(HelpAdapterPresenter.this.supportUiConfig.getCategoryIds())) {
                if (CollectionUtils.isNotEmpty(HelpAdapterPresenter.this.supportUiConfig.getSectionIds())) {
                    errorType = SupportMvp.ErrorType.SECTION_LOAD;
                } else {
                    errorType = SupportMvp.ErrorType.ARTICLES_LOAD;
                }
            } else {
                errorType = SupportMvp.ErrorType.CATEGORY_LOAD;
            }
            HelpAdapterPresenter.this.contentPresenter.onErrorWithRetry(errorType, new RetryAction() { // from class: com.zendesk.sdk.support.help.HelpAdapterPresenter.2.1
                @Override // com.zendesk.sdk.network.RetryAction
                public void onRetry() {
                    HelpAdapterPresenter.this.hasError = false;
                    HelpAdapterPresenter.this.view.showItems(HelpAdapterPresenter.this.filteredItems);
                    HelpAdapterPresenter.this.requestHelpContent();
                }
            });
            HelpAdapterPresenter.this.hasError = true;
            HelpAdapterPresenter.this.view.showItems(HelpAdapterPresenter.this.filteredItems);
        }
    };

    public HelpAdapterPresenter(HelpMvp.View view, HelpMvp.Model model, NetworkInfoProvider networkInfoProvider, SupportUiConfig supportUiConfig) {
        this.view = view;
        this.model = model;
        this.networkInfoProvider = networkInfoProvider;
        this.supportUiConfig = supportUiConfig;
    }

    @Override // com.zendesk.sdk.support.help.HelpMvp.Presenter
    public void setContentPresenter(SupportMvp.Presenter presenter) {
        this.contentPresenter = presenter;
    }

    @Override // com.zendesk.sdk.support.help.HelpMvp.Presenter
    public void onAttached() {
        this.networkInfoProvider.register();
        if (CollectionUtils.isEmpty(this.helpItems)) {
            requestHelpContent();
        }
    }

    @Override // com.zendesk.sdk.support.help.HelpMvp.Presenter
    public void onDetached() {
        this.networkInfoProvider.removeRetryAction(RETRY_ACTION_ID);
        this.networkInfoProvider.unregister();
    }

    @Override // com.zendesk.sdk.support.help.HelpMvp.Presenter
    public boolean onCategoryClick(CategoryItem categoryItem, int i) {
        if (categoryItem == null) {
            return false;
        }
        boolean expanded = categoryItem.setExpanded(!categoryItem.isExpanded());
        if (expanded) {
            expandItem(categoryItem, this.filteredItems.indexOf(categoryItem));
        } else {
            collapseItem(this.filteredItems.indexOf(categoryItem));
        }
        return expanded;
    }

    @Override // com.zendesk.sdk.support.help.HelpMvp.Presenter
    public void onSeeAllClick(SeeAllArticlesItem seeAllArticlesItem) {
        loadMoreArticles(seeAllArticlesItem);
    }

    @Override // com.zendesk.sdk.support.help.HelpMvp.Presenter
    public HelpItem getItem(int i) {
        return this.filteredItems.get(i);
    }

    @Override // com.zendesk.sdk.support.help.HelpMvp.Presenter
    public int getItemCount() {
        if (this.hasError) {
            return 0;
        }
        return Math.max(this.filteredItems.size() + getPaddingItemCount(), 1);
    }

    private int getPaddingItemCount() {
        return this.supportUiConfig.isAddListPaddingBottom() ? 1 : 0;
    }

    @Override // com.zendesk.sdk.support.help.HelpMvp.Presenter
    public int getItemViewType(int i) {
        if (this.noResults) {
            return 7;
        }
        if (this.filteredItems.size() <= 0) {
            return 5;
        }
        if (i == this.filteredItems.size()) {
            return 8;
        }
        return this.filteredItems.get(i).getViewType();
    }

    @Override // com.zendesk.sdk.support.help.HelpMvp.Presenter
    public HelpItem getItemForBinding(int i) {
        if (this.filteredItems.size() <= 0 || i >= this.filteredItems.size()) {
            return null;
        }
        return this.filteredItems.get(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestHelpContent() {
        if (this.networkInfoProvider.isNetworkAvailable()) {
            this.model.getArticles(this.supportUiConfig.getCategoryIds(), this.supportUiConfig.getSectionIds(), this.supportUiConfig.getLabelNames(), this.callback);
            return;
        }
        RetryAction retryAction = new RetryAction() { // from class: com.zendesk.sdk.support.help.HelpAdapterPresenter.1
            @Override // com.zendesk.sdk.network.RetryAction
            public void onRetry() {
                HelpAdapterPresenter.this.requestHelpContent();
            }
        };
        this.retryAction = retryAction;
        this.networkInfoProvider.addRetryAction(RETRY_ACTION_ID, retryAction);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<HelpItem> getCollapsedCategories(List<HelpItem> list) {
        ArrayList arrayList = new ArrayList();
        if (list != null && list.size() != 0) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                if (1 == list.get(i).getViewType()) {
                    arrayList.add(list.get(i));
                    ((CategoryItem) list.get(i)).setExpanded(false);
                }
            }
        }
        return arrayList;
    }

    private void expandItem(CategoryItem categoryItem, int i) {
        int i2 = i + 1;
        for (HelpItem helpItem : categoryItem.getChildren()) {
            addItem(i2, helpItem);
            i2++;
            try {
                Iterator<HelpItem> it = ((SectionItem) helpItem).getChildren().iterator();
                while (it.hasNext()) {
                    addItem(i2, it.next());
                    i2++;
                }
            } catch (ClassCastException e) {
                Logger.e("HelpAdapterPresenter", "Error expanding item", e, new Object[0]);
            }
        }
    }

    private void collapseItem(int i) {
        if (i >= getItemCount() - 1) {
            return;
        }
        int i2 = i + 1;
        while (i2 < this.filteredItems.size() && 1 != this.filteredItems.get(i2).getViewType()) {
            removeItem(i2);
        }
    }

    private void addItem(int i, HelpItem helpItem) {
        this.filteredItems.add(i, helpItem);
        this.view.addItem(i, helpItem);
    }

    private void removeItem(int i) {
        this.filteredItems.remove(i);
        this.view.removeItem(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadMoreArticles(final SeeAllArticlesItem seeAllArticlesItem) {
        final SectionItem section = seeAllArticlesItem.getSection();
        final RetryAction retryAction = new RetryAction() { // from class: com.zendesk.sdk.support.help.HelpAdapterPresenter.3
            @Override // com.zendesk.sdk.network.RetryAction
            public void onRetry() {
                HelpAdapterPresenter.this.loadMoreArticles(seeAllArticlesItem);
            }
        };
        if (this.networkInfoProvider.isNetworkAvailable()) {
            this.model.getArticlesForSection(section, this.supportUiConfig.getLabelNames(), new ZendeskCallback<List<HelpItem>>() { // from class: com.zendesk.sdk.support.help.HelpAdapterPresenter.4
                @Override // com.zendesk.service.ZendeskCallback
                public void onSuccess(List<HelpItem> list) {
                    int iIndexOf = HelpAdapterPresenter.this.helpItems.indexOf(seeAllArticlesItem);
                    int iIndexOf2 = HelpAdapterPresenter.this.filteredItems.indexOf(seeAllArticlesItem);
                    for (HelpItem helpItem : list) {
                        if (!HelpAdapterPresenter.this.helpItems.contains(helpItem)) {
                            int i = iIndexOf + 1;
                            HelpAdapterPresenter.this.helpItems.add(iIndexOf, helpItem);
                            section.addChild(helpItem);
                            if (iIndexOf2 != -1) {
                                HelpAdapterPresenter.this.filteredItems.add(iIndexOf2, helpItem);
                                HelpAdapterPresenter.this.view.addItem(iIndexOf2, helpItem);
                                iIndexOf2++;
                            }
                            iIndexOf = i;
                        }
                    }
                    HelpAdapterPresenter.this.helpItems.remove(seeAllArticlesItem);
                    int iIndexOf3 = HelpAdapterPresenter.this.filteredItems.indexOf(seeAllArticlesItem);
                    HelpAdapterPresenter.this.filteredItems.remove(seeAllArticlesItem);
                    section.removeChild(seeAllArticlesItem);
                    HelpAdapterPresenter.this.view.removeItem(iIndexOf3);
                }

                @Override // com.zendesk.service.ZendeskCallback
                public void onError(ErrorResponse errorResponse) {
                    HelpAdapterPresenter.this.helpItems.remove(seeAllArticlesItem);
                    Logger.e("HelpAdapterPresenter", "Failed to load more articles", errorResponse);
                    HelpAdapterPresenter.this.contentPresenter.onErrorWithRetry(SupportMvp.ErrorType.ARTICLES_LOAD, retryAction);
                }
            });
        } else {
            this.retryAction = retryAction;
            this.networkInfoProvider.addRetryAction(RETRY_ACTION_ID, retryAction);
        }
    }
}
