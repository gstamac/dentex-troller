package org.bouncycastle.asn1.x509;

import java.util.Enumeration;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;

/* JADX INFO: loaded from: classes2.dex */
public class SubjectDirectoryAttributes extends ASN1Encodable {
    private Vector attributes = new Vector();

    public SubjectDirectoryAttributes(Vector vector) {
        Enumeration enumerationElements = vector.elements();
        while (enumerationElements.hasMoreElements()) {
            this.attributes.addElement(enumerationElements.nextElement());
        }
    }

    public SubjectDirectoryAttributes(ASN1Sequence aSN1Sequence) {
        Enumeration objects = aSN1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            this.attributes.addElement(new Attribute(ASN1Sequence.getInstance(objects.nextElement())));
        }
    }

    public static SubjectDirectoryAttributes getInstance(Object obj) {
        if (obj == null || (obj instanceof SubjectDirectoryAttributes)) {
            return (SubjectDirectoryAttributes) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new SubjectDirectoryAttributes((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    public Vector getAttributes() {
        return this.attributes;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        Enumeration enumerationElements = this.attributes.elements();
        while (enumerationElements.hasMoreElements()) {
            aSN1EncodableVector.add((Attribute) enumerationElements.nextElement());
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
