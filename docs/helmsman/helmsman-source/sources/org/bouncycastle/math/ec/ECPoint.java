package org.bouncycastle.math.ec;

import java.math.BigInteger;
import org.bouncycastle.asn1.x9.X9IntegerConverter;

/* JADX INFO: loaded from: classes2.dex */
public abstract class ECPoint {
    private static X9IntegerConverter converter = new X9IntegerConverter();
    ECCurve curve;
    protected ECMultiplier multiplier = null;
    protected PreCompInfo preCompInfo = null;
    protected boolean withCompression;
    ECFieldElement x;
    ECFieldElement y;

    public static class F2m extends ECPoint {
        public F2m(ECCurve eCCurve) {
            super(eCCurve, null, null);
        }

        public F2m(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            this(eCCurve, eCFieldElement, eCFieldElement2, false);
        }

        public F2m(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean z) {
            super(eCCurve, eCFieldElement, eCFieldElement2);
            if ((eCFieldElement != null && eCFieldElement2 == null) || (eCFieldElement == null && eCFieldElement2 != null)) {
                throw new IllegalArgumentException("Exactly one of the field elements is null");
            }
            if (eCFieldElement != null) {
                ECFieldElement.F2m.checkFieldElements(this.x, this.y);
                if (eCCurve != null) {
                    ECFieldElement.F2m.checkFieldElements(this.x, this.curve.getA());
                }
            }
            this.withCompression = z;
        }

        private static void checkPoints(ECPoint eCPoint, ECPoint eCPoint2) {
            if (!eCPoint.curve.equals(eCPoint2.curve)) {
                throw new IllegalArgumentException("Only points on the same curve can be added or subtracted");
            }
        }

        @Override // org.bouncycastle.math.ec.ECPoint
        public ECPoint add(ECPoint eCPoint) {
            checkPoints(this, eCPoint);
            return addSimple((F2m) eCPoint);
        }

        public F2m addSimple(F2m f2m) {
            if (isInfinity()) {
                return f2m;
            }
            if (f2m.isInfinity()) {
                return this;
            }
            ECFieldElement.F2m f2m2 = (ECFieldElement.F2m) f2m.getX();
            ECFieldElement.F2m f2m3 = (ECFieldElement.F2m) f2m.getY();
            if (this.x.equals(f2m2)) {
                return (F2m) (this.y.equals(f2m3) ? twice() : this.curve.getInfinity());
            }
            ECFieldElement eCFieldElement = (ECFieldElement.F2m) this.y.add(f2m3).divide(this.x.add(f2m2));
            ECFieldElement.F2m f2m4 = (ECFieldElement.F2m) eCFieldElement.square().add(eCFieldElement).add(this.x).add(f2m2).add(this.curve.getA());
            return new F2m(this.curve, f2m4, (ECFieldElement.F2m) eCFieldElement.multiply(this.x.add(f2m4)).add(f2m4).add(this.y), this.withCompression);
        }

        @Override // org.bouncycastle.math.ec.ECPoint
        public byte[] getEncoded() {
            if (isInfinity()) {
                return new byte[1];
            }
            int byteLength = ECPoint.converter.getByteLength(this.x);
            byte[] bArrIntegerToBytes = ECPoint.converter.integerToBytes(getX().toBigInteger(), byteLength);
            if (!this.withCompression) {
                byte[] bArrIntegerToBytes2 = ECPoint.converter.integerToBytes(getY().toBigInteger(), byteLength);
                byte[] bArr = new byte[byteLength + byteLength + 1];
                bArr[0] = 4;
                System.arraycopy(bArrIntegerToBytes, 0, bArr, 1, byteLength);
                System.arraycopy(bArrIntegerToBytes2, 0, bArr, byteLength + 1, byteLength);
                return bArr;
            }
            byte[] bArr2 = new byte[byteLength + 1];
            bArr2[0] = 2;
            if (!getX().toBigInteger().equals(ECConstants.ZERO) && getY().multiply(getX().invert()).toBigInteger().testBit(0)) {
                bArr2[0] = 3;
            }
            System.arraycopy(bArrIntegerToBytes, 0, bArr2, 1, byteLength);
            return bArr2;
        }

        @Override // org.bouncycastle.math.ec.ECPoint
        public ECPoint negate() {
            return new F2m(this.curve, getX(), getY().add(getX()), this.withCompression);
        }

        @Override // org.bouncycastle.math.ec.ECPoint
        public ECPoint subtract(ECPoint eCPoint) {
            checkPoints(this, eCPoint);
            return subtractSimple((F2m) eCPoint);
        }

        public F2m subtractSimple(F2m f2m) {
            return f2m.isInfinity() ? this : addSimple((F2m) f2m.negate());
        }

        @Override // org.bouncycastle.math.ec.ECPoint
        public ECPoint twice() {
            if (isInfinity()) {
                return this;
            }
            if (this.x.toBigInteger().signum() == 0) {
                return this.curve.getInfinity();
            }
            ECFieldElement eCFieldElement = (ECFieldElement.F2m) this.x.add(this.y.divide(this.x));
            ECFieldElement.F2m f2m = (ECFieldElement.F2m) eCFieldElement.square().add(eCFieldElement).add(this.curve.getA());
            return new F2m(this.curve, f2m, (ECFieldElement.F2m) this.x.square().add(f2m.multiply(eCFieldElement.add(this.curve.fromBigInteger(ECConstants.ONE)))), this.withCompression);
        }
    }

    public static class Fp extends ECPoint {
        public Fp(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            this(eCCurve, eCFieldElement, eCFieldElement2, false);
        }

