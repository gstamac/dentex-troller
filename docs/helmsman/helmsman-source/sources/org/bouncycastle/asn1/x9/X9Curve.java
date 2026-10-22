package org.bouncycastle.asn1.x9;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.math.ec.ECCurve;

/* JADX INFO: loaded from: classes2.dex */
public class X9Curve extends ASN1Encodable implements X9ObjectIdentifiers {
    private ECCurve curve;
    private DERObjectIdentifier fieldIdentifier;
    private byte[] seed;

    /* JADX WARN: Code duplicated, block: B:15:0x00fa  */
    /* JADX WARN: Code duplicated, block: B:17:? A[RETURN, SYNTHETIC] */
    public X9Curve(X9FieldID x9FieldID, ASN1Sequence aSN1Sequence) {
        int iIntValue;
        int iIntValue2;
        int i;
        ECCurve f2m;
        this.fieldIdentifier = null;
        DERObjectIdentifier identifier = x9FieldID.getIdentifier();
        this.fieldIdentifier = identifier;
        if (!identifier.equals(prime_field)) {
            if (this.fieldIdentifier.equals(characteristic_two_field)) {
                DERSequence dERSequence = (DERSequence) x9FieldID.getParameters();
                int iIntValue3 = ((DERInteger) dERSequence.getObjectAt(0)).getValue().intValue();
                boolean zEquals = ((DERObjectIdentifier) dERSequence.getObjectAt(1)).equals(tpBasis);
                DEREncodable objectAt = dERSequence.getObjectAt(2);
                if (zEquals) {
                    iIntValue2 = ((DERInteger) objectAt).getValue().intValue();
                    i = 0;
                    iIntValue = 0;
                } else {
                    DERSequence dERSequence2 = (DERSequence) objectAt;
                    int iIntValue4 = ((DERInteger) dERSequence2.getObjectAt(0)).getValue().intValue();
                    int iIntValue5 = ((DERInteger) dERSequence2.getObjectAt(1)).getValue().intValue();
                    iIntValue = ((DERInteger) dERSequence2.getObjectAt(2)).getValue().intValue();
                    iIntValue2 = iIntValue4;
                    i = iIntValue5;
                }
                int i2 = iIntValue2;
                int i3 = i;
                int i4 = iIntValue;
                f2m = new ECCurve.F2m(iIntValue3, i2, i3, i4, new X9FieldElement(iIntValue3, i2, i3, i4, (ASN1OctetString) aSN1Sequence.getObjectAt(0)).getValue().toBigInteger(), new X9FieldElement(iIntValue3, i2, i3, i4, (ASN1OctetString) aSN1Sequence.getObjectAt(1)).getValue().toBigInteger());
            }
            if (aSN1Sequence.size() == 3) {
                this.seed = ((DERBitString) aSN1Sequence.getObjectAt(2)).getBytes();
            }
        }
        BigInteger value = ((DERInteger) x9FieldID.getParameters()).getValue();
        f2m = new ECCurve.Fp(value, new X9FieldElement(value, (ASN1OctetString) aSN1Sequence.getObjectAt(0)).getValue().toBigInteger(), new X9FieldElement(value, (ASN1OctetString) aSN1Sequence.getObjectAt(1)).getValue().toBigInteger());
        this.curve = f2m;
        if (aSN1Sequence.size() == 3) {
            this.seed = ((DERBitString) aSN1Sequence.getObjectAt(2)).getBytes();
        }
    }

    public X9Curve(ECCurve eCCurve) {
        this.fieldIdentifier = null;
        this.curve = eCCurve;
        this.seed = null;
        setFieldIdentifier();
    }

    public X9Curve(ECCurve eCCurve, byte[] bArr) {
        this.fieldIdentifier = null;
        this.curve = eCCurve;
        this.seed = bArr;
        setFieldIdentifier();
    }

    private void setFieldIdentifier() {
        DERObjectIdentifier dERObjectIdentifier;
        ECCurve eCCurve = this.curve;
        if (eCCurve instanceof ECCurve.Fp) {
            dERObjectIdentifier = prime_field;
        } else {
            if (!(eCCurve instanceof ECCurve.F2m)) {
                throw new IllegalArgumentException("This type of ECCurve is not implemented");
            }
            dERObjectIdentifier = characteristic_two_field;
        }
        this.fieldIdentifier = dERObjectIdentifier;
    }

    public ECCurve getCurve() {
        return this.curve;
    }

    public byte[] getSeed() {
        return this.seed;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.fieldIdentifier.equals(prime_field) || this.fieldIdentifier.equals(characteristic_two_field)) {
            aSN1EncodableVector.add(new X9FieldElement(this.curve.getA()).getDERObject());
            X9FieldElement x9FieldElement = new X9FieldElement(this.curve.getB());
            aSN1EncodableVector.add(x9FieldElement.getDERObject());
        }
        if (this.seed != null) {
            aSN1EncodableVector.add(new DERBitString(this.seed));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
