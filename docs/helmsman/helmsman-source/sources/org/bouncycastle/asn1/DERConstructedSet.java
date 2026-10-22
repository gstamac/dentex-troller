package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;

/* JADX INFO: loaded from: classes2.dex */
public class DERConstructedSet extends ASN1Set {
    public DERConstructedSet() {
    }

    public DERConstructedSet(DEREncodable dEREncodable) {
        addObject(dEREncodable);
    }

    public DERConstructedSet(DEREncodableVector dEREncodableVector) {
        for (int i = 0; i != dEREncodableVector.size(); i++) {
            addObject(dEREncodableVector.get(i));
        }
    }

    @Override // org.bouncycastle.asn1.ASN1Set
    public void addObject(DEREncodable dEREncodable) {
        super.addObject(dEREncodable);
    }

    @Override // org.bouncycastle.asn1.ASN1Set, org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject
    void encode(DEROutputStream dEROutputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DEROutputStream dEROutputStream2 = new DEROutputStream(byteArrayOutputStream);
        Enumeration objects = getObjects();
        while (objects.hasMoreElements()) {
            dEROutputStream2.writeObject(objects.nextElement());
        }
        dEROutputStream2.close();
        dEROutputStream.writeEncoded(49, byteArrayOutputStream.toByteArray());
    }

    public int getSize() {
        return size();
    }
}
