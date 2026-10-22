package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.util.BigIntegers;

/* JADX INFO: loaded from: classes2.dex */
class DHParametersHelper {
    private static final BigInteger ONE = BigInteger.valueOf(1);
    private static final BigInteger TWO = BigInteger.valueOf(2);

    DHParametersHelper() {
    }

    static BigInteger[] generateSafePrimes(int i, int i2, SecureRandom secureRandom) {
        BigInteger bigInteger;
        BigInteger bigIntegerAdd;
        int i3 = i - 1;
        while (true) {
            bigInteger = new BigInteger(i3, 2, secureRandom);
            bigIntegerAdd = bigInteger.shiftLeft(1).add(ONE);
            if (bigIntegerAdd.isProbablePrime(i2) && (i2 <= 2 || bigInteger.isProbablePrime(i2))) {
                break;
            }
        }
        return new BigInteger[]{bigIntegerAdd, bigInteger};
    }

    static BigInteger selectGenerator(BigInteger bigInteger, BigInteger bigInteger2, SecureRandom secureRandom) {
        BigInteger bigIntegerSubtract = bigInteger.subtract(TWO);
        while (true) {
            BigInteger bigInteger3 = TWO;
            BigInteger bigIntegerCreateRandomInRange = BigIntegers.createRandomInRange(bigInteger3, bigIntegerSubtract, secureRandom);
            BigInteger bigIntegerModPow = bigIntegerCreateRandomInRange.modPow(bigInteger3, bigInteger);
            BigInteger bigInteger4 = ONE;
            if (!bigIntegerModPow.equals(bigInteger4) && !bigIntegerCreateRandomInRange.modPow(bigInteger2, bigInteger).equals(bigInteger4)) {
                return bigIntegerCreateRandomInRange;
            }
        }
    }
}
