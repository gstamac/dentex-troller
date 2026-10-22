package org.bouncycastle.asn1.teletrust;

import org.bouncycastle.asn1.DERObjectIdentifier;

/* JADX INFO: loaded from: classes2.dex */
public interface TeleTrusTObjectIdentifiers {
    public static final DERObjectIdentifier brainpoolP160r1;
    public static final DERObjectIdentifier brainpoolP160t1;
    public static final DERObjectIdentifier brainpoolP192r1;
    public static final DERObjectIdentifier brainpoolP192t1;
    public static final DERObjectIdentifier brainpoolP224r1;
    public static final DERObjectIdentifier brainpoolP224t1;
    public static final DERObjectIdentifier brainpoolP256r1;
    public static final DERObjectIdentifier brainpoolP256t1;
    public static final DERObjectIdentifier brainpoolP320r1;
    public static final DERObjectIdentifier brainpoolP320t1;
    public static final DERObjectIdentifier brainpoolP384r1;
    public static final DERObjectIdentifier brainpoolP384t1;
    public static final DERObjectIdentifier brainpoolP512r1;
    public static final DERObjectIdentifier brainpoolP512t1;
    public static final DERObjectIdentifier ecSign;
    public static final DERObjectIdentifier ecSignWithRipemd160;
    public static final DERObjectIdentifier ecSignWithSha1;
    public static final DERObjectIdentifier ecc_brainpool;
    public static final DERObjectIdentifier ellipticCurve;
    public static final String teleTrusTAlgorithm = "1.3.36.3";
    public static final String teleTrusTRSAsignatureAlgorithm = "1.3.36.3.3.1";
    public static final DERObjectIdentifier versionOne;
    public static final DERObjectIdentifier ripemd160 = new DERObjectIdentifier("1.3.36.3.2.1");
    public static final DERObjectIdentifier ripemd128 = new DERObjectIdentifier("1.3.36.3.2.2");
    public static final DERObjectIdentifier ripemd256 = new DERObjectIdentifier("1.3.36.3.2.3");
    public static final DERObjectIdentifier rsaSignatureWithripemd160 = new DERObjectIdentifier("1.3.36.3.3.1.2");
    public static final DERObjectIdentifier rsaSignatureWithripemd128 = new DERObjectIdentifier("1.3.36.3.3.1.3");
    public static final DERObjectIdentifier rsaSignatureWithripemd256 = new DERObjectIdentifier("1.3.36.3.3.1.4");

    static {
        DERObjectIdentifier dERObjectIdentifier = new DERObjectIdentifier("1.3.36.3.3.2");
        ecSign = dERObjectIdentifier;
        ecSignWithSha1 = new DERObjectIdentifier(dERObjectIdentifier + ".1");
        ecSignWithRipemd160 = new DERObjectIdentifier(dERObjectIdentifier + ".2");
        DERObjectIdentifier dERObjectIdentifier2 = new DERObjectIdentifier("1.3.36.3.3.2.8");
        ecc_brainpool = dERObjectIdentifier2;
        DERObjectIdentifier dERObjectIdentifier3 = new DERObjectIdentifier(dERObjectIdentifier2 + ".1");
        ellipticCurve = dERObjectIdentifier3;
        DERObjectIdentifier dERObjectIdentifier4 = new DERObjectIdentifier(dERObjectIdentifier3 + ".1");
        versionOne = dERObjectIdentifier4;
        brainpoolP160r1 = new DERObjectIdentifier(dERObjectIdentifier4 + ".1");
        brainpoolP160t1 = new DERObjectIdentifier(dERObjectIdentifier4 + ".2");
        brainpoolP192r1 = new DERObjectIdentifier(dERObjectIdentifier4 + ".3");
        brainpoolP192t1 = new DERObjectIdentifier(dERObjectIdentifier4 + ".4");
        brainpoolP224r1 = new DERObjectIdentifier(dERObjectIdentifier4 + ".5");
        brainpoolP224t1 = new DERObjectIdentifier(dERObjectIdentifier4 + ".6");
        brainpoolP256r1 = new DERObjectIdentifier(dERObjectIdentifier4 + ".7");
        brainpoolP256t1 = new DERObjectIdentifier(dERObjectIdentifier4 + ".8");
        brainpoolP320r1 = new DERObjectIdentifier(dERObjectIdentifier4 + ".9");
        brainpoolP320t1 = new DERObjectIdentifier(dERObjectIdentifier4 + ".10");
        brainpoolP384r1 = new DERObjectIdentifier(dERObjectIdentifier4 + ".11");
        brainpoolP384t1 = new DERObjectIdentifier(dERObjectIdentifier4 + ".12");
        brainpoolP512r1 = new DERObjectIdentifier(dERObjectIdentifier4 + ".13");
        brainpoolP512t1 = new DERObjectIdentifier(dERObjectIdentifier4 + ".14");
    }
}
