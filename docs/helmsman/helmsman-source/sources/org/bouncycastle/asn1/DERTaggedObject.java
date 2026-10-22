package org.bouncycastle.asn1;

import java.io.IOException;

/* JADX INFO: loaded from: classes2.dex */
public class DERTaggedObject extends ASN1TaggedObject {
    private static final byte[] ZERO_BYTES = new byte[0];

    public DERTaggedObject(int i) {
        super(false, i, new DERSequence());
    }

    public DERTaggedObject(int i, DEREncodable dEREncodable) {
        super(i, dEREncodable);
    }

    public DERTaggedObject(boolean z, int i, DEREncodable dEREncodable) {
        super(z, i, dEREncodable);
    }

    @Override // org.bouncycastle.asn1.ASN1TaggedObject, org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject
    void encode(DEROutputStream dEROutputStream) throws IOException {
        if (this.empty) {
            dEROutputStream.writeEncoded(160, this.tagNo, ZERO_BYTES);
            return;
        }
        byte[] encoded = this.obj.getDERObject().getEncoded(ASN1Encodable.DER);
        if (this.explicit) {
            dEROutputStream.writeEncoded(160, this.tagNo, encoded);
        } else {
            dEROutputStream.writeTag((encoded[0] & 32) == 0 ? 128 : 160, this.tagNo);
            dEROutputStream.write(encoded, 1, encoded.length - 1);
        }
    }
}
