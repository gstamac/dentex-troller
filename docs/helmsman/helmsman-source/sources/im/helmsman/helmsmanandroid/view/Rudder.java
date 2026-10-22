package im.helmsman.helmsmanandroid.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.utils.MathUtils;
import im.helmsman.helmsmanandroid.utils.VibratorUtil;
import im.helmsman.helmsmanandroid.utils.ViewUtils;

/* JADX INFO: loaded from: classes2.dex */
public class Rudder extends View implements SensorEventListener {
    public static final int ACTION_RUDDER = 1;
    public static final int ACTION_STOP = 2;
    private final int ADDSPEED;
    private final int MINUSSPEED;
    private final int NOCHANGE;
    private Bitmap bitmapJoystick;
    private float degree;
    private Handler handler;
    private RudderListener listener;
    private long longClickTime;
    private Context mContext;
    private Point mCtrlPoint;
    private Paint mPaint;
    private Point mRockerPosition;
    private SensorManager mSensorManager;
    private int mSpeedChangeFlag;
    private int mWheelRadius;
    private int radius;
    private int x;
    private int y;

    public interface RudderListener {
        void onClickStop();

        void onLongPressDown();

        void onLongPressUp();

        void onSteeringWheelBottom();

        void onSteeringWheelLeft();

        void onSteeringWheelRight();

        void onSteeringWheelUp();
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public Rudder(Context context) {
        super(context);
        this.degree = 0.0f;
        this.ADDSPEED = 8;
        this.NOCHANGE = 0;
        this.MINUSSPEED = 9;
        this.longClickTime = 0L;
        this.mContext = context;
    }

    public Rudder(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.degree = 0.0f;
        this.ADDSPEED = 8;
        this.NOCHANGE = 0;
        this.MINUSSPEED = 9;
        this.longClickTime = 0L;
        SensorManager sensorManager = (SensorManager) context.getSystemService("sensor");
        this.mSensorManager = sensorManager;
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(3), 2);
        this.mContext = context;
        int screenWidth = ViewUtils.getScreenWidth((Activity) context) / 2;
        this.mWheelRadius = screenWidth;
        this.radius = screenWidth / 6;
        this.x = 0;
        this.y = 0;
        int i = this.x;
        int i2 = this.mWheelRadius;
        this.mCtrlPoint = new Point(i + (i2 / 2), this.y + (i2 / 2));
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setAntiAlias(true);
        this.mRockerPosition = new Point(this.mCtrlPoint);
        setFocusable(true);
        setFocusableInTouchMode(true);
        Bitmap bitmapDecodeResource = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.bg_joystick);
        this.bitmapJoystick = bitmapDecodeResource;
        int i3 = this.mWheelRadius;
        this.bitmapJoystick = Bitmap.createScaledBitmap(bitmapDecodeResource, i3, i3, false);
        this.handler = new Handler();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Matrix matrix = new Matrix();
        matrix.setTranslate(this.x, this.y);
        matrix.postRotate(this.degree, this.mCtrlPoint.x, this.mCtrlPoint.y);
        this.mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mPaint.setAlpha(200);
        canvas.drawCircle(this.mCtrlPoint.x, this.mCtrlPoint.y, this.mWheelRadius / 2, this.mPaint);
        canvas.drawBitmap(this.bitmapJoystick, matrix, this.mPaint);
        this.mPaint.setColor(SupportMenu.CATEGORY_MASK);
        this.mPaint.setAlpha(200);
        canvas.drawCircle(this.mRockerPosition.x, this.mRockerPosition.y, this.radius, this.mPaint);
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int i3 = this.mWheelRadius;
        setMeasuredDimension(i3, i3);
    }

    public void setRudderListener(RudderListener rudderListener) {
        this.listener = rudderListener;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int length = MathUtils.getLength(this.mCtrlPoint.x, this.mCtrlPoint.y, motionEvent.getX(), motionEvent.getY());
        if (motionEvent.getAction() == 0) {
            if (length > this.mWheelRadius) {
                return true;
            }
            VibratorUtil.vibrator(this.mContext, new long[]{0, 110});
            this.mRockerPosition.set((int) motionEvent.getX(), (int) motionEvent.getY());
            if (length < this.mWheelRadius / 5) {
                return true;
            }
        } else if (motionEvent.getAction() == 2) {
            int i = this.mWheelRadius;
            if (length < i / 5) {
                this.mRockerPosition.set((int) motionEvent.getX(), (int) motionEvent.getY());
                return true;
            }
            if (length <= (i / 2) - this.radius) {
                this.mRockerPosition.set((int) motionEvent.getX(), (int) motionEvent.getY());
            } else {
                this.mRockerPosition = MathUtils.getBorderPoint(this.mCtrlPoint, new Point((int) motionEvent.getX(), (int) motionEvent.getY()), (this.mWheelRadius / 2) - this.radius);
            }
        }
        callBack(1, motionEvent);
        if (motionEvent.getAction() == 1) {
            this.mRockerPosition = new Point(this.mCtrlPoint);
            callBack(2, motionEvent);
        }
        invalidate();
        return true;
    }

    private int getAngleCouvert(float f) {
        int iRound = (int) Math.round((((double) f) / 3.141592653589793d) * 180.0d);
        return iRound < 0 ? -iRound : (180 - iRound) + 180;
    }

    private void callBack(int i, MotionEvent motionEvent) {
        if (this.listener != null) {
            decodeCommand(i, getAngleCouvert(MathUtils.getRadian(this.mCtrlPoint, new Point((int) motionEvent.getX(), (int) motionEvent.getY()))));
        }
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == 3) {
            float f = sensorEvent.values[0];
            if (f > 0.0f && f < 180.0f) {
                this.degree = -f;
            } else {
                this.degree = 360.0f - f;
            }
            invalidate();
        }
    }

    protected void decodeCommand(int i, int i2) {
        if (i == 2) {
            int i3 = this.mSpeedChangeFlag;
            if (i3 != 0) {
                if (i3 == 8) {
                    this.listener.onSteeringWheelUp();
                } else if (i3 == 9) {
                    this.listener.onSteeringWheelBottom();
                }
            }
            this.longClickTime = 0L;
            this.listener.onClickStop();
            return;
        }
        if (i2 > 50 && i2 < 130) {
            this.mSpeedChangeFlag = 8;
            this.listener.onLongPressUp();
            return;
        }
        if (i2 > 230 && i2 < 310) {
            this.mSpeedChangeFlag = 9;
            this.listener.onLongPressDown();
            return;
        }
        if (i2 < 50 || i2 > 310) {
            this.longClickTime = 0L;
            this.mSpeedChangeFlag = 0;
            this.listener.onSteeringWheelRight();
        } else {
            if (i2 <= 130 || i2 >= 230) {
                return;
            }
            this.mSpeedChangeFlag = 0;
            this.longClickTime = 0L;
            this.listener.onSteeringWheelLeft();
        }
    }
}
