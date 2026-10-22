package com.zendesk.sdk.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import com.zendesk.logger.Logger;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
public class UiUtils {
    private static final String LOG_TAG = "UiUtils";

    public enum ScreenSize {
        UNKNOWN,
        UNDEFINED,
        X_LARGE,
        LARGE,
        NORMAL,
        SMALL
    }

    private UiUtils() {
    }

    public static int dpToPixels(int i, DisplayMetrics displayMetrics) {
        return Math.round(TypedValue.applyDimension(1, i, displayMetrics));
    }

    public static int spToPixels(int i, DisplayMetrics displayMetrics) {
        return Math.round(TypedValue.applyDimension(2, i, displayMetrics));
    }

    public static int themeAttributeToPixels(int i, Context context, int i2, float f) {
        TypedValue typedValue = new TypedValue();
        if (!context.getTheme().resolveAttribute(i, typedValue, true)) {
            Logger.e(LOG_TAG, String.format(Locale.US, "Resource %d not found. Resource is either missing or you are using a non-ui context.", Integer.valueOf(i)), new Object[0]);
            return Math.round(TypedValue.applyDimension(i2, f, context.getResources().getDisplayMetrics()));
        }
        return Math.round(typedValue.getDimension(context.getResources().getDisplayMetrics()));
    }

    public static int themeAttributeToColor(int i, Context context, int i2) {
        if (i == 0 || context == null || i2 == 0) {
            Logger.d(LOG_TAG, "themeAttributeId, context, and fallbackColorId are required.", new Object[0]);
            return ViewCompat.MEASURED_STATE_MASK;
        }
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(i, typedValue, true)) {
            return typedValue.resourceId == 0 ? typedValue.data : resolveColor(typedValue.resourceId, context);
        }
        Logger.e(LOG_TAG, String.format(Locale.US, "Resource %d not found. Resource is either missing or you are using a non-ui context.", Integer.valueOf(i)), new Object[0]);
        return resolveColor(i2, context);
    }

    private static int resolveColor(int i, Context context) {
        return ContextCompat.getColor(context, i);
    }

    public static ScreenSize getScreenSize(Context context) {
        int i = context.getResources().getConfiguration().screenLayout & 15;
        ScreenSize screenSize = ScreenSize.UNKNOWN;
        if (i == 1) {
            return ScreenSize.SMALL;
        }
        if (i == 2) {
            return ScreenSize.NORMAL;
        }
        if (i == 3) {
            return ScreenSize.LARGE;
        }
        if (i == 4) {
            return ScreenSize.X_LARGE;
        }
        return i == 0 ? ScreenSize.UNDEFINED : screenSize;
    }

    public static boolean isTablet(Context context) {
        ScreenSize screenSize = getScreenSize(context);
        return screenSize == ScreenSize.LARGE || screenSize == ScreenSize.X_LARGE;
    }

    public static void sizeDialogWidthForTablets(Dialog dialog, float f) {
        if (dialog == null || !isTablet(dialog.getContext())) {
            Logger.e(LOG_TAG, "Dialog is null or the device is not a tablet", new Object[0]);
        } else {
            dialog.getWindow().setLayout(Math.round(dialog.getContext().getResources().getDisplayMetrics().widthPixels * f), -2);
        }
    }

    public static void setVisibility(View view, int i) {
        if (view == null) {
            Logger.w(LOG_TAG, "View is is null and can't change visibility", new Object[0]);
        } else {
            view.setVisibility(i);
        }
    }

    public static void dismissKeyboard(Activity activity) {
        if (activity == null) {
            Logger.w(LOG_TAG, "Cannot dismiss the keyboard when fragment is detached or the activity is null.", new Object[0]);
            return;
        }
        Object systemService = activity.getSystemService("input_method");
        if (systemService instanceof InputMethodManager) {
            InputMethodManager inputMethodManager = (InputMethodManager) systemService;
            View currentFocus = activity.getCurrentFocus();
            if (currentFocus != null) {
                inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                return;
            } else {
                Logger.w(LOG_TAG, "Cannot hide soft input because window token could not be obtained", new Object[0]);
                return;
            }
        }
        Logger.w(LOG_TAG, "Cannot hide soft input because we could not get the InputMethodManager", new Object[0]);
    }
}
