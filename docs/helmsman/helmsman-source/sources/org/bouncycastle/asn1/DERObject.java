package org.bouncycastle.asn1;

import java.io.IOException;

/* JADX INFO: loaded from: classes2.dex */
public abstract class DERObject extends ASN1Encodable implements DERTags {
    abstract void encode(DEROutputStream dEROutputStream) throws IOException;

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public abstract boolean equals(Object obj);

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public abstract int hashCode();

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        return this;
    }
}
