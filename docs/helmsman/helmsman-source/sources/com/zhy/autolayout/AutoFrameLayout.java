package com.zhy.autolayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.zhy.autolayout.utils.AutoLayoutHelper;

/* JADX INFO: loaded from: classes2.dex */
public class AutoFrameLayout extends FrameLayout {
    private final AutoLayoutHelper mHelper;

    public AutoFrameLayout(Context context) {
        super(context);
        this.mHelper = new AutoLayoutHelper(this);
    }

    public AutoFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mHelper = new AutoLayoutHelper(this);
    }

    public AutoFrameLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mHelper = new AutoLayoutHelper(this);
    }

    public AutoFrameLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mHelper = new AutoLayoutHelper(this);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        if (!isInEditMode()) {
            this.mHelper.adjustChildren();
        }
        super.onMeasure(i, i2);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
    }

    public static class LayoutParams extends FrameLayout.LayoutParams implements AutoLayoutHelper.AutoLayoutParams {
        private AutoLayoutInfo mAutoLayoutInfo;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.mAutoLayoutInfo = AutoLayoutHelper.getAutoLayoutInfo(context, attributeSet);
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
        }

        public LayoutParams(int i, int i2, int i3) {
            super(i, i2, i3);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }

        public LayoutParams(FrameLayout.LayoutParams layoutParams) {
            super((ViewGroup.MarginLayoutParams) layoutParams);
            this.gravity = layoutParams.gravity;
        }

        public LayoutParams(LayoutParams layoutParams) {
            this((FrameLayout.LayoutParams) layoutParams);
            this.mAutoLayoutInfo = layoutParams.mAutoLayoutInfo;
        }

        @Override // com.zhy.autolayout.utils.AutoLayoutHelper.AutoLayoutParams
        public AutoLayoutInfo getAutoLayoutInfo() {
            return this.mAutoLayoutInfo;
        }
    }
}
