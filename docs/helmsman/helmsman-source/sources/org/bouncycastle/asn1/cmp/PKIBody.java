package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.crmf.CertReqMessages;
import org.bouncycastle.asn1.pkcs.CertificationRequest;

/* JADX INFO: loaded from: classes2.dex */
public class PKIBody extends ASN1Encodable implements ASN1Choice {
    private ASN1Encodable body;
    private int tagNo;

    private PKIBody(ASN1TaggedObject aSN1TaggedObject) {
        ASN1Encodable certReqMessages;
        this.tagNo = aSN1TaggedObject.getTagNo();
        switch (aSN1TaggedObject.getTagNo()) {
            case 0:
            case 2:
            case 7:
            case 9:
            case 13:
                certReqMessages = CertReqMessages.getInstance(aSN1TaggedObject.getObject());
                break;
            case 1:
            case 3:
            case 8:
            case 14:
                certReqMessages = CertRepMessage.getInstance(aSN1TaggedObject.getObject());
                break;
            case 4:
                certReqMessages = CertificationRequest.getInstance(aSN1TaggedObject.getObject());
                break;
            case 5:
                certReqMessages = POPODecKeyChallContent.getInstance(aSN1TaggedObject.getObject());
                break;
            case 6:
                certReqMessages = POPODecKeyRespContent.getInstance(aSN1TaggedObject.getObject());
                break;
            case 10:
                certReqMessages = KeyRecRepContent.getInstance(aSN1TaggedObject.getObject());
                break;
            case 11:
                certReqMessages = RevReqContent.getInstance(aSN1TaggedObject.getObject());
                break;
            case 12:
                certReqMessages = RevRepContent.getInstance(aSN1TaggedObject.getObject());
                break;
            case 15:
                certReqMessages = CAKeyUpdAnnContent.getInstance(aSN1TaggedObject.getObject());
                break;
            case 16:
                certReqMessages = CMPCertificate.getInstance(aSN1TaggedObject.getObject());
                break;
            case 17:
                certReqMessages = RevAnnContent.getInstance(aSN1TaggedObject.getObject());
                break;
            case 18:
                certReqMessages = CRLAnnContent.getInstance(aSN1TaggedObject.getObject());
                break;
            case 19:
                certReqMessages = PKIConfirmContent.getInstance(aSN1TaggedObject.getObject());
                break;
            case 20:
                certReqMessages = PKIMessages.getInstance(aSN1TaggedObject.getObject());
                break;
            case 21:
                certReqMessages = GenMsgContent.getInstance(aSN1TaggedObject.getObject());
                break;
            case 22:
                certReqMessages = GenRepContent.getInstance(aSN1TaggedObject.getObject());
                break;
            case 23:
                certReqMessages = ErrorMsgContent.getInstance(aSN1TaggedObject.getObject());
                break;
            case 24:
                certReqMessages = CertConfirmContent.getInstance(aSN1TaggedObject.getObject());
                break;
            case 25:
                certReqMessages = PollReqContent.getInstance(aSN1TaggedObject.getObject());
                break;
            case 26:
                certReqMessages = PollRepContent.getInstance(aSN1TaggedObject.getObject());
                break;
            default:
                throw new IllegalArgumentException("unknown tag number: " + aSN1TaggedObject.getTagNo());
        }
        this.body = certReqMessages;
    }

    public static PKIBody getInstance(Object obj) {
        if (obj instanceof PKIBody) {
            return (PKIBody) obj;
        }
        if (obj instanceof ASN1TaggedObject) {
            return new PKIBody((ASN1TaggedObject) obj);
        }
        throw new IllegalArgumentException("Invalid object: " + obj.getClass().getName());
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        return new DERTaggedObject(true, this.tagNo, this.body);
    }
}
