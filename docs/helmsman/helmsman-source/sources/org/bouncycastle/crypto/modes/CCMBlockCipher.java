package org.bouncycastle.crypto.modes;

import java.io.ByteArrayOutputStream;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.macs.CBCBlockCipherMac;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Arrays;

/* JADX INFO: loaded from: classes2.dex */
public class CCMBlockCipher implements AEADBlockCipher {
    private byte[] associatedText;
    private int blockSize;
    private BlockCipher cipher;
    private ByteArrayOutputStream data = new ByteArrayOutputStream();
    private boolean forEncryption;
    private CipherParameters keyParam;
    private byte[] macBlock;
    private int macSize;
    private byte[] nonce;

    public CCMBlockCipher(BlockCipher blockCipher) {
        this.cipher = blockCipher;
        int blockSize = blockCipher.getBlockSize();
        this.blockSize = blockSize;
        this.macBlock = new byte[blockSize];
        if (blockSize != 16) {
            throw new IllegalArgumentException("cipher required with a block size of 16.");
        }
    }

    private int calculateMac(byte[] bArr, int i, int i2, byte[] bArr2) {
        CBCBlockCipherMac cBCBlockCipherMac = new CBCBlockCipherMac(this.cipher, this.macSize * 8);
        cBCBlockCipherMac.init(this.keyParam);
        byte[] bArr3 = new byte[16];
        if (hasAssociatedText()) {
            bArr3[0] = (byte) (bArr3[0] | 64);
        }
        int i3 = 2;
        byte macSize = (byte) (bArr3[0] | ((((cBCBlockCipherMac.getMacSize() - 2) / 2) & 7) << 3));
        bArr3[0] = macSize;
        byte[] bArr4 = this.nonce;
        bArr3[0] = (byte) (macSize | (((15 - bArr4.length) - 1) & 7));
        System.arraycopy(bArr4, 0, bArr3, 1, bArr4.length);
        int i4 = i2;
        int i5 = 1;
        while (i4 > 0) {
            bArr3[16 - i5] = (byte) (i4 & 255);
            i4 >>>= 8;
            i5++;
        }
        cBCBlockCipherMac.update(bArr3, 0, 16);
        if (hasAssociatedText()) {
            byte[] bArr5 = this.associatedText;
            if (bArr5.length < 65280) {
                cBCBlockCipherMac.update((byte) (bArr5.length >> 8));
                cBCBlockCipherMac.update((byte) this.associatedText.length);
            } else {
                cBCBlockCipherMac.update((byte) -1);
                cBCBlockCipherMac.update((byte) -2);
                cBCBlockCipherMac.update((byte) (this.associatedText.length >> 24));
                cBCBlockCipherMac.update((byte) (this.associatedText.length >> 16));
                cBCBlockCipherMac.update((byte) (this.associatedText.length >> 8));
                cBCBlockCipherMac.update((byte) this.associatedText.length);
                i3 = 6;
            }
            byte[] bArr6 = this.associatedText;
            cBCBlockCipherMac.update(bArr6, 0, bArr6.length);
            int length = (i3 + this.associatedText.length) % 16;
            if (length != 0) {
                for (int i6 = 0; i6 != 16 - length; i6++) {
                    cBCBlockCipherMac.update((byte) 0);
                }
            }
        }
        cBCBlockCipherMac.update(bArr, i, i2);
        return cBCBlockCipherMac.doFinal(bArr2, 0);
    }

