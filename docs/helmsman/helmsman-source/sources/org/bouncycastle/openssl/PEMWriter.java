package org.bouncycastle.openssl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.CRLException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure;
import org.bouncycastle.asn1.x509.DSAParameter;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.util.Strings;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.x509.X509AttributeCertificate;
import org.bouncycastle.x509.X509V2AttributeCertificate;

/* JADX INFO: loaded from: classes2.dex */
public class PEMWriter extends BufferedWriter {
    private String provider;

    public PEMWriter(Writer writer) {
        this(writer, "BC");
    }

    public PEMWriter(Writer writer, String str) {
        super(writer);
        this.provider = str;
    }

    private void writeEncoded(byte[] bArr) throws IOException {
        char[] cArr = new char[64];
        byte[] bArrEncode = Base64.encode(bArr);
        for (int i = 0; i < bArrEncode.length; i += 64) {
            int i2 = 0;
            while (i2 != 64) {
                int i3 = i + i2;
                if (i3 >= bArrEncode.length) {
                    break;
                }
                cArr[i2] = (char) bArrEncode[i3];
                i2++;
            }
            write(cArr, 0, i2);
            newLine();
        }
    }

    private void writeFooter(String str) throws IOException {
        write("-----END " + str + "-----");
        newLine();
    }

    private void writeHeader(String str) throws IOException {
        write("-----BEGIN " + str + "-----");
        newLine();
    }

    private void writeHexEncoded(byte[] bArr) throws IOException {
        byte[] bArrEncode = Hex.encode(bArr);
        for (int i = 0; i != bArrEncode.length; i++) {
            write((char) bArrEncode[i]);
        }
    }

    public void writeObject(Object obj) throws IOException {
        byte[] encoded;
        String str;
        if (obj instanceof X509Certificate) {
            try {
                encoded = ((X509Certificate) obj).getEncoded();
                str = "CERTIFICATE";
            } catch (CertificateEncodingException e) {
                throw new IOException("Cannot encode object: " + e.toString());
            }
        } else if (obj instanceof X509CRL) {
            try {
                encoded = ((X509CRL) obj).getEncoded();
                str = "X509 CRL";
            } catch (CRLException e2) {
                throw new IOException("Cannot encode object: " + e2.toString());
            }
        } else {
            if (obj instanceof KeyPair) {
                writeObject(((KeyPair) obj).getPrivate());
                return;
            }
            if (obj instanceof PrivateKey) {
                PrivateKeyInfo privateKeyInfo = new PrivateKeyInfo((ASN1Sequence) ASN1Object.fromByteArray(((Key) obj).getEncoded()));
                if (obj instanceof RSAPrivateKey) {
                    encoded = privateKeyInfo.getPrivateKey().getEncoded();
                    str = "RSA PRIVATE KEY";
                } else if (obj instanceof DSAPrivateKey) {
                    DSAParameter dSAParameter = DSAParameter.getInstance(privateKeyInfo.getAlgorithmId().getParameters());
                    ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
                    aSN1EncodableVector.add(new DERInteger(0));
                    aSN1EncodableVector.add(new DERInteger(dSAParameter.getP()));
                    aSN1EncodableVector.add(new DERInteger(dSAParameter.getQ()));
                    aSN1EncodableVector.add(new DERInteger(dSAParameter.getG()));
                    BigInteger x = ((DSAPrivateKey) obj).getX();
                    aSN1EncodableVector.add(new DERInteger(dSAParameter.getG().modPow(x, dSAParameter.getP())));
                    aSN1EncodableVector.add(new DERInteger(x));
                    encoded = new DERSequence(aSN1EncodableVector).getEncoded();
                    str = "DSA PRIVATE KEY";
                } else {
                    if (!((PrivateKey) obj).getAlgorithm().equals("ECDSA")) {
                        throw new IOException("Cannot identify private key");
                    }
                    encoded = privateKeyInfo.getPrivateKey().getEncoded();
                    str = "EC PRIVATE KEY";
                }
            } else if (obj instanceof PublicKey) {
                encoded = ((PublicKey) obj).getEncoded();
                str = "PUBLIC KEY";
            } else if (obj instanceof X509AttributeCertificate) {
                encoded = ((X509V2AttributeCertificate) obj).getEncoded();
                str = "ATTRIBUTE CERTIFICATE";
            } else if (obj instanceof PKCS10CertificationRequest) {
                encoded = ((PKCS10CertificationRequest) obj).getEncoded();
                str = "CERTIFICATE REQUEST";
            } else {
                if (!(obj instanceof ContentInfo)) {
                    throw new IOException("unknown object passed - can't encode.");
                }
                encoded = ((ContentInfo) obj).getEncoded();
                str = "PKCS7";
            }
        }
        writeHeader(str);
        writeEncoded(encoded);
        writeFooter(str);
    }

