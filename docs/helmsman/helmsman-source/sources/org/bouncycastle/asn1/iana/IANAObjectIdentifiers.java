package org.bouncycastle.asn1.iana;

import org.bouncycastle.asn1.DERObjectIdentifier;

/* JADX INFO: loaded from: classes2.dex */
public interface IANAObjectIdentifiers {
    public static final DERObjectIdentifier hmacMD5;
    public static final DERObjectIdentifier hmacRIPEMD160;
    public static final DERObjectIdentifier hmacSHA1;
    public static final DERObjectIdentifier hmacTIGER;
    public static final DERObjectIdentifier isakmpOakley;

    static {
        DERObjectIdentifier dERObjectIdentifier = new DERObjectIdentifier("1.3.6.1.5.5.8.1");
        isakmpOakley = dERObjectIdentifier;
        hmacMD5 = new DERObjectIdentifier(dERObjectIdentifier + ".1");
        hmacSHA1 = new DERObjectIdentifier(dERObjectIdentifier + ".2");
        hmacTIGER = new DERObjectIdentifier(dERObjectIdentifier + ".3");
        hmacRIPEMD160 = new DERObjectIdentifier(dERObjectIdentifier + ".4");
    }
}
