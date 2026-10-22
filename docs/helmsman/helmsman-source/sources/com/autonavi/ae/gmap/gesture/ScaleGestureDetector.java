package com.autonavi.ae.gmap.gesture;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/* JADX INFO: loaded from: classes.dex */
public class ScaleGestureDetector {
    private static final float PRESSURE_THRESHOLD = 0.67f;
    private boolean mActive0MostRecent;
    private int mActiveId0;
    private int mActiveId1;
    private float mBottomSlopEdge;
    private final Context mContext;
    private MotionEvent mCurrEvent;
    private float mCurrFingerDiffX;
    private float mCurrFingerDiffY;
    private float mCurrLen;
    private float mCurrPressure;
    private final float mEdgeSlop;
    private float mFocusX;
    private float mFocusY;
    private boolean mGestureInProgress;
    private boolean mInvalidGesture;
    private final OnScaleGestureListener mListener;
    private MotionEvent mPrevEvent;
    private float mPrevFingerDiffX;
    private float mPrevFingerDiffY;
    private float mPrevLen;
    private float mPrevPressure;
    private float mRightSlopEdge;
    private float mScaleFactor;
    private boolean mSloppyGesture;
    private long mTimeDelta;

    public interface OnScaleGestureListener {
        boolean onScale(ScaleGestureDetector scaleGestureDetector);

        boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector);

