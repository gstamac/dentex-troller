package com.tt.whorlviewlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/* JADX INFO: loaded from: classes2.dex */
public class WhorlView extends View {
    public static final int FAST = 1;
    public static final int MEDIUM = 0;
    private static final int PARALLAX_FAST = 60;
    private static final int PARALLAX_MEDIUM = 72;
    private static final int PARALLAX_SLOW = 90;
    private static final long REFRESH_DURATION = 16;
    public static final int SLOW = 2;
    private static final String SPLI_CHAR = "_";
    private Paint mArcPaint;
    private int mCircleSpeed;
    private long mCircleTime;
    private float mIntervalWidth;
    private boolean mIsCircling;
    private int[] mLayerColors;
    private RectF mOval;
    private int mParallaxSpeed;
    private float mStrokeWidth;
    private float mSweepAngle;

    public WhorlView(Context context) {
        this(context, null, 0);
    }

    public WhorlView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public WhorlView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mIsCircling = false;
        if (attributeSet != null) {
            TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.whorlview_style);
            String string = typedArrayObtainStyledAttributes.getString(R.styleable.whorlview_style_whorlview_circle_colors);
            parseStringToLayerColors(TextUtils.isEmpty(string) ? "#F44336_#4CAF50_#5677fc" : string);
            this.mCircleSpeed = typedArrayObtainStyledAttributes.getInt(R.styleable.whorlview_style_whorlview_circle_speed, 270);
            setParallax(typedArrayObtainStyledAttributes.getInt(R.styleable.whorlview_style_whorlview_parallax, 0));
            float f = typedArrayObtainStyledAttributes.getFloat(R.styleable.whorlview_style_whorlview_sweepAngle, 90.0f);
            this.mSweepAngle = f;
            if (f <= 0.0f || f >= 360.0f) {
                throw new IllegalArgumentException("sweep angle out of bound");
            }
            this.mStrokeWidth = typedArrayObtainStyledAttributes.getFloat(R.styleable.whorlview_style_whorlview_strokeWidth, 5.0f);
            typedArrayObtainStyledAttributes.recycle();
            return;
        }
        parseStringToLayerColors("#F44336_#4CAF50_#5677fc");
        this.mCircleSpeed = 270;
        this.mParallaxSpeed = 72;
        this.mSweepAngle = 90.0f;
        this.mStrokeWidth = 5.0f;
    }

    private void parseStringToLayerColors(String str) {
        String[] strArrSplit = str.split(SPLI_CHAR);
        this.mLayerColors = new int[strArrSplit.length];
        for (int i = 0; i < strArrSplit.length; i++) {
            try {
                this.mLayerColors[i] = Color.parseColor(strArrSplit[i]);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("whorlview_circle_colors can not be parsed | " + e.getLocalizedMessage());
            }
        }
    }

    private void setParallax(int i) {
        if (i == 0) {
            this.mParallaxSpeed = 72;
        } else if (i == 1) {
            this.mParallaxSpeed = 60;
        } else {
            if (i == 2) {
                this.mParallaxSpeed = 90;
                return;
            }
            throw new IllegalStateException("no such parallax type");
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < this.mLayerColors.length; i++) {
            drawArc(canvas, i, ((long) (this.mCircleSpeed + (this.mParallaxSpeed * i))) * this.mCircleTime * 0.001f);
        }
    }

    public void start() {
        this.mIsCircling = true;
        new Thread(new Runnable() { // from class: com.tt.whorlviewlibrary.WhorlView.1
            @Override // java.lang.Runnable
            public void run() {
                WhorlView.this.mCircleTime = 0L;
                while (WhorlView.this.mIsCircling) {
                    WhorlView.this.invalidateWrap();
                    WhorlView.this.mCircleTime += 16;
                    try {
                        Thread.sleep(16L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void stop() {
        this.mIsCircling = false;
        this.mCircleTime = 0L;
        invalidateWrap();
    }

    public boolean isCircling() {
        return this.mIsCircling;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invalidateWrap() {
        postInvalidateOnAnimation();
    }

    private void drawArc(Canvas canvas, int i, float f) {
        canvas.drawArc(checkRectF(i), f, this.mSweepAngle, false, checkArcPaint(i));
    }

    private Paint checkArcPaint(int i) {
        Paint paint = this.mArcPaint;
        if (paint == null) {
            this.mArcPaint = new Paint();
        } else {
            paint.reset();
        }
        this.mArcPaint.setColor(this.mLayerColors[i]);
        this.mArcPaint.setStyle(Paint.Style.STROKE);
        this.mArcPaint.setStrokeWidth(this.mStrokeWidth);
        this.mArcPaint.setAntiAlias(true);
        return this.mArcPaint;
    }

    private RectF checkRectF(int i) {
        if (this.mOval == null) {
            this.mOval = new RectF();
        }
        float f = this.mStrokeWidth;
        float f2 = (i * (this.mIntervalWidth + f)) + (f / 2.0f);
        float minLength = getMinLength() - f2;
        this.mOval.set(f2, f2, minLength, minLength);
        return this.mOval;
    }

    private int getMinLength() {
        return Math.min(getWidth(), getHeight());
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        float f = this.mStrokeWidth;
        int[] iArr = this.mLayerColors;
        int iMeasureSize = measureSize(i, (int) ((8.0f * f * iArr.length) + f), (int) ((4.0f * f * iArr.length) + f));
        calculateIntervalWidth(iMeasureSize);
        setMeasuredDimension(iMeasureSize, iMeasureSize);
    }

    private void calculateIntervalWidth(int i) {
        this.mIntervalWidth = (i / (this.mLayerColors.length * 2)) - this.mStrokeWidth;
    }

    public static int measureSize(int i, int i2, int i3) {
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        if (mode == 1073741824) {
            i2 = size;
        } else if (mode == Integer.MIN_VALUE) {
            i2 = Math.min(i2, size);
        }
        return Math.max(i2, i3);
    }
}
