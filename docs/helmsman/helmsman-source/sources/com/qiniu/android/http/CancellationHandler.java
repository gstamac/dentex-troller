package com.qiniu.android.http;

import java.io.IOException;

/* JADX INFO: loaded from: classes2.dex */
public interface CancellationHandler {

    public static class CancellationException extends IOException {
    }

    boolean isCancelled();
}
