package org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;

/* JADX INFO: loaded from: classes2.dex */
public class BERSet extends DERSet {
    public BERSet() {
    }

    public BERSet(DEREncodable dEREncodable) {
        super(dEREncodable);
    }

    public BERSet(DEREncodableVector dEREncodableVector) {
        super(dEREncodableVector, false);
    }

    BERSet(DEREncodableVector dEREncodableVector, boolean z) {
        super(dEREncodableVector, z);
    }

    @Override // org.bouncycastle.asn1.DERSet, org.bouncycastle.asn1.ASN1Set, org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject
    void encode(DEROutputStream dEROutputStream) throws IOException {
        if (!(dEROutputStream instanceof ASN1OutputStream) && !(dEROutputStream instanceof BEROutputStream)) {
            super.encode(dEROutputStream);
            return;
        }
        dEROutputStream.write(49);
        dEROutputStream.write(128);
        Enumeration objects = getObjects();
        while (objects.hasMoreElements()) {
            dEROutputStream.writeObject(objects.nextElement());
        }
        dEROutputStream.write(0);
        dEROutputStream.write(0);
    }
}
