package org.bouncycastle.crypto.params;

import java.math.BigInteger;

/* JADX INFO: loaded from: classes2.dex */
public class DSAPrivateKeyParameters extends DSAKeyParameters {
    private BigInteger x;

    public DSAPrivateKeyParameters(BigInteger bigInteger, DSAParameters dSAParameters) {
        super(true, dSAParameters);
        this.x = bigInteger;
    }

    public BigInteger getX() {
        return this.x;
    }
}
