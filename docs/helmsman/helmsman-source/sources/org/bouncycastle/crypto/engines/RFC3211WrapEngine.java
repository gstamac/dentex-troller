package org.bouncycastle.crypto.engines;

import java.security.SecureRandom;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.Wrapper;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.ParametersWithRandom;

/* JADX INFO: loaded from: classes2.dex */
public class RFC3211WrapEngine implements Wrapper {
    private CBCBlockCipher engine;
    private boolean forWrapping;
    private ParametersWithIV param;
    private SecureRandom rand;

    public RFC3211WrapEngine(BlockCipher blockCipher) {
        this.engine = new CBCBlockCipher(blockCipher);
    }

    @Override // org.bouncycastle.crypto.Wrapper
    public String getAlgorithmName() {
        return this.engine.getUnderlyingCipher().getAlgorithmName() + "/RFC3211Wrap";
    }

    @Override // org.bouncycastle.crypto.Wrapper
    public void init(boolean z, CipherParameters cipherParameters) {
        this.forWrapping = z;
        if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom) cipherParameters;
            this.rand = parametersWithRandom.getRandom();
            this.param = (ParametersWithIV) parametersWithRandom.getParameters();
        } else {
            if (z) {
                this.rand = new SecureRandom();
            }
            this.param = (ParametersWithIV) cipherParameters;
        }
    }

    @Override // org.bouncycastle.crypto.Wrapper
    public byte[] unwrap(byte[] bArr, int i, int i2) throws InvalidCipherTextException {
        if (this.forWrapping) {
            throw new IllegalStateException("not set for unwrapping");
        }
        int blockSize = this.engine.getBlockSize();
        if (i2 < blockSize * 2) {
            throw new InvalidCipherTextException("input too short");
        }
        byte[] bArr2 = new byte[i2];
        byte[] bArr3 = new byte[blockSize];
        int i3 = 0;
        System.arraycopy(bArr, i, bArr2, 0, i2);
        System.arraycopy(bArr, i, bArr3, 0, blockSize);
        this.engine.init(false, new ParametersWithIV(this.param.getParameters(), bArr3));
        for (int i4 = blockSize; i4 < i2; i4 += blockSize) {
            this.engine.processBlock(bArr2, i4, bArr2, i4);
        }
        System.arraycopy(bArr2, i2 - blockSize, bArr3, 0, blockSize);
        this.engine.init(false, new ParametersWithIV(this.param.getParameters(), bArr3));
        this.engine.processBlock(bArr2, 0, bArr2, 0);
        this.engine.init(false, this.param);
        for (int i5 = 0; i5 < i2; i5 += blockSize) {
            this.engine.processBlock(bArr2, i5, bArr2, i5);
        }
        int i6 = bArr2[0];
        if ((i6 & 255) > i2 - 4) {
            throw new InvalidCipherTextException("wrapped key corrupted");
        }
        byte[] bArr4 = new byte[i6 & 255];
        System.arraycopy(bArr2, 4, bArr4, 0, i6);
        int i7 = 0;
        while (i3 != 3) {
            int i8 = i3 + 1;
            i7 |= ((byte) (~bArr2[i8])) ^ bArr4[i3];
            i3 = i8;
        }
        if (i7 == 0) {
            return bArr4;
        }
        throw new InvalidCipherTextException("wrapped key fails checksum");
    }

    @Override // org.bouncycastle.crypto.Wrapper
    public byte[] wrap(byte[] bArr, int i, int i2) {
        if (!this.forWrapping) {
            throw new IllegalStateException("not set for wrapping");
        }
        this.engine.init(true, this.param);
        int blockSize = this.engine.getBlockSize();
        int i3 = i2 + 4;
        int i4 = blockSize * 2;
        if (i3 >= i4) {
            i4 = i3 % blockSize == 0 ? i3 : ((i3 / blockSize) + 1) * blockSize;
        }
        byte[] bArr2 = new byte[i4];
        bArr2[0] = (byte) i2;
        bArr2[1] = (byte) (~bArr[i]);
        bArr2[2] = (byte) (~bArr[i + 1]);
        bArr2[3] = (byte) (~bArr[i + 2]);
        System.arraycopy(bArr, i, bArr2, 4, i2);
        while (i3 < bArr2.length) {
            bArr2[i3] = (byte) this.rand.nextInt();
            i3++;
        }
        for (int i5 = 0; i5 < bArr2.length; i5 += blockSize) {
            this.engine.processBlock(bArr2, i5, bArr2, i5);
        }
        for (int i6 = 0; i6 < bArr2.length; i6 += blockSize) {
            this.engine.processBlock(bArr2, i6, bArr2, i6);
        }
        return bArr2;
    }
}
