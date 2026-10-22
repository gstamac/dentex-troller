package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import kotlin.UByte;

/* JADX INFO: loaded from: classes2.dex */
public abstract class ASN1Set extends ASN1Object {
    protected Vector set = new Vector();

    private byte[] getEncoded(DEREncodable dEREncodable) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            new ASN1OutputStream(byteArrayOutputStream).writeObject(dEREncodable);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException unused) {
            throw new IllegalArgumentException("cannot encode object added to SET");
        }
    }

    public static ASN1Set getInstance(Object obj) {
        if (obj == null || (obj instanceof ASN1Set)) {
            return (ASN1Set) obj;
        }
        throw new IllegalArgumentException("unknown object in getInstance: " + obj.getClass().getName());
    }

    public static ASN1Set getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        if (z) {
            if (aSN1TaggedObject.isExplicit()) {
                return (ASN1Set) aSN1TaggedObject.getObject();
            }
            throw new IllegalArgumentException("object implicit - explicit expected.");
        }
        if (aSN1TaggedObject.isExplicit()) {
            return new DERSet(aSN1TaggedObject.getObject());
        }
        if (aSN1TaggedObject.getObject() instanceof ASN1Set) {
            return (ASN1Set) aSN1TaggedObject.getObject();
        }
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (!(aSN1TaggedObject.getObject() instanceof ASN1Sequence)) {
            throw new IllegalArgumentException("unknown object in getInstance: " + aSN1TaggedObject.getClass().getName());
        }
        Enumeration objects = ((ASN1Sequence) aSN1TaggedObject.getObject()).getObjects();
        while (objects.hasMoreElements()) {
            aSN1EncodableVector.add((DEREncodable) objects.nextElement());
        }
        return new DERSet(aSN1EncodableVector, false);
    }

    private boolean lessThanOrEqual(byte[] bArr, byte[] bArr2) {
        int i;
        int i2;
        if (bArr.length <= bArr2.length) {
            for (int i3 = 0; i3 != bArr.length && (i2 = bArr2[i3] & UByte.MAX_VALUE) <= (i = bArr[i3] & UByte.MAX_VALUE); i3++) {
                if (i > i2) {
                    return false;
                }
            }
            return true;
        }
        for (int i4 = 0; i4 != bArr2.length; i4++) {
            int i5 = bArr[i4] & UByte.MAX_VALUE;
            int i6 = bArr2[i4] & UByte.MAX_VALUE;
            if (i6 > i5) {
                return true;
            }
            if (i5 > i6) {
                return false;
            }
        }
        return false;
    }

    protected void addObject(DEREncodable dEREncodable) {
        this.set.addElement(dEREncodable);
    }

    @Override // org.bouncycastle.asn1.ASN1Object
    boolean asn1Equals(DERObject dERObject) {
        if (!(dERObject instanceof ASN1Set)) {
            return false;
        }
        ASN1Set aSN1Set = (ASN1Set) dERObject;
        if (size() != aSN1Set.size()) {
            return false;
        }
        Enumeration objects = getObjects();
        Enumeration objects2 = aSN1Set.getObjects();
        while (objects.hasMoreElements()) {
            DERObject dERObject2 = ((DEREncodable) objects.nextElement()).getDERObject();
            DERObject dERObject3 = ((DEREncodable) objects2.nextElement()).getDERObject();
            if (dERObject2 != dERObject3 && (dERObject2 == null || !dERObject2.equals(dERObject3))) {
                return false;
            }
        }
        return true;
    }

    @Override // org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject
    abstract void encode(DEROutputStream dEROutputStream) throws IOException;

    public DEREncodable getObjectAt(int i) {
        return (DEREncodable) this.set.elementAt(i);
    }

    public Enumeration getObjects() {
        return this.set.elements();
    }

    @Override // org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject, org.bouncycastle.asn1.ASN1Encodable
    public int hashCode() {
        Enumeration objects = getObjects();
        int size = size();
        while (objects.hasMoreElements()) {
            Object objNextElement = objects.nextElement();
            size *= 17;
            if (objNextElement != null) {
                size ^= objNextElement.hashCode();
            }
        }
        return size;
    }

    public ASN1SetParser parser() {
        return new ASN1SetParser() { // from class: org.bouncycastle.asn1.ASN1Set.1
            private int index;
            private final int max;

            {
                this.max = ASN1Set.this.size();
            }

            @Override // org.bouncycastle.asn1.DEREncodable
            public DERObject getDERObject() {
                return this;
            }

            @Override // org.bouncycastle.asn1.ASN1SetParser
            public DEREncodable readObject() throws IOException {
                int i = this.index;
                if (i == this.max) {
                    return null;
                }
                ASN1Set aSN1Set = ASN1Set.this;
                this.index = i + 1;
                DEREncodable objectAt = aSN1Set.getObjectAt(i);
                if (objectAt instanceof ASN1Sequence) {
                    return ((ASN1Sequence) objectAt).parser();
                }
                return objectAt instanceof ASN1Set ? ((ASN1Set) objectAt).parser() : objectAt;
            }
        };
    }

    public int size() {
        return this.set.size();
    }

    protected void sort() {
        if (this.set.size() > 1) {
            int size = this.set.size() - 1;
            boolean z = true;
            while (z) {
                int i = 0;
                byte[] encoded = getEncoded((DEREncodable) this.set.elementAt(0));
                z = false;
                int i2 = 0;
                while (i2 != size) {
                    int i3 = i2 + 1;
                    byte[] encoded2 = getEncoded((DEREncodable) this.set.elementAt(i3));
                    if (lessThanOrEqual(encoded, encoded2)) {
                        encoded = encoded2;
                    } else {
                        Object objElementAt = this.set.elementAt(i2);
                        Vector vector = this.set;
                        vector.setElementAt(vector.elementAt(i3), i2);
                        this.set.setElementAt(objElementAt, i3);
                        z = true;
                        i = i2;
                    }
                    i2 = i3;
                }
                size = i;
            }
        }
    }

    public String toString() {
        return this.set.toString();
    }
}
