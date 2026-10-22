package org.bouncycastle.asn1.x509.sigi;

import org.bouncycastle.asn1.DERObjectIdentifier;

/* JADX INFO: loaded from: classes2.dex */
public interface SigIObjectIdentifiers {
    public static final DERObjectIdentifier id_sigi;
    public static final DERObjectIdentifier id_sigi_cp;
    public static final DERObjectIdentifier id_sigi_cp_sigconform;
    public static final DERObjectIdentifier id_sigi_kp;
    public static final DERObjectIdentifier id_sigi_kp_directoryService;
    public static final DERObjectIdentifier id_sigi_on;
    public static final DERObjectIdentifier id_sigi_on_personalData;

    static {
        DERObjectIdentifier dERObjectIdentifier = new DERObjectIdentifier("1.3.36.8");
        id_sigi = dERObjectIdentifier;
        DERObjectIdentifier dERObjectIdentifier2 = new DERObjectIdentifier(dERObjectIdentifier + ".2");
        id_sigi_kp = dERObjectIdentifier2;
        DERObjectIdentifier dERObjectIdentifier3 = new DERObjectIdentifier(dERObjectIdentifier + ".1");
        id_sigi_cp = dERObjectIdentifier3;
        DERObjectIdentifier dERObjectIdentifier4 = new DERObjectIdentifier(dERObjectIdentifier + ".4");
        id_sigi_on = dERObjectIdentifier4;
        id_sigi_kp_directoryService = new DERObjectIdentifier(dERObjectIdentifier2 + ".1");
        id_sigi_on_personalData = new DERObjectIdentifier(dERObjectIdentifier4 + ".1");
        id_sigi_cp_sigconform = new DERObjectIdentifier(dERObjectIdentifier3 + ".1");
    }
}
