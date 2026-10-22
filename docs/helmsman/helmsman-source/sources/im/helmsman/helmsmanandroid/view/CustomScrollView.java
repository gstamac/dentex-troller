package im.helmsman.helmsmanandroid.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/* JADX INFO: loaded from: classes2.dex */
public class CustomScrollView extends ScrollView {
    private static final int size = 4;
    private View inner;
    private Rect normal;
    private float y;

    public CustomScrollView(Context context) {
        super(context);
        this.normal = new Rect();
    }

    public CustomScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.normal = new Rect();
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            this.inner = getChildAt(0);
        }
    }

    @Override // android.widget.ScrollView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.inner == null) {
            return super.onTouchEvent(motionEvent);
        }
        commOnTouchEvent(motionEvent);
        return super.onTouchEvent(motionEvent);
    }

    public void commOnTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            this.y = motionEvent.getY();
            return;
        }
        if (action == 1) {
            if (isNeedAnimation()) {
                animation();
            }
        } else {
            if (action != 2) {
                return;
            }
            float f = this.y;
            float y = motionEvent.getY();
            int i = ((int) (f - y)) / 4;
            this.y = y;
            if (isNeedMove()) {
                if (this.normal.isEmpty()) {
                    this.normal.set(this.inner.getLeft(), this.inner.getTop(), this.inner.getRight(), this.inner.getBottom());
                    return;
                }
                int top = this.inner.getTop() - i;
                View view = this.inner;
                view.layout(view.getLeft(), top, this.inner.getRight(), this.inner.getBottom() - i);
            }
        }
    }

    public void animation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, this.inner.getTop(), this.normal.top);
        translateAnimation.setDuration(200L);
        this.inner.startAnimation(translateAnimation);
        this.inner.layout(this.normal.left, this.normal.top, this.normal.right, this.normal.bottom);
        this.normal.setEmpty();
    }

    public boolean isNeedAnimation() {
        return !this.normal.isEmpty();
    }

    public boolean isNeedMove() {
        int measuredHeight = this.inner.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        return scrollY == 0 || scrollY == measuredHeight;
    }
}
