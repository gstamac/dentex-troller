package org.bouncycastle.asn1.crmf;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObject;

/* JADX INFO: loaded from: classes2.dex */
public class CertReqMessages extends ASN1Encodable {
    private ASN1Sequence content;

    private CertReqMessages(ASN1Sequence aSN1Sequence) {
        this.content = aSN1Sequence;
    }

    public static CertReqMessages getInstance(Object obj) {
        if (obj instanceof CertReqMessages) {
            return (CertReqMessages) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new CertReqMessages((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("Invalid object: " + obj.getClass().getName());
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        return this.content;
    }

    public CertReqMsg[] toCertReqMsgArray() {
        int size = this.content.size();
        CertReqMsg[] certReqMsgArr = new CertReqMsg[size];
        for (int i = 0; i != size; i++) {
            certReqMsgArr[i] = CertReqMsg.getInstance(this.content.getObjectAt(i));
        }
        return certReqMsgArr;
    }
}
