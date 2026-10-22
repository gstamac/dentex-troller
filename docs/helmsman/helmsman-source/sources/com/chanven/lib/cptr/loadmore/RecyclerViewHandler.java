package com.chanven.lib.cptr.loadmore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;

/* JADX INFO: loaded from: classes.dex */
public class RecyclerViewHandler implements LoadMoreHandler {
    private View mFooter;
    private RecyclerAdapterWithHF mRecyclerAdapter;

    @Override // com.chanven.lib.cptr.loadmore.LoadMoreHandler
    public boolean handleSetAdapter(View view, ILoadMoreViewFactory.ILoadMoreView iLoadMoreView, View.OnClickListener onClickListener) {
        final RecyclerView recyclerView = (RecyclerView) view;
        this.mRecyclerAdapter = (RecyclerAdapterWithHF) recyclerView.getAdapter();
        if (iLoadMoreView == null) {
            return false;
        }
        final Context applicationContext = recyclerView.getContext().getApplicationContext();
        iLoadMoreView.init(new ILoadMoreViewFactory.FootViewAdder() { // from class: com.chanven.lib.cptr.loadmore.RecyclerViewHandler.1
            @Override // com.chanven.lib.cptr.loadmore.ILoadMoreViewFactory.FootViewAdder
            public View addFootView(int i) {
                View viewInflate = LayoutInflater.from(applicationContext).inflate(i, (ViewGroup) recyclerView, false);
                RecyclerViewHandler.this.mFooter = viewInflate;
                return addFootView(viewInflate);
            }

            @Override // com.chanven.lib.cptr.loadmore.ILoadMoreViewFactory.FootViewAdder
            public View addFootView(View view2) {
                RecyclerViewHandler.this.mRecyclerAdapter.addFooter(view2);
                return view2;
            }
        }, onClickListener);
        return true;
    }

    @Override // com.chanven.lib.cptr.loadmore.LoadMoreHandler
    public void addFooter() {
        View view;
        if (this.mRecyclerAdapter.getFootSize() > 0 || (view = this.mFooter) == null) {
            return;
        }
        this.mRecyclerAdapter.addFooter(view);
    }

    @Override // com.chanven.lib.cptr.loadmore.LoadMoreHandler
    public void removeFooter() {
        View view;
        if (this.mRecyclerAdapter.getFootSize() <= 0 || (view = this.mFooter) == null) {
            return;
        }
        this.mRecyclerAdapter.removeFooter(view);
    }

    @Override // com.chanven.lib.cptr.loadmore.LoadMoreHandler
    public void setOnScrollBottomListener(View view, OnScrollBottomListener onScrollBottomListener) {
        ((RecyclerView) view).addOnScrollListener(new RecyclerViewOnScrollListener(onScrollBottomListener));
    }

    private static class RecyclerViewOnScrollListener extends RecyclerView.OnScrollListener {
        private OnScrollBottomListener onScrollBottomListener;

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
        }

        public RecyclerViewOnScrollListener(OnScrollBottomListener onScrollBottomListener) {
            this.onScrollBottomListener = onScrollBottomListener;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            OnScrollBottomListener onScrollBottomListener;
            if (i == 0 && isScollBottom(recyclerView) && (onScrollBottomListener = this.onScrollBottomListener) != null) {
                onScrollBottomListener.onScorllBootom();
            }
        }

        private boolean isScollBottom(RecyclerView recyclerView) {
            return !isCanScollVertically(recyclerView);
        }

        private boolean isCanScollVertically(RecyclerView recyclerView) {
            return ViewCompat.canScrollVertically(recyclerView, 1);
        }
    }
}
