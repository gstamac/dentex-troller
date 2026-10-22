package com.zendesk.sdk.rating.impl;

import com.zendesk.sdk.R;
import com.zendesk.sdk.rating.RateMyAppButton;

/* JADX INFO: loaded from: classes2.dex */
public abstract class BaseRateMyAppButton implements RateMyAppButton {
    @Override // com.zendesk.sdk.rating.RateMyAppButton
    public int getId() {
        return -1;
    }

    @Override // com.zendesk.sdk.rating.RateMyAppButton
    public boolean shouldDismissDialog() {
        return true;
    }

    @Override // com.zendesk.sdk.rating.RateMyAppButton
    public int getStyleAttributeId() {
        return R.attr.RateMyAppButtonStyle;
    }
}
