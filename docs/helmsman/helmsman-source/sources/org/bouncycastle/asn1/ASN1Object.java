package org.bouncycastle.asn1;

import java.io.IOException;

/* JADX INFO: loaded from: classes2.dex */
public abstract class ASN1Object extends DERObject {
    public static ASN1Object fromByteArray(byte[] bArr) throws IOException {
        return (ASN1Object) new ASN1InputStream(bArr).readObject();
    }

    abstract boolean asn1Equals(DERObject dERObject);

    @Override // org.bouncycastle.asn1.DERObject
    abstract void encode(DEROutputStream dEROutputStream) throws IOException;

    @Override // org.bouncycastle.asn1.DERObject, org.bouncycastle.asn1.ASN1Encodable
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof DEREncodable) && asn1Equals(((DEREncodable) obj).getDERObject());
    }

    @Override // org.bouncycastle.asn1.DERObject, org.bouncycastle.asn1.ASN1Encodable
    public abstract int hashCode();
}
