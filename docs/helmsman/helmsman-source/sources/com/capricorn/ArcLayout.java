package com.capricorn;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;

/* JADX INFO: loaded from: classes.dex */
public class ArcLayout extends ViewGroup {
    public static final float DEFAULT_FROM_DEGREES = 270.0f;
    public static final float DEFAULT_TO_DEGREES = 360.0f;
    private static final int MIN_RADIUS = 100;
    public static boolean isAllAnimaFinish = false;
    public static boolean isClick = true;
    private int mChildPadding;
    private int mChildSize;
    private boolean mExpanded;
    private float mFromDegrees;
    private int mLayoutPadding;
    private int mRadius;
    private float mToDegrees;

    private static int getTransformedIndex(boolean z, int i, int i2) {
        return z ? (i - 1) - i2 : i2;
    }

    public ArcLayout(Context context) {
        super(context);
        this.mChildPadding = 5;
        this.mLayoutPadding = 50;
        this.mFromDegrees = 270.0f;
        this.mToDegrees = 360.0f;
        this.mExpanded = false;
    }

    public ArcLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mChildPadding = 5;
        this.mLayoutPadding = 50;
        this.mFromDegrees = 270.0f;
        this.mToDegrees = 360.0f;
        this.mExpanded = false;
        if (attributeSet != null) {
            TypedArray typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.ArcLayout, 0, 0);
            this.mFromDegrees = typedArrayObtainStyledAttributes.getFloat(R.styleable.ArcLayout_fromDegrees, 270.0f);
            this.mToDegrees = typedArrayObtainStyledAttributes.getFloat(R.styleable.ArcLayout_toDegrees, 360.0f);
            this.mChildSize = Math.max(typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.ArcLayout_childSize, 0), 0);
            typedArrayObtainStyledAttributes.recycle();
        }
    }

    private static int computeRadius(float f, int i, int i2, int i3, int i4) {
        if (i < 2) {
            return i4;
        }
        return Math.max((int) (((double) ((i2 + i3) / 2)) / Math.sin(Math.toRadians((f / (i - 1)) / 2.0f))), i4);
    }

    private static Rect computeChildFrame(int i, int i2, int i3, float f, int i4) {
        double d = i3;
        double d2 = f;
        double dCos = ((double) i) + (Math.cos(Math.toRadians(d2)) * d);
        double dSin = ((double) i2) + (d * Math.sin(Math.toRadians(d2)));
        double d3 = i4 / 2;
        return new Rect((int) (dCos - d3), (int) (dSin - d3), (int) (dCos + d3), (int) (dSin + d3));
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int iComputeRadius = computeRadius(Math.abs(this.mToDegrees - this.mFromDegrees), getChildCount(), this.mChildSize, this.mChildPadding, 100) + 25;
        this.mRadius = iComputeRadius;
        int i3 = (iComputeRadius * 2) + this.mChildSize + this.mChildPadding + (this.mLayoutPadding * 2);
        setMeasuredDimension(i3 / 2, i3);
        int childCount = getChildCount();
        for (int i4 = 0; i4 < childCount; i4++) {
            getChildAt(i4).measure(View.MeasureSpec.makeMeasureSpec(this.mChildSize, 1073741824), View.MeasureSpec.makeMeasureSpec(this.mChildSize, 1073741824));
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int width = getWidth() - 56;
        int height = getHeight() / 2;
        int i5 = this.mExpanded ? this.mRadius : 0;
        int childCount = getChildCount();
        float f = this.mToDegrees;
        float f2 = this.mFromDegrees;
        float f3 = (f - f2) / (childCount - 1);
        for (int i6 = 0; i6 < childCount; i6++) {
            Rect rectComputeChildFrame = computeChildFrame(width, height, i5, f2, this.mChildSize);
            f2 += f3;
            getChildAt(i6).layout(rectComputeChildFrame.left, rectComputeChildFrame.top, rectComputeChildFrame.right, rectComputeChildFrame.bottom);
        }
    }

    private static long computeStartOffset(int i, boolean z, int i2, float f, long j, Interpolator interpolator) {
        float f2 = f * j;
        long transformedIndex = (long) (getTransformedIndex(z, i, i2) * f2);
        float f3 = f2 * i;
        return (long) (interpolator.getInterpolation(transformedIndex / f3) * f3);
    }

    private static Animation createExpandAnimation(float f, float f2, float f3, float f4, long j, long j2, Interpolator interpolator) {
        Log.e("f xy", f + "  " + f3);
        Log.e("t xy", f2 + "  " + f4);
        RotateAndTranslateAnimation rotateAndTranslateAnimation = new RotateAndTranslateAnimation(f, f2, f3, f4, 0.0f, 720.0f);
        rotateAndTranslateAnimation.setStartOffset(j);
        rotateAndTranslateAnimation.setDuration(j2);
        rotateAndTranslateAnimation.setInterpolator(interpolator);
        rotateAndTranslateAnimation.setFillAfter(true);
        return rotateAndTranslateAnimation;
    }

    private static Animation createShrinkAnimation(float f, float f2, float f3, float f4, long j, long j2, Interpolator interpolator) {
        Log.e("createShrinkAnimation", "createShrinkAnimation");
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.setFillAfter(true);
        long j3 = j2 / 2;
        RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 360.0f, 1, 0.5f, 1, 0.5f);
        rotateAnimation.setStartOffset(j);
        rotateAnimation.setDuration(j3);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setFillAfter(true);
        animationSet.addAnimation(rotateAnimation);
        RotateAndTranslateAnimation rotateAndTranslateAnimation = new RotateAndTranslateAnimation(0.0f, f2, 0.0f, f4, 360.0f, 720.0f);
        rotateAndTranslateAnimation.setStartOffset(j + j3);
        rotateAndTranslateAnimation.setDuration(j2 - j3);
        rotateAndTranslateAnimation.setInterpolator(interpolator);
        rotateAndTranslateAnimation.setFillAfter(true);
        animationSet.addAnimation(rotateAndTranslateAnimation);
        return animationSet;
    }

    private float[] startPointOffset(int i, int i2) {
        float[] fArr = new float[2];
        if (i == 0) {
            double d = 62.5f;
            double d2 = i2;
            float fCos = (float) (Math.cos(d) * d2);
            float fSin = (float) (Math.sin(d) * d2);
            float fAbs = Math.abs(fCos);
            float fAbs2 = Math.abs(fSin);
            fArr[0] = fAbs - 90.0f;
            fArr[1] = fAbs2 + 120.0f;
        } else if (i == 1) {
            double d3 = 31.25f;
            double d4 = i2;
            float fCos2 = (float) (Math.cos(d3) * d4);
            float fSin2 = (float) (Math.sin(d3) * d4);
            float fAbs3 = Math.abs(fCos2);
            float fAbs4 = Math.abs(fSin2);
            fArr[0] = fAbs3;
            fArr[1] = fAbs4 + 45.0f;
        } else if (i == 2) {
            double d5 = 31.25f;
            double d6 = i2;
            float fCos3 = (float) (Math.cos(d5) * d6);
            float fSin3 = (float) (Math.sin(d5) * d6);
            float fAbs5 = Math.abs(fCos3);
            float fAbs6 = Math.abs(fSin3) * (-1.0f);
            fArr[0] = fAbs5;
            fArr[1] = fAbs6 - 45.0f;
        } else if (i == 3) {
            double d7 = 62.5f;
            double d8 = i2;
            float fCos4 = (float) (Math.cos(d7) * d8);
            float fSin4 = (float) (Math.sin(d7) * d8);
            float fAbs7 = Math.abs(fCos4);
            float fAbs8 = Math.abs(fSin4) * (-1.0f);
            fArr[0] = fAbs7 - 80.0f;
            fArr[1] = fAbs8 - 80.0f;
        }
        return fArr;
    }

    private void bindChildAnimation(View view, int i, long j) {
        boolean z = this.mExpanded;
        int width = getWidth() - 56;
        int height = getHeight() / 2;
        int i2 = this.mRadius;
        int childCount = getChildCount();
        float f = this.mToDegrees;
        float f2 = this.mFromDegrees;
        int i3 = childCount - 1;
        Rect rectComputeChildFrame = computeChildFrame(width, height, i2, f2 + (i * ((f - f2) / i3)), this.mChildSize);
        int left = rectComputeChildFrame.left - view.getLeft();
        int top = rectComputeChildFrame.top - view.getTop();
        Log.e("toXDelta", left + "  " + rectComputeChildFrame.left + "   " + view.getLeft());
        Log.e("toYDelta", top + "  " + rectComputeChildFrame.top + "   " + view.getTop());
        Interpolator accelerateInterpolator = this.mExpanded ? new AccelerateInterpolator() : new OvershootInterpolator(1.5f);
        long jComputeStartOffset = computeStartOffset(childCount, this.mExpanded, i, 0.1f, j, accelerateInterpolator);
        float[] fArrStartPointOffset = startPointOffset(i, i2);
        Animation animationCreateShrinkAnimation = this.mExpanded ? createShrinkAnimation(0.0f, left, 0.0f, top, jComputeStartOffset, j, accelerateInterpolator) : createExpandAnimation(fArrStartPointOffset[0], 0.0f, fArrStartPointOffset[1], 0.0f, jComputeStartOffset, j, accelerateInterpolator);
        final boolean z2 = getTransformedIndex(z, childCount, i) == i3;
        animationCreateShrinkAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.capricorn.ArcLayout.1
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                Log.e("islast", z2 + "");
                if (z2) {
                    ArcLayout.this.postDelayed(new Runnable() { // from class: com.capricorn.ArcLayout.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            ArcLayout.this.onAllAnimationsEnd();
                        }
                    }, 0L);
                }
            }
        });
        view.setAnimation(animationCreateShrinkAnimation);
    }

    public boolean isExpanded() {
        return this.mExpanded;
    }

    public void setArc(float f, float f2) {
        if (this.mFromDegrees == f && this.mToDegrees == f2) {
            return;
        }
        this.mFromDegrees = f;
        this.mToDegrees = f2;
        requestLayout();
    }

    public void setChildSize(int i) {
        if (this.mChildSize == i || i < 0) {
            return;
        }
        this.mChildSize = i;
        requestLayout();
    }

    public int getChildSize() {
        return this.mChildSize;
    }

    public void switchState(boolean z) {
        if (z) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                bindChildAnimation(getChildAt(i), i, 300L);
            }
        }
        this.mExpanded = !this.mExpanded;
        if (!z) {
            requestLayout();
        }
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAllAnimationsEnd() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).clearAnimation();
        }
        isClick = true;
        isAllAnimaFinish = this.mExpanded;
        requestLayout();
    }
}
