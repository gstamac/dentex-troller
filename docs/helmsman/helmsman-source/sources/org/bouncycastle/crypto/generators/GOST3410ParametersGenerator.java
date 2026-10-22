package org.bouncycastle.crypto.generators;

import com.autonavi.ae.gmap.utils.GLMapStaticValue;
import com.facebook.appevents.AppEventsConstants;
import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.params.GOST3410Parameters;
import org.bouncycastle.crypto.params.GOST3410ValidationParameters;

/* JADX INFO: loaded from: classes2.dex */
public class GOST3410ParametersGenerator {
    private static final BigInteger ONE = BigInteger.valueOf(1);
    private static final BigInteger TWO = BigInteger.valueOf(2);
    private SecureRandom init_random;
    private int size;
    private int typeproc;

    private int procedure_A(int i, int i2, BigInteger[] bigIntegerArr, int i3) {
        BigInteger bigInteger;
        BigInteger[] bigIntegerArr2;
        BigInteger bigInteger2;
        BigInteger bigInteger3;
        int i4;
        int iNextInt = i;
        while (true) {
            if (iNextInt >= 0 && iNextInt <= 65536) {
                break;
            }
            iNextInt = this.init_random.nextInt() / 32768;
        }
        int iNextInt2 = i2;
        while (true) {
            if (iNextInt2 >= 0 && iNextInt2 <= 65536 && iNextInt2 / 2 != 0) {
                break;
            }
            iNextInt2 = (this.init_random.nextInt() / 32768) + 1;
        }
        BigInteger bigInteger4 = new BigInteger(Integer.toString(iNextInt2));
        BigInteger bigInteger5 = new BigInteger("19381");
        BigInteger bigInteger6 = new BigInteger(Integer.toString(iNextInt));
        int i5 = 0;
        BigInteger[] bigIntegerArr3 = {bigInteger6};
        int[] iArr = {i3};
        int i6 = 0;
        int i7 = 0;
        while (iArr[i6] >= 17) {
            int length = iArr.length + 1;
            int[] iArr2 = new int[length];
            System.arraycopy(iArr, 0, iArr2, 0, iArr.length);
            iArr = new int[length];
            System.arraycopy(iArr2, 0, iArr, 0, length);
            i7 = i6 + 1;
            iArr[i7] = iArr[i6] / 2;
            i6 = i7;
        }
        BigInteger[] bigIntegerArr4 = new BigInteger[i7 + 1];
        int i8 = 16;
        bigIntegerArr4[i7] = new BigInteger("8003", 16);
        int i9 = i7 - 1;
        int i10 = 0;
        while (i10 < i7) {
            int i11 = iArr[i9] / i8;
            while (true) {
                int length2 = bigIntegerArr3.length;
                BigInteger[] bigIntegerArr5 = new BigInteger[length2];
                System.arraycopy(bigIntegerArr3, i5, bigIntegerArr5, i5, bigIntegerArr3.length);
                bigIntegerArr2 = new BigInteger[i11 + 1];
                System.arraycopy(bigIntegerArr5, i5, bigIntegerArr2, i5, length2);
                int i12 = i5;
                while (i12 < i11) {
                    int i13 = i12 + 1;
                    bigIntegerArr2[i13] = bigIntegerArr2[i12].multiply(bigInteger5).add(bigInteger4).mod(TWO.pow(i8));
                    i12 = i13;
                }
                BigInteger bigInteger7 = new BigInteger(AppEventsConstants.EVENT_PARAM_VALUE_NO);
                for (int i14 = i5; i14 < i11; i14++) {
                    bigInteger7 = bigInteger7.add(bigIntegerArr2[i14].multiply(TWO.pow(i14 * 16)));
                }
                bigIntegerArr2[i5] = bigIntegerArr2[i11];
                BigInteger bigInteger8 = TWO;
                int i15 = i9 + 1;
                BigInteger bigIntegerAdd = bigInteger8.pow(iArr[i9] - 1).divide(bigIntegerArr4[i15]).add(bigInteger8.pow(iArr[i9] - 1).multiply(bigInteger7).divide(bigIntegerArr4[i15].multiply(bigInteger8.pow(i11 * 16))));
                BigInteger bigIntegerMod = bigIntegerAdd.mod(bigInteger8);
                BigInteger bigInteger9 = ONE;
                if (bigIntegerMod.compareTo(bigInteger9) == 0) {
                    bigIntegerAdd = bigIntegerAdd.add(bigInteger9);
                }
                int i16 = 0;
                while (true) {
                    bigInteger2 = bigInteger4;
                    bigInteger3 = bigInteger5;
                    long j = i16;
                    i4 = i7;
                    BigInteger bigIntegerMultiply = bigIntegerArr4[i15].multiply(bigIntegerAdd.add(BigInteger.valueOf(j)));
                    BigInteger bigInteger10 = ONE;
                    BigInteger bigIntegerAdd2 = bigIntegerMultiply.add(bigInteger10);
                    bigIntegerArr4[i9] = bigIntegerAdd2;
                    BigInteger bigInteger11 = TWO;
                    int i17 = i11;
                    if (bigIntegerAdd2.compareTo(bigInteger11.pow(iArr[i9])) != 1) {
                        if (bigInteger11.modPow(bigIntegerArr4[i15].multiply(bigIntegerAdd.add(BigInteger.valueOf(j))), bigIntegerArr4[i9]).compareTo(bigInteger10) == 0 && bigInteger11.modPow(bigIntegerAdd.add(BigInteger.valueOf(j)), bigIntegerArr4[i9]).compareTo(bigInteger10) != 0) {
                            break;
                        }
                        i16 += 2;
                        i7 = i4;
                        bigInteger5 = bigInteger3;
                        bigInteger4 = bigInteger2;
                        i11 = i17;
                    } else {
                        i7 = i4;
                        bigInteger5 = bigInteger3;
                        bigIntegerArr3 = bigIntegerArr2;
                        bigInteger4 = bigInteger2;
                        i11 = i17;
                        i5 = 0;
                        i8 = 16;
                    }
                }
            }
            i9--;
            if (i9 < 0) {
                bigIntegerArr[0] = bigIntegerArr4[0];
                bigIntegerArr[1] = bigIntegerArr4[1];
                bigInteger = bigIntegerArr2[0];
                return bigInteger.intValue();
            }
            i10++;
            i7 = i4;
            bigInteger5 = bigInteger3;
            bigIntegerArr3 = bigIntegerArr2;
            bigInteger4 = bigInteger2;
            i5 = 0;
            i8 = 16;
        }
        bigInteger = bigIntegerArr3[i5];
        return bigInteger.intValue();
    }

