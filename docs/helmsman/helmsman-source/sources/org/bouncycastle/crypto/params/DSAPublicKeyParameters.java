package org.bouncycastle.crypto.params;

import java.math.BigInteger;

/* JADX INFO: loaded from: classes2.dex */
public class DSAPublicKeyParameters extends DSAKeyParameters {
    private BigInteger y;

    public DSAPublicKeyParameters(BigInteger bigInteger, DSAParameters dSAParameters) {
        super(false, dSAParameters);
        this.y = bigInteger;
    }

    public BigInteger getY() {
        return this.y;
    }
}
