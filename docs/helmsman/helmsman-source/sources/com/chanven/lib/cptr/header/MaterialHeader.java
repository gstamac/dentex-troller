package com.chanven.lib.cptr.header;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrUIHandler;
import com.chanven.lib.cptr.PtrUIHandlerHook;
import com.chanven.lib.cptr.indicator.PtrIndicator;

/* JADX INFO: loaded from: classes.dex */
public class MaterialHeader extends View implements PtrUIHandler {
    private MaterialProgressDrawable mDrawable;
    private PtrFrameLayout mPtrFrameLayout;
    private float mScale;
    private Animation mScaleAnimation;

    @Override // com.chanven.lib.cptr.PtrUIHandler
    public void onUIRefreshPrepare(PtrFrameLayout ptrFrameLayout) {
    }

    public MaterialHeader(Context context) {
        super(context);
        this.mScale = 1.0f;
        this.mScaleAnimation = new Animation() { // from class: com.chanven.lib.cptr.header.MaterialHeader.1
            @Override // android.view.animation.Animation
            public void applyTransformation(float f, Transformation transformation) {
                MaterialHeader.this.mScale = 1.0f - f;
                MaterialHeader.this.mDrawable.setAlpha((int) (MaterialHeader.this.mScale * 255.0f));
                MaterialHeader.this.invalidate();
            }
        };
        initView();
    }

    public MaterialHeader(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mScale = 1.0f;
        this.mScaleAnimation = new Animation() { // from class: com.chanven.lib.cptr.header.MaterialHeader.1
            @Override // android.view.animation.Animation
            public void applyTransformation(float f, Transformation transformation) {
                MaterialHeader.this.mScale = 1.0f - f;
                MaterialHeader.this.mDrawable.setAlpha((int) (MaterialHeader.this.mScale * 255.0f));
                MaterialHeader.this.invalidate();
            }
        };
        initView();
    }

    public MaterialHeader(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mScale = 1.0f;
        this.mScaleAnimation = new Animation() { // from class: com.chanven.lib.cptr.header.MaterialHeader.1
            @Override // android.view.animation.Animation
            public void applyTransformation(float f, Transformation transformation) {
                MaterialHeader.this.mScale = 1.0f - f;
                MaterialHeader.this.mDrawable.setAlpha((int) (MaterialHeader.this.mScale * 255.0f));
                MaterialHeader.this.invalidate();
            }
        };
        initView();
    }

    public void setPtrFrameLayout(PtrFrameLayout ptrFrameLayout) {
        final PtrUIHandlerHook ptrUIHandlerHook = new PtrUIHandlerHook() { // from class: com.chanven.lib.cptr.header.MaterialHeader.2
            @Override // java.lang.Runnable
            public void run() {
                MaterialHeader materialHeader = MaterialHeader.this;
                materialHeader.startAnimation(materialHeader.mScaleAnimation);
            }
        };
        this.mScaleAnimation.setDuration(200L);
        this.mScaleAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.chanven.lib.cptr.header.MaterialHeader.3
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                ptrUIHandlerHook.resume();
            }
        });
        this.mPtrFrameLayout = ptrFrameLayout;
        ptrFrameLayout.setRefreshCompleteHook(ptrUIHandlerHook);
    }

    private void initView() {
        MaterialProgressDrawable materialProgressDrawable = new MaterialProgressDrawable(getContext(), this);
        this.mDrawable = materialProgressDrawable;
        materialProgressDrawable.setBackgroundColor(-1);
        this.mDrawable.setCallback(this);
    }

    @Override // android.view.View, android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable drawable) {
        if (drawable == this.mDrawable) {
            invalidate();
        } else {
            super.invalidateDrawable(drawable);
        }
    }

    public void setColorSchemeColors(int[] iArr) {
        this.mDrawable.setColorSchemeColors(iArr);
        invalidate();
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(this.mDrawable.getIntrinsicHeight() + getPaddingTop() + getPaddingBottom(), 1073741824));
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int intrinsicHeight = this.mDrawable.getIntrinsicHeight();
        this.mDrawable.setBounds(0, 0, intrinsicHeight, intrinsicHeight);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        int iSave = canvas.save();
        Rect bounds = this.mDrawable.getBounds();
        canvas.translate(getPaddingLeft() + ((getMeasuredWidth() - this.mDrawable.getIntrinsicWidth()) / 2), getPaddingTop());
        float f = this.mScale;
        canvas.scale(f, f, bounds.exactCenterX(), bounds.exactCenterY());
        this.mDrawable.draw(canvas);
        canvas.restoreToCount(iSave);
    }

    @Override // com.chanven.lib.cptr.PtrUIHandler
    public void onUIReset(PtrFrameLayout ptrFrameLayout) {
        this.mScale = 1.0f;
        this.mDrawable.stop();
    }

    @Override // com.chanven.lib.cptr.PtrUIHandler
    public void onUIRefreshBegin(PtrFrameLayout ptrFrameLayout) {
        this.mDrawable.setAlpha(255);
        this.mDrawable.start();
    }

    @Override // com.chanven.lib.cptr.PtrUIHandler
    public void onUIRefreshComplete(PtrFrameLayout ptrFrameLayout) {
        this.mDrawable.stop();
    }

    @Override // com.chanven.lib.cptr.PtrUIHandler
    public void onUIPositionChange(PtrFrameLayout ptrFrameLayout, boolean z, byte b, PtrIndicator ptrIndicator) {
        float fMin = Math.min(1.0f, ptrIndicator.getCurrentPercent());
        if (b == 2) {
            this.mDrawable.setAlpha((int) (255.0f * fMin));
            this.mDrawable.showArrow(true);
            this.mDrawable.setStartEndTrim(0.0f, Math.min(0.8f, fMin * 0.8f));
            this.mDrawable.setArrowScale(Math.min(1.0f, fMin));
            this.mDrawable.setProgressRotation((((0.4f * fMin) - 0.25f) + (fMin * 2.0f)) * 0.5f);
            invalidate();
        }
    }
}
