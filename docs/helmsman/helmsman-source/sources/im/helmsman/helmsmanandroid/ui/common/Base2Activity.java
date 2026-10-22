package im.helmsman.helmsmanandroid.ui.common;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import androidx.core.view.accessibility.AccessibilityEventCompat;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.zhy.autolayout.AutoLayoutActivity;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.utils.AppForegroundStateManager;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import im.helmsman.lib.HelmsmanSDK;
import java.util.Iterator;
import java.util.Stack;
import org.greenrobot.eventbus.EventBus;

/* JADX INFO: loaded from: classes2.dex */
public abstract class Base2Activity extends AutoLayoutActivity {
    protected static Stack<Activity> activityStack = BaseActivity.activityStack;
    public static boolean isPad = false;
    protected boolean isBind = true;

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRequestedOrientation(7);
        activityStack.push(this);
    }

    public static void push(Activity activity) {
        activityStack.push(activity);
    }

    public static void remove(Activity activity) {
        activityStack.remove(activity);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void setContentView(int i) {
        super.setContentView(i);
        if (this.isBind) {
            ButterKnife.bind(this);
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStart() {
        super.onStart();
        AppForegroundStateManager.getInstance().onActivityVisible(this);
        Log.d("Activity Count", activityStack.size() + "");
        Iterator<Activity> it = activityStack.iterator();
        while (it.hasNext()) {
            Log.d("Activity Name", it.next().getClass().getName());
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStop() {
        AppForegroundStateManager.getInstance().onActivityNotVisible(this);
        super.onStop();
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 82 && !HelmsmanSDK.productIsNull() && HelmsmanSDK.getProduct().motorComponent.isOn()) {
            HelmsmanSDK.getProduct().motorComponent.motorOff();
        }
        return super.onKeyDown(i, keyEvent);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        activityStack.remove(this);
        EventBus.getDefault().unregister(this);
        ViewUtils.cancelProgressDialog();
    }

    public static Activity getTopActivity() {
        if (activityStack.isEmpty()) {
            return null;
        }
        return activityStack.peek();
    }

    protected void finishTopActivity() {
        activityStack.peek().finish();
    }

    public void finishActivity(Activity activity) {
        activityStack.elementAt(activityStack.search(activity)).finish();
    }

    public static void finishActivity(Class cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                activity.finish();
            }
        }
    }

    protected void finishAllActivity() {
        for (Activity activity : activityStack) {
            Log.d("finishAllActivity", activity.getLocalClassName());
            activity.finish();
        }
    }

    public boolean activityIsTop(Class cls) {
        return getTopActivity() != null && cls.equals(getTopActivity().getClass());
    }

    protected void registerEventBus() {
        EventBus.getDefault().register(this);
    }

    protected void setStatusBar(int i) {
        getWindow().addFlags(AccessibilityEventCompat.TYPE_VIEW_TARGETED_BY_SCROLL);
        SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
        systemBarTintManager.setStatusBarTintEnabled(true);
        systemBarTintManager.setStatusBarTintResource(i);
    }

    protected void setStatusBarImage(int i) {
        getWindow().addFlags(AccessibilityEventCompat.TYPE_VIEW_TARGETED_BY_SCROLL);
        SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
        systemBarTintManager.setStatusBarTintEnabled(true);
        systemBarTintManager.setStatusBarTintResource(i);
    }

    protected void showToast(CharSequence charSequence) {
        showToast(charSequence);
    }

    protected void showToast(int i) {
        showToast(i);
    }

    protected void showLoading(CharSequence charSequence) {
        ViewUtils.showProgressDialog(charSequence);
    }

    protected void showLoading(int i) {
        ViewUtils.showProgressDialog(i);
    }

    protected void showLoading(CharSequence charSequence, CharSequence charSequence2) {
        ViewUtils.showProgressDialog(charSequence, charSequence2);
    }

    protected void showLoading(int i, int i2) {
        Context applicationContext = MyApplication.getInstance().getApplicationContext();
        ViewUtils.showProgressDialog(applicationContext.getString(i), applicationContext.getString(i2));
    }

    protected void dimissLoading() {
        ViewUtils.cancelProgressDialog();
    }
}
