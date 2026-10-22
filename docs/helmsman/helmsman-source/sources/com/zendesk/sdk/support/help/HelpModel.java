package com.zendesk.sdk.support.help;

import com.zendesk.sdk.model.helpcenter.help.HelpItem;
import com.zendesk.sdk.model.helpcenter.help.HelpRequest;
import com.zendesk.sdk.model.helpcenter.help.SectionItem;
import com.zendesk.sdk.network.HelpCenterProvider;
import com.zendesk.service.ZendeskCallback;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
class HelpModel implements HelpMvp.Model {
    private HelpCenterProvider provider;

    HelpModel(HelpCenterProvider helpCenterProvider) {
        this.provider = helpCenterProvider;
    }

    @Override // com.zendesk.sdk.support.help.HelpMvp.Model
    public void getArticles(List<Long> list, List<Long> list2, String[] strArr, ZendeskCallback<List<HelpItem>> zendeskCallback) {
        this.provider.getHelp(new HelpRequest.Builder().withCategoryIds(list).withSectionIds(list2).withLabelNames(strArr).includeCategories().includeSections().build(), zendeskCallback);
    }

    @Override // com.zendesk.sdk.support.help.HelpMvp.Model
    public void getArticlesForSection(SectionItem sectionItem, String[] strArr, ZendeskCallback<List<HelpItem>> zendeskCallback) {
        this.provider.getHelp(new HelpRequest.Builder().withSectionIds(Collections.singletonList(sectionItem.getId())).withArticlesPerSectionLimit(sectionItem.getTotalArticlesCount()).withLabelNames(strArr).includeSections().build(), zendeskCallback);
    }
}
