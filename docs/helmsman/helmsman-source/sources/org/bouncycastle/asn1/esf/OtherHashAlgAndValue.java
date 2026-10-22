package org.bouncycastle.asn1.esf;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

/* JADX INFO: loaded from: classes2.dex */
public class OtherHashAlgAndValue extends ASN1Encodable {
    private AlgorithmIdentifier hashAlgorithm;
    private ASN1OctetString hashValue;

    public OtherHashAlgAndValue(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() != 2) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        this.hashAlgorithm = AlgorithmIdentifier.getInstance(aSN1Sequence.getObjectAt(0));
        this.hashValue = ASN1OctetString.getInstance(aSN1Sequence.getObjectAt(1));
    }

    public OtherHashAlgAndValue(AlgorithmIdentifier algorithmIdentifier, ASN1OctetString aSN1OctetString) {
        this.hashAlgorithm = algorithmIdentifier;
        this.hashValue = aSN1OctetString;
    }

    public static OtherHashAlgAndValue getInstance(Object obj) {
        if (obj == null || (obj instanceof OtherHashAlgAndValue)) {
            return (OtherHashAlgAndValue) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new OtherHashAlgAndValue((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("unknown object in 'OtherHashAlgAndValue' factory : " + obj.getClass().getName() + ".");
    }

    public AlgorithmIdentifier getHashAlgorithm() {
        return this.hashAlgorithm;
    }

    public ASN1OctetString getHashValue() {
        return this.hashValue;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.hashAlgorithm);
        aSN1EncodableVector.add(this.hashValue);
        return new DERSequence(aSN1EncodableVector);
    }
}
