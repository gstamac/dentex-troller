package org.bouncycastle.crypto.modes.gcm;

import org.bouncycastle.util.Arrays;

/* JADX INFO: loaded from: classes2.dex */
public class BasicGCMMultiplier implements GCMMultiplier {
    private byte[] H;

    @Override // org.bouncycastle.crypto.modes.gcm.GCMMultiplier
    public void init(byte[] bArr) {
        this.H = Arrays.clone(bArr);
    }

    @Override // org.bouncycastle.crypto.modes.gcm.GCMMultiplier
    public void multiplyH(byte[] bArr) {
        byte[] bArr2 = new byte[16];
        for (int i = 0; i < 16; i++) {
            byte b = this.H[i];
            for (int i2 = 7; i2 >= 0; i2--) {
                if (((1 << i2) & b) != 0) {
                    GCMUtil.xor(bArr2, bArr);
                }
                boolean z = (bArr[15] & 1) != 0;
                GCMUtil.shiftRight(bArr);
                if (z) {
                    bArr[0] = (byte) (bArr[0] ^ (-31));
                }
            }
        }
        System.arraycopy(bArr2, 0, bArr, 0, 16);
    }
}
