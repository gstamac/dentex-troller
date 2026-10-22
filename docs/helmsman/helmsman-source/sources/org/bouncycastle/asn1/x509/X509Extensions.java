package org.bouncycastle.asn1.x509;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBoolean;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;

/* JADX INFO: loaded from: classes2.dex */
public class X509Extensions extends ASN1Encodable {
    private Hashtable extensions;
    private Vector ordering;
    public static final DERObjectIdentifier SubjectDirectoryAttributes = new DERObjectIdentifier("2.5.29.9");
    public static final DERObjectIdentifier SubjectKeyIdentifier = new DERObjectIdentifier("2.5.29.14");
    public static final DERObjectIdentifier KeyUsage = new DERObjectIdentifier("2.5.29.15");
    public static final DERObjectIdentifier PrivateKeyUsagePeriod = new DERObjectIdentifier("2.5.29.16");
    public static final DERObjectIdentifier SubjectAlternativeName = new DERObjectIdentifier("2.5.29.17");
    public static final DERObjectIdentifier IssuerAlternativeName = new DERObjectIdentifier("2.5.29.18");
    public static final DERObjectIdentifier BasicConstraints = new DERObjectIdentifier("2.5.29.19");
    public static final DERObjectIdentifier CRLNumber = new DERObjectIdentifier("2.5.29.20");
    public static final DERObjectIdentifier ReasonCode = new DERObjectIdentifier("2.5.29.21");
    public static final DERObjectIdentifier InstructionCode = new DERObjectIdentifier("2.5.29.23");
    public static final DERObjectIdentifier InvalidityDate = new DERObjectIdentifier("2.5.29.24");
    public static final DERObjectIdentifier DeltaCRLIndicator = new DERObjectIdentifier("2.5.29.27");
    public static final DERObjectIdentifier IssuingDistributionPoint = new DERObjectIdentifier("2.5.29.28");
    public static final DERObjectIdentifier CertificateIssuer = new DERObjectIdentifier("2.5.29.29");
    public static final DERObjectIdentifier NameConstraints = new DERObjectIdentifier("2.5.29.30");
    public static final DERObjectIdentifier CRLDistributionPoints = new DERObjectIdentifier("2.5.29.31");
    public static final DERObjectIdentifier CertificatePolicies = new DERObjectIdentifier("2.5.29.32");
    public static final DERObjectIdentifier PolicyMappings = new DERObjectIdentifier("2.5.29.33");
    public static final DERObjectIdentifier AuthorityKeyIdentifier = new DERObjectIdentifier("2.5.29.35");
    public static final DERObjectIdentifier PolicyConstraints = new DERObjectIdentifier("2.5.29.36");
    public static final DERObjectIdentifier ExtendedKeyUsage = new DERObjectIdentifier("2.5.29.37");
    public static final DERObjectIdentifier FreshestCRL = new DERObjectIdentifier("2.5.29.46");
    public static final DERObjectIdentifier InhibitAnyPolicy = new DERObjectIdentifier("2.5.29.54");
    public static final DERObjectIdentifier AuthorityInfoAccess = new DERObjectIdentifier("1.3.6.1.5.5.7.1.1");
    public static final DERObjectIdentifier SubjectInfoAccess = new DERObjectIdentifier("1.3.6.1.5.5.7.1.11");
    public static final DERObjectIdentifier LogoType = new DERObjectIdentifier("1.3.6.1.5.5.7.1.12");
    public static final DERObjectIdentifier BiometricInfo = new DERObjectIdentifier("1.3.6.1.5.5.7.1.2");
    public static final DERObjectIdentifier QCStatements = new DERObjectIdentifier("1.3.6.1.5.5.7.1.3");
    public static final DERObjectIdentifier AuditIdentity = new DERObjectIdentifier("1.3.6.1.5.5.7.1.4");
    public static final DERObjectIdentifier NoRevAvail = new DERObjectIdentifier("2.5.29.56");
    public static final DERObjectIdentifier TargetInformation = new DERObjectIdentifier("2.5.29.55");

