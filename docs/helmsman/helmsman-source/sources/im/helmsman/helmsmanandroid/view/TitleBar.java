package im.helmsman.helmsmanandroid.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import com.MAVLink.enums.MAV_COMPONENT;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.utils.AutoLayoutExtendsUtils;
import im.helmsman.helmsmanandroid.utils.ViewUtils;

/* JADX INFO: loaded from: classes2.dex */
public class TitleBar extends RelativeLayout implements View.OnClickListener {
    private int backHeight;
    private int backtWidth;
    private float centerIconHeight;
    private float centerIconWidth;
    private View centerView;
    private Context context;
    private int leftDrawable;
    private View leftView;
    private ProgressBar pb;
    private int rightDrawable;
    private float rightIconHeight;
    private float rightIconWidth;
    private boolean rightIsRemove;
    private String rightText;
    private View rightView;
    private int textColor;
    private String titleText;
    private float titleTextSize;

    private int measureHeight(int i, int i2) {
        return (i == Integer.MIN_VALUE || i == 0 || i == 1073741824) ? i2 : MAV_COMPONENT.MAV_COMP_ID_SERVO11;
    }

    private int measureWidth(int i, int i2) {
        return i != 1073741824 ? MAV_COMPONENT.MAV_COMP_ID_SERVO11 : i2;
    }

