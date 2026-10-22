package org.bouncycastle.crypto.signers;

import java.security.SecureRandom;
import kotlin.UByte;
import kotlin.jvm.internal.ByteCompanionObject;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.SignerWithRecovery;
import org.bouncycastle.crypto.digests.RIPEMD128Digest;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.params.ParametersWithSalt;
import org.bouncycastle.crypto.params.RSAKeyParameters;

/* JADX INFO: loaded from: classes2.dex */
public class ISO9796d2PSSSigner implements SignerWithRecovery {
    public static final int TRAILER_IMPLICIT = 188;
    public static final int TRAILER_RIPEMD128 = 13004;
    public static final int TRAILER_RIPEMD160 = 12748;
    public static final int TRAILER_SHA1 = 13260;
    private byte[] block;
    private AsymmetricBlockCipher cipher;
    private Digest digest;
    private boolean fullMessage;
    private int hLen;
    private int keyBits;
    private byte[] mBuf;
    private int messageLength;
    private SecureRandom random;
    private byte[] recoveredMessage;
    private int saltLength;
    private byte[] standardSalt;
    private int trailer;

    public ISO9796d2PSSSigner(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, int i) {
        this(asymmetricBlockCipher, digest, i, false);
    }

    public ISO9796d2PSSSigner(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, int i, boolean z) {
        int i2;
        this.cipher = asymmetricBlockCipher;
        this.digest = digest;
        this.hLen = digest.getDigestSize();
        this.saltLength = i;
        if (z) {
            i2 = 188;
        } else if (digest instanceof SHA1Digest) {
            i2 = 13260;
        } else if (digest instanceof RIPEMD160Digest) {
            i2 = 12748;
        } else {
            if (!(digest instanceof RIPEMD128Digest)) {
                throw new IllegalArgumentException("no valid trailer for digest");
            }
            i2 = 13004;
        }
        this.trailer = i2;
    }

    private void ItoOSP(int i, byte[] bArr) {
        bArr[0] = (byte) (i >>> 24);
        bArr[1] = (byte) (i >>> 16);
        bArr[2] = (byte) (i >>> 8);
        bArr[3] = (byte) (i >>> 0);
    }

    private void LtoOSP(long j, byte[] bArr) {
        bArr[0] = (byte) (j >>> 56);
        bArr[1] = (byte) (j >>> 48);
        bArr[2] = (byte) (j >>> 40);
        bArr[3] = (byte) (j >>> 32);
        bArr[4] = (byte) (j >>> 24);
        bArr[5] = (byte) (j >>> 16);
        bArr[6] = (byte) (j >>> 8);
        bArr[7] = (byte) (j >>> 0);
    }

    private void clearBlock(byte[] bArr) {
        for (int i = 0; i != bArr.length; i++) {
            bArr[i] = 0;
        }
    }

    private boolean isSameAs(byte[] bArr, byte[] bArr2) {
        if (this.messageLength != bArr2.length) {
            return false;
        }
        for (int i = 0; i != bArr2.length; i++) {
            if (bArr[i] != bArr2[i]) {
                return false;
            }
        }
        return true;
    }

    private byte[] maskGeneratorFunction1(byte[] bArr, int i, int i2, int i3) {
        int i4;
        byte[] bArr2 = new byte[i3];
        byte[] bArr3 = new byte[this.hLen];
        byte[] bArr4 = new byte[4];
        this.digest.reset();
        int i5 = 0;
        while (true) {
            i4 = this.hLen;
            if (i5 >= i3 / i4) {
                break;
            }
            ItoOSP(i5, bArr4);
            this.digest.update(bArr, i, i2);
            this.digest.update(bArr4, 0, 4);
            this.digest.doFinal(bArr3, 0);
            int i6 = this.hLen;
            System.arraycopy(bArr3, 0, bArr2, i5 * i6, i6);
            i5++;
        }
        if (i4 * i5 < i3) {
            ItoOSP(i5, bArr4);
            this.digest.update(bArr, i, i2);
            this.digest.update(bArr4, 0, 4);
            this.digest.doFinal(bArr3, 0);
            int i7 = this.hLen;
            System.arraycopy(bArr3, 0, bArr2, i5 * i7, i3 - (i5 * i7));
        }
        return bArr2;
    }

