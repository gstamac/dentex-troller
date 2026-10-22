package com.autonavi.ae.gmap.gesture;

import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
public class ScaleRotateGestureDetector extends ScaleGestureDetector {

    public static abstract class SimpleOnScaleRotateGestureListener implements ScaleGestureDetector.OnScaleGestureListener {
        public abstract boolean onScaleRotate(ScaleRotateGestureDetector scaleRotateGestureDetector);

        public abstract boolean onScaleRotateBegin(ScaleRotateGestureDetector scaleRotateGestureDetector);

        public abstract void onScaleRotateEnd(ScaleRotateGestureDetector scaleRotateGestureDetector);

        @Override // com.autonavi.ae.gmap.gesture.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            return onScaleRotate((ScaleRotateGestureDetector) scaleGestureDetector);
        }

        @Override // com.autonavi.ae.gmap.gesture.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            return onScaleRotateBegin((ScaleRotateGestureDetector) scaleGestureDetector);
        }

        @Override // com.autonavi.ae.gmap.gesture.ScaleGestureDetector.OnScaleGestureListener
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            onScaleRotateEnd((ScaleRotateGestureDetector) scaleGestureDetector);
        }
    }

    public ScaleRotateGestureDetector(Context context, SimpleOnScaleRotateGestureListener simpleOnScaleRotateGestureListener) {
        super(context, simpleOnScaleRotateGestureListener);
    }

    public float getRotationDegreesDelta() {
        return (float) (((Math.atan2(getPreviousSpanY(), getPreviousSpanX()) - Math.atan2(getCurrentSpanY(), getCurrentSpanX())) * 180.0d) / 3.141592653589793d);
    }
}