    public X509Extensions(Hashtable hashtable) {
        this((Vector) null, hashtable);
    }

    public X509Extensions(Vector vector, Hashtable hashtable) {
        this.extensions = new Hashtable();
        this.ordering = new Vector();
        Enumeration enumerationKeys = vector == null ? hashtable.keys() : vector.elements();
        while (enumerationKeys.hasMoreElements()) {
            this.ordering.addElement(enumerationKeys.nextElement());
        }
        Enumeration enumerationElements = this.ordering.elements();
        while (enumerationElements.hasMoreElements()) {
            DERObjectIdentifier dERObjectIdentifier = (DERObjectIdentifier) enumerationElements.nextElement();
            this.extensions.put(dERObjectIdentifier, (X509Extension) hashtable.get(dERObjectIdentifier));
        }
    }

    public X509Extensions(Vector vector, Vector vector2) {
        this.extensions = new Hashtable();
        this.ordering = new Vector();
        Enumeration enumerationElements = vector.elements();
        while (enumerationElements.hasMoreElements()) {
            this.ordering.addElement(enumerationElements.nextElement());
        }
        Enumeration enumerationElements2 = this.ordering.elements();
        int i = 0;
        while (enumerationElements2.hasMoreElements()) {
            this.extensions.put((DERObjectIdentifier) enumerationElements2.nextElement(), (X509Extension) vector2.elementAt(i));
            i++;
        }
    }

    public X509Extensions(ASN1Sequence aSN1Sequence) {
        this.extensions = new Hashtable();
        this.ordering = new Vector();
        Enumeration objects = aSN1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            ASN1Sequence aSN1Sequence2 = ASN1Sequence.getInstance(objects.nextElement());
            if (aSN1Sequence2.size() == 3) {
                this.extensions.put(aSN1Sequence2.getObjectAt(0), new X509Extension(DERBoolean.getInstance(aSN1Sequence2.getObjectAt(1)), ASN1OctetString.getInstance(aSN1Sequence2.getObjectAt(2))));
            } else {
                if (aSN1Sequence2.size() != 2) {
                    throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence2.size());
                }
                this.extensions.put(aSN1Sequence2.getObjectAt(0), new X509Extension(false, ASN1OctetString.getInstance(aSN1Sequence2.getObjectAt(1))));
            }
            this.ordering.addElement(aSN1Sequence2.getObjectAt(0));
        }
    }

    public static X509Extensions getInstance(Object obj) {
        if (obj == null || (obj instanceof X509Extensions)) {
            return (X509Extensions) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new X509Extensions((ASN1Sequence) obj);
        }
        if (obj instanceof ASN1TaggedObject) {
            return getInstance(((ASN1TaggedObject) obj).getObject());
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    public static X509Extensions getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, z));
    }

    public boolean equivalent(X509Extensions x509Extensions) {
        if (this.extensions.size() != x509Extensions.extensions.size()) {
            return false;
        }
        Enumeration enumerationKeys = this.extensions.keys();
        while (enumerationKeys.hasMoreElements()) {
            Object objNextElement = enumerationKeys.nextElement();
            if (!this.extensions.get(objNextElement).equals(x509Extensions.extensions.get(objNextElement))) {
                return false;
            }
        }
        return true;
    }

    public X509Extension getExtension(DERObjectIdentifier dERObjectIdentifier) {
        return (X509Extension) this.extensions.get(dERObjectIdentifier);
    }

    public Enumeration oids() {
        return this.ordering.elements();
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        Enumeration enumerationElements = this.ordering.elements();
        while (enumerationElements.hasMoreElements()) {
            DERObjectIdentifier dERObjectIdentifier = (DERObjectIdentifier) enumerationElements.nextElement();
            X509Extension x509Extension = (X509Extension) this.extensions.get(dERObjectIdentifier);
            ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
            aSN1EncodableVector2.add(dERObjectIdentifier);
            if (x509Extension.isCritical()) {
                aSN1EncodableVector2.add(new DERBoolean(true));
            }
            aSN1EncodableVector2.add(x509Extension.getValue());
            aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector2));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
