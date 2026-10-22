package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import kotlin.jvm.internal.ByteCompanionObject;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.params.DSAParameters;
import org.bouncycastle.crypto.params.DSAValidationParameters;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;

/* JADX INFO: loaded from: classes2.dex */
public class DSAParametersGenerator {
    private int L;
    private int N;
    private int certainty;
    private SecureRandom random;
    private static final BigInteger ZERO = BigInteger.valueOf(0);
    private static final BigInteger ONE = BigInteger.valueOf(1);
    private static final BigInteger TWO = BigInteger.valueOf(2);

    private static BigInteger calculateGenerator_FIPS186_2(BigInteger bigInteger, BigInteger bigInteger2, SecureRandom secureRandom) {
        BigInteger bigIntegerModPow;
        BigInteger bigIntegerDivide = bigInteger.subtract(ONE).divide(bigInteger2);
        BigInteger bigIntegerSubtract = bigInteger.subtract(TWO);
        do {
            bigIntegerModPow = BigIntegers.createRandomInRange(TWO, bigIntegerSubtract, secureRandom).modPow(bigIntegerDivide, bigInteger);
        } while (bigIntegerModPow.bitLength() <= 1);
        return bigIntegerModPow;
    }

    private static BigInteger calculateGenerator_FIPS186_3_Unverifiable(BigInteger bigInteger, BigInteger bigInteger2, SecureRandom secureRandom) {
        return calculateGenerator_FIPS186_2(bigInteger, bigInteger2, secureRandom);
    }

    private DSAParameters generateParameters_FIPS186_2() {
        int i = 20;
        byte[] bArr = new byte[20];
        byte[] bArr2 = new byte[20];
        byte[] bArr3 = new byte[20];
        byte[] bArr4 = new byte[20];
        SHA1Digest sHA1Digest = new SHA1Digest();
        int i2 = this.L;
        int i3 = (i2 - 1) / 160;
        int i4 = i2 / 8;
        byte[] bArr5 = new byte[i4];
        while (true) {
            this.random.nextBytes(bArr);
            hash(sHA1Digest, bArr, bArr2);
            int i5 = 0;
            System.arraycopy(bArr, 0, bArr3, 0, i);
            inc(bArr3);
            hash(sHA1Digest, bArr3, bArr3);
            for (int i6 = 0; i6 != i; i6++) {
                bArr4[i6] = (byte) (bArr2[i6] ^ bArr3[i6]);
            }
            bArr4[0] = (byte) (bArr4[0] | ByteCompanionObject.MIN_VALUE);
            bArr4[19] = (byte) (bArr4[19] | 1);
            BigInteger bigInteger = new BigInteger(1, bArr4);
            if (bigInteger.isProbablePrime(this.certainty)) {
                byte[] bArrClone = Arrays.clone(bArr);
                inc(bArrClone);
                int i7 = 0;
                while (i7 < 4096) {
                    int i8 = i5;
                    while (true) {
                        inc(bArrClone);
                        hash(sHA1Digest, bArrClone, bArr2);
                        if (i8 >= i3) {
                            break;
                        }
                        i8++;
                        System.arraycopy(bArr2, i5, bArr5, i4 - (i8 * 20), i);
                    }
                    int i9 = i4 - (i3 * 20);
                    System.arraycopy(bArr2, 20 - i9, bArr5, i5, i9);
                    bArr5[i5] = (byte) (bArr5[i5] | ByteCompanionObject.MIN_VALUE);
                    BigInteger bigInteger2 = new BigInteger(1, bArr5);
                    BigInteger bigIntegerSubtract = bigInteger2.subtract(bigInteger2.mod(bigInteger.shiftLeft(1)).subtract(ONE));
                    if (bigIntegerSubtract.bitLength() == this.L && bigIntegerSubtract.isProbablePrime(this.certainty)) {
                        return new DSAParameters(bigIntegerSubtract, bigInteger, calculateGenerator_FIPS186_2(bigIntegerSubtract, bigInteger, this.random), new DSAValidationParameters(bArr, i7));
                    }
                    i7++;
                    i = 20;
                    i5 = 0;
                }
            }
        }
    }

