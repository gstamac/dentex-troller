package im.helmsman.helmsmanandroid.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.codeless.internal.Constants;
import com.google.android.material.snackbar.Snackbar;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;

/* JADX INFO: loaded from: classes2.dex */
public class ViewUtils {
    private static AlertDialog alertDialog;
    private static Handler handler;
    private static ProgressDialog mPd;
    private static DisplayMetrics metric;

    static {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
    }

    public static void ShowToast(final String str) {
        handler.post(new Runnable() { // from class: im.helmsman.helmsmanandroid.utils.ViewUtils.1
            @Override // java.lang.Runnable
            public void run() {
                Activity topActivity = Base2Activity.getTopActivity();
                if (topActivity.isDestroyed()) {
                    return;
                }
                Toast.makeText(topActivity, str, 0).show();
            }
        });
    }

    public static void ShowToast(final int i) {
        handler.post(new Runnable() { // from class: im.helmsman.helmsmanandroid.utils.ViewUtils.2
            @Override // java.lang.Runnable
            public void run() {
                Toast.makeText(Base2Activity.getTopActivity(), i, 0).show();
            }
        });
    }

    public static ProgressDialog showProgressDialog(CharSequence charSequence) {
        return showProgressDialog(null, charSequence);
    }

    public static ProgressDialog showProgressDialog(CharSequence charSequence, CharSequence charSequence2) {
        try {
            ProgressDialog progressDialog = mPd;
            if (progressDialog != null && progressDialog.isShowing()) {
                mPd.dismiss();
                mPd = null;
            }
            ProgressDialog progressDialog2 = new ProgressDialog(Base2Activity.getTopActivity());
            mPd = progressDialog2;
            progressDialog2.setCancelable(true);
            mPd.setMessage(charSequence2);
            mPd.setTitle(charSequence);
            mPd.show();
        } catch (IllegalArgumentException unused) {
        }
        return mPd;
    }

    public static ProgressDialog showProgressDialog(int i) {
        return showProgressDialog(null, MyApplication.getInstance().getString(i));
    }

