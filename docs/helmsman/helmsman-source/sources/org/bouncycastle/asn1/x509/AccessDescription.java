package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers;

/* JADX INFO: loaded from: classes2.dex */
public class AccessDescription extends ASN1Encodable {
    public static final DERObjectIdentifier id_ad_caIssuers = new DERObjectIdentifier("1.3.6.1.5.5.7.48.2");
    public static final DERObjectIdentifier id_ad_ocsp = new DERObjectIdentifier(OCSPObjectIdentifiers.pkix_ocsp);
    GeneralName accessLocation;
    DERObjectIdentifier accessMethod;

    public AccessDescription(ASN1Sequence aSN1Sequence) {
        this.accessMethod = null;
        this.accessLocation = null;
        if (aSN1Sequence.size() != 2) {
            throw new IllegalArgumentException("wrong number of elements in sequence");
        }
        this.accessMethod = DERObjectIdentifier.getInstance(aSN1Sequence.getObjectAt(0));
        this.accessLocation = GeneralName.getInstance(aSN1Sequence.getObjectAt(1));
    }

    public AccessDescription(DERObjectIdentifier dERObjectIdentifier, GeneralName generalName) {
        this.accessMethod = dERObjectIdentifier;
        this.accessLocation = generalName;
    }

    public static AccessDescription getInstance(Object obj) {
        if (obj instanceof AccessDescription) {
            return (AccessDescription) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new AccessDescription((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("unknown object in factory: " + obj.getClass().getName());
    }

    public GeneralName getAccessLocation() {
        return this.accessLocation;
    }

    public DERObjectIdentifier getAccessMethod() {
        return this.accessMethod;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.accessMethod);
        aSN1EncodableVector.add(this.accessLocation);
        return new DERSequence(aSN1EncodableVector);
    }

    public String toString() {
        return "AccessDescription: Oid(" + this.accessMethod.getId() + ")";
    }
}
