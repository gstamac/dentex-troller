package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

/* JADX INFO: loaded from: classes2.dex */
public class BERInputStream extends DERInputStream {
    private static final DERObject END_OF_STREAM = new DERObject() { // from class: org.bouncycastle.asn1.BERInputStream.1
        @Override // org.bouncycastle.asn1.DERObject
        void encode(DEROutputStream dEROutputStream) throws IOException {
            throw new IOException("Eeek!");
        }

        @Override // org.bouncycastle.asn1.DERObject, org.bouncycastle.asn1.ASN1Encodable
        public boolean equals(Object obj) {
            return obj == this;
        }

        @Override // org.bouncycastle.asn1.DERObject, org.bouncycastle.asn1.ASN1Encodable
        public int hashCode() {
            return 0;
        }
    };

    public BERInputStream(InputStream inputStream) {
        super(inputStream);
    }

    private BERConstructedOctetString buildConstructedOctetString() throws IOException {
        Vector vector = new Vector();
        while (true) {
            DERObject object = readObject();
            if (object == END_OF_STREAM) {
                return new BERConstructedOctetString(vector);
            }
            vector.addElement(object);
        }
    }

    private byte[] readIndefiniteLengthFully() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = read();
        while (true) {
            int i2 = read();
            if (i2 < 0 || (i == 0 && i2 == 0)) {
                break;
            }
            byteArrayOutputStream.write(i);
            i = i2;
        }
        return byteArrayOutputStream.toByteArray();
    }

    @Override // org.bouncycastle.asn1.DERInputStream
    public DERObject readObject() throws IOException {
        int i = read();
        if (i == -1) {
            throw new EOFException();
        }
        int length = readLength();
        if (length >= 0) {
            if (i == 0 && length == 0) {
                return END_OF_STREAM;
            }
            byte[] bArr = new byte[length];
            readFully(bArr);
            return buildObject(i, bArr);
        }
        if (i == 5) {
            return null;
        }
        if (i == 36) {
            return buildConstructedOctetString();
        }
        if (i == 48) {
            BERConstructedSequence bERConstructedSequence = new BERConstructedSequence();
            while (true) {
                DERObject object = readObject();
                if (object == END_OF_STREAM) {
                    return bERConstructedSequence;
                }
                bERConstructedSequence.addObject(object);
            }
        } else {
            if (i != 49) {
                if ((i & 128) == 0) {
                    throw new IOException("unknown BER object encountered");
                }
                int i2 = i & 31;
                if (i2 == 31) {
                    throw new IOException("unsupported high tag encountered");
                }
                if ((i & 32) == 0) {
                    return new BERTaggedObject(false, i2, new DEROctetString(readIndefiniteLengthFully()));
                }
                DERObject object2 = readObject();
                DERObject dERObject = END_OF_STREAM;
                if (object2 == dERObject) {
                    return new DERTaggedObject(i2);
                }
                DERObject object3 = readObject();
                if (object3 == dERObject) {
                    return new BERTaggedObject(i2, object2);
                }
                BERConstructedSequence bERConstructedSequence2 = new BERConstructedSequence();
                bERConstructedSequence2.addObject(object2);
                do {
                    bERConstructedSequence2.addObject(object3);
                    object3 = readObject();
                } while (object3 != END_OF_STREAM);
                return new BERTaggedObject(false, i2, bERConstructedSequence2);
            }
            ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
            while (true) {
                DERObject object4 = readObject();
                if (object4 == END_OF_STREAM) {
                    return new BERSet(aSN1EncodableVector);
                }
                aSN1EncodableVector.add(object4);
            }
        }
    }
}
