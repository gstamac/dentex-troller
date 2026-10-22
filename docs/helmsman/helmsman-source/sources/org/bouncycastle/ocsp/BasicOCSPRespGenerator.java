package org.bouncycastle.ocsp;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.ocsp.BasicOCSPResponse;
import org.bouncycastle.asn1.ocsp.CertStatus;
import org.bouncycastle.asn1.ocsp.ResponseData;
import org.bouncycastle.asn1.ocsp.RevokedInfo;
import org.bouncycastle.asn1.ocsp.SingleResponse;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.x509.X509CertificateStructure;
import org.bouncycastle.asn1.x509.X509Extensions;

/* JADX INFO: loaded from: classes2.dex */
public class BasicOCSPRespGenerator {
    private RespID responderID;
    private List list = new ArrayList();
    private X509Extensions responseExtensions = null;

    private class ResponseObject {
        CertificateID certId;
        CertStatus certStatus;
        X509Extensions extensions;
        DERGeneralizedTime nextUpdate;
        DERGeneralizedTime thisUpdate;

        public ResponseObject(CertificateID certificateID, CertificateStatus certificateStatus, Date date, Date date2, X509Extensions x509Extensions) {
            CertStatus certStatus;
            this.certId = certificateID;
            if (certificateStatus == null) {
                certStatus = new CertStatus();
            } else if (certificateStatus instanceof UnknownStatus) {
                certStatus = new CertStatus(2, new DERNull());
            } else {
                RevokedStatus revokedStatus = (RevokedStatus) certificateStatus;
                certStatus = revokedStatus.hasRevocationReason() ? new CertStatus(new RevokedInfo(new DERGeneralizedTime(revokedStatus.getRevocationTime()), new CRLReason(revokedStatus.getRevocationReason()))) : new CertStatus(new RevokedInfo(new DERGeneralizedTime(revokedStatus.getRevocationTime()), null));
            }
            this.certStatus = certStatus;
            this.thisUpdate = new DERGeneralizedTime(date);
            this.nextUpdate = date2 != null ? new DERGeneralizedTime(date2) : null;
            this.extensions = x509Extensions;
        }

        public SingleResponse toResponse() throws Exception {
            return new SingleResponse(this.certId.toASN1Object(), this.certStatus, this.thisUpdate, this.nextUpdate, this.extensions);
        }
    }

    public BasicOCSPRespGenerator(PublicKey publicKey) throws OCSPException {
        this.responderID = new RespID(publicKey);
    }

    public BasicOCSPRespGenerator(RespID respID) {
        this.responderID = respID;
    }

    private BasicOCSPResp generateResponse(String str, PrivateKey privateKey, X509Certificate[] x509CertificateArr, Date date, String str2, SecureRandom secureRandom) throws OCSPException, NoSuchProviderException {
        DERSequence dERSequence;
        Iterator it = this.list.iterator();
        try {
            DERObjectIdentifier algorithmOID = OCSPUtil.getAlgorithmOID(str);
            ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
            while (it.hasNext()) {
                try {
                    aSN1EncodableVector.add(((ResponseObject) it.next()).toResponse());
                } catch (Exception e) {
                    throw new OCSPException("exception creating Request", e);
                }
            }
            ResponseData responseData = new ResponseData(this.responderID.toASN1Object(), new DERGeneralizedTime(date), new DERSequence(aSN1EncodableVector), this.responseExtensions);
            try {
                Signature signatureCreateSignatureInstance = OCSPUtil.createSignatureInstance(str, str2);
                if (secureRandom != null) {
                    signatureCreateSignatureInstance.initSign(privateKey, secureRandom);
                } else {
                    signatureCreateSignatureInstance.initSign(privateKey);
                }
                try {
                    signatureCreateSignatureInstance.update(responseData.getEncoded(ASN1Encodable.DER));
                    DERBitString dERBitString = new DERBitString(signatureCreateSignatureInstance.sign());
                    AlgorithmIdentifier sigAlgID = OCSPUtil.getSigAlgID(algorithmOID);
                    if (x509CertificateArr == null || x509CertificateArr.length <= 0) {
                        dERSequence = null;
                    } else {
                        ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
                        for (int i = 0; i != x509CertificateArr.length; i++) {
                            try {
                                aSN1EncodableVector2.add(new X509CertificateStructure((ASN1Sequence) ASN1Object.fromByteArray(x509CertificateArr[i].getEncoded())));
                            } catch (IOException e2) {
                                throw new OCSPException("error processing certs", e2);
                            } catch (CertificateEncodingException e3) {
                                throw new OCSPException("error encoding certs", e3);
                            }
                        }
                        dERSequence = new DERSequence(aSN1EncodableVector2);
                    }
                    return new BasicOCSPResp(new BasicOCSPResponse(responseData, sigAlgID, dERBitString, dERSequence));
                } catch (Exception e4) {
                    throw new OCSPException("exception processing TBSRequest: " + e4, e4);
                }
            } catch (NoSuchProviderException e5) {
                throw e5;
            } catch (GeneralSecurityException e6) {
                throw new OCSPException("exception creating signature: " + e6, e6);
            }
        } catch (Exception unused) {
            throw new IllegalArgumentException("unknown signing algorithm specified");
        }
    }

    public void addResponse(CertificateID certificateID, CertificateStatus certificateStatus) {
        this.list.add(new ResponseObject(certificateID, certificateStatus, new Date(), null, null));
    }

    public void addResponse(CertificateID certificateID, CertificateStatus certificateStatus, Date date, Date date2, X509Extensions x509Extensions) {
        this.list.add(new ResponseObject(certificateID, certificateStatus, date, date2, x509Extensions));
    }

    public void addResponse(CertificateID certificateID, CertificateStatus certificateStatus, Date date, X509Extensions x509Extensions) {
        this.list.add(new ResponseObject(certificateID, certificateStatus, new Date(), date, x509Extensions));
    }

    public void addResponse(CertificateID certificateID, CertificateStatus certificateStatus, X509Extensions x509Extensions) {
        this.list.add(new ResponseObject(certificateID, certificateStatus, new Date(), null, x509Extensions));
    }

    public BasicOCSPResp generate(String str, PrivateKey privateKey, X509Certificate[] x509CertificateArr, Date date, String str2) throws OCSPException, IllegalArgumentException, NoSuchProviderException {
        return generate(str, privateKey, x509CertificateArr, date, str2, null);
    }

    public BasicOCSPResp generate(String str, PrivateKey privateKey, X509Certificate[] x509CertificateArr, Date date, String str2, SecureRandom secureRandom) throws OCSPException, IllegalArgumentException, NoSuchProviderException {
        if (str != null) {
            return generateResponse(str, privateKey, x509CertificateArr, date, str2, secureRandom);
        }
        throw new IllegalArgumentException("no signing algorithm specified");
    }

    public Iterator getSignatureAlgNames() {
        return OCSPUtil.getAlgNames();
    }

    public void setResponseExtensions(X509Extensions x509Extensions) {
        this.responseExtensions = x509Extensions;
    }
}
