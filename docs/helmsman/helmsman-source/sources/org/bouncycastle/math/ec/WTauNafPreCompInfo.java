package org.bouncycastle.math.ec;

/* JADX INFO: loaded from: classes2.dex */
class WTauNafPreCompInfo implements PreCompInfo {
    private ECPoint.F2m[] preComp;

    WTauNafPreCompInfo(ECPoint.F2m[] f2mArr) {
        this.preComp = f2mArr;
    }

    protected ECPoint.F2m[] getPreComp() {
        return this.preComp;
    }
}
