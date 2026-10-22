package com.chanven.lib.cptr.loadmore;

import android.view.View;

/* JADX INFO: loaded from: classes.dex */
public interface ILoadMoreViewFactory {

    public interface FootViewAdder {
        View addFootView(int i);

        View addFootView(View view);
    }

    public interface ILoadMoreView {
        void init(FootViewAdder footViewAdder, View.OnClickListener onClickListener);

        void setFooterVisibility(boolean z);

        void showFail(Exception exc);

        void showLoading();

        void showNomore();

        void showNormal();
    }

    ILoadMoreView madeLoadMoreView();
}
