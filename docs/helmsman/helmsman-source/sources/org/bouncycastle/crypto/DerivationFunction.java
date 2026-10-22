package org.bouncycastle.crypto;

/* JADX INFO: loaded from: classes2.dex */
public interface DerivationFunction {
    int generateBytes(byte[] bArr, int i, int i2) throws DataLengthException, IllegalArgumentException;

    Digest getDigest();

    void init(DerivationParameters derivationParameters);
}
