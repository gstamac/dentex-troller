package com.chanven.lib.cptr.loadmore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

/* JADX INFO: loaded from: classes.dex */
public class ListViewHandler implements LoadMoreHandler {
    private View mFooter;
    private ListView mListView;

    @Override // com.chanven.lib.cptr.loadmore.LoadMoreHandler
    public boolean handleSetAdapter(View view, ILoadMoreViewFactory.ILoadMoreView iLoadMoreView, View.OnClickListener onClickListener) {
        final ListView listView = (ListView) view;
        this.mListView = listView;
        if (iLoadMoreView == null) {
            return false;
        }
        final Context applicationContext = listView.getContext().getApplicationContext();
        iLoadMoreView.init(new ILoadMoreViewFactory.FootViewAdder() { // from class: com.chanven.lib.cptr.loadmore.ListViewHandler.1
            @Override // com.chanven.lib.cptr.loadmore.ILoadMoreViewFactory.FootViewAdder
            public View addFootView(int i) {
                View viewInflate = LayoutInflater.from(applicationContext).inflate(i, (ViewGroup) listView, false);
                ListViewHandler.this.mFooter = viewInflate;
                return addFootView(viewInflate);
            }

            @Override // com.chanven.lib.cptr.loadmore.ILoadMoreViewFactory.FootViewAdder
            public View addFootView(View view2) {
                listView.addFooterView(view2);
                return view2;
            }
        }, onClickListener);
        return true;
    }

    @Override // com.chanven.lib.cptr.loadmore.LoadMoreHandler
    public void setOnScrollBottomListener(View view, OnScrollBottomListener onScrollBottomListener) {
        ListView listView = (ListView) view;
        listView.setOnScrollListener(new ListViewOnScrollListener(onScrollBottomListener));
        listView.setOnItemSelectedListener(new ListViewOnItemSelectedListener(onScrollBottomListener));
    }

    @Override // com.chanven.lib.cptr.loadmore.LoadMoreHandler
    public void removeFooter() {
        View view;
        if (this.mListView.getFooterViewsCount() <= 0 || (view = this.mFooter) == null) {
            return;
        }
        this.mListView.removeFooterView(view);
    }

    @Override // com.chanven.lib.cptr.loadmore.LoadMoreHandler
    public void addFooter() {
        View view;
        if (this.mListView.getFooterViewsCount() > 0 || (view = this.mFooter) == null) {
            return;
        }
        this.mListView.addFooterView(view);
    }

    private class ListViewOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        private OnScrollBottomListener onScrollBottomListener;

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView<?> adapterView) {
        }

        public ListViewOnItemSelectedListener(OnScrollBottomListener onScrollBottomListener) {
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

    private static class ListViewOnScrollListener implements AbsListView.OnScrollListener {
        private OnScrollBottomListener onScrollBottomListener;

        @Override // android.widget.AbsListView.OnScrollListener
        public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        }

        public ListViewOnScrollListener(OnScrollBottomListener onScrollBottomListener) {
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
