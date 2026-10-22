package kotlin.collections;

import kotlin.Metadata;
import kotlin.UByte;
import kotlin.UByteArray;
import kotlin.UIntArray;
import kotlin.ULongArray;
import kotlin.UShort;
import kotlin.UShortArray;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: UArraySorting.kt */
/* JADX INFO: loaded from: classes2.dex */
@Metadata(d1 = {"\u00000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0010\u001a'\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\u0006\u0010\u0007\u001a'\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\t\u0010\n\u001a'\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\f\u0010\r\u001a'\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\u000f\u0010\u0010\u001a'\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\u0013\u0010\u0014\u001a'\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\u0015\u0010\u0016\u001a'\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\u0017\u0010\u0018\u001a'\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\u0019\u0010\u001a\u001a'\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001¢\u0006\u0004\b\u001e\u0010\u0014\u001a'\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001¢\u0006\u0004\b\u001f\u0010\u0016\u001a'\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001¢\u0006\u0004\b \u0010\u0018\u001a'\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001¢\u0006\u0004\b!\u0010\u001a¨\u0006\""}, d2 = {"partition", "", "array", "Lkotlin/UByteArray;", "left", "right", "partition-4UcCI2c", "([BII)I", "Lkotlin/UIntArray;", "partition-oBK06Vg", "([III)I", "Lkotlin/ULongArray;", "partition--nroSd4", "([JII)I", "Lkotlin/UShortArray;", "partition-Aa5vz7o", "([SII)I", "quickSort", "", "quickSort-4UcCI2c", "([BII)V", "quickSort-oBK06Vg", "([III)V", "quickSort--nroSd4", "([JII)V", "quickSort-Aa5vz7o", "([SII)V", "sortArray", "fromIndex", "toIndex", "sortArray-4UcCI2c", "sortArray-oBK06Vg", "sortArray--nroSd4", "sortArray-Aa5vz7o", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class UArraySortingKt {
    /* JADX INFO: renamed from: partition-4UcCI2c, reason: not valid java name */
    private static final int m732partition4UcCI2c(byte[] bArr, int i, int i2) {
        int i3;
        byte bM352getw2LRezQ = UByteArray.m352getw2LRezQ(bArr, (i + i2) / 2);
        while (i <= i2) {
            while (true) {
                int iM352getw2LRezQ = UByteArray.m352getw2LRezQ(bArr, i) & UByte.MAX_VALUE;
                i3 = bM352getw2LRezQ & UByte.MAX_VALUE;
                if (Intrinsics.compare(iM352getw2LRezQ, i3) >= 0) {
                    break;
                }
                i++;
            }
            while (Intrinsics.compare(UByteArray.m352getw2LRezQ(bArr, i2) & UByte.MAX_VALUE, i3) > 0) {
                i2--;
            }
            if (i <= i2) {
                byte bM352getw2LRezQ2 = UByteArray.m352getw2LRezQ(bArr, i);
                UByteArray.m357setVurrAj0(bArr, i, UByteArray.m352getw2LRezQ(bArr, i2));
                UByteArray.m357setVurrAj0(bArr, i2, bM352getw2LRezQ2);
                i++;
                i2--;
            }
        }
        return i;
    }

    /* JADX INFO: renamed from: quickSort-4UcCI2c, reason: not valid java name */
    private static final void m736quickSort4UcCI2c(byte[] bArr, int i, int i2) {
        int iM732partition4UcCI2c = m732partition4UcCI2c(bArr, i, i2);
        int i3 = iM732partition4UcCI2c - 1;
        if (i < i3) {
            m736quickSort4UcCI2c(bArr, i, i3);
        }
        if (iM732partition4UcCI2c < i2) {
            m736quickSort4UcCI2c(bArr, iM732partition4UcCI2c, i2);
        }
    }

    /* JADX INFO: renamed from: partition-Aa5vz7o, reason: not valid java name */
    private static final int m733partitionAa5vz7o(short[] sArr, int i, int i2) {
        int i3;
        short sM615getMh2AYeg = UShortArray.m615getMh2AYeg(sArr, (i + i2) / 2);
        while (i <= i2) {
            while (true) {
                int iM615getMh2AYeg = UShortArray.m615getMh2AYeg(sArr, i) & UShort.MAX_VALUE;
                i3 = sM615getMh2AYeg & UShort.MAX_VALUE;
                if (Intrinsics.compare(iM615getMh2AYeg, i3) >= 0) {
                    break;
                }
                i++;
            }
            while (Intrinsics.compare(UShortArray.m615getMh2AYeg(sArr, i2) & UShort.MAX_VALUE, i3) > 0) {
                i2--;
            }
            if (i <= i2) {
                short sM615getMh2AYeg2 = UShortArray.m615getMh2AYeg(sArr, i);
                UShortArray.m620set01HTLdE(sArr, i, UShortArray.m615getMh2AYeg(sArr, i2));
                UShortArray.m620set01HTLdE(sArr, i2, sM615getMh2AYeg2);
                i++;
                i2--;
            }
        }
        return i;
    }

    /* JADX INFO: renamed from: quickSort-Aa5vz7o, reason: not valid java name */
    private static final void m737quickSortAa5vz7o(short[] sArr, int i, int i2) {
        int iM733partitionAa5vz7o = m733partitionAa5vz7o(sArr, i, i2);
        int i3 = iM733partitionAa5vz7o - 1;
        if (i < i3) {
            m737quickSortAa5vz7o(sArr, i, i3);
        }
        if (iM733partitionAa5vz7o < i2) {
            m737quickSortAa5vz7o(sArr, iM733partitionAa5vz7o, i2);
        }
    }

    /* JADX INFO: renamed from: partition-oBK06Vg, reason: not valid java name */
    private static final int m734partitionoBK06Vg(int[] iArr, int i, int i2) {
        int iM431getpVg5ArA = UIntArray.m431getpVg5ArA(iArr, (i + i2) / 2);
        while (i <= i2) {
            while (Integer.compareUnsigned(UIntArray.m431getpVg5ArA(iArr, i), iM431getpVg5ArA) < 0) {
                i++;
            }
            while (Integer.compareUnsigned(UIntArray.m431getpVg5ArA(iArr, i2), iM431getpVg5ArA) > 0) {
                i2--;
            }
            if (i <= i2) {
                int iM431getpVg5ArA2 = UIntArray.m431getpVg5ArA(iArr, i);
                UIntArray.m436setVXSXFK8(iArr, i, UIntArray.m431getpVg5ArA(iArr, i2));
                UIntArray.m436setVXSXFK8(iArr, i2, iM431getpVg5ArA2);
                i++;
                i2--;
            }
        }
        return i;
    }

    /* JADX INFO: renamed from: quickSort-oBK06Vg, reason: not valid java name */
    private static final void m738quickSortoBK06Vg(int[] iArr, int i, int i2) {
        int iM734partitionoBK06Vg = m734partitionoBK06Vg(iArr, i, i2);
        int i3 = iM734partitionoBK06Vg - 1;
        if (i < i3) {
            m738quickSortoBK06Vg(iArr, i, i3);
        }
        if (iM734partitionoBK06Vg < i2) {
            m738quickSortoBK06Vg(iArr, iM734partitionoBK06Vg, i2);
        }
    }

    /* JADX INFO: renamed from: partition--nroSd4, reason: not valid java name */
    private static final int m731partitionnroSd4(long[] jArr, int i, int i2) {
        long jM510getsVKNKU = ULongArray.m510getsVKNKU(jArr, (i + i2) / 2);
        while (i <= i2) {
            while (Long.compareUnsigned(ULongArray.m510getsVKNKU(jArr, i), jM510getsVKNKU) < 0) {
                i++;
            }
            while (Long.compareUnsigned(ULongArray.m510getsVKNKU(jArr, i2), jM510getsVKNKU) > 0) {
                i2--;
            }
            if (i <= i2) {
                long jM510getsVKNKU2 = ULongArray.m510getsVKNKU(jArr, i);
                ULongArray.m515setk8EXiF4(jArr, i, ULongArray.m510getsVKNKU(jArr, i2));
                ULongArray.m515setk8EXiF4(jArr, i2, jM510getsVKNKU2);
                i++;
                i2--;
            }
        }
        return i;
    }

    /* JADX INFO: renamed from: quickSort--nroSd4, reason: not valid java name */
    private static final void m735quickSortnroSd4(long[] jArr, int i, int i2) {
        int iM731partitionnroSd4 = m731partitionnroSd4(jArr, i, i2);
        int i3 = iM731partitionnroSd4 - 1;
        if (i < i3) {
            m735quickSortnroSd4(jArr, i, i3);
        }
        if (iM731partitionnroSd4 < i2) {
            m735quickSortnroSd4(jArr, iM731partitionnroSd4, i2);
        }
    }

    /* JADX INFO: renamed from: sortArray-4UcCI2c, reason: not valid java name */
    public static final void m740sortArray4UcCI2c(byte[] array, int i, int i2) {
        Intrinsics.checkNotNullParameter(array, "array");
        m736quickSort4UcCI2c(array, i, i2 - 1);
    }

    /* JADX INFO: renamed from: sortArray-Aa5vz7o, reason: not valid java name */
    public static final void m741sortArrayAa5vz7o(short[] array, int i, int i2) {
        Intrinsics.checkNotNullParameter(array, "array");
        m737quickSortAa5vz7o(array, i, i2 - 1);
    }

    /* JADX INFO: renamed from: sortArray-oBK06Vg, reason: not valid java name */
    public static final void m742sortArrayoBK06Vg(int[] array, int i, int i2) {
        Intrinsics.checkNotNullParameter(array, "array");
        m738quickSortoBK06Vg(array, i, i2 - 1);
    }

    /* JADX INFO: renamed from: sortArray--nroSd4, reason: not valid java name */
    public static final void m739sortArraynroSd4(long[] array, int i, int i2) {
        Intrinsics.checkNotNullParameter(array, "array");
        m735quickSortnroSd4(array, i, i2 - 1);
    }
}
