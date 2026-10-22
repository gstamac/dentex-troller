package org.bouncycastle.asn1.crmf;

import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERObject;

/* JADX INFO: loaded from: classes2.dex */
public class POPOPrivKey extends ASN1Encodable implements ASN1Choice {
    private DERObject obj;

    private POPOPrivKey(DERObject dERObject) {
        this.obj = dERObject;
    }

    public static ASN1Encodable getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return new POPOPrivKey(aSN1TaggedObject.getObject());
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        return this.obj;
    }
}
