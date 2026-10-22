package com.chanven.lib.cptr.loadmore;

import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
public class SwipeRefreshHelper {
    private View mContentView;
    private LoadMoreHandler mLoadMoreHandler;
    private ILoadMoreViewFactory.ILoadMoreView mLoadMoreView;
    private OnLoadMoreListener mOnLoadMoreListener;
    private OnSwipeRefreshListener mOnSwipeRefreshListener;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isLoadingMore = false;
    private boolean isAutoLoadMoreEnable = true;
    private boolean isLoadMoreEnable = false;
    private boolean hasInitLoadMoreView = false;
    private ILoadMoreViewFactory loadMoreViewFactory = new DefaultLoadMoreViewFooter();
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() { // from class: com.chanven.lib.cptr.loadmore.SwipeRefreshHelper.1
        @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
        public void onRefresh() {
            if (SwipeRefreshHelper.this.mOnSwipeRefreshListener != null) {
                SwipeRefreshHelper.this.mOnSwipeRefreshListener.onfresh();
            }
        }
    };
    private OnScrollBottomListener onScrollBottomListener = new OnScrollBottomListener() { // from class: com.chanven.lib.cptr.loadmore.SwipeRefreshHelper.2
        @Override // com.chanven.lib.cptr.loadmore.OnScrollBottomListener
        public void onScorllBootom() {
            if (SwipeRefreshHelper.this.isAutoLoadMoreEnable && SwipeRefreshHelper.this.isLoadMoreEnable && !SwipeRefreshHelper.this.isLoadingMore()) {
                SwipeRefreshHelper.this.loadMore();
            }
        }
    };
    private View.OnClickListener onClickLoadMoreListener = new View.OnClickListener() { // from class: com.chanven.lib.cptr.loadmore.SwipeRefreshHelper.3
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (!SwipeRefreshHelper.this.isLoadMoreEnable || SwipeRefreshHelper.this.isLoadingMore()) {
                return;
            }
            SwipeRefreshHelper.this.loadMore();
        }
    };

    public interface OnSwipeRefreshListener {
        void onfresh();
    }

    public void autoRefresh() {
        if (this.mOnSwipeRefreshListener != null) {
            this.mSwipeRefreshLayout.setRefreshing(true);
            this.mOnSwipeRefreshListener.onfresh();
        }
    }

    public SwipeRefreshHelper(SwipeRefreshLayout swipeRefreshLayout) {
        this.mSwipeRefreshLayout = swipeRefreshLayout;
        init();
    }

    private void init() {
        if (this.mSwipeRefreshLayout.getChildCount() <= 0) {
            throw new RuntimeException("SwipRefreshLayout has no child view");
        }
        try {
            Field declaredField = this.mSwipeRefreshLayout.getClass().getDeclaredField("mTarget");
            declaredField.setAccessible(true);
            this.mContentView = (View) declaredField.get(this.mSwipeRefreshLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnSwipeRefreshListener(OnSwipeRefreshListener onSwipeRefreshListener) {
        this.mOnSwipeRefreshListener = onSwipeRefreshListener;
        this.mSwipeRefreshLayout.setOnRefreshListener(this.mOnRefreshListener);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
    }

    public void refreshComplete() {
        this.mSwipeRefreshLayout.setRefreshing(false);
    }

    public void setFooterView(ILoadMoreViewFactory iLoadMoreViewFactory) {
        if (iLoadMoreViewFactory != null) {
            ILoadMoreViewFactory iLoadMoreViewFactory2 = this.loadMoreViewFactory;
            if (iLoadMoreViewFactory2 == null || iLoadMoreViewFactory2 != iLoadMoreViewFactory) {
                this.loadMoreViewFactory = iLoadMoreViewFactory;
                if (this.hasInitLoadMoreView) {
                    this.mLoadMoreHandler.removeFooter();
                    ILoadMoreViewFactory.ILoadMoreView iLoadMoreViewMadeLoadMoreView = this.loadMoreViewFactory.madeLoadMoreView();
                    this.mLoadMoreView = iLoadMoreViewMadeLoadMoreView;
                    this.hasInitLoadMoreView = this.mLoadMoreHandler.handleSetAdapter(this.mContentView, iLoadMoreViewMadeLoadMoreView, this.onClickLoadMoreListener);
                    if (this.isLoadMoreEnable) {
                        return;
                    }
                    this.mLoadMoreHandler.removeFooter();
                }
            }
        }
    }

    public void setLoadMoreEnable(boolean z) {
        if (this.isLoadMoreEnable == z) {
            return;
        }
        this.isLoadMoreEnable = z;
        boolean z2 = this.hasInitLoadMoreView;
        if (z2 || !z) {
            if (z2) {
                if (z) {
                    this.mLoadMoreHandler.addFooter();
                    return;
                } else {
                    this.mLoadMoreHandler.removeFooter();
                    return;
                }
            }
            return;
        }
        this.mLoadMoreView = this.loadMoreViewFactory.madeLoadMoreView();
        if (this.mLoadMoreHandler == null) {
            View view = this.mContentView;
            if (view instanceof GridView) {
                this.mLoadMoreHandler = new GridViewHandler();
            } else if (view instanceof AbsListView) {
                this.mLoadMoreHandler = new ListViewHandler();
            } else if (view instanceof RecyclerView) {
                this.mLoadMoreHandler = new RecyclerViewHandler();
            }
        }
        LoadMoreHandler loadMoreHandler = this.mLoadMoreHandler;
        if (loadMoreHandler == null) {
            throw new IllegalStateException("unSupported contentView !");
        }
        this.hasInitLoadMoreView = loadMoreHandler.handleSetAdapter(this.mContentView, this.mLoadMoreView, this.onClickLoadMoreListener);
        this.mLoadMoreHandler.setOnScrollBottomListener(this.mContentView, this.onScrollBottomListener);
    }

    public boolean isLoadMoreEnable() {
        return this.isLoadMoreEnable;
    }

    public void setAutoLoadMoreEnable(boolean z) {
        this.isAutoLoadMoreEnable = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadMore() {
        this.isLoadingMore = true;
        this.mLoadMoreView.showLoading();
        OnLoadMoreListener onLoadMoreListener = this.mOnLoadMoreListener;
        if (onLoadMoreListener != null) {
            onLoadMoreListener.loadMore();
        }
    }

    public void loadMoreComplete(boolean z) {
        this.isLoadingMore = false;
        if (z) {
            this.mLoadMoreView.showNormal();
        } else {
            setNoMoreData();
        }
    }

    public void setNoMoreData() {
        this.isLoadingMore = false;
        this.mLoadMoreView.showNomore();
    }

    public boolean isLoadingMore() {
        return this.isLoadingMore;
    }
}
