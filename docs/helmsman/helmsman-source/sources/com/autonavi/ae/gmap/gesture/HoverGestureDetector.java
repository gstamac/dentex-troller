package com.autonavi.ae.gmap.gesture;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;
import com.autonavi.amap.mapcore.FPoint;

/* JADX INFO: loaded from: classes.dex */
public class HoverGestureDetector extends TwoFingerGestureDetector {
    private static final FPoint FOCUS_DELTA_ZERO = FPoint.obtain();
    private FPoint mCurrFocusInternal;
    private FPoint mFocusDeltaExternal;
    private FPoint mFocusExternal;
    private final OnHoverGestureListener mListener;
    private FPoint mPrevFocusInternal;
    private boolean mSloppyGesture;

    public interface OnHoverGestureListener {
        boolean onHove(HoverGestureDetector hoverGestureDetector);

        boolean onHoveBegin(HoverGestureDetector hoverGestureDetector);

        void onHoveEnd(HoverGestureDetector hoverGestureDetector);
    }

    public HoverGestureDetector(Context context, OnHoverGestureListener onHoverGestureListener) {
        super(context);
        this.mFocusExternal = FPoint.obtain();
        this.mFocusDeltaExternal = FPoint.obtain();
        this.mListener = onHoverGestureListener;
    }

    @Override // com.autonavi.ae.gmap.gesture.TwoFingerGestureDetector, com.autonavi.ae.gmap.gesture.BaseGestureDetector
    protected void handleStartProgressEvent(int i, MotionEvent motionEvent) {
        if (i == 2) {
            if (this.mSloppyGesture) {
                boolean zIsSloppyGesture = isSloppyGesture(motionEvent);
                this.mSloppyGesture = zIsSloppyGesture;
                if (zIsSloppyGesture) {
                    return;
                }
                this.mGestureInProgress = this.mListener.onHoveBegin(this);
                return;
            }
            return;
        }
        if (i != 5) {
            return;
        }
        resetState();
        this.mPrevEvent = MotionEvent.obtain(motionEvent);
        this.mTimeDelta = 0L;
        updateStateByEvent(motionEvent);
        boolean zIsSloppyGesture2 = isSloppyGesture(motionEvent);
        this.mSloppyGesture = zIsSloppyGesture2;
        if (zIsSloppyGesture2) {
            return;
        }
        this.mGestureInProgress = this.mListener.onHoveBegin(this);
    }

    @Override // com.autonavi.ae.gmap.gesture.TwoFingerGestureDetector, com.autonavi.ae.gmap.gesture.BaseGestureDetector
    protected void handleInProgressEvent(int i, MotionEvent motionEvent) {
        if (i == 2) {
            updateStateByEvent(motionEvent);
            if (this.mCurrPressure / this.mPrevPressure <= 0.67f || !this.mListener.onHove(this)) {
                return;
            }
            this.mPrevEvent.recycle();
            this.mPrevEvent = MotionEvent.obtain(motionEvent);
            return;
        }
        if (i == 3) {
            if (!this.mSloppyGesture) {
                this.mListener.onHoveEnd(this);
            }
            resetState();
        } else {
            if (i != 6) {
                return;
            }
            updateStateByEvent(motionEvent);
            if (!this.mSloppyGesture) {
                this.mListener.onHoveEnd(this);
            }
            resetState();
        }
    }

    @Override // com.autonavi.ae.gmap.gesture.BaseGestureDetector
    protected void resetState() {
        super.resetState();
        this.mSloppyGesture = false;
    }

    @Override // com.autonavi.ae.gmap.gesture.TwoFingerGestureDetector, com.autonavi.ae.gmap.gesture.BaseGestureDetector
    protected void updateStateByEvent(MotionEvent motionEvent) {
        FPoint fPointObtain;
        super.updateStateByEvent(motionEvent);
        MotionEvent motionEvent2 = this.mPrevEvent;
        this.mCurrFocusInternal = determineFocalPoint(motionEvent);
        this.mPrevFocusInternal = determineFocalPoint(motionEvent2);
        if (this.mPrevEvent.getPointerCount() != motionEvent.getPointerCount()) {
            fPointObtain = FOCUS_DELTA_ZERO;
        } else {
            fPointObtain = FPoint.obtain(this.mCurrFocusInternal.x - this.mPrevFocusInternal.x, this.mCurrFocusInternal.y - this.mPrevFocusInternal.y);
        }
        this.mFocusDeltaExternal = fPointObtain;
        this.mCurrFocusInternal.recycle();
        this.mPrevFocusInternal.recycle();
        this.mFocusExternal.x += this.mFocusDeltaExternal.x;
        this.mFocusExternal.y += this.mFocusDeltaExternal.y;
    }

    public float getFocusX() {
        return this.mFocusExternal.x;
    }

    public float getFocusY() {
        return this.mFocusExternal.y;
    }

    public PointF getFocusDelta() {
        return this.mFocusDeltaExternal;
    }
}
