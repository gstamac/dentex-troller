package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;

/* JADX INFO: loaded from: classes2.dex */
public class RSAKeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private static final BigInteger ONE = BigInteger.valueOf(1);
    private RSAKeyGenerationParameters param;

    @Override // org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator
    public AsymmetricCipherKeyPair generateKeyPair() {
        BigInteger bigInteger;
        BigInteger bigInteger2;
        BigInteger bigInteger3;
        BigInteger bigIntegerMultiply;
        BigInteger bigInteger4;
        int strength = this.param.getStrength();
        int i = (strength + 1) / 2;
        int i2 = strength - i;
        int i3 = strength / 3;
        BigInteger publicExponent = this.param.getPublicExponent();
        while (true) {
            bigInteger = new BigInteger(i, 1, this.param.getRandom());
            BigInteger bigIntegerMod = bigInteger.mod(publicExponent);
            BigInteger bigInteger5 = ONE;
            if (!bigIntegerMod.equals(bigInteger5) && bigInteger.isProbablePrime(this.param.getCertainty()) && publicExponent.gcd(bigInteger.subtract(bigInteger5)).equals(bigInteger5)) {
                break;
            }
        }
        while (true) {
            bigInteger2 = new BigInteger(i2, 1, this.param.getRandom());
            if (bigInteger2.subtract(bigInteger).abs().bitLength() >= i3) {
                BigInteger bigIntegerMod2 = bigInteger2.mod(publicExponent);
                bigInteger3 = ONE;
                if (!bigIntegerMod2.equals(bigInteger3) && bigInteger2.isProbablePrime(this.param.getCertainty()) && publicExponent.gcd(bigInteger2.subtract(bigInteger3)).equals(bigInteger3)) {
                    bigIntegerMultiply = bigInteger.multiply(bigInteger2);
                    if (bigIntegerMultiply.bitLength() == this.param.getStrength()) {
                        break;
                    }
                    bigInteger = bigInteger.max(bigInteger2);
                }
            }
        }
        if (bigInteger.compareTo(bigInteger2) < 0) {
            bigInteger4 = bigInteger;
        } else {
            bigInteger4 = bigInteger2;
            bigInteger2 = bigInteger;
        }
        BigInteger bigIntegerSubtract = bigInteger2.subtract(bigInteger3);
        BigInteger bigIntegerSubtract2 = bigInteger4.subtract(bigInteger3);
        BigInteger bigIntegerModInverse = publicExponent.modInverse(bigIntegerSubtract.multiply(bigIntegerSubtract2));
        return new AsymmetricCipherKeyPair(new RSAKeyParameters(false, bigIntegerMultiply, publicExponent), new RSAPrivateCrtKeyParameters(bigIntegerMultiply, publicExponent, bigIntegerModInverse, bigInteger2, bigInteger4, bigIntegerModInverse.remainder(bigIntegerSubtract), bigIntegerModInverse.remainder(bigIntegerSubtract2), bigInteger4.modInverse(bigInteger2)));
    }

    @Override // org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator
    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.param = (RSAKeyGenerationParameters) keyGenerationParameters;
    }
}
