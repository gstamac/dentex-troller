package org.bouncycastle.crypto.tls;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;

/* JADX INFO: loaded from: classes2.dex */
public class TlsUtils {
    protected static void PRF(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4) {
        int length = (bArr.length + 1) / 2;
        byte[] bArr5 = new byte[length];
        byte[] bArr6 = new byte[length];
        System.arraycopy(bArr, 0, bArr5, 0, length);
        System.arraycopy(bArr, bArr.length - length, bArr6, 0, length);
        byte[] bArr7 = new byte[bArr2.length + bArr3.length];
        System.arraycopy(bArr2, 0, bArr7, 0, bArr2.length);
        System.arraycopy(bArr3, 0, bArr7, bArr2.length, bArr3.length);
        byte[] bArr8 = new byte[bArr4.length];
        hmac_hash(new MD5Digest(), bArr5, bArr7, bArr8);
        hmac_hash(new SHA1Digest(), bArr6, bArr7, bArr4);
        for (int i = 0; i < bArr4.length; i++) {
            bArr4[i] = (byte) (bArr4[i] ^ bArr8[i]);
        }
    }

    protected static void checkVersion(InputStream inputStream, TlsProtocolHandler tlsProtocolHandler) throws IOException {
        int i = inputStream.read();
        int i2 = inputStream.read();
        if (i == 3 && i2 == 1) {
            return;
        }
        tlsProtocolHandler.failWithError((short) 2, (short) 70);
    }

    protected static void checkVersion(byte[] bArr, TlsProtocolHandler tlsProtocolHandler) throws IOException {
        if (bArr[0] == 3 && bArr[1] == 1) {
            return;
        }
        tlsProtocolHandler.failWithError((short) 2, (short) 70);
    }

    private static void hmac_hash(Digest digest, byte[] bArr, byte[] bArr2, byte[] bArr3) {
        HMac hMac = new HMac(digest);
        KeyParameter keyParameter = new KeyParameter(bArr);
        int digestSize = digest.getDigestSize();
        int length = ((bArr3.length + digestSize) - 1) / digestSize;
        int macSize = hMac.getMacSize();
        byte[] bArr4 = new byte[macSize];
        byte[] bArr5 = new byte[hMac.getMacSize()];
        byte[] bArr6 = bArr2;
        int i = 0;
        while (i < length) {
            hMac.init(keyParameter);
            hMac.update(bArr6, 0, bArr6.length);
            hMac.doFinal(bArr4, 0);
            hMac.init(keyParameter);
            hMac.update(bArr4, 0, macSize);
            hMac.update(bArr2, 0, bArr2.length);
            hMac.doFinal(bArr5, 0);
            int i2 = digestSize * i;
            System.arraycopy(bArr5, 0, bArr3, i2, Math.min(digestSize, bArr3.length - i2));
            i++;
            bArr6 = bArr4;
        }
    }

    protected static void readFully(byte[] bArr, InputStream inputStream) throws IOException {
        int i = 0;
        while (i != bArr.length) {
            int i2 = inputStream.read(bArr, i, bArr.length - i);
            if (i2 == -1) {
                throw new EOFException();
            }
            i += i2;
        }
    }

    protected static byte[] readOpaque16(InputStream inputStream) throws IOException {
        byte[] bArr = new byte[readUint16(inputStream)];
        readFully(bArr, inputStream);
        return bArr;
    }

    protected static byte[] readOpaque8(InputStream inputStream) throws IOException {
        byte[] bArr = new byte[readUint8(inputStream)];
        readFully(bArr, inputStream);
        return bArr;
    }

    protected static int readUint16(InputStream inputStream) throws IOException {
        int i = inputStream.read();
        int i2 = inputStream.read();
        if ((i | i2) >= 0) {
            return i2 | (i << 8);
        }
        throw new EOFException();
    }

    protected static int readUint24(InputStream inputStream) throws IOException {
        int i = inputStream.read();
        int i2 = inputStream.read();
        int i3 = inputStream.read();
        if ((i | i2 | i3) >= 0) {
            return i3 | (i << 16) | (i2 << 8);
        }
        throw new EOFException();
    }

