package com.zendesk.sdk.rating.impl;

import android.content.Context;
import android.view.View;
import com.zendesk.sdk.R;
import com.zendesk.sdk.storage.RateMyAppStorage;

/* JADX INFO: loaded from: classes2.dex */
public class RateMyAppDontAskAgainButton extends BaseRateMyAppButton {
    private final String mLabel;

    public RateMyAppDontAskAgainButton(Context context) {
        this.mLabel = context.getString(R.string.rate_my_app_dialog_dismiss_action_label);
    }

    @Override // com.zendesk.sdk.rating.RateMyAppButton
    public String getLabel() {
        return this.mLabel;
    }

    @Override // com.zendesk.sdk.rating.RateMyAppButton
    public View.OnClickListener getOnClickListener() {
        return new View.OnClickListener() { // from class: com.zendesk.sdk.rating.impl.RateMyAppDontAskAgainButton.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                new RateMyAppStorage(view.getContext()).setDontShowAgain();
            }
        };
    }

    @Override // com.zendesk.sdk.rating.impl.BaseRateMyAppButton, com.zendesk.sdk.rating.RateMyAppButton
    public int getId() {
        return R.id.rma_dont_ask_again;
    }
}
