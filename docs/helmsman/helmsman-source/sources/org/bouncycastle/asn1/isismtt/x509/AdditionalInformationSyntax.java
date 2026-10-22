package org.bouncycastle.asn1.isismtt.x509;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERString;
import org.bouncycastle.asn1.x500.DirectoryString;

/* JADX INFO: loaded from: classes2.dex */
public class AdditionalInformationSyntax extends ASN1Encodable {
    private DirectoryString information;

    public AdditionalInformationSyntax(String str) {
        this(new DirectoryString(str));
    }

    private AdditionalInformationSyntax(DirectoryString directoryString) {
        this.information = directoryString;
    }

    public static AdditionalInformationSyntax getInstance(Object obj) {
        if (obj == null || (obj instanceof AdditionalInformationSyntax)) {
            return (AdditionalInformationSyntax) obj;
        }
        if (obj instanceof DERString) {
            return new AdditionalInformationSyntax(DirectoryString.getInstance(obj));
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    public DirectoryString getInformation() {
        return this.information;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        return this.information.toASN1Object();
    }
}
