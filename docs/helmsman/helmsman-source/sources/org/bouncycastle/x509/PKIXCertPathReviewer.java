package org.bouncycastle.x509;

import im.helmsman.helmsmanandroid.factory.OkHttpFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.PKIXParameters;
import java.security.cert.PolicyNode;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.security.auth.x500.X500Principal;
import kotlin.UByte;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DEREnumerated;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.x509.AccessDescription;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.AuthorityInformationAccess;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.GeneralSubtree;
import org.bouncycastle.asn1.x509.IssuingDistributionPoint;
import org.bouncycastle.asn1.x509.NameConstraints;
import org.bouncycastle.asn1.x509.PolicyInformation;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.qualified.MonetaryValue;
import org.bouncycastle.asn1.x509.qualified.QCStatement;
import org.bouncycastle.i18n.ErrorBundle;
import org.bouncycastle.i18n.LocaleString;
import org.bouncycastle.i18n.filter.TrustedInput;
import org.bouncycastle.i18n.filter.UntrustedInput;
import org.bouncycastle.i18n.filter.UntrustedUrlInput;
import org.bouncycastle.jce.provider.AnnotatedException;
import org.bouncycastle.jce.provider.CertPathValidatorUtilities;
import org.bouncycastle.jce.provider.PKIXNameConstraintValidator;
import org.bouncycastle.jce.provider.PKIXNameConstraintValidatorException;
import org.bouncycastle.jce.provider.PKIXPolicyNode;
import org.bouncycastle.x509.extension.X509ExtensionUtil;

/* JADX INFO: loaded from: classes2.dex */
public class PKIXCertPathReviewer extends CertPathValidatorUtilities {
    private static final String RESOURCE_NAME = "org.bouncycastle.x509.CertPathReviewerMessages";
    protected CertPath certPath;
    protected List certs;
    protected List[] errors;
    private boolean initialized;
    protected int n;
    protected List[] notifications;
    protected PKIXParameters pkixParams;
    protected PolicyNode policyTree;
    protected PublicKey subjectPublicKey;
    protected TrustAnchor trustAnchor;
    protected Date validDate;
    private static final String QC_STATEMENT = X509Extensions.QCStatements.getId();
    private static final String CRL_DIST_POINTS = X509Extensions.CRLDistributionPoints.getId();
    private static final String AUTH_INFO_ACCESS = X509Extensions.AuthorityInfoAccess.getId();

    public PKIXCertPathReviewer() {
    }

    public PKIXCertPathReviewer(CertPath certPath, PKIXParameters pKIXParameters) throws CertPathReviewerException {
        init(certPath, pKIXParameters);
    }

