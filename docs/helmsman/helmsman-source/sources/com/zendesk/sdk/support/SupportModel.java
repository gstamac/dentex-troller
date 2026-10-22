package com.zendesk.sdk.support;

import com.zendesk.sdk.model.helpcenter.HelpCenterSearch;
import com.zendesk.sdk.model.helpcenter.SearchArticle;
import com.zendesk.sdk.model.settings.SafeMobileSettings;
import com.zendesk.sdk.network.HelpCenterProvider;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zendesk.service.ZendeskCallback;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
class SupportModel implements SupportMvp.Model {
    private HelpCenterProvider provider = ZendeskConfig.INSTANCE.provider().helpCenterProvider();

    SupportModel() {
    }

    @Override // com.zendesk.sdk.support.SupportMvp.Model
    public void search(List<Long> list, List<Long> list2, String str, String[] strArr, ZendeskCallback<List<SearchArticle>> zendeskCallback) {
        this.provider.searchArticles(new HelpCenterSearch.Builder().withQuery(str).withCategoryIds(list).withSectionIds(list2).withLabelNames(strArr).build(), zendeskCallback);
    }

    @Override // com.zendesk.sdk.support.SupportMvp.Model
    public void getSettings(ZendeskCallback<SafeMobileSettings> zendeskCallback) {
        if (ZendeskConfig.INSTANCE.storage().sdkSettingsStorage().hasStoredSdkSettings()) {
            zendeskCallback.onSuccess(ZendeskConfig.INSTANCE.storage().sdkSettingsStorage().getStoredSettings());
        } else {
            ZendeskConfig.INSTANCE.provider().sdkSettingsProvider().getSettings(zendeskCallback);
        }
    }
}