    private long procedure_Aa(long j, long j2, BigInteger[] bigIntegerArr, int i) {
        BigInteger bigInteger;
        BigInteger[] bigIntegerArr2;
        BigInteger bigInteger2;
        BigInteger bigInteger3;
        int i2;
        long jNextInt = j;
        while (true) {
            if (jNextInt >= 0 && jNextInt <= 4294967296L) {
                break;
            }
            jNextInt = this.init_random.nextInt() * 2;
        }
        long jNextInt2 = j2;
        while (true) {
            if (jNextInt2 >= 0 && jNextInt2 <= 4294967296L && jNextInt2 / 2 != 0) {
                break;
            }
            jNextInt2 = (this.init_random.nextInt() * 2) + 1;
        }
        BigInteger bigInteger4 = new BigInteger(Long.toString(jNextInt2));
        BigInteger bigInteger5 = new BigInteger("97781173");
        BigInteger bigInteger6 = new BigInteger(Long.toString(jNextInt));
        int i3 = 0;
        BigInteger[] bigIntegerArr3 = {bigInteger6};
        int[] iArr = {i};
        int i4 = 0;
        int i5 = 0;
        while (iArr[i4] >= 33) {
            int length = iArr.length + 1;
            int[] iArr2 = new int[length];
            System.arraycopy(iArr, 0, iArr2, 0, iArr.length);
            iArr = new int[length];
            System.arraycopy(iArr2, 0, iArr, 0, length);
            i5 = i4 + 1;
            iArr[i5] = iArr[i4] / 2;
            i4 = i5;
        }
        BigInteger[] bigIntegerArr4 = new BigInteger[i5 + 1];
        bigIntegerArr4[i5] = new BigInteger("8000000B", 16);
        int i6 = i5 - 1;
        int i7 = 0;
        while (i7 < i5) {
            int i8 = 32;
            int i9 = iArr[i6] / 32;
            while (true) {
                int length2 = bigIntegerArr3.length;
                BigInteger[] bigIntegerArr5 = new BigInteger[length2];
                System.arraycopy(bigIntegerArr3, i3, bigIntegerArr5, i3, bigIntegerArr3.length);
                bigIntegerArr2 = new BigInteger[i9 + 1];
                System.arraycopy(bigIntegerArr5, i3, bigIntegerArr2, i3, length2);
                int i10 = i3;
                while (i10 < i9) {
                    int i11 = i10 + 1;
                    bigIntegerArr2[i11] = bigIntegerArr2[i10].multiply(bigInteger5).add(bigInteger4).mod(TWO.pow(i8));
                    i10 = i11;
                }
                BigInteger bigInteger7 = new BigInteger(AppEventsConstants.EVENT_PARAM_VALUE_NO);
                for (int i12 = i3; i12 < i9; i12++) {
                    bigInteger7 = bigInteger7.add(bigIntegerArr2[i12].multiply(TWO.pow(i12 * 32)));
                }
                bigIntegerArr2[i3] = bigIntegerArr2[i9];
                BigInteger bigInteger8 = TWO;
                int i13 = i6 + 1;
                BigInteger bigIntegerAdd = bigInteger8.pow(iArr[i6] - 1).divide(bigIntegerArr4[i13]).add(bigInteger8.pow(iArr[i6] - 1).multiply(bigInteger7).divide(bigIntegerArr4[i13].multiply(bigInteger8.pow(i9 * 32))));
                BigInteger bigIntegerMod = bigIntegerAdd.mod(bigInteger8);
                BigInteger bigInteger9 = ONE;
                if (bigIntegerMod.compareTo(bigInteger9) == 0) {
                    bigIntegerAdd = bigIntegerAdd.add(bigInteger9);
                }
                int i14 = 0;
                while (true) {
                    long j3 = i14;
                    bigInteger2 = bigInteger4;
                    BigInteger bigIntegerMultiply = bigIntegerArr4[i13].multiply(bigIntegerAdd.add(BigInteger.valueOf(j3)));
                    BigInteger bigInteger10 = ONE;
                    BigInteger bigIntegerAdd2 = bigIntegerMultiply.add(bigInteger10);
                    bigIntegerArr4[i6] = bigIntegerAdd2;
                    bigInteger3 = bigInteger5;
                    BigInteger bigInteger11 = TWO;
                    i2 = i5;
                    if (bigIntegerAdd2.compareTo(bigInteger11.pow(iArr[i6])) != 1) {
                        if (bigInteger11.modPow(bigIntegerArr4[i13].multiply(bigIntegerAdd.add(BigInteger.valueOf(j3))), bigIntegerArr4[i6]).compareTo(bigInteger10) == 0 && bigInteger11.modPow(bigIntegerAdd.add(BigInteger.valueOf(j3)), bigIntegerArr4[i6]).compareTo(bigInteger10) != 0) {
                            break;
                        }
                        i14 += 2;
                        bigInteger4 = bigInteger2;
                        i5 = i2;
                        bigInteger5 = bigInteger3;
                    } else {
                        bigInteger4 = bigInteger2;
                        bigIntegerArr3 = bigIntegerArr2;
                        bigInteger5 = bigInteger3;
                        i3 = 0;
                        i8 = 32;
                        i5 = i2;
                    }
                }
            }
            i6--;
            if (i6 < 0) {
                bigIntegerArr[0] = bigIntegerArr4[0];
                bigIntegerArr[1] = bigIntegerArr4[1];
                bigInteger = bigIntegerArr2[0];
                return bigInteger.longValue();
            }
            i7++;
            bigInteger4 = bigInteger2;
            i5 = i2;
            bigIntegerArr3 = bigIntegerArr2;
            bigInteger5 = bigInteger3;
            i3 = 0;
        }
        bigInteger = bigIntegerArr3[i3];
        return bigInteger.longValue();
    }

