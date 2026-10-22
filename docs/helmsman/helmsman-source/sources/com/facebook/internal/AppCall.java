package com.facebook.internal;

import android.content.Intent;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public class AppCall {
    private static AppCall currentPendingCall;
    private UUID callId;
    private int requestCode;
    private Intent requestIntent;

    public static AppCall getCurrentPendingCall() {
        return currentPendingCall;
    }

    public static synchronized AppCall finishPendingCall(UUID uuid, int i) {
        AppCall currentPendingCall2 = getCurrentPendingCall();
        if (currentPendingCall2 != null && currentPendingCall2.getCallId().equals(uuid) && currentPendingCall2.getRequestCode() == i) {
            setCurrentPendingCall(null);
            return currentPendingCall2;
        }
        return null;
    }

    private static synchronized boolean setCurrentPendingCall(AppCall appCall) {
        AppCall currentPendingCall2;
        currentPendingCall2 = getCurrentPendingCall();
        currentPendingCall = appCall;
        return currentPendingCall2 != null;
    }

    public AppCall(int i) {
        this(i, UUID.randomUUID());
    }

    public AppCall(int i, UUID uuid) {
        this.callId = uuid;
        this.requestCode = i;
    }

    public Intent getRequestIntent() {
        return this.requestIntent;
    }

    public UUID getCallId() {
        return this.callId;
    }

    public int getRequestCode() {
        return this.requestCode;
    }

    public void setRequestCode(int i) {
        this.requestCode = i;
    }

    public void setRequestIntent(Intent intent) {
        this.requestIntent = intent;
    }

    public boolean setPending() {
        return setCurrentPendingCall(this);
    }
}
