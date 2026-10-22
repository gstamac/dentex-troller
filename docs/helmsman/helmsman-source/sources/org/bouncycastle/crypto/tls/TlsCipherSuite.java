package org.bouncycastle.crypto.tls;

import java.io.IOException;

/* JADX INFO: loaded from: classes2.dex */
public abstract class TlsCipherSuite {
    protected static final short KE_DHE_DSS = 3;
    protected static final short KE_DHE_DSS_EXPORT = 4;
    protected static final short KE_DHE_RSA = 5;
    protected static final short KE_DHE_RSA_EXPORT = 6;
    protected static final short KE_DH_DSS = 7;
    protected static final short KE_DH_RSA = 8;
    protected static final short KE_DH_anon = 9;
    protected static final short KE_RSA = 1;
    protected static final short KE_RSA_EXPORT = 2;
    protected static final short KE_SRP = 10;
    protected static final short KE_SRP_DSS = 12;
    protected static final short KE_SRP_RSA = 11;

    protected abstract byte[] decodeCiphertext(short s, byte[] bArr, int i, int i2, TlsProtocolHandler tlsProtocolHandler) throws IOException;

    protected abstract byte[] encodePlaintext(short s, byte[] bArr, int i, int i2);

    protected abstract short getKeyExchangeAlgorithm();

    protected abstract void init(byte[] bArr, byte[] bArr2, byte[] bArr3);
}
