package org.bouncycastle.asn1.x509;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DERBoolean;

/* JADX INFO: loaded from: classes2.dex */
public class X509Extension {
    boolean critical;
    ASN1OctetString value;

    public X509Extension(DERBoolean dERBoolean, ASN1OctetString aSN1OctetString) {
        this.critical = dERBoolean.isTrue();
        this.value = aSN1OctetString;
    }

    public X509Extension(boolean z, ASN1OctetString aSN1OctetString) {
        this.critical = z;
        this.value = aSN1OctetString;
    }

    public static ASN1Object convertValueToObject(X509Extension x509Extension) throws IllegalArgumentException {
        try {
            return ASN1Object.fromByteArray(x509Extension.getValue().getOctets());
        } catch (IOException e) {
            throw new IllegalArgumentException("can't convert extension: " + e);
        }
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof X509Extension)) {
            return false;
        }
        X509Extension x509Extension = (X509Extension) obj;
        return x509Extension.getValue().equals(getValue()) && x509Extension.isCritical() == isCritical();
    }

    public ASN1OctetString getValue() {
        return this.value;
    }

    public int hashCode() {
        return isCritical() ? getValue().hashCode() : ~getValue().hashCode();
    }

    public boolean isCritical() {
        return this.critical;
    }
}
