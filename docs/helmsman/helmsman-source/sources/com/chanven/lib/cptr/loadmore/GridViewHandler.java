package com.chanven.lib.cptr.loadmore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;

/* JADX INFO: loaded from: classes.dex */
public class GridViewHandler implements LoadMoreHandler {
    private View mFooter;
    private GridViewWithHeaderAndFooter mGridView;

    @Override // com.chanven.lib.cptr.loadmore.LoadMoreHandler
    public boolean handleSetAdapter(View view, ILoadMoreViewFactory.ILoadMoreView iLoadMoreView, View.OnClickListener onClickListener) {
        GridViewWithHeaderAndFooter gridViewWithHeaderAndFooter = (GridViewWithHeaderAndFooter) view;
        this.mGridView = gridViewWithHeaderAndFooter;
        ListAdapter adapter = gridViewWithHeaderAndFooter.getAdapter();
        if (iLoadMoreView == null) {
            return false;
        }
        final Context applicationContext = this.mGridView.getContext().getApplicationContext();
        iLoadMoreView.init(new ILoadMoreViewFactory.FootViewAdder() { // from class: com.chanven.lib.cptr.loadmore.GridViewHandler.1
            @Override // com.chanven.lib.cptr.loadmore.ILoadMoreViewFactory.FootViewAdder
            public View addFootView(int i) {
                View viewInflate = LayoutInflater.from(applicationContext).inflate(i, (ViewGroup) GridViewHandler.this.mGridView, false);
                GridViewHandler.this.mFooter = viewInflate;
                return addFootView(viewInflate);
            }

            @Override // com.chanven.lib.cptr.loadmore.ILoadMoreViewFactory.FootViewAdder
            public View addFootView(View view2) {
                GridViewHandler.this.mGridView.addFooterView(view2);
                return view2;
            }
        }, onClickListener);
        if (adapter != null) {
            this.mGridView.setAdapter(adapter);
        }
        return true;
    }

    @Override // com.chanven.lib.cptr.loadmore.LoadMoreHandler
    public void addFooter() {
        View view;
        if (this.mGridView.getFooterViewCount() > 0 || (view = this.mFooter) == null) {
            return;
        }
        this.mGridView.addFooterView(view);
    }

    @Override // com.chanven.lib.cptr.loadmore.LoadMoreHandler
    public void removeFooter() {
        View view;
        if (this.mGridView.getFooterViewCount() <= 0 || (view = this.mFooter) == null) {
            return;
        }
        this.mGridView.removeFooterView(view);
    }

    @Override // com.chanven.lib.cptr.loadmore.LoadMoreHandler
    public void setOnScrollBottomListener(View view, OnScrollBottomListener onScrollBottomListener) {
        GridViewWithHeaderAndFooter gridViewWithHeaderAndFooter = (GridViewWithHeaderAndFooter) view;
        gridViewWithHeaderAndFooter.setOnScrollListener(new GridViewOnScrollListener(onScrollBottomListener));
        gridViewWithHeaderAndFooter.setOnItemSelectedListener(new GridViewOnItemSelectedListener(onScrollBottomListener));
    }

    private class GridViewOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        private OnScrollBottomListener onScrollBottomListener;

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView<?> adapterView) {
        }

        public GridViewOnItemSelectedListener(OnScrollBottomListener onScrollBottomListener) {
            this.onScrollBottomListener = onScrollBottomListener;
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            OnScrollBottomListener onScrollBottomListener;
            if (adapterView.getLastVisiblePosition() + 1 != adapterView.getCount() || (onScrollBottomListener = this.onScrollBottomListener) == null) {
                return;
            }
            onScrollBottomListener.onScorllBootom();
        }
    }

    private static class GridViewOnScrollListener implements AbsListView.OnScrollListener {
        private OnScrollBottomListener onScrollBottomListener;

        @Override // android.widget.AbsListView.OnScrollListener
        public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        }

        public GridViewOnScrollListener(OnScrollBottomListener onScrollBottomListener) {
            this.onScrollBottomListener = onScrollBottomListener;
        }

        @Override // android.widget.AbsListView.OnScrollListener
        public void onScrollStateChanged(AbsListView absListView, int i) {
            OnScrollBottomListener onScrollBottomListener;
            if (i == 0 && absListView.getLastVisiblePosition() + 1 == absListView.getCount() && (onScrollBottomListener = this.onScrollBottomListener) != null) {
                onScrollBottomListener.onScorllBootom();
            }
        }
    }
}
