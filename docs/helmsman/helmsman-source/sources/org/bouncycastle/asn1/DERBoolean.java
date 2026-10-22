package org.bouncycastle.asn1;

import java.io.IOException;

/* JADX INFO: loaded from: classes2.dex */
public class DERBoolean extends ASN1Object {
    public static final DERBoolean FALSE = new DERBoolean(false);
    public static final DERBoolean TRUE = new DERBoolean(true);
    byte value;

    public DERBoolean(boolean z) {
        this.value = z ? (byte) -1 : (byte) 0;
    }

    public DERBoolean(byte[] bArr) {
        this.value = bArr[0];
    }

    public static DERBoolean getInstance(Object obj) {
        if (obj == null || (obj instanceof DERBoolean)) {
            return (DERBoolean) obj;
        }
        if (obj instanceof ASN1OctetString) {
            return new DERBoolean(((ASN1OctetString) obj).getOctets());
        }
        if (obj instanceof ASN1TaggedObject) {
            return getInstance(((ASN1TaggedObject) obj).getObject());
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    public static DERBoolean getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return getInstance(aSN1TaggedObject.getObject());
    }

    public static DERBoolean getInstance(boolean z) {
        return z ? TRUE : FALSE;
    }

    @Override // org.bouncycastle.asn1.ASN1Object
    protected boolean asn1Equals(DERObject dERObject) {
        return dERObject != null && (dERObject instanceof DERBoolean) && this.value == ((DERBoolean) dERObject).value;
    }

    @Override // org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject
    void encode(DEROutputStream dEROutputStream) throws IOException {
        dEROutputStream.writeEncoded(1, new byte[]{this.value});
    }

    @Override // org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject, org.bouncycastle.asn1.ASN1Encodable
    public int hashCode() {
        return this.value;
    }

    public boolean isTrue() {
        return this.value != 0;
    }

    public String toString() {
        return this.value != 0 ? "TRUE" : "FALSE";
    }
}
