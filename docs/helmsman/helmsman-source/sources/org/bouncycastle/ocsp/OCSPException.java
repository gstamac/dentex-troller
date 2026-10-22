package org.bouncycastle.ocsp;

/* JADX INFO: loaded from: classes2.dex */
public class OCSPException extends Exception {
    Exception e;

    public OCSPException(String str) {
        super(str);
    }

    public OCSPException(String str, Exception exc) {
        super(str);
        this.e = exc;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.e;
    }

    public Exception getUnderlyingException() {
        return this.e;
    }
}
