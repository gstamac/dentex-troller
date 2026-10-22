package com.autonavi.ae.gmap.gesture;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;
import com.autonavi.amap.mapcore.FPoint;

/* JADX INFO: loaded from: classes.dex */
public class MoveGestureDetector extends BaseGestureDetector {
    private static final PointF FOCUS_DELTA_ZERO = new PointF();
    private FPoint mCurrFocusInternal;
    private PointF mFocusDeltaExternal;
    private PointF mFocusExternal;
    private final OnMoveGestureListener mListener;
    private FPoint mPrevFocusInternal;

    public interface OnMoveGestureListener {
        boolean onMove(MoveGestureDetector moveGestureDetector);

        boolean onMoveBegin(MoveGestureDetector moveGestureDetector);

        void onMoveEnd(MoveGestureDetector moveGestureDetector);
    }

    public static class SimpleOnMoveGestureListener implements OnMoveGestureListener {
        @Override // com.autonavi.ae.gmap.gesture.MoveGestureDetector.OnMoveGestureListener
        public boolean onMove(MoveGestureDetector moveGestureDetector) {
            return false;
        }

        @Override // com.autonavi.ae.gmap.gesture.MoveGestureDetector.OnMoveGestureListener
        public boolean onMoveBegin(MoveGestureDetector moveGestureDetector) {
            return true;
        }

        @Override // com.autonavi.ae.gmap.gesture.MoveGestureDetector.OnMoveGestureListener
        public void onMoveEnd(MoveGestureDetector moveGestureDetector) {
        }
    }

    public MoveGestureDetector(Context context, OnMoveGestureListener onMoveGestureListener) {
        super(context);
        this.mFocusExternal = new PointF();
        this.mFocusDeltaExternal = new PointF();
        this.mListener = onMoveGestureListener;
    }

    @Override // com.autonavi.ae.gmap.gesture.BaseGestureDetector
    protected void handleStartProgressEvent(int i, MotionEvent motionEvent) {
        if (i == 0) {
            resetState();
            this.mPrevEvent = MotionEvent.obtain(motionEvent);
            this.mTimeDelta = 0L;
            updateStateByEvent(motionEvent);
            return;
        }
        if (i == 2) {
            this.mGestureInProgress = this.mListener.onMoveBegin(this);
        } else {
            if (i != 5) {
                return;
            }
            if (this.mPrevEvent != null) {
                this.mPrevEvent.recycle();
            }
            this.mPrevEvent = MotionEvent.obtain(motionEvent);
            updateStateByEvent(motionEvent);
        }
    }

    @Override // com.autonavi.ae.gmap.gesture.BaseGestureDetector
    protected void handleInProgressEvent(int i, MotionEvent motionEvent) {
        if (i != 1) {
            if (i == 2) {
                updateStateByEvent(motionEvent);
                if (this.mCurrPressure / this.mPrevPressure <= 0.67f || motionEvent.getPointerCount() > 1 || !this.mListener.onMove(this)) {
                    return;
                }
                this.mPrevEvent.recycle();
                this.mPrevEvent = MotionEvent.obtain(motionEvent);
                return;
            }
            if (i != 3) {
                return;
            }
        }
        this.mListener.onMoveEnd(this);
        resetState();
    }

    @Override // com.autonavi.ae.gmap.gesture.BaseGestureDetector
    protected void updateStateByEvent(MotionEvent motionEvent) {
        super.updateStateByEvent(motionEvent);
        MotionEvent motionEvent2 = this.mPrevEvent;
        this.mCurrFocusInternal = determineFocalPoint(motionEvent);
        this.mPrevFocusInternal = determineFocalPoint(motionEvent2);
        boolean z = this.mPrevEvent.getPointerCount() != motionEvent.getPointerCount();
        this.mFocusDeltaExternal = z ? FOCUS_DELTA_ZERO : new PointF(this.mCurrFocusInternal.x - this.mPrevFocusInternal.x, this.mCurrFocusInternal.y - this.mPrevFocusInternal.y);
        this.mCurrFocusInternal.recycle();
        this.mPrevFocusInternal.recycle();
        if (z) {
            this.mPrevEvent.recycle();
            this.mPrevEvent = MotionEvent.obtain(motionEvent);
        }
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
