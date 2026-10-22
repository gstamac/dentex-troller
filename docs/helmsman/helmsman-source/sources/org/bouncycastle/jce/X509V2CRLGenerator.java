package org.bouncycastle.jce;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CRLException;
import java.security.cert.X509CRL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.SimpleTimeZone;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DEROutputStream;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERUTCTime;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.CertificateList;
import org.bouncycastle.asn1.x509.TBSCertList;
import org.bouncycastle.asn1.x509.V2TBSCertListGenerator;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.provider.X509CRLObject;
import org.bouncycastle.util.Strings;

/* JADX INFO: loaded from: classes2.dex */
public class X509V2CRLGenerator {
    private static Hashtable algorithms;
    private SimpleDateFormat dateF = new SimpleDateFormat("yyMMddHHmmss");
    private Vector extOrdering;
    private Hashtable extensions;
    private AlgorithmIdentifier sigAlgId;
    private DERObjectIdentifier sigOID;
    private String signatureAlgorithm;
    private V2TBSCertListGenerator tbsGen;
    private SimpleTimeZone tz;

    static {
        Hashtable hashtable = new Hashtable();
        algorithms = hashtable;
        hashtable.put("MD2WITHRSAENCRYPTION", new DERObjectIdentifier("1.2.840.113549.1.1.2"));
        algorithms.put("MD2WITHRSA", new DERObjectIdentifier("1.2.840.113549.1.1.2"));
        algorithms.put("MD5WITHRSAENCRYPTION", new DERObjectIdentifier("1.2.840.113549.1.1.4"));
        algorithms.put("MD5WITHRSA", new DERObjectIdentifier("1.2.840.113549.1.1.4"));
        algorithms.put("SHA1WITHRSAENCRYPTION", new DERObjectIdentifier("1.2.840.113549.1.1.5"));
        algorithms.put("SHA1WITHRSA", new DERObjectIdentifier("1.2.840.113549.1.1.5"));
        algorithms.put("RIPEMD160WITHRSAENCRYPTION", new DERObjectIdentifier("1.3.36.3.3.1.2"));
        algorithms.put("RIPEMD160WITHRSA", new DERObjectIdentifier("1.3.36.3.3.1.2"));
        algorithms.put("SHA1WITHDSA", new DERObjectIdentifier("1.2.840.10040.4.3"));
        algorithms.put("DSAWITHSHA1", new DERObjectIdentifier("1.2.840.10040.4.3"));
        algorithms.put("SHA1WITHECDSA", new DERObjectIdentifier("1.2.840.10045.4.1"));
        algorithms.put("ECDSAWITHSHA1", new DERObjectIdentifier("1.2.840.10045.4.1"));
    }

    public X509V2CRLGenerator() {
        SimpleTimeZone simpleTimeZone = new SimpleTimeZone(0, "Z");
        this.tz = simpleTimeZone;
        this.extensions = null;
        this.extOrdering = null;
        this.dateF.setTimeZone(simpleTimeZone);
        this.tbsGen = new V2TBSCertListGenerator();
    }

    public void addCRLEntry(BigInteger bigInteger, Date date, int i) {
        this.tbsGen.addCRLEntry(new DERInteger(bigInteger), new DERUTCTime(this.dateF.format(date) + "Z"), i);
    }

    public void addExtension(String str, boolean z, DEREncodable dEREncodable) {
        addExtension(new DERObjectIdentifier(str), z, dEREncodable);
    }

    public void addExtension(String str, boolean z, byte[] bArr) {
        addExtension(new DERObjectIdentifier(str), z, bArr);
    }

