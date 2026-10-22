package org.bouncycastle.asn1.icao;

import org.bouncycastle.asn1.DERObjectIdentifier;

/* JADX INFO: loaded from: classes2.dex */
public interface ICAOObjectIdentifiers {
    public static final String id_icao = "2.23.136";
    public static final DERObjectIdentifier id_icao_ldsSecurityObject;
    public static final DERObjectIdentifier id_icao_mrtd;
    public static final DERObjectIdentifier id_icao_mrtd_security;

    static {
        DERObjectIdentifier dERObjectIdentifier = new DERObjectIdentifier("2.23.136.1");
        id_icao_mrtd = dERObjectIdentifier;
        DERObjectIdentifier dERObjectIdentifier2 = new DERObjectIdentifier(dERObjectIdentifier + ".1");
        id_icao_mrtd_security = dERObjectIdentifier2;
        id_icao_ldsSecurityObject = new DERObjectIdentifier(dERObjectIdentifier2 + ".1");
    }
}
