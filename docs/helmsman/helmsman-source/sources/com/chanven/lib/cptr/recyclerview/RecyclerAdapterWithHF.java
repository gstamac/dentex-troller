package com.chanven.lib.cptr.recyclerview;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class RecyclerAdapterWithHF extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_FOOTER = 7899;
    public static final int TYPE_HEADER = 7898;
    public static final int TYPE_MANAGER_GRID = 2;
    public static final int TYPE_MANAGER_LINEAR = 1;
    public static final int TYPE_MANAGER_OTHER = 0;
    public static final int TYPE_MANAGER_STAGGERED_GRID = 3;
    private RecyclerView.AdapterDataObserver adapterDataObserver;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;
    private int mManagerType;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private List<View> mHeaders = new ArrayList();
    private List<View> mFooters = new ArrayList();

    public interface OnItemClickListener {
        void onItemClick(RecyclerAdapterWithHF recyclerAdapterWithHF, RecyclerView.ViewHolder viewHolder, int i);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(RecyclerAdapterWithHF recyclerAdapterWithHF, RecyclerView.ViewHolder viewHolder, int i);
    }

    protected void onItemClick(RecyclerView.ViewHolder viewHolder, int i) {
    }

    protected void onItemLongClick(RecyclerView.ViewHolder viewHolder, int i) {
    }

    public int getHeadSize() {
        return this.mHeaders.size();
    }

    public int getFootSize() {
        return this.mFooters.size();
    }

    public int getManagerType() {
        return this.mManagerType;
    }

    public void notifyDataSetChangedHF() {
        notifyDataSetChanged();
    }

    public void notifyItemChangedHF(int i) {
        notifyItemChanged(getRealPosition(i));
    }

    public void notifyItemMovedHF(int i, int i2) {
        notifyItemMovedHF(getRealPosition(i), getRealPosition(i2));
    }

    public void notifyItemRangeChangedHF(int i, int i2) {
        notifyItemRangeChanged(getRealPosition(i), i2);
    }

    public void notifyItemRangeRemovedHF(int i, int i2) {
        notifyItemRangeRemoved(getRealPosition(i), i2);
    }

    public void notifyItemRemovedHF(int i) {
        notifyItemRemoved(getRealPosition(i));
    }

    public void notifyItemInsertedHF(int i) {
        notifyItemInserted(getRealPosition(i));
    }

    public void notifyItemRangeInsertedHF(int i, int i2) {
        notifyItemRangeInserted(getRealPosition(i), i2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final long getItemId(int i) {
        return getItemIdHF(getRealPosition(i));
    }

    public long getItemIdHF(int i) {
        return this.mAdapter.getItemId(i);
    }

    public RecyclerView.ViewHolder onCreateViewHolderHF(ViewGroup viewGroup, int i) {
        return this.mAdapter.onCreateViewHolder(viewGroup, i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i != 7898 && i != 7899) {
            return onCreateViewHolderHF(viewGroup, i);
        }
        FrameLayout frameLayout = new FrameLayout(viewGroup.getContext());
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        return new HeaderFooterViewHolder(frameLayout);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (isHeader(i)) {
            prepareHeaderFooter((HeaderFooterViewHolder) viewHolder, this.mHeaders.get(i));
        } else if (isFooter(i)) {
            prepareHeaderFooter((HeaderFooterViewHolder) viewHolder, this.mFooters.get((i - getItemCountHF()) - this.mHeaders.size()));
        } else {
            viewHolder.itemView.setOnClickListener(new MyOnClickListener(viewHolder));
            viewHolder.itemView.setOnLongClickListener(new MyOnLongClickListener(viewHolder));
            onBindViewHolderHF(viewHolder, getRealPosition(i));
        }
    }

    public int getRealPosition(int i) {
        return i - this.mHeaders.size();
    }

    public void onBindViewHolderHF(RecyclerView.ViewHolder viewHolder, int i) {
        this.mAdapter.onBindViewHolder(viewHolder, i);
    }

    private void prepareHeaderFooter(HeaderFooterViewHolder headerFooterViewHolder, View view) {
        if (this.mManagerType == 3) {
            StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(-1, -1);
            layoutParams.setFullSpan(true);
            headerFooterViewHolder.itemView.setLayoutParams(layoutParams);
        }
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        headerFooterViewHolder.base.removeAllViews();
        headerFooterViewHolder.base.addView(view);
    }

    private boolean isHeader(int i) {
        return i < this.mHeaders.size();
    }

    private boolean isFooter(int i) {
        return i >= this.mHeaders.size() + getItemCountHF();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final int getItemCount() {
        return this.mHeaders.size() + getItemCountHF() + this.mFooters.size();
    }

    public int getItemCountHF() {
        return this.mAdapter.getItemCount();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final int getItemViewType(int i) {
        if (isHeader(i)) {
            return TYPE_HEADER;
        }
        if (isFooter(i)) {
            return TYPE_FOOTER;
        }
        int itemViewTypeHF = getItemViewTypeHF(getRealPosition(i));
        if (itemViewTypeHF == 7898 || itemViewTypeHF == 7899) {
            throw new IllegalArgumentException("Item type cannot equal 7898 or 7899");
        }
        return itemViewTypeHF;
    }

    public int getItemViewTypeHF(int i) {
        return this.mAdapter.getItemViewType(i);
    }

    public void addHeader(View view) {
        if (this.mHeaders.contains(view)) {
            return;
        }
        this.mHeaders.add(view);
        notifyItemInserted(this.mHeaders.size() - 1);
    }

    public void removeHeader(View view) {
        if (this.mHeaders.contains(view)) {
            notifyItemRemoved(this.mHeaders.indexOf(view));
            this.mHeaders.remove(view);
        }
    }

    public void addFooter(View view) {
        if (this.mFooters.contains(view)) {
            return;
        }
        this.mFooters.add(view);
        notifyItemInserted(((this.mHeaders.size() + getItemCountHF()) + this.mFooters.size()) - 1);
    }

    public void removeFooter(View view) {
        if (this.mFooters.contains(view)) {
            notifyItemRemoved(this.mHeaders.size() + getItemCountHF() + this.mFooters.indexOf(view));
            this.mFooters.remove(view);
        }
    }

    public static class HeaderFooterViewHolder extends RecyclerView.ViewHolder {
        FrameLayout base;

        public HeaderFooterViewHolder(View view) {
            super(view);
            this.base = (FrameLayout) view;
        }
    }

    public OnItemClickListener getOnItemClickListener() {
        return this.onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        Log.d("eeee", "setOnItemClickListener " + this.onItemClickListener);
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return this.onItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    private class MyOnClickListener implements View.OnClickListener {
        private RecyclerView.ViewHolder vh;

        public MyOnClickListener(RecyclerView.ViewHolder viewHolder) {
            this.vh = viewHolder;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int realPosition = RecyclerAdapterWithHF.this.getRealPosition(this.vh.getLayoutPosition());
            if (RecyclerAdapterWithHF.this.onItemClickListener != null) {
                RecyclerAdapterWithHF.this.onItemClickListener.onItemClick(RecyclerAdapterWithHF.this, this.vh, realPosition);
            }
            RecyclerAdapterWithHF.this.onItemClick(this.vh, realPosition);
        }
    }

    private class MyOnLongClickListener implements View.OnLongClickListener {
        private RecyclerView.ViewHolder vh;

        public MyOnLongClickListener(RecyclerView.ViewHolder viewHolder) {
            this.vh = viewHolder;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            int realPosition = RecyclerAdapterWithHF.this.getRealPosition(this.vh.getLayoutPosition());
            if (RecyclerAdapterWithHF.this.onItemLongClickListener != null) {
                RecyclerAdapterWithHF.this.onItemLongClickListener.onItemLongClick(RecyclerAdapterWithHF.this, this.vh, realPosition);
            }
            RecyclerAdapterWithHF.this.onItemLongClick(this.vh, realPosition);
            return true;
        }
    }

    public RecyclerAdapterWithHF(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        RecyclerView.AdapterDataObserver adapterDataObserver = new RecyclerView.AdapterDataObserver() { // from class: com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF.1
            @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
            public void onChanged() {
                RecyclerAdapterWithHF.this.notifyDataSetChanged();
            }

            @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
            public void onItemRangeChanged(int i, int i2) {
                RecyclerAdapterWithHF recyclerAdapterWithHF = RecyclerAdapterWithHF.this;
                recyclerAdapterWithHF.notifyItemRangeChanged(i + recyclerAdapterWithHF.getHeadSize(), i2);
            }

            @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
            public void onItemRangeInserted(int i, int i2) {
                RecyclerAdapterWithHF recyclerAdapterWithHF = RecyclerAdapterWithHF.this;
                recyclerAdapterWithHF.notifyItemRangeInserted(i + recyclerAdapterWithHF.getHeadSize(), i2);
            }

            @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
            public void onItemRangeRemoved(int i, int i2) {
                RecyclerAdapterWithHF recyclerAdapterWithHF = RecyclerAdapterWithHF.this;
                recyclerAdapterWithHF.notifyItemRangeRemoved(i + recyclerAdapterWithHF.getHeadSize(), i2);
            }

            @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
            public void onItemRangeMoved(int i, int i2, int i3) {
                RecyclerAdapterWithHF recyclerAdapterWithHF = RecyclerAdapterWithHF.this;
                recyclerAdapterWithHF.notifyItemMoved(i + recyclerAdapterWithHF.getHeadSize(), i2 + RecyclerAdapterWithHF.this.getHeadSize());
            }
        };
        this.adapterDataObserver = adapterDataObserver;
        this.mAdapter = adapter;
        adapter.registerAdapterDataObserver(adapterDataObserver);
    }
}
