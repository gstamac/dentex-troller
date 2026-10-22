package org.bouncycastle.util.encoders;

import java.io.IOException;
import java.io.OutputStream;
import kotlin.UByte;

/* JADX INFO: loaded from: classes2.dex */
public class Base64Encoder implements Encoder {
    protected final byte[] encodingTable = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
    protected byte padding = kotlin.io.encoding.Base64.padSymbol;
    protected final byte[] decodingTable = new byte[128];

    public Base64Encoder() {
        initialiseDecodingTable();
    }

    private int decodeLastBlock(OutputStream outputStream, char c, char c2, char c3, char c4) throws IOException {
        char c5 = this.padding;
        if (c3 == c5) {
            byte[] bArr = this.decodingTable;
            outputStream.write((bArr[c] << 2) | (bArr[c2] >> 4));
            return 1;
        }
        if (c4 == c5) {
            byte[] bArr2 = this.decodingTable;
            byte b = bArr2[c];
            byte b2 = bArr2[c2];
            byte b3 = bArr2[c3];
            outputStream.write((b << 2) | (b2 >> 4));
            outputStream.write((b2 << 4) | (b3 >> 2));
            return 2;
        }
        byte[] bArr3 = this.decodingTable;
        byte b4 = bArr3[c];
        byte b5 = bArr3[c2];
        byte b6 = bArr3[c3];
        byte b7 = bArr3[c4];
        outputStream.write((b4 << 2) | (b5 >> 4));
        outputStream.write((b5 << 4) | (b6 >> 2));
        outputStream.write((b6 << 6) | b7);
        return 3;
    }

    private boolean ignore(char c) {
        return c == '\n' || c == '\r' || c == '\t' || c == ' ';
    }

    private int nextI(String str, int i, int i2) {
        while (i < i2 && ignore(str.charAt(i))) {
            i++;
        }
        return i;
    }

    private int nextI(byte[] bArr, int i, int i2) {
        while (i < i2 && ignore((char) bArr[i])) {
            i++;
        }
        return i;
    }

    @Override // org.bouncycastle.util.encoders.Encoder
    public int decode(String str, OutputStream outputStream) throws IOException {
        int length = str.length();
        while (length > 0 && ignore(str.charAt(length - 1))) {
            length--;
        }
        int i = length - 4;
        int i2 = 0;
        int iNextI = nextI(str, 0, i);
        while (iNextI < i) {
            int i3 = iNextI + 1;
            byte b = this.decodingTable[str.charAt(iNextI)];
            int iNextI2 = nextI(str, i3, i);
            int i4 = iNextI2 + 1;
            byte b2 = this.decodingTable[str.charAt(iNextI2)];
            int iNextI3 = nextI(str, i4, i);
            int i5 = iNextI3 + 1;
            byte b3 = this.decodingTable[str.charAt(iNextI3)];
            int iNextI4 = nextI(str, i5, i);
            int i6 = iNextI4 + 1;
            byte b4 = this.decodingTable[str.charAt(iNextI4)];
            outputStream.write((b << 2) | (b2 >> 4));
            outputStream.write((b2 << 4) | (b3 >> 2));
            outputStream.write((b3 << 6) | b4);
            i2 += 3;
            iNextI = nextI(str, i6, i);
        }
        return i2 + decodeLastBlock(outputStream, str.charAt(i), str.charAt(length - 3), str.charAt(length - 2), str.charAt(length - 1));
    }

    @Override // org.bouncycastle.util.encoders.Encoder
    public int decode(byte[] bArr, int i, int i2, OutputStream outputStream) throws IOException {
        int i3 = i2 + i;
        while (i3 > i && ignore((char) bArr[i3 - 1])) {
            i3--;
        }
        int i4 = i3 - 4;
        int iNextI = nextI(bArr, i, i4);
        int i5 = 0;
        while (iNextI < i4) {
            int i6 = iNextI + 1;
            byte b = this.decodingTable[bArr[iNextI]];
            int iNextI2 = nextI(bArr, i6, i4);
            int i7 = iNextI2 + 1;
            byte b2 = this.decodingTable[bArr[iNextI2]];
            int iNextI3 = nextI(bArr, i7, i4);
            int i8 = iNextI3 + 1;
            byte b3 = this.decodingTable[bArr[iNextI3]];
            int iNextI4 = nextI(bArr, i8, i4);
            int i9 = iNextI4 + 1;
            byte b4 = this.decodingTable[bArr[iNextI4]];
            outputStream.write((b << 2) | (b2 >> 4));
            outputStream.write((b2 << 4) | (b3 >> 2));
            outputStream.write((b3 << 6) | b4);
            i5 += 3;
            iNextI = nextI(bArr, i9, i4);
        }
        return i5 + decodeLastBlock(outputStream, (char) bArr[i4], (char) bArr[i3 - 3], (char) bArr[i3 - 2], (char) bArr[i3 - 1]);
    }

    /* JADX WARN: Code duplicated, block: B:15:0x00a3  */
    @Override // org.bouncycastle.util.encoders.Encoder
    public int encode(byte[] bArr, int i, int i2, OutputStream outputStream) throws IOException {
        int i3;
        byte b;
        int i4 = i2 % 3;
        int i5 = i2 - i4;
        int i6 = i;
        while (true) {
            i3 = i + i5;
            if (i6 >= i3) {
                break;
            }
            int i7 = bArr[i6] & UByte.MAX_VALUE;
            int i8 = bArr[i6 + 1] & UByte.MAX_VALUE;
            int i9 = bArr[i6 + 2] & UByte.MAX_VALUE;
            outputStream.write(this.encodingTable[(i7 >>> 2) & 63]);
            outputStream.write(this.encodingTable[((i7 << 4) | (i8 >>> 4)) & 63]);
            outputStream.write(this.encodingTable[((i8 << 2) | (i9 >>> 6)) & 63]);
            outputStream.write(this.encodingTable[i9 & 63]);
            i6 += 3;
        }
        if (i4 != 1) {
            if (i4 == 2) {
                int i10 = bArr[i3] & UByte.MAX_VALUE;
                int i11 = bArr[i3 + 1] & UByte.MAX_VALUE;
                outputStream.write(this.encodingTable[(i10 >>> 2) & 63]);
                outputStream.write(this.encodingTable[((i10 << 4) | (i11 >>> 4)) & 63]);
                b = this.encodingTable[(i11 << 2) & 63];
            }
            return ((i5 / 3) * 4) + (i4 == 0 ? 0 : 4);
        }
        int i12 = bArr[i3] & UByte.MAX_VALUE;
        outputStream.write(this.encodingTable[(i12 >>> 2) & 63]);
        outputStream.write(this.encodingTable[(i12 << 4) & 63]);
        b = this.padding;
        outputStream.write(b);
        outputStream.write(this.padding);
        return ((i5 / 3) * 4) + (i4 == 0 ? 0 : 4);
    }

    protected void initialiseDecodingTable() {
        int i = 0;
        while (true) {
            byte[] bArr = this.encodingTable;
            if (i >= bArr.length) {
                return;
            }
            this.decodingTable[bArr[i]] = (byte) i;
            i++;
        }
    }
}
