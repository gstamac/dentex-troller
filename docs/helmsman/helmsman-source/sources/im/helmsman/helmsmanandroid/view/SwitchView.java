package im.helmsman.helmsmanandroid.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.view.ViewCompat;

/* JADX INFO: loaded from: classes2.dex */
public class SwitchView extends View {
    public static final int STATE_SWITCH_OFF = 1;
    public static final int STATE_SWITCH_OFF2 = 2;
    public static final int STATE_SWITCH_ON = 4;
    public static final int STATE_SWITCH_ON2 = 3;
    private float bAnim;
    private float bBottom;
    private float bLeft;
    private float bOff2LeftX;
    private float bOffLeftX;
    private float bOffset;
    private float bOn2LeftX;
    private float bOnLeftX;
    private final Path bPath;
    private float bRadius;
    private final RectF bRectF;
    private float bRight;
    private float bStrokeWidth;
    private float bTop;
    private float bWidth;
    private int lastState;
    private OnSwitchStateChangedListener listener;
    private int mHeight;
    private int mWidth;
    private final Paint paint;
    private float sAnim;
    private float sBottom;
    private float sCenterX;
    private float sCenterY;
    private float sHeight;
    private float sLeft;
    private final Path sPath;
    private float sRight;
    private float sScale;
    private float sTop;
    private float sWidth;
    private RadialGradient shadowGradient;
    private float shadowHeight;
    private int state;
    private int tag;

    public interface OnSwitchStateChangedListener {
        void stateChangeListener(boolean z, int i);
    }

    public SwitchView(Context context) {
        this(context, null);
    }

