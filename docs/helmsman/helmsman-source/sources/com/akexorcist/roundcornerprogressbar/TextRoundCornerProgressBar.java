package com.akexorcist.roundcornerprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar;

/* JADX INFO: loaded from: classes.dex */
public class TextRoundCornerProgressBar extends BaseRoundCornerProgressBar implements ViewTreeObserver.OnGlobalLayoutListener {
    protected static final int DEFAULT_TEXT_MARGIN = 10;
    protected static final int DEFAULT_TEXT_SIZE = 16;
    private int colorTextProgress;
    private String textProgress;
    private int textProgressMargin;
    private int textProgressSize;
    private TextView tvProgress;

    public TextRoundCornerProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public TextRoundCornerProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar
    protected int initLayout() {
        return R.layout.layout_text_round_corner_progress_bar;
    }

    @Override // com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar
    protected void initStyleable(Context context, AttributeSet attributeSet) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.TextRoundCornerProgress);
        this.colorTextProgress = typedArrayObtainStyledAttributes.getColor(R.styleable.TextRoundCornerProgress_rcTextProgressColor, -1);
        this.textProgressSize = (int) typedArrayObtainStyledAttributes.getDimension(R.styleable.TextRoundCornerProgress_rcTextProgressSize, dp2px(16.0f));
        this.textProgressMargin = (int) typedArrayObtainStyledAttributes.getDimension(R.styleable.TextRoundCornerProgress_rcTextProgressMargin, dp2px(10.0f));
        this.textProgress = typedArrayObtainStyledAttributes.getString(R.styleable.TextRoundCornerProgress_rcTextProgress);
        typedArrayObtainStyledAttributes.recycle();
    }

    @Override // com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar
    protected void initView() {
        TextView textView = (TextView) findViewById(R.id.tv_progress);
        this.tvProgress = textView;
        textView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override // com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar
    protected void drawProgress(LinearLayout linearLayout, float f, float f2, float f3, int i, int i2, int i3, boolean z) {
        GradientDrawable gradientDrawableCreateGradientDrawable = createGradientDrawable(i3);
        float f4 = i - (i2 / 2);
        gradientDrawableCreateGradientDrawable.setCornerRadii(new float[]{f4, f4, f4, f4, f4, f4, f4, f4});
        linearLayout.setBackground(gradientDrawableCreateGradientDrawable);
        int i4 = (int) ((f3 - (i2 * 2)) / (f / f2));
        ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();
        layoutParams.width = i4;
        linearLayout.setLayoutParams(layoutParams);
    }

    @Override // com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar
    protected void onViewDraw() {
        drawTextProgress();
        drawTextProgressSize();
        drawTextProgressMargin();
        drawTextProgressPosition();
        drawTextProgressColor();
    }

    private void drawTextProgress() {
        this.tvProgress.setText(this.textProgress);
    }

    private void drawTextProgressColor() {
        this.tvProgress.setTextColor(this.colorTextProgress);
    }

    private void drawTextProgressSize() {
        this.tvProgress.setTextSize(0, this.textProgressSize);
    }

    private void drawTextProgressMargin() {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.tvProgress.getLayoutParams();
        int i = this.textProgressMargin;
        marginLayoutParams.setMargins(i, 0, i, 0);
        this.tvProgress.setLayoutParams(marginLayoutParams);
    }

    private void drawTextProgressPosition() {
        clearTextProgressAlign();
        if (this.tvProgress.getMeasuredWidth() + (getTextProgressMargin() * 2) + this.textProgressMargin < ((int) ((getLayoutWidth() - (getPadding() * 2)) / (getMax() / getProgress())))) {
            alignTextProgressInsideProgress();
        } else {
            alignTextProgressOutsideProgress();
        }
    }

    /* JADX INFO: renamed from: com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar$2, reason: invalid class name */
    class AnonymousClass2 implements ViewTreeObserver.OnGlobalLayoutListener {
        AnonymousClass2() {
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            TextRoundCornerProgressBar.access$000(TextRoundCornerProgressBar.this).getViewTreeObserver().removeOnGlobalLayoutListener(this);
            TextRoundCornerProgressBar.access$000(TextRoundCornerProgressBar.this).setVisibility(0);
        }
    }

    private void clearTextProgressAlign() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.tvProgress.getLayoutParams();
        layoutParams.addRule(5, 0);
        layoutParams.addRule(7, 0);
        layoutParams.addRule(0, 0);
        layoutParams.addRule(1, 0);
        layoutParams.removeRule(16);
        layoutParams.removeRule(17);
        layoutParams.removeRule(18);
        layoutParams.removeRule(19);
        this.tvProgress.setLayoutParams(layoutParams);
    }

    private void alignTextProgressInsideProgress() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.tvProgress.getLayoutParams();
        if (isReverse()) {
            layoutParams.addRule(5, R.id.layout_progress);
            layoutParams.addRule(18, R.id.layout_progress);
        } else {
            layoutParams.addRule(7, R.id.layout_progress);
            layoutParams.addRule(19, R.id.layout_progress);
        }
        this.tvProgress.setLayoutParams(layoutParams);
    }

    private void alignTextProgressOutsideProgress() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.tvProgress.getLayoutParams();
        if (isReverse()) {
            layoutParams.addRule(0, R.id.layout_progress);
            layoutParams.addRule(16, R.id.layout_progress);
        } else {
            layoutParams.addRule(1, R.id.layout_progress);
            layoutParams.addRule(17, R.id.layout_progress);
        }
        this.tvProgress.setLayoutParams(layoutParams);
    }

    public String getProgressText() {
        return this.textProgress;
    }

    public void setProgressText(String str) {
        this.textProgress = str;
        drawTextProgress();
        drawTextProgressPosition();
    }

    @Override // com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar
    public void setProgress(float f) {
        super.setProgress(f);
        drawTextProgressPosition();
    }

    public int getTextProgressColor() {
        return this.colorTextProgress;
    }

    public void setTextProgressColor(int i) {
        this.colorTextProgress = i;
        drawTextProgressColor();
    }

    public int getTextProgressSize() {
        return this.textProgressSize;
    }

    public void setTextProgressSize(int i) {
        this.textProgressSize = i;
        drawTextProgressSize();
        drawTextProgressPosition();
    }

    public int getTextProgressMargin() {
        return this.textProgressMargin;
    }

    public void setTextProgressMargin(int i) {
        this.textProgressMargin = i;
        drawTextProgressMargin();
        drawTextProgressPosition();
    }

    @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
    public void onGlobalLayout() {
        this.tvProgress.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        drawTextProgressPosition();
    }

    @Override // com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar, android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.colorTextProgress = this.colorTextProgress;
        savedState.textProgressSize = this.textProgressSize;
        savedState.textProgressMargin = this.textProgressMargin;
        savedState.textProgress = this.textProgress;
        return savedState;
    }

    @Override // com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar, android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.colorTextProgress = savedState.colorTextProgress;
        this.textProgressSize = savedState.textProgressSize;
        this.textProgressMargin = savedState.textProgressMargin;
        this.textProgress = savedState.textProgress;
    }

    private static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar.SavedState.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        int colorTextProgress;
        String textProgress;
        int textProgressMargin;
        int textProgressSize;

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.colorTextProgress = parcel.readInt();
            this.textProgressSize = parcel.readInt();
            this.textProgressMargin = parcel.readInt();
            this.textProgress = parcel.readString();
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.colorTextProgress);
            parcel.writeInt(this.textProgressSize);
            parcel.writeInt(this.textProgressMargin);
            parcel.writeString(this.textProgress);
        }
    }
}
