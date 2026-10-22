package org.bouncycastle.asn1;

import java.io.IOException;

/* JADX INFO: loaded from: classes2.dex */
public interface ASN1SequenceParser extends DEREncodable {
    DEREncodable readObject() throws IOException;
}
