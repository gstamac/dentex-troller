package org.bouncycastle.asn1.crmf;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509Name;

/* JADX INFO: loaded from: classes2.dex */
public class CertTemplate extends ASN1Encodable {
    private X509Extensions extensions;
    private X509Name issuer;
    private DERBitString issuerUID;
    private SubjectPublicKeyInfo publicKey;
    private DERInteger serialNumber;
    private AlgorithmIdentifier signingAlg;
    private X509Name subject;
    private DERBitString subjectUID;
    private OptionalValidity validity;
    private DERInteger version;

    private CertTemplate(ASN1Sequence aSN1Sequence) {
        Enumeration objects = aSN1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject) objects.nextElement();
            switch (aSN1TaggedObject.getTagNo()) {
                case 0:
                    this.version = DERInteger.getInstance(aSN1TaggedObject, false);
                    break;
                case 1:
                    this.serialNumber = DERInteger.getInstance(aSN1TaggedObject, false);
                    break;
                case 2:
                    this.signingAlg = AlgorithmIdentifier.getInstance(aSN1TaggedObject, false);
                    break;
                case 3:
                    this.issuer = X509Name.getInstance(aSN1TaggedObject, true);
                    break;
                case 4:
                    this.validity = OptionalValidity.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, false));
                    break;
                case 5:
                    this.subject = X509Name.getInstance(aSN1TaggedObject, true);
                    break;
                case 6:
                    this.publicKey = SubjectPublicKeyInfo.getInstance(aSN1TaggedObject, false);
                    break;
                case 7:
                    this.issuerUID = DERBitString.getInstance(aSN1TaggedObject, false);
                    break;
                case 8:
                    this.subjectUID = DERBitString.getInstance(aSN1TaggedObject, false);
                    break;
                case 9:
                    this.extensions = X509Extensions.getInstance(aSN1TaggedObject, false);
                    break;
                default:
                    throw new IllegalArgumentException("unknown tag: " + aSN1TaggedObject.getTagNo());
            }
        }
    }

    private void addOptional(ASN1EncodableVector aSN1EncodableVector, int i, boolean z, ASN1Encodable aSN1Encodable) {
        if (aSN1Encodable != null) {
            aSN1EncodableVector.add(new DERTaggedObject(z, i, aSN1Encodable));
        }
    }

    public static CertTemplate getInstance(Object obj) {
        if (obj instanceof CertTemplate) {
            return (CertTemplate) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new CertTemplate((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("Invalid object: " + obj.getClass().getName());
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        addOptional(aSN1EncodableVector, 0, false, this.version);
        addOptional(aSN1EncodableVector, 1, false, this.serialNumber);
        addOptional(aSN1EncodableVector, 2, false, this.signingAlg);
        addOptional(aSN1EncodableVector, 3, true, this.issuer);
        addOptional(aSN1EncodableVector, 4, false, this.validity);
        addOptional(aSN1EncodableVector, 5, true, this.subject);
        addOptional(aSN1EncodableVector, 6, false, this.publicKey);
        addOptional(aSN1EncodableVector, 7, false, this.issuerUID);
        addOptional(aSN1EncodableVector, 8, false, this.subjectUID);
        addOptional(aSN1EncodableVector, 9, false, this.extensions);
        return new DERSequence(aSN1EncodableVector);
    }
}
