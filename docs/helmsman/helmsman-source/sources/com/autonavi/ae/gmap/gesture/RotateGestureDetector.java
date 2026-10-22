package com.autonavi.ae.gmap.gesture;

import android.content.Context;
import android.view.MotionEvent;

/* JADX INFO: loaded from: classes.dex */
public class RotateGestureDetector extends TwoFingerGestureDetector {
    private final OnRotateGestureListener mListener;
    private boolean mSloppyGesture;

    public interface OnRotateGestureListener {
        boolean onRotate(RotateGestureDetector rotateGestureDetector);

        boolean onRotateBegin(RotateGestureDetector rotateGestureDetector);

        void onRotateEnd(RotateGestureDetector rotateGestureDetector);
    }

    public static class SimpleOnRotateGestureListener implements OnRotateGestureListener {
        @Override // com.autonavi.ae.gmap.gesture.RotateGestureDetector.OnRotateGestureListener
        public boolean onRotate(RotateGestureDetector rotateGestureDetector) {
            return false;
        }

        @Override // com.autonavi.ae.gmap.gesture.RotateGestureDetector.OnRotateGestureListener
        public boolean onRotateBegin(RotateGestureDetector rotateGestureDetector) {
            return true;
        }

        @Override // com.autonavi.ae.gmap.gesture.RotateGestureDetector.OnRotateGestureListener
        public void onRotateEnd(RotateGestureDetector rotateGestureDetector) {
        }
    }

    public RotateGestureDetector(Context context, OnRotateGestureListener onRotateGestureListener) {
        super(context);
        this.mListener = onRotateGestureListener;
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
                this.mGestureInProgress = this.mListener.onRotateBegin(this);
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
        this.mGestureInProgress = this.mListener.onRotateBegin(this);
    }

    @Override // com.autonavi.ae.gmap.gesture.TwoFingerGestureDetector, com.autonavi.ae.gmap.gesture.BaseGestureDetector
    protected void handleInProgressEvent(int i, MotionEvent motionEvent) {
        if (i == 2) {
            updateStateByEvent(motionEvent);
            if (this.mCurrPressure / this.mPrevPressure <= 0.67f || !this.mListener.onRotate(this)) {
                return;
            }
            this.mPrevEvent.recycle();
            this.mPrevEvent = MotionEvent.obtain(motionEvent);
            return;
        }
        if (i == 3) {
            if (!this.mSloppyGesture) {
                this.mListener.onRotateEnd(this);
            }
            resetState();
        } else {
            if (i != 6) {
                return;
            }
            updateStateByEvent(motionEvent);
            if (!this.mSloppyGesture) {
                this.mListener.onRotateEnd(this);
            }
            resetState();
        }
    }

    @Override // com.autonavi.ae.gmap.gesture.BaseGestureDetector
    protected void resetState() {
        super.resetState();
        this.mSloppyGesture = false;
    }

    public float getRotationDegreesDelta() {
        return (float) (((Math.atan2(this.mPrevFingerDiffY, this.mPrevFingerDiffX) - Math.atan2(this.mCurrFingerDiffY, this.mCurrFingerDiffX)) * 180.0d) / 3.141592653589793d);
    }
}
