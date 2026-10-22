package com.zendesk.sdk.support.help;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

/* JADX INFO: loaded from: classes2.dex */
class SeparatorDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    SeparatorDecoration(Drawable drawable) {
        this.mDivider = drawable;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void onDrawOver(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
        super.onDraw(canvas, recyclerView, state);
        if (recyclerView.getItemAnimator() == null || !recyclerView.getItemAnimator().isRunning()) {
            int childCount = recyclerView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = recyclerView.getChildAt(i);
                if (shouldShowTopSeparator(recyclerView, i)) {
                    int paddingLeft = recyclerView.getPaddingLeft();
                    int width = recyclerView.getWidth() - recyclerView.getPaddingRight();
                    int top = childAt.getTop() + ((RecyclerView.LayoutParams) childAt.getLayoutParams()).topMargin;
                    this.mDivider.setBounds(paddingLeft, top, width, this.mDivider.getIntrinsicHeight() + top);
                    this.mDivider.draw(canvas);
                }
            }
        }
    }

    boolean shouldShowTopSeparator(RecyclerView recyclerView, int i) {
        boolean zIsItemACategory = isItemACategory(recyclerView.getChildViewHolder(recyclerView.getChildAt(i)));
        boolean zIsItemAnExpandedCategory = isItemAnExpandedCategory(recyclerView.getChildViewHolder(recyclerView.getChildAt(i)));
        boolean z = i > 0 && isItemAnUnexpandedCategory(recyclerView.getChildViewHolder(recyclerView.getChildAt(i - 1)));
        if (zIsItemACategory) {
            return zIsItemAnExpandedCategory || !z;
        }
        return false;
    }

    private boolean isItemACategory(RecyclerView.ViewHolder viewHolder) {
        return viewHolder instanceof HelpRecyclerViewAdapter.CategoryViewHolder;
    }

    private boolean isItemAnExpandedCategory(RecyclerView.ViewHolder viewHolder) {
        return (viewHolder instanceof HelpRecyclerViewAdapter.CategoryViewHolder) && ((HelpRecyclerViewAdapter.CategoryViewHolder) viewHolder).isExpanded();
    }

    private boolean isItemAnUnexpandedCategory(RecyclerView.ViewHolder viewHolder) {
        return (viewHolder instanceof HelpRecyclerViewAdapter.CategoryViewHolder) && !((HelpRecyclerViewAdapter.CategoryViewHolder) viewHolder).isExpanded();
    }
}
