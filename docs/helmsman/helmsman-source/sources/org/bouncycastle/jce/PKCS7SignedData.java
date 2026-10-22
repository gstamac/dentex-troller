package org.bouncycastle.jce;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CRL;
import java.security.cert.CRLException;
import java.security.cert.Certificate;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DEROutputStream;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.pkcs.ContentInfo;
import org.bouncycastle.asn1.pkcs.IssuerAndSerialNumber;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.SignedData;
import org.bouncycastle.asn1.pkcs.SignerInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.CertificateList;
import org.bouncycastle.asn1.x509.X509CertificateStructure;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.provider.X509CRLObject;
import org.bouncycastle.jce.provider.X509CertificateObject;
import org.bouncycastle.ocsp.CertificateID;

/* JADX INFO: loaded from: classes2.dex */
public class PKCS7SignedData implements PKCSObjectIdentifiers {
    private final String ID_DSA;
    private final String ID_MD2;
    private final String ID_MD5;
    private final String ID_PKCS7_DATA;
    private final String ID_PKCS7_SIGNED_DATA;
    private final String ID_RSA;
    private final String ID_SHA1;
    private Collection certs;
    private Collection crls;
    private byte[] digest;
    private String digestAlgorithm;
    private String digestEncryptionAlgorithm;
    private Set digestalgos;
    private transient PrivateKey privKey;
    private Signature sig;
    private X509Certificate signCert;
    private int signerversion;
    private int version;

    public PKCS7SignedData(PrivateKey privateKey, Certificate[] certificateArr, String str) throws NoSuchAlgorithmException, SecurityException, InvalidKeyException, NoSuchProviderException {
        this(privateKey, certificateArr, str, "BC");
    }

    public PKCS7SignedData(PrivateKey privateKey, Certificate[] certificateArr, String str, String str2) throws NoSuchAlgorithmException, SecurityException, InvalidKeyException, NoSuchProviderException {
        this(privateKey, certificateArr, null, str, str2);
    }

    public PKCS7SignedData(PrivateKey privateKey, Certificate[] certificateArr, CRL[] crlArr, String str, String str2) throws NoSuchAlgorithmException, InvalidKeyException, SecurityException, NoSuchProviderException {
        this.ID_PKCS7_DATA = "1.2.840.113549.1.7.1";
        this.ID_PKCS7_SIGNED_DATA = "1.2.840.113549.1.7.2";
        this.ID_MD5 = "1.2.840.113549.2.5";
        this.ID_MD2 = "1.2.840.113549.2.2";
        this.ID_SHA1 = CertificateID.HASH_SHA1;
        this.ID_RSA = "1.2.840.113549.1.1.1";
        this.ID_DSA = "1.2.840.10040.4.1";
        this.privKey = privateKey;
        if (str.equals("MD5")) {
            this.digestAlgorithm = "1.2.840.113549.2.5";
        } else if (str.equals("MD2")) {
            this.digestAlgorithm = "1.2.840.113549.2.2";
        } else {
            if (!str.equals("SHA") && !str.equals("SHA1")) {
                throw new NoSuchAlgorithmException("Unknown Hash Algorithm " + str);
            }
            this.digestAlgorithm = CertificateID.HASH_SHA1;
        }
        this.signerversion = 1;
        this.version = 1;
        this.certs = new ArrayList();
        this.crls = new ArrayList();
        HashSet hashSet = new HashSet();
        this.digestalgos = hashSet;
        hashSet.add(this.digestAlgorithm);
        this.signCert = (X509Certificate) certificateArr[0];
        for (Certificate certificate : certificateArr) {
            this.certs.add(certificate);
        }
        if (crlArr != null) {
            for (CRL crl : crlArr) {
                this.crls.add(crl);
            }
        }
        String algorithm = privateKey.getAlgorithm();
        this.digestEncryptionAlgorithm = algorithm;
        if (algorithm.equals("RSA")) {
            this.digestEncryptionAlgorithm = "1.2.840.113549.1.1.1";
        } else {
            if (!this.digestEncryptionAlgorithm.equals("DSA")) {
                throw new NoSuchAlgorithmException("Unknown Key Algorithm " + this.digestEncryptionAlgorithm);
            }
            this.digestEncryptionAlgorithm = "1.2.840.10040.4.1";
        }
        Signature signature = Signature.getInstance(getDigestAlgorithm(), str2);
        this.sig = signature;
        signature.initSign(privateKey);
    }

    public PKCS7SignedData(byte[] bArr) throws NoSuchAlgorithmException, SecurityException, InvalidKeyException, CRLException, NoSuchProviderException {
        this(bArr, "BC");
    }

