package com.bugsnag.android;

import androidx.core.app.NotificationCompat;
import com.facebook.internal.ServerProtocol;
import com.facebook.share.internal.ShareConstants;

/* JADX INFO: loaded from: classes.dex */
public enum BreadcrumbType {
    ERROR("error"),
    LOG("log"),
    MANUAL("manual"),
    NAVIGATION(NotificationCompat.CATEGORY_NAVIGATION),
    PROCESS("process"),
    REQUEST(ShareConstants.WEB_DIALOG_RESULT_PARAM_REQUEST_ID),
    STATE(ServerProtocol.DIALOG_PARAM_STATE),
    USER("user");

    private final String type;

    BreadcrumbType(String str) {
        this.type = str;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.type;
    }
}
