package org.bouncycastle.openssl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.StringTokenizer;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.sec.ECPrivateKeyStructure;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.RSAPublicKeyStructure;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.x509.X509AttributeCertificate;
import org.bouncycastle.x509.X509V2AttributeCertificate;

/* JADX INFO: loaded from: classes2.dex */
public class PEMReader extends BufferedReader {
    private final PasswordFinder pFinder;
    private final String provider;

    public PEMReader(Reader reader) {
        this(reader, null, "BC");
    }

    public PEMReader(Reader reader, PasswordFinder passwordFinder) {
        this(reader, passwordFinder, "BC");
    }

    public PEMReader(Reader reader, PasswordFinder passwordFinder, String str) {
        super(reader);
        this.pFinder = passwordFinder;
        this.provider = str;
    }

    private X509AttributeCertificate readAttributeCertificate(String str) throws IOException {
        return new X509V2AttributeCertificate(readBytes(str));
    }

    private byte[] readBytes(String str) throws IOException {
        String line;
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            line = readLine();
            if (line == null || line.indexOf(str) != -1) {
                break;
            }
            stringBuffer.append(line.trim());
        }
        if (line != null) {
            return Base64.decode(stringBuffer.toString());
        }
        throw new IOException(str + " not found");
    }

    private X509CRL readCRL(String str) throws IOException {
        try {
            return (X509CRL) CertificateFactory.getInstance("X.509", this.provider).generateCRL(new ByteArrayInputStream(readBytes(str)));
        } catch (Exception e) {
            throw new PEMException("problem parsing cert: " + e.toString(), e);
        }
    }

    private X509Certificate readCertificate(String str) throws IOException {
        try {
            return (X509Certificate) CertificateFactory.getInstance("X.509", this.provider).generateCertificate(new ByteArrayInputStream(readBytes(str)));
        } catch (Exception e) {
            throw new PEMException("problem parsing cert: " + e.toString(), e);
        }
    }

    private PKCS10CertificationRequest readCertificateRequest(String str) throws IOException {
        try {
            return new PKCS10CertificationRequest(readBytes(str));
        } catch (Exception e) {
            throw new PEMException("problem parsing certrequest: " + e.toString(), e);
        }
    }

    private ECNamedCurveParameterSpec readECParameters(String str) throws IOException {
        return ECNamedCurveTable.getParameterSpec(((DERObjectIdentifier) ASN1Object.fromByteArray(readBytes(str))).getId());
    }

    private KeyPair readKeyPair(String str, String str2) throws Exception {
        KeySpec dSAPrivateKeySpec;
        KeySpec dSAPublicKeySpec;
        StringBuffer stringBuffer = new StringBuffer();
        boolean z = false;
        String strSubstring = null;
        while (true) {
            String line = readLine();
            if (line == null) {
                break;
            }
            if (line.startsWith("Proc-Type: 4,ENCRYPTED")) {
                z = true;
            } else if (line.startsWith("DEK-Info:")) {
                strSubstring = line.substring(10);
            } else {
                if (line.indexOf(str2) != -1) {
                    break;
                }
                stringBuffer.append(line.trim());
            }
        }
        byte[] bArrDecode = Base64.decode(stringBuffer.toString());
        if (z) {
            PasswordFinder passwordFinder = this.pFinder;
            if (passwordFinder == null) {
                throw new PasswordException("No password finder specified, but a password is required");
            }
            char[] password = passwordFinder.getPassword();
            if (password == null) {
                throw new PasswordException("Password is null, but a password is required");
            }
            StringTokenizer stringTokenizer = new StringTokenizer(strSubstring, ",");
            bArrDecode = PEMUtilities.crypt(false, this.provider, bArrDecode, password, stringTokenizer.nextToken(), Hex.decode(stringTokenizer.nextToken()));
        }
        ASN1Sequence aSN1Sequence = (ASN1Sequence) ASN1Object.fromByteArray(bArrDecode);
        if (str.equals("RSA")) {
            DERInteger dERInteger = (DERInteger) aSN1Sequence.getObjectAt(1);
            DERInteger dERInteger2 = (DERInteger) aSN1Sequence.getObjectAt(2);
            DERInteger dERInteger3 = (DERInteger) aSN1Sequence.getObjectAt(3);
            DERInteger dERInteger4 = (DERInteger) aSN1Sequence.getObjectAt(4);
            DERInteger dERInteger5 = (DERInteger) aSN1Sequence.getObjectAt(5);
            DERInteger dERInteger6 = (DERInteger) aSN1Sequence.getObjectAt(6);
            DERInteger dERInteger7 = (DERInteger) aSN1Sequence.getObjectAt(7);
            DERInteger dERInteger8 = (DERInteger) aSN1Sequence.getObjectAt(8);
            dSAPublicKeySpec = new RSAPublicKeySpec(dERInteger.getValue(), dERInteger2.getValue());
            dSAPrivateKeySpec = new RSAPrivateCrtKeySpec(dERInteger.getValue(), dERInteger2.getValue(), dERInteger3.getValue(), dERInteger4.getValue(), dERInteger5.getValue(), dERInteger6.getValue(), dERInteger7.getValue(), dERInteger8.getValue());
        } else if (str.equals("ECDSA")) {
            ECPrivateKeyStructure eCPrivateKeyStructure = new ECPrivateKeyStructure(aSN1Sequence);
            AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, eCPrivateKeyStructure.getParameters());
            PrivateKeyInfo privateKeyInfo = new PrivateKeyInfo(algorithmIdentifier, eCPrivateKeyStructure.getDERObject());
            SubjectPublicKeyInfo subjectPublicKeyInfo = new SubjectPublicKeyInfo(algorithmIdentifier, eCPrivateKeyStructure.getPublicKey().getBytes());
            dSAPrivateKeySpec = new PKCS8EncodedKeySpec(privateKeyInfo.getEncoded());
            dSAPublicKeySpec = new X509EncodedKeySpec(subjectPublicKeyInfo.getEncoded());
        } else {
            DERInteger dERInteger9 = (DERInteger) aSN1Sequence.getObjectAt(1);
            DERInteger dERInteger10 = (DERInteger) aSN1Sequence.getObjectAt(2);
            DERInteger dERInteger11 = (DERInteger) aSN1Sequence.getObjectAt(3);
            DERInteger dERInteger12 = (DERInteger) aSN1Sequence.getObjectAt(4);
            dSAPrivateKeySpec = new DSAPrivateKeySpec(((DERInteger) aSN1Sequence.getObjectAt(5)).getValue(), dERInteger9.getValue(), dERInteger10.getValue(), dERInteger11.getValue());
            dSAPublicKeySpec = new DSAPublicKeySpec(dERInteger12.getValue(), dERInteger9.getValue(), dERInteger10.getValue(), dERInteger11.getValue());
        }
        KeyFactory keyFactory = KeyFactory.getInstance(str, this.provider);
        return new KeyPair(keyFactory.generatePublic(dSAPublicKeySpec), keyFactory.generatePrivate(dSAPrivateKeySpec));
    }

    private ContentInfo readPKCS7(String str) throws IOException {
        String line;
        StringBuffer stringBuffer = new StringBuffer();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (true) {
            line = readLine();
            if (line == null || line.indexOf(str) != -1) {
                break;
            }
            stringBuffer.append(line.trim().trim());
            Base64.decode(stringBuffer.substring(0, (stringBuffer.length() / 4) * 4), byteArrayOutputStream);
            stringBuffer.delete(0, (stringBuffer.length() / 4) * 4);
        }
        if (stringBuffer.length() != 0) {
            throw new IOException("base64 data appears to be truncated");
        }
        if (line == null) {
            throw new IOException(str + " not found");
        }
        try {
            return ContentInfo.getInstance(new ASN1InputStream(byteArrayOutputStream.toByteArray()).readObject());
        } catch (Exception e) {
            throw new PEMException("problem parsing PKCS7 object: " + e.toString(), e);
        }
    }

    private PublicKey readPublicKey(String str) throws IOException {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(readBytes(str));
        String[] strArr = {"DSA", "RSA"};
        for (int i = 0; i < 2; i++) {
            try {
                return KeyFactory.getInstance(strArr[i], this.provider).generatePublic(x509EncodedKeySpec);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException unused) {
            } catch (NoSuchProviderException unused2) {
                throw new RuntimeException("can't find provider " + this.provider);
            }
        }
        return null;
    }

    private PublicKey readRSAPublicKey(String str) throws IOException {
        RSAPublicKeyStructure rSAPublicKeyStructure = new RSAPublicKeyStructure((ASN1Sequence) new ASN1InputStream(readBytes(str)).readObject());
        try {
            return KeyFactory.getInstance("RSA", this.provider).generatePublic(new RSAPublicKeySpec(rSAPublicKeyStructure.getModulus(), rSAPublicKeyStructure.getPublicExponent()));
        } catch (NoSuchProviderException unused) {
            throw new IOException("can't find provider " + this.provider);
        } catch (Exception e) {
            throw new PEMException("problem extracting key: " + e.toString(), e);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:66:0x0108, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0121, code lost:
    
        throw new org.bouncycastle.openssl.PEMException("problem creating ECDSA private key: " + r0.toString(), r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x0122, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0123, code lost:
    
        throw r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.Object readObject() throws java.io.IOException {
        /*
            Method dump skipped, instruction units count: 294
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.openssl.PEMReader.readObject():java.lang.Object");
    }
}
