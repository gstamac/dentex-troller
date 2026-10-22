package cn.bingoogolapple.qrcode.core;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/* JADX INFO: loaded from: classes.dex */
public class ScanBoxView extends View {
    private int mAnimDelayTime;
    private int mAnimTime;
    private String mBarCodeTipText;
    private int mBarcodeRectHeight;
    private int mBorderColor;
    private int mBorderSize;
    private int mCornerColor;
    private int mCornerLength;
    private int mCornerSize;
    private Drawable mCustomGridScanLineDrawable;
    private Drawable mCustomScanLineDrawable;
    private Rect mFramingRect;
    private Bitmap mGridScanLineBitmap;
    private float mGridScanLineBottom;
    private float mGridScanLineRight;
    private float mHalfCornerSize;
    private boolean mIsBarcode;
    private boolean mIsCenterVertical;
    private boolean mIsOnlyDecodeScanBoxArea;
    private boolean mIsScanLineReverse;
    private boolean mIsShowDefaultGridScanLineDrawable;
    private boolean mIsShowDefaultScanLineDrawable;
    private boolean mIsShowTipBackground;
    private boolean mIsShowTipTextAsSingleLine;
    private boolean mIsTipTextBelowRect;
    private int mMaskColor;
    private int mMoveStepDistance;
    private Bitmap mOriginBarCodeGridScanLineBitmap;
    private Bitmap mOriginBarCodeScanLineBitmap;
    private Bitmap mOriginQRCodeGridScanLineBitmap;
    private Bitmap mOriginQRCodeScanLineBitmap;
    private Paint mPaint;
    private String mQRCodeTipText;
    private int mRectHeight;
    private int mRectWidth;
    private Bitmap mScanLineBitmap;
    private int mScanLineColor;
    private float mScanLineLeft;
    private int mScanLineMargin;
    private int mScanLineSize;
    private float mScanLineTop;
    private int mTipBackgroundColor;
    private int mTipBackgroundRadius;
    private TextPaint mTipPaint;
    private String mTipText;
    private int mTipTextColor;
    private int mTipTextMargin;
    private int mTipTextSize;
    private StaticLayout mTipTextSl;
    private int mToolbarHeight;
    private int mTopOffset;

    public ScanBoxView(Context context) {
        super(context);
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setAntiAlias(true);
        this.mMaskColor = Color.parseColor("#33FFFFFF");
        this.mCornerColor = -1;
        this.mCornerLength = BGAQRCodeUtil.dp2px(context, 20.0f);
        this.mCornerSize = BGAQRCodeUtil.dp2px(context, 3.0f);
        this.mScanLineSize = BGAQRCodeUtil.dp2px(context, 1.0f);
        this.mScanLineColor = -1;
        this.mTopOffset = BGAQRCodeUtil.dp2px(context, 90.0f);
        this.mRectWidth = BGAQRCodeUtil.dp2px(context, 200.0f);
        this.mBarcodeRectHeight = BGAQRCodeUtil.dp2px(context, 140.0f);
        this.mScanLineMargin = 0;
        this.mIsShowDefaultScanLineDrawable = false;
        this.mCustomScanLineDrawable = null;
        this.mScanLineBitmap = null;
        this.mBorderSize = BGAQRCodeUtil.dp2px(context, 1.0f);
        this.mBorderColor = -1;
        this.mAnimTime = 1000;
        this.mIsCenterVertical = false;
        this.mToolbarHeight = 0;
        this.mIsBarcode = false;
        this.mMoveStepDistance = BGAQRCodeUtil.dp2px(context, 2.0f);
        this.mTipText = null;
        this.mTipTextSize = BGAQRCodeUtil.sp2px(context, 14.0f);
        this.mTipTextColor = -1;
        this.mIsTipTextBelowRect = false;
        this.mTipTextMargin = BGAQRCodeUtil.dp2px(context, 20.0f);
        this.mIsShowTipTextAsSingleLine = false;
        this.mTipBackgroundColor = Color.parseColor("#22000000");
        this.mIsShowTipBackground = false;
        this.mIsScanLineReverse = false;
        this.mIsShowDefaultGridScanLineDrawable = false;
        TextPaint textPaint = new TextPaint();
        this.mTipPaint = textPaint;
        textPaint.setAntiAlias(true);
        this.mTipBackgroundRadius = BGAQRCodeUtil.dp2px(context, 4.0f);
        this.mIsOnlyDecodeScanBoxArea = false;
    }

