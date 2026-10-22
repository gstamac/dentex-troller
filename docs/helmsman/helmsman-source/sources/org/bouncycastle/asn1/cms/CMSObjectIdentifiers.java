package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;

/* JADX INFO: loaded from: classes2.dex */
public interface CMSObjectIdentifiers {
    public static final DERObjectIdentifier data = PKCSObjectIdentifiers.data;
    public static final DERObjectIdentifier signedData = PKCSObjectIdentifiers.signedData;
    public static final DERObjectIdentifier envelopedData = PKCSObjectIdentifiers.envelopedData;
    public static final DERObjectIdentifier signedAndEnvelopedData = PKCSObjectIdentifiers.signedAndEnvelopedData;
    public static final DERObjectIdentifier digestedData = PKCSObjectIdentifiers.digestedData;
    public static final DERObjectIdentifier encryptedData = PKCSObjectIdentifiers.encryptedData;
    public static final DERObjectIdentifier authenticatedData = PKCSObjectIdentifiers.id_ct_authData;
    public static final DERObjectIdentifier compressedData = PKCSObjectIdentifiers.id_ct_compressedData;
    public static final DERObjectIdentifier authEnvelopedData = PKCSObjectIdentifiers.id_ct_authEnvelopedData;
}
