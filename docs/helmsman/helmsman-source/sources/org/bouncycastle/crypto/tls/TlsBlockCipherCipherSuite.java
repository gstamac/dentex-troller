package org.bouncycastle.crypto.tls;

import java.io.IOException;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

/* JADX INFO: loaded from: classes2.dex */
public class TlsBlockCipherCipherSuite extends TlsCipherSuite {
    private int cipherKeySize;
    private BlockCipher decryptCipher;
    private BlockCipher encryptCipher;
    private short keyExchange;
    private Digest readDigest;
    private TlsMac readMac;
    private Digest writeDigest;
    private TlsMac writeMac;

    protected TlsBlockCipherCipherSuite(BlockCipher blockCipher, BlockCipher blockCipher2, Digest digest, Digest digest2, int i, short s) {
        this.encryptCipher = blockCipher;
        this.decryptCipher = blockCipher2;
        this.writeDigest = digest;
        this.readDigest = digest2;
        this.cipherKeySize = i;
        this.keyExchange = s;
    }

    private void initCipher(boolean z, BlockCipher blockCipher, byte[] bArr, int i, int i2, int i3) {
        blockCipher.init(z, new ParametersWithIV(new KeyParameter(bArr, i2, i), bArr, i3, blockCipher.getBlockSize()));
    }

    @Override // org.bouncycastle.crypto.tls.TlsCipherSuite
    protected byte[] decodeCiphertext(short s, byte[] bArr, int i, int i2, TlsProtocolHandler tlsProtocolHandler) throws IOException {
        boolean z;
        int blockSize = this.decryptCipher.getBlockSize();
        for (int i3 = 0; i3 < i2; i3 += blockSize) {
            int i4 = i3 + i;
            this.decryptCipher.processBlock(bArr, i4, bArr, i4);
        }
        int i5 = (i + i2) - 1;
        byte b = bArr[i5];
        if (i5 - b < 0) {
            b = 0;
            z = true;
        } else {
            z = false;
            for (int i6 = 0; i6 <= b; i6++) {
                if (bArr[i5 - i6] != b) {
                    z = true;
                }
            }
        }
        int size = ((i2 - this.readMac.getSize()) - b) - 1;
        byte[] bArrCalculateMac = this.readMac.calculateMac(s, bArr, i, size);
        for (int i7 = 0; i7 < bArrCalculateMac.length; i7++) {
            if (bArr[i + size + i7] != bArrCalculateMac[i7]) {
                z = true;
            }
        }
        if (z) {
            tlsProtocolHandler.failWithError((short) 2, (short) 20);
        }
        byte[] bArr2 = new byte[size];
        System.arraycopy(bArr, i, bArr2, 0, size);
        return bArr2;
    }

    @Override // org.bouncycastle.crypto.tls.TlsCipherSuite
    protected byte[] encodePlaintext(short s, byte[] bArr, int i, int i2) {
        int blockSize = this.encryptCipher.getBlockSize();
        int size = blockSize - (((this.writeMac.getSize() + i2) + 1) % blockSize);
        int size2 = this.writeMac.getSize() + i2 + size + 1;
        byte[] bArr2 = new byte[size2];
        System.arraycopy(bArr, i, bArr2, 0, i2);
        byte[] bArrCalculateMac = this.writeMac.calculateMac(s, bArr, i, i2);
        System.arraycopy(bArrCalculateMac, 0, bArr2, i2, bArrCalculateMac.length);
        int length = i2 + bArrCalculateMac.length;
        for (int i3 = 0; i3 <= size; i3++) {
            bArr2[i3 + length] = (byte) size;
        }
        for (int i4 = 0; i4 < size2; i4 += blockSize) {
            this.encryptCipher.processBlock(bArr2, i4, bArr2, i4);
        }
        return bArr2;
    }

    @Override // org.bouncycastle.crypto.tls.TlsCipherSuite
    protected short getKeyExchangeAlgorithm() {
        return this.keyExchange;
    }

    @Override // org.bouncycastle.crypto.tls.TlsCipherSuite
    protected void init(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        byte[] bArr4 = new byte[(this.cipherKeySize * 2) + (this.writeDigest.getDigestSize() * 2) + (this.encryptCipher.getBlockSize() * 2)];
        byte[] bArr5 = new byte[bArr2.length + bArr3.length];
        System.arraycopy(bArr2, 0, bArr5, bArr3.length, bArr2.length);
        System.arraycopy(bArr3, 0, bArr5, 0, bArr3.length);
        TlsUtils.PRF(bArr, TlsUtils.toByteArray("key expansion"), bArr5, bArr4);
        Digest digest = this.writeDigest;
        this.writeMac = new TlsMac(digest, bArr4, 0, digest.getDigestSize());
        int digestSize = this.writeDigest.getDigestSize() + 0;
        Digest digest2 = this.readDigest;
        this.readMac = new TlsMac(digest2, bArr4, digestSize, digest2.getDigestSize());
        int digestSize2 = digestSize + this.readDigest.getDigestSize();
        BlockCipher blockCipher = this.encryptCipher;
        int i = this.cipherKeySize;
        initCipher(true, blockCipher, bArr4, i, digestSize2, digestSize2 + (i * 2));
        int i2 = this.cipherKeySize;
        int i3 = digestSize2 + i2;
        BlockCipher blockCipher2 = this.decryptCipher;
        initCipher(false, blockCipher2, bArr4, i2, i3, i3 + i2 + blockCipher2.getBlockSize());
    }
}
