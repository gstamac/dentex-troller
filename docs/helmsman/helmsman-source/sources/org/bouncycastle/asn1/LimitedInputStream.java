package org.bouncycastle.asn1;

import java.io.InputStream;

/* JADX INFO: loaded from: classes2.dex */
abstract class LimitedInputStream extends InputStream {
    protected final InputStream _in;

    LimitedInputStream(InputStream inputStream) {
        this._in = inputStream;
    }

    protected void setParentEofDetect(boolean z) {
        InputStream inputStream = this._in;
        if (inputStream instanceof IndefiniteLengthInputStream) {
            ((IndefiniteLengthInputStream) inputStream).setEofOn00(z);
        }
    }
}
