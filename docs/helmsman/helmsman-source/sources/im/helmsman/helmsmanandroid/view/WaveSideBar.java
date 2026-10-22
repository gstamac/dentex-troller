package im.helmsman.helmsmanandroid.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import androidx.exifinterface.media.ExifInterface;
import im.helmsman.helmsmanandroid.R;
import java.util.Arrays;

/* JADX INFO: loaded from: classes2.dex */
public class WaveSideBar extends View {
    private static final String[] DEFAULT_INDEX_ITEMS = {ExifInterface.GPS_MEASUREMENT_IN_PROGRESS, "B", "C", "D", ExifInterface.LONGITUDE_EAST, "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", ExifInterface.LATITUDE_SOUTH, ExifInterface.GPS_DIRECTION_TRUE, "U", ExifInterface.GPS_MEASUREMENT_INTERRUPTED, ExifInterface.LONGITUDE_WEST, "X", "Y", "Z", "#"};
    private static final int DEFAULT_MAX_OFFSET = 80;
    private static final int DEFAULT_TEXT_SIZE = 12;
    public static final int POSITION_LEFT = 1;
    public static final int POSITION_RIGHT = 0;
    public static final int TEXT_ALIGN_CENTER = 0;
    public static final int TEXT_ALIGN_LEFT = 1;
    public static final int TEXT_ALIGN_RIGHT = 2;
    private float mBarHeight;
    private float mBarWidth;
    private int mCurrentIndex;
    private float mCurrentY;
    private DisplayMetrics mDisplayMetrics;
    private float mFirstItemBaseLineY;
    private float mIndexItemHeight;
    private String[] mIndexItems;
    private boolean mLazyRespond;
    private float mMaxOffset;
    private Paint mPaint;
    private int mSideBarPosition;
    private boolean mStartTouching;
    private RectF mStartTouchingArea;
    private int mTextAlignment;
    private int mTextColor;
    private float mTextSize;
    private OnSelectIndexItemListener onSelectIndexItemListener;

    public interface OnSelectIndexItemListener {
        void onSelectIndexItem(String str);
    }

    public WaveSideBar(Context context) {
        this(context, null);
    }