    private void procedure_B(int i, int i2, BigInteger[] bigIntegerArr) {
        int iNextInt = i;
        while (true) {
            if (iNextInt >= 0 && iNextInt <= 65536) {
                break;
            } else {
                iNextInt = this.init_random.nextInt() / 32768;
            }
        }
        int iNextInt2 = i2;
        while (true) {
            if (iNextInt2 >= 0 && iNextInt2 <= 65536 && iNextInt2 / 2 != 0) {
                break;
            } else {
                iNextInt2 = (this.init_random.nextInt() / 32768) + 1;
            }
        }
        BigInteger[] bigIntegerArr2 = new BigInteger[2];
        BigInteger bigInteger = new BigInteger(Integer.toString(iNextInt2));
        BigInteger bigInteger2 = new BigInteger("19381");
        int iProcedure_A = procedure_A(iNextInt, iNextInt2, bigIntegerArr2, 256);
        int i3 = 0;
        BigInteger bigInteger3 = bigIntegerArr2[0];
        int iProcedure_A2 = procedure_A(iProcedure_A, iNextInt2, bigIntegerArr2, 512);
        BigInteger bigInteger4 = bigIntegerArr2[0];
        BigInteger[] bigIntegerArr3 = new BigInteger[65];
        bigIntegerArr3[0] = new BigInteger(Integer.toString(iProcedure_A2));
        while (true) {
            int i4 = i3;
            while (i4 < 64) {
                int i5 = i4 + 1;
                bigIntegerArr3[i5] = bigIntegerArr3[i4].multiply(bigInteger2).add(bigInteger).mod(TWO.pow(16));
                i4 = i5;
            }
            BigInteger bigInteger5 = new BigInteger(AppEventsConstants.EVENT_PARAM_VALUE_NO);
            for (int i6 = i3; i6 < 64; i6++) {
                bigInteger5 = bigInteger5.add(bigIntegerArr3[i6].multiply(TWO.pow(i6 * 16)));
            }
            bigIntegerArr3[i3] = bigIntegerArr3[64];
            BigInteger bigInteger6 = TWO;
            int i7 = 1024;
            BigInteger bigIntegerAdd = bigInteger6.pow(GLMapStaticValue.AM_PARAMETERNAME_PROCESS_ROADARROW).divide(bigInteger3.multiply(bigInteger4)).add(bigInteger6.pow(GLMapStaticValue.AM_PARAMETERNAME_PROCESS_ROADARROW).multiply(bigInteger5).divide(bigInteger3.multiply(bigInteger4).multiply(bigInteger6.pow(1024))));
            BigInteger bigIntegerMod = bigIntegerAdd.mod(bigInteger6);
            BigInteger bigInteger7 = ONE;
            if (bigIntegerMod.compareTo(bigInteger7) == 0) {
                bigIntegerAdd = bigIntegerAdd.add(bigInteger7);
            }
            BigInteger bigInteger8 = bigIntegerAdd;
            int i8 = i3;
            while (true) {
                long j = i8;
                BigInteger bigIntegerMultiply = bigInteger3.multiply(bigInteger4).multiply(bigInteger8.add(BigInteger.valueOf(j)));
                BigInteger bigInteger9 = ONE;
                BigInteger bigIntegerAdd2 = bigIntegerMultiply.add(bigInteger9);
                BigInteger bigInteger10 = TWO;
                if (bigIntegerAdd2.compareTo(bigInteger10.pow(i7)) == 1) {
                    break;
                }
                if (bigInteger10.modPow(bigInteger3.multiply(bigInteger4).multiply(bigInteger8.add(BigInteger.valueOf(j))), bigIntegerAdd2).compareTo(bigInteger9) == 0 && bigInteger10.modPow(bigInteger3.multiply(bigInteger8.add(BigInteger.valueOf(j))), bigIntegerAdd2).compareTo(bigInteger9) != 0) {
                    bigIntegerArr[0] = bigIntegerAdd2;
                    bigIntegerArr[1] = bigInteger3;
                    return;
                } else {
                    i8 += 2;
                    i7 = 1024;
                }
            }
            i3 = 0;
        }
    }

