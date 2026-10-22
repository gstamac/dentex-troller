package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;

/* JADX INFO: loaded from: classes2.dex */
public class DERSequence extends ASN1Sequence {
    public DERSequence() {
    }

    public DERSequence(DEREncodable dEREncodable) {
        addObject(dEREncodable);
    }

    public DERSequence(DEREncodableVector dEREncodableVector) {
        for (int i = 0; i != dEREncodableVector.size(); i++) {
            addObject(dEREncodableVector.get(i));
        }
    }

    public DERSequence(ASN1Encodable[] aSN1EncodableArr) {
        for (int i = 0; i != aSN1EncodableArr.length; i++) {
            addObject(aSN1EncodableArr[i]);
        }
    }

    @Override // org.bouncycastle.asn1.ASN1Sequence, org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject
    void encode(DEROutputStream dEROutputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DEROutputStream dEROutputStream2 = new DEROutputStream(byteArrayOutputStream);
        Enumeration objects = getObjects();
        while (objects.hasMoreElements()) {
            dEROutputStream2.writeObject(objects.nextElement());
        }
        dEROutputStream2.close();
        dEROutputStream.writeEncoded(48, byteArrayOutputStream.toByteArray());
    }
}
