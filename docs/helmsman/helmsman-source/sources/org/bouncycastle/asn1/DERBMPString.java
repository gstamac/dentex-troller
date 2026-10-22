package org.bouncycastle.asn1;

import java.io.IOException;
import kotlin.UByte;

/* JADX INFO: loaded from: classes2.dex */
public class DERBMPString extends ASN1Object implements DERString {
    String string;

    public DERBMPString(String str) {
        this.string = str;
    }

    public DERBMPString(byte[] bArr) {
        int length = bArr.length / 2;
        char[] cArr = new char[length];
        for (int i = 0; i != length; i++) {
            int i2 = i * 2;
            cArr[i] = (char) ((bArr[i2 + 1] & UByte.MAX_VALUE) | (bArr[i2] << 8));
        }
        this.string = new String(cArr);
    }

    public static DERBMPString getInstance(Object obj) {
        if (obj == null || (obj instanceof DERBMPString)) {
            return (DERBMPString) obj;
        }
        if (obj instanceof ASN1OctetString) {
            return new DERBMPString(((ASN1OctetString) obj).getOctets());
        }
        if (obj instanceof ASN1TaggedObject) {
            return getInstance(((ASN1TaggedObject) obj).getObject());
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    public static DERBMPString getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return getInstance(aSN1TaggedObject.getObject());
    }

    @Override // org.bouncycastle.asn1.ASN1Object
    protected boolean asn1Equals(DERObject dERObject) {
        if (dERObject instanceof DERBMPString) {
            return getString().equals(((DERBMPString) dERObject).getString());
        }
        return false;
    }

    @Override // org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject
    void encode(DEROutputStream dEROutputStream) throws IOException {
        char[] charArray = this.string.toCharArray();
        byte[] bArr = new byte[charArray.length * 2];
        for (int i = 0; i != charArray.length; i++) {
            int i2 = i * 2;
            char c = charArray[i];
            bArr[i2] = (byte) (c >> '\b');
            bArr[i2 + 1] = (byte) c;
        }
        dEROutputStream.writeEncoded(30, bArr);
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
