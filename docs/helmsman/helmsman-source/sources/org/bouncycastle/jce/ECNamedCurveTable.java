package org.bouncycastle.jce;

import java.util.Enumeration;
import java.util.Vector;
import org.bouncycastle.asn1.nist.NISTNamedCurves;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.teletrust.TeleTrusTNamedCurves;
import org.bouncycastle.asn1.x9.X962NamedCurves;

/* JADX INFO: loaded from: classes2.dex */
public class ECNamedCurveTable {
    private static void addEnumeration(Vector vector, Enumeration enumeration) {
        while (enumeration.hasMoreElements()) {
            vector.addElement(enumeration.nextElement());
        }
    }

    public static Enumeration getNames() {
        Vector vector = new Vector();
        addEnumeration(vector, X962NamedCurves.getNames());
        addEnumeration(vector, SECNamedCurves.getNames());
        addEnumeration(vector, NISTNamedCurves.getNames());
        addEnumeration(vector, TeleTrusTNamedCurves.getNames());
        return vector.elements();
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0022, code lost:
    
        r0 = org.bouncycastle.asn1.teletrust.TeleTrusTNamedCurves.getByName(r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0011, code lost:
    
        r0 = org.bouncycastle.asn1.sec.SECNamedCurves.getByName(r8);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static org.bouncycastle.jce.spec.ECNamedCurveParameterSpec getParameterSpec(java.lang.String r8) {
        /*
            org.bouncycastle.asn1.x9.X9ECParameters r0 = org.bouncycastle.asn1.x9.X962NamedCurves.getByName(r8)
            if (r0 != 0) goto Lf
            org.bouncycastle.asn1.DERObjectIdentifier r1 = new org.bouncycastle.asn1.DERObjectIdentifier     // Catch: java.lang.IllegalArgumentException -> Lf
            r1.<init>(r8)     // Catch: java.lang.IllegalArgumentException -> Lf
            org.bouncycastle.asn1.x9.X9ECParameters r0 = org.bouncycastle.asn1.x9.X962NamedCurves.getByOID(r1)     // Catch: java.lang.IllegalArgumentException -> Lf
        Lf:
            if (r0 != 0) goto L20
            org.bouncycastle.asn1.x9.X9ECParameters r0 = org.bouncycastle.asn1.sec.SECNamedCurves.getByName(r8)
            if (r0 != 0) goto L20
            org.bouncycastle.asn1.DERObjectIdentifier r1 = new org.bouncycastle.asn1.DERObjectIdentifier     // Catch: java.lang.IllegalArgumentException -> L20
            r1.<init>(r8)     // Catch: java.lang.IllegalArgumentException -> L20
            org.bouncycastle.asn1.x9.X9ECParameters r0 = org.bouncycastle.asn1.sec.SECNamedCurves.getByOID(r1)     // Catch: java.lang.IllegalArgumentException -> L20
        L20:
            if (r0 != 0) goto L31
            org.bouncycastle.asn1.x9.X9ECParameters r0 = org.bouncycastle.asn1.teletrust.TeleTrusTNamedCurves.getByName(r8)
            if (r0 != 0) goto L31
            org.bouncycastle.asn1.DERObjectIdentifier r1 = new org.bouncycastle.asn1.DERObjectIdentifier     // Catch: java.lang.IllegalArgumentException -> L31
            r1.<init>(r8)     // Catch: java.lang.IllegalArgumentException -> L31
            org.bouncycastle.asn1.x9.X9ECParameters r0 = org.bouncycastle.asn1.teletrust.TeleTrusTNamedCurves.getByOID(r1)     // Catch: java.lang.IllegalArgumentException -> L31
        L31:
            if (r0 != 0) goto L37
            org.bouncycastle.asn1.x9.X9ECParameters r0 = org.bouncycastle.asn1.nist.NISTNamedCurves.getByName(r8)
        L37:
            if (r0 != 0) goto L3b
            r8 = 0
            return r8
        L3b:
            org.bouncycastle.jce.spec.ECNamedCurveParameterSpec r7 = new org.bouncycastle.jce.spec.ECNamedCurveParameterSpec
            org.bouncycastle.math.ec.ECCurve r2 = r0.getCurve()
            org.bouncycastle.math.ec.ECPoint r3 = r0.getG()
            java.math.BigInteger r4 = r0.getN()
            java.math.BigInteger r5 = r0.getH()
            byte[] r6 = r0.getSeed()
            r0 = r7
            r1 = r8
            r0.<init>(r1, r2, r3, r4, r5, r6)
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.jce.ECNamedCurveTable.getParameterSpec(java.lang.String):org.bouncycastle.jce.spec.ECNamedCurveParameterSpec");
    }
}
