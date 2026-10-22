package org.bouncycastle.asn1;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes2.dex */
public class ASN1StreamParser {
    private final InputStream _in;
    private final int _limit;

    public ASN1StreamParser(InputStream inputStream) {
        this(inputStream, findLimit(inputStream));
    }

    public ASN1StreamParser(InputStream inputStream, int i) {
        this._in = inputStream;
        this._limit = i;
    }

    public ASN1StreamParser(byte[] bArr) {
        this(new ByteArrayInputStream(bArr), bArr.length);
    }

    private static int findLimit(InputStream inputStream) {
        if (inputStream instanceof DefiniteLengthInputStream) {
            return ((DefiniteLengthInputStream) inputStream).getRemaining();
        }
        return Integer.MAX_VALUE;
    }

    private void set00Check(boolean z) {
        InputStream inputStream = this._in;
        if (inputStream instanceof IndefiniteLengthInputStream) {
            ((IndefiniteLengthInputStream) inputStream).setEofOn00(z);
        }
    }

    public DEREncodable readObject() throws IOException {
        int i = this._in.read();
        if (i == -1) {
            return null;
        }
        set00Check(false);
        int tagNumber = ASN1InputStream.readTagNumber(this._in, i);
        boolean z = (i & 32) != 0;
        int length = ASN1InputStream.readLength(this._in, this._limit);
        if (length >= 0) {
            DefiniteLengthInputStream definiteLengthInputStream = new DefiniteLengthInputStream(this._in, length);
            if ((i & 64) != 0) {
                return new DERApplicationSpecific(z, tagNumber, definiteLengthInputStream.toByteArray());
            }
            if ((i & 128) != 0) {
                return new BERTaggedObjectParser(i, tagNumber, definiteLengthInputStream);
            }
            if (!z) {
                return tagNumber != 4 ? ASN1InputStream.createPrimitiveDERObject(tagNumber, definiteLengthInputStream.toByteArray()) : new DEROctetStringParser(definiteLengthInputStream);
            }
            if (tagNumber == 4) {
                return new BEROctetStringParser(new ASN1StreamParser(definiteLengthInputStream));
            }
            if (tagNumber == 8) {
                return new DERExternalParser(new ASN1StreamParser(definiteLengthInputStream));
            }
            if (tagNumber != 16) {
                return tagNumber != 17 ? new DERUnknownTag(true, tagNumber, definiteLengthInputStream.toByteArray()) : new DERSetParser(new ASN1StreamParser(definiteLengthInputStream));
            }
            return new DERSequenceParser(new ASN1StreamParser(definiteLengthInputStream));
        }
        if (!z) {
            throw new IOException("indefinite length primitive encoding encountered");
        }
        IndefiniteLengthInputStream indefiniteLengthInputStream = new IndefiniteLengthInputStream(this._in);
        if ((i & 64) != 0) {
            return new BERApplicationSpecificParser(tagNumber, new ASN1StreamParser(indefiniteLengthInputStream, this._limit));
        }
        if ((i & 128) != 0) {
            return new BERTaggedObjectParser(i, tagNumber, indefiniteLengthInputStream);
        }
        ASN1StreamParser aSN1StreamParser = new ASN1StreamParser(indefiniteLengthInputStream, this._limit);
        if (tagNumber == 4) {
            return new BEROctetStringParser(aSN1StreamParser);
        }
        if (tagNumber == 8) {
            return new DERExternalParser(aSN1StreamParser);
        }
        if (tagNumber == 16) {
            return new BERSequenceParser(aSN1StreamParser);
        }
        if (tagNumber == 17) {
            return new BERSetParser(aSN1StreamParser);
        }
        throw new IOException("unknown BER object encountered: 0x" + Integer.toHexString(tagNumber));
    }

    ASN1EncodableVector readVector() throws IOException {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        while (true) {
            DEREncodable object = readObject();
            if (object == null) {
                return aSN1EncodableVector;
            }
            aSN1EncodableVector.add(object.getDERObject());
        }
    }
}
