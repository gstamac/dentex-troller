package org.bouncycastle.asn1;

import java.io.IOException;

/* JADX INFO: loaded from: classes2.dex */
public interface ASN1TaggedObjectParser extends DEREncodable {
    DEREncodable getObjectParser(int i, boolean z) throws IOException;

    int getTagNo();
}
