package org.bouncycastle.asn1;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.util.io.Streams;

/* JADX INFO: loaded from: classes2.dex */
public class ASN1InputStream extends FilterInputStream implements DERTags {
    private final boolean lazyEvaluate;
    private final int limit;

    public ASN1InputStream(InputStream inputStream) {
        this(inputStream, Integer.MAX_VALUE);
    }

    public ASN1InputStream(InputStream inputStream, int i) {
        this(inputStream, i, false);
    }

    public ASN1InputStream(InputStream inputStream, int i, boolean z) {
        super(inputStream);
        this.limit = i;
        this.lazyEvaluate = z;
    }

    public ASN1InputStream(byte[] bArr) {
        this(new ByteArrayInputStream(bArr), bArr.length);
    }

    public ASN1InputStream(byte[] bArr, boolean z) {
        this(new ByteArrayInputStream(bArr), bArr.length, z);
    }

    static DERObject createPrimitiveDERObject(int i, byte[] bArr) {
        switch (i) {
            case 1:
                return new DERBoolean(bArr);
            case 2:
                return new DERInteger(bArr);
            case 3:
                byte b = bArr[0];
                byte[] bArr2 = new byte[bArr.length - 1];
                System.arraycopy(bArr, 1, bArr2, 0, bArr.length - 1);
                return new DERBitString(bArr2, b);
            case 4:
                return new DEROctetString(bArr);
            case 5:
                return DERNull.INSTANCE;
            case 6:
                return new DERObjectIdentifier(bArr);
            case 7:
            case 8:
            case 9:
            case 11:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 21:
            case 25:
            case 29:
            default:
                return new DERUnknownTag(false, i, bArr);
            case 10:
                return new DEREnumerated(bArr);
            case 12:
                return new DERUTF8String(bArr);
            case 18:
                return new DERNumericString(bArr);
            case 19:
                return new DERPrintableString(bArr);
            case 20:
                return new DERT61String(bArr);
            case 22:
                return new DERIA5String(bArr);
            case 23:
                return new DERUTCTime(bArr);
            case 24:
                return new DERGeneralizedTime(bArr);
            case 26:
                return new DERVisibleString(bArr);
            case 27:
                return new DERGeneralString(bArr);
            case 28:
                return new DERUniversalString(bArr);
            case 30:
                return new DERBMPString(bArr);
        }
    }

    static int readLength(InputStream inputStream, int i) throws IOException {
        int i2 = inputStream.read();
        if (i2 < 0) {
            throw new EOFException("EOF found when length expected");
        }
        if (i2 == 128) {
            return -1;
        }
        if (i2 <= 127) {
            return i2;
        }
        int i3 = i2 & 127;
        if (i3 > 4) {
            throw new IOException("DER length more than 4 bytes: " + i3);
        }
        int i4 = 0;
        for (int i5 = 0; i5 < i3; i5++) {
            int i6 = inputStream.read();
            if (i6 < 0) {
                throw new EOFException("EOF found reading length");
            }
            i4 = (i4 << 8) + i6;
        }
        if (i4 < 0) {
            throw new IOException("corrupted stream - negative length found");
        }
        if (i4 < i) {
            return i4;
        }
        throw new IOException("corrupted stream - out of bounds length found");
    }

    static int readTagNumber(InputStream inputStream, int i) throws IOException {
        int i2 = i & 31;
        if (i2 != 31) {
            return i2;
        }
        int i3 = inputStream.read();
        if ((i3 & 127) == 0) {
            throw new IOException("corrupted stream - invalid high tag number found");
        }
        int i4 = 0;
        while (i3 >= 0 && (i3 & 128) != 0) {
            i4 = ((i3 & 127) | i4) << 7;
            i3 = inputStream.read();
        }
        if (i3 >= 0) {
            return i4 | (i3 & 127);
        }
        throw new EOFException("EOF found inside tag value.");
    }

    ASN1EncodableVector buildDEREncodableVector(DefiniteLengthInputStream definiteLengthInputStream) throws IOException {
        return new ASN1InputStream(definiteLengthInputStream).buildEncodableVector();
    }

    ASN1EncodableVector buildEncodableVector() throws IOException {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        while (true) {
            DERObject object = readObject();
            if (object == null) {
                return aSN1EncodableVector;
            }
            aSN1EncodableVector.add(object);
        }
    }

    protected DERObject buildObject(int i, int i2, int i3) throws IOException {
        boolean z = (i & 32) != 0;
        DefiniteLengthInputStream definiteLengthInputStream = new DefiniteLengthInputStream(this, i3);
        if ((i & 64) != 0) {
            return new DERApplicationSpecific(z, i2, definiteLengthInputStream.toByteArray());
        }
        if ((i & 128) != 0) {
            return new BERTaggedObjectParser(i, i2, definiteLengthInputStream).getDERObject();
        }
        if (!z) {
            return createPrimitiveDERObject(i2, definiteLengthInputStream.toByteArray());
        }
        if (i2 == 4) {
            return new BERConstructedOctetString(buildDEREncodableVector(definiteLengthInputStream).v);
        }
        if (i2 == 8) {
            return new DERExternal(buildDEREncodableVector(definiteLengthInputStream));
        }
        if (i2 != 16) {
            return i2 != 17 ? new DERUnknownTag(true, i2, definiteLengthInputStream.toByteArray()) : DERFactory.createSet(buildDEREncodableVector(definiteLengthInputStream), false);
        }
        return this.lazyEvaluate ? new LazyDERSequence(definiteLengthInputStream.toByteArray()) : DERFactory.createSequence(buildDEREncodableVector(definiteLengthInputStream));
    }

    protected void readFully(byte[] bArr) throws IOException {
        if (Streams.readFully(this, bArr) != bArr.length) {
            throw new EOFException("EOF encountered in middle of object");
        }
    }

    protected int readLength() throws IOException {
        return readLength(this, this.limit);
    }

    public DERObject readObject() throws IOException {
        int i = read();
        if (i <= 0) {
            if (i != 0) {
                return null;
            }
            throw new IOException("unexpected end-of-contents marker");
        }
        int tagNumber = readTagNumber(this, i);
        boolean z = (i & 32) != 0;
        int length = readLength();
        if (length >= 0) {
            return buildObject(i, tagNumber, length);
        }
        if (!z) {
            throw new IOException("indefinite length primitive encoding encountered");
        }
        IndefiniteLengthInputStream indefiniteLengthInputStream = new IndefiniteLengthInputStream(this);
        if ((i & 64) != 0) {
            return new BERApplicationSpecificParser(tagNumber, new ASN1StreamParser(indefiniteLengthInputStream, this.limit)).getDERObject();
        }
        if ((i & 128) != 0) {
            return new BERTaggedObjectParser(i, tagNumber, indefiniteLengthInputStream).getDERObject();
        }
        ASN1StreamParser aSN1StreamParser = new ASN1StreamParser(indefiniteLengthInputStream, this.limit);
        if (tagNumber == 4) {
            return new BEROctetStringParser(aSN1StreamParser).getDERObject();
        }
        if (tagNumber == 8) {
            return new DERExternalParser(aSN1StreamParser).getDERObject();
        }
        if (tagNumber == 16) {
            return new BERSequenceParser(aSN1StreamParser).getDERObject();
        }
        if (tagNumber == 17) {
            return new BERSetParser(aSN1StreamParser).getDERObject();
        }
        throw new IOException("unknown BER object encountered");
    }
}
