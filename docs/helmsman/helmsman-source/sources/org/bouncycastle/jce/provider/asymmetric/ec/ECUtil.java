package org.bouncycastle.jce.provider.asymmetric.ec;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.cryptopro.ECGOST3410NamedCurves;
import org.bouncycastle.asn1.nist.NISTNamedCurves;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.teletrust.TeleTrusTNamedCurves;
import org.bouncycastle.asn1.x9.X962NamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.JCEECPublicKey;
import org.bouncycastle.jce.provider.ProviderUtil;
import org.bouncycastle.jce.spec.ECParameterSpec;

/* JADX INFO: loaded from: classes2.dex */
public class ECUtil {
    static int[] convertMidTerms(int[] iArr) {
        int i;
        int[] iArr2 = new int[3];
        if (iArr.length == 1) {
            iArr2[0] = iArr[0];
        } else {
            if (iArr.length != 3) {
                throw new IllegalArgumentException("Only Trinomials and pentanomials supported");
            }
            int i2 = iArr[0];
            int i3 = iArr[1];
            if (i2 >= i3 || i2 >= (i = iArr[2])) {
                int i4 = iArr[2];
                if (i3 < i4) {
                    iArr2[0] = i3;
                    int i5 = iArr[0];
                    if (i5 < i4) {
                        iArr2[1] = i5;
                        iArr2[2] = i4;
                    } else {
                        iArr2[1] = i4;
                        iArr2[2] = i5;
                    }
                } else {
                    iArr2[0] = i4;
                    int i6 = iArr[0];
                    if (i6 < i3) {
                        iArr2[1] = i6;
                        iArr2[2] = iArr[1];
                    } else {
                        iArr2[1] = i3;
                        iArr2[2] = i6;
                    }
                }
            } else {
                iArr2[0] = i2;
                if (i3 < i) {
                    iArr2[1] = i3;
                    iArr2[2] = i;
                } else {
                    iArr2[1] = i;
                    iArr2[2] = iArr[1];
                }
            }
        }
        return iArr2;
    }

    public static AsymmetricKeyParameter generatePrivateKeyParameter(PrivateKey privateKey) throws InvalidKeyException {
        if (!(privateKey instanceof ECPrivateKey)) {
            throw new InvalidKeyException("can't identify EC private key.");
        }
        ECPrivateKey eCPrivateKey = (ECPrivateKey) privateKey;
        ECParameterSpec parameters = eCPrivateKey.getParameters();
        if (parameters == null) {
            parameters = ProviderUtil.getEcImplicitlyCa();
        }
        return new ECPrivateKeyParameters(eCPrivateKey.getD(), new ECDomainParameters(parameters.getCurve(), parameters.getG(), parameters.getN(), parameters.getH(), parameters.getSeed()));
    }

    public static AsymmetricKeyParameter generatePublicKeyParameter(PublicKey publicKey) throws InvalidKeyException {
        if (!(publicKey instanceof ECPublicKey)) {
            if (!(publicKey instanceof java.security.interfaces.ECPublicKey)) {
                throw new InvalidKeyException("cannot identify EC public key.");
            }
            java.security.interfaces.ECPublicKey eCPublicKey = (java.security.interfaces.ECPublicKey) publicKey;
            ECParameterSpec eCParameterSpecConvertSpec = EC5Util.convertSpec(eCPublicKey.getParams(), false);
            return new ECPublicKeyParameters(EC5Util.convertPoint(eCPublicKey.getParams(), eCPublicKey.getW(), false), new ECDomainParameters(eCParameterSpecConvertSpec.getCurve(), eCParameterSpecConvertSpec.getG(), eCParameterSpecConvertSpec.getN(), eCParameterSpecConvertSpec.getH(), eCParameterSpecConvertSpec.getSeed()));
        }
        ECPublicKey eCPublicKey2 = (ECPublicKey) publicKey;
        ECParameterSpec parameters = eCPublicKey2.getParameters();
        if (parameters != null) {
            return new ECPublicKeyParameters(eCPublicKey2.getQ(), new ECDomainParameters(parameters.getCurve(), parameters.getG(), parameters.getN(), parameters.getH(), parameters.getSeed()));
        }
        ECParameterSpec ecImplicitlyCa = ProviderUtil.getEcImplicitlyCa();
        return new ECPublicKeyParameters(((JCEECPublicKey) eCPublicKey2).engineGetQ(), new ECDomainParameters(ecImplicitlyCa.getCurve(), ecImplicitlyCa.getG(), ecImplicitlyCa.getN(), ecImplicitlyCa.getH(), ecImplicitlyCa.getSeed()));
    }

    public static String getCurveName(DERObjectIdentifier dERObjectIdentifier) {
        String name = X962NamedCurves.getName(dERObjectIdentifier);
        if (name != null) {
            return name;
        }
        String name2 = SECNamedCurves.getName(dERObjectIdentifier);
        if (name2 == null) {
            name2 = NISTNamedCurves.getName(dERObjectIdentifier);
        }
        if (name2 == null) {
            name2 = TeleTrusTNamedCurves.getName(dERObjectIdentifier);
        }
        return name2 == null ? ECGOST3410NamedCurves.getName(dERObjectIdentifier) : name2;
    }

    public static X9ECParameters getNamedCurveByOid(DERObjectIdentifier dERObjectIdentifier) {
        X9ECParameters byOID = X962NamedCurves.getByOID(dERObjectIdentifier);
        if (byOID != null) {
            return byOID;
        }
        X9ECParameters byOID2 = SECNamedCurves.getByOID(dERObjectIdentifier);
        if (byOID2 == null) {
            byOID2 = NISTNamedCurves.getByOID(dERObjectIdentifier);
        }
        return byOID2 == null ? TeleTrusTNamedCurves.getByOID(dERObjectIdentifier) : byOID2;
    }

    public static DERObjectIdentifier getNamedCurveOid(String str) {
        DERObjectIdentifier oid = X962NamedCurves.getOID(str);
        if (oid != null) {
            return oid;
        }
        DERObjectIdentifier oid2 = SECNamedCurves.getOID(str);
        if (oid2 == null) {
            oid2 = NISTNamedCurves.getOID(str);
        }
        if (oid2 == null) {
            oid2 = TeleTrusTNamedCurves.getOID(str);
        }
        return oid2 == null ? ECGOST3410NamedCurves.getOID(str) : oid2;
    }
}
