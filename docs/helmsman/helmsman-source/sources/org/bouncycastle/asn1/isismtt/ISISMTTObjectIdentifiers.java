package org.bouncycastle.asn1.isismtt;

import org.bouncycastle.asn1.DERObjectIdentifier;

/* JADX INFO: loaded from: classes2.dex */
public interface ISISMTTObjectIdentifiers {
    public static final DERObjectIdentifier id_isismtt;
    public static final DERObjectIdentifier id_isismtt_at;
    public static final DERObjectIdentifier id_isismtt_at_PKReference;
    public static final DERObjectIdentifier id_isismtt_at_additionalInformation;
    public static final DERObjectIdentifier id_isismtt_at_admission;
    public static final DERObjectIdentifier id_isismtt_at_certHash;
    public static final DERObjectIdentifier id_isismtt_at_certInDirSince;
    public static final DERObjectIdentifier id_isismtt_at_dateOfCertGen;
    public static final DERObjectIdentifier id_isismtt_at_declarationOfMajority;
    public static final DERObjectIdentifier id_isismtt_at_iCCSN;
    public static final DERObjectIdentifier id_isismtt_at_liabilityLimitationFlag;
    public static final DERObjectIdentifier id_isismtt_at_monetaryLimit;
    public static final DERObjectIdentifier id_isismtt_at_nameAtBirth;
    public static final DERObjectIdentifier id_isismtt_at_namingAuthorities;
    public static final DERObjectIdentifier id_isismtt_at_procuration;
    public static final DERObjectIdentifier id_isismtt_at_requestedCertificate;
    public static final DERObjectIdentifier id_isismtt_at_restriction;
    public static final DERObjectIdentifier id_isismtt_at_retrieveIfAllowed;
    public static final DERObjectIdentifier id_isismtt_cp;
    public static final DERObjectIdentifier id_isismtt_cp_accredited;

    static {
        DERObjectIdentifier dERObjectIdentifier = new DERObjectIdentifier("1.3.36.8");
        id_isismtt = dERObjectIdentifier;
        DERObjectIdentifier dERObjectIdentifier2 = new DERObjectIdentifier(dERObjectIdentifier + ".1");
        id_isismtt_cp = dERObjectIdentifier2;
        id_isismtt_cp_accredited = new DERObjectIdentifier(dERObjectIdentifier2 + ".1");
        DERObjectIdentifier dERObjectIdentifier3 = new DERObjectIdentifier(dERObjectIdentifier + ".3");
        id_isismtt_at = dERObjectIdentifier3;
        id_isismtt_at_dateOfCertGen = new DERObjectIdentifier(dERObjectIdentifier3 + ".1");
        id_isismtt_at_procuration = new DERObjectIdentifier(dERObjectIdentifier3 + ".2");
        id_isismtt_at_admission = new DERObjectIdentifier(dERObjectIdentifier3 + ".3");
        id_isismtt_at_monetaryLimit = new DERObjectIdentifier(dERObjectIdentifier3 + ".4");
        id_isismtt_at_declarationOfMajority = new DERObjectIdentifier(dERObjectIdentifier3 + ".5");
        id_isismtt_at_iCCSN = new DERObjectIdentifier(dERObjectIdentifier3 + ".6");
        id_isismtt_at_PKReference = new DERObjectIdentifier(dERObjectIdentifier3 + ".7");
        id_isismtt_at_restriction = new DERObjectIdentifier(dERObjectIdentifier3 + ".8");
        id_isismtt_at_retrieveIfAllowed = new DERObjectIdentifier(dERObjectIdentifier3 + ".9");
        id_isismtt_at_requestedCertificate = new DERObjectIdentifier(dERObjectIdentifier3 + ".10");
        id_isismtt_at_namingAuthorities = new DERObjectIdentifier(dERObjectIdentifier3 + ".11");
        id_isismtt_at_certInDirSince = new DERObjectIdentifier(dERObjectIdentifier3 + ".12");
        id_isismtt_at_certHash = new DERObjectIdentifier(dERObjectIdentifier3 + ".13");
        id_isismtt_at_nameAtBirth = new DERObjectIdentifier(dERObjectIdentifier3 + ".14");
        id_isismtt_at_additionalInformation = new DERObjectIdentifier(dERObjectIdentifier3 + ".15");
        id_isismtt_at_liabilityLimitationFlag = new DERObjectIdentifier("0.2.262.1.10.12.0");
    }
}
