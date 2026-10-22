package org.bouncycastle.asn1.x509.qualified;

import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;

/* JADX INFO: loaded from: classes2.dex */
public class TypeOfBiometricData extends ASN1Encodable implements ASN1Choice {
    public static final int HANDWRITTEN_SIGNATURE = 1;
    public static final int PICTURE = 0;
    DEREncodable obj;

    public TypeOfBiometricData(int i) {
        if (i != 0 && i != 1) {
            throw new IllegalArgumentException("unknow PredefinedBiometricType : " + i);
        }
        this.obj = new DERInteger(i);
    }

    public TypeOfBiometricData(DERObjectIdentifier dERObjectIdentifier) {
        this.obj = dERObjectIdentifier;
    }

    public static TypeOfBiometricData getInstance(Object obj) {
        if (obj == null || (obj instanceof TypeOfBiometricData)) {
            return (TypeOfBiometricData) obj;
        }
        if (obj instanceof DERInteger) {
            return new TypeOfBiometricData(DERInteger.getInstance(obj).getValue().intValue());
        }
        if (obj instanceof DERObjectIdentifier) {
            return new TypeOfBiometricData(DERObjectIdentifier.getInstance(obj));
        }
        throw new IllegalArgumentException("unknown object in getInstance");
    }

    public DERObjectIdentifier getBiometricDataOid() {
        return (DERObjectIdentifier) this.obj;
    }

    public int getPredefinedBiometricType() {
        return ((DERInteger) this.obj).getValue().intValue();
    }

    public boolean isPredefined() {
        return this.obj instanceof DERInteger;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        return this.obj.getDERObject();
    }
}
