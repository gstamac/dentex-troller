package org.bouncycastle.asn1.smime;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;

/* JADX INFO: loaded from: classes2.dex */
public class SMIMECapabilityVector {
    private ASN1EncodableVector capabilities = new ASN1EncodableVector();

    public void addCapability(DERObjectIdentifier dERObjectIdentifier) {
        this.capabilities.add(new DERSequence(dERObjectIdentifier));
    }

    public void addCapability(DERObjectIdentifier dERObjectIdentifier, int i) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(dERObjectIdentifier);
        aSN1EncodableVector.add(new DERInteger(i));
        this.capabilities.add(new DERSequence(aSN1EncodableVector));
    }

    public void addCapability(DERObjectIdentifier dERObjectIdentifier, DEREncodable dEREncodable) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(dERObjectIdentifier);
        aSN1EncodableVector.add(dEREncodable);
        this.capabilities.add(new DERSequence(aSN1EncodableVector));
    }

    public DEREncodableVector toDEREncodableVector() {
        return this.capabilities;
    }
}