    private String IPtoString(byte[] bArr) {
        try {
            return InetAddress.getByAddress(bArr).getHostAddress();
        } catch (Exception unused) {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i != bArr.length; i++) {
                stringBuffer.append(Integer.toHexString(bArr[i] & UByte.MAX_VALUE));
                stringBuffer.append(' ');
            }
            return stringBuffer.toString();
        }
    }

    private void checkCriticalExtensions() {
        List<PKIXCertPathChecker> certPathCheckers = this.pkixParams.getCertPathCheckers();
        Iterator<PKIXCertPathChecker> it = certPathCheckers.iterator();
        while (it.hasNext()) {
            try {
                try {
                    it.next().init(false);
                } catch (CertPathValidatorException e) {
                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.certPathCheckerError", new Object[]{e.getMessage(), e, e.getClass().getName()}), e);
                }
            } catch (CertPathReviewerException e2) {
                addError(e2.getErrorMessage(), e2.getIndex());
                return;
            }
        }
        for (int size = this.certs.size() - 1; size >= 0; size--) {
            X509Certificate x509Certificate = (X509Certificate) this.certs.get(size);
            Set<String> criticalExtensionOIDs = x509Certificate.getCriticalExtensionOIDs();
            if (criticalExtensionOIDs != null && !criticalExtensionOIDs.isEmpty()) {
                criticalExtensionOIDs.remove(KEY_USAGE);
                criticalExtensionOIDs.remove(CERTIFICATE_POLICIES);
                criticalExtensionOIDs.remove(POLICY_MAPPINGS);
                criticalExtensionOIDs.remove(INHIBIT_ANY_POLICY);
                criticalExtensionOIDs.remove(ISSUING_DISTRIBUTION_POINT);
                criticalExtensionOIDs.remove(DELTA_CRL_INDICATOR);
                criticalExtensionOIDs.remove(POLICY_CONSTRAINTS);
                criticalExtensionOIDs.remove(BASIC_CONSTRAINTS);
                criticalExtensionOIDs.remove(SUBJECT_ALTERNATIVE_NAME);
                criticalExtensionOIDs.remove(NAME_CONSTRAINTS);
                String str = QC_STATEMENT;
                if (criticalExtensionOIDs.contains(str) && processQcStatements(x509Certificate, size)) {
                    criticalExtensionOIDs.remove(str);
                }
                Iterator<PKIXCertPathChecker> it2 = certPathCheckers.iterator();
                while (it2.hasNext()) {
                    try {
                        it2.next().check(x509Certificate, criticalExtensionOIDs);
                    } catch (CertPathValidatorException e3) {
                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.criticalExtensionError", new Object[]{e3.getMessage(), e3, e3.getClass().getName()}), e3.getCause(), this.certPath, size);
                    }
                }
                if (!criticalExtensionOIDs.isEmpty()) {
                    Iterator<String> it3 = criticalExtensionOIDs.iterator();
                    while (it3.hasNext()) {
                        addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.unknownCriticalExt", new Object[]{new DERObjectIdentifier(it3.next())}), size);
                    }
                }
            }
        }
    }

    private void checkNameConstraints() {
        PKIXNameConstraintValidator pKIXNameConstraintValidator = new PKIXNameConstraintValidator();
        try {
            for (int size = this.certs.size() - 1; size > 0; size--) {
                X509Certificate x509Certificate = (X509Certificate) this.certs.get(size);
                if (!isSelfIssued(x509Certificate)) {
                    X500Principal subjectPrincipal = getSubjectPrincipal(x509Certificate);
                    try {
                        ASN1Sequence aSN1Sequence = (ASN1Sequence) new ASN1InputStream(new ByteArrayInputStream(subjectPrincipal.getEncoded())).readObject();
                        try {
                            pKIXNameConstraintValidator.checkPermittedDN(aSN1Sequence);
                            try {
                                pKIXNameConstraintValidator.checkExcludedDN(aSN1Sequence);
                                try {
                                    ASN1Sequence aSN1Sequence2 = (ASN1Sequence) getExtensionValue(x509Certificate, SUBJECT_ALTERNATIVE_NAME);
                                    if (aSN1Sequence2 != null) {
                                        for (int i = 0; i < aSN1Sequence2.size(); i++) {
                                            GeneralName generalName = GeneralName.getInstance(aSN1Sequence2.getObjectAt(i));
                                            try {
                                                pKIXNameConstraintValidator.checkPermitted(generalName);
                                                pKIXNameConstraintValidator.checkExcluded(generalName);
                                            } catch (PKIXNameConstraintValidatorException e) {
                                                throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.notPermittedEmail", new Object[]{new UntrustedInput(generalName)}), e, this.certPath, size);
                                            }
                                        }
                                    }
                                } catch (AnnotatedException e2) {
                                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.subjAltNameExtError"), e2, this.certPath, size);
                                }
                            } catch (PKIXNameConstraintValidatorException e3) {
                                throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.excludedDN", new Object[]{new UntrustedInput(subjectPrincipal.getName())}), e3, this.certPath, size);
                            }
                        } catch (PKIXNameConstraintValidatorException e4) {
                            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.notPermittedDN", new Object[]{new UntrustedInput(subjectPrincipal.getName())}), e4, this.certPath, size);
                        }
                    } catch (IOException e5) {
                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.ncSubjectNameError", new Object[]{new UntrustedInput(subjectPrincipal)}), e5, this.certPath, size);
                    }
                }
                try {
                    ASN1Sequence aSN1Sequence3 = (ASN1Sequence) getExtensionValue(x509Certificate, NAME_CONSTRAINTS);
                    if (aSN1Sequence3 != null) {
                        NameConstraints nameConstraints = new NameConstraints(aSN1Sequence3);
                        ASN1Sequence permittedSubtrees = nameConstraints.getPermittedSubtrees();
                        if (permittedSubtrees != null) {
                            pKIXNameConstraintValidator.intersectPermittedSubtree(permittedSubtrees);
                        }
                        ASN1Sequence excludedSubtrees = nameConstraints.getExcludedSubtrees();
                        if (excludedSubtrees != null) {
                            Enumeration objects = excludedSubtrees.getObjects();
                            while (objects.hasMoreElements()) {
                                pKIXNameConstraintValidator.addExcludedSubtree(GeneralSubtree.getInstance(objects.nextElement()));
                            }
                        }
                    }
                } catch (AnnotatedException e6) {
                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.ncExtError"), e6, this.certPath, size);
                }
            }
        } catch (CertPathReviewerException e7) {
            addError(e7.getErrorMessage(), e7.getIndex());
        }
    }

    private void checkPathLength() {
        BasicConstraints basicConstraints;
        BigInteger pathLenConstraint;
        int iIntValue;
        int i = this.n;
        int i2 = 0;
        for (int size = this.certs.size() - 1; size > 0; size--) {
            X509Certificate x509Certificate = (X509Certificate) this.certs.get(size);
            if (!isSelfIssued(x509Certificate)) {
                if (i <= 0) {
                    addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.pathLenghtExtended"));
                }
                i--;
                i2++;
            }
            try {
                basicConstraints = BasicConstraints.getInstance(getExtensionValue(x509Certificate, BASIC_CONSTRAINTS));
            } catch (AnnotatedException unused) {
                addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.processLengthConstError"), size);
                basicConstraints = null;
            }
            if (basicConstraints != null && (pathLenConstraint = basicConstraints.getPathLenConstraint()) != null && (iIntValue = pathLenConstraint.intValue()) < i) {
                i = iIntValue;
            }
        }
        addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.totalPathLength", new Object[]{new Integer(i2)}));
    }

    /* JADX WARN: Code duplicated, block: B:100:0x021f A[Catch: CertPathReviewerException -> 0x0602, TryCatch #8 {CertPathReviewerException -> 0x0602, blocks: (B:17:0x006f, B:21:0x007f, B:23:0x008c, B:27:0x009c, B:28:0x00a7, B:30:0x00ad, B:32:0x00ce, B:33:0x00d6, B:35:0x00dc, B:37:0x00e1, B:38:0x00ed, B:42:0x00f9, B:45:0x0100, B:46:0x0109, B:48:0x010f, B:50:0x0119, B:53:0x0120, B:55:0x0124, B:95:0x0210, B:97:0x0216, B:98:0x0219, B:100:0x021f, B:102:0x022b, B:105:0x0233, B:106:0x0236, B:107:0x0239, B:109:0x023f, B:110:0x0248, B:112:0x024e, B:120:0x0271, B:121:0x027d, B:122:0x027e, B:124:0x0282, B:126:0x028a, B:127:0x028e, B:129:0x0294, B:132:0x02b6, B:134:0x02c0, B:135:0x02c5, B:136:0x02d1, B:137:0x02d2, B:138:0x02de, B:140:0x02e1, B:141:0x02ee, B:143:0x02f4, B:145:0x031a, B:147:0x0332, B:146:0x0329, B:148:0x0339, B:149:0x033f, B:151:0x0345, B:153:0x034d, B:164:0x0377, B:157:0x0355, B:158:0x0361, B:160:0x0363, B:161:0x0372, B:167:0x0380, B:178:0x039f, B:180:0x03a9, B:181:0x03ad, B:183:0x03b3, B:188:0x03c3, B:191:0x03d3, B:194:0x03e3, B:196:0x03ed, B:207:0x042f, B:199:0x03f9, B:200:0x0407, B:201:0x0408, B:202:0x0416, B:204:0x0418, B:205:0x0426, B:59:0x0133, B:60:0x0137, B:62:0x013d, B:64:0x0153, B:66:0x015d, B:67:0x0162, B:69:0x0168, B:70:0x0176, B:72:0x017c, B:74:0x0188, B:78:0x0195, B:79:0x019b, B:81:0x01a1, B:86:0x01ba, B:75:0x018b, B:77:0x018f, B:90:0x01f3, B:93:0x0203, B:94:0x020f, B:209:0x043e, B:210:0x044b, B:211:0x044c, B:215:0x045d, B:217:0x0467, B:218:0x046c, B:220:0x0472, B:223:0x047f, B:229:0x0494, B:308:0x05e8, B:309:0x05f4, B:232:0x049f, B:233:0x04ab, B:234:0x04ac, B:236:0x04b2, B:238:0x04ba, B:240:0x04c0, B:243:0x04ca, B:244:0x04cd, B:246:0x04d3, B:248:0x04e3, B:249:0x04e7, B:251:0x04ed, B:252:0x04f5, B:253:0x04f8, B:254:0x04fd, B:255:0x0501, B:257:0x0507, B:259:0x0517, B:261:0x051f, B:262:0x0522, B:264:0x0528, B:266:0x0534, B:267:0x0538, B:268:0x053b, B:269:0x053e, B:270:0x054a, B:272:0x054f, B:274:0x0559, B:275:0x055c, B:277:0x0562, B:279:0x0572, B:280:0x0576, B:282:0x057c, B:284:0x058c, B:285:0x0590, B:286:0x0593, B:287:0x0596, B:288:0x059c, B:290:0x05a2, B:292:0x05b4, B:295:0x05be, B:297:0x05c4, B:298:0x05c7, B:300:0x05cd, B:302:0x05d9, B:303:0x05dd, B:304:0x05e0, B:310:0x05f5, B:311:0x0601), top: B:327:0x006f, inners: #0, #1, #2, #4, #5, #7, #9, #10 }] */
    /* JADX WARN: Code duplicated, block: B:102:0x022b A[Catch: CertPathReviewerException -> 0x0602, TryCatch #8 {CertPathReviewerException -> 0x0602, blocks: (B:17:0x006f, B:21:0x007f, B:23:0x008c, B:27:0x009c, B:28:0x00a7, B:30:0x00ad, B:32:0x00ce, B:33:0x00d6, B:35:0x00dc, B:37:0x00e1, B:38:0x00ed, B:42:0x00f9, B:45:0x0100, B:46:0x0109, B:48:0x010f, B:50:0x0119, B:53:0x0120, B:55:0x0124, B:95:0x0210, B:97:0x0216, B:98:0x0219, B:100:0x021f, B:102:0x022b, B:105:0x0233, B:106:0x0236, B:107:0x0239, B:109:0x023f, B:110:0x0248, B:112:0x024e, B:120:0x0271, B:121:0x027d, B:122:0x027e, B:124:0x0282, B:126:0x028a, B:127:0x028e, B:129:0x0294, B:132:0x02b6, B:134:0x02c0, B:135:0x02c5, B:136:0x02d1, B:137:0x02d2, B:138:0x02de, B:140:0x02e1, B:141:0x02ee, B:143:0x02f4, B:145:0x031a, B:147:0x0332, B:146:0x0329, B:148:0x0339, B:149:0x033f, B:151:0x0345, B:153:0x034d, B:164:0x0377, B:157:0x0355, B:158:0x0361, B:160:0x0363, B:161:0x0372, B:167:0x0380, B:178:0x039f, B:180:0x03a9, B:181:0x03ad, B:183:0x03b3, B:188:0x03c3, B:191:0x03d3, B:194:0x03e3, B:196:0x03ed, B:207:0x042f, B:199:0x03f9, B:200:0x0407, B:201:0x0408, B:202:0x0416, B:204:0x0418, B:205:0x0426, B:59:0x0133, B:60:0x0137, B:62:0x013d, B:64:0x0153, B:66:0x015d, B:67:0x0162, B:69:0x0168, B:70:0x0176, B:72:0x017c, B:74:0x0188, B:78:0x0195, B:79:0x019b, B:81:0x01a1, B:86:0x01ba, B:75:0x018b, B:77:0x018f, B:90:0x01f3, B:93:0x0203, B:94:0x020f, B:209:0x043e, B:210:0x044b, B:211:0x044c, B:215:0x045d, B:217:0x0467, B:218:0x046c, B:220:0x0472, B:223:0x047f, B:229:0x0494, B:308:0x05e8, B:309:0x05f4, B:232:0x049f, B:233:0x04ab, B:234:0x04ac, B:236:0x04b2, B:238:0x04ba, B:240:0x04c0, B:243:0x04ca, B:244:0x04cd, B:246:0x04d3, B:248:0x04e3, B:249:0x04e7, B:251:0x04ed, B:252:0x04f5, B:253:0x04f8, B:254:0x04fd, B:255:0x0501, B:257:0x0507, B:259:0x0517, B:261:0x051f, B:262:0x0522, B:264:0x0528, B:266:0x0534, B:267:0x0538, B:268:0x053b, B:269:0x053e, B:270:0x054a, B:272:0x054f, B:274:0x0559, B:275:0x055c, B:277:0x0562, B:279:0x0572, B:280:0x0576, B:282:0x057c, B:284:0x058c, B:285:0x0590, B:286:0x0593, B:287:0x0596, B:288:0x059c, B:290:0x05a2, B:292:0x05b4, B:295:0x05be, B:297:0x05c4, B:298:0x05c7, B:300:0x05cd, B:302:0x05d9, B:303:0x05dd, B:304:0x05e0, B:310:0x05f5, B:311:0x0601), top: B:327:0x006f, inners: #0, #1, #2, #4, #5, #7, #9, #10 }] */
    /* JADX WARN: Code duplicated, block: B:109:0x023f A[Catch: CertPathReviewerException -> 0x0602, TryCatch #8 {CertPathReviewerException -> 0x0602, blocks: (B:17:0x006f, B:21:0x007f, B:23:0x008c, B:27:0x009c, B:28:0x00a7, B:30:0x00ad, B:32:0x00ce, B:33:0x00d6, B:35:0x00dc, B:37:0x00e1, B:38:0x00ed, B:42:0x00f9, B:45:0x0100, B:46:0x0109, B:48:0x010f, B:50:0x0119, B:53:0x0120, B:55:0x0124, B:95:0x0210, B:97:0x0216, B:98:0x0219, B:100:0x021f, B:102:0x022b, B:105:0x0233, B:106:0x0236, B:107:0x0239, B:109:0x023f, B:110:0x0248, B:112:0x024e, B:120:0x0271, B:121:0x027d, B:122:0x027e, B:124:0x0282, B:126:0x028a, B:127:0x028e, B:129:0x0294, B:132:0x02b6, B:134:0x02c0, B:135:0x02c5, B:136:0x02d1, B:137:0x02d2, B:138:0x02de, B:140:0x02e1, B:141:0x02ee, B:143:0x02f4, B:145:0x031a, B:147:0x0332, B:146:0x0329, B:148:0x0339, B:149:0x033f, B:151:0x0345, B:153:0x034d, B:164:0x0377, B:157:0x0355, B:158:0x0361, B:160:0x0363, B:161:0x0372, B:167:0x0380, B:178:0x039f, B:180:0x03a9, B:181:0x03ad, B:183:0x03b3, B:188:0x03c3, B:191:0x03d3, B:194:0x03e3, B:196:0x03ed, B:207:0x042f, B:199:0x03f9, B:200:0x0407, B:201:0x0408, B:202:0x0416, B:204:0x0418, B:205:0x0426, B:59:0x0133, B:60:0x0137, B:62:0x013d, B:64:0x0153, B:66:0x015d, B:67:0x0162, B:69:0x0168, B:70:0x0176, B:72:0x017c, B:74:0x0188, B:78:0x0195, B:79:0x019b, B:81:0x01a1, B:86:0x01ba, B:75:0x018b, B:77:0x018f, B:90:0x01f3, B:93:0x0203, B:94:0x020f, B:209:0x043e, B:210:0x044b, B:211:0x044c, B:215:0x045d, B:217:0x0467, B:218:0x046c, B:220:0x0472, B:223:0x047f, B:229:0x0494, B:308:0x05e8, B:309:0x05f4, B:232:0x049f, B:233:0x04ab, B:234:0x04ac, B:236:0x04b2, B:238:0x04ba, B:240:0x04c0, B:243:0x04ca, B:244:0x04cd, B:246:0x04d3, B:248:0x04e3, B:249:0x04e7, B:251:0x04ed, B:252:0x04f5, B:253:0x04f8, B:254:0x04fd, B:255:0x0501, B:257:0x0507, B:259:0x0517, B:261:0x051f, B:262:0x0522, B:264:0x0528, B:266:0x0534, B:267:0x0538, B:268:0x053b, B:269:0x053e, B:270:0x054a, B:272:0x054f, B:274:0x0559, B:275:0x055c, B:277:0x0562, B:279:0x0572, B:280:0x0576, B:282:0x057c, B:284:0x058c, B:285:0x0590, B:286:0x0593, B:287:0x0596, B:288:0x059c, B:290:0x05a2, B:292:0x05b4, B:295:0x05be, B:297:0x05c4, B:298:0x05c7, B:300:0x05cd, B:302:0x05d9, B:303:0x05dd, B:304:0x05e0, B:310:0x05f5, B:311:0x0601), top: B:327:0x006f, inners: #0, #1, #2, #4, #5, #7, #9, #10 }] */
    /* JADX WARN: Code duplicated, block: B:112:0x024e A[Catch: CertPathReviewerException -> 0x0602, LOOP:6: B:110:0x0248->B:112:0x024e, LOOP_END, TryCatch #8 {CertPathReviewerException -> 0x0602, blocks: (B:17:0x006f, B:21:0x007f, B:23:0x008c, B:27:0x009c, B:28:0x00a7, B:30:0x00ad, B:32:0x00ce, B:33:0x00d6, B:35:0x00dc, B:37:0x00e1, B:38:0x00ed, B:42:0x00f9, B:45:0x0100, B:46:0x0109, B:48:0x010f, B:50:0x0119, B:53:0x0120, B:55:0x0124, B:95:0x0210, B:97:0x0216, B:98:0x0219, B:100:0x021f, B:102:0x022b, B:105:0x0233, B:106:0x0236, B:107:0x0239, B:109:0x023f, B:110:0x0248, B:112:0x024e, B:120:0x0271, B:121:0x027d, B:122:0x027e, B:124:0x0282, B:126:0x028a, B:127:0x028e, B:129:0x0294, B:132:0x02b6, B:134:0x02c0, B:135:0x02c5, B:136:0x02d1, B:137:0x02d2, B:138:0x02de, B:140:0x02e1, B:141:0x02ee, B:143:0x02f4, B:145:0x031a, B:147:0x0332, B:146:0x0329, B:148:0x0339, B:149:0x033f, B:151:0x0345, B:153:0x034d, B:164:0x0377, B:157:0x0355, B:158:0x0361, B:160:0x0363, B:161:0x0372, B:167:0x0380, B:178:0x039f, B:180:0x03a9, B:181:0x03ad, B:183:0x03b3, B:188:0x03c3, B:191:0x03d3, B:194:0x03e3, B:196:0x03ed, B:207:0x042f, B:199:0x03f9, B:200:0x0407, B:201:0x0408, B:202:0x0416, B:204:0x0418, B:205:0x0426, B:59:0x0133, B:60:0x0137, B:62:0x013d, B:64:0x0153, B:66:0x015d, B:67:0x0162, B:69:0x0168, B:70:0x0176, B:72:0x017c, B:74:0x0188, B:78:0x0195, B:79:0x019b, B:81:0x01a1, B:86:0x01ba, B:75:0x018b, B:77:0x018f, B:90:0x01f3, B:93:0x0203, B:94:0x020f, B:209:0x043e, B:210:0x044b, B:211:0x044c, B:215:0x045d, B:217:0x0467, B:218:0x046c, B:220:0x0472, B:223:0x047f, B:229:0x0494, B:308:0x05e8, B:309:0x05f4, B:232:0x049f, B:233:0x04ab, B:234:0x04ac, B:236:0x04b2, B:238:0x04ba, B:240:0x04c0, B:243:0x04ca, B:244:0x04cd, B:246:0x04d3, B:248:0x04e3, B:249:0x04e7, B:251:0x04ed, B:252:0x04f5, B:253:0x04f8, B:254:0x04fd, B:255:0x0501, B:257:0x0507, B:259:0x0517, B:261:0x051f, B:262:0x0522, B:264:0x0528, B:266:0x0534, B:267:0x0538, B:268:0x053b, B:269:0x053e, B:270:0x054a, B:272:0x054f, B:274:0x0559, B:275:0x055c, B:277:0x0562, B:279:0x0572, B:280:0x0576, B:282:0x057c, B:284:0x058c, B:285:0x0590, B:286:0x0593, B:287:0x0596, B:288:0x059c, B:290:0x05a2, B:292:0x05b4, B:295:0x05be, B:297:0x05c4, B:298:0x05c7, B:300:0x05cd, B:302:0x05d9, B:303:0x05dd, B:304:0x05e0, B:310:0x05f5, B:311:0x0601), top: B:327:0x006f, inners: #0, #1, #2, #4, #5, #7, #9, #10 }] */
    /* JADX WARN: Code duplicated, block: B:271:0x054b  */
    /* JADX WARN: Code duplicated, block: B:353:0x0232 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:354:0x0233 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:355:0x0233 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:97:0x0216 A[Catch: CertPathReviewerException -> 0x0602, TryCatch #8 {CertPathReviewerException -> 0x0602, blocks: (B:17:0x006f, B:21:0x007f, B:23:0x008c, B:27:0x009c, B:28:0x00a7, B:30:0x00ad, B:32:0x00ce, B:33:0x00d6, B:35:0x00dc, B:37:0x00e1, B:38:0x00ed, B:42:0x00f9, B:45:0x0100, B:46:0x0109, B:48:0x010f, B:50:0x0119, B:53:0x0120, B:55:0x0124, B:95:0x0210, B:97:0x0216, B:98:0x0219, B:100:0x021f, B:102:0x022b, B:105:0x0233, B:106:0x0236, B:107:0x0239, B:109:0x023f, B:110:0x0248, B:112:0x024e, B:120:0x0271, B:121:0x027d, B:122:0x027e, B:124:0x0282, B:126:0x028a, B:127:0x028e, B:129:0x0294, B:132:0x02b6, B:134:0x02c0, B:135:0x02c5, B:136:0x02d1, B:137:0x02d2, B:138:0x02de, B:140:0x02e1, B:141:0x02ee, B:143:0x02f4, B:145:0x031a, B:147:0x0332, B:146:0x0329, B:148:0x0339, B:149:0x033f, B:151:0x0345, B:153:0x034d, B:164:0x0377, B:157:0x0355, B:158:0x0361, B:160:0x0363, B:161:0x0372, B:167:0x0380, B:178:0x039f, B:180:0x03a9, B:181:0x03ad, B:183:0x03b3, B:188:0x03c3, B:191:0x03d3, B:194:0x03e3, B:196:0x03ed, B:207:0x042f, B:199:0x03f9, B:200:0x0407, B:201:0x0408, B:202:0x0416, B:204:0x0418, B:205:0x0426, B:59:0x0133, B:60:0x0137, B:62:0x013d, B:64:0x0153, B:66:0x015d, B:67:0x0162, B:69:0x0168, B:70:0x0176, B:72:0x017c, B:74:0x0188, B:78:0x0195, B:79:0x019b, B:81:0x01a1, B:86:0x01ba, B:75:0x018b, B:77:0x018f, B:90:0x01f3, B:93:0x0203, B:94:0x020f, B:209:0x043e, B:210:0x044b, B:211:0x044c, B:215:0x045d, B:217:0x0467, B:218:0x046c, B:220:0x0472, B:223:0x047f, B:229:0x0494, B:308:0x05e8, B:309:0x05f4, B:232:0x049f, B:233:0x04ab, B:234:0x04ac, B:236:0x04b2, B:238:0x04ba, B:240:0x04c0, B:243:0x04ca, B:244:0x04cd, B:246:0x04d3, B:248:0x04e3, B:249:0x04e7, B:251:0x04ed, B:252:0x04f5, B:253:0x04f8, B:254:0x04fd, B:255:0x0501, B:257:0x0507, B:259:0x0517, B:261:0x051f, B:262:0x0522, B:264:0x0528, B:266:0x0534, B:267:0x0538, B:268:0x053b, B:269:0x053e, B:270:0x054a, B:272:0x054f, B:274:0x0559, B:275:0x055c, B:277:0x0562, B:279:0x0572, B:280:0x0576, B:282:0x057c, B:284:0x058c, B:285:0x0590, B:286:0x0593, B:287:0x0596, B:288:0x059c, B:290:0x05a2, B:292:0x05b4, B:295:0x05be, B:297:0x05c4, B:298:0x05c7, B:300:0x05cd, B:302:0x05d9, B:303:0x05dd, B:304:0x05e0, B:310:0x05f5, B:311:0x0601), top: B:327:0x006f, inners: #0, #1, #2, #4, #5, #7, #9, #10 }] */
    private void checkPolicy() {
        int i;
        int i2;
        PKIXPolicyNode pKIXPolicyNodeRemovePolicyNode;
        String str;
        int i3;
        int i4;
        HashSet hashSet;
        String str2;
        int i5;
        int iIntValue;
        int iIntValue2;
        String str3;
        HashSet hashSet2;
        String id;
        int i6;
        Set<String> criticalExtensionOIDs;
        boolean zContains;
        ArrayList arrayList;
        int i7;
        ArrayList arrayList2;
        int i8;
        PKIXPolicyNode pKIXPolicyNode;
        PKIXPolicyNode pKIXPolicyNodeRemovePolicyNode2;
        String str4 = "CertPathReviewer.policyExtError";
        Set<String> initialPolicies = this.pkixParams.getInitialPolicies();
        int i9 = this.n + 1;
        ArrayList[] arrayListArr = new ArrayList[i9];
        for (int i10 = 0; i10 < i9; i10++) {
            arrayListArr[i10] = new ArrayList();
        }
        HashSet hashSet3 = new HashSet();
        hashSet3.add("2.5.29.32.0");
        PKIXPolicyNode pKIXPolicyNode2 = new PKIXPolicyNode(new ArrayList(), 0, hashSet3, null, new HashSet(), "2.5.29.32.0", false);
        arrayListArr[0].add(pKIXPolicyNode2);
        if (this.pkixParams.isExplicitPolicyRequired()) {
            i2 = 0;
            i = 1;
        } else {
            i = 1;
            i2 = this.n + 1;
        }
        int i11 = this.pkixParams.isAnyPolicyInhibited() ? 0 : this.n + i;
        int i12 = this.pkixParams.isPolicyMappingInhibited() ? 0 : this.n + i;
        try {
            int size = this.certs.size() - i;
            PKIXPolicyNode pKIXPolicyNodePrepareNextCertB2 = pKIXPolicyNode2;
            X509Certificate x509Certificate = null;
            HashSet hashSet4 = null;
            while (size >= 0) {
                int i13 = this.n - size;
                X509Certificate x509Certificate2 = (X509Certificate) this.certs.get(size);
                int i14 = i9;
                try {
                    ASN1Sequence aSN1Sequence = (ASN1Sequence) getExtensionValue(x509Certificate2, CERTIFICATE_POLICIES);
                    if (aSN1Sequence == null || pKIXPolicyNodePrepareNextCertB2 == null) {
                        str = str4;
                        i3 = i11;
                        i4 = i12;
                        pKIXPolicyNodePrepareNextCertB2 = pKIXPolicyNodePrepareNextCertB2;
                    } else {
                        Enumeration objects = aSN1Sequence.getObjects();
                        HashSet hashSet5 = new HashSet();
                        while (objects.hasMoreElements()) {
                            PolicyInformation policyInformation = PolicyInformation.getInstance(objects.nextElement());
                            PKIXPolicyNode pKIXPolicyNode3 = pKIXPolicyNodePrepareNextCertB2;
                            DERObjectIdentifier policyIdentifier = policyInformation.getPolicyIdentifier();
                            String str5 = str4;
                            hashSet5.add(policyIdentifier.getId());
                            if (!"2.5.29.32.0".equals(policyIdentifier.getId())) {
                                try {
                                    Set qualifierSet = getQualifierSet(policyInformation.getPolicyQualifiers());
                                    if (!processCertD1i(i13, arrayListArr, policyIdentifier, qualifierSet)) {
                                        processCertD1ii(i13, arrayListArr, policyIdentifier, qualifierSet);
                                    }
                                } catch (CertPathValidatorException e) {
                                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.policyQualifierError"), e, this.certPath, size);
                                }
                            }
                            pKIXPolicyNodePrepareNextCertB2 = pKIXPolicyNode3;
                            str4 = str5;
                        }
                        str = str4;
                        PKIXPolicyNode pKIXPolicyNode4 = pKIXPolicyNodePrepareNextCertB2;
                        if (hashSet4 == null || hashSet4.contains("2.5.29.32.0")) {
                            hashSet2 = hashSet5;
                        } else {
                            hashSet2 = new HashSet();
                            for (Object obj : hashSet4) {
                                if (hashSet5.contains(obj)) {
                                    hashSet2.add(obj);
                                }
                            }
                        }
                        if (i11 > 0 || (i13 < this.n && isSelfIssued(x509Certificate2))) {
                            Enumeration objects2 = aSN1Sequence.getObjects();
                            while (true) {
                                if (!objects2.hasMoreElements()) {
                                    i3 = i11;
                                    i4 = i12;
                                    break;
                                }
                                PolicyInformation policyInformation2 = PolicyInformation.getInstance(objects2.nextElement());
                                if ("2.5.29.32.0".equals(policyInformation2.getPolicyIdentifier().getId())) {
                                    try {
                                        Set qualifierSet2 = getQualifierSet(policyInformation2.getPolicyQualifiers());
                                        ArrayList arrayList3 = arrayListArr[i13 - 1];
                                        for (int i15 = 0; i15 < arrayList3.size(); i15++) {
                                            PKIXPolicyNode pKIXPolicyNode5 = (PKIXPolicyNode) arrayList3.get(i15);
                                            for (Object obj2 : pKIXPolicyNode5.getExpectedPolicies()) {
                                                arrayList3 = arrayList3;
                                                i11 = i11;
                                                if (obj2 instanceof String) {
                                                    id = (String) obj2;
                                                } else if (obj2 instanceof DERObjectIdentifier) {
                                                    id = ((DERObjectIdentifier) obj2).getId();
                                                }
                                                Iterator children = pKIXPolicyNode5.getChildren();
                                                boolean z = false;
                                                while (children.hasNext()) {
                                                    Iterator it = children;
                                                    if (id.equals(((PKIXPolicyNode) children.next()).getValidPolicy())) {
                                                        z = true;
                                                    }
                                                    children = it;
                                                }
                                                if (!z) {
                                                    HashSet hashSet6 = new HashSet();
                                                    hashSet6.add(id);
                                                    PKIXPolicyNode pKIXPolicyNode6 = new PKIXPolicyNode(new ArrayList(), i13, hashSet6, pKIXPolicyNode5, qualifierSet2, id, false);
                                                    pKIXPolicyNode5.addChild(pKIXPolicyNode6);
                                                    arrayListArr[i13].add(pKIXPolicyNode6);
                                                }
                                                i12 = i12;
                                            }
                                        }
                                        i3 = i11;
                                        i4 = i12;
                                        break;
                                    } catch (CertPathValidatorException e2) {
                                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.policyQualifierError"), e2, this.certPath, size);
                                    }
                                }
                            }
                            pKIXPolicyNodePrepareNextCertB2 = pKIXPolicyNode4;
                            for (i6 = i13 - 1; i6 >= 0; i6--) {
                                arrayList2 = arrayListArr[i6];
                                for (i8 = 0; i8 < arrayList2.size(); i8++) {
                                    pKIXPolicyNode = (PKIXPolicyNode) arrayList2.get(i8);
                                    if (!pKIXPolicyNode.hasChildren()) {
                                        pKIXPolicyNodeRemovePolicyNode2 = removePolicyNode(pKIXPolicyNodePrepareNextCertB2, arrayListArr, pKIXPolicyNode);
                                        pKIXPolicyNodePrepareNextCertB2 = pKIXPolicyNodeRemovePolicyNode2;
                                        if (pKIXPolicyNodeRemovePolicyNode2 == null) {
                                            break;
                                        }
                                    }
                                }
                            }
                            criticalExtensionOIDs = x509Certificate2.getCriticalExtensionOIDs();
                            if (criticalExtensionOIDs != null) {
                                zContains = criticalExtensionOIDs.contains(CERTIFICATE_POLICIES);
                                arrayList = arrayListArr[i13];
                                for (i7 = 0; i7 < arrayList.size(); i7++) {
                                    ((PKIXPolicyNode) arrayList.get(i7)).setCritical(zContains);
                                }
                            }
                            hashSet4 = hashSet2;
                        } else {
                            i3 = i11;
                            i4 = i12;
                            pKIXPolicyNodePrepareNextCertB2 = pKIXPolicyNode4;
                            while (i6 >= 0) {
                                arrayList2 = arrayListArr[i6];
                                while (i8 < arrayList2.size()) {
                                    pKIXPolicyNode = (PKIXPolicyNode) arrayList2.get(i8);
                                    if (!pKIXPolicyNode.hasChildren()) {
                                        pKIXPolicyNodeRemovePolicyNode2 = removePolicyNode(pKIXPolicyNodePrepareNextCertB2, arrayListArr, pKIXPolicyNode);
                                        pKIXPolicyNodePrepareNextCertB2 = pKIXPolicyNodeRemovePolicyNode2;
                                        if (pKIXPolicyNodeRemovePolicyNode2 == null) {
                                            break;
                                            break;
                                        }
                                    }
                                }
                            }
                            criticalExtensionOIDs = x509Certificate2.getCriticalExtensionOIDs();
                            if (criticalExtensionOIDs != null) {
                                zContains = criticalExtensionOIDs.contains(CERTIFICATE_POLICIES);
                                arrayList = arrayListArr[i13];
                                while (i7 < arrayList.size()) {
                                    ((PKIXPolicyNode) arrayList.get(i7)).setCritical(zContains);
                                }
                            }
                            hashSet4 = hashSet2;
                        }
                    }
                    if (aSN1Sequence == null) {
                        pKIXPolicyNodePrepareNextCertB2 = null;
                    }
                    if (i2 <= 0 && pKIXPolicyNodePrepareNextCertB2 == null) {
                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noValidPolicyTree"));
                    }
                    if (i13 != this.n) {
                        try {
                            DERObject extensionValue = getExtensionValue(x509Certificate2, POLICY_MAPPINGS);
                            if (extensionValue != null) {
                                ASN1Sequence aSN1Sequence2 = (ASN1Sequence) extensionValue;
                                int i16 = 0;
                                while (i16 < aSN1Sequence2.size()) {
                                    ASN1Sequence aSN1Sequence3 = (ASN1Sequence) aSN1Sequence2.getObjectAt(i16);
                                    DERObjectIdentifier dERObjectIdentifier = (DERObjectIdentifier) aSN1Sequence3.getObjectAt(0);
                                    DERObjectIdentifier dERObjectIdentifier2 = (DERObjectIdentifier) aSN1Sequence3.getObjectAt(1);
                                    boolean zEquals = "2.5.29.32.0".equals(dERObjectIdentifier.getId());
                                    ASN1Sequence aSN1Sequence4 = aSN1Sequence2;
                                    if (zEquals) {
                                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.invalidPolicyMapping"), this.certPath, size);
                                    }
                                    if ("2.5.29.32.0".equals(dERObjectIdentifier2.getId())) {
                                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.invalidPolicyMapping"), this.certPath, size);
                                    }
                                    i16++;
                                    aSN1Sequence2 = aSN1Sequence4;
                                }
                            }
                            if (extensionValue != null) {
                                ASN1Sequence aSN1Sequence5 = (ASN1Sequence) extensionValue;
                                HashMap map = new HashMap();
                                HashSet<String> hashSet7 = new HashSet();
                                int i17 = 0;
                                while (i17 < aSN1Sequence5.size()) {
                                    ASN1Sequence aSN1Sequence6 = (ASN1Sequence) aSN1Sequence5.getObjectAt(i17);
                                    ASN1Sequence aSN1Sequence7 = aSN1Sequence5;
                                    String id2 = ((DERObjectIdentifier) aSN1Sequence6.getObjectAt(0)).getId();
                                    HashSet hashSet8 = hashSet4;
                                    String id3 = ((DERObjectIdentifier) aSN1Sequence6.getObjectAt(1)).getId();
                                    if (map.containsKey(id2)) {
                                        ((Set) map.get(id2)).add(id3);
                                    } else {
                                        HashSet hashSet9 = new HashSet();
                                        hashSet9.add(id3);
                                        map.put(id2, hashSet9);
                                        hashSet7.add(id2);
                                    }
                                    i17++;
                                    aSN1Sequence5 = aSN1Sequence7;
                                    hashSet4 = hashSet8;
                                }
                                hashSet = hashSet4;
                                for (String str6 : hashSet7) {
                                    if (i4 > 0) {
                                        try {
                                            prepareNextCertB1(i13, arrayListArr, str6, map, x509Certificate2);
                                            str3 = str;
                                        } catch (CertPathValidatorException e3) {
                                            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.policyQualifierError"), e3, this.certPath, size);
                                        } catch (AnnotatedException e4) {
                                            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, str), e4, this.certPath, size);
                                        }
                                    } else {
                                        str3 = str;
                                        if (i4 <= 0) {
                                            pKIXPolicyNodePrepareNextCertB2 = prepareNextCertB2(i13, arrayListArr, str6, pKIXPolicyNodePrepareNextCertB2);
                                        }
                                    }
                                    str = str3;
                                }
                            } else {
                                hashSet = hashSet4;
                            }
                            str2 = str;
                            if (isSelfIssued(x509Certificate2)) {
                                i5 = i3;
                                i12 = i4;
                            } else {
                                if (i2 != 0) {
                                    i2--;
                                }
                                i12 = i4 != 0 ? i4 - 1 : i4;
                                i5 = i3 != 0 ? i3 - 1 : i3;
                            }
                            try {
                                ASN1Sequence aSN1Sequence8 = (ASN1Sequence) getExtensionValue(x509Certificate2, POLICY_CONSTRAINTS);
                                if (aSN1Sequence8 != null) {
                                    Enumeration objects3 = aSN1Sequence8.getObjects();
                                    while (objects3.hasMoreElements()) {
                                        ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject) objects3.nextElement();
                                        int tagNo = aSN1TaggedObject.getTagNo();
                                        if (tagNo == 0) {
                                            int iIntValue3 = DERInteger.getInstance(aSN1TaggedObject).getValue().intValue();
                                            if (iIntValue3 < i2) {
                                                i2 = iIntValue3;
                                            }
                                        } else if (tagNo == 1 && (iIntValue2 = DERInteger.getInstance(aSN1TaggedObject).getValue().intValue()) < i12) {
                                            i12 = iIntValue2;
                                        }
                                    }
                                }
                                try {
                                    DERInteger dERInteger = (DERInteger) getExtensionValue(x509Certificate2, INHIBIT_ANY_POLICY);
                                    if (dERInteger != null && (iIntValue = dERInteger.getValue().intValue()) < i5) {
                                        i5 = iIntValue;
                                    }
                                } catch (AnnotatedException unused) {
                                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.policyInhibitExtError"), this.certPath, size);
                                }
                            } catch (AnnotatedException unused2) {
                                throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.policyConstExtError"), this.certPath, size);
                            }
                        } catch (AnnotatedException e5) {
                            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.policyMapExtError"), e5, this.certPath, size);
                        }
                    } else {
                        hashSet = hashSet4;
                        str2 = str;
                        i5 = i3;
                        i12 = i4;
                    }
                    size--;
                    x509Certificate = x509Certificate2;
                    str4 = str2;
                    hashSet4 = hashSet;
                    i9 = i14;
                    i11 = i5;
                    initialPolicies = initialPolicies;
                } catch (AnnotatedException e6) {
                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, str4), e6, this.certPath, size);
                }
            }
            Set<String> set = initialPolicies;
            int i18 = i9;
            PKIXPolicyNode pKIXPolicyNode7 = pKIXPolicyNodePrepareNextCertB2;
            if (!isSelfIssued(x509Certificate) && i2 > 0) {
                i2--;
            }
            try {
                ASN1Sequence aSN1Sequence9 = (ASN1Sequence) getExtensionValue(x509Certificate, POLICY_CONSTRAINTS);
                if (aSN1Sequence9 != null) {
                    Enumeration objects4 = aSN1Sequence9.getObjects();
                    int i19 = i2;
                    while (objects4.hasMoreElements()) {
                        ASN1TaggedObject aSN1TaggedObject2 = (ASN1TaggedObject) objects4.nextElement();
                        if (aSN1TaggedObject2.getTagNo() == 0 && DERInteger.getInstance(aSN1TaggedObject2).getValue().intValue() == 0) {
                            i19 = 0;
                        }
                    }
                    i2 = i19;
                }
                if (pKIXPolicyNode7 == null) {
                    if (this.pkixParams.isExplicitPolicyRequired()) {
                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.explicitPolicy"), this.certPath, size);
                    }
                    pKIXPolicyNodeRemovePolicyNode = null;
                } else if (!isAnyPolicy(set)) {
                    HashSet<PKIXPolicyNode> hashSet10 = new HashSet();
                    for (int i20 = 0; i20 < i18; i20++) {
                        ArrayList arrayList4 = arrayListArr[i20];
                        for (int i21 = 0; i21 < arrayList4.size(); i21++) {
                            PKIXPolicyNode pKIXPolicyNode8 = (PKIXPolicyNode) arrayList4.get(i21);
                            if ("2.5.29.32.0".equals(pKIXPolicyNode8.getValidPolicy())) {
                                Iterator children2 = pKIXPolicyNode8.getChildren();
                                while (children2.hasNext()) {
                                    PKIXPolicyNode pKIXPolicyNode9 = (PKIXPolicyNode) children2.next();
                                    if (!"2.5.29.32.0".equals(pKIXPolicyNode9.getValidPolicy())) {
                                        hashSet10.add(pKIXPolicyNode9);
                                    }
                                }
                            }
                        }
                    }
                    pKIXPolicyNodeRemovePolicyNode = pKIXPolicyNode7;
                    for (PKIXPolicyNode pKIXPolicyNode10 : hashSet10) {
                        Set<String> set2 = set;
                        if (!set2.contains(pKIXPolicyNode10.getValidPolicy())) {
                            pKIXPolicyNodeRemovePolicyNode = removePolicyNode(pKIXPolicyNodeRemovePolicyNode, arrayListArr, pKIXPolicyNode10);
                        }
                        set = set2;
                    }
                    if (pKIXPolicyNodeRemovePolicyNode != null) {
                        for (int i22 = this.n - 1; i22 >= 0; i22--) {
                            ArrayList arrayList5 = arrayListArr[i22];
                            for (int i23 = 0; i23 < arrayList5.size(); i23++) {
                                PKIXPolicyNode pKIXPolicyNode11 = (PKIXPolicyNode) arrayList5.get(i23);
                                if (!pKIXPolicyNode11.hasChildren()) {
                                    pKIXPolicyNodeRemovePolicyNode = removePolicyNode(pKIXPolicyNodeRemovePolicyNode, arrayListArr, pKIXPolicyNode11);
                                }
                            }
                        }
                    }
                } else if (!this.pkixParams.isExplicitPolicyRequired()) {
                    pKIXPolicyNodeRemovePolicyNode = pKIXPolicyNode7;
                } else {
                    if (hashSet4.isEmpty()) {
                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.explicitPolicy"), this.certPath, size);
                    }
                    HashSet hashSet11 = new HashSet();
                    int i24 = 0;
                    while (true) {
                        int i25 = i18;
                        if (i24 >= i25) {
                            break;
                        }
                        ArrayList arrayList6 = arrayListArr[i24];
                        for (int i26 = 0; i26 < arrayList6.size(); i26++) {
                            PKIXPolicyNode pKIXPolicyNode12 = (PKIXPolicyNode) arrayList6.get(i26);
                            if ("2.5.29.32.0".equals(pKIXPolicyNode12.getValidPolicy())) {
                                Iterator children3 = pKIXPolicyNode12.getChildren();
                                while (children3.hasNext()) {
                                    hashSet11.add(children3.next());
                                }
                            }
                        }
                        i24++;
                        i18 = i25;
                    }
                    Iterator it2 = hashSet11.iterator();
                    while (it2.hasNext()) {
                        hashSet4.contains(((PKIXPolicyNode) it2.next()).getValidPolicy());
                    }
                    if (pKIXPolicyNode7 != null) {
                        pKIXPolicyNodeRemovePolicyNode = pKIXPolicyNode7;
                        for (int i27 = this.n - 1; i27 >= 0; i27--) {
                            ArrayList arrayList7 = arrayListArr[i27];
                            for (int i28 = 0; i28 < arrayList7.size(); i28++) {
                                PKIXPolicyNode pKIXPolicyNode13 = (PKIXPolicyNode) arrayList7.get(i28);
                                if (!pKIXPolicyNode13.hasChildren()) {
                                    pKIXPolicyNodeRemovePolicyNode = removePolicyNode(pKIXPolicyNodeRemovePolicyNode, arrayListArr, pKIXPolicyNode13);
                                }
                            }
                        }
                    } else {
                        pKIXPolicyNodeRemovePolicyNode = pKIXPolicyNode7;
                    }
                }
                if (i2 <= 0 && pKIXPolicyNodeRemovePolicyNode == null) {
                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.invalidPolicy"));
                }
            } catch (AnnotatedException unused3) {
                throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.policyConstExtError"), this.certPath, size);
            }
        } catch (CertPathReviewerException e7) {
            addError(e7.getErrorMessage(), e7.getIndex());
        }
    }

    /* JADX WARN: Code duplicated, block: B:103:0x02bf A[LOOP:1: B:101:0x02b9->B:103:0x02bf, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:107:0x02e2 A[LOOP:2: B:105:0x02dc->B:107:0x02e2, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:117:0x0325  */
    /* JADX WARN: Code duplicated, block: B:124:0x0359  */
    /* JADX WARN: Code duplicated, block: B:126:0x035d  */
    /* JADX WARN: Code duplicated, block: B:128:0x0364  */
    /* JADX WARN: Code duplicated, block: B:129:0x036d  */
    /* JADX WARN: Code duplicated, block: B:132:0x037a A[Catch: AnnotatedException -> 0x0394, TryCatch #19 {AnnotatedException -> 0x0394, blocks: (B:130:0x036e, B:132:0x037a, B:134:0x0380, B:135:0x0389), top: B:180:0x036e }] */
    /* JADX WARN: Code duplicated, block: B:134:0x0380 A[Catch: AnnotatedException -> 0x0394, TryCatch #19 {AnnotatedException -> 0x0394, blocks: (B:130:0x036e, B:132:0x037a, B:134:0x0380, B:135:0x0389), top: B:180:0x036e }] */
    /* JADX WARN: Code duplicated, block: B:135:0x0389 A[Catch: AnnotatedException -> 0x0394, TRY_LEAVE, TryCatch #19 {AnnotatedException -> 0x0394, blocks: (B:130:0x036e, B:132:0x037a, B:134:0x0380, B:135:0x0389), top: B:180:0x036e }] */
    /* JADX WARN: Code duplicated, block: B:143:0x03b3  */
    /* JADX WARN: Code duplicated, block: B:157:0x027b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:168:0x0185 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:172:0x01b3 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:174:0x01f5 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:31:0x00f9  */
    /* JADX WARN: Code duplicated, block: B:33:0x00ff A[Catch: IllegalArgumentException -> 0x010e, TRY_ENTER, TryCatch #2 {IllegalArgumentException -> 0x010e, blocks: (B:33:0x00ff, B:34:0x0104), top: B:160:0x00fd }] */
    /* JADX WARN: Code duplicated, block: B:34:0x0104 A[Catch: IllegalArgumentException -> 0x010e, TRY_LEAVE, TryCatch #2 {IllegalArgumentException -> 0x010e, blocks: (B:33:0x00ff, B:34:0x0104), top: B:160:0x00fd }] */
    /* JADX WARN: Code duplicated, block: B:43:0x013d  */
    /* JADX WARN: Code duplicated, block: B:45:0x0140  */
    /* JADX WARN: Code duplicated, block: B:47:0x0146  */
    /* JADX WARN: Code duplicated, block: B:48:0x014b  */
    /* JADX WARN: Code duplicated, block: B:52:0x0165  */
    /* JADX WARN: Code duplicated, block: B:55:0x0174  */
    /* JADX WARN: Code duplicated, block: B:63:0x01ad  */
    /* JADX WARN: Code duplicated, block: B:69:0x01e2  */
    /* JADX WARN: Code duplicated, block: B:73:0x0203 A[Catch: IOException -> 0x023d, TryCatch #16 {IOException -> 0x023d, blocks: (B:71:0x01f5, B:73:0x0203, B:75:0x020f), top: B:174:0x01f5 }] */
    /* JADX WARN: Code duplicated, block: B:75:0x020f A[Catch: IOException -> 0x023d, TRY_LEAVE, TryCatch #16 {IOException -> 0x023d, blocks: (B:71:0x01f5, B:73:0x0203, B:75:0x020f), top: B:174:0x01f5 }] */
    /* JADX WARN: Code duplicated, block: B:79:0x023d  */
    /* JADX WARN: Code duplicated, block: B:90:0x0283 A[Catch: AnnotatedException -> 0x028a, TRY_LEAVE, TryCatch #1 {AnnotatedException -> 0x028a, blocks: (B:88:0x027b, B:90:0x0283), top: B:157:0x027b }] */
    /* JADX WARN: Code duplicated, block: B:92:0x0288  */
    /* JADX WARN: Code duplicated, block: B:96:0x029d A[Catch: AnnotatedException -> 0x02a2, TRY_LEAVE, TryCatch #5 {AnnotatedException -> 0x02a2, blocks: (B:94:0x0295, B:96:0x029d), top: B:162:0x0295 }] */
    /* JADX WARN: Code duplicated, block: B:99:0x02ac  */
    private void checkSignatures() {
        TrustAnchor trustAnchor;
        TrustAnchor trustAnchor2;
        X500Principal x500Principal;
        X509Certificate trustedCert;
        PublicKey cAPublicKey;
        X509Certificate x509Certificate;
        X500Principal subjectX500Principal;
        PublicKey nextWorkingKey;
        int size;
        int i;
        X509Certificate x509Certificate2;
        ErrorBundle errorBundle;
        ErrorBundle errorBundle2;
        CRLDistPoint cRLDistPoint;
        AuthorityInformationAccess authorityInformationAccess;
        Iterator it;
        Iterator it2;
        X509Certificate x509Certificate3;
        int i2;
        int i3;
        PublicKey publicKey;
        X500Principal x500Principal2;
        TrustAnchor trustAnchor3;
        DERObject extensionValue;
        DERObject extensionValue2;
        boolean z;
        boolean[] keyUsage;
        BasicConstraints basicConstraints;
        ErrorBundle errorBundle3;
        byte[] extensionValue3;
        AuthorityKeyIdentifier authorityKeyIdentifier;
        GeneralNames authorityCertIssuer;
        GeneralName generalName;
        BigInteger authorityCertSerialNumber;
        X509Certificate trustedCert2;
        boolean[] keyUsage2;
        addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.certPathValidDate", new Object[]{new TrustedInput(this.validDate), new TrustedInput(new Date())}));
        char c = 2;
        char c2 = 0;
        try {
            List list = this.certs;
            X509Certificate x509Certificate4 = (X509Certificate) list.get(list.size() - 1);
            Collection trustAnchors = getTrustAnchors(x509Certificate4, this.pkixParams.getTrustAnchors());
            if (trustAnchors.size() <= 1) {
                if (trustAnchors.isEmpty()) {
                    addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noTrustAnchorFound", new Object[]{new UntrustedInput(x509Certificate4.getIssuerX500Principal()), new Integer(this.pkixParams.getTrustAnchors().size())}));
                } else {
                    trustAnchor = (TrustAnchor) trustAnchors.iterator().next();
                    try {
                        try {
                            CertPathValidatorUtilities.verifyX509Certificate(x509Certificate4, trustAnchor.getTrustedCert() != null ? trustAnchor.getTrustedCert().getPublicKey() : trustAnchor.getCAPublicKey(), this.pkixParams.getSigProvider());
                        } catch (SignatureException unused) {
                            addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.trustButInvalidCert"));
                        } catch (Exception unused2) {
                        }
                    } catch (CertPathReviewerException e) {
                        e = e;
                        addError(e.getErrorMessage());
                    } catch (Throwable th) {
                        th = th;
                        addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.unknown", new Object[]{new UntrustedInput(th.getMessage()), new UntrustedInput(th)}));
                    }
                }
                trustAnchor2 = trustAnchor;
                if (trustAnchor2 != null) {
                    trustedCert2 = trustAnchor2.getTrustedCert();
                    try {
                        if (trustedCert2 != null) {
                            x500Principal = getSubjectPrincipal(trustedCert2);
                        } else {
                            x500Principal = new X500Principal(trustAnchor2.getCAName());
                        }
                    } catch (IllegalArgumentException unused3) {
                        addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.trustDNInvalid", new Object[]{new UntrustedInput(trustAnchor2.getCAName())}));
                        x500Principal = null;
                    }
                    if (trustedCert2 != null && (keyUsage2 = trustedCert2.getKeyUsage()) != null && !keyUsage2[5]) {
                        addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.trustKeyUsage"));
                    }
                } else {
                    x500Principal = null;
                }
                if (trustAnchor2 != null) {
                    trustedCert = trustAnchor2.getTrustedCert();
                    if (trustedCert != null) {
                        cAPublicKey = trustedCert.getPublicKey();
                    } else {
                        cAPublicKey = trustAnchor2.getCAPublicKey();
                    }
                    try {
                        AlgorithmIdentifier algorithmIdentifier = getAlgorithmIdentifier(cAPublicKey);
                        algorithmIdentifier.getObjectId();
                        algorithmIdentifier.getParameters();
                    } catch (CertPathValidatorException unused4) {
                        addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.trustPubKeyError"));
                    }
                } else {
                    trustedCert = null;
                    cAPublicKey = null;
                }
                x509Certificate = trustedCert;
                subjectX500Principal = x500Principal;
                nextWorkingKey = cAPublicKey;
                size = this.certs.size() - 1;
                while (size >= 0) {
                    i = this.n - size;
                    x509Certificate2 = (X509Certificate) this.certs.get(size);
                    if (nextWorkingKey != null) {
                        try {
                            CertPathValidatorUtilities.verifyX509Certificate(x509Certificate2, nextWorkingKey, this.pkixParams.getSigProvider());
                        } catch (GeneralSecurityException e2) {
                            errorBundle = new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.signatureNotVerified", new Object[]{e2.getMessage(), e2, e2.getClass().getName()});
                            addError(errorBundle, size);
                        }
                    } else if (isSelfIssued(x509Certificate2)) {
                        try {
                            CertPathValidatorUtilities.verifyX509Certificate(x509Certificate2, x509Certificate2.getPublicKey(), this.pkixParams.getSigProvider());
                            addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.rootKeyIsValidButNotATrustAnchor"), size);
                        } catch (GeneralSecurityException e3) {
                            errorBundle = new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.signatureNotVerified", new Object[]{e3.getMessage(), e3, e3.getClass().getName()});
                            addError(errorBundle, size);
                        }
                    } else {
                        errorBundle3 = new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.NoIssuerPublicKey");
                        extensionValue3 = x509Certificate2.getExtensionValue(X509Extensions.AuthorityKeyIdentifier.getId());
                        if (extensionValue3 != null) {
                            try {
                                authorityKeyIdentifier = AuthorityKeyIdentifier.getInstance(X509ExtensionUtil.fromExtensionValue(extensionValue3));
                                authorityCertIssuer = authorityKeyIdentifier.getAuthorityCertIssuer();
                                if (authorityCertIssuer != null) {
                                    generalName = authorityCertIssuer.getNames()[c2];
                                    authorityCertSerialNumber = authorityKeyIdentifier.getAuthorityCertSerialNumber();
                                    if (authorityCertSerialNumber != null) {
                                        Object[] objArr = new Object[7];
                                        objArr[c2] = new LocaleString(RESOURCE_NAME, "missingIssuer");
                                        objArr[1] = " \"";
                                        try {
                                            objArr[2] = generalName;
                                            objArr[3] = "\" ";
                                            objArr[4] = new LocaleString(RESOURCE_NAME, "missingSerial");
                                            objArr[5] = " ";
                                            objArr[6] = authorityCertSerialNumber;
                                            errorBundle3.setExtraArguments(objArr);
                                        } catch (IOException unused5) {
                                        }
                                    }
                                }
                            } catch (IOException unused6) {
                            }
                        }
                        addError(errorBundle3, size);
                    }
                    try {
                        x509Certificate2.checkValidity(this.validDate);
                    } catch (CertificateExpiredException unused7) {
                        errorBundle2 = new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.certificateExpired", new Object[]{new TrustedInput(x509Certificate2.getNotAfter())});
                        addError(errorBundle2, size);
                    } catch (CertificateNotYetValidException unused8) {
                        errorBundle2 = new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.certificateNotYetValid", new Object[]{new TrustedInput(x509Certificate2.getNotBefore())});
                        addError(errorBundle2, size);
                    }
                    if (this.pkixParams.isRevocationEnabled()) {
                        try {
                            extensionValue2 = getExtensionValue(x509Certificate2, CRL_DIST_POINTS);
                            if (extensionValue2 != null) {
                                cRLDistPoint = CRLDistPoint.getInstance(extensionValue2);
                            } else {
                                cRLDistPoint = null;
                            }
                        } catch (AnnotatedException unused9) {
                            addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlDistPtExtError"), size);
                        }
                        try {
                            extensionValue = getExtensionValue(x509Certificate2, AUTH_INFO_ACCESS);
                            if (extensionValue != null) {
                                authorityInformationAccess = AuthorityInformationAccess.getInstance(extensionValue);
                            } else {
                                authorityInformationAccess = null;
                            }
                        } catch (AnnotatedException unused10) {
                            addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlAuthInfoAccError"), size);
                        }
                        Vector cRLDistUrls = getCRLDistUrls(cRLDistPoint);
                        Vector oCSPUrls = getOCSPUrls(authorityInformationAccess);
                        it = cRLDistUrls.iterator();
                        while (it.hasNext()) {
                            addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlDistPoint", new Object[]{new UntrustedUrlInput(it.next())}), size);
                        }
                        it2 = oCSPUrls.iterator();
                        while (it2.hasNext()) {
                            addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.ocspLocation", new Object[]{new UntrustedUrlInput(it2.next())}), size);
                        }
                        try {
                            x509Certificate3 = x509Certificate2;
                            i3 = size;
                            publicKey = nextWorkingKey;
                            i2 = i;
                            x500Principal2 = subjectX500Principal;
                            trustAnchor3 = trustAnchor2;
                            try {
                                checkRevocation(this.pkixParams, x509Certificate2, this.validDate, x509Certificate, nextWorkingKey, cRLDistUrls, oCSPUrls, i3);
                            } catch (CertPathReviewerException e4) {
                                e = e4;
                                addError(e.getErrorMessage(), i3);
                            }
                        } catch (CertPathReviewerException e5) {
                            e = e5;
                            x509Certificate3 = x509Certificate2;
                            i2 = i;
                            i3 = size;
                            publicKey = nextWorkingKey;
                            x500Principal2 = subjectX500Principal;
                            trustAnchor3 = trustAnchor2;
                        }
                    } else {
                        x509Certificate3 = x509Certificate2;
                        i2 = i;
                        i3 = size;
                        publicKey = nextWorkingKey;
                        x500Principal2 = subjectX500Principal;
                        trustAnchor3 = trustAnchor2;
                    }
                    if (x500Principal2 != null && !x509Certificate3.getIssuerX500Principal().equals(x500Principal2)) {
                        addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.certWrongIssuer", new Object[]{x500Principal2.getName(), x509Certificate3.getIssuerX500Principal().getName()}), i3);
                    }
                    if (i2 != this.n) {
                        if (x509Certificate3 != null) {
                            z = true;
                            if (x509Certificate3.getVersion() == 1) {
                                addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noCACert"), i3);
                            }
                        } else {
                            z = true;
                        }
                        try {
                            basicConstraints = BasicConstraints.getInstance(getExtensionValue(x509Certificate3, BASIC_CONSTRAINTS));
                            if (basicConstraints != null) {
                                addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noBasicConstraints"), i3);
                            } else if (!basicConstraints.isCA()) {
                                addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noCACert"), i3);
                            }
                        } catch (AnnotatedException unused11) {
                            addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.errorProcesingBC"), i3);
                        }
                        keyUsage = x509Certificate3.getKeyUsage();
                        if (keyUsage != null && !keyUsage[5]) {
                            addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noCertSign"), i3);
                        }
                    } else {
                        z = true;
                    }
                    subjectX500Principal = x509Certificate3.getSubjectX500Principal();
                    try {
                        nextWorkingKey = getNextWorkingKey(this.certs, i3);
                        try {
                            AlgorithmIdentifier algorithmIdentifier2 = getAlgorithmIdentifier(nextWorkingKey);
                            algorithmIdentifier2.getObjectId();
                            algorithmIdentifier2.getParameters();
                        } catch (CertPathValidatorException unused12) {
                            addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.pubKeyError"), i3);
                        }
                    } catch (CertPathValidatorException unused13) {
                        nextWorkingKey = publicKey;
                    }
                    size = i3 - 1;
                    trustAnchor2 = trustAnchor3;
                    x509Certificate = x509Certificate3;
                    c = 2;
                    c2 = 0;
                }
                this.trustAnchor = trustAnchor2;
                this.subjectPublicKey = nextWorkingKey;
            }
            addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.conflictingTrustAnchors", new Object[]{new Integer(trustAnchors.size()), new UntrustedInput(x509Certificate4.getIssuerX500Principal())}));
            trustAnchor = null;
        } catch (CertPathReviewerException e6) {
            e = e6;
            trustAnchor = null;
        } catch (Throwable th2) {
            th = th2;
            trustAnchor = null;
        }
        trustAnchor2 = trustAnchor;
        if (trustAnchor2 != null) {
            trustedCert2 = trustAnchor2.getTrustedCert();
            if (trustedCert2 != null) {
                x500Principal = getSubjectPrincipal(trustedCert2);
            } else {
                x500Principal = new X500Principal(trustAnchor2.getCAName());
            }
            if (trustedCert2 != null) {
                addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.trustKeyUsage"));
            }
        } else {
            x500Principal = null;
        }
        if (trustAnchor2 != null) {
            trustedCert = trustAnchor2.getTrustedCert();
            if (trustedCert != null) {
                cAPublicKey = trustedCert.getPublicKey();
            } else {
                cAPublicKey = trustAnchor2.getCAPublicKey();
            }
            AlgorithmIdentifier algorithmIdentifier3 = getAlgorithmIdentifier(cAPublicKey);
            algorithmIdentifier3.getObjectId();
            algorithmIdentifier3.getParameters();
        } else {
            trustedCert = null;
            cAPublicKey = null;
        }
        x509Certificate = trustedCert;
        subjectX500Principal = x500Principal;
        nextWorkingKey = cAPublicKey;
        size = this.certs.size() - 1;
        while (size >= 0) {
            i = this.n - size;
            x509Certificate2 = (X509Certificate) this.certs.get(size);
            if (nextWorkingKey != null) {
                CertPathValidatorUtilities.verifyX509Certificate(x509Certificate2, nextWorkingKey, this.pkixParams.getSigProvider());
            } else if (isSelfIssued(x509Certificate2)) {
                CertPathValidatorUtilities.verifyX509Certificate(x509Certificate2, x509Certificate2.getPublicKey(), this.pkixParams.getSigProvider());
                addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.rootKeyIsValidButNotATrustAnchor"), size);
            } else {
                errorBundle3 = new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.NoIssuerPublicKey");
                extensionValue3 = x509Certificate2.getExtensionValue(X509Extensions.AuthorityKeyIdentifier.getId());
                if (extensionValue3 != null) {
                    authorityKeyIdentifier = AuthorityKeyIdentifier.getInstance(X509ExtensionUtil.fromExtensionValue(extensionValue3));
                    authorityCertIssuer = authorityKeyIdentifier.getAuthorityCertIssuer();
                    if (authorityCertIssuer != null) {
                        generalName = authorityCertIssuer.getNames()[c2];
                        authorityCertSerialNumber = authorityKeyIdentifier.getAuthorityCertSerialNumber();
                        if (authorityCertSerialNumber != null) {
                            Object[] objArr2 = new Object[7];
                            objArr2[c2] = new LocaleString(RESOURCE_NAME, "missingIssuer");
                            objArr2[1] = " \"";
                            objArr2[2] = generalName;
                            objArr2[3] = "\" ";
                            objArr2[4] = new LocaleString(RESOURCE_NAME, "missingSerial");
                            objArr2[5] = " ";
                            objArr2[6] = authorityCertSerialNumber;
                            errorBundle3.setExtraArguments(objArr2);
                        }
                    }
                }
                addError(errorBundle3, size);
            }
            x509Certificate2.checkValidity(this.validDate);
            if (this.pkixParams.isRevocationEnabled()) {
                extensionValue2 = getExtensionValue(x509Certificate2, CRL_DIST_POINTS);
                if (extensionValue2 != null) {
                    cRLDistPoint = CRLDistPoint.getInstance(extensionValue2);
                } else {
                    cRLDistPoint = null;
                }
                extensionValue = getExtensionValue(x509Certificate2, AUTH_INFO_ACCESS);
                if (extensionValue != null) {
                    authorityInformationAccess = AuthorityInformationAccess.getInstance(extensionValue);
                } else {
                    authorityInformationAccess = null;
                }
                Vector cRLDistUrls2 = getCRLDistUrls(cRLDistPoint);
                Vector oCSPUrls2 = getOCSPUrls(authorityInformationAccess);
                it = cRLDistUrls2.iterator();
                while (it.hasNext()) {
                    addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlDistPoint", new Object[]{new UntrustedUrlInput(it.next())}), size);
                }
                it2 = oCSPUrls2.iterator();
                while (it2.hasNext()) {
                    addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.ocspLocation", new Object[]{new UntrustedUrlInput(it2.next())}), size);
                }
                x509Certificate3 = x509Certificate2;
                i3 = size;
                publicKey = nextWorkingKey;
                i2 = i;
                x500Principal2 = subjectX500Principal;
                trustAnchor3 = trustAnchor2;
                checkRevocation(this.pkixParams, x509Certificate2, this.validDate, x509Certificate, nextWorkingKey, cRLDistUrls2, oCSPUrls2, i3);
            } else {
                x509Certificate3 = x509Certificate2;
                i2 = i;
                i3 = size;
                publicKey = nextWorkingKey;
                x500Principal2 = subjectX500Principal;
                trustAnchor3 = trustAnchor2;
            }
            if (x500Principal2 != null) {
                addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.certWrongIssuer", new Object[]{x500Principal2.getName(), x509Certificate3.getIssuerX500Principal().getName()}), i3);
            }
            if (i2 != this.n) {
                if (x509Certificate3 != null) {
                    z = true;
                    if (x509Certificate3.getVersion() == 1) {
                        addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noCACert"), i3);
                    }
                } else {
                    z = true;
                }
                basicConstraints = BasicConstraints.getInstance(getExtensionValue(x509Certificate3, BASIC_CONSTRAINTS));
                if (basicConstraints != null) {
                    addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noBasicConstraints"), i3);
                } else if (!basicConstraints.isCA()) {
                    addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noCACert"), i3);
                }
                keyUsage = x509Certificate3.getKeyUsage();
                if (keyUsage != null) {
                    addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noCertSign"), i3);
                }
            } else {
                z = true;
            }
            subjectX500Principal = x509Certificate3.getSubjectX500Principal();
            nextWorkingKey = getNextWorkingKey(this.certs, i3);
            AlgorithmIdentifier algorithmIdentifier4 = getAlgorithmIdentifier(nextWorkingKey);
            algorithmIdentifier4.getObjectId();
            algorithmIdentifier4.getParameters();
            size = i3 - 1;
            trustAnchor2 = trustAnchor3;
            x509Certificate = x509Certificate3;
            c = 2;
            c2 = 0;
        }
        this.trustAnchor = trustAnchor2;
        this.subjectPublicKey = nextWorkingKey;
    }

    private X509CRL getCRL(String str) throws CertPathReviewerException {
        try {
            URL url = new URL(str);
            if (!url.getProtocol().equals(OkHttpFactory.TYPE_HTTP) && !url.getProtocol().equals(OkHttpFactory.TYPE_HTTPS)) {
                return null;
            }
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == 200) {
                return (X509CRL) CertificateFactory.getInstance("X.509", "BC").generateCRL(httpURLConnection.getInputStream());
            }
            throw new Exception(httpURLConnection.getResponseMessage());
        } catch (Exception e) {
            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.loadCrlDistPointError", new Object[]{new UntrustedInput(str), e.getMessage(), e, e.getClass().getName()}));
        }
    }

    private boolean processQcStatements(X509Certificate x509Certificate, int i) {
        ErrorBundle errorBundle;
        try {
            ASN1Sequence aSN1Sequence = (ASN1Sequence) getExtensionValue(x509Certificate, QC_STATEMENT);
            boolean z = false;
            for (int i2 = 0; i2 < aSN1Sequence.size(); i2++) {
                QCStatement qCStatement = QCStatement.getInstance(aSN1Sequence.getObjectAt(i2));
                if (QCStatement.id_etsi_qcs_QcCompliance.equals(qCStatement.getStatementId())) {
                    errorBundle = new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.QcEuCompliance");
                } else {
                    if (!QCStatement.id_qcs_pkixQCSyntax_v1.equals(qCStatement.getStatementId())) {
                        if (QCStatement.id_etsi_qcs_QcSSCD.equals(qCStatement.getStatementId())) {
                            errorBundle = new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.QcSSCD");
                        } else if (QCStatement.id_etsi_qcs_LimiteValue.equals(qCStatement.getStatementId())) {
                            MonetaryValue monetaryValue = MonetaryValue.getInstance(qCStatement.getStatementInfo());
                            monetaryValue.getCurrency();
                            double dDoubleValue = monetaryValue.getAmount().doubleValue() * Math.pow(10.0d, monetaryValue.getExponent().doubleValue());
                            addNotification(monetaryValue.getCurrency().isAlphabetic() ? new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.QcLimitValueAlpha", new Object[]{monetaryValue.getCurrency().getAlphabetic(), new TrustedInput(new Double(dDoubleValue)), monetaryValue}) : new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.QcLimitValueNum", new Object[]{new Integer(monetaryValue.getCurrency().getNumeric()), new TrustedInput(new Double(dDoubleValue)), monetaryValue}), i);
                        } else {
                            addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.QcUnknownStatement", new Object[]{qCStatement.getStatementId(), new UntrustedInput(qCStatement)}), i);
                            z = true;
                        }
                    }
                }
                addNotification(errorBundle, i);
            }
            return true ^ z;
        } catch (AnnotatedException unused) {
            addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.QcStatementExtError"), i);
            return false;
        }
    }

    protected void addError(ErrorBundle errorBundle) {
        this.errors[0].add(errorBundle);
    }

    protected void addError(ErrorBundle errorBundle, int i) {
        if (i < -1 || i >= this.n) {
            throw new IndexOutOfBoundsException();
        }
        this.errors[i + 1].add(errorBundle);
    }

    protected void addNotification(ErrorBundle errorBundle) {
        this.notifications[0].add(errorBundle);
    }

    protected void addNotification(ErrorBundle errorBundle, int i) {
        if (i < -1 || i >= this.n) {
            throw new IndexOutOfBoundsException();
        }
        this.notifications[i + 1].add(errorBundle);
    }

    protected void checkCRLs(PKIXParameters pKIXParameters, X509Certificate x509Certificate, Date date, X509Certificate x509Certificate2, PublicKey publicKey, Vector vector, int i) throws CertPathReviewerException {
        Iterator it;
        X509CRL x509crl;
        X509CRL x509crl2;
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        String str;
        boolean[] keyUsage;
        X509CRL x509crl3;
        Iterator it2;
        X509CRLStoreSelector x509CRLStoreSelector = new X509CRLStoreSelector();
        try {
            x509CRLStoreSelector.addIssuerName(getEncodedIssuerPrincipal(x509Certificate).getEncoded());
            x509CRLStoreSelector.setCertificateChecking(x509Certificate);
            try {
                Collection collectionFindCRLs = findCRLs(x509CRLStoreSelector, pKIXParameters.getCertStores());
                it = collectionFindCRLs.iterator();
                if (collectionFindCRLs.isEmpty()) {
                    Iterator it3 = findCRLs(new X509CRLStoreSelector(), pKIXParameters.getCertStores()).iterator();
                    ArrayList arrayList = new ArrayList();
                    while (it3.hasNext()) {
                        arrayList.add(((X509CRL) it3.next()).getIssuerX500Principal());
                    }
                    addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noCrlInCertstore", new Object[]{new UntrustedInput(x509CRLStoreSelector.getIssuerNames()), new UntrustedInput(arrayList), new Integer(arrayList.size())}), i);
                }
                while (true) {
                    if (!it.hasNext()) {
                        x509crl2 = x509crl;
                        z = false;
                        break;
                    }
                    x509crl = (X509CRL) it.next();
                    if (x509crl.getNextUpdate() == null || new Date().before(x509crl.getNextUpdate())) {
                        addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.localValidCRL", new Object[]{new TrustedInput(x509crl.getThisUpdate()), new TrustedInput(x509crl.getNextUpdate())}), i);
                        x509crl2 = x509crl;
                        z = true;
                        break;
                    }
                    addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.localInvalidCRL", new Object[]{new TrustedInput(x509crl.getThisUpdate()), new TrustedInput(x509crl.getNextUpdate())}), i);
                }
            } catch (AnnotatedException e) {
                addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlExtractionError", new Object[]{e.getCause().getMessage(), e.getCause(), e.getCause().getClass().getName()}), i);
                it = new ArrayList().iterator();
            }
            x509crl = null;
            if (!z) {
                Iterator it4 = vector.iterator();
                z4 = z;
                while (true) {
                    if (!it4.hasNext()) {
                        z2 = true;
                        z3 = false;
                        break;
                    }
                    try {
                        String str2 = (String) it4.next();
                        X509CRL crl = getCRL(str2);
                        if (crl != null) {
                            if (x509Certificate.getIssuerX500Principal().equals(crl.getIssuerX500Principal())) {
                                x509crl3 = x509crl2;
                                if (crl.getNextUpdate() != null && !new Date().before(crl.getNextUpdate())) {
                                    Object[] objArr = new Object[3];
                                    it2 = it4;
                                    try {
                                        objArr[0] = new TrustedInput(crl.getThisUpdate());
                                        objArr[1] = new TrustedInput(crl.getNextUpdate());
                                        objArr[2] = new UntrustedUrlInput(str2);
                                        addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.onlineInvalidCRL", objArr), i);
                                    } catch (CertPathReviewerException e2) {
                                        e = e2;
                                        addNotification(e.getErrorMessage(), i);
                                    }
                                }
                                it2 = it4;
                                try {
                                    Object[] objArr2 = new Object[3];
                                    z3 = false;
                                    try {
                                        objArr2[0] = new TrustedInput(crl.getThisUpdate());
                                        z2 = true;
                                        try {
                                            objArr2[1] = new TrustedInput(crl.getNextUpdate());
                                            try {
                                                objArr2[2] = new UntrustedUrlInput(str2);
                                                addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.onlineValidCRL", objArr2), i);
                                                z4 = true;
                                                x509crl2 = crl;
                                                break;
                                            } catch (CertPathReviewerException e3) {
                                                e = e3;
                                                z4 = z2;
                                                addNotification(e.getErrorMessage(), i);
                                                it4 = it2;
                                                x509crl2 = x509crl3;
                                            }
                                        } catch (CertPathReviewerException e4) {
                                            e = e4;
                                            z4 = z2;
                                            addNotification(e.getErrorMessage(), i);
                                            it4 = it2;
                                            x509crl2 = x509crl3;
                                        }
                                    } catch (CertPathReviewerException e5) {
                                        e = e5;
                                        z2 = true;
                                    }
                                } catch (CertPathReviewerException e6) {
                                    e = e6;
                                    z2 = true;
                                }
                            } else {
                                x509crl3 = x509crl2;
                                try {
                                    addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.onlineCRLWrongCA", new Object[]{new UntrustedInput(crl.getIssuerX500Principal().getName()), new UntrustedInput(x509Certificate.getIssuerX500Principal().getName()), new UntrustedUrlInput(str2)}), i);
                                } catch (CertPathReviewerException e7) {
                                    e = e7;
                                    it2 = it4;
                                    addNotification(e.getErrorMessage(), i);
                                }
                            }
                            it4 = it2;
                            x509crl2 = x509crl3;
                        } else {
                            x509crl3 = x509crl2;
                        }
                        it2 = it4;
                    } catch (CertPathReviewerException e8) {
                        e = e8;
                        x509crl3 = x509crl2;
                    }
                    it4 = it2;
                    x509crl2 = x509crl3;
                }
            } else {
                z2 = true;
                z3 = false;
                z4 = z;
            }
            if (x509crl2 != null) {
                if (x509Certificate2 != null && (keyUsage = x509Certificate2.getKeyUsage()) != null && (keyUsage.length < 7 || !keyUsage[6])) {
                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noCrlSigningPermited"));
                }
                if (publicKey == null) {
                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlNoIssuerPublicKey"));
                }
                try {
                    x509crl2.verify(publicKey, "BC");
                    X509CRLEntry revokedCertificate = x509crl2.getRevokedCertificate(x509Certificate.getSerialNumber());
                    if (revokedCertificate != null) {
                        if (revokedCertificate.hasExtensions()) {
                            try {
                                DEREnumerated dEREnumerated = DEREnumerated.getInstance(getExtensionValue(revokedCertificate, X509Extensions.ReasonCode.getId()));
                                str = dEREnumerated != null ? crlReasons[dEREnumerated.getValue().intValue()] : crlReasons[7];
                            } catch (AnnotatedException e9) {
                                throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlReasonExtError"), e9);
                            }
                        } else {
                            str = null;
                        }
                        LocaleString localeString = new LocaleString(RESOURCE_NAME, str);
                        if (!date.before(revokedCertificate.getRevocationDate())) {
                            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.certRevoked", new Object[]{new TrustedInput(revokedCertificate.getRevocationDate()), localeString}));
                        }
                        addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.revokedAfterValidation", new Object[]{new TrustedInput(revokedCertificate.getRevocationDate()), localeString}), i);
                    } else {
                        addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.notRevoked"), i);
                    }
                    if (x509crl2.getNextUpdate() != null && x509crl2.getNextUpdate().before(new Date())) {
                        addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlUpdateAvailable", new Object[]{new TrustedInput(x509crl2.getNextUpdate())}), i);
                    }
                    try {
                        DERObject extensionValue = getExtensionValue(x509crl2, ISSUING_DISTRIBUTION_POINT);
                        try {
                            DERObject extensionValue2 = getExtensionValue(x509crl2, DELTA_CRL_INDICATOR);
                            if (extensionValue2 != null) {
                                X509CRLStoreSelector x509CRLStoreSelector2 = new X509CRLStoreSelector();
                                try {
                                    x509CRLStoreSelector2.addIssuerName(getIssuerPrincipal(x509crl2).getEncoded());
                                    x509CRLStoreSelector2.setMinCRLNumber(((DERInteger) extensionValue2).getPositiveValue());
                                    try {
                                        x509CRLStoreSelector2.setMaxCRLNumber(((DERInteger) getExtensionValue(x509crl2, CRL_NUMBER)).getPositiveValue().subtract(BigInteger.valueOf(1L)));
                                        try {
                                            Iterator it5 = findCRLs(x509CRLStoreSelector2, pKIXParameters.getCertStores()).iterator();
                                            while (true) {
                                                if (!it5.hasNext()) {
                                                    z2 = z3;
                                                    break;
                                                }
                                                try {
                                                    DERObject extensionValue3 = getExtensionValue((X509CRL) it5.next(), ISSUING_DISTRIBUTION_POINT);
                                                    if (extensionValue == null) {
                                                        if (extensionValue3 == null) {
                                                            break;
                                                        }
                                                    } else if (extensionValue.equals(extensionValue3)) {
                                                        break;
                                                    }
                                                } catch (AnnotatedException e10) {
                                                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.distrPtExtError"), e10);
                                                }
                                            }
                                            if (!z2) {
                                                throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noBaseCRL"));
                                            }
                                        } catch (AnnotatedException e11) {
                                            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlExtractionError"), e11);
                                        }
                                    } catch (AnnotatedException e12) {
                                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlNbrExtError"), e12);
                                    }
                                } catch (IOException e13) {
                                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlIssuerException"), e13);
                                }
                            }
                            if (extensionValue != null) {
                                IssuingDistributionPoint issuingDistributionPoint = IssuingDistributionPoint.getInstance(extensionValue);
                                try {
                                    BasicConstraints basicConstraints = BasicConstraints.getInstance(getExtensionValue(x509Certificate, BASIC_CONSTRAINTS));
                                    if (issuingDistributionPoint.onlyContainsUserCerts() && basicConstraints != null && basicConstraints.isCA()) {
                                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlOnlyUserCert"));
                                    }
                                    if (issuingDistributionPoint.onlyContainsCACerts() && (basicConstraints == null || !basicConstraints.isCA())) {
                                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlOnlyCaCert"));
                                    }
                                    if (issuingDistributionPoint.onlyContainsAttributeCerts()) {
                                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlOnlyAttrCert"));
                                    }
                                } catch (AnnotatedException e14) {
                                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlBCExtError"), e14);
                                }
                            }
                        } catch (AnnotatedException unused) {
                            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.deltaCrlExtError"));
                        }
                    } catch (AnnotatedException unused2) {
                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.distrPtExtError"));
                    }
                } catch (Exception e15) {
                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlVerifyFailed"), e15);
                }
            }
            if (!z4) {
                throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noValidCrlFound"));
            }
        } catch (IOException e16) {
            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlIssuerException"), e16);
        }
    }

    protected void checkRevocation(PKIXParameters pKIXParameters, X509Certificate x509Certificate, Date date, X509Certificate x509Certificate2, PublicKey publicKey, Vector vector, Vector vector2, int i) throws CertPathReviewerException {
        checkCRLs(pKIXParameters, x509Certificate, date, x509Certificate2, publicKey, vector, i);
    }

    protected void doChecks() {
        if (!this.initialized) {
            throw new IllegalStateException("Object not initialized. Call init() first.");
        }
        if (this.notifications != null) {
            return;
        }
        int i = this.n;
        this.notifications = new List[i + 1];
        this.errors = new List[i + 1];
        int i2 = 0;
        while (true) {
            List[] listArr = this.notifications;
            if (i2 >= listArr.length) {
                checkSignatures();
                checkNameConstraints();
                checkPathLength();
                checkPolicy();
                checkCriticalExtensions();
                return;
            }
            listArr[i2] = new ArrayList();
            this.errors[i2] = new ArrayList();
            i2++;
        }
    }

    protected Vector getCRLDistUrls(CRLDistPoint cRLDistPoint) {
        Vector vector = new Vector();
        if (cRLDistPoint != null) {
            for (DistributionPoint distributionPoint : cRLDistPoint.getDistributionPoints()) {
                DistributionPointName distributionPoint2 = distributionPoint.getDistributionPoint();
                if (distributionPoint2.getType() == 0) {
                    GeneralName[] names = GeneralNames.getInstance(distributionPoint2.getName()).getNames();
                    for (int i = 0; i < names.length; i++) {
                        if (names[i].getTagNo() == 6) {
                            vector.add(((DERIA5String) names[i].getName()).getString());
                        }
                    }
                }
            }
        }
        return vector;
    }

    public CertPath getCertPath() {
        return this.certPath;
    }

    public int getCertPathSize() {
        return this.n;
    }

    public List getErrors(int i) {
        doChecks();
        return this.errors[i + 1];
    }

    public List[] getErrors() {
        doChecks();
        return this.errors;
    }

    public List getNotifications(int i) {
        doChecks();
        return this.notifications[i + 1];
    }

    public List[] getNotifications() {
        doChecks();
        return this.notifications;
    }

    protected Vector getOCSPUrls(AuthorityInformationAccess authorityInformationAccess) {
        Vector vector = new Vector();
        if (authorityInformationAccess != null) {
            AccessDescription[] accessDescriptions = authorityInformationAccess.getAccessDescriptions();
            for (int i = 0; i < accessDescriptions.length; i++) {
                if (accessDescriptions[i].getAccessMethod().equals(AccessDescription.id_ad_ocsp)) {
                    GeneralName accessLocation = accessDescriptions[i].getAccessLocation();
                    if (accessLocation.getTagNo() == 6) {
                        vector.add(((DERIA5String) accessLocation.getName()).getString());
                    }
                }
            }
        }
        return vector;
    }

    public PolicyNode getPolicyTree() {
        doChecks();
        return this.policyTree;
    }

    public PublicKey getSubjectPublicKey() {
        doChecks();
        return this.subjectPublicKey;
    }

    public TrustAnchor getTrustAnchor() {
        doChecks();
        return this.trustAnchor;
    }

    protected Collection getTrustAnchors(X509Certificate x509Certificate, Set set) throws CertPathReviewerException {
        ArrayList arrayList = new ArrayList();
        Iterator it = set.iterator();
        X509CertSelector x509CertSelector = new X509CertSelector();
        try {
            x509CertSelector.setSubject(getEncodedIssuerPrincipal(x509Certificate).getEncoded());
            byte[] extensionValue = x509Certificate.getExtensionValue(X509Extensions.AuthorityKeyIdentifier.getId());
            if (extensionValue != null) {
                AuthorityKeyIdentifier authorityKeyIdentifier = AuthorityKeyIdentifier.getInstance(ASN1Object.fromByteArray(((ASN1OctetString) ASN1Object.fromByteArray(extensionValue)).getOctets()));
                x509CertSelector.setSerialNumber(authorityKeyIdentifier.getAuthorityCertSerialNumber());
                byte[] keyIdentifier = authorityKeyIdentifier.getKeyIdentifier();
                if (keyIdentifier != null) {
                    x509CertSelector.setSubjectKeyIdentifier(new DEROctetString(keyIdentifier).getEncoded());
                }
            }
            while (it.hasNext()) {
                TrustAnchor trustAnchor = (TrustAnchor) it.next();
                if (trustAnchor.getTrustedCert() != null) {
                    if (x509CertSelector.match(trustAnchor.getTrustedCert())) {
                        arrayList.add(trustAnchor);
                    }
                } else if (trustAnchor.getCAName() != null && trustAnchor.getCAPublicKey() != null && getEncodedIssuerPrincipal(x509Certificate).equals(new X500Principal(trustAnchor.getCAName()))) {
                    arrayList.add(trustAnchor);
                }
            }
            return arrayList;
        } catch (IOException unused) {
            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.trustAnchorIssuerError"));
        }
    }

    public void init(CertPath certPath, PKIXParameters pKIXParameters) throws CertPathReviewerException {
        if (this.initialized) {
            throw new IllegalStateException("object is already initialized!");
        }
        this.initialized = true;
        if (certPath == null) {
            throw new NullPointerException("certPath was null");
        }
        this.certPath = certPath;
        List<? extends Certificate> certificates = certPath.getCertificates();
        this.certs = certificates;
        this.n = certificates.size();
        if (this.certs.isEmpty()) {
            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.emptyCertPath"));
        }
        PKIXParameters pKIXParameters2 = (PKIXParameters) pKIXParameters.clone();
        this.pkixParams = pKIXParameters2;
        this.validDate = getValidDate(pKIXParameters2);
        this.notifications = null;
        this.errors = null;
        this.trustAnchor = null;
        this.subjectPublicKey = null;
        this.policyTree = null;
    }

    public boolean isValidCertPath() {
        doChecks();
        int i = 0;
        while (true) {
            List[] listArr = this.errors;
            if (i >= listArr.length) {
                return true;
            }
            if (!listArr[i].isEmpty()) {
                return false;
            }
            i++;
        }
    }
}
