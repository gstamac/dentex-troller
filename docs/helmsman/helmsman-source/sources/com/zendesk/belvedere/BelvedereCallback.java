package com.zendesk.belvedere;

import android.os.Handler;
import android.os.Looper;

/* JADX INFO: loaded from: classes2.dex */
public abstract class BelvedereCallback<E> {
    private boolean canceled = false;

    public abstract void success(E e);

    public void cancel() {
        this.canceled = true;
    }

    void internalSuccess(final E e) {
        if (this.canceled) {
            return;
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.zendesk.belvedere.BelvedereCallback.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.lang.Runnable
            public void run() {
                BelvedereCallback.this.success(e);
            }
        });
    }
}
