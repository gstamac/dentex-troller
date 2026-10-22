package com.chanven.lib.cptr.loadmore;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.chanven.lib.cptr.R;

/* JADX INFO: loaded from: classes.dex */
public class DefaultLoadMoreViewFooter implements ILoadMoreViewFactory {
    @Override // com.chanven.lib.cptr.loadmore.ILoadMoreViewFactory
    public ILoadMoreViewFactory.ILoadMoreView madeLoadMoreView() {
        return new LoadMoreHelper();
    }

    private class LoadMoreHelper implements ILoadMoreViewFactory.ILoadMoreView {
        protected ProgressBar footerBar;
        protected TextView footerTv;
        protected View footerView;
        protected View.OnClickListener onClickRefreshListener;

        private LoadMoreHelper() {
        }

        @Override // com.chanven.lib.cptr.loadmore.ILoadMoreViewFactory.ILoadMoreView
        public void init(ILoadMoreViewFactory.FootViewAdder footViewAdder, View.OnClickListener onClickListener) {
            View viewAddFootView = footViewAdder.addFootView(R.layout.loadmore_default_footer);
            this.footerView = viewAddFootView;
            this.footerTv = (TextView) viewAddFootView.findViewById(R.id.loadmore_default_footer_tv);
            this.footerBar = (ProgressBar) this.footerView.findViewById(R.id.loadmore_default_footer_progressbar);
            this.onClickRefreshListener = onClickListener;
            showNormal();
        }

        @Override // com.chanven.lib.cptr.loadmore.ILoadMoreViewFactory.ILoadMoreView
        public void showNormal() {
            this.footerTv.setText(R.string.cptr_click_to_load_more);
            this.footerBar.setVisibility(8);
            this.footerView.setOnClickListener(this.onClickRefreshListener);
        }

        @Override // com.chanven.lib.cptr.loadmore.ILoadMoreViewFactory.ILoadMoreView
        public void showLoading() {
            this.footerTv.setText(R.string.cptr_loading);
            this.footerBar.setVisibility(0);
            this.footerView.setOnClickListener(null);
        }

        @Override // com.chanven.lib.cptr.loadmore.ILoadMoreViewFactory.ILoadMoreView
        public void showFail(Exception exc) {
            this.footerTv.setText(R.string.cptr_failed_to_load_click_refresh);
            this.footerBar.setVisibility(8);
            this.footerView.setOnClickListener(this.onClickRefreshListener);
        }

        @Override // com.chanven.lib.cptr.loadmore.ILoadMoreViewFactory.ILoadMoreView
        public void showNomore() {
            this.footerTv.setText(R.string.cptr_loaded_already);
            this.footerBar.setVisibility(8);
            this.footerView.setOnClickListener(null);
        }

        @Override // com.chanven.lib.cptr.loadmore.ILoadMoreViewFactory.ILoadMoreView
        public void setFooterVisibility(boolean z) {
            this.footerView.setVisibility(z ? 0 : 8);
        }
    }
}
