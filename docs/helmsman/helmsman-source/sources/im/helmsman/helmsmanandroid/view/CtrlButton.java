package im.helmsman.helmsmanandroid.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.view.ViewCompat;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.utils.ViewUtils;

/* JADX INFO: loaded from: classes2.dex */
public class CtrlButton extends View {
    private Handler handler;
    private float height;
    private boolean isFlicker;
    private boolean isShowCenterView;
    private Bitmap mBitMap;
    private Context mContext;
    private int mImageHeight;
    private int mImageWidth;
    private int mRadius;
    private Runnable runnable;
    private float width;

    public CtrlButton(Context context) {
        super(context);
        this.mImageWidth = 0;
        this.mImageHeight = 0;
        this.isShowCenterView = true;
        this.isFlicker = false;
        this.handler = new Handler() { // from class: im.helmsman.helmsmanandroid.view.CtrlButton.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
            }
        };
        this.runnable = new MyTaskThread();
    }

    public CtrlButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mImageWidth = 0;
        this.mImageHeight = 0;
        this.isShowCenterView = true;
        this.isFlicker = false;
        this.handler = new Handler() { // from class: im.helmsman.helmsmanandroid.view.CtrlButton.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
            }
        };
        this.runnable = new MyTaskThread();
        this.mContext = context;
        Bitmap bitmapDecodeResource = BitmapFactory.decodeResource(context.getResources(), context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.CustomView, 0, 0).getResourceId(0, 0));
        this.mBitMap = bitmapDecodeResource;
        this.mImageHeight = bitmapDecodeResource.getHeight();
        this.mImageWidth = this.mBitMap.getWidth();
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        int size2 = View.MeasureSpec.getSize(i2);
        if (mode != 1073741824 && mode2 != 1073741824) {
            int i3 = this.mImageHeight;
            int i4 = this.mImageWidth;
            this.mRadius = ((int) Math.sqrt((i3 * i3) + (i4 * i4))) / 2;
        } else if (size > size2) {
            this.mRadius = size2 / 2;
        } else {
            this.mRadius = size / 2;
        }
        this.width = getPaddingLeft() + (this.mRadius * 2) + getPaddingRight();
        float paddingTop = getPaddingTop() + (this.mRadius * 2) + getPaddingBottom();
        this.height = paddingTop;
        setMeasuredDimension((int) this.width, (int) paddingTop);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = (this.width * 0.5f) / this.mBitMap.getWidth();
        float height = (this.height * 0.5f) / this.mBitMap.getHeight();
        if (width > height) {
            width = height;
        }
        Paint paint = new Paint();
        paint.setColor(-1);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, this.mRadius, paint);
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, this.mRadius - ViewUtils.dpToPx(1), paint);
        if (this.isShowCenterView) {
            Matrix matrix = new Matrix();
            matrix.setScale(width, width);
            Bitmap bitmap = this.mBitMap;
            Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), this.mBitMap.getHeight(), matrix, true);
            canvas.drawBitmap(bitmapCreateBitmap, (this.width / 2.0f) - (bitmapCreateBitmap.getWidth() / 2), (this.height / 2.0f) - (bitmapCreateBitmap.getHeight() / 2), paint);
        }
    }

    public void setViewDrawable(Bitmap bitmap) {
        this.mBitMap = bitmap;
        initViewAcreAge();
        invalidate();
    }

    public void setViewDrawable(int i) {
        this.mBitMap = BitmapFactory.decodeResource(this.mContext.getResources(), i);
        initViewAcreAge();
        invalidate();
    }

    private void initViewAcreAge() {
        this.mImageHeight = this.mBitMap.getHeight();
        this.mImageWidth = this.mBitMap.getWidth();
    }

    public void flicker() {
        if (this.isFlicker) {
            return;
        }
        this.handler.postDelayed(this.runnable, 250L);
        this.isFlicker = true;
    }

    public void cancelFlicker() {
        if (this.isFlicker) {
            this.isShowCenterView = true;
            this.isFlicker = false;
            this.handler.removeCallbacks(this.runnable);
        }
    }

    private class MyTaskThread implements Runnable {
        private MyTaskThread() {
        }

        @Override // java.lang.Runnable
        public void run() {
            CtrlButton ctrlButton = CtrlButton.this;
            ctrlButton.isShowCenterView = !ctrlButton.isShowCenterView;
            CtrlButton.this.invalidate();
            CtrlButton.this.handler.postDelayed(CtrlButton.this.runnable, 250L);
        }
    }
}
