package org.bouncycastle.crypto.tls;

import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.engines.DESedeEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;

/* JADX INFO: loaded from: classes2.dex */
public class TlsCipherSuiteManager {
    private static final int TLS_DHE_DSS_WITH_3DES_EDE_CBC_SHA = 19;
    private static final int TLS_DHE_DSS_WITH_AES_128_CBC_SHA = 50;
    private static final int TLS_DHE_DSS_WITH_AES_256_CBC_SHA = 56;
    private static final int TLS_DHE_RSA_WITH_3DES_EDE_CBC_SHA = 22;
    private static final int TLS_DHE_RSA_WITH_AES_128_CBC_SHA = 51;
    private static final int TLS_DHE_RSA_WITH_AES_256_CBC_SHA = 57;
    private static final int TLS_RSA_WITH_3DES_EDE_CBC_SHA = 10;
    private static final int TLS_RSA_WITH_AES_128_CBC_SHA = 47;
    private static final int TLS_RSA_WITH_AES_256_CBC_SHA = 53;

    private static CBCBlockCipher createAESCipher() {
        return new CBCBlockCipher(new AESFastEngine());
    }

    private static TlsCipherSuite createAESCipherSuite(int i, short s) {
        return new TlsBlockCipherCipherSuite(createAESCipher(), createAESCipher(), new SHA1Digest(), new SHA1Digest(), i, s);
    }

    private static CBCBlockCipher createDESedeCipher() {
        return new CBCBlockCipher(new DESedeEngine());
    }

    private static TlsCipherSuite createDESedeCipherSuite(int i, short s) {
        return new TlsBlockCipherCipherSuite(createDESedeCipher(), createDESedeCipher(), new SHA1Digest(), new SHA1Digest(), i, s);
    }

    protected static TlsCipherSuite getCipherSuite(int i, TlsProtocolHandler tlsProtocolHandler) throws IOException {
        if (i == 10) {
            return createDESedeCipherSuite(24, (short) 1);
        }
        if (i == 19) {
            return createDESedeCipherSuite(24, (short) 3);
        }
        if (i == 22) {
            return createDESedeCipherSuite(24, (short) 5);
        }
        if (i == 47) {
            return createAESCipherSuite(16, (short) 1);
        }
        if (i == 53) {
            return createAESCipherSuite(32, (short) 1);
        }
        if (i == 50) {
            return createAESCipherSuite(16, (short) 3);
        }
        if (i == 51) {
            return createAESCipherSuite(16, (short) 5);
        }
        if (i == 56) {
            return createAESCipherSuite(32, (short) 3);
        }
        if (i == 57) {
            return createAESCipherSuite(32, (short) 5);
        }
        tlsProtocolHandler.failWithError((short) 2, (short) 40);
        return null;
    }

    protected static void writeCipherSuites(OutputStream outputStream) throws IOException {
        int[] iArr = {57, 56, 51, 50, 22, 19, 53, 47, 10};
        TlsUtils.writeUint16(18, outputStream);
        for (int i = 0; i < 9; i++) {
            TlsUtils.writeUint16(iArr[i], outputStream);
        }
    }
}
