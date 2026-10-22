package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.signers.DSADigestSigner;
import org.bouncycastle.crypto.signers.DSASigner;

/* JADX INFO: loaded from: classes2.dex */
class TlsDSSSigner extends DSADigestSigner {
    TlsDSSSigner() {
        super(new DSASigner(), new SHA1Digest());
    }
}
