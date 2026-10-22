package org.bouncycastle.asn1;

import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes2.dex */
public class BERTaggedObjectParser implements ASN1TaggedObjectParser {
    private int _baseTag;
    private InputStream _contentStream;
    private boolean _indefiniteLength;
    private int _tagNumber;

    protected BERTaggedObjectParser(int i, int i2, InputStream inputStream) {
        this._baseTag = i;
        this._tagNumber = i2;
        this._contentStream = inputStream;
        this._indefiniteLength = inputStream instanceof IndefiniteLengthInputStream;
    }

    private ASN1EncodableVector rLoadVector(InputStream inputStream) {
        try {
            return new ASN1StreamParser(inputStream).readVector();
        } catch (IOException e) {
            throw new ASN1ParsingException(e.getMessage(), e);
        }
    }

    @Override // org.bouncycastle.asn1.DEREncodable
    public DERObject getDERObject() {
        if (this._indefiniteLength) {
            ASN1EncodableVector aSN1EncodableVectorRLoadVector = rLoadVector(this._contentStream);
            return aSN1EncodableVectorRLoadVector.size() == 1 ? new BERTaggedObject(true, this._tagNumber, aSN1EncodableVectorRLoadVector.get(0)) : new BERTaggedObject(false, this._tagNumber, BERFactory.createSequence(aSN1EncodableVectorRLoadVector));
        }
        if (isConstructed()) {
            ASN1EncodableVector aSN1EncodableVectorRLoadVector2 = rLoadVector(this._contentStream);
            return aSN1EncodableVectorRLoadVector2.size() == 1 ? new DERTaggedObject(true, this._tagNumber, aSN1EncodableVectorRLoadVector2.get(0)) : new DERTaggedObject(false, this._tagNumber, DERFactory.createSequence(aSN1EncodableVectorRLoadVector2));
        }
        try {
            return new DERTaggedObject(false, this._tagNumber, new DEROctetString(((DefiniteLengthInputStream) this._contentStream).toByteArray()));
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    @Override // org.bouncycastle.asn1.ASN1TaggedObjectParser
    public DEREncodable getObjectParser(int i, boolean z) throws IOException {
        if (z) {
            return new ASN1StreamParser(this._contentStream).readObject();
        }
        if (i == 4) {
            return (this._indefiniteLength || isConstructed()) ? new BEROctetStringParser(new ASN1StreamParser(this._contentStream)) : new DEROctetStringParser((DefiniteLengthInputStream) this._contentStream);
        }
        if (i == 16) {
            return this._indefiniteLength ? new BERSequenceParser(new ASN1StreamParser(this._contentStream)) : new DERSequenceParser(new ASN1StreamParser(this._contentStream));
        }
        if (i == 17) {
            return this._indefiniteLength ? new BERSetParser(new ASN1StreamParser(this._contentStream)) : new DERSetParser(new ASN1StreamParser(this._contentStream));
        }
        throw new RuntimeException("implicit tagging not implemented");
    }

    @Override // org.bouncycastle.asn1.ASN1TaggedObjectParser
    public int getTagNo() {
        return this._tagNumber;
    }

    public boolean isConstructed() {
        return (this._baseTag & 32) != 0;
    }
}
