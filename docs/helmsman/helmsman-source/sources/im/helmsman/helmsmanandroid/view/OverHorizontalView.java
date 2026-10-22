package im.helmsman.helmsmanandroid.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;
import androidx.core.view.MotionEventCompat;
import com.MAVLink.common.msg_data_transmission_handshake;
import com.google.android.gms.common.ConnectionResult;
import java.lang.reflect.Field;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes2.dex */
public class OverHorizontalView extends FrameLayout implements View.OnTouchListener {
    static final int ANIMATED_SCROLL_GAP = 250;
    private static final int INVALID_POINTER = -1;
    static final float MAX_SCROLL_FACTOR = 0.5f;
    static final float OVERSHOOT_TENSION = 0.75f;
    protected View child;
    boolean hasFailedObtainingScrollFields;
    LayoutInflater inflater;
    boolean isInFlingMode;
    private int mActivePointerId;
    private View mChildToScrollTo;
    protected Context mContext;
    private boolean mFillViewport;
    private boolean mIsBeingDragged;
    private boolean mIsLayoutDirty;
    private float mLastMotionY;
    private long mLastScroll;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private boolean mScrollViewMovedFocus;
    Field mScrollXField;
    Field mScrollYField;
    private Scroller mScroller;
    private boolean mSmoothScrollingEnabled;
    private final Rect mTempRect;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    DisplayMetrics metrics;
    private Runnable overScrollerSpringbackTask;
    int prevScrollY;

    private int clamp(int i, int i2, int i3) {
        if (i2 >= i3 || i < 0) {
            return 0;
        }
        return i2 + i > i3 ? i3 - i2 : i;
    }

    protected int onOverScroll(int i) {
        return 0;
    }

    public OverHorizontalView(Context context) {
        this(context, null);
    }

