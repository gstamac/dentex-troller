package org.bouncycastle.crypto;

import java.math.BigInteger;

/* JADX INFO: loaded from: classes2.dex */
public interface BasicAgreement {
    BigInteger calculateAgreement(CipherParameters cipherParameters);

    void init(CipherParameters cipherParameters);
}
