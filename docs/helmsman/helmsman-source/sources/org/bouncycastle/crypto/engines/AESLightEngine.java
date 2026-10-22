package org.bouncycastle.crypto.engines;

import com.MAVLink.common.msg_power_status;
import com.MAVLink.enums.MAV_COMPONENT;
import com.MAVLink.enums.MAV_MODE;
import com.MAVLink.helmsman.msg_navigation_data;
import java.lang.reflect.Array;
import kotlin.UByte;
import kotlin.io.encoding.Base64;
import kotlin.jvm.internal.ByteCompanionObject;
import okio.Utf8;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.signers.PSSSigner;
import org.bouncycastle.math.ec.Tnaf;

/* JADX INFO: loaded from: classes2.dex */
public class AESLightEngine implements BlockCipher {
    private static final int BLOCK_SIZE = 16;
    private static final int m1 = -2139062144;
    private static final int m2 = 2139062143;
    private static final int m3 = 27;
    private int C0;
    private int C1;
    private int C2;
    private int C3;
    private int ROUNDS;
    private int[][] WorkingKey = null;
    private boolean forEncryption;
    private static final byte[] S = {99, 124, 119, 123, -14, 107, 111, -59, 48, 1, 103, 43, -2, -41, -85, 118, -54, -126, -55, 125, -6, 89, 71, -16, -83, -44, -94, -81, -100, -92, 114, -64, -73, -3, -109, 38, 54, Utf8.REPLACEMENT_BYTE, -9, -52, 52, -91, -27, -15, 113, -40, 49, 21, 4, -57, 35, -61, 24, -106, 5, -102, 7, 18, ByteCompanionObject.MIN_VALUE, -30, -21, 39, -78, 117, 9, -125, 44, 26, 27, 110, 90, -96, 82, 59, -42, -77, 41, -29, 47, -124, 83, -47, 0, -19, 32, -4, -79, 91, 106, -53, -66, 57, 74, 76, 88, -49, -48, -17, -86, -5, 67, 77, 51, -123, 69, -7, 2, ByteCompanionObject.MAX_VALUE, 80, 60, -97, -88, 81, -93, 64, -113, -110, -99, 56, -11, PSSSigner.TRAILER_IMPLICIT, -74, -38, 33, Tnaf.POW_2_WIDTH, -1, -13, -46, -51, 12, 19, -20, 95, -105, 68, 23, -60, -89, 126, Base64.padSymbol, 100, 93, 25, 115, 96, -127, 79, -36, 34, 42, -112, -120, 70, -18, -72, 20, -34, 94, 11, -37, -32, 50, 58, 10, 73, 6, 36, 92, -62, -45, -84, 98, -111, -107, -28, 121, -25, -56, 55, 109, -115, -43, 78, -87, 108, 86, -12, -22, 101, 122, -82, 8, -70, 120, 37, 46, 28, -90, -76, -58, -24, -35, 116, 31, 75, -67, -117, -118, 112, 62, -75, 102, 72, 3, -10, 14, 97, 53, 87, -71, -122, -63, 29, -98, -31, -8, -104, 17, 105, -39, -114, -108, -101, 30, -121, -23, -50, 85, 40, -33, -116, -95, -119, 13, -65, -26, 66, 104, 65, -103, 45, 15, -80, 84, -69, 22};
    private static final byte[] Si = {82, 9, 106, -43, 48, 54, -91, 56, -65, 64, -93, -98, -127, -13, -41, -5, 124, -29, 57, -126, -101, 47, -1, -121, 52, -114, 67, 68, -60, -34, -23, -53, 84, 123, -108, 50, -90, -62, 35, Base64.padSymbol, -18, 76, -107, 11, 66, -6, -61, 78, 8, 46, -95, 102, 40, -39, 36, -78, 118, 91, -94, 73, 109, -117, -47, 37, 114, -8, -10, 100, -122, 104, -104, 22, -44, -92, 92, -52, 93, 101, -74, -110, 108, 112, 72, 80, -3, -19, -71, -38, 94, 21, 70, 87, -89, -115, -99, -124, -112, -40, -85, 0, -116, PSSSigner.TRAILER_IMPLICIT, -45, 10, -9, -28, 88, 5, -72, -77, 69, 6, -48, 44, 30, -113, -54, Utf8.REPLACEMENT_BYTE, 15, 2, -63, -81, -67, 3, 1, 19, -118, 107, 58, -111, 17, 65, 79, 103, -36, -22, -105, -14, -49, -50, -16, -76, -26, 115, -106, -84, 116, 34, -25, -83, 53, -123, -30, -7, 55, -24, 28, 117, -33, 110, 71, -15, 26, 113, 29, 41, -59, -119, 111, -73, 98, 14, -86, 24, -66, 27, -4, 86, 62, 75, -58, -46, 121, 32, -102, -37, -64, -2, 120, -51, 90, -12, 31, -35, -88, 51, -120, 7, -57, 49, -79, 18, Tnaf.POW_2_WIDTH, 89, 39, ByteCompanionObject.MIN_VALUE, -20, 95, 96, 81, ByteCompanionObject.MAX_VALUE, -87, 25, -75, 74, 13, 45, -27, 122, -97, -109, -55, -100, -17, -96, -32, 59, 77, -82, 42, -11, -80, -56, -21, -69, 60, -125, 83, -103, 97, 23, 43, 4, 126, -70, 119, -42, 38, -31, 105, 20, 99, 85, 33, 12, 125};
    private static final int[] rcon = {1, 2, 4, 8, 16, 32, 64, 128, 27, 54, 108, MAV_MODE.MAV_MODE_GUIDED_ARMED, msg_navigation_data.MAVLINK_MSG_ID_NAVIGATION_DATA, 77, 154, 47, 94, 188, 99, 198, 151, 53, 106, 212, 179, msg_power_status.MAVLINK_MSG_ID_POWER_STATUS, 250, 239, 197, MAV_COMPONENT.MAV_COMP_ID_SERVO6};

