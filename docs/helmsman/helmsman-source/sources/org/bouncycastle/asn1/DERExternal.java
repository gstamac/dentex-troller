package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/* JADX INFO: loaded from: classes2.dex */
public class DERExternal extends ASN1Object {
    private ASN1Object dataValueDescriptor;
    private DERObjectIdentifier directReference;
    private int encoding;
    private DERObject externalContent;
    private DERInteger indirectReference;

    public DERExternal(ASN1EncodableVector aSN1EncodableVector) {
        int i = 0;
        DERObject dERObject = aSN1EncodableVector.get(0).getDERObject();
        if (dERObject instanceof DERObjectIdentifier) {
            this.directReference = (DERObjectIdentifier) dERObject;
            dERObject = aSN1EncodableVector.get(1).getDERObject();
            i = 1;
        }
        if (dERObject instanceof DERInteger) {
            this.indirectReference = (DERInteger) dERObject;
            i++;
            dERObject = aSN1EncodableVector.get(i).getDERObject();
        }
        if (!(dERObject instanceof DERTaggedObject)) {
            this.dataValueDescriptor = (ASN1Object) dERObject;
            dERObject = aSN1EncodableVector.get(i + 1).getDERObject();
        }
        if (!(dERObject instanceof DERTaggedObject)) {
            throw new IllegalArgumentException("No tagged object found in vector. Structure doesn't seem to be of type External");
        }
        DERTaggedObject dERTaggedObject = (DERTaggedObject) dERObject;
        setEncoding(dERTaggedObject.getTagNo());
        this.externalContent = dERTaggedObject.getObject();
    }

    public DERExternal(DERObjectIdentifier dERObjectIdentifier, DERInteger dERInteger, ASN1Object aSN1Object, int i, DERObject dERObject) {
        setDirectReference(dERObjectIdentifier);
        setIndirectReference(dERInteger);
        setDataValueDescriptor(aSN1Object);
        setEncoding(i);
        setExternalContent(dERObject.getDERObject());
    }

    public DERExternal(DERObjectIdentifier dERObjectIdentifier, DERInteger dERInteger, ASN1Object aSN1Object, DERTaggedObject dERTaggedObject) {
        this(dERObjectIdentifier, dERInteger, aSN1Object, dERTaggedObject.getTagNo(), dERTaggedObject.getDERObject());
    }

    private void setDataValueDescriptor(ASN1Object aSN1Object) {
        this.dataValueDescriptor = aSN1Object;
    }

    private void setDirectReference(DERObjectIdentifier dERObjectIdentifier) {
        this.directReference = dERObjectIdentifier;
    }

    private void setEncoding(int i) {
        if (i < 0 || i > 2) {
            throw new IllegalArgumentException("invalid encoding value: " + i);
        }
        this.encoding = i;
    }

    private void setExternalContent(DERObject dERObject) {
        this.externalContent = dERObject;
    }

    private void setIndirectReference(DERInteger dERInteger) {
        this.indirectReference = dERInteger;
    }

    @Override // org.bouncycastle.asn1.ASN1Object
    boolean asn1Equals(DERObject dERObject) {
        ASN1Object aSN1Object;
        DERInteger dERInteger;
        DERObjectIdentifier dERObjectIdentifier;
        if (!(dERObject instanceof DERExternal)) {
            return false;
        }
        if (this == dERObject) {
            return true;
        }
        DERExternal dERExternal = (DERExternal) dERObject;
        DERObjectIdentifier dERObjectIdentifier2 = this.directReference;
        if (dERObjectIdentifier2 != null && ((dERObjectIdentifier = dERExternal.directReference) == null || !dERObjectIdentifier.equals(dERObjectIdentifier2))) {
            return false;
        }
        DERInteger dERInteger2 = this.indirectReference;
        if (dERInteger2 != null && ((dERInteger = dERExternal.indirectReference) == null || !dERInteger.equals(dERInteger2))) {
            return false;
        }
        ASN1Object aSN1Object2 = this.dataValueDescriptor;
        if (aSN1Object2 == null || ((aSN1Object = dERExternal.dataValueDescriptor) != null && aSN1Object.equals(aSN1Object2))) {
            return this.externalContent.equals(dERExternal.externalContent);
        }
        return false;
    }

    @Override // org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject
    void encode(DEROutputStream dEROutputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DERObjectIdentifier dERObjectIdentifier = this.directReference;
        if (dERObjectIdentifier != null) {
            byteArrayOutputStream.write(dERObjectIdentifier.getDEREncoded());
        }
        DERInteger dERInteger = this.indirectReference;
        if (dERInteger != null) {
            byteArrayOutputStream.write(dERInteger.getDEREncoded());
        }
        ASN1Object aSN1Object = this.dataValueDescriptor;
        if (aSN1Object != null) {
            byteArrayOutputStream.write(aSN1Object.getDEREncoded());
        }
        byteArrayOutputStream.write(new DERTaggedObject(this.encoding, this.externalContent).getDEREncoded());
        dEROutputStream.writeEncoded(32, 8, byteArrayOutputStream.toByteArray());
    }

    public ASN1Object getDataValueDescriptor() {
        return this.dataValueDescriptor;
    }

    public DERObjectIdentifier getDirectReference() {
        return this.directReference;
    }

    public int getEncoding() {
        return this.encoding;
    }

    public DERObject getExternalContent() {
        return this.externalContent;
    }

    public DERInteger getIndirectReference() {
        return this.indirectReference;
    }

    @Override // org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject, org.bouncycastle.asn1.ASN1Encodable
    public int hashCode() {
        DERObjectIdentifier dERObjectIdentifier = this.directReference;
        int iHashCode = dERObjectIdentifier != null ? dERObjectIdentifier.hashCode() : 0;
        DERInteger dERInteger = this.indirectReference;
        if (dERInteger != null) {
            iHashCode ^= dERInteger.hashCode();
        }
        ASN1Object aSN1Object = this.dataValueDescriptor;
        if (aSN1Object != null) {
            iHashCode ^= aSN1Object.hashCode();
        }
        return iHashCode ^ this.externalContent.hashCode();
    }
}
