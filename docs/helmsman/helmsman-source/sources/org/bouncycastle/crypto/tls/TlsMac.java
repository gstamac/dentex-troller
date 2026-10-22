package org.bouncycastle.crypto.tls;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;

/* JADX INFO: loaded from: classes2.dex */
public class TlsMac {
    private HMac mac;
    private long seqNo;

    protected TlsMac(Digest digest, byte[] bArr, int i, int i2) {
        this.mac = new HMac(digest);
        this.mac.init(new KeyParameter(bArr, i, i2));
        this.seqNo = 0L;
    }

    protected byte[] calculateMac(short s, byte[] bArr, int i, int i2) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            long j = this.seqNo;
            this.seqNo = 1 + j;
            TlsUtils.writeUint64(j, byteArrayOutputStream);
            TlsUtils.writeUint8(s, byteArrayOutputStream);
            TlsUtils.writeVersion(byteArrayOutputStream);
            TlsUtils.writeUint16(i2, byteArrayOutputStream);
            byteArrayOutputStream.write(bArr, i, i2);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            this.mac.update(byteArray, 0, byteArray.length);
            byte[] bArr2 = new byte[this.mac.getMacSize()];
            this.mac.doFinal(bArr2, 0);
            this.mac.reset();
            return bArr2;
        } catch (IOException unused) {
            throw new IllegalStateException("Internal error during mac calculation");
        }
    }

    protected int getSize() {
        return this.mac.getMacSize();
    }
}