    protected static long readUint32(InputStream inputStream) throws IOException {
        int i = inputStream.read();
        int i2 = inputStream.read();
        int i3 = inputStream.read();
        int i4 = inputStream.read();
        if ((i | i2 | i3 | i4) < 0) {
            throw new EOFException();
        }
        return (((long) i2) << 16) | (((long) i) << 24) | (((long) i3) << 8) | ((long) i4);
    }

    protected static short readUint8(InputStream inputStream) throws IOException {
        int i = inputStream.read();
        if (i != -1) {
            return (short) i;
        }
        throw new EOFException();
    }

    static byte[] toByteArray(String str) {
        char[] charArray = str.toCharArray();
        int length = charArray.length;
        byte[] bArr = new byte[length];
        for (int i = 0; i != length; i++) {
            bArr[i] = (byte) charArray[i];
        }
        return bArr;
    }

    protected static void writeOpaque16(byte[] bArr, OutputStream outputStream) throws IOException {
        writeUint16(bArr.length, outputStream);
        outputStream.write(bArr);
    }

    protected static void writeOpaque8(byte[] bArr, OutputStream outputStream) throws IOException {
        writeUint8((short) bArr.length, outputStream);
        outputStream.write(bArr);
    }

    protected static void writeUint16(int i, OutputStream outputStream) throws IOException {
        outputStream.write(i >> 8);
        outputStream.write(i);
    }

    protected static void writeUint16(int i, byte[] bArr, int i2) {
        bArr[i2] = (byte) (i >> 8);
        bArr[i2 + 1] = (byte) i;
    }

    protected static void writeUint24(int i, OutputStream outputStream) throws IOException {
        outputStream.write(i >> 16);
        outputStream.write(i >> 8);
        outputStream.write(i);
    }

    protected static void writeUint24(int i, byte[] bArr, int i2) {
        bArr[i2] = (byte) (i >> 16);
        bArr[i2 + 1] = (byte) (i >> 8);
        bArr[i2 + 2] = (byte) i;
    }

    protected static void writeUint32(long j, OutputStream outputStream) throws IOException {
        outputStream.write((int) (j >> 24));
        outputStream.write((int) (j >> 16));
        outputStream.write((int) (j >> 8));
        outputStream.write((int) j);
    }

    protected static void writeUint32(long j, byte[] bArr, int i) {
        bArr[i] = (byte) (j >> 24);
        bArr[i + 1] = (byte) (j >> 16);
        bArr[i + 2] = (byte) (j >> 8);
        bArr[i + 3] = (byte) j;
    }

    protected static void writeUint64(long j, OutputStream outputStream) throws IOException {
        outputStream.write((int) (j >> 56));
        outputStream.write((int) (j >> 48));
        outputStream.write((int) (j >> 40));
        outputStream.write((int) (j >> 32));
        outputStream.write((int) (j >> 24));
        outputStream.write((int) (j >> 16));
        outputStream.write((int) (j >> 8));
        outputStream.write((int) j);
    }

    protected static void writeUint64(long j, byte[] bArr, int i) {
        bArr[i] = (byte) (j >> 56);
        bArr[i + 1] = (byte) (j >> 48);
        bArr[i + 2] = (byte) (j >> 40);
        bArr[i + 3] = (byte) (j >> 32);
        bArr[i + 4] = (byte) (j >> 24);
        bArr[i + 5] = (byte) (j >> 16);
        bArr[i + 6] = (byte) (j >> 8);
        bArr[i + 7] = (byte) j;
    }

    protected static void writeUint8(short s, OutputStream outputStream) throws IOException {
        outputStream.write(s);
    }

    protected static void writeUint8(short s, byte[] bArr, int i) {
        bArr[i] = (byte) s;
    }

    protected static void writeVersion(OutputStream outputStream) throws IOException {
        outputStream.write(3);
        outputStream.write(1);
    }
}
