package com.chanven.lib.cptr;

import android.view.View;

/* JADX INFO: loaded from: classes.dex */
public interface PtrHandler {
    boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View view, View view2);

    void onRefreshBegin(PtrFrameLayout ptrFrameLayout);
}
