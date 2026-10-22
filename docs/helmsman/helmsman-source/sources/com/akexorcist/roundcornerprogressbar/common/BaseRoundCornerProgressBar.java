package com.akexorcist.roundcornerprogressbar.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.akexorcist.roundcornerprogressbar.R;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseRoundCornerProgressBar extends LinearLayout {
    protected static final int DEFAULT_BACKGROUND_PADDING = 0;
    protected static final int DEFAULT_MAX_PROGRESS = 100;
    protected static final int DEFAULT_PROGRESS = 0;
    protected static final int DEFAULT_PROGRESS_RADIUS = 30;
    protected static final int DEFAULT_SECONDARY_PROGRESS = 0;
    private int colorBackground;
    private int colorProgress;
    private int colorSecondaryProgress;
    private boolean isReverse;
    private LinearLayout layoutBackground;
    private LinearLayout layoutProgress;
    private LinearLayout layoutSecondaryProgress;
    private float max;
    private int padding;
    private float progress;
    private OnProgressChangedListener progressChangedListener;
    private int radius;
    private float secondaryProgress;
    private int totalWidth;

    public interface OnProgressChangedListener {
        void onProgressChanged(int i, float f, boolean z, boolean z2);
    }

    protected abstract void drawProgress(LinearLayout linearLayout, float f, float f2, float f3, int i, int i2, int i3, boolean z);

    protected abstract int initLayout();

    protected abstract void initStyleable(Context context, AttributeSet attributeSet);

    protected abstract void initView();

    protected abstract void onViewDraw();

    public BaseRoundCornerProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (isInEditMode()) {
            previewLayout(context);
        } else {
            setup(context, attributeSet);
        }
    }

    public BaseRoundCornerProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        if (isInEditMode()) {
            previewLayout(context);
        } else {
            setup(context, attributeSet);
        }
    }

    private void previewLayout(Context context) {
        setGravity(17);
        TextView textView = new TextView(context);
        textView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        textView.setGravity(17);
        textView.setText(getClass().getSimpleName());
        textView.setTextColor(-1);
        textView.setBackgroundColor(-7829368);
        addView(textView);
    }

    public void setup(Context context, AttributeSet attributeSet) {
        setupStyleable(context, attributeSet);
        removeAllViews();
        LayoutInflater.from(context).inflate(initLayout(), this);
        this.layoutBackground = (LinearLayout) findViewById(R.id.layout_background);
        this.layoutProgress = (LinearLayout) findViewById(R.id.layout_progress);
        this.layoutSecondaryProgress = (LinearLayout) findViewById(R.id.layout_secondary_progress);
        initView();
    }

    public void setupStyleable(Context context, AttributeSet attributeSet) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.RoundCornerProgress);
        this.radius = (int) typedArrayObtainStyledAttributes.getDimension(R.styleable.RoundCornerProgress_rcRadius, dp2px(30.0f));
        this.padding = (int) typedArrayObtainStyledAttributes.getDimension(R.styleable.RoundCornerProgress_rcBackgroundPadding, dp2px(0.0f));
        this.isReverse = typedArrayObtainStyledAttributes.getBoolean(R.styleable.RoundCornerProgress_rcReverse, false);
        this.max = typedArrayObtainStyledAttributes.getFloat(R.styleable.RoundCornerProgress_rcMax, 100.0f);
        this.progress = typedArrayObtainStyledAttributes.getFloat(R.styleable.RoundCornerProgress_rcProgress, 0.0f);
        this.secondaryProgress = typedArrayObtainStyledAttributes.getFloat(R.styleable.RoundCornerProgress_rcSecondaryProgress, 0.0f);
        this.colorBackground = typedArrayObtainStyledAttributes.getColor(R.styleable.RoundCornerProgress_rcBackgroundColor, context.getResources().getColor(R.color.round_corner_progress_bar_background_default));
        this.colorProgress = typedArrayObtainStyledAttributes.getColor(R.styleable.RoundCornerProgress_rcProgressColor, context.getResources().getColor(R.color.round_corner_progress_bar_progress_default));
        this.colorSecondaryProgress = typedArrayObtainStyledAttributes.getColor(R.styleable.RoundCornerProgress_rcSecondaryProgressColor, context.getResources().getColor(R.color.round_corner_progress_bar_secondary_progress_default));
        typedArrayObtainStyledAttributes.recycle();
        initStyleable(context, attributeSet);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (isInEditMode()) {
            return;
        }
        this.totalWidth = i;
        drawAll();
        postDelayed(new Runnable() { // from class: com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar.1
            @Override // java.lang.Runnable
            public void run() {
                BaseRoundCornerProgressBar.this.drawPrimaryProgress();
                BaseRoundCornerProgressBar.this.drawSecondaryProgress();
            }
        }, 5L);
    }

    protected void drawAll() {
        drawBackgroundProgress();
        drawPadding();
        drawProgressReverse();
        drawPrimaryProgress();
        drawSecondaryProgress();
        onViewDraw();
    }

    private void drawBackgroundProgress() {
        GradientDrawable gradientDrawableCreateGradientDrawable = createGradientDrawable(this.colorBackground);
        float f = this.radius - (this.padding / 2);
        gradientDrawableCreateGradientDrawable.setCornerRadii(new float[]{f, f, f, f, f, f, f, f});
        this.layoutBackground.setBackground(gradientDrawableCreateGradientDrawable);
    }

    protected GradientDrawable createGradientDrawable(int i) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setColor(i);
        return gradientDrawable;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void drawPrimaryProgress() {
        drawProgress(this.layoutProgress, this.max, this.progress, this.totalWidth, this.radius, this.padding, this.colorProgress, this.isReverse);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void drawSecondaryProgress() {
        drawProgress(this.layoutSecondaryProgress, this.max, this.secondaryProgress, this.totalWidth, this.radius, this.padding, this.colorSecondaryProgress, this.isReverse);
    }

    private void drawProgressReverse() {
        setupReverse(this.layoutProgress);
        setupReverse(this.layoutSecondaryProgress);
    }

    private void setupReverse(LinearLayout linearLayout) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) linearLayout.getLayoutParams();
        removeLayoutParamsRule(layoutParams);
        if (this.isReverse) {
            layoutParams.addRule(11);
            layoutParams.addRule(21);
        } else {
            layoutParams.addRule(9);
            layoutParams.addRule(20);
        }
        linearLayout.setLayoutParams(layoutParams);
    }

    private void drawPadding() {
        LinearLayout linearLayout = this.layoutBackground;
        int i = this.padding;
        linearLayout.setPadding(i, i, i, i);
    }

    private void removeLayoutParamsRule(RelativeLayout.LayoutParams layoutParams) {
        layoutParams.removeRule(11);
        layoutParams.removeRule(21);
        layoutParams.removeRule(9);
        layoutParams.removeRule(20);
    }

    protected float dp2px(float f) {
        return Math.round(f * (getContext().getResources().getDisplayMetrics().densityDpi / 160));
    }

    public boolean isReverse() {
        return this.isReverse;
    }

    public void setReverse(boolean z) {
        this.isReverse = z;
        drawProgressReverse();
        drawPrimaryProgress();
        drawSecondaryProgress();
    }

    public int getRadius() {
        return this.radius;
    }

    public void setRadius(int i) {
        if (i >= 0) {
            this.radius = i;
        }
        drawBackgroundProgress();
        drawPrimaryProgress();
        drawSecondaryProgress();
    }

    public int getPadding() {
        return this.padding;
    }

    public void setPadding(int i) {
        if (i >= 0) {
            this.padding = i;
        }
        drawPadding();
        drawPrimaryProgress();
        drawSecondaryProgress();
    }

    public float getMax() {
        return this.max;
    }

    public void setMax(float f) {
        if (f >= 0.0f) {
            this.max = f;
        }
        if (this.progress > f) {
            this.progress = f;
        }
        drawPrimaryProgress();
        drawSecondaryProgress();
    }

    public float getLayoutWidth() {
        return this.totalWidth;
    }

    public float getProgress() {
        return this.progress;
    }

    public void setProgress(float f) {
        if (f < 0.0f) {
            this.progress = 0.0f;
        } else {
            float f2 = this.max;
            if (f > f2) {
                this.progress = f2;
            } else {
                this.progress = f;
            }
        }
        drawPrimaryProgress();
        OnProgressChangedListener onProgressChangedListener = this.progressChangedListener;
        if (onProgressChangedListener != null) {
            onProgressChangedListener.onProgressChanged(getId(), this.progress, true, false);
        }
    }

    public float getSecondaryProgressWidth() {
        LinearLayout linearLayout = this.layoutSecondaryProgress;
        if (linearLayout != null) {
            return linearLayout.getWidth();
        }
        return 0.0f;
    }

    public float getSecondaryProgress() {
        return this.secondaryProgress;
    }

    public void setSecondaryProgress(float f) {
        if (f < 0.0f) {
            this.secondaryProgress = 0.0f;
        } else {
            float f2 = this.max;
            if (f > f2) {
                this.secondaryProgress = f2;
            } else {
                this.secondaryProgress = f;
            }
        }
        drawSecondaryProgress();
        OnProgressChangedListener onProgressChangedListener = this.progressChangedListener;
        if (onProgressChangedListener != null) {
            onProgressChangedListener.onProgressChanged(getId(), this.secondaryProgress, false, true);
        }
    }

    public int getProgressBackgroundColor() {
        return this.colorBackground;
    }

    public void setProgressBackgroundColor(int i) {
        this.colorBackground = i;
        drawBackgroundProgress();
    }

    public int getProgressColor() {
        return this.colorProgress;
    }

    public void setProgressColor(int i) {
        this.colorProgress = i;
        drawPrimaryProgress();
    }

    public int getSecondaryProgressColor() {
        return this.colorSecondaryProgress;
    }

    public void setSecondaryProgressColor(int i) {
        this.colorSecondaryProgress = i;
        drawSecondaryProgress();
    }

    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        this.progressChangedListener = onProgressChangedListener;
    }

    @Override // android.view.View
    public void invalidate() {
        super.invalidate();
        drawAll();
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.radius = this.radius;
        savedState.padding = this.padding;
        savedState.colorBackground = this.colorBackground;
        savedState.colorProgress = this.colorProgress;
        savedState.colorSecondaryProgress = this.colorSecondaryProgress;
        savedState.max = this.max;
        savedState.progress = this.progress;
        savedState.secondaryProgress = this.secondaryProgress;
        savedState.isReverse = this.isReverse;
        return savedState;
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.radius = savedState.radius;
        this.padding = savedState.padding;
        this.colorBackground = savedState.colorBackground;
        this.colorProgress = savedState.colorProgress;
        this.colorSecondaryProgress = savedState.colorSecondaryProgress;
        this.max = savedState.max;
        this.progress = savedState.progress;
        this.secondaryProgress = savedState.secondaryProgress;
        this.isReverse = savedState.isReverse;
    }

    private static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar.SavedState.1
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
        int colorBackground;
        int colorProgress;
        int colorSecondaryProgress;
        boolean isReverse;
        float max;
        int padding;
        float progress;
        int radius;
        float secondaryProgress;

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.max = parcel.readFloat();
            this.progress = parcel.readFloat();
            this.secondaryProgress = parcel.readFloat();
            this.radius = parcel.readInt();
            this.padding = parcel.readInt();
            this.colorBackground = parcel.readInt();
            this.colorProgress = parcel.readInt();
            this.colorSecondaryProgress = parcel.readInt();
            this.isReverse = parcel.readByte() != 0;
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeFloat(this.max);
            parcel.writeFloat(this.progress);
            parcel.writeFloat(this.secondaryProgress);
            parcel.writeInt(this.radius);
            parcel.writeInt(this.padding);
            parcel.writeInt(this.colorBackground);
            parcel.writeInt(this.colorProgress);
            parcel.writeInt(this.colorSecondaryProgress);
            parcel.writeByte(this.isReverse ? (byte) 1 : (byte) 0);
        }
    }
}
