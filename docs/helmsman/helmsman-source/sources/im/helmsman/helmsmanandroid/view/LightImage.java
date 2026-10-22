package im.helmsman.helmsmanandroid.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;
import androidx.core.internal.view.SupportMenu;
import im.helmsman.helmsmanandroid.utils.ViewUtils;

/* JADX INFO: loaded from: classes2.dex */
public class LightImage extends ImageView {
    private ThisHandler handler;
    private boolean isShine;
    private Context mContext;
    private Paint p;
    private MyRunnable runnable;

    public LightImage(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.isShine = true;
        this.handler = new ThisHandler();
        this.p = new Paint();
        this.runnable = new MyRunnable();
        this.mContext = context;
        startShine();
    }

    public LightImage(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.isShine = true;
        this.handler = new ThisHandler();
        this.p = new Paint();
        this.runnable = new MyRunnable();
        this.mContext = context;
        startShine();
    }

    public LightImage(Context context) {
        super(context);
        this.isShine = true;
        this.handler = new ThisHandler();
        this.p = new Paint();
        this.runnable = new MyRunnable();
        this.mContext = context;
        startShine();
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.isShine) {
            this.p.setColor(SupportMenu.CATEGORY_MASK);
            this.p.setAlpha(25);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, (getWidth() / 3) + ViewUtils.dpToPx(5), this.p);
        }
    }

    public void setIsShine(boolean z) {
        this.isShine = z;
    }

    public void startShine() {
        this.handler.postDelayed(this.runnable, 1000L);
    }

    class ThisHandler extends Handler {
        ThisHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
        }
    }

    class MyRunnable implements Runnable {
        MyRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            LightImage lightImage = LightImage.this;
            lightImage.isShine = !lightImage.isShine;
            LightImage.this.invalidate();
            LightImage.this.handler.postDelayed(LightImage.this.runnable, 800L);
        }
    }
}
