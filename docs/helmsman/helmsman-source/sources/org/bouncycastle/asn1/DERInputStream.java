package org.bouncycastle.asn1;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes2.dex */
public class DERInputStream extends FilterInputStream implements DERTags {
    public DERInputStream(InputStream inputStream) {
        super(inputStream);
    }

    protected DERObject buildObject(int i, byte[] bArr) throws IOException {
        if (i == 10) {
            return new DEREnumerated(bArr);
        }
        if (i == 12) {
            return new DERUTF8String(bArr);
        }
        if (i == 30) {
            return new DERBMPString(bArr);
        }
        if (i == 19) {
            return new DERPrintableString(bArr);
        }
        if (i == 20) {
            return new DERT61String(bArr);
        }
        if (i == 48) {
            BERInputStream bERInputStream = new BERInputStream(new ByteArrayInputStream(bArr));
            DERConstructedSequence dERConstructedSequence = new DERConstructedSequence();
            while (true) {
                try {
                    dERConstructedSequence.addObject(bERInputStream.readObject());
                } catch (EOFException unused) {
                    return dERConstructedSequence;
                }
            }
        } else if (i != 49) {
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
                    return null;
                case 6:
                    return new DERObjectIdentifier(bArr);
                default:
                    switch (i) {
                        case 22:
                            return new DERIA5String(bArr);
                        case 23:
                            return new DERUTCTime(bArr);
                        case 24:
                            return new DERGeneralizedTime(bArr);
                        default:
                            switch (i) {
                                case 26:
                                    return new DERVisibleString(bArr);
                                case 27:
                                    return new DERGeneralString(bArr);
                                case 28:
                                    return new DERUniversalString(bArr);
                                default:
                                    if ((i & 128) == 0) {
                                        return new DERUnknownTag(i, bArr);
                                    }
                                    int i2 = i & 31;
                                    if (i2 == 31) {
                                        throw new IOException("unsupported high tag encountered");
                                    }
                                    int i3 = i & 32;
                                    if (bArr.length == 0) {
                                        return i3 == 0 ? new DERTaggedObject(false, i2, new DERNull()) : new DERTaggedObject(false, i2, new DERConstructedSequence());
                                    }
                                    if (i3 == 0) {
                                        return new DERTaggedObject(false, i2, new DEROctetString(bArr));
                                    }
                                    BERInputStream bERInputStream2 = new BERInputStream(new ByteArrayInputStream(bArr));
                                    DERObject object = bERInputStream2.readObject();
                                    if (bERInputStream2.available() == 0) {
                                        return new DERTaggedObject(i2, object);
                                    }
                                    DERConstructedSequence dERConstructedSequence2 = new DERConstructedSequence();
                                    dERConstructedSequence2.addObject(object);
                                    while (true) {
                                        try {
                                            dERConstructedSequence2.addObject(bERInputStream2.readObject());
                                        } catch (EOFException unused2) {
                                            return new DERTaggedObject(false, i2, dERConstructedSequence2);
                                        }
                                    }
                                    break;
                            }
                            break;
                    }
                    break;
            }
        } else {
            BERInputStream bERInputStream3 = new BERInputStream(new ByteArrayInputStream(bArr));
            ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
            while (true) {
                try {
                    aSN1EncodableVector.add(bERInputStream3.readObject());
                } catch (EOFException unused3) {
                    return new DERConstructedSet(aSN1EncodableVector);
                }
            }
        }
    }

    protected void readFully(byte[] bArr) throws IOException {
        int length = bArr.length;
        if (length == 0) {
            return;
        }
        while (length > 0) {
            int i = read(bArr, bArr.length - length, length);
            if (i < 0) {
                throw new EOFException("unexpected end of stream");
            }
            length -= i;
        }
    }

    protected int readLength() throws IOException {
        int i = read();
        if (i < 0) {
            throw new IOException("EOF found when length expected");
        }
        if (i == 128) {
            return -1;
        }
        if (i <= 127) {
            return i;
        }
        int i2 = i & 127;
        if (i2 > 4) {
            throw new IOException("DER length more than 4 bytes");
        }
        int i3 = 0;
        for (int i4 = 0; i4 < i2; i4++) {
            int i5 = read();
            if (i5 < 0) {
                throw new IOException("EOF found reading length");
            }
            i3 = (i3 << 8) + i5;
        }
        if (i3 >= 0) {
            return i3;
        }
        throw new IOException("corrupted stream - negative length found");
    }

    public DERObject readObject() throws IOException {
        int i = read();
        if (i == -1) {
            throw new EOFException();
        }
        byte[] bArr = new byte[readLength()];
        readFully(bArr);
        return buildObject(i, bArr);
    }
}
