package com.chanven.lib.cptr;

import android.view.View;

/* JADX INFO: loaded from: classes.dex */
public abstract class PtrDefaultHandler implements PtrHandler {
    public static boolean canChildScrollUp(View view) {
        return view.canScrollVertically(-1);
    }

    public static boolean checkContentCanBePulledDown(PtrFrameLayout ptrFrameLayout, View view, View view2) {
        return !canChildScrollUp(view);
    }

    @Override // com.chanven.lib.cptr.PtrHandler
    public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View view, View view2) {
        return checkContentCanBePulledDown(ptrFrameLayout, view, view2);
    }
}