        public Fp(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean z) {
            super(eCCurve, eCFieldElement, eCFieldElement2);
            if ((eCFieldElement != null && eCFieldElement2 == null) || (eCFieldElement == null && eCFieldElement2 != null)) {
                throw new IllegalArgumentException("Exactly one of the field elements is null");
            }
            this.withCompression = z;
        }

        @Override // org.bouncycastle.math.ec.ECPoint
        public ECPoint add(ECPoint eCPoint) {
            if (isInfinity()) {
                return eCPoint;
            }
            if (eCPoint.isInfinity()) {
                return this;
            }
            if (this.x.equals(eCPoint.x)) {
                return this.y.equals(eCPoint.y) ? twice() : this.curve.getInfinity();
            }
            ECFieldElement eCFieldElementDivide = eCPoint.y.subtract(this.y).divide(eCPoint.x.subtract(this.x));
            ECFieldElement eCFieldElementSubtract = eCFieldElementDivide.square().subtract(this.x).subtract(eCPoint.x);
            return new Fp(this.curve, eCFieldElementSubtract, eCFieldElementDivide.multiply(this.x.subtract(eCFieldElementSubtract)).subtract(this.y));
        }

        @Override // org.bouncycastle.math.ec.ECPoint
        public byte[] getEncoded() {
            if (isInfinity()) {
                return new byte[1];
            }
            int byteLength = ECPoint.converter.getByteLength(this.x);
            if (this.withCompression) {
                byte b = getY().toBigInteger().testBit(0) ? (byte) 3 : (byte) 2;
                byte[] bArrIntegerToBytes = ECPoint.converter.integerToBytes(getX().toBigInteger(), byteLength);
                byte[] bArr = new byte[bArrIntegerToBytes.length + 1];
                bArr[0] = b;
                System.arraycopy(bArrIntegerToBytes, 0, bArr, 1, bArrIntegerToBytes.length);
                return bArr;
            }
            byte[] bArrIntegerToBytes2 = ECPoint.converter.integerToBytes(getX().toBigInteger(), byteLength);
            byte[] bArrIntegerToBytes3 = ECPoint.converter.integerToBytes(getY().toBigInteger(), byteLength);
            byte[] bArr2 = new byte[bArrIntegerToBytes2.length + bArrIntegerToBytes3.length + 1];
            bArr2[0] = 4;
            System.arraycopy(bArrIntegerToBytes2, 0, bArr2, 1, bArrIntegerToBytes2.length);
            System.arraycopy(bArrIntegerToBytes3, 0, bArr2, bArrIntegerToBytes2.length + 1, bArrIntegerToBytes3.length);
            return bArr2;
        }

        @Override // org.bouncycastle.math.ec.ECPoint
        public ECPoint negate() {
            return new Fp(this.curve, this.x, this.y.negate(), this.withCompression);
        }

        @Override // org.bouncycastle.math.ec.ECPoint
        public ECPoint subtract(ECPoint eCPoint) {
            return eCPoint.isInfinity() ? this : add(eCPoint.negate());
        }

        @Override // org.bouncycastle.math.ec.ECPoint
        public ECPoint twice() {
            if (isInfinity()) {
                return this;
            }
            if (this.y.toBigInteger().signum() == 0) {
                return this.curve.getInfinity();
            }
            ECFieldElement eCFieldElementFromBigInteger = this.curve.fromBigInteger(BigInteger.valueOf(2L));
            ECFieldElement eCFieldElementDivide = this.x.square().multiply(this.curve.fromBigInteger(BigInteger.valueOf(3L))).add(this.curve.a).divide(this.y.multiply(eCFieldElementFromBigInteger));
            ECFieldElement eCFieldElementSubtract = eCFieldElementDivide.square().subtract(this.x.multiply(eCFieldElementFromBigInteger));
            return new Fp(this.curve, eCFieldElementSubtract, eCFieldElementDivide.multiply(this.x.subtract(eCFieldElementSubtract)).subtract(this.y), this.withCompression);
        }
    }

    protected ECPoint(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        this.curve = eCCurve;
        this.x = eCFieldElement;
        this.y = eCFieldElement2;
    }

    public abstract ECPoint add(ECPoint eCPoint);

    synchronized void assertECMultiplier() {
        if (this.multiplier == null) {
            this.multiplier = new FpNafMultiplier();
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ECPoint)) {
            return false;
        }
        ECPoint eCPoint = (ECPoint) obj;
        if (isInfinity()) {
            return eCPoint.isInfinity();
        }
        return this.x.equals(eCPoint.x) && this.y.equals(eCPoint.y);
    }

    public ECCurve getCurve() {
        return this.curve;
    }

    public abstract byte[] getEncoded();

    public ECFieldElement getX() {
        return this.x;
    }

    public ECFieldElement getY() {
        return this.y;
    }

    public int hashCode() {
        if (isInfinity()) {
            return 0;
        }
        return this.x.hashCode() ^ this.y.hashCode();
    }

    public boolean isCompressed() {
        return this.withCompression;
    }

    public boolean isInfinity() {
        return this.x == null && this.y == null;
    }

    public ECPoint multiply(BigInteger bigInteger) {
        if (isInfinity()) {
            return this;
        }
        if (bigInteger.signum() == 0) {
            return this.curve.getInfinity();
        }
        assertECMultiplier();
        return this.multiplier.multiply(this, bigInteger, this.preCompInfo);
    }

    public abstract ECPoint negate();

    void setPreCompInfo(PreCompInfo preCompInfo) {
        this.preCompInfo = preCompInfo;
    }

    public abstract ECPoint subtract(ECPoint eCPoint);

    public abstract ECPoint twice();
}
