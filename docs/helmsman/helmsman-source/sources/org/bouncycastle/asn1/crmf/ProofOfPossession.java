package org.bouncycastle.asn1.crmf;

import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERTaggedObject;

/* JADX INFO: loaded from: classes2.dex */
public class ProofOfPossession extends ASN1Encodable implements ASN1Choice {
    private ASN1Encodable obj;
    private int tagNo;

    private ProofOfPossession(ASN1TaggedObject aSN1TaggedObject) {
        ASN1Encodable pOPOSigningKey;
        int tagNo = aSN1TaggedObject.getTagNo();
        this.tagNo = tagNo;
        if (tagNo == 0) {
            pOPOSigningKey = DERNull.INSTANCE;
        } else if (tagNo == 1) {
            pOPOSigningKey = POPOSigningKey.getInstance(aSN1TaggedObject, false);
        } else {
            if (tagNo != 2 && tagNo != 3) {
                throw new IllegalArgumentException("unknown tag: " + this.tagNo);
            }
            pOPOSigningKey = POPOPrivKey.getInstance(aSN1TaggedObject, false);
        }
        this.obj = pOPOSigningKey;
    }

    public static ProofOfPossession getInstance(Object obj) {
        if (obj instanceof ProofOfPossession) {
            return (ProofOfPossession) obj;
        }
        if (obj instanceof ASN1TaggedObject) {
            return new ProofOfPossession((ASN1TaggedObject) obj);
        }
        throw new IllegalArgumentException("Invalid object: " + obj.getClass().getName());
    }

    public ASN1Encodable getObject() {
        return this.obj;
    }

    public int getType() {
        return this.tagNo;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        return new DERTaggedObject(false, this.tagNo, this.obj);
    }
}
