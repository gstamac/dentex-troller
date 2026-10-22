package org.bouncycastle.crypto.tls;

/* JADX INFO: loaded from: classes2.dex */
public class TlsRuntimeException extends RuntimeException {
    Throwable e;

    public TlsRuntimeException(String str) {
        super(str);
    }

    public TlsRuntimeException(String str, Throwable th) {
        super(str);
        this.e = th;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.e;
    }
}
