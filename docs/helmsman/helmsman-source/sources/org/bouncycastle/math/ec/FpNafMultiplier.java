package org.bouncycastle.math.ec;

import java.math.BigInteger;

/* JADX INFO: loaded from: classes2.dex */
class FpNafMultiplier implements ECMultiplier {
    FpNafMultiplier() {
    }

    @Override // org.bouncycastle.math.ec.ECMultiplier
    public ECPoint multiply(ECPoint eCPoint, BigInteger bigInteger, PreCompInfo preCompInfo) {
        BigInteger bigIntegerMultiply = bigInteger.multiply(BigInteger.valueOf(3L));
        ECPoint eCPointNegate = eCPoint.negate();
        ECPoint eCPointTwice = eCPoint;
        for (int iBitLength = bigIntegerMultiply.bitLength() - 2; iBitLength > 0; iBitLength--) {
            eCPointTwice = eCPointTwice.twice();
            boolean zTestBit = bigIntegerMultiply.testBit(iBitLength);
            if (zTestBit != bigInteger.testBit(iBitLength)) {
                eCPointTwice = eCPointTwice.add(zTestBit ? eCPoint : eCPointNegate);
            }
        }
        return eCPointTwice;
    }
}
