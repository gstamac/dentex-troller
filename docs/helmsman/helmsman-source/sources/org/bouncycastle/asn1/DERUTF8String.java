package org.bouncycastle.asn1;

import java.io.IOException;
import org.bouncycastle.util.Strings;

/* JADX INFO: loaded from: classes2.dex */
public class DERUTF8String extends ASN1Object implements DERString {
    String string;

    public DERUTF8String(String str) {
        this.string = str;
    }

    DERUTF8String(byte[] bArr) {
        this.string = Strings.fromUTF8ByteArray(bArr);
    }

    public static DERUTF8String getInstance(Object obj) {
        if (obj == null || (obj instanceof DERUTF8String)) {
            return (DERUTF8String) obj;
        }
        if (obj instanceof ASN1OctetString) {
            return new DERUTF8String(((ASN1OctetString) obj).getOctets());
        }
        if (obj instanceof ASN1TaggedObject) {
            return getInstance(((ASN1TaggedObject) obj).getObject());
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    public static DERUTF8String getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return getInstance(aSN1TaggedObject.getObject());
    }

    @Override // org.bouncycastle.asn1.ASN1Object
    boolean asn1Equals(DERObject dERObject) {
        if (dERObject instanceof DERUTF8String) {
            return getString().equals(((DERUTF8String) dERObject).getString());
        }
        return false;
    }

    @Override // org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject
    void encode(DEROutputStream dEROutputStream) throws IOException {
        dEROutputStream.writeEncoded(12, Strings.toUTF8ByteArray(this.string));
    }

    @Override // org.bouncycastle.asn1.DERString
    public String getString() {
        return this.string;
    }

    @Override // org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject, org.bouncycastle.asn1.ASN1Encodable
    public int hashCode() {
        return getString().hashCode();
    }

    public String toString() {
        return this.string;
    }
}
