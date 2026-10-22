package org.bouncycastle.asn1.cms;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERObjectIdentifier;

/* JADX INFO: loaded from: classes2.dex */
public class AttributeTable {
    private Hashtable attributes;

    public AttributeTable(Hashtable hashtable) {
        this.attributes = new Hashtable();
        this.attributes = copyTable(hashtable);
    }

    public AttributeTable(ASN1Set aSN1Set) {
        this.attributes = new Hashtable();
        for (int i = 0; i != aSN1Set.size(); i++) {
            Attribute attribute = Attribute.getInstance(aSN1Set.getObjectAt(i));
            addAttribute(attribute.getAttrType(), attribute);
        }
    }

    public AttributeTable(DEREncodableVector dEREncodableVector) {
        this.attributes = new Hashtable();
        for (int i = 0; i != dEREncodableVector.size(); i++) {
            Attribute attribute = Attribute.getInstance(dEREncodableVector.get(i));
            addAttribute(attribute.getAttrType(), attribute);
        }
    }

    private void addAttribute(DERObjectIdentifier dERObjectIdentifier, Attribute attribute) {
        Vector vector;
        Object obj = this.attributes.get(dERObjectIdentifier);
        if (obj == null) {
            this.attributes.put(dERObjectIdentifier, attribute);
            return;
        }
        if (obj instanceof Attribute) {
            vector = new Vector();
            vector.addElement(obj);
        } else {
            vector = (Vector) obj;
        }
        vector.addElement(attribute);
        this.attributes.put(dERObjectIdentifier, vector);
    }

    private Hashtable copyTable(Hashtable hashtable) {
        Hashtable hashtable2 = new Hashtable();
        Enumeration enumerationKeys = hashtable.keys();
        while (enumerationKeys.hasMoreElements()) {
            Object objNextElement = enumerationKeys.nextElement();
            hashtable2.put(objNextElement, hashtable.get(objNextElement));
        }
        return hashtable2;
    }

    public Attribute get(DERObjectIdentifier dERObjectIdentifier) {
        Object obj = this.attributes.get(dERObjectIdentifier);
        return obj instanceof Vector ? (Attribute) ((Vector) obj).elementAt(0) : (Attribute) obj;
    }

    public ASN1EncodableVector getAll(DERObjectIdentifier dERObjectIdentifier) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        Object obj = this.attributes.get(dERObjectIdentifier);
        if (obj instanceof Vector) {
            Enumeration enumerationElements = ((Vector) obj).elements();
            while (enumerationElements.hasMoreElements()) {
                aSN1EncodableVector.add((Attribute) enumerationElements.nextElement());
            }
        } else if (obj != null) {
            aSN1EncodableVector.add((Attribute) obj);
        }
        return aSN1EncodableVector;
    }

    public ASN1EncodableVector toASN1EncodableVector() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        Enumeration enumerationElements = this.attributes.elements();
        while (enumerationElements.hasMoreElements()) {
            Object objNextElement = enumerationElements.nextElement();
            if (objNextElement instanceof Vector) {
                Enumeration enumerationElements2 = ((Vector) objNextElement).elements();
                while (enumerationElements2.hasMoreElements()) {
                    aSN1EncodableVector.add(Attribute.getInstance(enumerationElements2.nextElement()));
                }
            } else {
                aSN1EncodableVector.add(Attribute.getInstance(objNextElement));
            }
        }
        return aSN1EncodableVector;
    }

    public Hashtable toHashtable() {
        return copyTable(this.attributes);
    }
}