    /* JADX WARN: Code duplicated, block: B:20:0x00dd A[ADDED_TO_REGION] */
    public void writeObject(Object obj, String str, char[] cArr, SecureRandom secureRandom) throws IOException {
        String str2;
        byte[] bArr;
        byte[] encoded;
        String str3;
        if (obj instanceof KeyPair) {
            writeObject(((KeyPair) obj).getPrivate());
            return;
        }
        if (obj instanceof RSAPrivateCrtKey) {
            RSAPrivateCrtKey rSAPrivateCrtKey = (RSAPrivateCrtKey) obj;
            encoded = new RSAPrivateKeyStructure(rSAPrivateCrtKey.getModulus(), rSAPrivateCrtKey.getPublicExponent(), rSAPrivateCrtKey.getPrivateExponent(), rSAPrivateCrtKey.getPrimeP(), rSAPrivateCrtKey.getPrimeQ(), rSAPrivateCrtKey.getPrimeExponentP(), rSAPrivateCrtKey.getPrimeExponentQ(), rSAPrivateCrtKey.getCrtCoefficient()).getEncoded();
            str3 = "RSA PRIVATE KEY";
        } else {
            if (!(obj instanceof DSAPrivateKey)) {
                if (obj instanceof PrivateKey) {
                    PrivateKey privateKey = (PrivateKey) obj;
                    if ("ECDSA".equals(privateKey.getAlgorithm())) {
                        encoded = PrivateKeyInfo.getInstance(ASN1Object.fromByteArray(privateKey.getEncoded())).getPrivateKey().getEncoded();
                        str3 = "EC PRIVATE KEY";
                    }
                    if (str2 != null || bArr == null) {
                        throw new IllegalArgumentException("Object type not supported: " + obj.getClass().getName());
                    }
                    String upperCase = Strings.toUpperCase(str);
                    if (upperCase.equals("DESEDE")) {
                        upperCase = "DES-EDE3-CBC";
                    }
                    byte[] bArr2 = new byte[upperCase.startsWith("AES-") ? 16 : 8];
                    secureRandom.nextBytes(bArr2);
                    byte[] bArrCrypt = PEMUtilities.crypt(true, this.provider, bArr, cArr, upperCase, bArr2);
                    writeHeader(str2);
                    write("Proc-Type: 4,ENCRYPTED");
                    newLine();
                    write("DEK-Info: " + upperCase + ",");
                    writeHexEncoded(bArr2);
                    newLine();
                    newLine();
                    writeEncoded(bArrCrypt);
                    writeFooter(str2);
                    return;
                }
                str2 = null;
                bArr = null;
                if (str2 != null) {
                }
                throw new IllegalArgumentException("Object type not supported: " + obj.getClass().getName());
            }
            DSAPrivateKey dSAPrivateKey = (DSAPrivateKey) obj;
            DSAParams params = dSAPrivateKey.getParams();
            ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
            aSN1EncodableVector.add(new DERInteger(0));
            aSN1EncodableVector.add(new DERInteger(params.getP()));
            aSN1EncodableVector.add(new DERInteger(params.getQ()));
            aSN1EncodableVector.add(new DERInteger(params.getG()));
            BigInteger x = dSAPrivateKey.getX();
            aSN1EncodableVector.add(new DERInteger(params.getG().modPow(x, params.getP())));
            aSN1EncodableVector.add(new DERInteger(x));
            encoded = new DERSequence(aSN1EncodableVector).getEncoded();
            str3 = "DSA PRIVATE KEY";
        }
        bArr = encoded;
        str2 = str3;
        if (str2 != null) {
        }
        throw new IllegalArgumentException("Object type not supported: " + obj.getClass().getName());
    }
}
