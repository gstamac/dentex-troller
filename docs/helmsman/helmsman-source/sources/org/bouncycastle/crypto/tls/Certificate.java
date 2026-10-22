package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.x509.X509CertificateStructure;

/* JADX INFO: loaded from: classes2.dex */
public class Certificate {
    protected X509CertificateStructure[] certs;

    private Certificate(X509CertificateStructure[] x509CertificateStructureArr) {
        this.certs = x509CertificateStructureArr;
    }

    protected static Certificate parse(InputStream inputStream) throws IOException {
        int uint24 = TlsUtils.readUint24(inputStream);
        Vector vector = new Vector();
        while (uint24 > 0) {
            int uint25 = TlsUtils.readUint24(inputStream);
            uint24 -= uint25 + 3;
            byte[] bArr = new byte[uint25];
            TlsUtils.readFully(bArr, inputStream);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
            vector.addElement(X509CertificateStructure.getInstance(new ASN1InputStream(byteArrayInputStream).readObject()));
            if (byteArrayInputStream.available() > 0) {
                throw new IllegalArgumentException("Sorry, there is garbage data left after the certificate");
            }
        }
        X509CertificateStructure[] x509CertificateStructureArr = new X509CertificateStructure[vector.size()];
        for (int i = 0; i < vector.size(); i++) {
            x509CertificateStructureArr[i] = (X509CertificateStructure) vector.elementAt(i);
        }
        return new Certificate(x509CertificateStructureArr);
    }

    public X509CertificateStructure[] getCerts() {
        X509CertificateStructure[] x509CertificateStructureArr = this.certs;
        X509CertificateStructure[] x509CertificateStructureArr2 = new X509CertificateStructure[x509CertificateStructureArr.length];
        System.arraycopy(x509CertificateStructureArr, 0, x509CertificateStructureArr2, 0, x509CertificateStructureArr.length);
        return x509CertificateStructureArr2;
    }
}
