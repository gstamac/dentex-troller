package com.zendesk.sdk.rating;

import android.view.View;
import java.io.Serializable;

/* JADX INFO: loaded from: classes2.dex */
public interface RateMyAppButton extends Serializable {
    int getId();

    String getLabel();

    View.OnClickListener getOnClickListener();

    int getStyleAttributeId();

    boolean shouldDismissDialog();
}
