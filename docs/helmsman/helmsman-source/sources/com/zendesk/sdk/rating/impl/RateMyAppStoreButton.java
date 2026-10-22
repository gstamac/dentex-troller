package com.zendesk.sdk.rating.impl;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.R;
import com.zendesk.sdk.model.settings.SafeMobileSettings;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zendesk.sdk.storage.RateMyAppStorage;
import com.zendesk.util.StringUtils;

/* JADX INFO: loaded from: classes2.dex */
public class RateMyAppStoreButton extends BaseRateMyAppButton {
    private static final String LOG_TAG = "RateMyAppStoreButton";
    private final String mButtonLabel;

    public RateMyAppStoreButton(Context context) {
        this.mButtonLabel = context.getString(R.string.rate_my_app_dialog_positive_action_label);
    }

    @Override // com.zendesk.sdk.rating.RateMyAppButton
    public String getLabel() {
        return this.mButtonLabel;
    }

    @Override // com.zendesk.sdk.rating.RateMyAppButton
    public View.OnClickListener getOnClickListener() {
        final SafeMobileSettings mobileSettings = ZendeskConfig.INSTANCE.getMobileSettings();
        if (StringUtils.isEmpty(mobileSettings.getRateMyAppStoreUrl())) {
            Logger.e(LOG_TAG, "Rate my app settings and / or store url is null, disabling button", new Object[0]);
            return null;
        }
        return new View.OnClickListener() { // from class: com.zendesk.sdk.rating.impl.RateMyAppStoreButton.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Context context = view.getContext();
                try {
                    String rateMyAppStoreUrl = mobileSettings.getRateMyAppStoreUrl();
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(rateMyAppStoreUrl));
                    Logger.i(RateMyAppStoreButton.LOG_TAG, "Using store URL: " + rateMyAppStoreUrl, new Object[0]);
                    context.startActivity(intent);
                    new RateMyAppStorage(context).setRatedForCurrentVersion();
                    ZendeskConfig.INSTANCE.getTracker().rateMyAppRated();
                } catch (Exception e) {
                    Logger.e(RateMyAppStoreButton.LOG_TAG, e.getMessage(), e, new Object[0]);
                }
            }
        };
    }

    @Override // com.zendesk.sdk.rating.impl.BaseRateMyAppButton, com.zendesk.sdk.rating.RateMyAppButton
    public int getId() {
        return R.id.rma_store_button;
    }
}