    public WaveSideBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public WaveSideBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mCurrentIndex = -1;
        this.mCurrentY = -1.0f;
        this.mStartTouchingArea = new RectF();
        this.mStartTouching = false;
        this.mLazyRespond = false;
        this.mDisplayMetrics = context.getResources().getDisplayMetrics();
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.WaveSideBar);
        this.mLazyRespond = typedArrayObtainStyledAttributes.getBoolean(0, false);
        this.mTextColor = typedArrayObtainStyledAttributes.getColor(4, -7829368);
        this.mMaxOffset = typedArrayObtainStyledAttributes.getDimension(1, dp2px(80));
        this.mSideBarPosition = typedArrayObtainStyledAttributes.getInt(2, 0);
        this.mTextAlignment = typedArrayObtainStyledAttributes.getInt(3, 0);
        typedArrayObtainStyledAttributes.recycle();
        this.mTextSize = sp2px(12);
        this.mIndexItems = DEFAULT_INDEX_ITEMS;
        initPaint();
    }

    private void initPaint() {
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setAntiAlias(true);
        this.mPaint.setColor(this.mTextColor);
        this.mPaint.setTextSize(this.mTextSize);
        int i = this.mTextAlignment;
        if (i == 0) {
            this.mPaint.setTextAlign(Paint.Align.CENTER);
        } else if (i == 1) {
            this.mPaint.setTextAlign(Paint.Align.LEFT);
        } else {
            if (i != 2) {
                return;
            }
            this.mPaint.setTextAlign(Paint.Align.RIGHT);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int size = View.MeasureSpec.getSize(i2);
        int size2 = View.MeasureSpec.getSize(i);
        Paint.FontMetrics fontMetrics = this.mPaint.getFontMetrics();
        float f = fontMetrics.bottom - fontMetrics.top;
        this.mIndexItemHeight = f;
        String[] strArr = this.mIndexItems;
        this.mBarHeight = strArr.length * f;
        for (String str : strArr) {
            this.mBarWidth = Math.max(this.mBarWidth, this.mPaint.measureText(str));
        }
        float paddingRight = this.mSideBarPosition == 1 ? 0.0f : (size2 - this.mBarWidth) - getPaddingRight();
        float paddingLeft = this.mSideBarPosition == 1 ? getPaddingLeft() + paddingRight + this.mBarWidth : size2;
        float f2 = size / 2;
        float f3 = this.mBarHeight;
        float f4 = f2 - (f3 / 2.0f);
        this.mStartTouchingArea.set(paddingRight, f4, paddingLeft, f3 + f4);
        float length = this.mIndexItems.length;
        float f5 = this.mIndexItemHeight;
        this.mFirstItemBaseLineY = ((f2 - ((length * f5) / 2.0f)) + ((f5 / 2.0f) - ((fontMetrics.descent - fontMetrics.ascent) / 2.0f))) - fontMetrics.ascent;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        float width;
        float f;
        float paddingLeft;
        float f2;
        super.onDraw(canvas);
        int length = this.mIndexItems.length;
        int i = 0;
        while (true) {
            if (i < length) {
                float f3 = this.mFirstItemBaseLineY + (this.mIndexItemHeight * i);
                float itemScale = getItemScale(i);
                this.mPaint.setAlpha(i != this.mCurrentIndex ? (int) ((1.0f - itemScale) * 255.0f) : 255);
                Paint paint = this.mPaint;
                float f4 = this.mTextSize;
                paint.setTextSize(f4 + (f4 * itemScale));
                float f5 = 0.0f;
                if (this.mSideBarPosition == 1) {
                    int i2 = this.mTextAlignment;
                    if (i2 == 0) {
                        paddingLeft = getPaddingLeft() + (this.mBarWidth / 2.0f);
                        f2 = this.mMaxOffset;
                    } else if (i2 == 1) {
                        paddingLeft = getPaddingLeft();
                        f2 = this.mMaxOffset;
                    } else if (i2 == 2) {
                        paddingLeft = getPaddingLeft() + this.mBarWidth;
                        f2 = this.mMaxOffset;
                    }
                    f5 = paddingLeft + (f2 * itemScale);
                } else {
                    int i3 = this.mTextAlignment;
                    if (i3 == 0) {
                        width = (getWidth() - getPaddingRight()) - (this.mBarWidth / 2.0f);
                        f = this.mMaxOffset;
                    } else if (i3 == 1) {
                        width = (getWidth() - getPaddingRight()) - this.mBarWidth;
                        f = this.mMaxOffset;
                    } else if (i3 == 2) {
                        width = getWidth() - getPaddingRight();
                        f = this.mMaxOffset;
                    }
                    f5 = width - (f * itemScale);
                }
                canvas.drawText(this.mIndexItems[i], f5, f3, this.mPaint);
                i++;
            } else {
                this.mPaint.setAlpha(255);
                this.mPaint.setTextSize(this.mTextSize);
                return;
            }
        }
    }

    private float getItemScale(int i) {
        if (this.mCurrentIndex == -1) {
            return 0.0f;
        }
        float f = this.mCurrentY;
        float f2 = this.mIndexItemHeight;
        float fAbs = Math.abs(f - ((i * f2) + (f2 / 2.0f))) / this.mIndexItemHeight;
        return Math.max(1.0f - ((fAbs * fAbs) / 16.0f), 0.0f);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        OnSelectIndexItemListener onSelectIndexItemListener;
        OnSelectIndexItemListener onSelectIndexItemListener2;
        OnSelectIndexItemListener onSelectIndexItemListener3;
        if (this.mIndexItems.length == 0) {
            return super.onTouchEvent(motionEvent);
        }
        float y = motionEvent.getY();
        float x = motionEvent.getX();
        this.mCurrentIndex = getSelectedIndex(y);
        int action = motionEvent.getAction();
        if (action == 0) {
            if (this.mStartTouchingArea.contains(x, y)) {
                this.mStartTouching = true;
                if (!this.mLazyRespond && (onSelectIndexItemListener = this.onSelectIndexItemListener) != null) {
                    onSelectIndexItemListener.onSelectIndexItem(this.mIndexItems[this.mCurrentIndex]);
                }
                invalidate();
                return true;
            }
            this.mCurrentIndex = -1;
            return false;
        }
        if (action != 1) {
            if (action == 2) {
                if (this.mStartTouching && !this.mLazyRespond && (onSelectIndexItemListener3 = this.onSelectIndexItemListener) != null) {
                    onSelectIndexItemListener3.onSelectIndexItem(this.mIndexItems[this.mCurrentIndex]);
                }
                invalidate();
                return true;
            }
            if (action != 3) {
                return super.onTouchEvent(motionEvent);
            }
        }
        if (this.mLazyRespond && (onSelectIndexItemListener2 = this.onSelectIndexItemListener) != null) {
            onSelectIndexItemListener2.onSelectIndexItem(this.mIndexItems[this.mCurrentIndex]);
        }
        this.mCurrentIndex = -1;
        this.mStartTouching = false;
        invalidate();
        return true;
    }

    private int getSelectedIndex(float f) {
        float height = f - ((getHeight() / 2) - (this.mBarHeight / 2.0f));
        this.mCurrentY = height;
        if (height <= 0.0f) {
            return 0;
        }
        int i = (int) (height / this.mIndexItemHeight);
        String[] strArr = this.mIndexItems;
        return i >= strArr.length ? strArr.length - 1 : i;
    }

    private float dp2px(int i) {
        return TypedValue.applyDimension(1, i, this.mDisplayMetrics);
    }

    private float sp2px(int i) {
        return TypedValue.applyDimension(2, i, this.mDisplayMetrics);
    }

    public void setIndexItems(String... strArr) {
        this.mIndexItems = (String[]) Arrays.copyOf(strArr, strArr.length);
        requestLayout();
    }

    public void setTextColor(int i) {
        this.mTextColor = i;
        this.mPaint.setColor(i);
        invalidate();
    }

    public void setPosition(int i) {
        if (i != 0 && i != 1) {
            throw new IllegalArgumentException("the position must be POSITION_RIGHT or POSITION_LEFT");
        }
        this.mSideBarPosition = i;
        requestLayout();
    }

    public void setMaxOffset(int i) {
        this.mMaxOffset = i;
        invalidate();
    }

    public void setLazyRespond(boolean z) {
        this.mLazyRespond = z;
    }

    public void setTextAlign(int i) {
        if (this.mTextAlignment == i) {
            return;
        }
        if (i == 0) {
            this.mPaint.setTextAlign(Paint.Align.CENTER);
        } else if (i == 1) {
            this.mPaint.setTextAlign(Paint.Align.LEFT);
        } else if (i == 2) {
            this.mPaint.setTextAlign(Paint.Align.RIGHT);
        } else {
            throw new IllegalArgumentException("the alignment must be TEXT_ALIGN_CENTER, TEXT_ALIGN_LEFT or TEXT_ALIGN_RIGHT");
        }
        this.mTextAlignment = i;
        invalidate();
    }

    public void setOnSelectIndexItemListener(OnSelectIndexItemListener onSelectIndexItemListener) {
        this.onSelectIndexItemListener = onSelectIndexItemListener;
    }
}
