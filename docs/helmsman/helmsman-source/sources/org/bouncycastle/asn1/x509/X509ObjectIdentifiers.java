package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.ocsp.CertificateID;

/* JADX INFO: loaded from: classes2.dex */
public interface X509ObjectIdentifiers {
    public static final DERObjectIdentifier crlAccessMethod;
    public static final String id = "2.5.4";
    public static final DERObjectIdentifier id_ad;
    public static final DERObjectIdentifier id_ad_caIssuers;
    public static final DERObjectIdentifier id_ad_ocsp;
    public static final DERObjectIdentifier id_pe;
    public static final DERObjectIdentifier id_pkix;
    public static final DERObjectIdentifier ocspAccessMethod;
    public static final DERObjectIdentifier commonName = new DERObjectIdentifier("2.5.4.3");
    public static final DERObjectIdentifier countryName = new DERObjectIdentifier("2.5.4.6");
    public static final DERObjectIdentifier localityName = new DERObjectIdentifier("2.5.4.7");
    public static final DERObjectIdentifier stateOrProvinceName = new DERObjectIdentifier("2.5.4.8");
    public static final DERObjectIdentifier organization = new DERObjectIdentifier("2.5.4.10");
    public static final DERObjectIdentifier organizationalUnitName = new DERObjectIdentifier("2.5.4.11");
    public static final DERObjectIdentifier id_at_telephoneNumber = new DERObjectIdentifier("2.5.4.20");
    public static final DERObjectIdentifier id_at_name = new DERObjectIdentifier("2.5.4.41");
    public static final DERObjectIdentifier id_SHA1 = new DERObjectIdentifier(CertificateID.HASH_SHA1);
    public static final DERObjectIdentifier ripemd160 = new DERObjectIdentifier("1.3.36.3.2.1");
    public static final DERObjectIdentifier ripemd160WithRSAEncryption = new DERObjectIdentifier("1.3.36.3.3.1.2");
    public static final DERObjectIdentifier id_ea_rsa = new DERObjectIdentifier("2.5.8.1.1");

    static {
        DERObjectIdentifier dERObjectIdentifier = new DERObjectIdentifier("1.3.6.1.5.5.7");
        id_pkix = dERObjectIdentifier;
        id_pe = new DERObjectIdentifier(dERObjectIdentifier + ".1");
        DERObjectIdentifier dERObjectIdentifier2 = new DERObjectIdentifier(dERObjectIdentifier + ".48");
        id_ad = dERObjectIdentifier2;
        DERObjectIdentifier dERObjectIdentifier3 = new DERObjectIdentifier(dERObjectIdentifier2 + ".2");
        id_ad_caIssuers = dERObjectIdentifier3;
        DERObjectIdentifier dERObjectIdentifier4 = new DERObjectIdentifier(dERObjectIdentifier2 + ".1");
        id_ad_ocsp = dERObjectIdentifier4;
        ocspAccessMethod = dERObjectIdentifier4;
        crlAccessMethod = dERObjectIdentifier3;
    }
}