    public static void setProgressDialogText(String str) {
        ProgressDialog progressDialog = mPd;
        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }
        mPd.setMessage(str);
    }

    public static void cancelProgressDialog() {
        ProgressDialog progressDialog = mPd;
        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }
        mPd.dismiss();
        mPd = null;
    }

    public static int getScreenWidth(Activity activity) {
        if (metric == null) {
            metric = new DisplayMetrics();
        }
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    public static int getScreenHeight() {
        Activity topActivity = Base2Activity.getTopActivity();
        if (metric == null) {
            metric = new DisplayMetrics();
        }
        topActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }

    public static int dpToPx(int i) {
        return (int) ((i * MyApplication.getInstance().getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int getecorViewHeight(Context context) {
        try {
            Class<?> cls = Class.forName("com.android.internal.R$dimen");
            return context.getResources().getDimensionPixelSize(Integer.parseInt(cls.getField("status_bar_height").get(cls.newInstance()).toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static boolean checkDeviceHasNavigationBar(Context context) {
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier("config_showNavigationBar", "bool", Constants.PLATFORM);
        boolean z = false;
        boolean z2 = identifier > 0 ? resources.getBoolean(identifier) : false;
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            String str = (String) cls.getMethod("get", String.class).invoke(cls, "qemu.hw.mainkeys");
            if (!"1".equals(str)) {
                z = AppEventsConstants.EVENT_PARAM_VALUE_NO.equals(str) ? true : z2;
            }
            return z;
        } catch (Exception e) {
            Log.w(NotificationCompat.CATEGORY_ERROR, e);
            return z2;
        }
    }

    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier("navigation_bar_height", "dimen", Constants.PLATFORM);
        if (identifier <= 0 || !checkDeviceHasNavigationBar(context)) {
            return 0;
        }
        return resources.getDimensionPixelSize(identifier);
    }

    public static Bitmap getViewBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
        view.layout(0, 0, (int) (((double) view.getMeasuredWidth()) / 1.5d), (int) (((double) view.getMeasuredHeight()) / 1.5d));
        view.buildDrawingCache();
        Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(drawingCache);
        drawingCache.recycle();
        view.destroyDrawingCache();
        return bitmapCreateBitmap;
    }

    public static void showSnackBar(View view, String str, boolean z) {
        Snackbar snackbarMake;
        if (view == null) {
            return;
        }
        if (z) {
            snackbarMake = Snackbar.make(view, str, 0);
        } else {
            snackbarMake = Snackbar.make(view, str, -2);
        }
        snackbarMake.show();
    }

    public static void showSnackBar(View view, int i, boolean z) {
        showSnackBar(view, MyApplication.getInstance().getString(i), z);
    }

    public static void showSnackBar(View view, String str, String str2, boolean z, View.OnClickListener onClickListener) {
        final Snackbar snackbarMake;
        if (view == null) {
            return;
        }
        if (z) {
            snackbarMake = Snackbar.make(view, str, 0);
        } else {
            snackbarMake = Snackbar.make(view, str, -2);
        }
        if (onClickListener == null) {
            snackbarMake.setAction(str2, new View.OnClickListener() { // from class: im.helmsman.helmsmanandroid.utils.ViewUtils.3
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    snackbarMake.dismiss();
                }
            });
        } else {
            snackbarMake.setAction(str2, onClickListener);
        }
        snackbarMake.show();
    }

    public static void showSnackBar(View view, int i, int i2, boolean z, View.OnClickListener onClickListener) {
        showSnackBar(view, MyApplication.getInstance().getString(i), MyApplication.getInstance().getString(i2), z, onClickListener);
    }

    public static PopupWindow showPopupWindows(View view, String str, String str2, String str3, View.OnClickListener onClickListener) {
        View viewInflate = LayoutInflater.from(Base2Activity.getTopActivity()).inflate(R.layout.popupwindows_dialog, (ViewGroup) null, false);
        PopupWindow popupWindow = new PopupWindow(viewInflate, AutoLayoutExtendsUtils.widthConvert(643), AutoLayoutExtendsUtils.widthConvert(342));
        TextView textView = (TextView) viewInflate.findViewById(R.id.tv_title);
        TextView textView2 = (TextView) viewInflate.findViewById(R.id.tv_content);
        TextView textView3 = (TextView) viewInflate.findViewById(R.id.tv_button_text);
        RelativeLayout relativeLayout = (RelativeLayout) viewInflate.findViewById(R.id.rel_byn);
        textView.setText(str);
        textView3.setText(str3);
        textView2.setText(str2);
        relativeLayout.setOnClickListener(onClickListener);
        popupWindow.showAtLocation(view, 17, 0, 0);
        backgroundAlpha(0.7f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: im.helmsman.helmsmanandroid.utils.ViewUtils.4
            @Override // android.widget.PopupWindow.OnDismissListener
            public void onDismiss() {
                ViewUtils.backgroundAlpha(1.0f);
            }
        });
        return popupWindow;
    }

    public static PopupWindow showPopupWindows(View view, int i, int i2, int i3, View.OnClickListener onClickListener) {
        MyApplication myApplication = MyApplication.getInstance();
        return showPopupWindows(view, myApplication.getString(i), myApplication.getString(i2), myApplication.getString(i3), onClickListener);
    }

    public static void backgroundAlpha(float f) {
        WindowManager.LayoutParams attributes = Base2Activity.getTopActivity().getWindow().getAttributes();
        attributes.alpha = f;
        Base2Activity.getTopActivity().getWindow().setAttributes(attributes);
    }

    public static AlertDialog showAlertDialog(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, CharSequence charSequence4, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2) {
        try {
            AlertDialog alertDialog2 = alertDialog;
            if (alertDialog2 != null && alertDialog2.isShowing()) {
                alertDialog.dismiss();
            }
        } catch (Exception unused) {
        }
        Activity topActivity = Base2Activity.getTopActivity();
        if (topActivity == null) {
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(topActivity);
        builder.setMessage(charSequence2);
        if (TextUtils.isEmpty(charSequence3)) {
            builder.setPositiveButton(R.string.app_ok, new DialogInterface.OnClickListener() { // from class: im.helmsman.helmsmanandroid.utils.ViewUtils.5
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    ViewUtils.dimissAlertDialog();
                }
            });
        } else {
            builder.setPositiveButton(charSequence3, onClickListener);
        }
        if (!TextUtils.isEmpty(charSequence)) {
            builder.setTitle(charSequence);
        }
        if (!TextUtils.isEmpty(charSequence4)) {
            builder.setNegativeButton(charSequence4, onClickListener2);
        }
        AlertDialog alertDialogCreate = builder.create();
        alertDialog = alertDialogCreate;
        alertDialogCreate.setCancelable(false);
        alertDialog.show();
        return alertDialog;
    }

    public static AlertDialog showAlertDialog(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, CharSequence charSequence4, DialogInterface.OnClickListener onClickListener) {
        return showAlertDialog(charSequence, charSequence2, charSequence3, charSequence4, onClickListener, null);
    }

    public static AlertDialog showAlertDialog(int i, int i2, int i3, int i4, DialogInterface.OnClickListener onClickListener) {
        Context context = MyApplication.getContext();
        return showAlertDialog(context.getString(i), context.getString(i2), context.getString(i3), i4 != 0 ? context.getString(i4) : null, onClickListener);
    }

    public static void dimissAlertDialog() {
        AlertDialog alertDialog2 = alertDialog;
        if (alertDialog2 == null || !alertDialog2.isShowing()) {
            return;
        }
        alertDialog.dismiss();
    }
}
