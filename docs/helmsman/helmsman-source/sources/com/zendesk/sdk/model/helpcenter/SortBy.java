package com.zendesk.sdk.model.helpcenter;

import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
public enum SortBy {
    POSITION,
    TITLE,
    CREATED_AT,
    UPDATED_AT;

    public String getApiValue() {
        return name().toLowerCase(Locale.US);
    }
}