    private DSAParameters generateParameters_FIPS186_3() {
        SHA256Digest sHA256Digest = new SHA256Digest();
        int digestSize = sHA256Digest.getDigestSize() * 8;
        byte[] bArr = new byte[this.N / 8];
        int i = this.L;
        int i2 = (i - 1) / digestSize;
        int i3 = 1;
        int i4 = (i - 1) % digestSize;
        byte[] bArr2 = new byte[sHA256Digest.getDigestSize()];
        while (true) {
            this.random.nextBytes(bArr);
            hash(sHA256Digest, bArr, bArr2);
            BigInteger bigInteger = new BigInteger(i3, bArr2);
            BigInteger bigInteger2 = ONE;
            BigInteger bigIntegerMod = bigInteger.mod(bigInteger2.shiftLeft(this.N - i3));
            BigInteger bigIntegerSubtract = bigInteger2.shiftLeft(this.N - i3).add(bigIntegerMod).add(bigInteger2).subtract(bigIntegerMod.mod(TWO));
            if (bigIntegerSubtract.isProbablePrime(this.certainty)) {
                byte[] bArrClone = Arrays.clone(bArr);
                int i5 = this.L * 4;
                int i6 = 0;
                while (i6 < i5) {
                    BigInteger bigIntegerAdd = ZERO;
                    int i7 = 0;
                    int i8 = 0;
                    while (i7 <= i2) {
                        inc(bArrClone);
                        hash(sHA256Digest, bArrClone, bArr2);
                        BigInteger bigInteger3 = new BigInteger(i3, bArr2);
                        if (i7 == i2) {
                            bigInteger3 = bigInteger3.mod(ONE.shiftLeft(i4));
                        }
                        bigIntegerAdd = bigIntegerAdd.add(bigInteger3.shiftLeft(i8));
                        i7++;
                        i8 += digestSize;
                        i3 = 1;
                    }
                    BigInteger bigInteger4 = ONE;
                    BigInteger bigIntegerAdd2 = bigIntegerAdd.add(bigInteger4.shiftLeft(this.L - 1));
                    BigInteger bigIntegerSubtract2 = bigIntegerAdd2.subtract(bigIntegerAdd2.mod(bigIntegerSubtract.shiftLeft(1)).subtract(bigInteger4));
                    if (bigIntegerSubtract2.bitLength() == this.L && bigIntegerSubtract2.isProbablePrime(this.certainty)) {
                        return new DSAParameters(bigIntegerSubtract2, bigIntegerSubtract, calculateGenerator_FIPS186_3_Unverifiable(bigIntegerSubtract2, bigIntegerSubtract, this.random), new DSAValidationParameters(bArr, i6));
                    }
                    i6++;
                    i3 = 1;
                }
            }
        }
    }

    private static int getDefaultN(int i) {
        return i > 1024 ? 256 : 160;
    }

    private static void hash(Digest digest, byte[] bArr, byte[] bArr2) {
        digest.update(bArr, 0, bArr.length);
        digest.doFinal(bArr2, 0);
    }

    private static void inc(byte[] bArr) {
        for (int length = bArr.length - 1; length >= 0; length--) {
            byte b = (byte) ((bArr[length] + 1) & 255);
            bArr[length] = b;
            if (b != 0) {
                return;
            }
        }
    }

    private void init(int i, int i2, int i3, SecureRandom secureRandom) {
        this.L = i;
        this.N = i2;
        this.certainty = i3;
        this.random = secureRandom;
    }

    public DSAParameters generateParameters() {
        return this.L > 1024 ? generateParameters_FIPS186_3() : generateParameters_FIPS186_2();
    }

    public void init(int i, int i2, SecureRandom secureRandom) {
        init(i, getDefaultN(i), i2, secureRandom);
    }
}
