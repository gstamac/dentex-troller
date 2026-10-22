package im.helmsman.helmsmanandroid.view;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;

/* JADX INFO: loaded from: classes2.dex */
public class CheckGpsAndWifiPopuWindow {
    private Handler h = new Handler();
    private Activity mContext;
    private OnCheckGpsAndWifiListener mListener;
    private View mPopupView;
    private PopupWindow mPopupWindow;
    private RelativeLayout mRelPopuBg;
    private TextView mTvPopuConnection;
    private TextView mTvPopuContent;

    public interface OnCheckGpsAndWifiListener {
        void GpsAndWifiSuccess();
    }

    public CheckGpsAndWifiPopuWindow(Activity activity) {
        this.mContext = activity;
        if (this.mPopupView == null) {
            this.mPopupView = LayoutInflater.from(this.mContext).inflate(R.layout.item_popuwindow, (ViewGroup) null);
            this.mPopupWindow = new PopupWindow(this.mPopupView, -1, -2, false);
            this.mRelPopuBg = (RelativeLayout) this.mPopupView.findViewById(R.id.rel_pupu);
            this.mTvPopuConnection = (TextView) this.mPopupView.findViewById(R.id.tv_popu_connection);
            this.mTvPopuContent = (TextView) this.mPopupView.findViewById(R.id.tv_popu_conection_content);
            this.mPopupWindow.setAnimationStyle(R.style.AnimationPreview);
        }
        this.mPopupView.setOnClickListener(new View.OnClickListener() { // from class: im.helmsman.helmsmanandroid.view.CheckGpsAndWifiPopuWindow.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (CheckGpsAndWifiPopuWindow.this.mPopupWindow == null || !CheckGpsAndWifiPopuWindow.this.mPopupWindow.isShowing() || CheckGpsAndWifiPopuWindow.this.mContext.isDestroyed()) {
                    return;
                }
                CheckGpsAndWifiPopuWindow.this.mPopupWindow.dismiss();
            }
        });
    }

    public CheckGpsAndWifiPopuWindow checkGpsAndWifi(View view) {
        this.h.removeCallbacksAndMessages(null);
        this.h.postDelayed(new Runnable() { // from class: im.helmsman.helmsmanandroid.view.CheckGpsAndWifiPopuWindow.2
            @Override // java.lang.Runnable
            public void run() {
                if (CheckGpsAndWifiPopuWindow.this.mPopupWindow == null || !CheckGpsAndWifiPopuWindow.this.mPopupWindow.isShowing() || CheckGpsAndWifiPopuWindow.this.mContext.isDestroyed()) {
                    return;
                }
                CheckGpsAndWifiPopuWindow.this.mPopupWindow.dismiss();
            }
        }, 5000L);
        if (!MyApplication.getInstance().wifiState) {
            this.mRelPopuBg.setBackgroundColor(Color.parseColor("#e0575b"));
            this.mTvPopuConnection.setText(R.string.main_no_boat_connected);
            this.mTvPopuContent.setText(R.string.main_no_boat_connected_message);
        } else if (MyApplication.getInstance().gpsState != 2) {
            this.mRelPopuBg.setBackgroundColor(Color.parseColor("#e0575b"));
            this.mTvPopuConnection.setText(R.string.main_no_gps_data_received);
            this.mTvPopuContent.setText(R.string.main_no_gps_data_received_message);
        } else {
            OnCheckGpsAndWifiListener onCheckGpsAndWifiListener = this.mListener;
            if (onCheckGpsAndWifiListener != null) {
                onCheckGpsAndWifiListener.GpsAndWifiSuccess();
            }
            return this;
        }
        this.mPopupWindow.showAsDropDown(view);
        return this;
    }

    public PopupWindow checkWifi(View view) {
        this.h.removeCallbacksAndMessages(null);
        this.h.postDelayed(new Runnable() { // from class: im.helmsman.helmsmanandroid.view.CheckGpsAndWifiPopuWindow.3
            @Override // java.lang.Runnable
            public void run() {
                if (CheckGpsAndWifiPopuWindow.this.mPopupWindow == null || !CheckGpsAndWifiPopuWindow.this.mPopupWindow.isShowing() || CheckGpsAndWifiPopuWindow.this.mContext.isDestroyed()) {
                    return;
                }
                CheckGpsAndWifiPopuWindow.this.mPopupWindow.dismiss();
            }
        }, 5000L);
        if (!MyApplication.getInstance().wifiState) {
            this.mTvPopuConnection.setText(R.string.main_no_boat_connected);
            this.mTvPopuContent.setText(R.string.main_no_boat_connected_message);
            this.mRelPopuBg.setBackgroundColor(Color.parseColor("#e0575b"));
            this.mPopupWindow.showAsDropDown(view);
        } else {
            OnCheckGpsAndWifiListener onCheckGpsAndWifiListener = this.mListener;
            if (onCheckGpsAndWifiListener != null) {
                onCheckGpsAndWifiListener.GpsAndWifiSuccess();
            }
        }
        return this.mPopupWindow;
    }

    public CheckGpsAndWifiPopuWindow setOnCheckGpsAndWifi(OnCheckGpsAndWifiListener onCheckGpsAndWifiListener) {
        this.mListener = onCheckGpsAndWifiListener;
        return this;
    }

    public void showPopupWindow(View view, String str, String str2) {
        this.mRelPopuBg.setBackgroundColor(Color.parseColor("#e0575b"));
        this.mTvPopuConnection.setText(str);
        this.mTvPopuContent.setText(str2);
        this.mPopupWindow.showAsDropDown(view);
        this.h.removeCallbacksAndMessages(null);
        this.h.postDelayed(new Runnable() { // from class: im.helmsman.helmsmanandroid.view.CheckGpsAndWifiPopuWindow.4
            @Override // java.lang.Runnable
            public void run() {
                if (CheckGpsAndWifiPopuWindow.this.mPopupWindow == null || !CheckGpsAndWifiPopuWindow.this.mPopupWindow.isShowing() || CheckGpsAndWifiPopuWindow.this.mContext.isDestroyed()) {
                    return;
                }
                CheckGpsAndWifiPopuWindow.this.mPopupWindow.dismiss();
            }
        }, 5000L);
    }

    public void showPopupWindow(View view, int i, int i2) {
        showPopupWindow(view, this.mContext.getString(i), this.mContext.getString(i2));
    }
}
