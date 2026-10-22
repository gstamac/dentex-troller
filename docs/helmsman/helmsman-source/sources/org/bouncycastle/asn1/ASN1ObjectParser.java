package org.bouncycastle.asn1;

import java.io.InputStream;

/* JADX INFO: loaded from: classes2.dex */
public class ASN1ObjectParser {
    ASN1StreamParser _aIn;

    protected ASN1ObjectParser(int i, int i2, InputStream inputStream) {
        this._aIn = new ASN1StreamParser(inputStream);
    }
}
