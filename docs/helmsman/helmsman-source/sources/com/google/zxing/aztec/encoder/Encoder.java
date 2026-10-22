package com.google.zxing.aztec.encoder;

import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.reedsolomon.GenericGF;
import com.google.zxing.common.reedsolomon.ReedSolomonEncoder;

/* JADX INFO: loaded from: classes2.dex */
public final class Encoder {
    public static final int DEFAULT_AZTEC_LAYERS = 0;
    public static final int DEFAULT_EC_PERCENT = 33;
    private static final int MAX_NB_BITS = 32;
    private static final int MAX_NB_BITS_COMPACT = 4;
    private static final int[] WORD_SIZE = {4, 6, 6, 8, 8, 8, 8, 8, 8, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12};

    private static int totalBitsInLayer(int i, boolean z) {
        return ((z ? 88 : 112) + (i << 4)) * i;
    }

    private Encoder() {
    }

    public static AztecCode encode(byte[] bArr) {
        return encode(bArr, 33, 0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static AztecCode encode(byte[] bArr, int i, int i2) {
        BitArray bitArrayStuffBits;
        boolean z;
        int iAbs;
        int i3;
        int i4;
        int i5;
        BitArray bitArrayEncode = new HighLevelEncoder(bArr).encode();
        int size = ((bitArrayEncode.getSize() * i) / 100) + 11;
        int size2 = bitArrayEncode.getSize() + size;
        int i6 = 1;
        if (i2 != 0) {
            boolean z2 = i2 < 0;
            iAbs = Math.abs(i2);
            if (iAbs > (z2 ? 4 : 32)) {
                throw new IllegalArgumentException(String.format("Illegal value %s for layers", Integer.valueOf(i2)));
            }
            i3 = totalBitsInLayer(iAbs, z2);
            i4 = WORD_SIZE[iAbs];
            int i7 = i3 - (i3 % i4);
            bitArrayStuffBits = stuffBits(bitArrayEncode, i4);
            if (bitArrayStuffBits.getSize() + size > i7) {
                z = z2;
                throw new IllegalArgumentException("Data to large for user specified layer");
            }
            if (z2) {
                z = z2;
                if (bitArrayStuffBits.getSize() > (i4 << 6)) {
                    throw new IllegalArgumentException("Data to large for user specified layer");
                }
            }
        } else {
            BitArray bitArrayStuffBits2 = null;
            int i8 = 0;
            int i9 = 0;
            while (true) {
                if (i8 > 32) {
                    throw new IllegalArgumentException("Data too large for an Aztec code");
                }
                boolean z3 = i8 <= 3 ? i6 : 0;
                int i10 = z3 != 0 ? i8 + 1 : i8;
                int i11 = totalBitsInLayer(i10, z3);
                if (size2 <= i11) {
                    int i12 = WORD_SIZE[i10];
                    if (i9 != i12) {
                        bitArrayStuffBits2 = stuffBits(bitArrayEncode, i12);
                    } else {
                        i12 = i9;
                    }
                    int i13 = i11 - (i11 % i12);
                    if ((z3 == 0 || bitArrayStuffBits2.getSize() <= (i12 << 6)) && bitArrayStuffBits2.getSize() + size <= i13) {
                        bitArrayStuffBits = bitArrayStuffBits2;
                        z = z3;
                        iAbs = i10;
                        i3 = i11;
                        i4 = i12;
                        break;
                    }
                    i9 = i12;
                }
                i8++;
                i6 = i6;
            }
        }
        BitArray bitArrayGenerateCheckWords = generateCheckWords(bitArrayStuffBits, i3, i4);
        int size3 = bitArrayStuffBits.getSize() / i4;
        BitArray bitArrayGenerateModeMessage = generateModeMessage(z, iAbs, size3);
        int i14 = (z ? 11 : 14) + (iAbs << 2);
        int[] iArr = new int[i14];
        int i15 = 2;
        if (z) {
            for (int i16 = 0; i16 < i14; i16++) {
                iArr[i16] = i16;
            }
            i5 = i14;
        } else {
            int i17 = i14 / 2;
            i5 = i14 + 1 + (((i17 - 1) / 15) * 2);
            int i18 = i5 / 2;
            for (int i19 = 0; i19 < i17; i19++) {
                int i20 = (i19 / 15) + i19;
                iArr[(i17 - i19) - i6] = (i18 - i20) - 1;
                iArr[i17 + i19] = i20 + i18 + i6;
            }
        }
        BitMatrix bitMatrix = new BitMatrix(i5);
        int i21 = 0;
        int i22 = 0;
        while (i21 < iAbs) {
            int i23 = ((iAbs - i21) << i15) + (z ? 9 : 12);
            int i24 = 0;
            while (i24 < i23) {
                int i25 = i24 << 1;
                int i26 = 0;
                while (i26 < i15) {
                    if (bitArrayGenerateCheckWords.get(i22 + i25 + i26)) {
                        int i27 = i21 << 1;
                        bitMatrix.set(iArr[i27 + i26], iArr[i27 + i24]);
                    }
                    if (bitArrayGenerateCheckWords.get((i23 << 1) + i22 + i25 + i26)) {
                        int i28 = i21 << 1;
                        bitMatrix.set(iArr[i28 + i24], iArr[((i14 - 1) - i28) - i26]);
                    }
                    if (bitArrayGenerateCheckWords.get((i23 << 2) + i22 + i25 + i26)) {
                        int i29 = (i14 - 1) - (i21 << 1);
                        bitMatrix.set(iArr[i29 - i26], iArr[i29 - i24]);
                    }
                    if (bitArrayGenerateCheckWords.get((i23 * 6) + i22 + i25 + i26)) {
                        int i30 = i21 << 1;
                        bitMatrix.set(iArr[((i14 - 1) - i30) - i24], iArr[i30 + i26]);
                    }
                    i26++;
                    i15 = 2;
                }
                i24++;
                i15 = 2;
            }
            i22 += i23 << 3;
            i21++;
            i15 = 2;
        }
        drawModeMessage(bitMatrix, z, i5, bitArrayGenerateModeMessage);
        if (z) {
            drawBullsEye(bitMatrix, i5 / 2, 5);
        } else {
            int i31 = i5 / 2;
            drawBullsEye(bitMatrix, i31, 7);
            int i32 = 0;
            int i33 = 0;
            while (i33 < (i14 / 2) - 1) {
                for (int i34 = i31 & 1; i34 < i5; i34 += 2) {
                    int i35 = i31 - i32;
                    bitMatrix.set(i35, i34);
                    int i36 = i31 + i32;
                    bitMatrix.set(i36, i34);
                    bitMatrix.set(i34, i35);
                    bitMatrix.set(i34, i36);
                }
                i33 += 15;
                i32 += 16;
            }
        }
        AztecCode aztecCode = new AztecCode();
        aztecCode.setCompact(z);
        aztecCode.setSize(i5);
        aztecCode.setLayers(iAbs);
        aztecCode.setCodeWords(size3);
        aztecCode.setMatrix(bitMatrix);
        return aztecCode;
    }

    private static void drawBullsEye(BitMatrix bitMatrix, int i, int i2) {
        for (int i3 = 0; i3 < i2; i3 += 2) {
            int i4 = i - i3;
            int i5 = i4;
            while (true) {
                int i6 = i + i3;
                if (i5 <= i6) {
                    bitMatrix.set(i5, i4);
                    bitMatrix.set(i5, i6);
                    bitMatrix.set(i4, i5);
                    bitMatrix.set(i6, i5);
                    i5++;
                }
            }
        }
        int i7 = i - i2;
        bitMatrix.set(i7, i7);
        int i8 = i7 + 1;
        bitMatrix.set(i8, i7);
        bitMatrix.set(i7, i8);
        int i9 = i + i2;
        bitMatrix.set(i9, i7);
        bitMatrix.set(i9, i8);
        bitMatrix.set(i9, i9 - 1);
    }

    static BitArray generateModeMessage(boolean z, int i, int i2) {
        BitArray bitArray = new BitArray();
        if (z) {
            bitArray.appendBits(i - 1, 2);
            bitArray.appendBits(i2 - 1, 6);
            return generateCheckWords(bitArray, 28, 4);
        }
        bitArray.appendBits(i - 1, 5);
        bitArray.appendBits(i2 - 1, 11);
        return generateCheckWords(bitArray, 40, 4);
    }

    private static void drawModeMessage(BitMatrix bitMatrix, boolean z, int i, BitArray bitArray) {
        int i2 = i / 2;
        int i3 = 0;
        if (z) {
            while (i3 < 7) {
                int i4 = (i2 - 3) + i3;
                if (bitArray.get(i3)) {
                    bitMatrix.set(i4, i2 - 5);
                }
                if (bitArray.get(i3 + 7)) {
                    bitMatrix.set(i2 + 5, i4);
                }
                if (bitArray.get(20 - i3)) {
                    bitMatrix.set(i4, i2 + 5);
                }
                if (bitArray.get(27 - i3)) {
                    bitMatrix.set(i2 - 5, i4);
                }
                i3++;
            }
            return;
        }
        while (i3 < 10) {
            int i5 = (i2 - 5) + i3 + (i3 / 5);
            if (bitArray.get(i3)) {
                bitMatrix.set(i5, i2 - 7);
            }
            if (bitArray.get(i3 + 10)) {
                bitMatrix.set(i2 + 7, i5);
            }
            if (bitArray.get(29 - i3)) {
                bitMatrix.set(i5, i2 + 7);
            }
            if (bitArray.get(39 - i3)) {
                bitMatrix.set(i2 - 7, i5);
            }
            i3++;
        }
    }

    private static BitArray generateCheckWords(BitArray bitArray, int i, int i2) {
        int size = bitArray.getSize() / i2;
        ReedSolomonEncoder reedSolomonEncoder = new ReedSolomonEncoder(getGF(i2));
        int i3 = i / i2;
        int[] iArrBitsToWords = bitsToWords(bitArray, i2, i3);
        reedSolomonEncoder.encode(iArrBitsToWords, i3 - size);
        BitArray bitArray2 = new BitArray();
        bitArray2.appendBits(0, i % i2);
        for (int i4 : iArrBitsToWords) {
            bitArray2.appendBits(i4, i2);
        }
        return bitArray2;
    }

    private static int[] bitsToWords(BitArray bitArray, int i, int i2) {
        int[] iArr = new int[i2];
        int size = bitArray.getSize() / i;
        for (int i3 = 0; i3 < size; i3++) {
            int i4 = 0;
            for (int i5 = 0; i5 < i; i5++) {
                i4 |= bitArray.get((i3 * i) + i5) ? 1 << ((i - i5) - 1) : 0;
            }
            iArr[i3] = i4;
        }
        return iArr;
    }

    private static GenericGF getGF(int i) {
        if (i == 4) {
            return GenericGF.AZTEC_PARAM;
        }
        if (i == 6) {
            return GenericGF.AZTEC_DATA_6;
        }
        if (i == 8) {
            return GenericGF.AZTEC_DATA_8;
        }
        if (i == 10) {
            return GenericGF.AZTEC_DATA_10;
        }
        if (i == 12) {
            return GenericGF.AZTEC_DATA_12;
        }
        throw new IllegalArgumentException("Unsupported word size " + i);
    }

    static BitArray stuffBits(BitArray bitArray, int i) {
        BitArray bitArray2 = new BitArray();
        int size = bitArray.getSize();
        int i2 = (1 << i) - 2;
        int i3 = 0;
        while (i3 < size) {
            int i4 = 0;
            for (int i5 = 0; i5 < i; i5++) {
                int i6 = i3 + i5;
                if (i6 >= size || bitArray.get(i6)) {
                    i4 |= 1 << ((i - 1) - i5);
                }
            }
            int i7 = i4 & i2;
            if (i7 == i2) {
                bitArray2.appendBits(i7, i);
            } else {
                if (i7 == 0) {
                    bitArray2.appendBits(i4 | 1, i);
                } else {
                    bitArray2.appendBits(i4, i);
                }
                i3 += i;
            }
            i3--;
            i3 += i;
        }
        return bitArray2;
    }
}
