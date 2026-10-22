package org.bouncycastle.crypto.signers;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DSA;
import org.bouncycastle.crypto.params.ECKeyParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECConstants;

/* JADX INFO: loaded from: classes2.dex */
public class ECDSASigner implements ECConstants, DSA {
    ECKeyParameters key;
    SecureRandom random;

    private BigInteger calculateE(BigInteger bigInteger, byte[] bArr) {
        if (bigInteger.bitLength() > bArr.length * 8) {
            return new BigInteger(1, bArr);
        }
        int length = bArr.length * 8;
        BigInteger bigInteger2 = new BigInteger(1, bArr);
        return length - bigInteger.bitLength() > 0 ? bigInteger2.shiftRight(length - bigInteger.bitLength()) : bigInteger2;
    }

    @Override // org.bouncycastle.crypto.DSA
    public BigInteger[] generateSignature(byte[] bArr) {
        BigInteger bigInteger;
        BigInteger bigIntegerMod;
        BigInteger bigIntegerMod2;
        BigInteger n = this.key.getParameters().getN();
        BigInteger bigIntegerCalculateE = calculateE(n, bArr);
        do {
            int iBitLength = n.bitLength();
            while (true) {
                bigInteger = new BigInteger(iBitLength, this.random);
                if (!bigInteger.equals(ZERO)) {
                    bigIntegerMod = this.key.getParameters().getG().multiply(bigInteger).getX().toBigInteger().mod(n);
                    if (!bigIntegerMod.equals(ZERO)) {
                        break;
                    }
                }
            }
            bigIntegerMod2 = bigInteger.modInverse(n).multiply(bigIntegerCalculateE.add(((ECPrivateKeyParameters) this.key).getD().multiply(bigIntegerMod))).mod(n);
        } while (bigIntegerMod2.equals(ZERO));
        return new BigInteger[]{bigIntegerMod, bigIntegerMod2};
    }

    @Override // org.bouncycastle.crypto.DSA
    public void init(boolean z, CipherParameters cipherParameters) {
        ECKeyParameters eCKeyParameters;
        if (!z) {
            eCKeyParameters = (ECPublicKeyParameters) cipherParameters;
        } else {
            if (cipherParameters instanceof ParametersWithRandom) {
                ParametersWithRandom parametersWithRandom = (ParametersWithRandom) cipherParameters;
                this.random = parametersWithRandom.getRandom();
                this.key = (ECPrivateKeyParameters) parametersWithRandom.getParameters();
                return;
            }
            this.random = new SecureRandom();
            eCKeyParameters = (ECPrivateKeyParameters) cipherParameters;
        }
        this.key = eCKeyParameters;
    }

    @Override // org.bouncycastle.crypto.DSA
    public boolean verifySignature(byte[] bArr, BigInteger bigInteger, BigInteger bigInteger2) {
        BigInteger n = this.key.getParameters().getN();
        BigInteger bigIntegerCalculateE = calculateE(n, bArr);
        if (bigInteger.compareTo(ONE) < 0 || bigInteger.compareTo(n) >= 0 || bigInteger2.compareTo(ONE) < 0 || bigInteger2.compareTo(n) >= 0) {
            return false;
        }
        BigInteger bigIntegerModInverse = bigInteger2.modInverse(n);
        return ECAlgorithms.sumOfTwoMultiplies(this.key.getParameters().getG(), bigIntegerCalculateE.multiply(bigIntegerModInverse).mod(n), ((ECPublicKeyParameters) this.key).getQ(), bigInteger.multiply(bigIntegerModInverse).mod(n)).getX().toBigInteger().mod(n).equals(bigInteger);
    }
}
