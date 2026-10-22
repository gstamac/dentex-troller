package im.helmsman.helmsmanandroid.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* JADX INFO: loaded from: classes2.dex */
public class AppForegroundStateManager {
    public static final long APP_CLOSED_VALIDATION_TIME_IN_MS = 0;
    private static final int MESSAGE_NOTIFY_LISTENERS = 1;
    private static final String TAG = "AppForegroundStateManager";
    private AppForegroundState mAppForegroundState;
    private Reference<Activity> mForegroundActivity;
    private NotifyListenersHandler mHandler;
    private Set<OnAppForegroundStateChangeListener> mListeners;

    public enum AppForegroundState {
        IN_FOREGROUND,
        NOT_IN_FOREGROUND
    }

    public interface OnAppForegroundStateChangeListener {
        void onAppForegroundStateChange(AppForegroundState appForegroundState);
    }

    private static class SingletonHolder {
        public static final AppForegroundStateManager INSTANCE = new AppForegroundStateManager();

        private SingletonHolder() {
        }
    }

    public static AppForegroundStateManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private AppForegroundStateManager() {
        this.mListeners = new HashSet();
        this.mAppForegroundState = AppForegroundState.NOT_IN_FOREGROUND;
        this.mHandler = new NotifyListenersHandler(Looper.getMainLooper());
    }

    public void onActivityVisible(Activity activity) {
        Reference<Activity> reference = this.mForegroundActivity;
        if (reference != null) {
            reference.clear();
        }
        this.mForegroundActivity = new WeakReference(activity);
        determineAppForegroundState();
    }

    public void onActivityNotVisible(Activity activity) {
        Reference<Activity> reference = this.mForegroundActivity;
        if (reference != null && activity == reference.get()) {
            this.mForegroundActivity.clear();
            this.mForegroundActivity = null;
        }
        determineAppForegroundState();
    }

    public Boolean isAppInForeground() {
        return Boolean.valueOf(this.mAppForegroundState == AppForegroundState.IN_FOREGROUND);
    }

    private void determineAppForegroundState() {
        AppForegroundState appForegroundState = this.mAppForegroundState;
        Reference<Activity> reference = this.mForegroundActivity;
        AppForegroundState appForegroundState2 = reference != null && reference.get() != null ? AppForegroundState.IN_FOREGROUND : AppForegroundState.NOT_IN_FOREGROUND;
        this.mAppForegroundState = appForegroundState2;
        if (appForegroundState2 != appForegroundState) {
            validateThenNotifyListeners();
        }
    }

    public void addListener(OnAppForegroundStateChangeListener onAppForegroundStateChangeListener) {
        this.mListeners.add(onAppForegroundStateChangeListener);
    }

    public void removeListener(OnAppForegroundStateChangeListener onAppForegroundStateChangeListener) {
        this.mListeners.remove(onAppForegroundStateChangeListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyListeners(AppForegroundState appForegroundState) {
        Log.i(TAG, "Notifying subscribers that app just entered state: " + appForegroundState);
        Iterator<OnAppForegroundStateChangeListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onAppForegroundStateChange(appForegroundState);
        }
    }

    private void validateThenNotifyListeners() {
        if (this.mHandler.hasMessages(1)) {
            Log.v(TAG, "Validation Failed: Throwing out app foreground state change notification");
            this.mHandler.removeMessages(1);
        } else if (this.mAppForegroundState == AppForegroundState.IN_FOREGROUND) {
            this.mHandler.sendEmptyMessage(1);
        } else {
            this.mHandler.sendEmptyMessageDelayed(1, 0L);
        }
    }

    private class NotifyListenersHandler extends Handler {
        private NotifyListenersHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 1) {
                Log.v(AppForegroundStateManager.TAG, "App just changed foreground state to: " + AppForegroundStateManager.this.mAppForegroundState);
                AppForegroundStateManager appForegroundStateManager = AppForegroundStateManager.this;
                appForegroundStateManager.notifyListeners(appForegroundStateManager.mAppForegroundState);
                return;
            }
            super.handleMessage(message);
        }
    }
}
