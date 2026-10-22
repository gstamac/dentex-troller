package org.bouncycastle.crypto.tls;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes2.dex */
public class RecordStream {
    private TlsProtocolHandler handler;
    protected CombinedHash hash1 = new CombinedHash();
    protected CombinedHash hash2 = new CombinedHash();
    private InputStream is;
    private OutputStream os;
    protected TlsCipherSuite readSuite;
    protected TlsCipherSuite writeSuite;

    protected RecordStream(TlsProtocolHandler tlsProtocolHandler, InputStream inputStream, OutputStream outputStream) {
        this.readSuite = null;
        this.writeSuite = null;
        this.handler = tlsProtocolHandler;
        this.is = inputStream;
        this.os = outputStream;
        TlsNullCipherSuite tlsNullCipherSuite = new TlsNullCipherSuite();
        this.readSuite = tlsNullCipherSuite;
        this.writeSuite = tlsNullCipherSuite;
    }

    protected void close() throws IOException {
        try {
            this.is.close();
            e = null;
        } catch (IOException e) {
            e = e;
        }
        try {
            this.os.close();
        } catch (IOException e2) {
            e = e2;
        }
        if (e != null) {
            throw e;
        }
    }

    protected byte[] decodeAndVerify(short s, InputStream inputStream, int i) throws IOException {
        byte[] bArr = new byte[i];
        TlsUtils.readFully(bArr, inputStream);
        return this.readSuite.decodeCiphertext(s, bArr, 0, i, this.handler);
    }

    protected void flush() throws IOException {
        this.os.flush();
    }

    public void readData() throws IOException {
        short uint8 = TlsUtils.readUint8(this.is);
        TlsUtils.checkVersion(this.is, this.handler);
        byte[] bArrDecodeAndVerify = decodeAndVerify(uint8, this.is, TlsUtils.readUint16(this.is));
        this.handler.processData(uint8, bArrDecodeAndVerify, 0, bArrDecodeAndVerify.length);
    }

    protected void writeMessage(short s, byte[] bArr, int i, int i2) throws IOException {
        if (s == 22) {
            this.hash1.update(bArr, i, i2);
            this.hash2.update(bArr, i, i2);
        }
        byte[] bArrEncodePlaintext = this.writeSuite.encodePlaintext(s, bArr, i, i2);
        byte[] bArr2 = new byte[bArrEncodePlaintext.length + 5];
        TlsUtils.writeUint8(s, bArr2, 0);
        TlsUtils.writeUint8((short) 3, bArr2, 1);
        TlsUtils.writeUint8((short) 1, bArr2, 2);
        TlsUtils.writeUint16(bArrEncodePlaintext.length, bArr2, 3);
        System.arraycopy(bArrEncodePlaintext, 0, bArr2, 5, bArrEncodePlaintext.length);
        this.os.write(bArr2);
        this.os.flush();
    }
}