        void onScaleEnd(ScaleGestureDetector scaleGestureDetector);
    }

    public static class SimpleOnScaleGestureListener implements OnScaleGestureListener {
        @Override // com.autonavi.ae.gmap.gesture.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            return false;
        }

        @Override // com.autonavi.ae.gmap.gesture.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            return true;
        }

        @Override // com.autonavi.ae.gmap.gesture.ScaleGestureDetector.OnScaleGestureListener
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        }
    }

    public ScaleGestureDetector(Context context, OnScaleGestureListener onScaleGestureListener) {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.mContext = context;
        this.mListener = onScaleGestureListener;
        this.mEdgeSlop = viewConfiguration.getScaledEdgeSlop();
    }

    /* JADX WARN: Code duplicated, block: B:142:0x028f  */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int iFindNewActiveIndex;
        int iFindNewActiveIndex2;
        int iFindNewActiveIndex3;
        int action = motionEvent.getAction() & 255;
        if (action == 0) {
            reset();
        }
        boolean z = false;
        if (this.mInvalidGesture) {
            return false;
        }
        if (this.mGestureInProgress) {
            if (action == 1) {
                reset();
            } else if (action == 2) {
                setContext(motionEvent);
                if (this.mCurrPressure / this.mPrevPressure > PRESSURE_THRESHOLD && this.mListener.onScale(this)) {
                    this.mPrevEvent.recycle();
                    this.mPrevEvent = MotionEvent.obtain(motionEvent);
                }
            } else if (action == 3) {
                this.mListener.onScaleEnd(this);
                reset();
            } else if (action == 5) {
                this.mListener.onScaleEnd(this);
                int i = this.mActiveId0;
                int i2 = this.mActiveId1;
                reset();
                this.mPrevEvent = MotionEvent.obtain(motionEvent);
                if (!this.mActive0MostRecent) {
                    i = i2;
                }
                this.mActiveId0 = i;
                this.mActiveId1 = motionEvent.getPointerId(motionEvent.getActionIndex());
                this.mActive0MostRecent = false;
                int iFindPointerIndex = motionEvent.findPointerIndex(this.mActiveId0);
                if (iFindPointerIndex < 0 || this.mActiveId0 == this.mActiveId1) {
                    int i3 = this.mActiveId0;
                    int i4 = this.mActiveId1;
                    this.mActiveId0 = motionEvent.getPointerId(findNewActiveIndex(motionEvent, i3 != i4 ? i4 : -1, iFindPointerIndex));
                }
                setContext(motionEvent);
                this.mGestureInProgress = this.mListener.onScaleBegin(this);
            } else if (action == 6) {
                int pointerCount = motionEvent.getPointerCount();
                int actionIndex = motionEvent.getActionIndex();
                int pointerId = motionEvent.getPointerId(actionIndex);
                if (pointerCount > 2) {
                    int i5 = this.mActiveId0;
                    if (pointerId == i5) {
                        int iFindNewActiveIndex4 = findNewActiveIndex(motionEvent, this.mActiveId1, actionIndex);
                        if (iFindNewActiveIndex4 >= 0) {
                            this.mListener.onScaleEnd(this);
                            this.mActiveId0 = motionEvent.getPointerId(iFindNewActiveIndex4);
                            this.mActive0MostRecent = true;
                            this.mPrevEvent = MotionEvent.obtain(motionEvent);
                            setContext(motionEvent);
                            this.mGestureInProgress = this.mListener.onScaleBegin(this);
                        } else {
                            z = true;
                        }
                    } else if (pointerId == this.mActiveId1) {
                        int iFindNewActiveIndex5 = findNewActiveIndex(motionEvent, i5, actionIndex);
                        if (iFindNewActiveIndex5 >= 0) {
                            this.mListener.onScaleEnd(this);
                            this.mActiveId1 = motionEvent.getPointerId(iFindNewActiveIndex5);
                            this.mActive0MostRecent = false;
                            this.mPrevEvent = MotionEvent.obtain(motionEvent);
                            setContext(motionEvent);
                            this.mGestureInProgress = this.mListener.onScaleBegin(this);
                        } else {
                            z = true;
                        }
                    }
                    this.mPrevEvent.recycle();
                    this.mPrevEvent = MotionEvent.obtain(motionEvent);
                    setContext(motionEvent);
                } else {
                    z = true;
                }
                if (z) {
                    setContext(motionEvent);
                    int i6 = this.mActiveId0;
                    if (pointerId == i6) {
                        i6 = this.mActiveId1;
                    }
                    int iFindPointerIndex2 = motionEvent.findPointerIndex(i6);
                    this.mFocusX = motionEvent.getX(iFindPointerIndex2);
                    this.mFocusY = motionEvent.getY(iFindPointerIndex2);
                    this.mListener.onScaleEnd(this);
                    reset();
                    this.mActiveId0 = i6;
                    this.mActive0MostRecent = true;
                }
            }
        } else if (action == 0) {
            this.mActiveId0 = motionEvent.getPointerId(0);
            this.mActive0MostRecent = true;
        } else if (action == 1) {
            reset();
        } else if (action != 2) {
            if (action == 5) {
                DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
                this.mRightSlopEdge = displayMetrics.widthPixels - this.mEdgeSlop;
                this.mBottomSlopEdge = displayMetrics.heightPixels - this.mEdgeSlop;
                MotionEvent motionEvent2 = this.mPrevEvent;
                if (motionEvent2 != null) {
                    motionEvent2.recycle();
                }
                this.mPrevEvent = MotionEvent.obtain(motionEvent);
                this.mTimeDelta = 0L;
                int actionIndex2 = motionEvent.getActionIndex();
                int iFindPointerIndex3 = motionEvent.findPointerIndex(this.mActiveId0);
                int pointerId2 = motionEvent.getPointerId(actionIndex2);
                this.mActiveId1 = pointerId2;
                if (iFindPointerIndex3 < 0 || iFindPointerIndex3 == actionIndex2) {
                    iFindPointerIndex3 = findNewActiveIndex(motionEvent, iFindPointerIndex3 != actionIndex2 ? pointerId2 : -1, iFindPointerIndex3);
                    this.mActiveId0 = motionEvent.getPointerId(iFindPointerIndex3);
                }
                this.mActive0MostRecent = false;
                setContext(motionEvent);
                float f = this.mEdgeSlop;
                float f2 = this.mRightSlopEdge;
                float f3 = this.mBottomSlopEdge;
                float rawX = getRawX(motionEvent, iFindPointerIndex3);
                float rawY = getRawY(motionEvent, iFindPointerIndex3);
                float rawX2 = getRawX(motionEvent, actionIndex2);
                float rawY2 = getRawY(motionEvent, actionIndex2);
                boolean z2 = rawX < f || rawY < f || rawX > f2 || rawY > f3;
                boolean z3 = rawX2 < f || rawY2 < f || rawX2 > f2 || rawY2 > f3;
                if (z2 && z3) {
                    this.mFocusX = -1.0f;
                    this.mFocusY = -1.0f;
                    this.mSloppyGesture = true;
                } else if (z2) {
                    this.mFocusX = motionEvent.getX(actionIndex2);
                    this.mFocusY = motionEvent.getY(actionIndex2);
                    this.mSloppyGesture = true;
                } else if (z3) {
                    this.mFocusX = motionEvent.getX(iFindPointerIndex3);
                    this.mFocusY = motionEvent.getY(iFindPointerIndex3);
                    this.mSloppyGesture = true;
                } else {
                    this.mSloppyGesture = false;
                    this.mGestureInProgress = this.mListener.onScaleBegin(this);
                }
            } else if (action == 6 && this.mSloppyGesture) {
                int pointerCount2 = motionEvent.getPointerCount();
                int actionIndex3 = motionEvent.getActionIndex();
                int pointerId3 = motionEvent.getPointerId(actionIndex3);
                if (pointerCount2 > 2) {
                    int i7 = this.mActiveId0;
                    if (pointerId3 == i7) {
                        int iFindNewActiveIndex6 = findNewActiveIndex(motionEvent, this.mActiveId1, actionIndex3);
                        if (iFindNewActiveIndex6 >= 0) {
                            this.mActiveId0 = motionEvent.getPointerId(iFindNewActiveIndex6);
                        }
                    } else if (pointerId3 == this.mActiveId1 && (iFindNewActiveIndex3 = findNewActiveIndex(motionEvent, i7, actionIndex3)) >= 0) {
                        this.mActiveId1 = motionEvent.getPointerId(iFindNewActiveIndex3);
                    }
                } else {
                    int i8 = this.mActiveId0;
                    if (pointerId3 == i8) {
                        i8 = this.mActiveId1;
                    }
                    int iFindPointerIndex4 = motionEvent.findPointerIndex(i8);
                    if (iFindPointerIndex4 < 0) {
                        this.mInvalidGesture = true;
                        if (this.mGestureInProgress) {
                            this.mListener.onScaleEnd(this);
                        }
                        return false;
                    }
                    this.mActiveId0 = motionEvent.getPointerId(iFindPointerIndex4);
                    this.mActive0MostRecent = true;
                    this.mActiveId1 = -1;
                    this.mFocusX = motionEvent.getX(iFindPointerIndex4);
                    this.mFocusY = motionEvent.getY(iFindPointerIndex4);
                }
            }
        } else if (this.mSloppyGesture) {
            float f4 = this.mEdgeSlop;
            float f5 = this.mRightSlopEdge;
            float f6 = this.mBottomSlopEdge;
            int iFindPointerIndex5 = motionEvent.findPointerIndex(this.mActiveId0);
            int iFindPointerIndex6 = motionEvent.findPointerIndex(this.mActiveId1);
            float rawX3 = getRawX(motionEvent, iFindPointerIndex5);
            float rawY3 = getRawY(motionEvent, iFindPointerIndex5);
            float rawX4 = getRawX(motionEvent, iFindPointerIndex6);
            float rawY4 = getRawY(motionEvent, iFindPointerIndex6);
            boolean z4 = rawX3 < f4 || rawY3 < f4 || rawX3 > f5 || rawY3 > f6;
            boolean z5 = rawX4 < f4 || rawY4 < f4 || rawX4 > f5 || rawY4 > f6;
            if (z4 && (iFindNewActiveIndex2 = findNewActiveIndex(motionEvent, this.mActiveId1, iFindPointerIndex5)) >= 0) {
                this.mActiveId0 = motionEvent.getPointerId(iFindNewActiveIndex2);
                getRawX(motionEvent, iFindNewActiveIndex2);
                getRawY(motionEvent, iFindNewActiveIndex2);
                z4 = false;
                iFindPointerIndex5 = iFindNewActiveIndex2;
            }
            if (z5 && (iFindNewActiveIndex = findNewActiveIndex(motionEvent, this.mActiveId0, iFindPointerIndex6)) >= 0) {
                this.mActiveId1 = motionEvent.getPointerId(iFindNewActiveIndex);
                getRawX(motionEvent, iFindNewActiveIndex);
                getRawY(motionEvent, iFindNewActiveIndex);
                z5 = false;
                iFindPointerIndex6 = iFindNewActiveIndex;
            }
            if (z4 && z5) {
                this.mFocusX = -1.0f;
                this.mFocusY = -1.0f;
            } else if (z4) {
                this.mFocusX = motionEvent.getX(iFindPointerIndex6);
                this.mFocusY = motionEvent.getY(iFindPointerIndex6);
            } else if (z5) {
                this.mFocusX = motionEvent.getX(iFindPointerIndex5);
                this.mFocusY = motionEvent.getY(iFindPointerIndex5);
            } else {
                this.mSloppyGesture = false;
                this.mGestureInProgress = this.mListener.onScaleBegin(this);
            }
        }
        return true;
    }

    private int findNewActiveIndex(MotionEvent motionEvent, int i, int i2) {
        int pointerCount = motionEvent.getPointerCount();
        int iFindPointerIndex = motionEvent.findPointerIndex(i);
        for (int i3 = 0; i3 < pointerCount; i3++) {
            if (i3 != i2 && i3 != iFindPointerIndex) {
                float f = this.mEdgeSlop;
                float f2 = this.mRightSlopEdge;
                float f3 = this.mBottomSlopEdge;
                float rawX = getRawX(motionEvent, i3);
                float rawY = getRawY(motionEvent, i3);
                if (rawX >= f && rawY >= f && rawX <= f2 && rawY <= f3) {
                    return i3;
                }
            }
        }
        return -1;
    }

    private static float getRawX(MotionEvent motionEvent, int i) {
        if (i < 0) {
            return Float.MIN_VALUE;
        }
        if (i == 0) {
            return motionEvent.getRawX();
        }
        return motionEvent.getX(i) + (motionEvent.getRawX() - motionEvent.getX());
    }

    private static float getRawY(MotionEvent motionEvent, int i) {
        if (i < 0) {
            return Float.MIN_VALUE;
        }
        if (i == 0) {
            return motionEvent.getRawY();
        }
        return motionEvent.getY(i) + (motionEvent.getRawY() - motionEvent.getY());
    }

    private void setContext(MotionEvent motionEvent) {
        MotionEvent motionEvent2 = this.mCurrEvent;
        if (motionEvent2 != null) {
            motionEvent2.recycle();
        }
        this.mCurrEvent = MotionEvent.obtain(motionEvent);
        this.mCurrLen = -1.0f;
        this.mPrevLen = -1.0f;
        this.mScaleFactor = -1.0f;
        MotionEvent motionEvent3 = this.mPrevEvent;
        int iFindPointerIndex = motionEvent3.findPointerIndex(this.mActiveId0);
        int iFindPointerIndex2 = motionEvent3.findPointerIndex(this.mActiveId1);
        int iFindPointerIndex3 = motionEvent.findPointerIndex(this.mActiveId0);
        int iFindPointerIndex4 = motionEvent.findPointerIndex(this.mActiveId1);
        if (iFindPointerIndex < 0 || iFindPointerIndex2 < 0 || iFindPointerIndex3 < 0 || iFindPointerIndex4 < 0) {
            this.mInvalidGesture = true;
            if (this.mGestureInProgress) {
                this.mListener.onScaleEnd(this);
                return;
            }
            return;
        }
        float x = motionEvent3.getX(iFindPointerIndex);
        float y = motionEvent3.getY(iFindPointerIndex);
        float x2 = motionEvent3.getX(iFindPointerIndex2);
        float y2 = motionEvent3.getY(iFindPointerIndex2);
        float x3 = motionEvent.getX(iFindPointerIndex3);
        float y3 = motionEvent.getY(iFindPointerIndex3);
        float x4 = motionEvent.getX(iFindPointerIndex4) - x3;
        float y4 = motionEvent.getY(iFindPointerIndex4) - y3;
        this.mPrevFingerDiffX = x2 - x;
        this.mPrevFingerDiffY = y2 - y;
        this.mCurrFingerDiffX = x4;
        this.mCurrFingerDiffY = y4;
        this.mFocusX = x3 + (x4 * 0.5f);
        this.mFocusY = y3 + (y4 * 0.5f);
        this.mTimeDelta = motionEvent.getEventTime() - motionEvent3.getEventTime();
        this.mCurrPressure = motionEvent.getPressure(iFindPointerIndex3) + motionEvent.getPressure(iFindPointerIndex4);
        this.mPrevPressure = motionEvent3.getPressure(iFindPointerIndex) + motionEvent3.getPressure(iFindPointerIndex2);
    }

    private void reset() {
        MotionEvent motionEvent = this.mPrevEvent;
        if (motionEvent != null) {
            motionEvent.recycle();
            this.mPrevEvent = null;
        }
        MotionEvent motionEvent2 = this.mCurrEvent;
        if (motionEvent2 != null) {
            motionEvent2.recycle();
            this.mCurrEvent = null;
        }
        this.mSloppyGesture = false;
        this.mGestureInProgress = false;
        this.mActiveId0 = -1;
        this.mActiveId1 = -1;
        this.mInvalidGesture = false;
    }

    public boolean isInProgress() {
        return this.mGestureInProgress;
    }

    public float getFocusX() {
        return this.mFocusX;
    }

    public float getFocusY() {
        return this.mFocusY;
    }

    public float getCurrentSpan() {
        if (this.mCurrLen == -1.0f) {
            float f = this.mCurrFingerDiffX;
            float f2 = this.mCurrFingerDiffY;
            this.mCurrLen = (float) Math.sqrt((f * f) + (f2 * f2));
        }
        return this.mCurrLen;
    }

    public float getCurrentSpanX() {
        return this.mCurrFingerDiffX;
    }

    public float getCurrentSpanY() {
        return this.mCurrFingerDiffY;
    }

    public float getPreviousSpan() {
        if (this.mPrevLen == -1.0f) {
            float f = this.mPrevFingerDiffX;
            float f2 = this.mPrevFingerDiffY;
            this.mPrevLen = (float) Math.sqrt((f * f) + (f2 * f2));
        }
        return this.mPrevLen;
    }

    public float getPreviousSpanX() {
        return this.mPrevFingerDiffX;
    }

    public float getPreviousSpanY() {
        return this.mPrevFingerDiffY;
    }

    public float getScaleFactor() {
        if (this.mScaleFactor == -1.0f) {
            this.mScaleFactor = getCurrentSpan() / getPreviousSpan();
        }
        return this.mScaleFactor;
    }

    public long getTimeDelta() {
        return this.mTimeDelta;
    }

    public long getEventTime() {
        return this.mCurrEvent.getEventTime();
    }

    public MotionEvent getEvent() {
        return this.mCurrEvent;
    }
}
