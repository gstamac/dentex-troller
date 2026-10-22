package org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;

/* JADX INFO: loaded from: classes2.dex */
public class LazyDERSequence extends DERSequence {
    private byte[] encoded;
    private boolean parsed = false;
    private int size = -1;

    LazyDERSequence(byte[] bArr) throws IOException {
        this.encoded = bArr;
    }

    private void parse() {
        LazyDERConstructionEnumeration lazyDERConstructionEnumeration = new LazyDERConstructionEnumeration(this.encoded);
        while (lazyDERConstructionEnumeration.hasMoreElements()) {
            addObject((DEREncodable) lazyDERConstructionEnumeration.nextElement());
        }
        this.parsed = true;
    }

    @Override // org.bouncycastle.asn1.DERSequence, org.bouncycastle.asn1.ASN1Sequence, org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject
    void encode(DEROutputStream dEROutputStream) throws IOException {
        dEROutputStream.writeEncoded(48, this.encoded);
    }

    @Override // org.bouncycastle.asn1.ASN1Sequence
    public DEREncodable getObjectAt(int i) {
        if (!this.parsed) {
            parse();
        }
        return super.getObjectAt(i);
    }

    @Override // org.bouncycastle.asn1.ASN1Sequence
    public Enumeration getObjects() {
        return this.parsed ? super.getObjects() : new LazyDERConstructionEnumeration(this.encoded);
    }

    @Override // org.bouncycastle.asn1.ASN1Sequence
    public int size() {
        if (this.size < 0) {
            LazyDERConstructionEnumeration lazyDERConstructionEnumeration = new LazyDERConstructionEnumeration(this.encoded);
            int i = 0;
            while (true) {
                this.size = i;
                if (!lazyDERConstructionEnumeration.hasMoreElements()) {
                    break;
                }
                lazyDERConstructionEnumeration.nextElement();
                i = this.size + 1;
            }
        }
        return this.size;
    }
}
