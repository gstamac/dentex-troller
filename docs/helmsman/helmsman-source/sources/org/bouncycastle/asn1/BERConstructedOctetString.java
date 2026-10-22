package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

/* JADX INFO: loaded from: classes2.dex */
public class BERConstructedOctetString extends DEROctetString {
    private static final int MAX_LENGTH = 1000;
    private Vector octs;

    public BERConstructedOctetString(Vector vector) {
        super(toBytes(vector));
        this.octs = vector;
    }

    public BERConstructedOctetString(DEREncodable dEREncodable) {
        super(dEREncodable.getDERObject());
    }

    public BERConstructedOctetString(DERObject dERObject) {
        super(dERObject);
    }

    public BERConstructedOctetString(byte[] bArr) {
        super(bArr);
    }

    private Vector generateOcts() {
        Vector vector = new Vector();
        int i = 0;
        while (i < this.string.length) {
            int i2 = i + 1000;
            int length = (i2 > this.string.length ? this.string.length : i2) - i;
            byte[] bArr = new byte[length];
            System.arraycopy(this.string, i, bArr, 0, length);
            vector.addElement(new DEROctetString(bArr));
            i = i2;
        }
        return vector;
    }

    private static byte[] toBytes(Vector vector) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i = 0; i != vector.size(); i++) {
            try {
                byteArrayOutputStream.write(((DEROctetString) vector.elementAt(i)).getOctets());
            } catch (IOException e) {
                throw new IllegalArgumentException("exception converting octets " + e.toString());
            } catch (ClassCastException unused) {
                throw new IllegalArgumentException(vector.elementAt(i).getClass().getName() + " found in input should only contain DEROctetString");
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    @Override // org.bouncycastle.asn1.DEROctetString, org.bouncycastle.asn1.ASN1OctetString, org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject
    public void encode(DEROutputStream dEROutputStream) throws IOException {
        if (!(dEROutputStream instanceof ASN1OutputStream) && !(dEROutputStream instanceof BEROutputStream)) {
            super.encode(dEROutputStream);
            return;
        }
        dEROutputStream.write(36);
        dEROutputStream.write(128);
        Enumeration objects = getObjects();
        while (objects.hasMoreElements()) {
            dEROutputStream.writeObject(objects.nextElement());
        }
        dEROutputStream.write(0);
        dEROutputStream.write(0);
    }

    public Enumeration getObjects() {
        Vector vector = this.octs;
        return vector == null ? generateOcts().elements() : vector.elements();
    }

    @Override // org.bouncycastle.asn1.ASN1OctetString
    public byte[] getOctets() {
        return this.string;
    }
}
