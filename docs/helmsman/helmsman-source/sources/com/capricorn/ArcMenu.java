package com.capricorn;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/* JADX INFO: loaded from: classes.dex */
public class ArcMenu extends RelativeLayout {
    private ArcLayout mArcLayout;
    private ImageView mHintView;
    private int newChildSize;

    public ArcMenu(Context context) {
        super(context);
        init(context);
    }

    public ArcMenu(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
        applyAttrs(attributeSet);
    }

    public ImageView getHintView() {
        return this.mHintView;
    }

    public boolean isExpanded() {
        return this.mArcLayout.isExpanded();
    }

    private void init(Context context) {
        ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.arc_menu, this);
        ArcLayout arcLayout = (ArcLayout) findViewById(R.id.item_layout);
        this.mArcLayout = arcLayout;
        arcLayout.setChildSize(112);
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.control_layout);
        viewGroup.setClickable(true);
        viewGroup.setOnTouchListener(new View.OnTouchListener() { // from class: com.capricorn.ArcMenu.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0 && ArcLayout.isClick) {
                    ArcLayout.isClick = false;
                    ArcMenu.this.clickStartAnimation();
                }
                return false;
            }
        });
        this.mHintView = (ImageView) findViewById(R.id.control_hint);
    }

    private int measureHanlder(int i, int i2) {
        return i != Integer.MIN_VALUE ? i2 : this.newChildSize;
    }

    public void clickStartAnimation() {
        this.mHintView.startAnimation(createHintSwitchAnimation(this.mArcLayout.isExpanded()));
        this.mArcLayout.switchState(true);
    }

    private void applyAttrs(AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.ArcLayout, 0, 0);
            this.mArcLayout.setArc(typedArrayObtainStyledAttributes.getFloat(R.styleable.ArcLayout_fromDegrees, 270.0f), typedArrayObtainStyledAttributes.getFloat(R.styleable.ArcLayout_toDegrees, 360.0f));
            int dimensionPixelSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.ArcLayout_childSize, this.mArcLayout.getChildSize());
            this.newChildSize = dimensionPixelSize;
            this.mArcLayout.setChildSize(dimensionPixelSize);
            typedArrayObtainStyledAttributes.recycle();
        }
    }

    public void addItem(View view, View.OnClickListener onClickListener) {
        this.mArcLayout.addView(view);
        view.setOnClickListener(getItemClickListener(onClickListener));
    }

    /* JADX INFO: renamed from: com.capricorn.ArcMenu$2, reason: invalid class name */
    class AnonymousClass2 implements View.OnClickListener {
        final /* synthetic */ View.OnClickListener val$listener;

        AnonymousClass2(View.OnClickListener onClickListener) {
            this.val$listener = onClickListener;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ArcMenu.this.bindItemAnimation(view, true, 400L).setAnimationListener(new Animation.AnimationListener() { // from class: com.capricorn.ArcMenu.2.1
                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationRepeat(Animation animation) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationStart(Animation animation) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationEnd(Animation animation) {
                    ArcMenu.this.postDelayed(new Runnable() { // from class: com.capricorn.ArcMenu.2.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            ArcMenu.this.itemDidDisappear();
                        }
                    }, 0L);
                }
            });
            int childCount = ArcMenu.this.mArcLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = ArcMenu.this.mArcLayout.getChildAt(i);
                if (view != childAt) {
                    ArcMenu.this.bindItemAnimation(childAt, false, 300L);
                }
            }
            ArcMenu.this.mArcLayout.invalidate();
            ArcMenu.this.mHintView.startAnimation(ArcMenu.createHintSwitchAnimation(true));
            View.OnClickListener onClickListener = this.val$listener;
            if (onClickListener != null) {
                onClickListener.onClick(view);
            }
        }
    }

    private View.OnClickListener getItemClickListener(View.OnClickListener onClickListener) {
        return new AnonymousClass2(onClickListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Animation bindItemAnimation(View view, boolean z, long j) {
        Animation animationCreateItemDisapperAnimation = createItemDisapperAnimation(j, z);
        view.setAnimation(animationCreateItemDisapperAnimation);
        return animationCreateItemDisapperAnimation;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void itemDidDisappear() {
        int childCount = this.mArcLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            this.mArcLayout.getChildAt(i).clearAnimation();
        }
        this.mArcLayout.switchState(false);
    }

    private static Animation createItemDisapperAnimation(long j, boolean z) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(new ScaleAnimation(1.0f, z ? 2.0f : 0.0f, 1.0f, z ? 2.0f : 0.0f, 1, 0.5f, 1, 0.5f));
        animationSet.addAnimation(new AlphaAnimation(1.0f, 0.0f));
        animationSet.setDuration(j);
        animationSet.setInterpolator(new DecelerateInterpolator());
        animationSet.setFillAfter(true);
        return animationSet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Animation createHintSwitchAnimation(boolean z) {
        RotateAnimation rotateAnimation = new RotateAnimation(z ? 180.0f : 0.0f, z ? 0.0f : 180.0f, 1, 0.5f, 1, 0.5f);
        rotateAnimation.setStartOffset(0L);
        rotateAnimation.setDuration(200L);
        rotateAnimation.setInterpolator(new DecelerateInterpolator());
        rotateAnimation.setFillAfter(true);
        return rotateAnimation;
    }

    public boolean isAllAnimaFinshed() {
        return ArcLayout.isAllAnimaFinish;
    }

    public void changeVisbility() {
        ArcLayout.isAllAnimaFinish = false;
    }
}
