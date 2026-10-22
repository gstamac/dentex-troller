package org.bouncycastle.jce.provider.symmetric;

import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.engines.Grainv1Engine;
import org.bouncycastle.jce.provider.JCEKeyGenerator;
import org.bouncycastle.jce.provider.JCEStreamCipher;

/* JADX INFO: loaded from: classes2.dex */
public final class Grainv1 {

    public static class Base extends JCEStreamCipher {
        public Base() {
            super(new Grainv1Engine(), 8);
        }
    }

    public static class KeyGen extends JCEKeyGenerator {
        public KeyGen() {
            super("Grainv1", 80, new CipherKeyGenerator());
        }
    }

    private Grainv1() {
    }
}