    public PKCS7SignedData(byte[] bArr, String str) throws NoSuchAlgorithmException, InvalidKeyException, SecurityException, CRLException, NoSuchProviderException {
        this.ID_PKCS7_DATA = "1.2.840.113549.1.7.1";
        this.ID_PKCS7_SIGNED_DATA = "1.2.840.113549.1.7.2";
        this.ID_MD5 = "1.2.840.113549.2.5";
        this.ID_MD2 = "1.2.840.113549.2.2";
        this.ID_SHA1 = CertificateID.HASH_SHA1;
        this.ID_RSA = "1.2.840.113549.1.1.1";
        this.ID_DSA = "1.2.840.10040.4.1";
        try {
            DERObject object = new ASN1InputStream(new ByteArrayInputStream(bArr)).readObject();
            if (!(object instanceof ASN1Sequence)) {
                throw new SecurityException("Not a valid PKCS#7 object - not a sequence");
            }
            ContentInfo contentInfo = ContentInfo.getInstance(object);
            if (!contentInfo.getContentType().equals(signedData)) {
                throw new SecurityException("Not a valid PKCS#7 signed-data object - wrong header " + contentInfo.getContentType().getId());
            }
            SignedData signedData = SignedData.getInstance(contentInfo.getContent());
            this.certs = new ArrayList();
            if (signedData.getCertificates() != null) {
                Enumeration objects = ASN1Set.getInstance(signedData.getCertificates()).getObjects();
                while (objects.hasMoreElements()) {
                    try {
                        this.certs.add(new X509CertificateObject(X509CertificateStructure.getInstance(objects.nextElement())));
                    } catch (CertificateParsingException e) {
                        throw new SecurityException(e.toString());
                    }
                }
            }
            this.crls = new ArrayList();
            if (signedData.getCRLs() != null) {
                Enumeration objects2 = ASN1Set.getInstance(signedData.getCRLs()).getObjects();
                while (objects2.hasMoreElements()) {
                    this.crls.add(new X509CRLObject(CertificateList.getInstance(objects2.nextElement())));
                }
            }
            this.version = signedData.getVersion().getValue().intValue();
            this.digestalgos = new HashSet();
            Enumeration objects3 = signedData.getDigestAlgorithms().getObjects();
            while (objects3.hasMoreElements()) {
                this.digestalgos.add(((DERObjectIdentifier) ((ASN1Sequence) objects3.nextElement()).getObjectAt(0)).getId());
            }
            ASN1Set signerInfos = signedData.getSignerInfos();
            if (signerInfos.size() != 1) {
                throw new SecurityException("This PKCS#7 object has multiple SignerInfos - only one is supported at this time");
            }
            SignerInfo signerInfo = SignerInfo.getInstance(signerInfos.getObjectAt(0));
            this.signerversion = signerInfo.getVersion().getValue().intValue();
            IssuerAndSerialNumber issuerAndSerialNumber = signerInfo.getIssuerAndSerialNumber();
            BigInteger value = issuerAndSerialNumber.getCertificateSerialNumber().getValue();
            X509Principal x509Principal = new X509Principal(issuerAndSerialNumber.getName());
            for (X509Certificate x509Certificate : this.certs) {
                if (value.equals(x509Certificate.getSerialNumber()) && x509Principal.equals(x509Certificate.getIssuerDN())) {
                    this.signCert = x509Certificate;
                    break;
                }
            }
            if (this.signCert == null) {
                throw new SecurityException("Can't find signing certificate with serial " + value.toString(16));
            }
            this.digestAlgorithm = signerInfo.getDigestAlgorithm().getObjectId().getId();
            this.digest = signerInfo.getEncryptedDigest().getOctets();
            this.digestEncryptionAlgorithm = signerInfo.getDigestEncryptionAlgorithm().getObjectId().getId();
            Signature signature = Signature.getInstance(getDigestAlgorithm(), str);
            this.sig = signature;
            signature.initVerify(this.signCert.getPublicKey());
        } catch (IOException unused) {
            throw new SecurityException("can't decode PKCS7SignedData object");
        }
    }

    private DERObject getIssuer(byte[] bArr) {
        try {
            ASN1Sequence aSN1Sequence = (ASN1Sequence) new ASN1InputStream(new ByteArrayInputStream(bArr)).readObject();
            return (DERObject) aSN1Sequence.getObjectAt(aSN1Sequence.getObjectAt(0) instanceof DERTaggedObject ? 3 : 2);
        } catch (IOException e) {
            throw new Error("IOException reading from ByteArray: " + e);
        }
    }

    public Collection getCRLs() {
        return this.crls;
    }

    public Certificate[] getCertificates() {
        Collection collection = this.certs;
        return (X509Certificate[]) collection.toArray(new X509Certificate[collection.size()]);
    }

