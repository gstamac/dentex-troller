package org.bouncycastle.math.ec;

import java.math.BigInteger;

/* JADX INFO: loaded from: classes2.dex */
class WNafMultiplier implements ECMultiplier {
    WNafMultiplier() {
    }

    @Override // org.bouncycastle.math.ec.ECMultiplier
    public ECPoint multiply(ECPoint eCPoint, BigInteger bigInteger, PreCompInfo preCompInfo) {
        byte b;
        int i;
        byte b2;
        int i2;
        int length;
        WNafPreCompInfo wNafPreCompInfo = (preCompInfo == null || !(preCompInfo instanceof WNafPreCompInfo)) ? new WNafPreCompInfo() : (WNafPreCompInfo) preCompInfo;
        int iBitLength = bigInteger.bitLength();
        if (iBitLength < 13) {
            b = 2;
            i = 1;
        } else if (iBitLength < 41) {
            b = 3;
            i = 2;
        } else if (iBitLength < 121) {
            i = 4;
            b = 4;
        } else {
            b = 8;
            if (iBitLength < 337) {
                b = 5;
                i = 8;
            } else {
                if (iBitLength < 897) {
                    b2 = 6;
                    i2 = 16;
                } else if (iBitLength < 2305) {
                    b2 = 7;
                    i2 = 32;
                } else {
                    i = 127;
                }
                b = b2;
                i = i2;
            }
        }
        ECPoint[] preComp = wNafPreCompInfo.getPreComp();
        ECPoint twiceP = wNafPreCompInfo.getTwiceP();
        if (preComp == null) {
            preComp = new ECPoint[]{eCPoint};
            length = 1;
        } else {
            length = preComp.length;
        }
        if (twiceP == null) {
            twiceP = eCPoint.twice();
        }
        if (length < i) {
            ECPoint[] eCPointArr = new ECPoint[i];
            System.arraycopy(preComp, 0, eCPointArr, 0, length);
            while (length < i) {
                eCPointArr[length] = twiceP.add(eCPointArr[length - 1]);
                length++;
            }
            preComp = eCPointArr;
        }
        byte[] bArrWindowNaf = windowNaf(b, bigInteger);
        int length2 = bArrWindowNaf.length;
        ECPoint infinity = eCPoint.getCurve().getInfinity();
        for (int i3 = length2 - 1; i3 >= 0; i3--) {
            infinity = infinity.twice();
            byte b3 = bArrWindowNaf[i3];
            if (b3 != 0) {
                infinity = b3 > 0 ? infinity.add(preComp[(b3 - 1) / 2]) : infinity.subtract(preComp[((-b3) - 1) / 2]);
            }
        }
        wNafPreCompInfo.setPreComp(preComp);
        wNafPreCompInfo.setTwiceP(twiceP);
        eCPoint.setPreCompInfo(wNafPreCompInfo);
        return infinity;
    }

    public byte[] windowNaf(byte b, BigInteger bigInteger) {
        byte[] bArr = new byte[bigInteger.bitLength() + 1];
        short s = (short) (1 << b);
        BigInteger bigIntegerValueOf = BigInteger.valueOf(s);
        int i = 0;
        int i2 = 0;
        while (bigInteger.signum() > 0) {
            if (bigInteger.testBit(0)) {
                BigInteger bigIntegerMod = bigInteger.mod(bigIntegerValueOf);
                boolean zTestBit = bigIntegerMod.testBit(b - 1);
                int iIntValue = bigIntegerMod.intValue();
                if (zTestBit) {
                    bArr[i2] = (byte) (iIntValue - s);
                } else {
                    bArr[i2] = (byte) iIntValue;
                }
                bigInteger = bigInteger.subtract(BigInteger.valueOf(bArr[i2]));
                i = i2;
            } else {
                bArr[i2] = 0;
            }
            bigInteger = bigInteger.shiftRight(1);
            i2++;
        }
        int i3 = i + 1;
        byte[] bArr2 = new byte[i3];
        System.arraycopy(bArr, 0, bArr2, 0, i3);
        return bArr2;
    }
}