    @Override // org.bouncycastle.crypto.Signer
    public byte[] generateSignature() throws CryptoException {
        int digestSize = this.digest.getDigestSize();
        byte[] bArr = new byte[digestSize];
        this.digest.doFinal(bArr, 0);
        byte[] bArr2 = new byte[8];
        LtoOSP(this.messageLength * 8, bArr2);
        this.digest.update(bArr2, 0, 8);
        this.digest.update(this.mBuf, 0, this.messageLength);
        this.digest.update(bArr, 0, digestSize);
        byte[] bArr3 = this.standardSalt;
        if (bArr3 == null) {
            bArr3 = new byte[this.saltLength];
            this.random.nextBytes(bArr3);
        }
        this.digest.update(bArr3, 0, bArr3.length);
        int digestSize2 = this.digest.getDigestSize();
        byte[] bArr4 = new byte[digestSize2];
        this.digest.doFinal(bArr4, 0);
        int i = this.trailer == 188 ? 1 : 2;
        byte[] bArr5 = this.block;
        int length = bArr5.length;
        int i2 = this.messageLength;
        int length2 = ((((length - i2) - bArr3.length) - this.hLen) - i) - 1;
        bArr5[length2] = 1;
        int i3 = length2 + 1;
        System.arraycopy(this.mBuf, 0, bArr5, i3, i2);
        System.arraycopy(bArr3, 0, this.block, i3 + this.messageLength, bArr3.length);
        byte[] bArrMaskGeneratorFunction1 = maskGeneratorFunction1(bArr4, 0, digestSize2, (this.block.length - this.hLen) - i);
        for (int i4 = 0; i4 != bArrMaskGeneratorFunction1.length; i4++) {
            byte[] bArr6 = this.block;
            bArr6[i4] = (byte) (bArr6[i4] ^ bArrMaskGeneratorFunction1[i4]);
        }
        byte[] bArr7 = this.block;
        int length3 = bArr7.length;
        int i5 = this.hLen;
        System.arraycopy(bArr4, 0, bArr7, (length3 - i5) - i, i5);
        int i6 = this.trailer;
        if (i6 == 188) {
            byte[] bArr8 = this.block;
            bArr8[bArr8.length - 1] = PSSSigner.TRAILER_IMPLICIT;
        } else {
            byte[] bArr9 = this.block;
            bArr9[bArr9.length - 2] = (byte) (i6 >>> 8);
            bArr9[bArr9.length - 1] = (byte) i6;
        }
        byte[] bArr10 = this.block;
        bArr10[0] = (byte) (bArr10[0] & ByteCompanionObject.MAX_VALUE);
        byte[] bArrProcessBlock = this.cipher.processBlock(bArr10, 0, bArr10.length);
        clearBlock(this.mBuf);
        clearBlock(this.block);
        this.messageLength = 0;
        return bArrProcessBlock;
    }

    @Override // org.bouncycastle.crypto.SignerWithRecovery
    public byte[] getRecoveredMessage() {
        return this.recoveredMessage;
    }

    @Override // org.bouncycastle.crypto.SignerWithRecovery
    public boolean hasFullMessage() {
        return this.fullMessage;
    }