    public void initCustomAttrs(Context context, AttributeSet attributeSet) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.QRCodeView);
        int indexCount = typedArrayObtainStyledAttributes.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            initCustomAttr(typedArrayObtainStyledAttributes.getIndex(i), typedArrayObtainStyledAttributes);
        }
        typedArrayObtainStyledAttributes.recycle();
        afterInitCustomAttrs();
    }

    private void initCustomAttr(int i, TypedArray typedArray) {
        if (i == R.styleable.QRCodeView_qrcv_topOffset) {
            this.mTopOffset = typedArray.getDimensionPixelSize(i, this.mTopOffset);
            return;
        }
        if (i == R.styleable.QRCodeView_qrcv_cornerSize) {
            this.mCornerSize = typedArray.getDimensionPixelSize(i, this.mCornerSize);
            return;
        }
        if (i == R.styleable.QRCodeView_qrcv_cornerLength) {
            this.mCornerLength = typedArray.getDimensionPixelSize(i, this.mCornerLength);
            return;
        }
        if (i == R.styleable.QRCodeView_qrcv_scanLineSize) {
            this.mScanLineSize = typedArray.getDimensionPixelSize(i, this.mScanLineSize);
            return;
        }
        if (i == R.styleable.QRCodeView_qrcv_rectWidth) {
            this.mRectWidth = typedArray.getDimensionPixelSize(i, this.mRectWidth);
            return;
        }
        if (i == R.styleable.QRCodeView_qrcv_maskColor) {
            this.mMaskColor = typedArray.getColor(i, this.mMaskColor);
            return;
        }
        if (i == R.styleable.QRCodeView_qrcv_cornerColor) {
            this.mCornerColor = typedArray.getColor(i, this.mCornerColor);
            return;
        }
        if (i == R.styleable.QRCodeView_qrcv_scanLineColor) {
            this.mScanLineColor = typedArray.getColor(i, this.mScanLineColor);
            return;
        }
        if (i == R.styleable.QRCodeView_qrcv_scanLineMargin) {
            this.mScanLineMargin = typedArray.getDimensionPixelSize(i, this.mScanLineMargin);
            return;
        }
        if (i == R.styleable.QRCodeView_qrcv_isShowDefaultScanLineDrawable) {
            this.mIsShowDefaultScanLineDrawable = typedArray.getBoolean(i, this.mIsShowDefaultScanLineDrawable);
            return;
        }
        if (i == R.styleable.QRCodeView_qrcv_customScanLineDrawable) {
            this.mCustomScanLineDrawable = typedArray.getDrawable(i);
            return;
        }
        if (i == R.styleable.QRCodeView_qrcv_borderSize) {
            this.mBorderSize = typedArray.getDimensionPixelSize(i, this.mBorderSize);
            return;
        }
        if (i == R.styleable.QRCodeView_qrcv_borderColor) {
            this.mBorderColor = typedArray.getColor(i, this.mBorderColor);
            return;
        }
        if (i == R.styleable.QRCodeView_qrcv_animTime) {
            this.mAnimTime = typedArray.getInteger(i, this.mAnimTime);
            return;
        }
        if (i == R.styleable.QRCodeView_qrcv_isCenterVertical) {
            this.mIsCenterVertical = typedArray.getBoolean(i, this.mIsCenterVertical);
            return;
        }
        if (i == R.styleable.QRCodeView_qrcv_toolbarHeight) {
            this.mToolbarHeight = typedArray.getDimensionPixelSize(i, this.mToolbarHeight);
            return;
        }
        if (i == R.styleable.QRCodeView_qrcv_barcodeRectHeight) {
            this.mBarcodeRectHeight = typedArray.getDimensionPixelSize(i, this.mBarcodeRectHeight);
            return;
        }
        if (i == R.styleable.QRCodeView_qrcv_isBarcode) {
            this.mIsBarcode = typedArray.getBoolean(i, this.mIsBarcode);
            return;
        }
        if (i == R.styleable.QRCodeView_qrcv_barCodeTipText) {
            this.mBarCodeTipText = typedArray.getString(i);
            return;
        }
        if (i == R.styleable.QRCodeView_qrcv_qrCodeTipText) {
            this.mQRCodeTipText = typedArray.getString(i);
            return;
        }
        if (i == R.styleable.QRCodeView_qrcv_tipTextSize) {
            this.mTipTextSize = typedArray.getDimensionPixelSize(i, this.mTipTextSize);
            return;
        }
        if (i == R.styleable.QRCodeView_qrcv_tipTextColor) {
            this.mTipTextColor = typedArray.getColor(i, this.mTipTextColor);
            return;
        }
        if (i == R.styleable.QRCodeView_qrcv_isTipTextBelowRect) {
            this.mIsTipTextBelowRect = typedArray.getBoolean(i, this.mIsTipTextBelowRect);
            return;
        }
        if (i == R.styleable.QRCodeView_qrcv_tipTextMargin) {
            this.mTipTextMargin = typedArray.getDimensionPixelSize(i, this.mTipTextMargin);
            return;
        }
        if (i == R.styleable.QRCodeView_qrcv_isShowTipTextAsSingleLine) {
            this.mIsShowTipTextAsSingleLine = typedArray.getBoolean(i, this.mIsShowTipTextAsSingleLine);
            return;
        }
        if (i == R.styleable.QRCodeView_qrcv_isShowTipBackground) {
            this.mIsShowTipBackground = typedArray.getBoolean(i, this.mIsShowTipBackground);
            return;
        }
        if (i == R.styleable.QRCodeView_qrcv_tipBackgroundColor) {
            this.mTipBackgroundColor = typedArray.getColor(i, this.mTipBackgroundColor);
            return;
        }
        if (i == R.styleable.QRCodeView_qrcv_isScanLineReverse) {
            this.mIsScanLineReverse = typedArray.getBoolean(i, this.mIsScanLineReverse);
            return;
        }
        if (i == R.styleable.QRCodeView_qrcv_isShowDefaultGridScanLineDrawable) {
            this.mIsShowDefaultGridScanLineDrawable = typedArray.getBoolean(i, this.mIsShowDefaultGridScanLineDrawable);
        } else if (i == R.styleable.QRCodeView_qrcv_customGridScanLineDrawable) {
            this.mCustomGridScanLineDrawable = typedArray.getDrawable(i);
        } else if (i == R.styleable.QRCodeView_qrcv_isOnlyDecodeScanBoxArea) {
            this.mIsOnlyDecodeScanBoxArea = typedArray.getBoolean(i, this.mIsOnlyDecodeScanBoxArea);
        }
    }

    private void afterInitCustomAttrs() {
        Drawable drawable = this.mCustomGridScanLineDrawable;
        if (drawable != null) {
            this.mOriginQRCodeGridScanLineBitmap = ((BitmapDrawable) drawable).getBitmap();
        }
        if (this.mOriginQRCodeGridScanLineBitmap == null) {
            Bitmap bitmapDecodeResource = BitmapFactory.decodeResource(getResources(), R.mipmap.qrcode_default_grid_scan_line);
            this.mOriginQRCodeGridScanLineBitmap = bitmapDecodeResource;
            this.mOriginQRCodeGridScanLineBitmap = BGAQRCodeUtil.makeTintBitmap(bitmapDecodeResource, this.mScanLineColor);
        }
        Bitmap bitmapAdjustPhotoRotation = BGAQRCodeUtil.adjustPhotoRotation(this.mOriginQRCodeGridScanLineBitmap, 90);
        this.mOriginBarCodeGridScanLineBitmap = bitmapAdjustPhotoRotation;
        Bitmap bitmapAdjustPhotoRotation2 = BGAQRCodeUtil.adjustPhotoRotation(bitmapAdjustPhotoRotation, 90);
        this.mOriginBarCodeGridScanLineBitmap = bitmapAdjustPhotoRotation2;
        this.mOriginBarCodeGridScanLineBitmap = BGAQRCodeUtil.adjustPhotoRotation(bitmapAdjustPhotoRotation2, 90);
        Drawable drawable2 = this.mCustomScanLineDrawable;
        if (drawable2 != null) {
            this.mOriginQRCodeScanLineBitmap = ((BitmapDrawable) drawable2).getBitmap();
        }
        if (this.mOriginQRCodeScanLineBitmap == null) {
            Bitmap bitmapDecodeResource2 = BitmapFactory.decodeResource(getResources(), R.mipmap.qrcode_default_scan_line);
            this.mOriginQRCodeScanLineBitmap = bitmapDecodeResource2;
            this.mOriginQRCodeScanLineBitmap = BGAQRCodeUtil.makeTintBitmap(bitmapDecodeResource2, this.mScanLineColor);
        }
        this.mOriginBarCodeScanLineBitmap = BGAQRCodeUtil.adjustPhotoRotation(this.mOriginQRCodeScanLineBitmap, 90);
        this.mTopOffset += this.mToolbarHeight;
        this.mHalfCornerSize = (this.mCornerSize * 1.0f) / 2.0f;
        this.mTipPaint.setTextSize(this.mTipTextSize);
        this.mTipPaint.setColor(this.mTipTextColor);
        setIsBarcode(this.mIsBarcode);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        if (this.mFramingRect == null) {
            return;
        }
        drawMask(canvas);
        drawBorderLine(canvas);
        drawCornerLine(canvas);
        drawScanLine(canvas);
        drawTipText(canvas);
        moveScanLine();
    }

    private void drawMask(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        if (this.mMaskColor != 0) {
            this.mPaint.setStyle(Paint.Style.FILL);
            this.mPaint.setColor(this.mMaskColor);
            float f = width;
            canvas.drawRect(0.0f, 0.0f, f, this.mFramingRect.top, this.mPaint);
            canvas.drawRect(0.0f, this.mFramingRect.top, this.mFramingRect.left, this.mFramingRect.bottom + 1, this.mPaint);
            canvas.drawRect(this.mFramingRect.right + 1, this.mFramingRect.top, f, this.mFramingRect.bottom + 1, this.mPaint);
            canvas.drawRect(0.0f, this.mFramingRect.bottom + 1, f, height, this.mPaint);
        }
    }

    private void drawBorderLine(Canvas canvas) {
        if (this.mBorderSize > 0) {
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaint.setColor(this.mBorderColor);
            this.mPaint.setStrokeWidth(this.mBorderSize);
            canvas.drawRect(this.mFramingRect, this.mPaint);
        }
    }

    private void drawCornerLine(Canvas canvas) {
        if (this.mHalfCornerSize > 0.0f) {
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaint.setColor(this.mCornerColor);
            this.mPaint.setStrokeWidth(this.mCornerSize);
            canvas.drawLine(this.mFramingRect.left - this.mHalfCornerSize, this.mFramingRect.top, (this.mFramingRect.left - this.mHalfCornerSize) + this.mCornerLength, this.mFramingRect.top, this.mPaint);
            canvas.drawLine(this.mFramingRect.left, this.mFramingRect.top - this.mHalfCornerSize, this.mFramingRect.left, (this.mFramingRect.top - this.mHalfCornerSize) + this.mCornerLength, this.mPaint);
            canvas.drawLine(this.mFramingRect.right + this.mHalfCornerSize, this.mFramingRect.top, (this.mFramingRect.right + this.mHalfCornerSize) - this.mCornerLength, this.mFramingRect.top, this.mPaint);
            canvas.drawLine(this.mFramingRect.right, this.mFramingRect.top - this.mHalfCornerSize, this.mFramingRect.right, (this.mFramingRect.top - this.mHalfCornerSize) + this.mCornerLength, this.mPaint);
            canvas.drawLine(this.mFramingRect.left - this.mHalfCornerSize, this.mFramingRect.bottom, (this.mFramingRect.left - this.mHalfCornerSize) + this.mCornerLength, this.mFramingRect.bottom, this.mPaint);
            canvas.drawLine(this.mFramingRect.left, this.mFramingRect.bottom + this.mHalfCornerSize, this.mFramingRect.left, (this.mFramingRect.bottom + this.mHalfCornerSize) - this.mCornerLength, this.mPaint);
            canvas.drawLine(this.mFramingRect.right + this.mHalfCornerSize, this.mFramingRect.bottom, (this.mFramingRect.right + this.mHalfCornerSize) - this.mCornerLength, this.mFramingRect.bottom, this.mPaint);
            canvas.drawLine(this.mFramingRect.right, this.mFramingRect.bottom + this.mHalfCornerSize, this.mFramingRect.right, (this.mFramingRect.bottom + this.mHalfCornerSize) - this.mCornerLength, this.mPaint);
        }
    }

    private void drawScanLine(Canvas canvas) {
        if (this.mIsBarcode) {
            if (this.mGridScanLineBitmap != null) {
                RectF rectF = new RectF(this.mFramingRect.left + this.mHalfCornerSize + 0.5f, this.mFramingRect.top + this.mHalfCornerSize + this.mScanLineMargin, this.mGridScanLineRight, (this.mFramingRect.bottom - this.mHalfCornerSize) - this.mScanLineMargin);
                Rect rect = new Rect((int) (this.mGridScanLineBitmap.getWidth() - rectF.width()), 0, this.mGridScanLineBitmap.getWidth(), this.mGridScanLineBitmap.getHeight());
                if (rect.left < 0) {
                    rect.left = 0;
                    rectF.left = rectF.right - rect.width();
                }
                canvas.drawBitmap(this.mGridScanLineBitmap, rect, rectF, this.mPaint);
                return;
            }
            if (this.mScanLineBitmap != null) {
                canvas.drawBitmap(this.mScanLineBitmap, (Rect) null, new RectF(this.mScanLineLeft, this.mFramingRect.top + this.mHalfCornerSize + this.mScanLineMargin, this.mScanLineLeft + this.mScanLineBitmap.getWidth(), (this.mFramingRect.bottom - this.mHalfCornerSize) - this.mScanLineMargin), this.mPaint);
                return;
            } else {
                this.mPaint.setStyle(Paint.Style.FILL);
                this.mPaint.setColor(this.mScanLineColor);
                canvas.drawRect(this.mScanLineLeft, this.mFramingRect.top + this.mHalfCornerSize + this.mScanLineMargin, this.mScanLineLeft + this.mScanLineSize, (this.mFramingRect.bottom - this.mHalfCornerSize) - this.mScanLineMargin, this.mPaint);
                return;
            }
        }
        if (this.mGridScanLineBitmap != null) {
            RectF rectF2 = new RectF(this.mFramingRect.left + this.mHalfCornerSize + this.mScanLineMargin, this.mFramingRect.top + this.mHalfCornerSize + 0.5f, (this.mFramingRect.right - this.mHalfCornerSize) - this.mScanLineMargin, this.mGridScanLineBottom);
            Rect rect2 = new Rect(0, (int) (this.mGridScanLineBitmap.getHeight() - rectF2.height()), this.mGridScanLineBitmap.getWidth(), this.mGridScanLineBitmap.getHeight());
            if (rect2.top < 0) {
                rect2.top = 0;
                rectF2.top = rectF2.bottom - rect2.height();
            }
            canvas.drawBitmap(this.mGridScanLineBitmap, rect2, rectF2, this.mPaint);
            return;
        }
        if (this.mScanLineBitmap != null) {
            canvas.drawBitmap(this.mScanLineBitmap, (Rect) null, new RectF(this.mFramingRect.left + this.mHalfCornerSize + this.mScanLineMargin, this.mScanLineTop, (this.mFramingRect.right - this.mHalfCornerSize) - this.mScanLineMargin, this.mScanLineTop + this.mScanLineBitmap.getHeight()), this.mPaint);
        } else {
            this.mPaint.setStyle(Paint.Style.FILL);
            this.mPaint.setColor(this.mScanLineColor);
            canvas.drawRect(this.mFramingRect.left + this.mHalfCornerSize + this.mScanLineMargin, this.mScanLineTop, (this.mFramingRect.right - this.mHalfCornerSize) - this.mScanLineMargin, this.mScanLineTop + this.mScanLineSize, this.mPaint);
        }
    }

    private void drawTipText(Canvas canvas) {
        if (TextUtils.isEmpty(this.mTipText) || this.mTipTextSl == null) {
            return;
        }
        if (this.mIsTipTextBelowRect) {
            if (this.mIsShowTipBackground) {
                this.mPaint.setColor(this.mTipBackgroundColor);
                this.mPaint.setStyle(Paint.Style.FILL);
                if (this.mIsShowTipTextAsSingleLine) {
                    Rect rect = new Rect();
                    TextPaint textPaint = this.mTipPaint;
                    String str = this.mTipText;
                    textPaint.getTextBounds(str, 0, str.length(), rect);
                    float width = ((canvas.getWidth() - rect.width()) / 2) - this.mTipBackgroundRadius;
                    RectF rectF = new RectF(width, (this.mFramingRect.bottom + this.mTipTextMargin) - this.mTipBackgroundRadius, rect.width() + width + (this.mTipBackgroundRadius * 2), this.mFramingRect.bottom + this.mTipTextMargin + this.mTipTextSl.getHeight() + this.mTipBackgroundRadius);
                    int i = this.mTipBackgroundRadius;
                    canvas.drawRoundRect(rectF, i, i, this.mPaint);
                } else {
                    RectF rectF2 = new RectF(this.mFramingRect.left, (this.mFramingRect.bottom + this.mTipTextMargin) - this.mTipBackgroundRadius, this.mFramingRect.right, this.mFramingRect.bottom + this.mTipTextMargin + this.mTipTextSl.getHeight() + this.mTipBackgroundRadius);
                    int i2 = this.mTipBackgroundRadius;
                    canvas.drawRoundRect(rectF2, i2, i2, this.mPaint);
                }
            }
            canvas.save();
            if (this.mIsShowTipTextAsSingleLine) {
                canvas.translate(0.0f, this.mFramingRect.bottom + this.mTipTextMargin);
            } else {
                canvas.translate(this.mFramingRect.left + this.mTipBackgroundRadius, this.mFramingRect.bottom + this.mTipTextMargin);
            }
            this.mTipTextSl.draw(canvas);
            canvas.restore();
            return;
        }
        if (this.mIsShowTipBackground) {
            this.mPaint.setColor(this.mTipBackgroundColor);
            this.mPaint.setStyle(Paint.Style.FILL);
            if (this.mIsShowTipTextAsSingleLine) {
                Rect rect2 = new Rect();
                TextPaint textPaint2 = this.mTipPaint;
                String str2 = this.mTipText;
                textPaint2.getTextBounds(str2, 0, str2.length(), rect2);
                float width2 = ((canvas.getWidth() - rect2.width()) / 2) - this.mTipBackgroundRadius;
                RectF rectF3 = new RectF(width2, ((this.mFramingRect.top - this.mTipTextMargin) - this.mTipTextSl.getHeight()) - this.mTipBackgroundRadius, rect2.width() + width2 + (this.mTipBackgroundRadius * 2), (this.mFramingRect.top - this.mTipTextMargin) + this.mTipBackgroundRadius);
                int i3 = this.mTipBackgroundRadius;
                canvas.drawRoundRect(rectF3, i3, i3, this.mPaint);
            } else {
                RectF rectF4 = new RectF(this.mFramingRect.left, ((this.mFramingRect.top - this.mTipTextMargin) - this.mTipTextSl.getHeight()) - this.mTipBackgroundRadius, this.mFramingRect.right, (this.mFramingRect.top - this.mTipTextMargin) + this.mTipBackgroundRadius);
                int i4 = this.mTipBackgroundRadius;
                canvas.drawRoundRect(rectF4, i4, i4, this.mPaint);
            }
        }
        canvas.save();
        if (this.mIsShowTipTextAsSingleLine) {
            canvas.translate(0.0f, (this.mFramingRect.top - this.mTipTextMargin) - this.mTipTextSl.getHeight());
        } else {
            canvas.translate(this.mFramingRect.left + this.mTipBackgroundRadius, (this.mFramingRect.top - this.mTipTextMargin) - this.mTipTextSl.getHeight());
        }
        this.mTipTextSl.draw(canvas);
        canvas.restore();
    }

    private void moveScanLine() {
        if (this.mIsBarcode) {
            if (this.mGridScanLineBitmap == null) {
                this.mScanLineLeft += this.mMoveStepDistance;
                int width = this.mScanLineSize;
                Bitmap bitmap = this.mScanLineBitmap;
                if (bitmap != null) {
                    width = bitmap.getWidth();
                }
                if (this.mIsScanLineReverse) {
                    if (this.mScanLineLeft + width > this.mFramingRect.right - this.mHalfCornerSize || this.mScanLineLeft < this.mFramingRect.left + this.mHalfCornerSize) {
                        this.mMoveStepDistance = -this.mMoveStepDistance;
                    }
                } else if (this.mScanLineLeft + width > this.mFramingRect.right - this.mHalfCornerSize) {
                    this.mScanLineLeft = this.mFramingRect.left + this.mHalfCornerSize + 0.5f;
                }
            } else {
                float f = this.mGridScanLineRight + this.mMoveStepDistance;
                this.mGridScanLineRight = f;
                if (f > this.mFramingRect.right - this.mHalfCornerSize) {
                    this.mGridScanLineRight = this.mFramingRect.left + this.mHalfCornerSize + 0.5f;
                }
            }
        } else if (this.mGridScanLineBitmap == null) {
            this.mScanLineTop += this.mMoveStepDistance;
            int height = this.mScanLineSize;
            Bitmap bitmap2 = this.mScanLineBitmap;
            if (bitmap2 != null) {
                height = bitmap2.getHeight();
            }
            if (this.mIsScanLineReverse) {
                if (this.mScanLineTop + height > this.mFramingRect.bottom - this.mHalfCornerSize || this.mScanLineTop < this.mFramingRect.top + this.mHalfCornerSize) {
                    this.mMoveStepDistance = -this.mMoveStepDistance;
                }
            } else if (this.mScanLineTop + height > this.mFramingRect.bottom - this.mHalfCornerSize) {
                this.mScanLineTop = this.mFramingRect.top + this.mHalfCornerSize + 0.5f;
            }
        } else {
            float f2 = this.mGridScanLineBottom + this.mMoveStepDistance;
            this.mGridScanLineBottom = f2;
            if (f2 > this.mFramingRect.bottom - this.mHalfCornerSize) {
                this.mGridScanLineBottom = this.mFramingRect.top + this.mHalfCornerSize + 0.5f;
            }
        }
        postInvalidateDelayed(this.mAnimDelayTime, this.mFramingRect.left, this.mFramingRect.top, this.mFramingRect.right, this.mFramingRect.bottom);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        calFramingRect();
    }

    private void calFramingRect() {
        int width = (getWidth() - this.mRectWidth) / 2;
        int i = this.mTopOffset;
        Rect rect = new Rect(width, i, this.mRectWidth + width, this.mRectHeight + i);
        this.mFramingRect = rect;
        if (this.mIsBarcode) {
            float f = rect.left + this.mHalfCornerSize + 0.5f;
            this.mScanLineLeft = f;
            this.mGridScanLineRight = f;
        } else {
            float f2 = rect.top + this.mHalfCornerSize + 0.5f;
            this.mScanLineTop = f2;
            this.mGridScanLineBottom = f2;
        }
    }

    public Rect getScanBoxAreaRect(int i) {
        if (!this.mIsOnlyDecodeScanBoxArea) {
            return null;
        }
        Rect rect = new Rect(this.mFramingRect);
        float measuredHeight = (i * 1.0f) / getMeasuredHeight();
        rect.left = (int) (rect.left * measuredHeight);
        rect.right = (int) (rect.right * measuredHeight);
        rect.top = (int) (rect.top * measuredHeight);
        rect.bottom = (int) (rect.bottom * measuredHeight);
        return rect;
    }

    public void setIsBarcode(boolean z) {
        this.mIsBarcode = z;
        if (this.mCustomGridScanLineDrawable != null || this.mIsShowDefaultGridScanLineDrawable) {
            if (z) {
                this.mGridScanLineBitmap = this.mOriginBarCodeGridScanLineBitmap;
            } else {
                this.mGridScanLineBitmap = this.mOriginQRCodeGridScanLineBitmap;
            }
        } else if (this.mCustomScanLineDrawable != null || this.mIsShowDefaultScanLineDrawable) {
            if (z) {
                this.mScanLineBitmap = this.mOriginBarCodeScanLineBitmap;
            } else {
                this.mScanLineBitmap = this.mOriginQRCodeScanLineBitmap;
            }
        }
        if (z) {
            this.mTipText = this.mBarCodeTipText;
            this.mRectHeight = this.mBarcodeRectHeight;
            this.mAnimDelayTime = (int) (((this.mAnimTime * 1.0f) * this.mMoveStepDistance) / this.mRectWidth);
        } else {
            this.mTipText = this.mQRCodeTipText;
            int i = this.mRectWidth;
            this.mRectHeight = i;
            this.mAnimDelayTime = (int) (((this.mAnimTime * 1.0f) * this.mMoveStepDistance) / i);
        }
        if (!TextUtils.isEmpty(this.mTipText)) {
            if (this.mIsShowTipTextAsSingleLine) {
                this.mTipTextSl = new StaticLayout(this.mTipText, this.mTipPaint, BGAQRCodeUtil.getScreenResolution(getContext()).x, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
            } else {
                this.mTipTextSl = new StaticLayout(this.mTipText, this.mTipPaint, this.mRectWidth - (this.mTipBackgroundRadius * 2), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
            }
        }
        if (this.mIsCenterVertical) {
            int i2 = BGAQRCodeUtil.getScreenResolution(getContext()).y;
            int i3 = this.mToolbarHeight;
            if (i3 == 0) {
                this.mTopOffset = (i2 - this.mRectHeight) / 2;
            } else {
                this.mTopOffset = ((i2 - this.mRectHeight) / 2) + (i3 / 2);
            }
        }
        calFramingRect();
        postInvalidate();
    }

    public boolean getIsBarcode() {
        return this.mIsBarcode;
    }

    public int getMaskColor() {
        return this.mMaskColor;
    }

    public void setMaskColor(int i) {
        this.mMaskColor = i;
    }

    public int getCornerColor() {
        return this.mCornerColor;
    }

    public void setCornerColor(int i) {
        this.mCornerColor = i;
    }

    public int getCornerLength() {
        return this.mCornerLength;
    }

    public void setCornerLength(int i) {
        this.mCornerLength = i;
    }

    public int getCornerSize() {
        return this.mCornerSize;
    }

    public void setCornerSize(int i) {
        this.mCornerSize = i;
    }

    public int getRectWidth() {
        return this.mRectWidth;
    }

    public void setRectWidth(int i) {
        this.mRectWidth = i;
    }

    public int getRectHeight() {
        return this.mRectHeight;
    }

    public void setRectHeight(int i) {
        this.mRectHeight = i;
    }

    public int getBarcodeRectHeight() {
        return this.mBarcodeRectHeight;
    }

    public void setBarcodeRectHeight(int i) {
        this.mBarcodeRectHeight = i;
    }

    public int getTopOffset() {
        return this.mTopOffset;
    }

    public void setTopOffset(int i) {
        this.mTopOffset = i;
    }

    public int getScanLineSize() {
        return this.mScanLineSize;
    }

    public void setScanLineSize(int i) {
        this.mScanLineSize = i;
    }

    public int getScanLineColor() {
        return this.mScanLineColor;
    }

    public void setScanLineColor(int i) {
        this.mScanLineColor = i;
    }

    public int getScanLineMargin() {
        return this.mScanLineMargin;
    }

    public void setScanLineMargin(int i) {
        this.mScanLineMargin = i;
    }

    public boolean isShowDefaultScanLineDrawable() {
        return this.mIsShowDefaultScanLineDrawable;
    }

    public void setShowDefaultScanLineDrawable(boolean z) {
        this.mIsShowDefaultScanLineDrawable = z;
    }

    public Drawable getCustomScanLineDrawable() {
        return this.mCustomScanLineDrawable;
    }

    public void setCustomScanLineDrawable(Drawable drawable) {
        this.mCustomScanLineDrawable = drawable;
    }

    public Bitmap getScanLineBitmap() {
        return this.mScanLineBitmap;
    }

    public void setScanLineBitmap(Bitmap bitmap) {
        this.mScanLineBitmap = bitmap;
    }

    public int getBorderSize() {
        return this.mBorderSize;
    }

    public void setBorderSize(int i) {
        this.mBorderSize = i;
    }

    public int getBorderColor() {
        return this.mBorderColor;
    }

    public void setBorderColor(int i) {
        this.mBorderColor = i;
    }

    public int getAnimTime() {
        return this.mAnimTime;
    }

    public void setAnimTime(int i) {
        this.mAnimTime = i;
    }

    public boolean isCenterVertical() {
        return this.mIsCenterVertical;
    }

    public void setCenterVertical(boolean z) {
        this.mIsCenterVertical = z;
    }

    public int getToolbarHeight() {
        return this.mToolbarHeight;
    }

    public void setToolbarHeight(int i) {
        this.mToolbarHeight = i;
    }

    public String getQRCodeTipText() {
        return this.mQRCodeTipText;
    }

    public void setQRCodeTipText(String str) {
        this.mQRCodeTipText = str;
    }

    public String getBarCodeTipText() {
        return this.mBarCodeTipText;
    }

    public void setBarCodeTipText(String str) {
        this.mBarCodeTipText = str;
    }

    public String getTipText() {
        return this.mTipText;
    }

    public void setTipText(String str) {
        this.mTipText = str;
    }

    public int getTipTextColor() {
        return this.mTipTextColor;
    }

    public void setTipTextColor(int i) {
        this.mTipTextColor = i;
    }

    public int getTipTextSize() {
        return this.mTipTextSize;
    }

    public void setTipTextSize(int i) {
        this.mTipTextSize = i;
    }

    public boolean isTipTextBelowRect() {
        return this.mIsTipTextBelowRect;
    }

    public void setTipTextBelowRect(boolean z) {
        this.mIsTipTextBelowRect = z;
    }

    public int getTipTextMargin() {
        return this.mTipTextMargin;
    }

    public void setTipTextMargin(int i) {
        this.mTipTextMargin = i;
    }

    public boolean isShowTipTextAsSingleLine() {
        return this.mIsShowTipTextAsSingleLine;
    }

    public void setShowTipTextAsSingleLine(boolean z) {
        this.mIsShowTipTextAsSingleLine = z;
    }

    public boolean isShowTipBackground() {
        return this.mIsShowTipBackground;
    }

    public void setShowTipBackground(boolean z) {
        this.mIsShowTipBackground = z;
    }

    public int getTipBackgroundColor() {
        return this.mTipBackgroundColor;
    }

    public void setTipBackgroundColor(int i) {
        this.mTipBackgroundColor = i;
    }

    public boolean isScanLineReverse() {
        return this.mIsScanLineReverse;
    }

    public void setScanLineReverse(boolean z) {
        this.mIsScanLineReverse = z;
    }

    public boolean isShowDefaultGridScanLineDrawable() {
        return this.mIsShowDefaultGridScanLineDrawable;
    }

    public void setShowDefaultGridScanLineDrawable(boolean z) {
        this.mIsShowDefaultGridScanLineDrawable = z;
    }

    public float getHalfCornerSize() {
        return this.mHalfCornerSize;
    }

    public void setHalfCornerSize(float f) {
        this.mHalfCornerSize = f;
    }

    public StaticLayout getTipTextSl() {
        return this.mTipTextSl;
    }

    public void setTipTextSl(StaticLayout staticLayout) {
        this.mTipTextSl = staticLayout;
    }

    public int getTipBackgroundRadius() {
        return this.mTipBackgroundRadius;
    }

    public void setTipBackgroundRadius(int i) {
        this.mTipBackgroundRadius = i;
    }

    public boolean isOnlyDecodeScanBoxArea() {
        return this.mIsOnlyDecodeScanBoxArea;
    }

    public void setOnlyDecodeScanBoxArea(boolean z) {
        this.mIsOnlyDecodeScanBoxArea = z;
    }
}
