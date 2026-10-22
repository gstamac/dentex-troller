package org.bouncycastle.x509.extension;

import java.io.IOException;
import java.security.PublicKey;
import java.security.cert.CertificateParsingException;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

/* JADX INFO: loaded from: classes2.dex */
public class SubjectKeyIdentifierStructure extends SubjectKeyIdentifier {
    private AuthorityKeyIdentifier authKeyID;

    public SubjectKeyIdentifierStructure(PublicKey publicKey) throws CertificateParsingException {
        super(fromPublicKey(publicKey));
    }

    public SubjectKeyIdentifierStructure(byte[] bArr) throws IOException {
        super((ASN1OctetString) X509ExtensionUtil.fromExtensionValue(bArr));
    }

    private static ASN1OctetString fromPublicKey(PublicKey publicKey) throws CertificateParsingException {
        try {
            ASN1OctetString aSN1OctetString = (ASN1OctetString) new SubjectKeyIdentifier(new SubjectPublicKeyInfo((ASN1Sequence) new ASN1InputStream(publicKey.getEncoded()).readObject())).toASN1Object();
            return aSN1OctetString;
        } catch (Exception e) {
            throw new CertificateParsingException("Exception extracting certificate details: " + e.toString());
        }
    }
}
