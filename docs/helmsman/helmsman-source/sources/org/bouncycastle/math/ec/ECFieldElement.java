package org.bouncycastle.math.ec;

import java.math.BigInteger;
import java.util.Random;

/* JADX INFO: loaded from: classes2.dex */
public abstract class ECFieldElement implements ECConstants {

    public static class F2m extends ECFieldElement {
        public static final int GNB = 1;
        public static final int PPB = 3;
        public static final int TPB = 2;
        private int k1;
        private int k2;
        private int k3;
        private int m;
        private int representation;
        private int t;
        private IntArray x;

        public F2m(int i, int i2, int i3, int i4, BigInteger bigInteger) {
            int i5;
            this.t = (i + 31) >> 5;
            this.x = new IntArray(bigInteger, this.t);
            if (i3 == 0 && i4 == 0) {
                i5 = 2;
            } else {
                if (i3 >= i4) {
                    throw new IllegalArgumentException("k2 must be smaller than k3");
                }
                if (i3 <= 0) {
                    throw new IllegalArgumentException("k2 must be larger than 0");
                }
                i5 = 3;
            }
            this.representation = i5;
            if (bigInteger.signum() < 0) {
                throw new IllegalArgumentException("x value cannot be negative");
            }
            this.m = i;
            this.k1 = i2;
            this.k2 = i3;
            this.k3 = i4;
        }

        private F2m(int i, int i2, int i3, int i4, IntArray intArray) {
            this.t = (i + 31) >> 5;
            this.x = intArray;
            this.m = i;
            this.k1 = i2;
            this.k2 = i3;
            this.k3 = i4;
            this.representation = (i3 == 0 && i4 == 0) ? 2 : 3;
        }

        public F2m(int i, int i2, BigInteger bigInteger) {
            this(i, i2, 0, 0, bigInteger);
        }

        public static void checkFieldElements(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            if (!(eCFieldElement instanceof F2m) || !(eCFieldElement2 instanceof F2m)) {
                throw new IllegalArgumentException("Field elements are not both instances of ECFieldElement.F2m");
            }
            F2m f2m = (F2m) eCFieldElement;
            F2m f2m2 = (F2m) eCFieldElement2;
            if (f2m.m != f2m2.m || f2m.k1 != f2m2.k1 || f2m.k2 != f2m2.k2 || f2m.k3 != f2m2.k3) {
                throw new IllegalArgumentException("Field elements are not elements of the same field F2m");
            }
            if (f2m.representation != f2m2.representation) {
                throw new IllegalArgumentException("One of the field elements are not elements has incorrect representation");
            }
        }

        @Override // org.bouncycastle.math.ec.ECFieldElement
        public ECFieldElement add(ECFieldElement eCFieldElement) {
            IntArray intArray = (IntArray) this.x.clone();
            intArray.addShifted(((F2m) eCFieldElement).x, 0);
            return new F2m(this.m, this.k1, this.k2, this.k3, intArray);
        }