    public SwitchView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.paint = new Paint();
        this.sPath = new Path();
        this.bPath = new Path();
        this.bRectF = new RectF();
        this.state = 1;
        this.lastState = 1;
        setLayerType(1, null);
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        setMeasuredDimension(size, (int) (size * 0.65f));
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mWidth = i;
        this.mHeight = i2;
        this.sTop = 0.0f;
        this.sLeft = 0.0f;
        float f = i;
        this.sRight = f;
        float f2 = i2 * 0.91f;
        this.sBottom = f2;
        float f3 = f - 0.0f;
        this.sWidth = f3;
        float f4 = f2 - 0.0f;
        this.sHeight = f4;
        this.sCenterX = (f + 0.0f) / 2.0f;
        this.sCenterY = (f2 + 0.0f) / 2.0f;
        this.shadowHeight = i2 - f2;
        this.bTop = 0.0f;
        this.bLeft = 0.0f;
        this.bBottom = f2;
        this.bRight = f2;
        float f5 = f2 - 0.0f;
        this.bWidth = f5;
        float f6 = (f2 - 0.0f) / 2.0f;
        float f7 = 0.95f * f6;
        this.bRadius = f7;
        float f8 = 0.2f * f7;
        this.bOffset = f8;
        float f9 = (f6 - f7) * 2.0f;
        this.bStrokeWidth = f9;
        float f10 = f3 - f5;
        this.bOnLeftX = f10;
        this.bOn2LeftX = f10 - f8;
        this.bOffLeftX = 0.0f;
        this.bOff2LeftX = 0.0f;
        this.sScale = 1.0f - (f9 / f4);
        float f11 = this.sLeft;
        float f12 = this.sTop;
        float f13 = this.sBottom;
        RectF rectF = new RectF(f11, f12, f13, f13);
        this.sPath.arcTo(rectF, 90.0f, 180.0f);
        rectF.left = this.sRight - this.sBottom;
        rectF.right = this.sRight;
        this.sPath.arcTo(rectF, 270.0f, 180.0f);
        this.sPath.close();
        this.bRectF.left = this.bLeft;
        this.bRectF.right = this.bRight;
        this.bRectF.top = this.bTop + (this.bStrokeWidth / 2.0f);
        this.bRectF.bottom = this.bBottom - (this.bStrokeWidth / 2.0f);
        float f14 = this.bWidth;
        this.shadowGradient = new RadialGradient(f14 / 2.0f, f14 / 2.0f, f14 / 2.0f, ViewCompat.MEASURED_STATE_MASK, 0, Shader.TileMode.CLAMP);
    }

    private void calcBPath(float f) {
        this.bPath.reset();
        this.bRectF.left = this.bLeft + (this.bStrokeWidth / 2.0f);
        this.bRectF.right = this.bRight - (this.bStrokeWidth / 2.0f);
        this.bPath.arcTo(this.bRectF, 90.0f, 180.0f);
        this.bRectF.left = this.bLeft + (this.bOffset * f) + (this.bStrokeWidth / 2.0f);
        this.bRectF.right = (this.bRight + (f * this.bOffset)) - (this.bStrokeWidth / 2.0f);
        this.bPath.arcTo(this.bRectF, 270.0f, 180.0f);
        this.bPath.close();
    }

    private float calcBTranslate(float f) {
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        int i = this.state;
        int i2 = i - this.lastState;
        if (i2 == -3) {
            f2 = this.bOffLeftX;
            f3 = this.bOnLeftX;
        } else {
            if (i2 != -2) {
                if (i2 != -1) {
                    if (i2 == 1) {
                        if (i == 2) {
                            f5 = this.bOff2LeftX;
                            f6 = this.bOffLeftX;
                        } else if (i == 4) {
                            f5 = this.bOnLeftX;
                            f6 = this.bOn2LeftX;
                        }
                        f4 = f5 - ((f5 - f6) * f);
                    } else if (i2 == 2) {
                        if (i == 4) {
                            f5 = this.bOnLeftX;
                            f6 = this.bOff2LeftX;
                        } else if (i == 4) {
                            f5 = this.bOn2LeftX;
                            f6 = this.bOffLeftX;
                        }
                        f4 = f5 - ((f5 - f6) * f);
                    } else if (i2 == 3) {
                        f5 = this.bOnLeftX;
                        f6 = this.bOffLeftX;
                        f4 = f5 - ((f5 - f6) * f);
                    }
                } else if (i == 3) {
                    f2 = this.bOn2LeftX;
                    f3 = this.bOnLeftX;
                } else if (i == 1) {
                    f2 = this.bOffLeftX;
                    f3 = this.bOff2LeftX;
                }
                f4 = 0.0f;
            } else if (i == 1) {
                f2 = this.bOffLeftX;
                f3 = this.bOn2LeftX;
            } else if (i == 2) {
                f2 = this.bOff2LeftX;
                f3 = this.bOnLeftX;
            } else {
                f4 = 0.0f;
            }
            return f4 - this.bOffLeftX;
        }
        f4 = f2 + ((f3 - f2) * f);
        return f4 - this.bOffLeftX;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        boolean z = true;
        this.paint.setAntiAlias(true);
        int i = this.state;
        boolean z2 = i == 4 || i == 3;
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setColor(z2 ? -11806877 : -1842205);
        canvas.drawPath(this.sPath, this.paint);
        float f = this.sAnim;
        float f2 = f - 0.1f > 0.0f ? f - 0.1f : 0.0f;
        this.sAnim = f2;
        float f3 = this.bAnim;
        this.bAnim = f3 - 0.1f > 0.0f ? f3 - 0.1f : 0.0f;
        float f4 = this.sScale * (z2 ? f2 : 1.0f - f2);
        float f5 = (this.bOnLeftX + this.bRadius) - this.sCenterX;
        if (z2) {
            f2 = 1.0f - f2;
        }
        canvas.save();
        canvas.scale(f4, f4, this.sCenterX + (f5 * f2), this.sCenterY);
        this.paint.setColor(-1);
        canvas.drawPath(this.sPath, this.paint);
        canvas.restore();
        canvas.save();
        canvas.translate(calcBTranslate(this.bAnim), this.shadowHeight);
        int i2 = this.state;
        if (i2 != 3 && i2 != 2) {
            z = false;
        }
        calcBPath(z ? 1.0f - this.bAnim : this.bAnim);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setColor(-13421773);
        this.paint.setShader(this.shadowGradient);
        canvas.drawPath(this.bPath, this.paint);
        this.paint.setShader(null);
        canvas.translate(0.0f, -this.shadowHeight);
        float f6 = this.bWidth;
        canvas.scale(0.98f, 0.98f, f6 / 2.0f, f6 / 2.0f);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setColor(-1);
        canvas.drawPath(this.bPath, this.paint);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(this.bStrokeWidth * 0.5f);
        this.paint.setColor(z2 ? -11871648 : -4210753);
        canvas.drawPath(this.bPath, this.paint);
        canvas.restore();
        this.paint.reset();
        if (this.sAnim > 0.0f || this.bAnim > 0.0f) {
            invalidate();
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int i = this.state;
        if ((i == 4 || i == 1) && this.sAnim * this.bAnim == 0.0f) {
            int action = motionEvent.getAction();
            if (action == 0) {
                return true;
            }
            if (action == 1 || action == 3) {
                int i2 = this.state;
                this.lastState = i2;
                if (i2 == 1) {
                    refreshState(2);
                } else if (i2 == 4) {
                    refreshState(3);
                }
                this.bAnim = 1.0f;
                invalidate();
                OnSwitchStateChangedListener onSwitchStateChangedListener = this.listener;
                if (onSwitchStateChangedListener != null) {
                    int i3 = this.state;
                    if (i3 == 2) {
                        onSwitchStateChangedListener.stateChangeListener(true, this.tag);
                        toggleSwitch(4);
                    } else if (i3 == 3) {
                        onSwitchStateChangedListener.stateChangeListener(false, this.tag);
                        toggleSwitch(1);
                    }
                }
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    private void refreshState(int i) {
        this.lastState = this.state;
        this.state = i;
        postInvalidate();
    }

    public int getState() {
        return this.state;
    }

    public void setState(boolean z) {
        refreshState(z ? 4 : 1);
    }

    public void toggleSwitch(boolean z) {
        final int i = z ? 4 : 1;
        postDelayed(new Runnable() { // from class: im.helmsman.helmsmanandroid.view.SwitchView.1
            @Override // java.lang.Runnable
            public void run() {
                SwitchView.this.toggleSwitch(i);
            }
        }, 300L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:20:0x001e A[Catch: all -> 0x0013, TryCatch #0 {all -> 0x0013, blocks: (B:8:0x000b, B:20:0x001e, B:21:0x0020, B:16:0x0017), top: B:26:0x000b }] */
    public synchronized void toggleSwitch(int i) {
        int i2;
        if (i == 4 || i == 1) {
            if (i == 4) {
                try {
                    int i3 = this.lastState;
                    if (i3 == 1 || i3 == 2) {
                        this.sAnim = 1.0f;
                    } else if (i == 1 && ((i2 = this.lastState) == 4 || i2 == 3)) {
                        this.sAnim = 1.0f;
                    }
                    this.bAnim = 1.0f;
                    refreshState(i);
                } catch (Throwable th) {
                    throw th;
                }
            } else {
                if (i == 1) {
                    this.sAnim = 1.0f;
                }
                this.bAnim = 1.0f;
                refreshState(i);
            }
        }
    }

    public void setOnStateChangedListener(OnSwitchStateChangedListener onSwitchStateChangedListener) {
        if (onSwitchStateChangedListener == null) {
            throw new IllegalArgumentException("empty listener");
        }
        this.listener = onSwitchStateChangedListener;
    }

    public void setOnStateChangedListener(OnSwitchStateChangedListener onSwitchStateChangedListener, int i) {
        if (onSwitchStateChangedListener == null) {
            throw new IllegalArgumentException("empty listener");
        }
        this.listener = onSwitchStateChangedListener;
        this.tag = i;
    }
}
