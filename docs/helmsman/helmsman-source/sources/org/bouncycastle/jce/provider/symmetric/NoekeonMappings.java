package org.bouncycastle.jce.provider.symmetric;

import java.util.HashMap;

/* JADX INFO: loaded from: classes2.dex */
public class NoekeonMappings extends HashMap {
    public NoekeonMappings() {
        put("AlgorithmParameters.NOEKEON", "org.bouncycastle.jce.provider.symmetric.Noekeon$AlgParams");
        put("AlgorithmParameterGenerator.NOEKEON", "org.bouncycastle.jce.provider.symmetric.Noekeon$AlgParamGen");
        put("Cipher.NOEKEON", "org.bouncycastle.jce.provider.symmetric.Noekeon$ECB");
        put("KeyGenerator.NOEKEON", "org.bouncycastle.jce.provider.symmetric.Noekeon$KeyGen");
    }
}