    public AESLightEngine() {
    }

    private int FFmulX(int i) {
        return (((i & m1) >>> 7) * 27) ^ ((m2 & i) << 1);
    }

    private void decryptBlock(int[][] iArr) {
        int i = this.C0;
        int i2 = this.ROUNDS;
        int[] iArr2 = iArr[i2];
        this.C0 = i ^ iArr2[0];
        this.C1 ^= iArr2[1];
        this.C2 ^= iArr2[2];
        this.C3 ^= iArr2[3];
        int i3 = i2 - 1;
        while (true) {
            byte[] bArr = Si;
            int i4 = this.C0 & 255;
            if (i3 <= 1) {
                int iInv_mcol = inv_mcol((((bArr[i4] & UByte.MAX_VALUE) ^ ((bArr[(this.C3 >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr[(this.C2 >> 16) & 255] & UByte.MAX_VALUE) << 16)) ^ (bArr[(this.C1 >> 24) & 255] << 24)) ^ iArr[i3][0];
                int iInv_mcol2 = inv_mcol((((bArr[this.C1 & 255] & UByte.MAX_VALUE) ^ ((bArr[(this.C0 >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr[(this.C3 >> 16) & 255] & UByte.MAX_VALUE) << 16)) ^ (bArr[(this.C2 >> 24) & 255] << 24)) ^ iArr[i3][1];
                int iInv_mcol3 = inv_mcol((((bArr[this.C2 & 255] & UByte.MAX_VALUE) ^ ((bArr[(this.C1 >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr[(this.C0 >> 16) & 255] & UByte.MAX_VALUE) << 16)) ^ (bArr[(this.C3 >> 24) & 255] << 24)) ^ iArr[i3][2];
                int iInv_mcol4 = iArr[i3][3] ^ inv_mcol((((bArr[this.C3 & 255] & UByte.MAX_VALUE) ^ ((bArr[(this.C2 >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr[(this.C1 >> 16) & 255] & UByte.MAX_VALUE) << 16)) ^ (bArr[(this.C0 >> 24) & 255] << 24));
                int i5 = (((bArr[iInv_mcol & 255] & UByte.MAX_VALUE) ^ ((bArr[(iInv_mcol4 >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr[(iInv_mcol3 >> 16) & 255] & UByte.MAX_VALUE) << 16)) ^ (bArr[(iInv_mcol2 >> 24) & 255] << 24);
                int[] iArr3 = iArr[0];
                this.C0 = iArr3[0] ^ i5;
                this.C1 = ((((bArr[iInv_mcol2 & 255] & UByte.MAX_VALUE) ^ ((bArr[(iInv_mcol >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr[(iInv_mcol4 >> 16) & 255] & UByte.MAX_VALUE) << 16)) ^ (bArr[(iInv_mcol3 >> 24) & 255] << 24)) ^ iArr3[1];
                this.C2 = ((((bArr[iInv_mcol3 & 255] & UByte.MAX_VALUE) ^ ((bArr[(iInv_mcol2 >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr[(iInv_mcol >> 16) & 255] & UByte.MAX_VALUE) << 16)) ^ (bArr[(iInv_mcol4 >> 24) & 255] << 24)) ^ iArr3[2];
                this.C3 = iArr3[3] ^ ((bArr[(iInv_mcol >> 24) & 255] << 24) ^ (((bArr[iInv_mcol4 & 255] & UByte.MAX_VALUE) ^ ((bArr[(iInv_mcol3 >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr[(iInv_mcol2 >> 16) & 255] & UByte.MAX_VALUE) << 16)));
                return;
            }
            int iInv_mcol5 = inv_mcol((((bArr[i4] & UByte.MAX_VALUE) ^ ((bArr[(this.C3 >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr[(this.C2 >> 16) & 255] & UByte.MAX_VALUE) << 16)) ^ (bArr[(this.C1 >> 24) & 255] << 24)) ^ iArr[i3][0];
            int iInv_mcol6 = inv_mcol((((bArr[this.C1 & 255] & UByte.MAX_VALUE) ^ ((bArr[(this.C0 >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr[(this.C3 >> 16) & 255] & UByte.MAX_VALUE) << 16)) ^ (bArr[(this.C2 >> 24) & 255] << 24)) ^ iArr[i3][1];
            int iInv_mcol7 = inv_mcol((((bArr[this.C2 & 255] & UByte.MAX_VALUE) ^ ((bArr[(this.C1 >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr[(this.C0 >> 16) & 255] & UByte.MAX_VALUE) << 16)) ^ (bArr[(this.C3 >> 24) & 255] << 24)) ^ iArr[i3][2];
            int i6 = i3 - 1;
            int iInv_mcol8 = iArr[i3][3] ^ inv_mcol((((bArr[this.C3 & 255] & UByte.MAX_VALUE) ^ ((bArr[(this.C2 >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr[(this.C1 >> 16) & 255] & UByte.MAX_VALUE) << 16)) ^ (bArr[(this.C0 >> 24) & 255] << 24));
            this.C0 = inv_mcol((((bArr[iInv_mcol5 & 255] & UByte.MAX_VALUE) ^ ((bArr[(iInv_mcol8 >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr[(iInv_mcol7 >> 16) & 255] & UByte.MAX_VALUE) << 16)) ^ (bArr[(iInv_mcol6 >> 24) & 255] << 24)) ^ iArr[i6][0];
            this.C1 = inv_mcol((((bArr[iInv_mcol6 & 255] & UByte.MAX_VALUE) ^ ((bArr[(iInv_mcol5 >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr[(iInv_mcol8 >> 16) & 255] & UByte.MAX_VALUE) << 16)) ^ (bArr[(iInv_mcol7 >> 24) & 255] << 24)) ^ iArr[i6][1];
            this.C2 = inv_mcol((((bArr[iInv_mcol7 & 255] & UByte.MAX_VALUE) ^ ((bArr[(iInv_mcol6 >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr[(iInv_mcol5 >> 16) & 255] & UByte.MAX_VALUE) << 16)) ^ (bArr[(iInv_mcol8 >> 24) & 255] << 24)) ^ iArr[i6][2];
            int iInv_mcol9 = inv_mcol((bArr[(iInv_mcol5 >> 24) & 255] << 24) ^ (((bArr[iInv_mcol8 & 255] & UByte.MAX_VALUE) ^ ((bArr[(iInv_mcol7 >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr[(iInv_mcol6 >> 16) & 255] & UByte.MAX_VALUE) << 16)));
            i3 = i6 - 1;
            this.C3 = iInv_mcol9 ^ iArr[i6][3];
        }
    }

    private void encryptBlock(int[][] iArr) {
        int i = this.C0;
        int[] iArr2 = iArr[0];
        this.C0 = i ^ iArr2[0];
        this.C1 ^= iArr2[1];
        this.C2 ^= iArr2[2];
        this.C3 ^= iArr2[3];
        int i2 = 1;
        while (i2 < this.ROUNDS - 1) {
            byte[] bArr = S;
            int iMcol = mcol((((bArr[this.C0 & 255] & UByte.MAX_VALUE) ^ ((bArr[(this.C1 >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr[(this.C2 >> 16) & 255] & UByte.MAX_VALUE) << 16)) ^ (bArr[(this.C3 >> 24) & 255] << 24)) ^ iArr[i2][0];
            int iMcol2 = mcol((((bArr[this.C1 & 255] & UByte.MAX_VALUE) ^ ((bArr[(this.C2 >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr[(this.C3 >> 16) & 255] & UByte.MAX_VALUE) << 16)) ^ (bArr[(this.C0 >> 24) & 255] << 24)) ^ iArr[i2][1];
            int iMcol3 = mcol((((bArr[this.C2 & 255] & UByte.MAX_VALUE) ^ ((bArr[(this.C3 >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr[(this.C0 >> 16) & 255] & UByte.MAX_VALUE) << 16)) ^ (bArr[(this.C1 >> 24) & 255] << 24)) ^ iArr[i2][2];
            int i3 = i2 + 1;
            int iMcol4 = iArr[i2][3] ^ mcol((((bArr[this.C3 & 255] & UByte.MAX_VALUE) ^ ((bArr[(this.C0 >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr[(this.C1 >> 16) & 255] & UByte.MAX_VALUE) << 16)) ^ (bArr[(this.C2 >> 24) & 255] << 24));
            this.C0 = mcol((((bArr[iMcol & 255] & UByte.MAX_VALUE) ^ ((bArr[(iMcol2 >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr[(iMcol3 >> 16) & 255] & UByte.MAX_VALUE) << 16)) ^ (bArr[(iMcol4 >> 24) & 255] << 24)) ^ iArr[i3][0];
            this.C1 = mcol((((bArr[iMcol2 & 255] & UByte.MAX_VALUE) ^ ((bArr[(iMcol3 >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr[(iMcol4 >> 16) & 255] & UByte.MAX_VALUE) << 16)) ^ (bArr[(iMcol >> 24) & 255] << 24)) ^ iArr[i3][1];
            this.C2 = mcol((((bArr[iMcol3 & 255] & UByte.MAX_VALUE) ^ ((bArr[(iMcol4 >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr[(iMcol >> 16) & 255] & UByte.MAX_VALUE) << 16)) ^ (bArr[(iMcol2 >> 24) & 255] << 24)) ^ iArr[i3][2];
            this.C3 = mcol((((bArr[iMcol4 & 255] & UByte.MAX_VALUE) ^ ((bArr[(iMcol >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr[(iMcol2 >> 16) & 255] & UByte.MAX_VALUE) << 16)) ^ (bArr[(iMcol3 >> 24) & 255] << 24)) ^ iArr[i3][3];
            i2 = i3 + 1;
        }
        byte[] bArr2 = S;
        int iMcol5 = mcol((((bArr2[this.C0 & 255] & UByte.MAX_VALUE) ^ ((bArr2[(this.C1 >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr2[(this.C2 >> 16) & 255] & UByte.MAX_VALUE) << 16)) ^ (bArr2[(this.C3 >> 24) & 255] << 24)) ^ iArr[i2][0];
        int iMcol6 = mcol((((bArr2[this.C1 & 255] & UByte.MAX_VALUE) ^ ((bArr2[(this.C2 >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr2[(this.C3 >> 16) & 255] & UByte.MAX_VALUE) << 16)) ^ (bArr2[(this.C0 >> 24) & 255] << 24)) ^ iArr[i2][1];
        int iMcol7 = mcol((((bArr2[this.C2 & 255] & UByte.MAX_VALUE) ^ ((bArr2[(this.C3 >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr2[(this.C0 >> 16) & 255] & UByte.MAX_VALUE) << 16)) ^ (bArr2[(this.C1 >> 24) & 255] << 24)) ^ iArr[i2][2];
        int i4 = i2 + 1;
        int iMcol8 = iArr[i2][3] ^ mcol((((bArr2[this.C3 & 255] & UByte.MAX_VALUE) ^ ((bArr2[(this.C0 >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr2[(this.C1 >> 16) & 255] & UByte.MAX_VALUE) << 16)) ^ (bArr2[(this.C2 >> 24) & 255] << 24));
        int i5 = (((bArr2[iMcol5 & 255] & UByte.MAX_VALUE) ^ ((bArr2[(iMcol6 >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr2[(iMcol7 >> 16) & 255] & UByte.MAX_VALUE) << 16)) ^ (bArr2[(iMcol8 >> 24) & 255] << 24);
        int[] iArr3 = iArr[i4];
        this.C0 = iArr3[0] ^ i5;
        this.C1 = ((((bArr2[iMcol6 & 255] & UByte.MAX_VALUE) ^ ((bArr2[(iMcol7 >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr2[(iMcol8 >> 16) & 255] & UByte.MAX_VALUE) << 16)) ^ (bArr2[(iMcol5 >> 24) & 255] << 24)) ^ iArr3[1];
        this.C2 = ((((bArr2[iMcol7 & 255] & UByte.MAX_VALUE) ^ ((bArr2[(iMcol8 >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr2[(iMcol5 >> 16) & 255] & UByte.MAX_VALUE) << 16)) ^ (bArr2[(iMcol6 >> 24) & 255] << 24)) ^ iArr3[2];
        this.C3 = iArr3[3] ^ ((((bArr2[iMcol8 & 255] & UByte.MAX_VALUE) ^ ((bArr2[(iMcol5 >> 8) & 255] & UByte.MAX_VALUE) << 8)) ^ ((bArr2[(iMcol6 >> 16) & 255] & UByte.MAX_VALUE) << 16)) ^ (bArr2[(iMcol7 >> 24) & 255] << 24));
    }

    private int[][] generateWorkingKey(byte[] bArr, boolean z) {
        int length = bArr.length / 4;
        if ((length != 4 && length != 6 && length != 8) || length * 4 != bArr.length) {
            throw new IllegalArgumentException("Key length not 128/192/256 bits.");
        }
        int i = length + 6;
        this.ROUNDS = i;
        int[][] iArr = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, i + 1, 4);
        int i2 = 0;
        int i3 = 0;
        while (i2 < bArr.length) {
            iArr[i3 >> 2][i3 & 3] = (bArr[i2] & UByte.MAX_VALUE) | ((bArr[i2 + 1] & UByte.MAX_VALUE) << 8) | ((bArr[i2 + 2] & UByte.MAX_VALUE) << 16) | (bArr[i2 + 3] << 24);
            i2 += 4;
            i3++;
        }
        int i4 = (this.ROUNDS + 1) << 2;
        for (int i5 = length; i5 < i4; i5++) {
            int i6 = i5 - 1;
            int iSubWord = iArr[i6 >> 2][i6 & 3];
            int i7 = i5 % length;
            if (i7 == 0) {
                iSubWord = subWord(shift(iSubWord, 8)) ^ rcon[(i5 / length) - 1];
            } else if (length > 6 && i7 == 4) {
                iSubWord = subWord(iSubWord);
            }
            int i8 = i5 - length;
            iArr[i5 >> 2][i5 & 3] = iSubWord ^ iArr[i8 >> 2][i8 & 3];
        }
        if (!z) {
            for (int i9 = 1; i9 < this.ROUNDS; i9++) {
                for (int i10 = 0; i10 < 4; i10++) {
                    int[] iArr2 = iArr[i9];
                    iArr2[i10] = inv_mcol(iArr2[i10]);
                }
            }
        }
        return iArr;
    }

    private int inv_mcol(int i) {
        int iFFmulX = FFmulX(i);
        int iFFmulX2 = FFmulX(iFFmulX);
        int iFFmulX3 = FFmulX(iFFmulX2);
        int i2 = i ^ iFFmulX3;
        return shift(i2, 24) ^ ((shift(iFFmulX ^ i2, 8) ^ (iFFmulX3 ^ (iFFmulX ^ iFFmulX2))) ^ shift(iFFmulX2 ^ i2, 16));
    }

    private int mcol(int i) {
        int iFFmulX = FFmulX(i);
        return shift(i, 24) ^ ((iFFmulX ^ shift(i ^ iFFmulX, 8)) ^ shift(i, 16));
    }

    private void packBlock(byte[] bArr, int i) {
        int i2 = i + 1;
        int i3 = this.C0;
        bArr[i] = (byte) i3;
        int i4 = i2 + 1;
        bArr[i2] = (byte) (i3 >> 8);
        int i5 = i4 + 1;
        bArr[i4] = (byte) (i3 >> 16);
        int i6 = i5 + 1;
        bArr[i5] = (byte) (i3 >> 24);
        int i7 = i6 + 1;
        int i8 = this.C1;
        bArr[i6] = (byte) i8;
        int i9 = i7 + 1;
        bArr[i7] = (byte) (i8 >> 8);
        int i10 = i9 + 1;
        bArr[i9] = (byte) (i8 >> 16);
        int i11 = i10 + 1;
        bArr[i10] = (byte) (i8 >> 24);
        int i12 = i11 + 1;
        int i13 = this.C2;
        bArr[i11] = (byte) i13;
        int i14 = i12 + 1;
        bArr[i12] = (byte) (i13 >> 8);
        int i15 = i14 + 1;
        bArr[i14] = (byte) (i13 >> 16);
        int i16 = i15 + 1;
        bArr[i15] = (byte) (i13 >> 24);
        int i17 = i16 + 1;
        int i18 = this.C3;
        bArr[i16] = (byte) i18;
        int i19 = i17 + 1;
        bArr[i17] = (byte) (i18 >> 8);
        bArr[i19] = (byte) (i18 >> 16);
        bArr[i19 + 1] = (byte) (i18 >> 24);
    }

    private int shift(int i, int i2) {
        return (i << (-i2)) | (i >>> i2);
    }

    private int subWord(int i) {
        byte[] bArr = S;
        return (bArr[(i >> 24) & 255] << 24) | (bArr[i & 255] & UByte.MAX_VALUE) | ((bArr[(i >> 8) & 255] & UByte.MAX_VALUE) << 8) | ((bArr[(i >> 16) & 255] & UByte.MAX_VALUE) << 16);
    }

    private void unpackBlock(byte[] bArr, int i) {
        int i2 = i + 1;
        int i3 = i2 + 1;
        int i4 = (bArr[i] & UByte.MAX_VALUE) | ((bArr[i2] & UByte.MAX_VALUE) << 8);
        int i5 = i3 + 1;
        int i6 = i4 | ((bArr[i3] & UByte.MAX_VALUE) << 16);
        int i7 = i5 + 1;
        this.C0 = i6 | (bArr[i5] << 24);
        int i8 = i7 + 1;
        int i9 = bArr[i7] & UByte.MAX_VALUE;
        int i10 = i8 + 1;
        int i11 = ((bArr[i8] & UByte.MAX_VALUE) << 8) | i9;
        int i12 = i10 + 1;
        int i13 = i11 | ((bArr[i10] & UByte.MAX_VALUE) << 16);
        int i14 = i12 + 1;
        this.C1 = i13 | (bArr[i12] << 24);
        int i15 = i14 + 1;
        int i16 = bArr[i14] & UByte.MAX_VALUE;
        int i17 = i15 + 1;
        int i18 = ((bArr[i15] & UByte.MAX_VALUE) << 8) | i16;
        int i19 = i17 + 1;
        int i20 = i18 | ((bArr[i17] & UByte.MAX_VALUE) << 16);
        int i21 = i19 + 1;
        this.C2 = i20 | (bArr[i19] << 24);
        int i22 = i21 + 1;
        int i23 = bArr[i21] & UByte.MAX_VALUE;
        int i24 = i22 + 1;
        int i25 = ((bArr[i22] & UByte.MAX_VALUE) << 8) | i23;
        int i26 = i25 | ((bArr[i24] & UByte.MAX_VALUE) << 16);
        this.C3 = (bArr[i24 + 1] << 24) | i26;
    }

    @Override // org.bouncycastle.crypto.BlockCipher
    public String getAlgorithmName() {
        return "AES";
    }

    @Override // org.bouncycastle.crypto.BlockCipher
    public int getBlockSize() {
        return 16;
    }

    @Override // org.bouncycastle.crypto.BlockCipher
    public void init(boolean z, CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof KeyParameter)) {
            throw new IllegalArgumentException("invalid parameter passed to AES init - " + cipherParameters.getClass().getName());
        }
        this.WorkingKey = generateWorkingKey(((KeyParameter) cipherParameters).getKey(), z);
        this.forEncryption = z;
    }

    @Override // org.bouncycastle.crypto.BlockCipher
    public int processBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        if (this.WorkingKey == null) {
            throw new IllegalStateException("AES engine not initialised");
        }
        if (i + 16 > bArr.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (i2 + 16 > bArr2.length) {
            throw new DataLengthException("output buffer too short");
        }
        boolean z = this.forEncryption;
        unpackBlock(bArr, i);
        int[][] iArr = this.WorkingKey;
        if (z) {
            encryptBlock(iArr);
        } else {
            decryptBlock(iArr);
        }
        packBlock(bArr2, i2);
        return 16;
    }

    @Override // org.bouncycastle.crypto.BlockCipher
    public void reset() {
    }
}