    private void procedure_Bb(long j, long j2, BigInteger[] bigIntegerArr) {
        long jNextInt = j;
        while (true) {
            if (jNextInt >= 0 && jNextInt <= 4294967296L) {
                break;
            } else {
                jNextInt = this.init_random.nextInt() * 2;
            }
        }
        long jNextInt2 = j2;
        while (true) {
            if (jNextInt2 >= 0 && jNextInt2 <= 4294967296L && jNextInt2 / 2 != 0) {
                break;
            } else {
                jNextInt2 = (this.init_random.nextInt() * 2) + 1;
            }
        }
        BigInteger[] bigIntegerArr2 = new BigInteger[2];
        BigInteger bigInteger = new BigInteger(Long.toString(jNextInt2));
        BigInteger bigInteger2 = new BigInteger("97781173");
        long j3 = jNextInt2;
        long jProcedure_Aa = procedure_Aa(jNextInt, j3, bigIntegerArr2, 256);
        int i = 0;
        BigInteger bigInteger3 = bigIntegerArr2[0];
        long jProcedure_Aa2 = procedure_Aa(jProcedure_Aa, j3, bigIntegerArr2, 512);
        BigInteger bigInteger4 = bigIntegerArr2[0];
        BigInteger[] bigIntegerArr3 = new BigInteger[33];
        bigIntegerArr3[0] = new BigInteger(Long.toString(jProcedure_Aa2));
        while (true) {
            int i2 = i;
            while (i2 < 32) {
                int i3 = i2 + 1;
                bigIntegerArr3[i3] = bigIntegerArr3[i2].multiply(bigInteger2).add(bigInteger).mod(TWO.pow(32));
                i2 = i3;
            }
            BigInteger bigInteger5 = new BigInteger(AppEventsConstants.EVENT_PARAM_VALUE_NO);
            for (int i4 = i; i4 < 32; i4++) {
                bigInteger5 = bigInteger5.add(bigIntegerArr3[i4].multiply(TWO.pow(i4 * 32)));
            }
            bigIntegerArr3[i] = bigIntegerArr3[32];
            BigInteger bigInteger6 = TWO;
            int i5 = 1024;
            BigInteger bigIntegerAdd = bigInteger6.pow(GLMapStaticValue.AM_PARAMETERNAME_PROCESS_ROADARROW).divide(bigInteger3.multiply(bigInteger4)).add(bigInteger6.pow(GLMapStaticValue.AM_PARAMETERNAME_PROCESS_ROADARROW).multiply(bigInteger5).divide(bigInteger3.multiply(bigInteger4).multiply(bigInteger6.pow(1024))));
            BigInteger bigIntegerMod = bigIntegerAdd.mod(bigInteger6);
            BigInteger bigInteger7 = ONE;
            if (bigIntegerMod.compareTo(bigInteger7) == 0) {
                bigIntegerAdd = bigIntegerAdd.add(bigInteger7);
            }
            int i6 = i;
            while (true) {
                long j4 = i6;
                BigInteger bigIntegerMultiply = bigInteger3.multiply(bigInteger4).multiply(bigIntegerAdd.add(BigInteger.valueOf(j4)));
                BigInteger bigInteger8 = ONE;
                BigInteger bigIntegerAdd2 = bigIntegerMultiply.add(bigInteger8);
                BigInteger bigInteger9 = TWO;
                if (bigIntegerAdd2.compareTo(bigInteger9.pow(i5)) == 1) {
                    break;
                }
                if (bigInteger9.modPow(bigInteger3.multiply(bigInteger4).multiply(bigIntegerAdd.add(BigInteger.valueOf(j4))), bigIntegerAdd2).compareTo(bigInteger8) == 0 && bigInteger9.modPow(bigInteger3.multiply(bigIntegerAdd.add(BigInteger.valueOf(j4))), bigIntegerAdd2).compareTo(bigInteger8) != 0) {
                    bigIntegerArr[0] = bigIntegerAdd2;
                    bigIntegerArr[1] = bigInteger3;
                    return;
                } else {
                    i6 += 2;
                    i5 = 1024;
                }
            }
            i = 0;
        }
    }