    public TitleBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.titleTextSize = 18.0f;
        this.backtWidth = ViewUtils.dpToPx(34);
        this.backHeight = ViewUtils.dpToPx(34);
        this.rightIsRemove = false;
        this.context = context;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.TitleBar);
        this.titleText = typedArrayObtainStyledAttributes.getString(8);
        this.leftDrawable = typedArrayObtainStyledAttributes.getResourceId(2, -100);
        this.rightDrawable = typedArrayObtainStyledAttributes.getResourceId(3, -100);
        this.rightIconWidth = typedArrayObtainStyledAttributes.getDimension(5, 0.0f);
        this.rightIconHeight = typedArrayObtainStyledAttributes.getDimension(4, 0.0f);
        this.rightText = typedArrayObtainStyledAttributes.getString(6);
        this.centerIconHeight = typedArrayObtainStyledAttributes.getDimension(0, 0.0f);
        this.centerIconWidth = typedArrayObtainStyledAttributes.getDimension(1, 0.0f);
        this.textColor = typedArrayObtainStyledAttributes.getColor(7, ViewCompat.MEASURED_STATE_MASK);
        this.centerIconHeight = AutoLayoutExtendsUtils.heightConvert((int) this.centerIconHeight);
        this.centerIconWidth = AutoLayoutExtendsUtils.widthConvert((int) this.centerIconWidth);
        this.rightIconWidth = AutoLayoutExtendsUtils.widthConvert((int) this.rightIconWidth);
        this.rightIconHeight = AutoLayoutExtendsUtils.heightConvert((int) this.rightIconHeight);
        typedArrayObtainStyledAttributes.recycle();
        initView();
        setFitsSystemWindows(true);
    }

    public TitleBar(Context context) {
        super(context);
        this.titleTextSize = 18.0f;
        this.backtWidth = ViewUtils.dpToPx(34);
        this.backHeight = ViewUtils.dpToPx(34);
        this.rightIsRemove = false;
    }

    public TitleBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.titleTextSize = 18.0f;
        this.backtWidth = ViewUtils.dpToPx(34);
        this.backHeight = ViewUtils.dpToPx(34);
        this.rightIsRemove = false;
    }

    private void initView() {
        RelativeLayout.LayoutParams layoutParams;
        if (this.titleText != null) {
            TextView textView = new TextView(this.context);
            this.centerView = textView;
            textView.setId(View.generateViewId());
            TextView textView2 = (TextView) this.centerView;
            textView2.setText(this.titleText);
            textView2.setTextSize(this.titleTextSize);
            textView2.setTextColor(this.textColor);
            float f = this.centerIconHeight;
            if (f == 0.0f && this.centerIconWidth == 0.0f) {
                layoutParams = new RelativeLayout.LayoutParams(-2, -2);
            } else if (f == 0.0f && this.centerIconWidth != 0.0f) {
                layoutParams = new RelativeLayout.LayoutParams((int) this.centerIconWidth, -2);
            } else if (f != 0.0f && this.centerIconWidth == 0.0f) {
                layoutParams = new RelativeLayout.LayoutParams(-2, (int) this.centerIconHeight);
            } else {
                layoutParams = new RelativeLayout.LayoutParams(-2, -2);
            }
            layoutParams.addRule(13);
            addView(textView2, layoutParams);
        }
        if (this.leftDrawable != -100) {
            ImageButton imageButton = new ImageButton(this.context);
            imageButton.setBackgroundColor(0);
            imageButton.setImageResource(this.leftDrawable);
            imageButton.setScaleType(ImageView.ScaleType.FIT_XY);
            int iDpToPx = ViewUtils.dpToPx(5);
            imageButton.setPadding(iDpToPx, iDpToPx, iDpToPx, iDpToPx);
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(this.backtWidth, this.backHeight);
            layoutParams2.addRule(15);
            layoutParams2.setMargins(ViewUtils.dpToPx(16), 0, 0, 0);
            addView(imageButton, layoutParams2);
            imageButton.setOnClickListener(this);
            this.leftView = imageButton;
        }
        if (this.rightDrawable != -100) {
            ImageButton imageButton2 = new ImageButton(this.context);
            this.rightView = imageButton2;
            imageButton2.setId(View.generateViewId());
            this.rightView.setBackground(getResources().getDrawable(R.drawable.ripple_tran_bg));
            ((ImageButton) this.rightView).setImageResource(this.rightDrawable);
            RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams((int) this.rightIconWidth, (int) this.rightIconHeight);
            layoutParams3.setMargins(0, 0, ViewUtils.dpToPx(8), 0);
            layoutParams3.addRule(15);
            layoutParams3.addRule(11);
            addView(this.rightView, layoutParams3);
            return;
        }
        if (this.rightText != null) {
            TextView textView3 = new TextView(this.context);
            this.rightView = textView3;
            textView3.setId(View.generateViewId());
            TextView textView4 = (TextView) this.rightView;
            textView4.setTextColor(SupportMenu.CATEGORY_MASK);
            textView4.setText(this.rightText);
            textView4.setTextSize(this.titleTextSize - 1.0f);
            RelativeLayout.LayoutParams layoutParams4 = new RelativeLayout.LayoutParams(-2, -2);
            layoutParams4.setMargins(0, 62, 62, 0);
            layoutParams4.addRule(11);
            addView(this.rightView, layoutParams4);
        }
    }

    @Override // android.widget.RelativeLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int mode = View.MeasureSpec.getMode(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        setMeasuredDimension(measureWidth(mode, View.MeasureSpec.getSize(i)), measureHeight(mode2, View.MeasureSpec.getSize(i2)));
        super.onMeasure(i, i2);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        Activity topActivity = Base2Activity.getTopActivity();
        if (topActivity != null) {
            try {
                topActivity.onBackPressed();
            } catch (IllegalStateException unused) {
                if (topActivity.isDestroyed()) {
                    return;
                }
                topActivity.finish();
            }
        }
    }

    public View removeRightButton() {
        removeView(this.rightView);
        Log.e("remove", "removeView");
        this.rightIsRemove = true;
        return this.rightView;
    }

    public int getRightViewId() {
        return this.rightView.getId();
    }

    public int getCenterViewId() {
        View view = this.centerView;
        if (view == null) {
            return 0;
        }
        return view.getId();
    }

    public void showProgressBar() {
        removeRightButton();
        this.pb = new ProgressBar(this.context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) this.rightIconWidth, (int) this.rightIconHeight);
        layoutParams.addRule(11);
        layoutParams.setMargins(0, 62, 62, 0);
        addView(this.pb, layoutParams);
    }

    public void hideProgressBar() {
        removeView(this.pb);
        recoverRightview();
        this.pb = null;
    }

    public void hideCenterView() {
        View view = this.centerView;
        if (view != null) {
            view.setVisibility(8);
        }
    }

    public void showCenterView() {
        View view = this.centerView;
        if (view != null) {
            view.setVisibility(0);
        }
    }

    public void recoverRightview() {
        RelativeLayout.LayoutParams layoutParams;
        if (this.rightIsRemove) {
            if (this.rightView instanceof TextView) {
                layoutParams = new RelativeLayout.LayoutParams(-2, -2);
            } else {
                layoutParams = new RelativeLayout.LayoutParams((int) this.rightIconWidth, (int) this.rightIconHeight);
            }
            layoutParams.setMargins(0, 62, 62, 0);
            layoutParams.addRule(11);
            addView(this.rightView, layoutParams);
        }
    }

    public void setRightButtonOnclickListener(View.OnClickListener onClickListener) {
        View view = this.rightView;
        if (view != null) {
            view.setOnClickListener(onClickListener);
        }
    }

    public void setCenterViewOnClickListener(View.OnClickListener onClickListener) {
        View view = this.centerView;
        if (view != null) {
            view.setOnClickListener(onClickListener);
        }
    }

    public void setRightViewText(String str) {
        View view = this.rightView;
        if (view == null || !(view instanceof TextView)) {
            return;
        }
        ((TextView) view).setText(str);
    }

    public void setRightViewText(String str, int i, int i2, int i3, int i4) {
        View view = this.rightView;
        if (view == null || !(view instanceof TextView)) {
            return;
        }
        TextView textView = (TextView) view;
        textView.setText(str);
        textView.setTextColor(i);
        textView.setShadowLayer(2.0f, i3, i4, i2);
    }

    public void setCenterViewText(String str) {
        View view = this.centerView;
        if (view == null || !(view instanceof TextView)) {
            return;
        }
        ((TextView) view).setText(str);
    }

    public void setCenterViewText(int i) {
        setCenterViewText(this.context.getString(i));
    }

    public void setRightDrawable(int i) {
        View view = this.rightView;
        if (view == null || !(view instanceof ImageView)) {
            return;
        }
        ((ImageView) view).setImageResource(i);
    }

    public void setRightViewEnable(boolean z) {
        View view = this.rightView;
        if (view != null) {
            view.setEnabled(z);
        }
    }

    public void setLeftViewOnClickListener(View.OnClickListener onClickListener) {
        this.leftView.setOnClickListener(onClickListener);
    }

    public void replaceCenterView(View view) {
        replaceCenterView(view, 0, 0);
    }

    public void replaceCenterView(View view, int i, int i2) {
        removeView(this.centerView);
        this.centerView = view;
        view.setId(View.generateViewId());
        if (i == 0) {
            i = -2;
        }
        if (i2 == 0) {
            i2 = -2;
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(i, i2);
        layoutParams.addRule(13);
        layoutParams.setMargins(15, 15, 15, 15);
        addView(this.centerView, layoutParams);
    }

    public void hideRightView() {
        View view = this.rightView;
        if (view != null) {
            view.setVisibility(8);
        }
    }

    public void showRightView() {
        View view = this.rightView;
        if (view != null) {
            view.setVisibility(0);
        }
    }
}
