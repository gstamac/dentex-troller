package com.akexorcist.roundcornerprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar;

/* JADX INFO: loaded from: classes.dex */
public class IconRoundCornerProgressBar extends BaseRoundCornerProgressBar implements View.OnClickListener {
    protected static final int DEFAULT_ICON_PADDING_BOTTOM = 0;
    protected static final int DEFAULT_ICON_PADDING_LEFT = 0;
    protected static final int DEFAULT_ICON_PADDING_RIGHT = 0;
    protected static final int DEFAULT_ICON_PADDING_TOP = 0;
    protected static final int DEFAULT_ICON_SIZE = 20;
    private int colorIconBackground;
    private OnIconClickListener iconClickListener;
    private int iconHeight;
    private int iconPadding;
    private int iconPaddingBottom;
    private int iconPaddingLeft;
    private int iconPaddingRight;
    private int iconPaddingTop;
    private int iconResource;
    private int iconSize;
    private int iconWidth;
    private ImageView ivProgressIcon;

    public interface OnIconClickListener {
        void onIconClick();
    }

    public IconRoundCornerProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public IconRoundCornerProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar
    public int initLayout() {
        return R.layout.layout_icon_round_corner_progress_bar;
    }

    @Override // com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar
    protected void initStyleable(Context context, AttributeSet attributeSet) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.IconRoundCornerProgress);
        this.iconResource = typedArrayObtainStyledAttributes.getResourceId(R.styleable.IconRoundCornerProgress_rcIconSrc, R.mipmap.round_corner_progress_icon);
        this.iconSize = (int) typedArrayObtainStyledAttributes.getDimension(R.styleable.IconRoundCornerProgress_rcIconSize, -1.0f);
        this.iconWidth = (int) typedArrayObtainStyledAttributes.getDimension(R.styleable.IconRoundCornerProgress_rcIconWidth, dp2px(20.0f));
        this.iconHeight = (int) typedArrayObtainStyledAttributes.getDimension(R.styleable.IconRoundCornerProgress_rcIconHeight, dp2px(20.0f));
        this.iconPadding = (int) typedArrayObtainStyledAttributes.getDimension(R.styleable.IconRoundCornerProgress_rcIconPadding, -1.0f);
        this.iconPaddingLeft = (int) typedArrayObtainStyledAttributes.getDimension(R.styleable.IconRoundCornerProgress_rcIconPaddingLeft, dp2px(0.0f));
        this.iconPaddingRight = (int) typedArrayObtainStyledAttributes.getDimension(R.styleable.IconRoundCornerProgress_rcIconPaddingRight, dp2px(0.0f));
        this.iconPaddingTop = (int) typedArrayObtainStyledAttributes.getDimension(R.styleable.IconRoundCornerProgress_rcIconPaddingTop, dp2px(0.0f));
        this.iconPaddingBottom = (int) typedArrayObtainStyledAttributes.getDimension(R.styleable.IconRoundCornerProgress_rcIconPaddingBottom, dp2px(0.0f));
        this.colorIconBackground = typedArrayObtainStyledAttributes.getColor(R.styleable.IconRoundCornerProgress_rcIconBackgroundColor, context.getResources().getColor(R.color.round_corner_progress_bar_background_default));
        typedArrayObtainStyledAttributes.recycle();
    }

    @Override // com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar
    protected void initView() {
        ImageView imageView = (ImageView) findViewById(R.id.iv_progress_icon);
        this.ivProgressIcon = imageView;
        imageView.setOnClickListener(this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        OnIconClickListener onIconClickListener;
        if (view.getId() != R.id.iv_progress_icon || (onIconClickListener = this.iconClickListener) == null) {
            return;
        }
        onIconClickListener.onIconClick();
    }

    public void setOnIconClickListener(OnIconClickListener onIconClickListener) {
        this.iconClickListener = onIconClickListener;
    }

    @Override // com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar
    protected void drawProgress(LinearLayout linearLayout, float f, float f2, float f3, int i, int i2, int i3, boolean z) {
        GradientDrawable gradientDrawableCreateGradientDrawable = createGradientDrawable(i3);
        int i4 = i - (i2 / 2);
        if (z && f2 != f) {
            float f4 = i4;
            gradientDrawableCreateGradientDrawable.setCornerRadii(new float[]{f4, f4, f4, f4, f4, f4, f4, f4});
        } else {
            float f5 = i4;
            gradientDrawableCreateGradientDrawable.setCornerRadii(new float[]{0.0f, 0.0f, f5, f5, f5, f5, 0.0f, 0.0f});
        }
        linearLayout.setBackground(gradientDrawableCreateGradientDrawable);
        int width = (int) ((f3 - ((i2 * 2) + this.ivProgressIcon.getWidth())) / (f / f2));
        ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();
        layoutParams.width = width;
        linearLayout.setLayoutParams(layoutParams);
    }

    @Override // com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar
    protected void onViewDraw() {
        drawImageIcon();
        drawImageIconSize();
        drawImageIconPadding();
        drawIconBackgroundColor();
    }

    private void drawImageIcon() {
        this.ivProgressIcon.setImageResource(this.iconResource);
    }

    private void drawImageIconSize() {
        if (this.iconSize == -1) {
            this.ivProgressIcon.setLayoutParams(new LinearLayout.LayoutParams(this.iconWidth, this.iconHeight));
            return;
        }
        ImageView imageView = this.ivProgressIcon;
        int i = this.iconSize;
        imageView.setLayoutParams(new LinearLayout.LayoutParams(i, i));
    }

    private void drawImageIconPadding() {
        int i = this.iconPadding;
        if (i == -1 || i == 0) {
            this.ivProgressIcon.setPadding(this.iconPaddingLeft, this.iconPaddingTop, this.iconPaddingRight, this.iconPaddingBottom);
        } else {
            this.ivProgressIcon.setPadding(i, i, i, i);
        }
        this.ivProgressIcon.invalidate();
    }

    private void drawIconBackgroundColor() {
        GradientDrawable gradientDrawableCreateGradientDrawable = createGradientDrawable(this.colorIconBackground);
        float radius = getRadius() - (getPadding() / 2);
        gradientDrawableCreateGradientDrawable.setCornerRadii(new float[]{radius, radius, 0.0f, 0.0f, 0.0f, 0.0f, radius, radius});
        this.ivProgressIcon.setBackground(gradientDrawableCreateGradientDrawable);
    }

    public int getIconImageResource() {
        return this.iconResource;
    }

    public void setIconImageResource(int i) {
        this.iconResource = i;
        drawImageIcon();
    }

    public int getIconSize() {
        return this.iconSize;
    }

    public void setIconSize(int i) {
        if (i >= 0) {
            this.iconSize = i;
        }
        drawImageIconSize();
    }

    public int getIconPadding() {
        return this.iconPadding;
    }

    public void setIconPadding(int i) {
        if (i >= 0) {
            this.iconPadding = i;
        }
        drawImageIconPadding();
    }

    public int getIconPaddingLeft() {
        return this.iconPaddingLeft;
    }

    public void setIconPaddingLeft(int i) {
        if (i > 0) {
            this.iconPaddingLeft = i;
        }
        drawImageIconPadding();
    }

    public int getIconPaddingRight() {
        return this.iconPaddingRight;
    }

    public void setIconPaddingRight(int i) {
        if (i > 0) {
            this.iconPaddingRight = i;
        }
        drawImageIconPadding();
    }

    public int getIconPaddingTop() {
        return this.iconPaddingTop;
    }

    public void setIconPaddingTop(int i) {
        if (i > 0) {
            this.iconPaddingTop = i;
        }
        drawImageIconPadding();
    }

    public int getIconPaddingBottom() {
        return this.iconPaddingBottom;
    }

    public void setIconPaddingBottom(int i) {
        if (i > 0) {
            this.iconPaddingBottom = i;
        }
        drawImageIconPadding();
    }

    public int getColorIconBackground() {
        return this.colorIconBackground;
    }

    public void setIconBackgroundColor(int i) {
        this.colorIconBackground = i;
        drawIconBackgroundColor();
    }

    @Override // com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar, android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.iconResource = this.iconResource;
        savedState.iconSize = this.iconSize;
        savedState.iconWidth = this.iconWidth;
        savedState.iconHeight = this.iconHeight;
        savedState.iconPadding = this.iconPadding;
        savedState.iconPaddingLeft = this.iconPaddingLeft;
        savedState.iconPaddingRight = this.iconPaddingRight;
        savedState.iconPaddingTop = this.iconPaddingTop;
        savedState.iconPaddingBottom = this.iconPaddingBottom;
        savedState.colorIconBackground = this.colorIconBackground;
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
        this.iconResource = savedState.iconResource;
        this.iconSize = savedState.iconSize;
        this.iconWidth = savedState.iconWidth;
        this.iconHeight = savedState.iconHeight;
        this.iconPadding = savedState.iconPadding;
        this.iconPaddingLeft = savedState.iconPaddingLeft;
        this.iconPaddingRight = savedState.iconPaddingRight;
        this.iconPaddingTop = savedState.iconPaddingTop;
        this.iconPaddingBottom = savedState.iconPaddingBottom;
        this.colorIconBackground = savedState.colorIconBackground;
    }

    private static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar.SavedState.1
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
        int colorIconBackground;
        int iconHeight;
        int iconPadding;
        int iconPaddingBottom;
        int iconPaddingLeft;
        int iconPaddingRight;
        int iconPaddingTop;
        int iconResource;
        int iconSize;
        int iconWidth;

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.iconResource = parcel.readInt();
            this.iconSize = parcel.readInt();
            this.iconWidth = parcel.readInt();
            this.iconHeight = parcel.readInt();
            this.iconPadding = parcel.readInt();
            this.iconPaddingLeft = parcel.readInt();
            this.iconPaddingRight = parcel.readInt();
            this.iconPaddingTop = parcel.readInt();
            this.iconPaddingBottom = parcel.readInt();
            this.colorIconBackground = parcel.readInt();
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.iconResource);
            parcel.writeInt(this.iconSize);
            parcel.writeInt(this.iconWidth);
            parcel.writeInt(this.iconHeight);
            parcel.writeInt(this.iconPadding);
            parcel.writeInt(this.iconPaddingLeft);
            parcel.writeInt(this.iconPaddingRight);
            parcel.writeInt(this.iconPaddingTop);
            parcel.writeInt(this.iconPaddingBottom);
            parcel.writeInt(this.colorIconBackground);
        }
    }
}
