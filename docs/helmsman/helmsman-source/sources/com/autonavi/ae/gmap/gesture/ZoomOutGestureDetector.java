package com.autonavi.ae.gmap.gesture;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;
import com.autonavi.amap.mapcore.FPoint;

/* JADX INFO: loaded from: classes.dex */
public class ZoomOutGestureDetector extends TwoFingerGestureDetector {
    private static final PointF FOCUS_DELTA_ZERO = new PointF();
    private FPoint mCurrFocusInternal;
    private PointF mFocusDeltaExternal;
    private PointF mFocusExternal;
    private final OnZoomOutGestureListener mListener;
    private FPoint mPrevFocusInternal;
    private boolean mSloppyGesture;

    public interface OnZoomOutGestureListener {
        void onZoomOut(ZoomOutGestureDetector zoomOutGestureDetector);

        boolean onZoomOutBegin(ZoomOutGestureDetector zoomOutGestureDetector);
    }

    public static class SimpleOnZoomOutGestureListener implements OnZoomOutGestureListener {
        @Override // com.autonavi.ae.gmap.gesture.ZoomOutGestureDetector.OnZoomOutGestureListener
        public void onZoomOut(ZoomOutGestureDetector zoomOutGestureDetector) {
        }

        @Override // com.autonavi.ae.gmap.gesture.ZoomOutGestureDetector.OnZoomOutGestureListener
        public boolean onZoomOutBegin(ZoomOutGestureDetector zoomOutGestureDetector) {
            return true;
        }
    }

    public ZoomOutGestureDetector(Context context, OnZoomOutGestureListener onZoomOutGestureListener) {
        super(context);
        this.mFocusExternal = new PointF();
        this.mFocusDeltaExternal = new PointF();
        this.mListener = onZoomOutGestureListener;
    }

    @Override // com.autonavi.ae.gmap.gesture.TwoFingerGestureDetector, com.autonavi.ae.gmap.gesture.BaseGestureDetector
    protected void handleStartProgressEvent(int i, MotionEvent motionEvent) {
        if (i != 5) {
            return;
        }
        resetState();
        this.mPrevEvent = MotionEvent.obtain(motionEvent);
        this.mTimeDelta = 0L;
        updateStateByEvent(motionEvent);
        boolean zIsSloppyGesture = isSloppyGesture(motionEvent);
        this.mSloppyGesture = zIsSloppyGesture;
        if (zIsSloppyGesture) {
            return;
        }
        this.mGestureInProgress = this.mListener.onZoomOutBegin(this);
    }

    @Override // com.autonavi.ae.gmap.gesture.TwoFingerGestureDetector, com.autonavi.ae.gmap.gesture.BaseGestureDetector
    protected void handleInProgressEvent(int i, MotionEvent motionEvent) {
        if (i == 3) {
            resetState();
        } else {
            if (i != 6) {
                return;
            }
            updateStateByEvent(motionEvent);
            if (!this.mSloppyGesture) {
                this.mListener.onZoomOut(this);
            }
            resetState();
        }
    }

    @Override // com.autonavi.ae.gmap.gesture.BaseGestureDetector
    protected void resetState() {
        super.resetState();
        this.mSloppyGesture = false;
        this.mFocusExternal.x = 0.0f;
        this.mFocusDeltaExternal.x = 0.0f;
        this.mFocusExternal.y = 0.0f;
        this.mFocusDeltaExternal.y = 0.0f;
    }

    @Override // com.autonavi.ae.gmap.gesture.TwoFingerGestureDetector, com.autonavi.ae.gmap.gesture.BaseGestureDetector
    protected void updateStateByEvent(MotionEvent motionEvent) {
        super.updateStateByEvent(motionEvent);
        MotionEvent motionEvent2 = this.mPrevEvent;
        this.mCurrFocusInternal = determineFocalPoint(motionEvent);
        this.mPrevFocusInternal = determineFocalPoint(motionEvent2);
        this.mFocusDeltaExternal = this.mPrevEvent.getPointerCount() != motionEvent.getPointerCount() ? FOCUS_DELTA_ZERO : new PointF(this.mCurrFocusInternal.x - this.mPrevFocusInternal.x, this.mCurrFocusInternal.y - this.mPrevFocusInternal.y);
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
}