    public String getDigestAlgorithm() {
        String str = this.digestAlgorithm;
        String str2 = this.digestEncryptionAlgorithm;
        if (str.equals("1.2.840.113549.2.5")) {
            str = "MD5";
        } else if (this.digestAlgorithm.equals("1.2.840.113549.2.2")) {
            str = "MD2";
        } else if (this.digestAlgorithm.equals(CertificateID.HASH_SHA1)) {
            str = "SHA1";
        }
        if (this.digestEncryptionAlgorithm.equals("1.2.840.113549.1.1.1")) {
            str2 = "RSA";
        } else if (this.digestEncryptionAlgorithm.equals("1.2.840.10040.4.1")) {
            str2 = "DSA";
        }
        return str + "with" + str2;
    }

    public byte[] getEncoded() {
        try {
            this.digest = this.sig.sign();
            ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
            Iterator it = this.digestalgos.iterator();
            while (it.hasNext()) {
                aSN1EncodableVector.add(new AlgorithmIdentifier(new DERObjectIdentifier((String) it.next()), null));
            }
            DERSet dERSet = new DERSet(aSN1EncodableVector);
            DERSequence dERSequence = new DERSequence(new DERObjectIdentifier("1.2.840.113549.1.7.1"));
            ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
            Iterator it2 = this.certs.iterator();
            while (it2.hasNext()) {
                aSN1EncodableVector2.add(new ASN1InputStream(new ByteArrayInputStream(((X509Certificate) it2.next()).getEncoded())).readObject());
            }
            DERSet dERSet2 = new DERSet(aSN1EncodableVector2);
            ASN1EncodableVector aSN1EncodableVector3 = new ASN1EncodableVector();
            aSN1EncodableVector3.add(new DERInteger(this.signerversion));
            aSN1EncodableVector3.add(new IssuerAndSerialNumber(new X509Name((ASN1Sequence) getIssuer(this.signCert.getTBSCertificate())), new DERInteger(this.signCert.getSerialNumber())));
            aSN1EncodableVector3.add(new AlgorithmIdentifier(new DERObjectIdentifier(this.digestAlgorithm), new DERNull()));
            aSN1EncodableVector3.add(new AlgorithmIdentifier(new DERObjectIdentifier(this.digestEncryptionAlgorithm), new DERNull()));
            aSN1EncodableVector3.add(new DEROctetString(this.digest));
            ASN1EncodableVector aSN1EncodableVector4 = new ASN1EncodableVector();
            aSN1EncodableVector4.add(new DERInteger(this.version));
            aSN1EncodableVector4.add(dERSet);
            aSN1EncodableVector4.add(dERSequence);
            aSN1EncodableVector4.add(new DERTaggedObject(false, 0, dERSet2));
            if (this.crls.size() > 0) {
                ASN1EncodableVector aSN1EncodableVector5 = new ASN1EncodableVector();
                Iterator it3 = this.crls.iterator();
                while (it3.hasNext()) {
                    aSN1EncodableVector5.add(new ASN1InputStream(new ByteArrayInputStream(((X509CRL) it3.next()).getEncoded())).readObject());
                }
                aSN1EncodableVector4.add(new DERTaggedObject(false, 1, new DERSet(aSN1EncodableVector5)));
            }
            aSN1EncodableVector4.add(new DERSet(new DERSequence(aSN1EncodableVector3)));
            ASN1EncodableVector aSN1EncodableVector6 = new ASN1EncodableVector();
            aSN1EncodableVector6.add(new DERObjectIdentifier("1.2.840.113549.1.7.2"));
            aSN1EncodableVector6.add(new DERTaggedObject(0, new DERSequence(aSN1EncodableVector4)));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DEROutputStream dEROutputStream = new DEROutputStream(byteArrayOutputStream);
            dEROutputStream.writeObject(new DERSequence(aSN1EncodableVector6));
            dEROutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    public X509Certificate getSigningCertificate() {
        return this.signCert;
    }

    public int getSigningInfoVersion() {
        return this.signerversion;
    }

    public int getVersion() {
        return this.version;
    }

    public void reset() {
        try {
            PrivateKey privateKey = this.privKey;
            if (privateKey == null) {
                this.sig.initVerify(this.signCert.getPublicKey());
            } else {
                this.sig.initSign(privateKey);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    public void update(byte b) throws SignatureException {
        this.sig.update(b);
    }

    public void update(byte[] bArr, int i, int i2) throws SignatureException {
        this.sig.update(bArr, i, i2);
    }

    public boolean verify() throws SignatureException {
        return this.sig.verify(this.digest);
    }
}