        @Override // org.bouncycastle.math.ec.ECFieldElement
        public ECFieldElement divide(ECFieldElement eCFieldElement) {
            return multiply(eCFieldElement.invert());
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof F2m)) {
                return false;
            }
            F2m f2m = (F2m) obj;
            return this.m == f2m.m && this.k1 == f2m.k1 && this.k2 == f2m.k2 && this.k3 == f2m.k3 && this.representation == f2m.representation && this.x.equals(f2m.x);
        }

        @Override // org.bouncycastle.math.ec.ECFieldElement
        public String getFieldName() {
            return "F2m";
        }

        @Override // org.bouncycastle.math.ec.ECFieldElement
        public int getFieldSize() {
            return this.m;
        }

        public int getK1() {
            return this.k1;
        }

        public int getK2() {
            return this.k2;
        }

        public int getK3() {
            return this.k3;
        }

        public int getM() {
            return this.m;
        }

        public int getRepresentation() {
            return this.representation;
        }

        public int hashCode() {
            return (((this.x.hashCode() ^ this.m) ^ this.k1) ^ this.k2) ^ this.k3;
        }

        @Override // org.bouncycastle.math.ec.ECFieldElement
        public ECFieldElement invert() {
            IntArray intArray = (IntArray) this.x.clone();
            IntArray intArray2 = new IntArray(this.t);
            intArray2.setBit(this.m);
            intArray2.setBit(0);
            intArray2.setBit(this.k1);
            if (this.representation == 3) {
                intArray2.setBit(this.k2);
                intArray2.setBit(this.k3);
            }
            IntArray intArray3 = new IntArray(this.t);
            intArray3.setBit(0);
            IntArray intArray4 = new IntArray(this.t);
            while (!intArray.isZero()) {
                int iBitLength = intArray.bitLength() - intArray2.bitLength();
                if (iBitLength < 0) {
                    iBitLength = -iBitLength;
                    IntArray intArray5 = intArray2;
                    intArray2 = intArray;
                    intArray = intArray5;
                    IntArray intArray6 = intArray4;
                    intArray4 = intArray3;
                    intArray3 = intArray6;
                }
                int i = iBitLength >> 5;
                int i2 = iBitLength & 31;
                intArray.addShifted(intArray2.shiftLeft(i2), i);
                intArray3.addShifted(intArray4.shiftLeft(i2), i);
            }
            return new F2m(this.m, this.k1, this.k2, this.k3, intArray4);
        }

        @Override // org.bouncycastle.math.ec.ECFieldElement
        public ECFieldElement multiply(ECFieldElement eCFieldElement) {
            IntArray intArrayMultiply = this.x.multiply(((F2m) eCFieldElement).x, this.m);
            intArrayMultiply.reduce(this.m, new int[]{this.k1, this.k2, this.k3});
            return new F2m(this.m, this.k1, this.k2, this.k3, intArrayMultiply);
        }

        @Override // org.bouncycastle.math.ec.ECFieldElement
        public ECFieldElement negate() {
            return this;
        }

        @Override // org.bouncycastle.math.ec.ECFieldElement
        public ECFieldElement sqrt() {
            throw new RuntimeException("Not implemented");
        }

        @Override // org.bouncycastle.math.ec.ECFieldElement
        public ECFieldElement square() {
            IntArray intArraySquare = this.x.square(this.m);
            intArraySquare.reduce(this.m, new int[]{this.k1, this.k2, this.k3});
            return new F2m(this.m, this.k1, this.k2, this.k3, intArraySquare);
        }

        @Override // org.bouncycastle.math.ec.ECFieldElement
        public ECFieldElement subtract(ECFieldElement eCFieldElement) {
            return add(eCFieldElement);
        }

        @Override // org.bouncycastle.math.ec.ECFieldElement
        public BigInteger toBigInteger() {
            return this.x.toBigInteger();
        }
    }

    public static class Fp extends ECFieldElement {
        BigInteger q;
        BigInteger x;

        public Fp(BigInteger bigInteger, BigInteger bigInteger2) {
            this.x = bigInteger2;
            if (bigInteger2.compareTo(bigInteger) >= 0) {
                throw new IllegalArgumentException("x value too large in field element");
            }
            this.q = bigInteger;
        }

        private static BigInteger[] lucasSequence(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) {
            int iBitLength = bigInteger4.bitLength();
            int lowestSetBit = bigInteger4.getLowestSetBit();
            BigInteger bigIntegerMod = ECConstants.ONE;
            BigInteger bigIntegerMod2 = ECConstants.TWO;
            BigInteger bigIntegerMod3 = ECConstants.ONE;
            BigInteger bigIntegerMod4 = ECConstants.ONE;
            BigInteger bigIntegerMod5 = bigInteger2;
            for (int i = iBitLength - 1; i >= lowestSetBit + 1; i--) {
                bigIntegerMod3 = bigIntegerMod3.multiply(bigIntegerMod4).mod(bigInteger);
                if (bigInteger4.testBit(i)) {
                    bigIntegerMod4 = bigIntegerMod3.multiply(bigInteger3).mod(bigInteger);
                    bigIntegerMod = bigIntegerMod.multiply(bigIntegerMod5).mod(bigInteger);
                    bigIntegerMod2 = bigIntegerMod5.multiply(bigIntegerMod2).subtract(bigInteger2.multiply(bigIntegerMod3)).mod(bigInteger);
                    bigIntegerMod5 = bigIntegerMod5.multiply(bigIntegerMod5).subtract(bigIntegerMod4.shiftLeft(1)).mod(bigInteger);
                } else {
                    bigIntegerMod = bigIntegerMod.multiply(bigIntegerMod2).subtract(bigIntegerMod3).mod(bigInteger);
                    BigInteger bigIntegerMod6 = bigIntegerMod5.multiply(bigIntegerMod2).subtract(bigInteger2.multiply(bigIntegerMod3)).mod(bigInteger);
                    bigIntegerMod2 = bigIntegerMod2.multiply(bigIntegerMod2).subtract(bigIntegerMod3.shiftLeft(1)).mod(bigInteger);
                    bigIntegerMod5 = bigIntegerMod6;
                    bigIntegerMod4 = bigIntegerMod3;
                }
            }
            BigInteger bigIntegerMod7 = bigIntegerMod3.multiply(bigIntegerMod4).mod(bigInteger);
            BigInteger bigIntegerMod8 = bigIntegerMod7.multiply(bigInteger3).mod(bigInteger);
            BigInteger bigIntegerMod9 = bigIntegerMod.multiply(bigIntegerMod2).subtract(bigIntegerMod7).mod(bigInteger);
            BigInteger bigIntegerMod10 = bigIntegerMod5.multiply(bigIntegerMod2).subtract(bigInteger2.multiply(bigIntegerMod7)).mod(bigInteger);
            BigInteger bigIntegerMod11 = bigIntegerMod7.multiply(bigIntegerMod8).mod(bigInteger);
            for (int i2 = 1; i2 <= lowestSetBit; i2++) {
                bigIntegerMod9 = bigIntegerMod9.multiply(bigIntegerMod10).mod(bigInteger);
                bigIntegerMod10 = bigIntegerMod10.multiply(bigIntegerMod10).subtract(bigIntegerMod11.shiftLeft(1)).mod(bigInteger);
                bigIntegerMod11 = bigIntegerMod11.multiply(bigIntegerMod11).mod(bigInteger);
            }
            return new BigInteger[]{bigIntegerMod9, bigIntegerMod10};
        }

        @Override // org.bouncycastle.math.ec.ECFieldElement
        public ECFieldElement add(ECFieldElement eCFieldElement) {
            return new Fp(this.q, this.x.add(eCFieldElement.toBigInteger()).mod(this.q));
        }

        @Override // org.bouncycastle.math.ec.ECFieldElement
        public ECFieldElement divide(ECFieldElement eCFieldElement) {
            return new Fp(this.q, this.x.multiply(eCFieldElement.toBigInteger().modInverse(this.q)).mod(this.q));
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Fp)) {
                return false;
            }
            Fp fp = (Fp) obj;
            return this.q.equals(fp.q) && this.x.equals(fp.x);
        }

        @Override // org.bouncycastle.math.ec.ECFieldElement
        public String getFieldName() {
            return "Fp";
        }

        @Override // org.bouncycastle.math.ec.ECFieldElement
        public int getFieldSize() {
            return this.q.bitLength();
        }

        public BigInteger getQ() {
            return this.q;
        }

        public int hashCode() {
            return this.q.hashCode() ^ this.x.hashCode();
        }

        @Override // org.bouncycastle.math.ec.ECFieldElement
        public ECFieldElement invert() {
            BigInteger bigInteger = this.q;
            return new Fp(bigInteger, this.x.modInverse(bigInteger));
        }

        @Override // org.bouncycastle.math.ec.ECFieldElement
        public ECFieldElement multiply(ECFieldElement eCFieldElement) {
            return new Fp(this.q, this.x.multiply(eCFieldElement.toBigInteger()).mod(this.q));
        }

        @Override // org.bouncycastle.math.ec.ECFieldElement
        public ECFieldElement negate() {
            return new Fp(this.q, this.x.negate().mod(this.q));
        }

        @Override // org.bouncycastle.math.ec.ECFieldElement
        public ECFieldElement sqrt() {
            if (!this.q.testBit(0)) {
                throw new RuntimeException("not done yet");
            }
            if (this.q.testBit(1)) {
                BigInteger bigInteger = this.q;
                Fp fp = new Fp(bigInteger, this.x.modPow(bigInteger.shiftRight(2).add(ECConstants.ONE), this.q));
                if (fp.square().equals(this)) {
                    return fp;
                }
                return null;
            }
            BigInteger bigIntegerSubtract = this.q.subtract(ECConstants.ONE);
            BigInteger bigIntegerShiftRight = bigIntegerSubtract.shiftRight(1);
            if (!this.x.modPow(bigIntegerShiftRight, this.q).equals(ECConstants.ONE)) {
                return null;
            }
            BigInteger bigIntegerAdd = bigIntegerSubtract.shiftRight(2).shiftLeft(1).add(ECConstants.ONE);
            BigInteger bigInteger2 = this.x;
            BigInteger bigIntegerMod = bigInteger2.shiftLeft(2).mod(this.q);
            Random random = new Random();
            while (true) {
                BigInteger bigInteger3 = new BigInteger(this.q.bitLength(), random);
                if (bigInteger3.compareTo(this.q) < 0 && bigInteger3.multiply(bigInteger3).subtract(bigIntegerMod).modPow(bigIntegerShiftRight, this.q).equals(bigIntegerSubtract)) {
                    BigInteger[] bigIntegerArrLucasSequence = lucasSequence(this.q, bigInteger3, bigInteger2, bigIntegerAdd);
                    BigInteger bigInteger4 = bigIntegerArrLucasSequence[0];
                    BigInteger bigIntegerAdd2 = bigIntegerArrLucasSequence[1];
                    if (bigIntegerAdd2.multiply(bigIntegerAdd2).mod(this.q).equals(bigIntegerMod)) {
                        if (bigIntegerAdd2.testBit(0)) {
                            bigIntegerAdd2 = bigIntegerAdd2.add(this.q);
                        }
                        return new Fp(this.q, bigIntegerAdd2.shiftRight(1));
                    }
                    if (!bigInteger4.equals(ECConstants.ONE) && !bigInteger4.equals(bigIntegerSubtract)) {
                        return null;
                    }
                }
            }
        }

        @Override // org.bouncycastle.math.ec.ECFieldElement
        public ECFieldElement square() {
            BigInteger bigInteger = this.q;
            BigInteger bigInteger2 = this.x;
            return new Fp(bigInteger, bigInteger2.multiply(bigInteger2).mod(this.q));
        }

        @Override // org.bouncycastle.math.ec.ECFieldElement
        public ECFieldElement subtract(ECFieldElement eCFieldElement) {
            return new Fp(this.q, this.x.subtract(eCFieldElement.toBigInteger()).mod(this.q));
        }

        @Override // org.bouncycastle.math.ec.ECFieldElement
        public BigInteger toBigInteger() {
            return this.x;
        }
    }

    public abstract ECFieldElement add(ECFieldElement eCFieldElement);

    public abstract ECFieldElement divide(ECFieldElement eCFieldElement);

    public abstract String getFieldName();

    public abstract int getFieldSize();

    public abstract ECFieldElement invert();

    public abstract ECFieldElement multiply(ECFieldElement eCFieldElement);

    public abstract ECFieldElement negate();

    public abstract ECFieldElement sqrt();

    public abstract ECFieldElement square();

    public abstract ECFieldElement subtract(ECFieldElement eCFieldElement);

    public abstract BigInteger toBigInteger();

    public String toString() {
        return toBigInteger().toString(2);
    }
}
