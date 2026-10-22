package org.bouncycastle.asn1.pkcs;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

/* JADX INFO: loaded from: classes2.dex */
public class EncryptionScheme extends AlgorithmIdentifier {
    DERObject obj;
    DERObject objectId;

    EncryptionScheme(ASN1Sequence aSN1Sequence) {
        super(aSN1Sequence);
        this.objectId = (DERObject) aSN1Sequence.getObjectAt(0);
        this.obj = (DERObject) aSN1Sequence.getObjectAt(1);
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable, org.bouncycastle.asn1.DEREncodable
    public DERObject getDERObject() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.objectId);
        aSN1EncodableVector.add(this.obj);
        return new DERSequence(aSN1EncodableVector);
    }

    public DERObject getObject() {
        return this.obj;
    }
}
