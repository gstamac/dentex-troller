package org.bouncycastle.math.ec;

import java.math.BigInteger;

/* JADX INFO: loaded from: classes2.dex */
class WTauNafMultiplier implements ECMultiplier {
    WTauNafMultiplier() {
    }

    private static ECPoint.F2m multiplyFromWTnaf(ECPoint.F2m f2m, byte[] bArr, PreCompInfo preCompInfo) {
        ECPoint.F2m[] preComp;
        byte bByteValue = ((ECCurve.F2m) f2m.getCurve()).getA().toBigInteger().byteValue();
        if (preCompInfo == null || !(preCompInfo instanceof WTauNafPreCompInfo)) {
            preComp = Tnaf.getPreComp(f2m, bByteValue);
            f2m.setPreCompInfo(new WTauNafPreCompInfo(preComp));
        } else {
            preComp = ((WTauNafPreCompInfo) preCompInfo).getPreComp();
        }
        ECPoint.F2m f2mTau = (ECPoint.F2m) f2m.getCurve().getInfinity();
        for (int length = bArr.length - 1; length >= 0; length--) {
            f2mTau = Tnaf.tau(f2mTau);
            byte b = bArr[length];
            if (b != 0) {
                f2mTau = b > 0 ? f2mTau.addSimple(preComp[b]) : f2mTau.subtractSimple(preComp[-b]);
            }
        }
        return f2mTau;
    }

    private ECPoint.F2m multiplyWTnaf(ECPoint.F2m f2m, ZTauElement zTauElement, PreCompInfo preCompInfo, byte b, byte b2) {
        ZTauElement[] zTauElementArr = b == 0 ? Tnaf.alpha0 : Tnaf.alpha1;
        return multiplyFromWTnaf(f2m, Tnaf.tauAdicWNaf(b2, zTauElement, (byte) 4, BigInteger.valueOf(16L), Tnaf.getTw(b2, 4), zTauElementArr), preCompInfo);
    }

    @Override // org.bouncycastle.math.ec.ECMultiplier
    public ECPoint multiply(ECPoint eCPoint, BigInteger bigInteger, PreCompInfo preCompInfo) {
        if (!(eCPoint instanceof ECPoint.F2m)) {
            throw new IllegalArgumentException("Only ECPoint.F2m can be used in WTauNafMultiplier");
        }
        ECPoint.F2m f2m = (ECPoint.F2m) eCPoint;
        ECCurve.F2m f2m2 = (ECCurve.F2m) f2m.getCurve();
        int m = f2m2.getM();
        byte bByteValue = f2m2.getA().toBigInteger().byteValue();
        byte mu = f2m2.getMu();
        return multiplyWTnaf(f2m, Tnaf.partModReduction(bigInteger, m, bByteValue, f2m2.getSi(), mu, (byte) 10), preCompInfo, bByteValue, mu);
    }
}
