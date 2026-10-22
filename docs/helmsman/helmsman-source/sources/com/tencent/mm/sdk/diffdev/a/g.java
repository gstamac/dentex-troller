package com.tencent.mm.sdk.diffdev.a;

import com.zendesk.service.HttpConstants;

/* JADX INFO: loaded from: classes2.dex */
public enum g {
    UUID_EXPIRED(402),
    UUID_CANCELED(403),
    UUID_SCANED(HttpConstants.HTTP_NOT_FOUND),
    UUID_CONFIRM(HttpConstants.HTTP_BAD_METHOD),
    UUID_KEEP_CONNECT(HttpConstants.HTTP_CLIENT_TIMEOUT),
    UUID_ERROR(500);

    private int code;

    g(int i) {
        this.code = i;
    }

    public final int getCode() {
        return this.code;
    }

    @Override // java.lang.Enum
    public final String toString() {
        return "UUIDStatusCode:" + this.code;
    }
}
