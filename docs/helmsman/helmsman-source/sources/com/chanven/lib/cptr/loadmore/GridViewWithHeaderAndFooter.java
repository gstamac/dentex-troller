package com.chanven.lib.cptr.loadmore;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;
import java.util.ArrayList;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public class GridViewWithHeaderAndFooter extends GridView {
    public static boolean DEBUG = false;
    private static final String LOG_TAG = "GridViewHeaderAndFooter";
    private ArrayList<FixedViewInfo> mFooterViewInfos;
    private ArrayList<FixedViewInfo> mHeaderViewInfos;
    private ItemClickHandler mItemClickHandler;
    private int mNumColumns;
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private AdapterView.OnItemLongClickListener mOnItemLongClickListener;
    private ListAdapter mOriginalAdapter;
    private int mRowHeight;
    private View mViewForMeasureRowHeight;

    private void initHeaderGridView() {
    }

    @Override // android.view.ViewGroup
    public void setClipChildren(boolean z) {
    }

    private static class FixedViewInfo {
        public Object data;
        public boolean isSelectable;
        public View view;
        public ViewGroup viewContainer;

        private FixedViewInfo() {
        }
    }

    public GridViewWithHeaderAndFooter(Context context) {
        super(context);
        this.mNumColumns = -1;
        this.mViewForMeasureRowHeight = null;
        this.mRowHeight = -1;
        this.mHeaderViewInfos = new ArrayList<>();
        this.mFooterViewInfos = new ArrayList<>();
        initHeaderGridView();
    }

    public GridViewWithHeaderAndFooter(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mNumColumns = -1;
        this.mViewForMeasureRowHeight = null;
        this.mRowHeight = -1;
        this.mHeaderViewInfos = new ArrayList<>();
        this.mFooterViewInfos = new ArrayList<>();
        initHeaderGridView();
    }

    public GridViewWithHeaderAndFooter(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mNumColumns = -1;
        this.mViewForMeasureRowHeight = null;
        this.mRowHeight = -1;
        this.mHeaderViewInfos = new ArrayList<>();
        this.mFooterViewInfos = new ArrayList<>();
        initHeaderGridView();
    }

    @Override // android.widget.GridView, android.widget.AbsListView, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        ListAdapter adapter = getAdapter();
        if (adapter == null || !(adapter instanceof HeaderViewGridAdapter)) {
            return;
        }
        HeaderViewGridAdapter headerViewGridAdapter = (HeaderViewGridAdapter) adapter;
        headerViewGridAdapter.setNumColumns(getNumColumnsCompatible());
        headerViewGridAdapter.setRowHeight(getRowHeight());
    }

    public void setClipChildrenSupper(boolean z) {
        super.setClipChildren(false);
    }

    public void addHeaderView(View view) {
        addHeaderView(view, null, true);
    }

    public void addHeaderView(View view, Object obj, boolean z) {
        ListAdapter adapter = getAdapter();
        if (adapter != null && !(adapter instanceof HeaderViewGridAdapter)) {
            throw new IllegalStateException("Cannot add header view to grid -- setAdapter has already been called.");
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        FixedViewInfo fixedViewInfo = new FixedViewInfo();
        FullWidthFixedViewLayout fullWidthFixedViewLayout = new FullWidthFixedViewLayout(getContext());
        if (layoutParams != null) {
            view.setLayoutParams(new FrameLayout.LayoutParams(layoutParams.width, layoutParams.height));
            fullWidthFixedViewLayout.setLayoutParams(new AbsListView.LayoutParams(layoutParams.width, layoutParams.height));
        }
        fullWidthFixedViewLayout.addView(view);
        fixedViewInfo.view = view;
        fixedViewInfo.viewContainer = fullWidthFixedViewLayout;
        fixedViewInfo.data = obj;
        fixedViewInfo.isSelectable = z;
        this.mHeaderViewInfos.add(fixedViewInfo);
        if (adapter != null) {
            ((HeaderViewGridAdapter) adapter).notifyDataSetChanged();
        }
    }

    public void addFooterView(View view) {
        addFooterView(view, null, true);
    }

    public void addFooterView(View view, Object obj, boolean z) {
        ListAdapter adapter = getAdapter();
        if (adapter != null) {
            boolean z2 = adapter instanceof HeaderViewGridAdapter;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        FixedViewInfo fixedViewInfo = new FixedViewInfo();
        FullWidthFixedViewLayout fullWidthFixedViewLayout = new FullWidthFixedViewLayout(getContext());
        if (layoutParams != null) {
            view.setLayoutParams(new FrameLayout.LayoutParams(layoutParams.width, layoutParams.height));
            fullWidthFixedViewLayout.setLayoutParams(new AbsListView.LayoutParams(layoutParams.width, layoutParams.height));
        }
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        fullWidthFixedViewLayout.addView(view);
        fixedViewInfo.view = view;
        fixedViewInfo.viewContainer = fullWidthFixedViewLayout;
        fixedViewInfo.data = obj;
        fixedViewInfo.isSelectable = z;
        this.mFooterViewInfos.add(fixedViewInfo);
    }

    public int getHeaderViewCount() {
        return this.mHeaderViewInfos.size();
    }

    public int getFooterViewCount() {
        return this.mFooterViewInfos.size();
    }

    public boolean removeHeaderView(View view) {
        boolean z = false;
        if (this.mHeaderViewInfos.size() > 0) {
            ListAdapter adapter = getAdapter();
            if (adapter != null && ((HeaderViewGridAdapter) adapter).removeHeader(view)) {
                z = true;
            }
            removeFixedViewInfo(view, this.mHeaderViewInfos);
        }
        return z;
    }

    public boolean removeFooterView(View view) {
        ListAdapter adapter;
        return this.mFooterViewInfos.size() > 0 && (adapter = getAdapter()) != null && ((HeaderViewGridAdapter) adapter).removeFooter(view);
    }

    private void removeFixedViewInfo(View view, ArrayList<FixedViewInfo> arrayList) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            if (arrayList.get(i).view == view) {
                arrayList.remove(i);
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getNumColumnsCompatible() {
        return super.getNumColumns();
    }

    private int getColumnWidthCompatible() {
        return super.getColumnWidth();
    }

    @Override // android.widget.AbsListView, android.widget.AdapterView, android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mViewForMeasureRowHeight = null;
    }

    public void invalidateRowHeight() {
        this.mRowHeight = -1;
    }

    public int getHeaderHeight(int i) {
        if (i >= 0) {
            return this.mHeaderViewInfos.get(i).view.getMeasuredHeight();
        }
        return 0;
    }

    @Override // android.widget.GridView
    public int getVerticalSpacing() {
        try {
            return super.getVerticalSpacing();
        } catch (Exception unused) {
            return 0;
        }
    }

    @Override // android.widget.GridView
    public int getHorizontalSpacing() {
        try {
            return super.getHorizontalSpacing();
        } catch (Exception unused) {
            return 0;
        }
    }

    public int getRowHeight() {
        int i = this.mRowHeight;
        if (i > 0) {
            return i;
        }
        ListAdapter adapter = getAdapter();
        int numColumnsCompatible = getNumColumnsCompatible();
        if (adapter == null || adapter.getCount() <= (this.mHeaderViewInfos.size() + this.mFooterViewInfos.size()) * numColumnsCompatible) {
            return -1;
        }
        int columnWidthCompatible = getColumnWidthCompatible();
        View view = getAdapter().getView(numColumnsCompatible * this.mHeaderViewInfos.size(), this.mViewForMeasureRowHeight, this);
        AbsListView.LayoutParams layoutParams = (AbsListView.LayoutParams) view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new AbsListView.LayoutParams(-1, -2, 0);
            view.setLayoutParams(layoutParams);
        }
        view.measure(getChildMeasureSpec(View.MeasureSpec.makeMeasureSpec(columnWidthCompatible, 1073741824), 0, layoutParams.width), getChildMeasureSpec(View.MeasureSpec.makeMeasureSpec(0, 0), 0, layoutParams.height));
        this.mViewForMeasureRowHeight = view;
        int measuredHeight = view.getMeasuredHeight();
        this.mRowHeight = measuredHeight;
        return measuredHeight;
    }

    public void tryToScrollToBottomSmoothly() {
        smoothScrollToPositionFromTop(getAdapter().getCount() - 1, 0);
    }

    public void tryToScrollToBottomSmoothly(int i) {
        smoothScrollToPositionFromTop(getAdapter().getCount() - 1, 0, i);
    }

    @Override // android.widget.AdapterView
    public void setAdapter(ListAdapter listAdapter) {
        this.mOriginalAdapter = listAdapter;
        if (this.mHeaderViewInfos.size() > 0 || this.mFooterViewInfos.size() > 0) {
            HeaderViewGridAdapter headerViewGridAdapter = new HeaderViewGridAdapter(this.mHeaderViewInfos, this.mFooterViewInfos, listAdapter);
            int numColumnsCompatible = getNumColumnsCompatible();
            if (numColumnsCompatible > 1) {
                headerViewGridAdapter.setNumColumns(numColumnsCompatible);
            }
            headerViewGridAdapter.setRowHeight(getRowHeight());
            super.setAdapter((ListAdapter) headerViewGridAdapter);
            return;
        }
        super.setAdapter(listAdapter);
    }

    public ListAdapter getOriginalAdapter() {
        return this.mOriginalAdapter;
    }

    private class FullWidthFixedViewLayout extends FrameLayout {
        public FullWidthFixedViewLayout(Context context) {
            super(context);
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            int paddingLeft = GridViewWithHeaderAndFooter.this.getPaddingLeft() + getPaddingLeft();
            if (paddingLeft != i) {
                offsetLeftAndRight(paddingLeft - i);
            }
            super.onLayout(z, i, i2, i3, i4);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec((GridViewWithHeaderAndFooter.this.getMeasuredWidth() - GridViewWithHeaderAndFooter.this.getPaddingLeft()) - GridViewWithHeaderAndFooter.this.getPaddingRight(), View.MeasureSpec.getMode(i)), i2);
        }
    }

    @Override // android.widget.GridView
    public void setNumColumns(int i) {
        super.setNumColumns(i);
        this.mNumColumns = i;
        ListAdapter adapter = getAdapter();
        if (adapter == null || !(adapter instanceof HeaderViewGridAdapter)) {
            return;
        }
        ((HeaderViewGridAdapter) adapter).setNumColumns(i);
    }

    private static class HeaderViewGridAdapter implements WrapperListAdapter, Filterable {
        static final ArrayList<FixedViewInfo> EMPTY_INFO_LIST = new ArrayList<>();
        private final ListAdapter mAdapter;
        boolean mAreAllFixedViewsSelectable;
        ArrayList<FixedViewInfo> mFooterViewInfos;
        ArrayList<FixedViewInfo> mHeaderViewInfos;
        private final boolean mIsFilterable;
        private final DataSetObservable mDataSetObservable = new DataSetObservable();
        private int mNumColumns = 1;
        private int mRowHeight = -1;
        private boolean mCachePlaceHoldView = true;
        private boolean mCacheFirstHeaderView = false;

        public HeaderViewGridAdapter(ArrayList<FixedViewInfo> arrayList, ArrayList<FixedViewInfo> arrayList2, ListAdapter listAdapter) {
            this.mAdapter = listAdapter;
            this.mIsFilterable = listAdapter instanceof Filterable;
            if (arrayList == null) {
                this.mHeaderViewInfos = EMPTY_INFO_LIST;
            } else {
                this.mHeaderViewInfos = arrayList;
            }
            if (arrayList2 == null) {
                this.mFooterViewInfos = EMPTY_INFO_LIST;
            } else {
                this.mFooterViewInfos = arrayList2;
            }
            this.mAreAllFixedViewsSelectable = areAllListInfosSelectable(this.mHeaderViewInfos) && areAllListInfosSelectable(this.mFooterViewInfos);
        }

        public void setNumColumns(int i) {
            if (i >= 1 && this.mNumColumns != i) {
                this.mNumColumns = i;
                notifyDataSetChanged();
            }
        }

        public void setRowHeight(int i) {
            this.mRowHeight = i;
        }

        public int getHeadersCount() {
            return this.mHeaderViewInfos.size();
        }

        public int getFootersCount() {
            return this.mFooterViewInfos.size();
        }

        @Override // android.widget.Adapter
        public boolean isEmpty() {
            ListAdapter listAdapter = this.mAdapter;
            return listAdapter == null || listAdapter.isEmpty();
        }

        private boolean areAllListInfosSelectable(ArrayList<FixedViewInfo> arrayList) {
            if (arrayList == null) {
                return true;
            }
            Iterator<FixedViewInfo> it = arrayList.iterator();
            while (it.hasNext()) {
                if (!it.next().isSelectable) {
                    return false;
                }
            }
            return true;
        }

        public boolean removeHeader(View view) {
            boolean z = false;
            for (int i = 0; i < this.mHeaderViewInfos.size(); i++) {
                if (this.mHeaderViewInfos.get(i).view == view) {
                    this.mHeaderViewInfos.remove(i);
                    if (areAllListInfosSelectable(this.mHeaderViewInfos) && areAllListInfosSelectable(this.mFooterViewInfos)) {
                        z = true;
                    }
                    this.mAreAllFixedViewsSelectable = z;
                    this.mDataSetObservable.notifyChanged();
                    return true;
                }
            }
            return false;
        }

        public boolean removeFooter(View view) {
            boolean z = false;
            for (int i = 0; i < this.mFooterViewInfos.size(); i++) {
                if (this.mFooterViewInfos.get(i).view == view) {
                    this.mFooterViewInfos.remove(i);
                    if (areAllListInfosSelectable(this.mHeaderViewInfos) && areAllListInfosSelectable(this.mFooterViewInfos)) {
                        z = true;
                    }
                    this.mAreAllFixedViewsSelectable = z;
                    this.mDataSetObservable.notifyChanged();
                    return true;
                }
            }
            return false;
        }

        @Override // android.widget.Adapter
        public int getCount() {
            if (this.mAdapter != null) {
                return ((getFootersCount() + getHeadersCount()) * this.mNumColumns) + getAdapterAndPlaceHolderCount();
            }
            return (getFootersCount() + getHeadersCount()) * this.mNumColumns;
        }

        @Override // android.widget.ListAdapter
        public boolean areAllItemsEnabled() {
            ListAdapter listAdapter = this.mAdapter;
            return listAdapter == null || (this.mAreAllFixedViewsSelectable && listAdapter.areAllItemsEnabled());
        }

        private int getAdapterAndPlaceHolderCount() {
            return (int) (Math.ceil((this.mAdapter.getCount() * 1.0f) / this.mNumColumns) * ((double) this.mNumColumns));
        }

        @Override // android.widget.ListAdapter
        public boolean isEnabled(int i) {
            int adapterAndPlaceHolderCount;
            int headersCount = getHeadersCount();
            int i2 = this.mNumColumns;
            int i3 = headersCount * i2;
            if (i < i3) {
                return i % i2 == 0 && this.mHeaderViewInfos.get(i / i2).isSelectable;
            }
            int i4 = i - i3;
            if (this.mAdapter != null) {
                adapterAndPlaceHolderCount = getAdapterAndPlaceHolderCount();
                if (i4 < adapterAndPlaceHolderCount) {
                    return i4 < this.mAdapter.getCount() && this.mAdapter.isEnabled(i4);
                }
            } else {
                adapterAndPlaceHolderCount = 0;
            }
            int i5 = i4 - adapterAndPlaceHolderCount;
            int i6 = this.mNumColumns;
            return i5 % i6 == 0 && this.mFooterViewInfos.get(i5 / i6).isSelectable;
        }

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            int adapterAndPlaceHolderCount;
            int headersCount = getHeadersCount();
            int i2 = this.mNumColumns;
            int i3 = headersCount * i2;
            if (i < i3) {
                if (i % i2 == 0) {
                    return this.mHeaderViewInfos.get(i / i2).data;
                }
                return null;
            }
            int i4 = i - i3;
            if (this.mAdapter != null) {
                adapterAndPlaceHolderCount = getAdapterAndPlaceHolderCount();
                if (i4 < adapterAndPlaceHolderCount) {
                    if (i4 < this.mAdapter.getCount()) {
                        return this.mAdapter.getItem(i4);
                    }
                    return null;
                }
            } else {
                adapterAndPlaceHolderCount = 0;
            }
            int i5 = i4 - adapterAndPlaceHolderCount;
            if (i5 % this.mNumColumns == 0) {
                return this.mFooterViewInfos.get(i5).data;
            }
            return null;
        }

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            int i2;
            int headersCount = getHeadersCount() * this.mNumColumns;
            ListAdapter listAdapter = this.mAdapter;
            if (listAdapter == null || i < headersCount || (i2 = i - headersCount) >= listAdapter.getCount()) {
                return -1L;
            }
            return this.mAdapter.getItemId(i2);
        }

        @Override // android.widget.Adapter
        public boolean hasStableIds() {
            ListAdapter listAdapter = this.mAdapter;
            return listAdapter != null && listAdapter.hasStableIds();
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            int adapterAndPlaceHolderCount = 0;
            if (GridViewWithHeaderAndFooter.DEBUG) {
                Object[] objArr = new Object[2];
                objArr[0] = Integer.valueOf(i);
                objArr[1] = Boolean.valueOf(view == null);
                Log.d(GridViewWithHeaderAndFooter.LOG_TAG, String.format("getView: %s, reused: %s", objArr));
            }
            int headersCount = getHeadersCount();
            int i2 = this.mNumColumns;
            int i3 = headersCount * i2;
            if (i < i3) {
                ViewGroup viewGroup2 = this.mHeaderViewInfos.get(i / i2).viewContainer;
                if (i % this.mNumColumns == 0) {
                    return viewGroup2;
                }
                if (view == null) {
                    view = new View(viewGroup.getContext());
                }
                view.setVisibility(4);
                view.setMinimumHeight(viewGroup2.getHeight());
                return view;
            }
            int i4 = i - i3;
            if (this.mAdapter != null && i4 < (adapterAndPlaceHolderCount = getAdapterAndPlaceHolderCount())) {
                if (i4 < this.mAdapter.getCount()) {
                    return this.mAdapter.getView(i4, view, viewGroup);
                }
                if (view == null) {
                    view = new View(viewGroup.getContext());
                }
                view.setVisibility(4);
                view.setMinimumHeight(this.mRowHeight);
                return view;
            }
            int i5 = i4 - adapterAndPlaceHolderCount;
            if (i5 < getCount()) {
                ViewGroup viewGroup3 = this.mFooterViewInfos.get(i5 / this.mNumColumns).viewContainer;
                if (i % this.mNumColumns == 0) {
                    return viewGroup3;
                }
                if (view == null) {
                    view = new View(viewGroup.getContext());
                }
                view.setVisibility(4);
                view.setMinimumHeight(viewGroup3.getHeight());
                return view;
            }
            throw new ArrayIndexOutOfBoundsException(i);
        }

        @Override // android.widget.Adapter
        public int getItemViewType(int i) {
            int i2;
            int headersCount = getHeadersCount() * this.mNumColumns;
            ListAdapter listAdapter = this.mAdapter;
            int adapterAndPlaceHolderCount = 0;
            int viewTypeCount = listAdapter == null ? 0 : listAdapter.getViewTypeCount() - 1;
            int size = -2;
            if (this.mCachePlaceHoldView && i < headersCount) {
                if (i == 0 && this.mCacheFirstHeaderView) {
                    size = this.mHeaderViewInfos.size() + viewTypeCount + this.mFooterViewInfos.size() + 1 + 1;
                }
                int i3 = this.mNumColumns;
                if (i % i3 != 0) {
                    size = viewTypeCount + (i / i3) + 1;
                }
            }
            int i4 = i - headersCount;
            if (this.mAdapter != null) {
                adapterAndPlaceHolderCount = getAdapterAndPlaceHolderCount();
                if (i4 >= 0 && i4 < adapterAndPlaceHolderCount) {
                    if (i4 < this.mAdapter.getCount()) {
                        size = this.mAdapter.getItemViewType(i4);
                    } else if (this.mCachePlaceHoldView) {
                        size = this.mHeaderViewInfos.size() + viewTypeCount + 1;
                    }
                }
            }
            if (this.mCachePlaceHoldView && (i2 = i4 - adapterAndPlaceHolderCount) >= 0 && i2 < getCount() && i2 % this.mNumColumns != 0) {
                size = viewTypeCount + this.mHeaderViewInfos.size() + 1 + (i2 / this.mNumColumns) + 1;
            }
            if (GridViewWithHeaderAndFooter.DEBUG) {
                Log.d(GridViewWithHeaderAndFooter.LOG_TAG, String.format("getItemViewType: pos: %s, result: %s", Integer.valueOf(i), Integer.valueOf(size), Boolean.valueOf(this.mCachePlaceHoldView), Boolean.valueOf(this.mCacheFirstHeaderView)));
            }
            return size;
        }

        @Override // android.widget.Adapter
        public int getViewTypeCount() {
            ListAdapter listAdapter = this.mAdapter;
            int viewTypeCount = listAdapter == null ? 1 : listAdapter.getViewTypeCount();
            if (this.mCachePlaceHoldView) {
                int size = this.mHeaderViewInfos.size() + 1 + this.mFooterViewInfos.size();
                if (this.mCacheFirstHeaderView) {
                    size++;
                }
                viewTypeCount += size;
            }
            if (GridViewWithHeaderAndFooter.DEBUG) {
                Log.d(GridViewWithHeaderAndFooter.LOG_TAG, String.format("getViewTypeCount: %s", Integer.valueOf(viewTypeCount)));
            }
            return viewTypeCount;
        }

        @Override // android.widget.Adapter
        public void registerDataSetObserver(DataSetObserver dataSetObserver) {
            this.mDataSetObservable.registerObserver(dataSetObserver);
            ListAdapter listAdapter = this.mAdapter;
            if (listAdapter != null) {
                listAdapter.registerDataSetObserver(dataSetObserver);
            }
        }

        @Override // android.widget.Adapter
        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
            this.mDataSetObservable.unregisterObserver(dataSetObserver);
            ListAdapter listAdapter = this.mAdapter;
            if (listAdapter != null) {
                listAdapter.unregisterDataSetObserver(dataSetObserver);
            }
        }

        @Override // android.widget.Filterable
        public Filter getFilter() {
            if (this.mIsFilterable) {
                return ((Filterable) this.mAdapter).getFilter();
            }
            return null;
        }

        @Override // android.widget.WrapperListAdapter
        public ListAdapter getWrappedAdapter() {
            return this.mAdapter;
        }

        public void notifyDataSetChanged() {
            this.mDataSetObservable.notifyChanged();
        }
    }

    @Override // android.widget.AdapterView
    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
        super.setOnItemClickListener(getItemClickHandler());
    }

    @Override // android.widget.AdapterView
    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
        super.setOnItemLongClickListener(getItemClickHandler());
    }

    private ItemClickHandler getItemClickHandler() {
        if (this.mItemClickHandler == null) {
            this.mItemClickHandler = new ItemClickHandler();
        }
        return this.mItemClickHandler;
    }

    private class ItemClickHandler implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
        private ItemClickHandler() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            int headerViewCount;
            if (GridViewWithHeaderAndFooter.this.mOnItemClickListener == null || (headerViewCount = i - (GridViewWithHeaderAndFooter.this.getHeaderViewCount() * GridViewWithHeaderAndFooter.this.getNumColumnsCompatible())) < 0) {
                return;
            }
            GridViewWithHeaderAndFooter.this.mOnItemClickListener.onItemClick(adapterView, view, headerViewCount, j);
        }

        @Override // android.widget.AdapterView.OnItemLongClickListener
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
            int headerViewCount;
            if (GridViewWithHeaderAndFooter.this.mOnItemLongClickListener == null || (headerViewCount = i - (GridViewWithHeaderAndFooter.this.getHeaderViewCount() * GridViewWithHeaderAndFooter.this.getNumColumnsCompatible())) < 0) {
                return true;
            }
            GridViewWithHeaderAndFooter.this.mOnItemLongClickListener.onItemLongClick(adapterView, view, headerViewCount, j);
            return true;
        }
    }
}
