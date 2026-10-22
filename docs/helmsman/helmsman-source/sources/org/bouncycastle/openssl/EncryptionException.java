package org.bouncycastle.openssl;

import java.io.IOException;

/* JADX INFO: loaded from: classes2.dex */
public class EncryptionException extends IOException {
    private Throwable cause;

    public EncryptionException(String str) {
        super(str);
    }

    public EncryptionException(String str, Throwable th) {
        super(str);
        this.cause = th;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.cause;
    }
}