    private BigInteger procedure_C(BigInteger bigInteger, BigInteger bigInteger2) {
        BigInteger bigIntegerSubtract = bigInteger.subtract(ONE);
        BigInteger bigIntegerDivide = bigIntegerSubtract.divide(bigInteger2);
        int iBitLength = bigInteger.bitLength();
        while (true) {
            BigInteger bigInteger3 = new BigInteger(iBitLength, this.init_random);
            BigInteger bigInteger4 = ONE;
            if (bigInteger3.compareTo(bigInteger4) > 0 && bigInteger3.compareTo(bigIntegerSubtract) < 0) {
                BigInteger bigIntegerModPow = bigInteger3.modPow(bigIntegerDivide, bigInteger);
                if (bigIntegerModPow.compareTo(bigInteger4) != 0) {
                    return bigIntegerModPow;
                }
            }
        }
    }

    public GOST3410Parameters generateParameters() {
        BigInteger[] bigIntegerArr = new BigInteger[2];
        if (this.typeproc == 1) {
            int iNextInt = this.init_random.nextInt();
            int iNextInt2 = this.init_random.nextInt();
            int i = this.size;
            if (i == 512) {
                procedure_A(iNextInt, iNextInt2, bigIntegerArr, 512);
            } else {
                if (i != 1024) {
                    throw new IllegalArgumentException("Ooops! key size 512 or 1024 bit.");
                }
                procedure_B(iNextInt, iNextInt2, bigIntegerArr);
            }
            BigInteger bigInteger = bigIntegerArr[0];
            BigInteger bigInteger2 = bigIntegerArr[1];
            return new GOST3410Parameters(bigInteger, bigInteger2, procedure_C(bigInteger, bigInteger2), new GOST3410ValidationParameters(iNextInt, iNextInt2));
        }
        long jNextLong = this.init_random.nextLong();
        long jNextLong2 = this.init_random.nextLong();
        int i2 = this.size;
        if (i2 == 512) {
            procedure_Aa(jNextLong, jNextLong2, bigIntegerArr, 512);
        } else {
            if (i2 != 1024) {
                throw new IllegalStateException("Ooops! key size 512 or 1024 bit.");
            }
            procedure_Bb(jNextLong, jNextLong2, bigIntegerArr);
        }
        BigInteger bigInteger3 = bigIntegerArr[0];
        BigInteger bigInteger4 = bigIntegerArr[1];
        return new GOST3410Parameters(bigInteger3, bigInteger4, procedure_C(bigInteger3, bigInteger4), new GOST3410ValidationParameters(jNextLong, jNextLong2));
    }

    public void init(int i, int i2, SecureRandom secureRandom) {
        this.size = i;
        this.typeproc = i2;
        this.init_random = secureRandom;
    }
}
