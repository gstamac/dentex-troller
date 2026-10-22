package com.akexorcist.roundcornerprogressbar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/* JADX INFO: loaded from: classes.dex */
public class RoundCornerTextView extends TextView {
    private OnSizeChangedListener sizeChangedListener;

    public interface OnSizeChangedListener {
        void onTextViewSizeChanged(TextView textView);
    }

    public RoundCornerTextView(Context context) {
        super(context);
    }

    public RoundCornerTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public RoundCornerTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public RoundCornerTextView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        OnSizeChangedListener onSizeChangedListener = this.sizeChangedListener;
        if (onSizeChangedListener != null) {
            onSizeChangedListener.onTextViewSizeChanged(this);
        }
    }

    public void setOnSizeChangedListener(OnSizeChangedListener onSizeChangedListener) {
        this.sizeChangedListener = onSizeChangedListener;
    }
}