    public OverHorizontalView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public OverHorizontalView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mTempRect = new Rect();
        this.isInFlingMode = false;
        this.mIsLayoutDirty = true;
        this.mChildToScrollTo = null;
        this.mIsBeingDragged = false;
        this.mSmoothScrollingEnabled = true;
        this.mActivePointerId = -1;
        this.mContext = context;
        initScrollView();
        setFillViewport(true);
        initBounce();
    }

    private void initBounce() {
        this.metrics = this.mContext.getResources().getDisplayMetrics();
        this.mScroller = new Scroller(getContext(), new OvershootInterpolator(OVERSHOOT_TENSION));
        this.overScrollerSpringbackTask = new Runnable() { // from class: im.helmsman.helmsmanandroid.view.OverHorizontalView.1
            @Override // java.lang.Runnable
            public void run() {
                OverHorizontalView.this.mScroller.computeScrollOffset();
                OverHorizontalView overHorizontalView = OverHorizontalView.this;
                overHorizontalView.scrollTo(0, overHorizontalView.mScroller.getCurrY());
                if (OverHorizontalView.this.mScroller.isFinished()) {
                    return;
                }
                OverHorizontalView.this.post(this);
            }
        };
        this.prevScrollY = getPaddingTop();
        try {
            this.mScrollXField = View.class.getDeclaredField("mScrollX");
            this.mScrollYField = View.class.getDeclaredField("mScrollY");
        } catch (Exception unused) {
            this.hasFailedObtainingScrollFields = true;
        }
    }

    private void SetScrollY(int i) {
        Field field = this.mScrollYField;
        if (field != null) {
            try {
                field.setInt(this, i);
            } catch (Exception unused) {
            }
        }
    }

    private void SetScrollX(int i) {
        Field field = this.mScrollXField;
        if (field != null) {
            try {
                field.setInt(this, i);
            } catch (Exception unused) {
            }
        }
    }

    public void initChildPointer() {
        View childAt = getChildAt(0);
        this.child = childAt;
        childAt.setPadding(0, ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED, 0, ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED);
    }

    @Override // android.view.View
    protected float getTopFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }
        int verticalFadingEdgeLength = getVerticalFadingEdgeLength();
        if (getScrollY() < verticalFadingEdgeLength) {
            return getScrollY() / verticalFadingEdgeLength;
        }
        return 1.0f;
    }

    @Override // android.view.View
    protected float getBottomFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }
        int verticalFadingEdgeLength = getVerticalFadingEdgeLength();
        int bottom = (getChildAt(0).getBottom() - getScrollY()) - (getHeight() - getPaddingBottom());
        if (bottom < verticalFadingEdgeLength) {
            return bottom / verticalFadingEdgeLength;
        }
        return 1.0f;
    }

    public int getMaxScrollAmount() {
        return (int) ((getBottom() - getTop()) * MAX_SCROLL_FACTOR);
    }

    private void initScrollView() {
        this.mScroller = new Scroller(getContext());
        setFocusable(true);
        setDescendantFocusability(262144);
        setWillNotDraw(false);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(this.mContext);
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        setOnTouchListener(this);
        post(new Runnable() { // from class: im.helmsman.helmsmanandroid.view.OverHorizontalView.2
            @Override // java.lang.Runnable
            public void run() {
                OverHorizontalView overHorizontalView = OverHorizontalView.this;
                overHorizontalView.scrollTo(0, overHorizontalView.child.getPaddingTop());
            }
        });
    }

    @Override // android.view.ViewGroup
    public void addView(View view) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }
        super.addView(view);
        initChildPointer();
    }

    @Override // android.view.ViewGroup
    public void addView(View view, int i) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }
        super.addView(view, i);
        initChildPointer();
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void addView(View view, ViewGroup.LayoutParams layoutParams) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }
        super.addView(view, layoutParams);
        initChildPointer();
    }

    @Override // android.view.ViewGroup
    public void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }
        super.addView(view, i, layoutParams);
    }

    private boolean canScroll() {
        View childAt = getChildAt(0);
        if (childAt != null) {
            return getHeight() < (childAt.getHeight() + getPaddingTop()) + getPaddingBottom();
        }
        return false;
    }

    public boolean isFillViewport() {
        return this.mFillViewport;
    }

    public void setFillViewport(boolean z) {
        if (z != this.mFillViewport) {
            this.mFillViewport = z;
            requestLayout();
        }
    }

    public boolean isSmoothScrollingEnabled() {
        return this.mSmoothScrollingEnabled;
    }

    public void setSmoothScrollingEnabled(boolean z) {
        this.mSmoothScrollingEnabled = z;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.mFillViewport && View.MeasureSpec.getMode(i2) != 0 && getChildCount() > 0) {
            View childAt = getChildAt(0);
            int measuredHeight = getMeasuredHeight();
            if (childAt.getMeasuredHeight() < measuredHeight) {
                childAt.measure(getChildMeasureSpec(i, getPaddingLeft() + getPaddingRight(), ((FrameLayout.LayoutParams) childAt.getLayoutParams()).width), View.MeasureSpec.makeMeasureSpec((measuredHeight - getPaddingTop()) - getPaddingBottom(), 1073741824));
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent) || executeKeyEvent(keyEvent);
    }

    public boolean executeKeyEvent(KeyEvent keyEvent) {
        this.mTempRect.setEmpty();
        boolean zCanScroll = canScroll();
        int i = msg_data_transmission_handshake.MAVLINK_MSG_ID_DATA_TRANSMISSION_HANDSHAKE;
        if (!zCanScroll) {
            if (!isFocused() || keyEvent.getKeyCode() == 4) {
                return false;
            }
            View viewFindFocus = findFocus();
            if (viewFindFocus == this) {
                viewFindFocus = null;
            }
            View viewFindNextFocus = FocusFinder.getInstance().findNextFocus(this, viewFindFocus, msg_data_transmission_handshake.MAVLINK_MSG_ID_DATA_TRANSMISSION_HANDSHAKE);
            return (viewFindNextFocus == null || viewFindNextFocus == this || !viewFindNextFocus.requestFocus(msg_data_transmission_handshake.MAVLINK_MSG_ID_DATA_TRANSMISSION_HANDSHAKE)) ? false : true;
        }
        if (keyEvent.getAction() != 0) {
            return false;
        }
        int keyCode = keyEvent.getKeyCode();
        if (keyCode == 19) {
            if (!keyEvent.isAltPressed()) {
                return arrowScroll(33);
            }
            return fullScroll(33);
        }
        if (keyCode == 20) {
            if (!keyEvent.isAltPressed()) {
                return arrowScroll(msg_data_transmission_handshake.MAVLINK_MSG_ID_DATA_TRANSMISSION_HANDSHAKE);
            }
            return fullScroll(msg_data_transmission_handshake.MAVLINK_MSG_ID_DATA_TRANSMISSION_HANDSHAKE);
        }
        if (keyCode != 62) {
            return false;
        }
        if (keyEvent.isShiftPressed()) {
            i = 33;
        }
        pageScroll(i);
        return false;
    }

    public boolean inChild(int i, int i2) {
        if (getChildCount() <= 0) {
            return false;
        }
        int scrollY = getScrollY();
        View childAt = getChildAt(0);
        return i2 >= childAt.getTop() - scrollY && i2 < childAt.getBottom() - scrollY && i >= childAt.getLeft() && i < childAt.getRight();
    }

    /* JADX WARN: Code duplicated, block: B:24:0x0041  */
    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 2 && this.mIsBeingDragged) {
            return true;
        }
        int i = action & 255;
        if (i == 0) {
            float y = motionEvent.getY();
            if (!inChild((int) motionEvent.getX(), (int) y)) {
                this.mIsBeingDragged = false;
            } else {
                this.mLastMotionY = y;
                this.mActivePointerId = motionEvent.getPointerId(0);
                this.mIsBeingDragged = !this.mScroller.isFinished();
            }
        } else if (i == 1) {
            this.mIsBeingDragged = false;
            this.mActivePointerId = -1;
        } else if (i == 2) {
            int i2 = this.mActivePointerId;
            if (i2 != -1) {
                float y2 = motionEvent.getY(motionEvent.findPointerIndex(i2));
                if (((int) Math.abs(y2 - this.mLastMotionY)) > this.mTouchSlop) {
                    this.mIsBeingDragged = true;
                    this.mLastMotionY = y2;
                }
            }
        } else if (i == 3) {
            this.mIsBeingDragged = false;
            this.mActivePointerId = -1;
        } else if (i == 6) {
            onSecondaryPointerUp(motionEvent);
        }
        return this.mIsBeingDragged;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0 && motionEvent.getEdgeFlags() != 0) {
            return false;
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        int action = motionEvent.getAction() & 255;
        if (action == 0) {
            float y = motionEvent.getY();
            boolean zInChild = inChild((int) motionEvent.getX(), (int) y);
            this.mIsBeingDragged = zInChild;
            if (!zInChild) {
                return false;
            }
            if (!this.mScroller.isFinished()) {
                this.mScroller.abortAnimation();
            }
            this.mLastMotionY = y;
            this.mActivePointerId = motionEvent.getPointerId(0);
        } else if (action != 1) {
            if (action != 2) {
                if (action != 3) {
                    if (action == 6) {
                        onSecondaryPointerUp(motionEvent);
                    }
                } else if (this.mIsBeingDragged && getChildCount() > 0) {
                    this.mActivePointerId = -1;
                    this.mIsBeingDragged = false;
                    VelocityTracker velocityTracker = this.mVelocityTracker;
                    if (velocityTracker != null) {
                        velocityTracker.recycle();
                        this.mVelocityTracker = null;
                    }
                }
            } else if (this.mIsBeingDragged) {
                float y2 = motionEvent.getY(motionEvent.findPointerIndex(this.mActivePointerId));
                int i = (int) (this.mLastMotionY - y2);
                this.mLastMotionY = y2;
                if (isOverScrolled()) {
                    scrollBy(0, i / 2);
                } else {
                    scrollBy(0, i);
                }
            }
        } else if (this.mIsBeingDragged) {
            VelocityTracker velocityTracker2 = this.mVelocityTracker;
            velocityTracker2.computeCurrentVelocity(1000, this.mMaximumVelocity);
            int yVelocity = (int) velocityTracker2.getYVelocity(this.mActivePointerId);
            if (getChildCount() > 0 && Math.abs(yVelocity) > this.mMinimumVelocity) {
                fling(-yVelocity);
            }
            this.mActivePointerId = -1;
            this.mIsBeingDragged = false;
            VelocityTracker velocityTracker3 = this.mVelocityTracker;
            if (velocityTracker3 != null) {
                velocityTracker3.recycle();
                this.mVelocityTracker = null;
            }
        }
        return true;
    }

    public boolean isOverScrolled() {
        return getScrollY() < this.child.getPaddingTop() || getScrollY() > (this.child.getBottom() - this.child.getPaddingBottom()) - getHeight();
    }

    private void onSecondaryPointerUp(MotionEvent motionEvent) {
        int action = (motionEvent.getAction() & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
        if (motionEvent.getPointerId(action) == this.mActivePointerId) {
            int i = action == 0 ? 1 : 0;
            this.mLastMotionY = motionEvent.getY(i);
            this.mActivePointerId = motionEvent.getPointerId(i);
            VelocityTracker velocityTracker = this.mVelocityTracker;
            if (velocityTracker != null) {
                velocityTracker.clear();
            }
        }
    }

    private View findFocusableViewInMyBounds(boolean z, int i, View view) {
        int verticalFadingEdgeLength = getVerticalFadingEdgeLength() / 2;
        int i2 = i + verticalFadingEdgeLength;
        int height = (i + getHeight()) - verticalFadingEdgeLength;
        return (view == null || view.getTop() >= height || view.getBottom() <= i2) ? findFocusableViewInBounds(z, i2, height) : view;
    }

    /* JADX WARN: Code duplicated, block: B:29:0x004f  */
    private View findFocusableViewInBounds(boolean z, int i, int i2) {
        ArrayList focusables = getFocusables(2);
        int size = focusables.size();
        View view = null;
        boolean z2 = false;
        for (int i3 = 0; i3 < size; i3++) {
            View view2 = (View) focusables.get(i3);
            int top = view2.getTop();
            int bottom = view2.getBottom();
            if (i < bottom && top < i2) {
                boolean z3 = i < top && bottom < i2;
                if (view == null) {
                    view = view2;
                    z2 = z3;
                } else {
                    boolean z4 = (z && top < view.getTop()) || (!z && bottom > view.getBottom());
                    if (z2) {
                        if (z3 && z4) {
                            view = view2;
                        }
                    } else if (z3) {
                        view = view2;
                        z2 = true;
                    } else if (z4) {
                        view = view2;
                    }
                }
            }
        }
        return view;
    }

    public boolean pageScroll(int i) {
        boolean z = i == 130;
        int height = getHeight();
        if (z) {
            this.mTempRect.top = getScrollY() + height;
            int childCount = getChildCount();
            if (childCount > 0) {
                View childAt = getChildAt(childCount - 1);
                if (this.mTempRect.top + height > childAt.getBottom()) {
                    this.mTempRect.top = childAt.getBottom() - height;
                }
            }
        } else {
            this.mTempRect.top = getScrollY() - height;
            if (this.mTempRect.top < 0) {
                this.mTempRect.top = 0;
            }
        }
        Rect rect = this.mTempRect;
        rect.bottom = rect.top + height;
        return scrollAndFocus(i, this.mTempRect.top, this.mTempRect.bottom);
    }

    public boolean fullScroll(int i) {
        int childCount;
        boolean z = i == 130;
        int height = getHeight();
        this.mTempRect.top = 0;
        this.mTempRect.bottom = height;
        if (z && (childCount = getChildCount()) > 0) {
            this.mTempRect.bottom = getChildAt(childCount - 1).getBottom();
            Rect rect = this.mTempRect;
            rect.top = rect.bottom - height;
        }
        return scrollAndFocus(i, this.mTempRect.top, this.mTempRect.bottom);
    }

    private boolean scrollAndFocus(int i, int i2, int i3) {
        int height = getHeight();
        int scrollY = getScrollY();
        int i4 = height + scrollY;
        boolean z = true;
        boolean z2 = i == 33;
        View viewFindFocusableViewInBounds = findFocusableViewInBounds(z2, i2, i3);
        if (viewFindFocusableViewInBounds == null) {
            viewFindFocusableViewInBounds = this;
        }
        if (i2 < scrollY || i3 > i4) {
            doScrollY(z2 ? i2 - scrollY : i3 - i4);
        } else {
            z = false;
        }
        if (viewFindFocusableViewInBounds != findFocus() && viewFindFocusableViewInBounds.requestFocus(i)) {
            this.mScrollViewMovedFocus = false;
        }
        return z;
    }

    public boolean arrowScroll(int i) {
        int bottom;
        View viewFindFocus = findFocus();
        if (viewFindFocus == this) {
            viewFindFocus = null;
        }
        View viewFindNextFocus = FocusFinder.getInstance().findNextFocus(this, viewFindFocus, i);
        int maxScrollAmount = getMaxScrollAmount();
        if (viewFindNextFocus != null && isWithinDeltaOfScreen(viewFindNextFocus, maxScrollAmount, getHeight())) {
            viewFindNextFocus.getDrawingRect(this.mTempRect);
            offsetDescendantRectToMyCoords(viewFindNextFocus, this.mTempRect);
            doScrollY(computeScrollDeltaToGetChildRectOnScreen(this.mTempRect));
            viewFindNextFocus.requestFocus(i);
        } else {
            if (i == 33 && getScrollY() < maxScrollAmount) {
                maxScrollAmount = getScrollY();
            } else if (i == 130 && getChildCount() > 0 && (bottom = getChildAt(0).getBottom() - (getScrollY() + getHeight())) < maxScrollAmount) {
                maxScrollAmount = bottom;
            }
            if (maxScrollAmount == 0) {
                return false;
            }
            if (i != 130) {
                maxScrollAmount = -maxScrollAmount;
            }
            doScrollY(maxScrollAmount);
        }
        if (viewFindFocus == null || !viewFindFocus.isFocused() || !isOffScreen(viewFindFocus)) {
            return true;
        }
        int descendantFocusability = getDescendantFocusability();
        setDescendantFocusability(131072);
        requestFocus();
        setDescendantFocusability(descendantFocusability);
        return true;
    }

    private boolean isOffScreen(View view) {
        return !isWithinDeltaOfScreen(view, 0, getHeight());
    }

    private boolean isWithinDeltaOfScreen(View view, int i, int i2) {
        view.getDrawingRect(this.mTempRect);
        offsetDescendantRectToMyCoords(view, this.mTempRect);
        return this.mTempRect.bottom + i >= getScrollY() && this.mTempRect.top - i <= getScrollY() + i2;
    }

    private void doScrollY(int i) {
        if (i != 0) {
            if (this.mSmoothScrollingEnabled) {
                smoothScrollBy(0, i);
            } else {
                scrollBy(0, i);
            }
        }
    }

    public final void smoothScrollBy(int i, int i2) {
        if (getChildCount() == 0) {
            return;
        }
        if (AnimationUtils.currentAnimationTimeMillis() - this.mLastScroll > 250) {
            int iMax = Math.max(0, getChildAt(0).getHeight() - ((getHeight() - getPaddingBottom()) - getPaddingTop()));
            int scrollY = getScrollY();
            this.mScroller.startScroll(getScrollX(), scrollY, 0, Math.max(0, Math.min(i2 + scrollY, iMax)) - scrollY);
            invalidate();
        } else {
            if (!this.mScroller.isFinished()) {
                this.mScroller.abortAnimation();
            }
            scrollBy(i, i2);
        }
        this.mLastScroll = AnimationUtils.currentAnimationTimeMillis();
    }

    public final void smoothScrollToTop() {
        smoothScrollTo(0, this.child.getPaddingTop());
    }

    public final void smoothScrollToBottom() {
        smoothScrollTo(0, (this.child.getHeight() - this.child.getPaddingTop()) - getHeight());
    }

    public final void smoothScrollTo(int i, int i2) {
        smoothScrollBy(i - getScrollX(), i2 - getScrollY());
    }

    @Override // android.view.View
    protected int computeVerticalScrollRange() {
        return getChildCount() == 0 ? (getHeight() - getPaddingBottom()) - getPaddingTop() : getChildAt(0).getBottom();
    }

    @Override // android.view.View
    protected int computeVerticalScrollOffset() {
        return Math.max(0, super.computeVerticalScrollOffset());
    }

    @Override // android.view.ViewGroup
    protected void measureChild(View view, int i, int i2) {
        view.measure(getChildMeasureSpec(i, getPaddingLeft() + getPaddingRight(), view.getLayoutParams().width), View.MeasureSpec.makeMeasureSpec(0, 0));
    }

    @Override // android.view.ViewGroup
    protected void measureChildWithMargins(View view, int i, int i2, int i3, int i4) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        view.measure(getChildMeasureSpec(i, getPaddingLeft() + getPaddingRight() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin + i2, marginLayoutParams.width), View.MeasureSpec.makeMeasureSpec(marginLayoutParams.topMargin + marginLayoutParams.bottomMargin, 0));
    }

    @Override // android.view.View
    public void computeScroll() {
        if (this.hasFailedObtainingScrollFields) {
            super.computeScroll();
            return;
        }
        if (this.mScroller.computeScrollOffset()) {
            int scrollX = getScrollX();
            int scrollY = getScrollY();
            int currX = this.mScroller.getCurrX();
            int currY = this.mScroller.getCurrY();
            if (getChildCount() > 0) {
                View childAt = getChildAt(0);
                int iClamp = clamp(currX, (getWidth() - getPaddingRight()) - getPaddingLeft(), childAt.getWidth());
                int iClamp2 = clamp(currY, (getHeight() - getPaddingBottom()) - getPaddingTop(), childAt.getHeight());
                if (iClamp != scrollX || iClamp2 != scrollY) {
                    SetScrollX(iClamp);
                    SetScrollY(iClamp2);
                    onScrollChanged(iClamp, iClamp2, scrollX, scrollY);
                }
            }
            awakenScrollBars();
            postInvalidate();
        }
    }

    private void scrollToChild(View view) {
        view.getDrawingRect(this.mTempRect);
        offsetDescendantRectToMyCoords(view, this.mTempRect);
        int iComputeScrollDeltaToGetChildRectOnScreen = computeScrollDeltaToGetChildRectOnScreen(this.mTempRect);
        if (iComputeScrollDeltaToGetChildRectOnScreen != 0) {
            scrollBy(0, iComputeScrollDeltaToGetChildRectOnScreen);
        }
    }

    private boolean scrollToChildRect(Rect rect, boolean z) {
        int iComputeScrollDeltaToGetChildRectOnScreen = computeScrollDeltaToGetChildRectOnScreen(rect);
        boolean z2 = iComputeScrollDeltaToGetChildRectOnScreen != 0;
        if (z2) {
            if (z) {
                scrollBy(0, iComputeScrollDeltaToGetChildRectOnScreen);
            } else {
                smoothScrollBy(0, iComputeScrollDeltaToGetChildRectOnScreen);
            }
        }
        return z2;
    }

    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        int i;
        int i2;
        if (getChildCount() == 0) {
            return 0;
        }
        int height = getHeight();
        int scrollY = getScrollY();
        int i3 = scrollY + height;
        int verticalFadingEdgeLength = getVerticalFadingEdgeLength();
        if (rect.top > 0) {
            scrollY += verticalFadingEdgeLength;
        }
        if (rect.bottom < getChildAt(0).getHeight()) {
            i3 -= verticalFadingEdgeLength;
        }
        if (rect.bottom > i3 && rect.top > scrollY) {
            if (rect.height() > height) {
                i2 = rect.top - scrollY;
            } else {
                i2 = rect.bottom - i3;
            }
            return Math.min(i2 + 0, getChildAt(0).getBottom() - i3);
        }
        if (rect.top >= scrollY || rect.bottom >= i3) {
            return 0;
        }
        if (rect.height() > height) {
            i = 0 - (i3 - rect.bottom);
        } else {
            i = 0 - (scrollY - rect.top);
        }
        return Math.max(i, -getScrollY());
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestChildFocus(View view, View view2) {
        if (!this.mScrollViewMovedFocus) {
            if (!this.mIsLayoutDirty) {
                scrollToChild(view2);
            } else {
                this.mChildToScrollTo = view2;
            }
        }
        super.requestChildFocus(view, view2);
    }

    @Override // android.view.ViewGroup
    protected boolean onRequestFocusInDescendants(int i, Rect rect) {
        View viewFindNextFocusFromRect;
        if (i == 2) {
            i = msg_data_transmission_handshake.MAVLINK_MSG_ID_DATA_TRANSMISSION_HANDSHAKE;
        } else if (i == 1) {
            i = 33;
        }
        if (rect == null) {
            viewFindNextFocusFromRect = FocusFinder.getInstance().findNextFocus(this, null, i);
        } else {
            viewFindNextFocusFromRect = FocusFinder.getInstance().findNextFocusFromRect(this, rect, i);
        }
        if (viewFindNextFocusFromRect == null || isOffScreen(viewFindNextFocusFromRect)) {
            return false;
        }
        return viewFindNextFocusFromRect.requestFocus(i, rect);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean z) {
        rect.offset(view.getLeft() - view.getScrollX(), view.getTop() - view.getScrollY());
        return scrollToChildRect(rect, z);
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        this.mIsLayoutDirty = true;
        super.requestLayout();
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mIsLayoutDirty = false;
        View view = this.mChildToScrollTo;
        if (view != null && isViewDescendantOf(view, this)) {
            scrollToChild(this.mChildToScrollTo);
        }
        this.mChildToScrollTo = null;
        scrollTo(getScrollX(), getScrollY());
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        View viewFindFocus = findFocus();
        if (viewFindFocus == null || this == viewFindFocus || !isWithinDeltaOfScreen(viewFindFocus, 0, i4)) {
            return;
        }
        viewFindFocus.getDrawingRect(this.mTempRect);
        offsetDescendantRectToMyCoords(viewFindFocus, this.mTempRect);
        doScrollY(computeScrollDeltaToGetChildRectOnScreen(this.mTempRect));
    }

    @Override // android.view.View
    protected void onScrollChanged(int i, int i2, int i3, int i4) {
        int height = getHeight();
        int paddingTop = this.child.getPaddingTop();
        int height2 = this.child.getHeight() - this.child.getPaddingBottom();
        if (this.isInFlingMode && (i2 < paddingTop || i2 > height2 - height)) {
            if (i2 < paddingTop) {
                this.mScroller.startScroll(0, i2, 0, paddingTop - i2, 1000);
            } else {
                int i5 = height2 - height;
                if (i2 > i5) {
                    this.mScroller.startScroll(0, i2, 0, i5 - i2, 1000);
                }
            }
            post(this.overScrollerSpringbackTask);
            this.isInFlingMode = false;
            return;
        }
        super.onScrollChanged(i, i2, i3, i4);
    }

    private boolean isViewDescendantOf(View view, View view2) {
        if (view == view2) {
            return true;
        }
        Object parent = view.getParent();
        return (parent instanceof ViewGroup) && isViewDescendantOf((View) parent, view2);
    }

    public void fling(int i) {
        if (getChildCount() > 0) {
            this.mScroller.fling(getScrollX(), getScrollY(), 0, i, 0, 0, 0, Math.max(0, getChildAt(0).getHeight() - ((getHeight() - getPaddingBottom()) - getPaddingTop())));
            boolean z = i > 0;
            View viewFindFocusableViewInMyBounds = findFocusableViewInMyBounds(z, this.mScroller.getFinalY(), findFocus());
            if (viewFindFocusableViewInMyBounds == null) {
                viewFindFocusableViewInMyBounds = this;
            }
            if (viewFindFocusableViewInMyBounds != findFocus()) {
                if (viewFindFocusableViewInMyBounds.requestFocus(z ? msg_data_transmission_handshake.MAVLINK_MSG_ID_DATA_TRANSMISSION_HANDSHAKE : 33)) {
                    this.mScrollViewMovedFocus = false;
                }
            }
            invalidate();
        }
    }

    @Override // android.view.View
    public void scrollTo(int i, int i2) {
        if (getChildCount() > 0) {
            View childAt = getChildAt(0);
            int iClamp = clamp(i, (getWidth() - getPaddingRight()) - getPaddingLeft(), childAt.getWidth());
            int iClamp2 = clamp(i2, (getHeight() - getPaddingBottom()) - getPaddingTop(), childAt.getHeight());
            if (iClamp == getScrollX() && iClamp2 == getScrollY()) {
                return;
            }
            super.scrollTo(iClamp, iClamp2);
        }
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        this.mScroller.forceFinished(true);
        removeCallbacks(this.overScrollerSpringbackTask);
        if (motionEvent.getAction() == 1) {
            return overScrollView();
        }
        if (motionEvent.getAction() == 3) {
            return overScrollView();
        }
        return false;
    }

    private boolean overScrollView() {
        int iOnOverScroll;
        int height = getHeight();
        int paddingTop = this.child.getPaddingTop();
        int height2 = this.child.getHeight() - this.child.getPaddingBottom();
        int scrollY = getScrollY();
        if (scrollY < paddingTop) {
            onOverScroll(scrollY);
            iOnOverScroll = paddingTop - scrollY;
        } else if (scrollY + height > height2) {
            iOnOverScroll = ((this.child.getHeight() - this.child.getPaddingTop()) - this.child.getPaddingBottom() < height ? paddingTop - scrollY : (height2 - height) - scrollY) + onOverScroll(scrollY);
        } else {
            this.isInFlingMode = true;
            return false;
        }
        this.mScroller.startScroll(0, scrollY, 0, iOnOverScroll, 500);
        post(this.overScrollerSpringbackTask);
        this.prevScrollY = scrollY;
        return true;
    }
}
