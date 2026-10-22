package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.x509.X509CertificateStructure;

/* JADX INFO: loaded from: classes2.dex */
public class CMPCertificate extends ASN1Encodable implements ASN1Choice {
    private X509CertificateStructure x509v3PKCert;

    public CMPCertificate(X509CertificateStructure x509CertificateStructure) {
        if (x509CertificateStructure.getVersion() != 3) {
            throw new IllegalArgumentException("only version 3 certificates allowed");
        }
        this.x509v3PKCert = x509CertificateStructure;
    }

    public static CMPCertificate getInstance(Object obj) {
        if (obj instanceof CMPCertificate) {
            return (CMPCertificate) obj;
        }
        if (obj instanceof X509CertificateStructure) {
            return new CMPCertificate((X509CertificateStructure) obj);
        }
        if (obj instanceof ASN1Sequence) {
            return new CMPCertificate(X509CertificateStructure.getInstance(obj));
        }
        throw new IllegalArgumentException("Invalid object: " + obj.getClass().getName());
    }

    public X509CertificateStructure getX509v3PKCert() {
        return this.x509v3PKCert;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        return this.x509v3PKCert.toASN1Object();
    }
}
