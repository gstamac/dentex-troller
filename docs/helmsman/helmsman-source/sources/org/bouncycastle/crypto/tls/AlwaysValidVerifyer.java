package org.bouncycastle.crypto.tls;

import org.bouncycastle.asn1.x509.X509CertificateStructure;

/* JADX INFO: loaded from: classes2.dex */
public class AlwaysValidVerifyer implements CertificateVerifyer {
    @Override // org.bouncycastle.crypto.tls.CertificateVerifyer
    public boolean isValid(X509CertificateStructure[] x509CertificateStructureArr) {
        return true;
    }
}
