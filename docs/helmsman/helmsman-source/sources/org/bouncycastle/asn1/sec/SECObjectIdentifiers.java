package org.bouncycastle.asn1.sec;

import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;

/* JADX INFO: loaded from: classes2.dex */
public interface SECObjectIdentifiers {
    public static final DERObjectIdentifier ellipticCurve;
    public static final DERObjectIdentifier secp112r1;
    public static final DERObjectIdentifier secp112r2;
    public static final DERObjectIdentifier secp128r1;
    public static final DERObjectIdentifier secp128r2;
    public static final DERObjectIdentifier secp160k1;
    public static final DERObjectIdentifier secp160r1;
    public static final DERObjectIdentifier secp160r2;
    public static final DERObjectIdentifier secp192k1;
    public static final DERObjectIdentifier secp192r1;
    public static final DERObjectIdentifier secp224k1;
    public static final DERObjectIdentifier secp224r1;
    public static final DERObjectIdentifier secp256k1;
    public static final DERObjectIdentifier secp256r1;
    public static final DERObjectIdentifier secp384r1;
    public static final DERObjectIdentifier secp521r1;
    public static final DERObjectIdentifier sect113r1;
    public static final DERObjectIdentifier sect113r2;
    public static final DERObjectIdentifier sect131r1;
    public static final DERObjectIdentifier sect131r2;
    public static final DERObjectIdentifier sect163k1;
    public static final DERObjectIdentifier sect163r1;
    public static final DERObjectIdentifier sect163r2;
    public static final DERObjectIdentifier sect193r1;
    public static final DERObjectIdentifier sect193r2;
    public static final DERObjectIdentifier sect233k1;
    public static final DERObjectIdentifier sect233r1;
    public static final DERObjectIdentifier sect239k1;
    public static final DERObjectIdentifier sect283k1;
    public static final DERObjectIdentifier sect283r1;
    public static final DERObjectIdentifier sect409k1;
    public static final DERObjectIdentifier sect409r1;
    public static final DERObjectIdentifier sect571k1;
    public static final DERObjectIdentifier sect571r1;

    static {
        DERObjectIdentifier dERObjectIdentifier = new DERObjectIdentifier("1.3.132.0");
        ellipticCurve = dERObjectIdentifier;
        sect163k1 = new DERObjectIdentifier(dERObjectIdentifier + ".1");
        sect163r1 = new DERObjectIdentifier(dERObjectIdentifier + ".2");
        sect239k1 = new DERObjectIdentifier(dERObjectIdentifier + ".3");
        sect113r1 = new DERObjectIdentifier(dERObjectIdentifier + ".4");
        sect113r2 = new DERObjectIdentifier(dERObjectIdentifier + ".5");
        secp112r1 = new DERObjectIdentifier(dERObjectIdentifier + ".6");
        secp112r2 = new DERObjectIdentifier(dERObjectIdentifier + ".7");
        secp160r1 = new DERObjectIdentifier(dERObjectIdentifier + ".8");
        secp160k1 = new DERObjectIdentifier(dERObjectIdentifier + ".9");
        secp256k1 = new DERObjectIdentifier(dERObjectIdentifier + ".10");
        sect163r2 = new DERObjectIdentifier(dERObjectIdentifier + ".15");
        sect283k1 = new DERObjectIdentifier(dERObjectIdentifier + ".16");
        sect283r1 = new DERObjectIdentifier(dERObjectIdentifier + ".17");
        sect131r1 = new DERObjectIdentifier(dERObjectIdentifier + ".22");
        sect131r2 = new DERObjectIdentifier(dERObjectIdentifier + ".23");
        sect193r1 = new DERObjectIdentifier(dERObjectIdentifier + ".24");
        sect193r2 = new DERObjectIdentifier(dERObjectIdentifier + ".25");
        sect233k1 = new DERObjectIdentifier(dERObjectIdentifier + ".26");
        sect233r1 = new DERObjectIdentifier(dERObjectIdentifier + ".27");
        secp128r1 = new DERObjectIdentifier(dERObjectIdentifier + ".28");
        secp128r2 = new DERObjectIdentifier(dERObjectIdentifier + ".29");
        secp160r2 = new DERObjectIdentifier(dERObjectIdentifier + ".30");
        secp192k1 = new DERObjectIdentifier(dERObjectIdentifier + ".31");
        secp224k1 = new DERObjectIdentifier(dERObjectIdentifier + ".32");
        secp224r1 = new DERObjectIdentifier(dERObjectIdentifier + ".33");
        secp384r1 = new DERObjectIdentifier(dERObjectIdentifier + ".34");
        secp521r1 = new DERObjectIdentifier(dERObjectIdentifier + ".35");
        sect409k1 = new DERObjectIdentifier(dERObjectIdentifier + ".36");
        sect409r1 = new DERObjectIdentifier(dERObjectIdentifier + ".37");
        sect571k1 = new DERObjectIdentifier(dERObjectIdentifier + ".38");
        sect571r1 = new DERObjectIdentifier(dERObjectIdentifier + ".39");
        secp192r1 = X9ObjectIdentifiers.prime192v1;
        secp256r1 = X9ObjectIdentifiers.prime256v1;
    }
}
