package com.autonavi.ae.gmap.gesture;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/* JADX INFO: loaded from: classes.dex */
public abstract class TwoFingerGestureDetector extends BaseGestureDetector {
    private float finger0DiffX;
    private float finger0DiffY;
    private float finger1DiffX;
    private float finger1DiffY;
    private float mBottomSlopEdge;
    protected float mCurrFingerDiffX;
    protected float mCurrFingerDiffY;
    private float mCurrLen;
    private final float mEdgeSlop;
    protected float mPrevFingerDiffX;
    protected float mPrevFingerDiffY;
    private float mPrevLen;
    private float mRightSlopEdge;

    @Override // com.autonavi.ae.gmap.gesture.BaseGestureDetector
    protected abstract void handleInProgressEvent(int i, MotionEvent motionEvent);

    @Override // com.autonavi.ae.gmap.gesture.BaseGestureDetector
    protected abstract void handleStartProgressEvent(int i, MotionEvent motionEvent);

    public TwoFingerGestureDetector(Context context) {
        super(context);
        this.finger0DiffX = 0.0f;
        this.finger0DiffY = 0.0f;
        this.finger1DiffX = 0.0f;
        this.finger1DiffY = 0.0f;
        this.mEdgeSlop = ViewConfiguration.get(context).getScaledEdgeSlop();
    }

    @Override // com.autonavi.ae.gmap.gesture.BaseGestureDetector
    protected void updateStateByEvent(MotionEvent motionEvent) {
        super.updateStateByEvent(motionEvent);
        MotionEvent motionEvent2 = this.mPrevEvent;
        int pointerCount = this.mPrevEvent.getPointerCount();
        int pointerCount2 = motionEvent.getPointerCount();
        if (pointerCount2 == 2 && pointerCount2 == pointerCount) {
            this.mCurrLen = -1.0f;
            this.mPrevLen = -1.0f;
            float x = motionEvent2.getX(0);
            float y = motionEvent2.getY(0);
            float x2 = motionEvent2.getX(1);
            float y2 = motionEvent2.getY(1);
            this.mPrevFingerDiffX = x2 - x;
            this.mPrevFingerDiffY = y2 - y;
            float x3 = motionEvent.getX(0);
            float y3 = motionEvent.getY(0);
            float x4 = motionEvent.getX(1);
            float y4 = motionEvent.getY(1);
            this.mCurrFingerDiffX = x4 - x3;
            this.mCurrFingerDiffY = y4 - y3;
            this.finger0DiffX = x3 - x;
            this.finger0DiffY = y3 - y;
            this.finger1DiffX = x4 - x2;
            this.finger1DiffY = y4 - y2;
        }
    }

    public PointF getPointerDelta(int i) {
        if (i == 0) {
            return new PointF(this.finger0DiffX, this.finger0DiffY);
        }
        return new PointF(this.finger1DiffX, this.finger1DiffY);
    }

    public float getCurrentSpan() {
        if (this.mCurrLen == -1.0f) {
            float f = this.mCurrFingerDiffX;
            float f2 = this.mCurrFingerDiffY;
            this.mCurrLen = (float) Math.sqrt((f * f) + (f2 * f2));
        }
        return this.mCurrLen;
    }

    public float getPreviousSpan() {
        if (this.mPrevLen == -1.0f) {
            float f = this.mPrevFingerDiffX;
            float f2 = this.mPrevFingerDiffY;
            this.mPrevLen = (float) Math.sqrt((f * f) + (f2 * f2));
        }
        return this.mPrevLen;
    }

    protected static float getRawX(MotionEvent motionEvent, int i) {
        float x = motionEvent.getX() - motionEvent.getRawX();
        if (i < motionEvent.getPointerCount()) {
            return motionEvent.getX(i) - x;
        }
        return 0.0f;
    }

    protected static float getRawY(MotionEvent motionEvent, int i) {
        float y = motionEvent.getY() - motionEvent.getRawY();
        if (i < motionEvent.getPointerCount()) {
            return motionEvent.getY(i) - y;
        }
        return 0.0f;
    }

    protected boolean isSloppyGesture(MotionEvent motionEvent) {
        DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
        this.mRightSlopEdge = displayMetrics.widthPixels - this.mEdgeSlop;
        float f = displayMetrics.heightPixels;
        float f2 = this.mEdgeSlop;
        float f3 = f - f2;
        this.mBottomSlopEdge = f3;
        float f4 = this.mRightSlopEdge;
        float rawX = motionEvent.getRawX();
        float rawY = motionEvent.getRawY();
        float rawX2 = getRawX(motionEvent, 1);
        float rawY2 = getRawY(motionEvent, 1);
        boolean z = rawX < f2 || rawY < f2 || rawX > f4 || rawY > f3;
        boolean z2 = rawX2 < f2 || rawY2 < f2 || rawX2 > f4 || rawY2 > f3;
        return (z && z2) || z || z2;
    }
}