    @Override // org.bouncycastle.crypto.Signer
    public void init(boolean z, CipherParameters cipherParameters) {
        RSAKeyParameters rSAKeyParameters;
        SecureRandom secureRandom;
        int length = this.saltLength;
        if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom) cipherParameters;
            rSAKeyParameters = (RSAKeyParameters) parametersWithRandom.getParameters();
            if (z) {
                secureRandom = parametersWithRandom.getRandom();
                this.random = secureRandom;
            }
        } else if (cipherParameters instanceof ParametersWithSalt) {
            ParametersWithSalt parametersWithSalt = (ParametersWithSalt) cipherParameters;
            rSAKeyParameters = (RSAKeyParameters) parametersWithSalt.getParameters();
            byte[] salt = parametersWithSalt.getSalt();
            this.standardSalt = salt;
            length = salt.length;
            if (salt.length != this.saltLength) {
                throw new IllegalArgumentException("Fixed salt is of wrong length");
            }
        } else {
            rSAKeyParameters = (RSAKeyParameters) cipherParameters;
            if (z) {
                secureRandom = new SecureRandom();
                this.random = secureRandom;
            }
        }
        this.cipher.init(z, rSAKeyParameters);
        int iBitLength = rSAKeyParameters.getModulus().bitLength();
        this.keyBits = iBitLength;
        byte[] bArr = new byte[(iBitLength + 7) / 8];
        this.block = bArr;
        int i = this.trailer;
        int length2 = bArr.length;
        if (i == 188) {
            this.mBuf = new byte[(((length2 - this.digest.getDigestSize()) - length) - 1) - 1];
        } else {
            this.mBuf = new byte[(((length2 - this.digest.getDigestSize()) - length) - 1) - 2];
        }
        reset();
    }

    @Override // org.bouncycastle.crypto.Signer
    public void reset() {
        this.digest.reset();
        this.messageLength = 0;
        byte[] bArr = this.mBuf;
        if (bArr != null) {
            clearBlock(bArr);
        }
        byte[] bArr2 = this.recoveredMessage;
        if (bArr2 != null) {
            clearBlock(bArr2);
            this.recoveredMessage = null;
        }
        this.fullMessage = false;
    }

    @Override // org.bouncycastle.crypto.Signer
    public void update(byte b) {
        int i = this.messageLength;
        byte[] bArr = this.mBuf;
        if (i >= bArr.length) {
            this.digest.update(b);
        } else {
            this.messageLength = i + 1;
            bArr[i] = b;
        }
    }

    @Override // org.bouncycastle.crypto.Signer
    public void update(byte[] bArr, int i, int i2) {
        while (i2 > 0 && this.messageLength < this.mBuf.length) {
            update(bArr[i]);
            i++;
            i2--;
        }
        if (i2 > 0) {
            this.digest.update(bArr, i, i2);
        }
    }

    @Override // org.bouncycastle.crypto.Signer
    public boolean verifySignature(byte[] bArr) {
        int i;
        try {
            byte[] bArrProcessBlock = this.cipher.processBlock(bArr, 0, bArr.length);
            int length = bArrProcessBlock.length;
            int i2 = this.keyBits;
            if (length < (i2 + 7) / 8) {
                int i3 = (i2 + 7) / 8;
                byte[] bArr2 = new byte[i3];
                System.arraycopy(bArrProcessBlock, 0, bArr2, i3 - bArrProcessBlock.length, bArrProcessBlock.length);
                clearBlock(bArrProcessBlock);
                bArrProcessBlock = bArr2;
            }
            if (((bArrProcessBlock[bArrProcessBlock.length - 1] & UByte.MAX_VALUE) ^ 188) == 0) {
                i = 1;
            } else {
                i = 2;
                int i4 = ((bArrProcessBlock[bArrProcessBlock.length - 2] & UByte.MAX_VALUE) << 8) | (bArrProcessBlock[bArrProcessBlock.length - 1] & UByte.MAX_VALUE);
                if (i4 != 12748) {
                    if (i4 != 13004) {
                        if (i4 != 13260) {
                            throw new IllegalArgumentException("unrecognised hash in signature");
                        }
                        if (!(this.digest instanceof SHA1Digest)) {
                            throw new IllegalStateException("signer should be initialised with SHA1");
                        }
                    } else if (!(this.digest instanceof RIPEMD128Digest)) {
                        throw new IllegalStateException("signer should be initialised with RIPEMD128");
                    }
                } else if (!(this.digest instanceof RIPEMD160Digest)) {
                    throw new IllegalStateException("signer should be initialised with RIPEMD160");
                }
            }
            int i5 = this.hLen;
            byte[] bArr3 = new byte[i5];
            this.digest.doFinal(bArr3, 0);
            int length2 = bArrProcessBlock.length;
            int i6 = this.hLen;
            byte[] bArrMaskGeneratorFunction1 = maskGeneratorFunction1(bArrProcessBlock, (length2 - i6) - i, i6, (bArrProcessBlock.length - i6) - i);
            for (int i7 = 0; i7 != bArrMaskGeneratorFunction1.length; i7++) {
                bArrProcessBlock[i7] = (byte) (bArrProcessBlock[i7] ^ bArrMaskGeneratorFunction1[i7]);
            }
            bArrProcessBlock[0] = (byte) (bArrProcessBlock[0] & ByteCompanionObject.MAX_VALUE);
            int i8 = 0;
            while (i8 != bArrProcessBlock.length && bArrProcessBlock[i8] != 1) {
                i8++;
            }
            int i9 = i8 + 1;
            if (i9 >= bArrProcessBlock.length) {
                clearBlock(bArrProcessBlock);
                return false;
            }
            this.fullMessage = i9 > 1;
            byte[] bArr4 = new byte[(bArrMaskGeneratorFunction1.length - i9) - this.saltLength];
            this.recoveredMessage = bArr4;
            System.arraycopy(bArrProcessBlock, i9, bArr4, 0, bArr4.length);
            byte[] bArr5 = new byte[8];
            LtoOSP(this.recoveredMessage.length * 8, bArr5);
            this.digest.update(bArr5, 0, 8);
            byte[] bArr6 = this.recoveredMessage;
            if (bArr6.length != 0) {
                this.digest.update(bArr6, 0, bArr6.length);
            }
            this.digest.update(bArr3, 0, i5);
            this.digest.update(bArrProcessBlock, i9 + this.recoveredMessage.length, this.saltLength);
            int digestSize = this.digest.getDigestSize();
            byte[] bArr7 = new byte[digestSize];
            this.digest.doFinal(bArr7, 0);
            int length3 = (bArrProcessBlock.length - i) - digestSize;
            for (int i10 = 0; i10 != digestSize; i10++) {
                if (bArr7[i10] != bArrProcessBlock[length3 + i10]) {
                    clearBlock(bArrProcessBlock);
                    clearBlock(bArr7);
                    clearBlock(this.recoveredMessage);
                    this.fullMessage = false;
                    return false;
                }
            }
            clearBlock(bArrProcessBlock);
            clearBlock(bArr7);
            if (this.messageLength != 0) {
                if (!isSameAs(this.mBuf, this.recoveredMessage)) {
                    clearBlock(this.mBuf);
                    return false;
                }
                this.messageLength = 0;
            }
            clearBlock(this.mBuf);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }
}
