package com.facebook.appevents.codeless;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.appevents.codeless.internal.Constants;
import com.facebook.appevents.codeless.internal.EventBinding;
import com.facebook.appevents.codeless.internal.ViewHierarchy;
import com.facebook.appevents.internal.AppEventUtility;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes.dex */
public class CodelessLoggingEventListener {
    private static final String TAG = "com.facebook.appevents.codeless.CodelessLoggingEventListener";

    public static AutoLoggingAccessibilityDelegate getAccessibilityDelegate(EventBinding eventBinding, View view, View view2) {
        return new AutoLoggingAccessibilityDelegate(eventBinding, view, view2);
    }

    public static class AutoLoggingAccessibilityDelegate extends View.AccessibilityDelegate {
        private int accessibilityEventType;
        private View.AccessibilityDelegate existingDelegate;
        private WeakReference<View> hostView;
        private EventBinding mapping;
        private WeakReference<View> rootView;
        protected boolean supportButtonIndexing;
        private boolean supportCodelessLogging;

        public AutoLoggingAccessibilityDelegate() {
            this.supportCodelessLogging = false;
            this.supportButtonIndexing = false;
        }

        public AutoLoggingAccessibilityDelegate(EventBinding eventBinding, View view, View view2) {
            this.supportCodelessLogging = false;
            this.supportButtonIndexing = false;
            if (eventBinding == null || view == null || view2 == null) {
                return;
            }
            this.existingDelegate = ViewHierarchy.getExistingDelegate(view2);
            this.mapping = eventBinding;
            this.hostView = new WeakReference<>(view2);
            this.rootView = new WeakReference<>(view);
            EventBinding.ActionType type = eventBinding.getType();
            int i = AnonymousClass1.$SwitchMap$com$facebook$appevents$codeless$internal$EventBinding$ActionType[eventBinding.getType().ordinal()];
            if (i == 1) {
                this.accessibilityEventType = 1;
            } else if (i == 2) {
                this.accessibilityEventType = 4;
            } else if (i == 3) {
                this.accessibilityEventType = 16;
            } else {
                throw new FacebookException("Unsupported action type: " + type.toString());
            }
            this.supportCodelessLogging = true;
        }

        @Override // android.view.View.AccessibilityDelegate
        public void sendAccessibilityEvent(View view, int i) {
            if (i == -1) {
                Log.e(CodelessLoggingEventListener.TAG, "Unsupported action type");
            }
            if (i != this.accessibilityEventType) {
                return;
            }
            View.AccessibilityDelegate accessibilityDelegate = this.existingDelegate;
            if (accessibilityDelegate != null && !(accessibilityDelegate instanceof AutoLoggingAccessibilityDelegate)) {
                accessibilityDelegate.sendAccessibilityEvent(view, i);
            }
            logEvent();
        }

        private void logEvent() {
            final String eventName = this.mapping.getEventName();
            final Bundle parameters = CodelessMatcher.getParameters(this.mapping, this.rootView.get(), this.hostView.get());
            if (parameters.containsKey(AppEventsConstants.EVENT_PARAM_VALUE_TO_SUM)) {
                parameters.putDouble(AppEventsConstants.EVENT_PARAM_VALUE_TO_SUM, AppEventUtility.normalizePrice(parameters.getString(AppEventsConstants.EVENT_PARAM_VALUE_TO_SUM)));
            }
            parameters.putString(Constants.IS_CODELESS_EVENT_KEY, "1");
            FacebookSdk.getExecutor().execute(new Runnable() { // from class: com.facebook.appevents.codeless.CodelessLoggingEventListener.AutoLoggingAccessibilityDelegate.1
                @Override // java.lang.Runnable
                public void run() {
                    AppEventsLogger.newLogger(FacebookSdk.getApplicationContext()).logEvent(eventName, parameters);
                }
            });
        }

        public boolean getSupportCodelessLogging() {
            return this.supportCodelessLogging;
        }

        public boolean getSupportButtonIndexing() {
            return this.supportButtonIndexing;
        }
    }

    /* JADX INFO: renamed from: com.facebook.appevents.codeless.CodelessLoggingEventListener$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$facebook$appevents$codeless$internal$EventBinding$ActionType;

        static {
            int[] iArr = new int[EventBinding.ActionType.values().length];
            $SwitchMap$com$facebook$appevents$codeless$internal$EventBinding$ActionType = iArr;
            try {
                iArr[EventBinding.ActionType.CLICK.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$facebook$appevents$codeless$internal$EventBinding$ActionType[EventBinding.ActionType.SELECTED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$facebook$appevents$codeless$internal$EventBinding$ActionType[EventBinding.ActionType.TEXT_CHANGED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }
}
