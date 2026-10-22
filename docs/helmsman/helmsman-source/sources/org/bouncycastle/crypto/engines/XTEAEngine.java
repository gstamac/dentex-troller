package org.bouncycastle.crypto.engines;

import kotlin.UByte;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.KeyParameter;

/* JADX INFO: loaded from: classes2.dex */
public class XTEAEngine implements BlockCipher {
    private static final int block_size = 8;
    private static final int delta = -1640531527;
    private static final int rounds = 32;
    private boolean _forEncryption;
    private int[] _S = new int[4];
    private int[] _sum0 = new int[32];
    private int[] _sum1 = new int[32];
    private boolean _initialised = false;

    private int bytesToInt(byte[] bArr, int i) {
        int i2 = i + 1;
        int i3 = i2 + 1;
        int i4 = (bArr[i] << 24) | ((bArr[i2] & UByte.MAX_VALUE) << 16);
        int i5 = i3 + 1;
        return (bArr[i5] & UByte.MAX_VALUE) | i4 | ((bArr[i3] & UByte.MAX_VALUE) << 8);
    }

    private int decryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int iBytesToInt = bytesToInt(bArr, i);
        int iBytesToInt2 = bytesToInt(bArr, i + 4);
        for (int i3 = 31; i3 >= 0; i3--) {
            iBytesToInt2 -= (((iBytesToInt << 4) ^ (iBytesToInt >>> 5)) + iBytesToInt) ^ this._sum1[i3];
            iBytesToInt -= (((iBytesToInt2 << 4) ^ (iBytesToInt2 >>> 5)) + iBytesToInt2) ^ this._sum0[i3];
        }
        unpackInt(iBytesToInt, bArr2, i2);
        unpackInt(iBytesToInt2, bArr2, i2 + 4);
        return 8;
    }

    private int encryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int iBytesToInt = bytesToInt(bArr, i);
        int iBytesToInt2 = bytesToInt(bArr, i + 4);
        for (int i3 = 0; i3 < 32; i3++) {
            iBytesToInt += (((iBytesToInt2 << 4) ^ (iBytesToInt2 >>> 5)) + iBytesToInt2) ^ this._sum0[i3];
            iBytesToInt2 += (((iBytesToInt << 4) ^ (iBytesToInt >>> 5)) + iBytesToInt) ^ this._sum1[i3];
        }
        unpackInt(iBytesToInt, bArr2, i2);
        unpackInt(iBytesToInt2, bArr2, i2 + 4);
        return 8;
    }

    private void setKey(byte[] bArr) {
        int i = 0;
        int i2 = 0;
        while (i < 4) {
            this._S[i] = bytesToInt(bArr, i2);
            i++;
            i2 += 4;
        }
        int i3 = 0;
        for (int i4 = 0; i4 < 32; i4++) {
            int[] iArr = this._sum0;
            int[] iArr2 = this._S;
            iArr[i4] = iArr2[i3 & 3] + i3;
            i3 -= 1640531527;
            this._sum1[i4] = iArr2[(i3 >>> 11) & 3] + i3;
        }
    }

    private void unpackInt(int i, byte[] bArr, int i2) {
        int i3 = i2 + 1;
        bArr[i2] = (byte) (i >>> 24);
        int i4 = i3 + 1;
        bArr[i3] = (byte) (i >>> 16);
        bArr[i4] = (byte) (i >>> 8);
        bArr[i4 + 1] = (byte) i;
    }

    @Override // org.bouncycastle.crypto.BlockCipher
    public String getAlgorithmName() {
        return "XTEA";
    }

    @Override // org.bouncycastle.crypto.BlockCipher
    public int getBlockSize() {
        return 8;
    }

    @Override // org.bouncycastle.crypto.BlockCipher
    public void init(boolean z, CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof KeyParameter)) {
            throw new IllegalArgumentException("invalid parameter passed to TEA init - " + cipherParameters.getClass().getName());
        }
        this._forEncryption = z;
        this._initialised = true;
        setKey(((KeyParameter) cipherParameters).getKey());
    }

    @Override // org.bouncycastle.crypto.BlockCipher
    public int processBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        if (!this._initialised) {
            throw new IllegalStateException(getAlgorithmName() + " not initialised");
        }
        if (i + 8 > bArr.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (i2 + 8 <= bArr2.length) {
            return this._forEncryption ? encryptBlock(bArr, i, bArr2, i2) : decryptBlock(bArr, i, bArr2, i2);
        }
        throw new DataLengthException("output buffer too short");
    }

    @Override // org.bouncycastle.crypto.BlockCipher
    public void reset() {
    }
}
