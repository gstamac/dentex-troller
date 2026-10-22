package com.zendesk.sdk.ui;

import android.view.View;

/* JADX INFO: loaded from: classes2.dex */
public interface ListRowView<T> {
    void bind(T t);

    View getView();
}
