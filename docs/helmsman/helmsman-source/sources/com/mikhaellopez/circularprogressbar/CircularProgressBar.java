package com.mikhaellopez.circularprogressbar;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import androidx.core.view.ViewCompat;
import com.google.android.gms.common.ConnectionResult;

/* JADX INFO: loaded from: classes2.dex */
public class CircularProgressBar extends View {
    private int backgroundColor;
    private Paint backgroundPaint;
    private float backgroundStrokeWidth;
    private int color;
    private Paint foregroundPaint;
    private float progress;
    private RectF rectF;
    private int startAngle;
    private float strokeWidth;

    public CircularProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.progress = 0.0f;
        this.strokeWidth = getResources().getDimension(R.dimen.default_stroke_width);
        this.backgroundStrokeWidth = getResources().getDimension(R.dimen.default_background_stroke_width);
        this.color = ViewCompat.MEASURED_STATE_MASK;
        this.backgroundColor = -7829368;
        this.startAngle = -90;
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        this.rectF = new RectF();
        TypedArray typedArrayObtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.CircularProgressBar, 0, 0);
        try {
            this.progress = typedArrayObtainStyledAttributes.getFloat(R.styleable.CircularProgressBar_cpb_progress, this.progress);
            this.strokeWidth = typedArrayObtainStyledAttributes.getDimension(R.styleable.CircularProgressBar_cpb_progressbar_width, this.strokeWidth);
            this.backgroundStrokeWidth = typedArrayObtainStyledAttributes.getDimension(R.styleable.CircularProgressBar_cpb_background_progressbar_width, this.backgroundStrokeWidth);
            this.color = typedArrayObtainStyledAttributes.getInt(R.styleable.CircularProgressBar_cpb_progressbar_color, this.color);
            this.backgroundColor = typedArrayObtainStyledAttributes.getInt(R.styleable.CircularProgressBar_cpb_background_progressbar_color, this.backgroundColor);
            typedArrayObtainStyledAttributes.recycle();
            Paint paint = new Paint(1);
            this.backgroundPaint = paint;
            paint.setColor(this.backgroundColor);
            this.backgroundPaint.setStyle(Paint.Style.STROKE);
            this.backgroundPaint.setStrokeWidth(this.backgroundStrokeWidth);
            Paint paint2 = new Paint(1);
            this.foregroundPaint = paint2;
            paint2.setColor(this.color);
            this.foregroundPaint.setStyle(Paint.Style.STROKE);
            this.foregroundPaint.setStrokeWidth(this.strokeWidth);
        } catch (Throwable th) {
            typedArrayObtainStyledAttributes.recycle();
            throw th;
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawOval(this.rectF, this.backgroundPaint);
        canvas.drawArc(this.rectF, this.startAngle, (this.progress * 360.0f) / 100.0f, false, this.foregroundPaint);
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int iMin = Math.min(getDefaultSize(getSuggestedMinimumWidth(), i), getDefaultSize(getSuggestedMinimumHeight(), i2));
        setMeasuredDimension(iMin, iMin);
        float f = this.strokeWidth;
        float f2 = this.backgroundStrokeWidth;
        if (f <= f2) {
            f = f2;
        }
        float f3 = f / 2.0f;
        float f4 = 0.0f + f3;
        float f5 = iMin - f3;
        this.rectF.set(f4, f4, f5, f5);
    }

    public float getProgress() {
        return this.progress;
    }

    public void setProgress(float f) {
        if (f > 100.0f) {
            f = 100.0f;
        }
        this.progress = f;
        invalidate();
    }

    public float getProgressBarWidth() {
        return this.strokeWidth;
    }

    public void setProgressBarWidth(float f) {
        this.strokeWidth = f;
        this.foregroundPaint.setStrokeWidth(f);
        requestLayout();
        invalidate();
    }

    public float getBackgroundProgressBarWidth() {
        return this.backgroundStrokeWidth;
    }

    public void setBackgroundProgressBarWidth(float f) {
        this.backgroundStrokeWidth = f;
        this.backgroundPaint.setStrokeWidth(f);
        requestLayout();
        invalidate();
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int i) {
        this.color = i;
        this.foregroundPaint.setColor(i);
        invalidate();
        requestLayout();
    }

    public int getBackgroundColor() {
        return this.backgroundColor;
    }

    @Override // android.view.View
    public void setBackgroundColor(int i) {
        this.backgroundColor = i;
        this.backgroundPaint.setColor(i);
        invalidate();
        requestLayout();
    }

    public void setProgressWithAnimation(float f) {
        setProgressWithAnimation(f, ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED);
    }

    public void setProgressWithAnimation(float f, int i) {
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(this, "progress", f);
        objectAnimatorOfFloat.setDuration(i);
        objectAnimatorOfFloat.setInterpolator(new DecelerateInterpolator());
        objectAnimatorOfFloat.start();
    }
}
