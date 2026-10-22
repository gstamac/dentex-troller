package im.helmsman.helmsmanandroid.view;

import android.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import androidx.appcompat.widget.AppCompatImageView;

/* JADX INFO: loaded from: classes2.dex */
public class ProgressImageView extends AppCompatImageView {
    private static final int SMOOTH_ANIM_THRESHOLD = 5;
    private static final String TAG = "ProgressImageView";
    private int mHeight;
    private ValueAnimator mInterAnim;
    private float mInterDelta;
    private boolean mIsSquare;
    private float mMaskAnimDelta;
    private boolean mMaskAnimRunning;
    private int mMaskColor;
    private float mMaxMaskRadius;
    private long mMediumAnimTime;
    private Paint mPaint;
    private float mProgress;
    private ValueAnimator mProgressAnimator;
    private RectF mProgressOval;
    private float mRadius;
    private int mStrokeWidth;
    private int mWidth;

    public ProgressImageView(Context context) {
        super(context);
        init(context, null);
    }

    public ProgressImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public ProgressImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        this.mMediumAnimTime = getContext().getResources().getInteger(R.integer.config_mediumAnimTime);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, im.helmsman.helmsmanandroid.R.styleable.ProgressImageView);
        try {
            this.mProgress = typedArrayObtainStyledAttributes.getInteger(2, 0);
            this.mStrokeWidth = typedArrayObtainStyledAttributes.getDimensionPixelOffset(4, 0);
            this.mRadius = typedArrayObtainStyledAttributes.getDimensionPixelOffset(3, 0) + 2;
            this.mIsSquare = typedArrayObtainStyledAttributes.getBoolean(0, false);
            this.mMaskColor = typedArrayObtainStyledAttributes.getColor(1, Color.argb(180, 0, 0, 0));
            Paint paint = new Paint();
            this.mPaint = paint;
            paint.setColor(this.mMaskColor);
            this.mPaint.setAntiAlias(true);
        } finally {
            typedArrayObtainStyledAttributes.recycle();
        }
    }

    private void initParams() {
        int i;
        if (this.mWidth == 0) {
            this.mWidth = getWidth();
        }
        if (this.mHeight == 0) {
            this.mHeight = getHeight();
        }
        int i2 = this.mWidth;
        if (i2 == 0 || (i = this.mHeight) == 0) {
            return;
        }
        if (this.mRadius == 0.0f) {
            this.mRadius = Math.min(i2, i) / 4.0f;
        }
        if (this.mMaxMaskRadius == 0.0f) {
            int i3 = this.mWidth;
            int i4 = this.mHeight;
            this.mMaxMaskRadius = (float) (Math.sqrt((i3 * i3) + (i4 * i4)) * 0.5d);
        }
        if (this.mProgressOval == null) {
            int i5 = this.mWidth;
            float f = this.mRadius;
            int i6 = this.mStrokeWidth;
            int i7 = this.mHeight;
            this.mProgressOval = new RectF(((i5 / 2.0f) - f) + i6, ((i7 / 2.0f) - f) + i6, ((i5 / 2.0f) + f) - i6, ((i7 / 2.0f) + f) - i6);
        }
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int iSaveLayer = canvas.saveLayer(0.0f, 0.0f, getWidth(), getHeight(), null, 31);
        initParams();
        if (this.mProgress < 100.0f) {
            drawMask(canvas);
            if (this.mProgress == 0.0f) {
                updateInterAnim(canvas);
            } else {
                drawProgress(canvas);
            }
        }
        if (this.mMaskAnimRunning) {
            updateMaskAnim(canvas);
        }
        canvas.restoreToCount(iSaveLayer);
    }

    private void drawMask(Canvas canvas) {
        canvas.drawCircle(this.mWidth / 2.0f, this.mHeight / 2.0f, this.mRadius, this.mPaint);
    }

    private void drawProgress(Canvas canvas) {
        this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        canvas.drawCircle(this.mWidth / 2.0f, this.mHeight / 2.0f, this.mRadius, this.mPaint);
        this.mPaint.setXfermode(null);
        RectF rectF = this.mProgressOval;
        float f = this.mProgress;
        canvas.drawArc(rectF, (-90.0f) + (f * 3.6f), 360.0f - (f * 3.6f), true, this.mPaint);
    }

    private void updateInterAnim(Canvas canvas) {
        this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        canvas.drawCircle(this.mWidth / 2.0f, this.mHeight / 2.0f, this.mRadius, this.mPaint);
        this.mPaint.setXfermode(null);
        canvas.drawCircle(this.mWidth / 2.0f, this.mHeight / 2.0f, this.mRadius - this.mInterDelta, this.mPaint);
    }

    private void updateMaskAnim(Canvas canvas) {
        canvas.drawRect(0.0f, 0.0f, this.mWidth, this.mHeight, this.mPaint);
        this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        canvas.drawCircle(this.mWidth / 2.0f, this.mHeight / 2.0f, this.mRadius + this.mMaskAnimDelta, this.mPaint);
        this.mPaint.setXfermode(null);
    }

    private void startInterAnim(final int i) {
        ValueAnimator valueAnimator = this.mInterAnim;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(0.0f, this.mStrokeWidth);
        this.mInterAnim = valueAnimatorOfFloat;
        valueAnimatorOfFloat.setInterpolator(new DecelerateInterpolator());
        this.mInterAnim.setDuration(100L);
        this.mInterAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: im.helmsman.helmsmanandroid.view.ProgressImageView.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator2) {
                ProgressImageView.this.mInterDelta = ((Float) valueAnimator2.getAnimatedValue()).floatValue();
                ProgressImageView.this.invalidate();
            }
        });
        this.mInterAnim.addListener(new AnimatorListenerAdapter() { // from class: im.helmsman.helmsmanandroid.view.ProgressImageView.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                super.onAnimationStart(animator);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                super.onAnimationCancel(animator);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                int i2 = i;
                if (i2 > 0) {
                    ProgressImageView.this.startProgressAnim(0.0f, i2);
                }
            }
        });
        this.mInterAnim.start();
    }

    public void startProgressAnim(float f, float f2) {
        ValueAnimator valueAnimator = this.mProgressAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        final boolean z = f > f2;
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(f, f2);
        this.mProgressAnimator = valueAnimatorOfFloat;
        valueAnimatorOfFloat.setInterpolator(new DecelerateInterpolator());
        this.mProgressAnimator.setDuration(1500L);
        this.mProgressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: im.helmsman.helmsmanandroid.view.ProgressImageView.3
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator2) {
                ProgressImageView.this.mProgress = ((Float) valueAnimator2.getAnimatedValue()).floatValue();
                if (0.0f < ProgressImageView.this.mProgress && ProgressImageView.this.mProgress < 100.0f) {
                    ProgressImageView.this.invalidate();
                } else {
                    if (ProgressImageView.this.mProgress != 100.0f || z) {
                        return;
                    }
                    ProgressImageView.this.startMaskAnim();
                }
            }
        });
        this.mProgressAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startMaskAnim() {
        ValueAnimator valueAnimator = this.mProgressAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(0.0f, this.mMaxMaskRadius);
        valueAnimatorOfFloat.setInterpolator(new DecelerateInterpolator());
        valueAnimatorOfFloat.setDuration(this.mMediumAnimTime);
        valueAnimatorOfFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: im.helmsman.helmsmanandroid.view.ProgressImageView.4
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator2) {
                ProgressImageView.this.mMaskAnimRunning = true;
                ProgressImageView.this.mMaskAnimDelta = ((Float) valueAnimator2.getAnimatedValue()).floatValue();
                ProgressImageView.this.invalidate();
            }
        });
        valueAnimatorOfFloat.addListener(new AnimatorListenerAdapter() { // from class: im.helmsman.helmsmanandroid.view.ProgressImageView.5
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                super.onAnimationStart(animator);
                ProgressImageView.this.mMaskAnimRunning = true;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                super.onAnimationCancel(animator);
                ProgressImageView.this.mMaskAnimRunning = false;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                ProgressImageView.this.mMaskAnimRunning = false;
            }
        });
        valueAnimatorOfFloat.start();
    }

    public int getStrokeWidth() {
        return this.mStrokeWidth;
    }

    public void setStrokeWidth(int i) {
        this.mStrokeWidth = i;
        this.mProgressOval = null;
        invalidate();
    }

    public float getRadius() {
        return this.mRadius;
    }

    public void setRadius(float f) {
        this.mRadius = f;
        this.mProgressOval = null;
        invalidate();
    }

    public int getMaskColor() {
        return this.mMaskColor;
    }

    public void setMaskColor(int i) {
        this.mMaskColor = i;
        this.mPaint.setColor(i);
        invalidate();
    }

    public int getProgress() {
        return (int) this.mProgress;
    }

    public void setProgress(int i) {
        setProgress(i, true);
        this.mProgress = i;
    }

    public void setProgress(int i, boolean z) {
        if (i > 99) {
            i = 99;
        }
        Log.d(TAG, "setProgress: p:" + i + ",mp:" + this.mProgress);
        float f = i;
        if (Math.abs(f - this.mProgress) > 5.0f && z) {
            float f2 = this.mProgress;
            if (f2 == 0.0f) {
                startInterAnim(i);
                return;
            } else {
                startProgressAnim(f2, f);
                return;
            }
        }
        if (i == 100 && z) {
            this.mProgress = 100.0f;
            startMaskAnim();
        } else {
            this.mProgress = f;
            if (f == 0.0f) {
                this.mInterDelta = 0.0f;
            }
            invalidate();
        }
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.mIsSquare) {
            int size = View.MeasureSpec.getSize(i);
            if (size == 0) {
                size = View.MeasureSpec.getSize(i2);
            }
            setMeasuredDimension(size, size);
        }
    }
}
