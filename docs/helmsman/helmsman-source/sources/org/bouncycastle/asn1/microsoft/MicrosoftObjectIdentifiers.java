package org.bouncycastle.asn1.microsoft;

import org.bouncycastle.asn1.DERObjectIdentifier;

/* JADX INFO: loaded from: classes2.dex */
public interface MicrosoftObjectIdentifiers {
    public static final DERObjectIdentifier microsoft;
    public static final DERObjectIdentifier microsoftAppPolicies;
    public static final DERObjectIdentifier microsoftCaVersion;
    public static final DERObjectIdentifier microsoftCertTemplateV1;
    public static final DERObjectIdentifier microsoftCertTemplateV2;
    public static final DERObjectIdentifier microsoftPrevCaCertHash;

    static {
        DERObjectIdentifier dERObjectIdentifier = new DERObjectIdentifier("1.3.6.1.4.1.311");
        microsoft = dERObjectIdentifier;
        microsoftCertTemplateV1 = new DERObjectIdentifier(dERObjectIdentifier + ".20.2");
        microsoftCaVersion = new DERObjectIdentifier(dERObjectIdentifier + ".21.1");
        microsoftPrevCaCertHash = new DERObjectIdentifier(dERObjectIdentifier + ".21.2");
        microsoftCertTemplateV2 = new DERObjectIdentifier(dERObjectIdentifier + ".21.7");
        microsoftAppPolicies = new DERObjectIdentifier(dERObjectIdentifier + ".21.10");
    }
}
