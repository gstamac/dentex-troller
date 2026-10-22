package com.chanven.lib.cptr.loadmore;

import android.view.View;

/* JADX INFO: loaded from: classes.dex */
public interface LoadMoreHandler {
    void addFooter();

    boolean handleSetAdapter(View view, ILoadMoreViewFactory.ILoadMoreView iLoadMoreView, View.OnClickListener onClickListener);

    void removeFooter();

    void setOnScrollBottomListener(View view, OnScrollBottomListener onScrollBottomListener);
}