    private boolean hasAssociatedText() {
        byte[] bArr = this.associatedText;
        return (bArr == null || bArr.length == 0) ? false : true;
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public int doFinal(byte[] bArr, int i) throws InvalidCipherTextException, IllegalStateException {
        byte[] byteArray = this.data.toByteArray();
        byte[] bArrProcessPacket = processPacket(byteArray, 0, byteArray.length);
        System.arraycopy(bArrProcessPacket, 0, bArr, i, bArrProcessPacket.length);
        reset();
        return bArrProcessPacket.length;
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public String getAlgorithmName() {
        return this.cipher.getAlgorithmName() + "/CCM";
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public byte[] getMac() {
        int i = this.macSize;
        byte[] bArr = new byte[i];
        System.arraycopy(this.macBlock, 0, bArr, 0, i);
        return bArr;
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public int getOutputSize(int i) {
        return this.forEncryption ? this.data.size() + i + this.macSize : (this.data.size() + i) - this.macSize;
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public BlockCipher getUnderlyingCipher() {
        return this.cipher;
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public int getUpdateOutputSize(int i) {
        return 0;
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public void init(boolean z, CipherParameters cipherParameters) throws IllegalArgumentException {
        CipherParameters parameters;
        this.forEncryption = z;
        if (cipherParameters instanceof AEADParameters) {
            AEADParameters aEADParameters = (AEADParameters) cipherParameters;
            this.nonce = aEADParameters.getNonce();
            this.associatedText = aEADParameters.getAssociatedText();
            this.macSize = aEADParameters.getMacSize() / 8;
            parameters = aEADParameters.getKey();
        } else {
            if (!(cipherParameters instanceof ParametersWithIV)) {
                throw new IllegalArgumentException("invalid parameters passed to CCM");
            }
            ParametersWithIV parametersWithIV = (ParametersWithIV) cipherParameters;
            this.nonce = parametersWithIV.getIV();
            this.associatedText = null;
            this.macSize = this.macBlock.length / 2;
            parameters = parametersWithIV.getParameters();
        }
        this.keyParam = parameters;
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public int processByte(byte b, byte[] bArr, int i) throws IllegalStateException, DataLengthException {
        this.data.write(b);
        return 0;
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public int processBytes(byte[] bArr, int i, int i2, byte[] bArr2, int i3) throws IllegalStateException, DataLengthException {
        this.data.write(bArr, i, i2);
        return 0;
    }

    public byte[] processPacket(byte[] bArr, int i, int i2) throws InvalidCipherTextException, IllegalStateException {
        int i3;
        if (this.keyParam == null) {
            throw new IllegalStateException("CCM cipher unitialized.");
        }
        SICBlockCipher sICBlockCipher = new SICBlockCipher(this.cipher);
        byte[] bArr2 = new byte[this.blockSize];
        byte[] bArr3 = this.nonce;
        bArr2[0] = (byte) (((15 - bArr3.length) - 1) & 7);
        System.arraycopy(bArr3, 0, bArr2, 1, bArr3.length);
        sICBlockCipher.init(this.forEncryption, new ParametersWithIV(this.keyParam, bArr2));
        if (!this.forEncryption) {
            int i4 = this.macSize;
            int i5 = i2 - i4;
            byte[] bArr4 = new byte[i5];
            System.arraycopy(bArr, (i2 + i) - i4, this.macBlock, 0, i4);
            byte[] bArr5 = this.macBlock;
            sICBlockCipher.processBlock(bArr5, 0, bArr5, 0);
            int i6 = this.macSize;
            while (true) {
                byte[] bArr6 = this.macBlock;
                if (i6 == bArr6.length) {
                    break;
                }
                bArr6[i6] = 0;
                i6++;
            }
            int i7 = 0;
            while (true) {
                i3 = this.blockSize;
                if (i7 >= i5 - i3) {
                    break;
                }
                sICBlockCipher.processBlock(bArr, i, bArr4, i7);
                int i8 = this.blockSize;
                i7 += i8;
                i += i8;
            }
            byte[] bArr7 = new byte[i3];
            int i9 = i5 - i7;
            System.arraycopy(bArr, i, bArr7, 0, i9);
            sICBlockCipher.processBlock(bArr7, 0, bArr7, 0);
            System.arraycopy(bArr7, 0, bArr4, i7, i9);
            byte[] bArr8 = new byte[this.blockSize];
            calculateMac(bArr4, 0, i5, bArr8);
            if (Arrays.constantTimeAreEqual(this.macBlock, bArr8)) {
                return bArr4;
            }
            throw new InvalidCipherTextException("mac check in CCM failed");
        }
        int i10 = this.macSize + i2;
        byte[] bArr9 = new byte[i10];
        calculateMac(bArr, i, i2, this.macBlock);
        byte[] bArr10 = this.macBlock;
        sICBlockCipher.processBlock(bArr10, 0, bArr10, 0);
        int i11 = 0;
        while (true) {
            int i12 = this.blockSize;
            if (i >= i2 - i12) {
                byte[] bArr11 = new byte[i12];
                int i13 = i2 - i;
                System.arraycopy(bArr, i, bArr11, 0, i13);
                sICBlockCipher.processBlock(bArr11, 0, bArr11, 0);
                System.arraycopy(bArr11, 0, bArr9, i11, i13);
                int i14 = i11 + i13;
                System.arraycopy(this.macBlock, 0, bArr9, i14, i10 - i14);
                return bArr9;
            }
            sICBlockCipher.processBlock(bArr, i, bArr9, i11);
            int i15 = this.blockSize;
            i11 += i15;
            i += i15;
        }
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public void reset() {
        this.cipher.reset();
        this.data.reset();
    }
}