    public void addExtension(DERObjectIdentifier dERObjectIdentifier, boolean z, DEREncodable dEREncodable) {
        if (this.extensions == null) {
            this.extensions = new Hashtable();
            this.extOrdering = new Vector();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            new DEROutputStream(byteArrayOutputStream).writeObject(dEREncodable);
            addExtension(dERObjectIdentifier, z, byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            throw new IllegalArgumentException("error encoding value: " + e);
        }
    }

    public void addExtension(DERObjectIdentifier dERObjectIdentifier, boolean z, byte[] bArr) {
        if (this.extensions == null) {
            this.extensions = new Hashtable();
            this.extOrdering = new Vector();
        }
        this.extensions.put(dERObjectIdentifier, new X509Extension(z, new DEROctetString(bArr)));
        this.extOrdering.addElement(dERObjectIdentifier);
    }

    public X509CRL generateX509CRL(PrivateKey privateKey) throws SignatureException, SecurityException, InvalidKeyException {
        try {
            return generateX509CRL(privateKey, "BC", null);
        } catch (NoSuchProviderException unused) {
            throw new SecurityException("BC provider not installed!");
        }
    }

    public X509CRL generateX509CRL(PrivateKey privateKey, String str) throws SignatureException, SecurityException, InvalidKeyException, NoSuchProviderException {
        return generateX509CRL(privateKey, str, null);
    }

    public X509CRL generateX509CRL(PrivateKey privateKey, String str, SecureRandom secureRandom) throws SignatureException, InvalidKeyException, SecurityException, NoSuchProviderException {
        Signature signature;
        try {
            try {
                signature = Signature.getInstance(this.sigOID.getId(), str);
            } catch (NoSuchAlgorithmException unused) {
                signature = Signature.getInstance(this.signatureAlgorithm, str);
            }
            if (secureRandom != null) {
                signature.initSign(privateKey, secureRandom);
            } else {
                signature.initSign(privateKey);
            }
            if (this.extensions != null) {
                this.tbsGen.setExtensions(new X509Extensions(this.extOrdering, this.extensions));
            }
            TBSCertList tBSCertListGenerateTBSCertList = this.tbsGen.generateTBSCertList();
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                new DEROutputStream(byteArrayOutputStream).writeObject(tBSCertListGenerateTBSCertList);
                signature.update(byteArrayOutputStream.toByteArray());
                ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
                aSN1EncodableVector.add(tBSCertListGenerateTBSCertList);
                aSN1EncodableVector.add(this.sigAlgId);
                aSN1EncodableVector.add(new DERBitString(signature.sign()));
                try {
                    return new X509CRLObject(new CertificateList(new DERSequence(aSN1EncodableVector)));
                } catch (CRLException e) {
                    throw new IllegalStateException("attempt to create malformed CRL: " + e.getMessage());
                }
            } catch (Exception e2) {
                throw new SecurityException("exception encoding TBS cert - " + e2);
            }
        } catch (NoSuchAlgorithmException e3) {
            throw new SecurityException("exception creating signature: " + e3.toString());
        }
    }

    public X509CRL generateX509CRL(PrivateKey privateKey, SecureRandom secureRandom) throws SignatureException, SecurityException, InvalidKeyException {
        try {
            return generateX509CRL(privateKey, "BC", secureRandom);
        } catch (NoSuchProviderException unused) {
            throw new SecurityException("BC provider not installed!");
        }
    }

    public void reset() {
        this.tbsGen = new V2TBSCertListGenerator();
    }

    public void setIssuerDN(X509Name x509Name) {
        this.tbsGen.setIssuer(x509Name);
    }

    public void setNextUpdate(Date date) {
        this.tbsGen.setNextUpdate(new DERUTCTime(this.dateF.format(date) + "Z"));
    }

    public void setSignatureAlgorithm(String str) {
        this.signatureAlgorithm = str;
        DERObjectIdentifier dERObjectIdentifier = (DERObjectIdentifier) algorithms.get(Strings.toUpperCase(str));
        this.sigOID = dERObjectIdentifier;
        if (dERObjectIdentifier == null) {
            throw new IllegalArgumentException("Unknown signature type requested");
        }
        AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(this.sigOID, null);
        this.sigAlgId = algorithmIdentifier;
        this.tbsGen.setSignature(algorithmIdentifier);
    }

    public void setThisUpdate(Date date) {
        this.tbsGen.setThisUpdate(new DERUTCTime(this.dateF.format(date) + "Z"));
    }
}
