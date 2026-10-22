package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.Hashtable;
import kotlin.UByte;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.X509CertificateStructure;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.agreement.DHBasicAgreement;
import org.bouncycastle.crypto.agreement.srp.SRP6Client;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.RSABlindedEngine;
import org.bouncycastle.crypto.generators.DHBasicKeyPairGenerator;
import org.bouncycastle.crypto.io.SignerInputStream;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DHKeyGenerationParameters;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.params.DHPublicKeyParameters;
import org.bouncycastle.crypto.params.DSAPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.prng.ThreadedSeedGenerator;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.util.BigIntegers;

/* JADX INFO: loaded from: classes2.dex */
public class TlsProtocolHandler {
    protected static final short AL_fatal = 2;
    protected static final short AL_warning = 1;
    protected static final short AP_access_denied = 49;
    protected static final short AP_bad_certificate = 42;
    protected static final short AP_bad_record_mac = 20;
    protected static final short AP_certificate_expired = 45;
    protected static final short AP_certificate_revoked = 44;
    protected static final short AP_certificate_unknown = 46;
    protected static final short AP_close_notify = 0;
    protected static final short AP_decode_error = 50;
    protected static final short AP_decompression_failure = 30;
    protected static final short AP_decrypt_error = 51;
    protected static final short AP_decryption_failed = 21;
    protected static final short AP_export_restriction = 60;
    protected static final short AP_handshake_failure = 40;
    protected static final short AP_illegal_parameter = 47;
    protected static final short AP_insufficient_security = 71;
    protected static final short AP_internal_error = 80;
    protected static final short AP_no_renegotiation = 100;
    protected static final short AP_protocol_version = 70;
    protected static final short AP_record_overflow = 22;
    protected static final short AP_unexpected_message = 10;
    protected static final short AP_unknown_ca = 48;
    protected static final short AP_unsupported_certificate = 43;
    protected static final short AP_user_canceled = 90;
    private static final short CS_CERTIFICATE_REQUEST_RECEIVED = 5;
    private static final short CS_CLIENT_CHANGE_CIPHER_SPEC_SEND = 9;
    private static final short CS_CLIENT_FINISHED_SEND = 10;
    private static final short CS_CLIENT_HELLO_SEND = 1;
    private static final short CS_CLIENT_KEY_EXCHANGE_SEND = 7;
    private static final short CS_CLIENT_VERIFICATION_SEND = 8;
    private static final short CS_DONE = 12;
    private static final short CS_SERVER_CERTIFICATE_RECEIVED = 3;
    private static final short CS_SERVER_CHANGE_CIPHER_SPEC_RECEIVED = 11;
    private static final short CS_SERVER_HELLO_DONE_RECEIVED = 6;
    private static final short CS_SERVER_HELLO_RECEIVED = 2;
    private static final short CS_SERVER_KEY_EXCHANGE_RECEIVED = 4;
    private static final short HP_CERTIFICATE = 11;
    private static final short HP_CERTIFICATE_REQUEST = 13;
    private static final short HP_CERTIFICATE_VERIFY = 15;
    private static final short HP_CLIENT_HELLO = 1;
    private static final short HP_CLIENT_KEY_EXCHANGE = 16;
    private static final short HP_FINISHED = 20;
    private static final short HP_HELLO_REQUEST = 0;
    private static final short HP_SERVER_HELLO = 2;
    private static final short HP_SERVER_HELLO_DONE = 14;
    private static final short HP_SERVER_KEY_EXCHANGE = 12;
    private static final short RL_ALERT = 21;
    private static final short RL_APPLICATION_DATA = 23;
    private static final short RL_CHANGE_CIPHER_SPEC = 20;
    private static final short RL_HANDSHAKE = 22;
    private static final String TLS_ERROR_MESSAGE = "Internal TLS error, this could be an attack";
    private BigInteger SRP_A;
    private byte[] SRP_identity;
    private byte[] SRP_password;
    private BigInteger Yc;
    private byte[] clientRandom;
    private short connection_state;
    private boolean extendedClientHello;
    private byte[] ms;
    private byte[] pms;
    private SecureRandom random;
    private RecordStream rs;
    private byte[] serverRandom;
    private static final BigInteger ONE = BigInteger.valueOf(1);
    private static final BigInteger TWO = BigInteger.valueOf(2);
    private static final byte[] emptybuf = new byte[0];
    private ByteQueue applicationDataQueue = new ByteQueue();
    private ByteQueue changeCipherSpecQueue = new ByteQueue();
    private ByteQueue alertQueue = new ByteQueue();
    private ByteQueue handshakeQueue = new ByteQueue();
    private AsymmetricKeyParameter serverPublicKey = null;
    private TlsInputStream tlsInputStream = null;
    private TlsOuputStream tlsOutputStream = null;
    private boolean closed = false;
    private boolean failedWithError = false;
    private boolean appDataReady = false;
    private TlsCipherSuite chosenCipherSuite = null;
    private CertificateVerifyer verifyer = null;

    public TlsProtocolHandler(InputStream inputStream, OutputStream outputStream) {
        ThreadedSeedGenerator threadedSeedGenerator = new ThreadedSeedGenerator();
        SecureRandom secureRandom = new SecureRandom();
        this.random = secureRandom;
        secureRandom.setSeed(threadedSeedGenerator.generateSeed(20, true));
        this.rs = new RecordStream(this, inputStream, outputStream);
    }

    public TlsProtocolHandler(InputStream inputStream, OutputStream outputStream, SecureRandom secureRandom) {
        this.random = secureRandom;
        this.rs = new RecordStream(this, inputStream, outputStream);
    }

    private void processAlert() throws IOException {
        while (this.alertQueue.size() >= 2) {
            byte[] bArr = new byte[2];
            this.alertQueue.read(bArr, 0, 2, 0);
            this.alertQueue.removeData(2);
            short s = bArr[0];
            short s2 = bArr[1];
            if (s == 2) {
                this.failedWithError = true;
                this.closed = true;
                try {
                    this.rs.close();
                } catch (Exception unused) {
                }
                throw new IOException(TLS_ERROR_MESSAGE);
            }
            if (s2 == 0) {
                failWithError((short) 1, (short) 0);
            }
        }
    }

    private void processApplicationData() {
    }

    private void processChangeCipherSpec() throws IOException {
        while (this.changeCipherSpecQueue.size() > 0) {
            byte[] bArr = new byte[1];
            this.changeCipherSpecQueue.read(bArr, 0, 1, 0);
            this.changeCipherSpecQueue.removeData(1);
            if (bArr[0] != 1) {
                failWithError((short) 2, (short) 10);
            } else if (this.connection_state == 10) {
                RecordStream recordStream = this.rs;
                recordStream.readSuite = recordStream.writeSuite;
                this.connection_state = (short) 11;
            } else {
                failWithError((short) 2, AP_handshake_failure);
            }
        }
    }

    private void processDHEKeyExchange(ByteArrayInputStream byteArrayInputStream, Signer signer) throws IOException {
        InputStream signerInputStream;
        if (signer != null) {
            signer.init(false, this.serverPublicKey);
            byte[] bArr = this.clientRandom;
            signer.update(bArr, 0, bArr.length);
            byte[] bArr2 = this.serverRandom;
            signer.update(bArr2, 0, bArr2.length);
            signerInputStream = new SignerInputStream(byteArrayInputStream, signer);
        } else {
            signerInputStream = byteArrayInputStream;
        }
        byte[] opaque16 = TlsUtils.readOpaque16(signerInputStream);
        byte[] opaque17 = TlsUtils.readOpaque16(signerInputStream);
        byte[] opaque18 = TlsUtils.readOpaque16(signerInputStream);
        if (signer != null && !signer.verifySignature(TlsUtils.readOpaque16(byteArrayInputStream))) {
            failWithError((short) 2, AP_bad_certificate);
        }
        assertEmpty(byteArrayInputStream);
        BigInteger bigInteger = new BigInteger(1, opaque16);
        BigInteger bigInteger2 = new BigInteger(1, opaque17);
        BigInteger bigInteger3 = new BigInteger(1, opaque18);
        if (!bigInteger.isProbablePrime(10)) {
            failWithError((short) 2, AP_illegal_parameter);
        }
        BigInteger bigInteger4 = TWO;
        if (bigInteger2.compareTo(bigInteger4) < 0 || bigInteger2.compareTo(bigInteger.subtract(bigInteger4)) > 0) {
            failWithError((short) 2, AP_illegal_parameter);
        }
        if (bigInteger3.compareTo(bigInteger4) < 0 || bigInteger3.compareTo(bigInteger.subtract(ONE)) > 0) {
            failWithError((short) 2, AP_illegal_parameter);
        }
        DHParameters dHParameters = new DHParameters(bigInteger, bigInteger2);
        DHBasicKeyPairGenerator dHBasicKeyPairGenerator = new DHBasicKeyPairGenerator();
        dHBasicKeyPairGenerator.init(new DHKeyGenerationParameters(this.random, dHParameters));
        AsymmetricCipherKeyPair asymmetricCipherKeyPairGenerateKeyPair = dHBasicKeyPairGenerator.generateKeyPair();
        this.Yc = ((DHPublicKeyParameters) asymmetricCipherKeyPairGenerateKeyPair.getPublic()).getY();
        DHBasicAgreement dHBasicAgreement = new DHBasicAgreement();
        dHBasicAgreement.init(asymmetricCipherKeyPairGenerateKeyPair.getPrivate());
        this.pms = BigIntegers.asUnsignedByteArray(dHBasicAgreement.calculateAgreement(new DHPublicKeyParameters(bigInteger3, dHParameters)));
    }

    /* JADX WARN: Code duplicated, block: B:107:0x0258  */
    /* JADX WARN: Code duplicated, block: B:112:0x0268  */
    /* JADX WARN: Code duplicated, block: B:58:0x019f  */
    /* JADX WARN: Code duplicated, block: B:97:0x0234  */
    /* JADX WARN: Code duplicated, block: B:99:0x023a  */
    private void processHandshake() throws IOException {
        int i;
        int i2;
        Signer tlsDSSSigner;
        Signer tlsRSASigner;
        do {
            i = 0;
            if (this.handshakeQueue.size() >= 4) {
                byte[] bArr = new byte[4];
                this.handshakeQueue.read(bArr, 0, 4, 0);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
                short uint8 = TlsUtils.readUint8(byteArrayInputStream);
                int uint24 = TlsUtils.readUint24(byteArrayInputStream);
                int i3 = uint24 + 4;
                if (this.handshakeQueue.size() >= i3) {
                    byte[] bArr2 = new byte[uint24];
                    this.handshakeQueue.read(bArr2, 0, uint24, 4);
                    this.handshakeQueue.removeData(i3);
                    if (uint8 != 20) {
                        this.rs.hash1.update(bArr, 0, 4);
                        this.rs.hash2.update(bArr, 0, 4);
                        this.rs.hash1.update(bArr2, 0, uint24);
                        this.rs.hash2.update(bArr2, 0, uint24);
                    }
                    ByteArrayInputStream byteArrayInputStream2 = new ByteArrayInputStream(bArr2);
                    if (uint8 != 2) {
                        if (uint8 != 20) {
                            byte[] bArrProcessBlock = null;
                            switch (uint8) {
                                case 11:
                                    if (this.connection_state != 2) {
                                        failWithError((short) 2, (short) 10);
                                    } else {
                                        Certificate certificate = Certificate.parse(byteArrayInputStream2);
                                        assertEmpty(byteArrayInputStream2);
                                        X509CertificateStructure x509CertificateStructure = certificate.certs[0];
                                        try {
                                            this.serverPublicKey = PublicKeyFactory.createKey(x509CertificateStructure.getSubjectPublicKeyInfo());
                                        } catch (RuntimeException unused) {
                                            failWithError((short) 2, AP_unsupported_certificate);
                                        }
                                        if (this.serverPublicKey.isPrivate()) {
                                            failWithError((short) 2, AP_internal_error);
                                        }
                                        short keyExchangeAlgorithm = this.chosenCipherSuite.getKeyExchangeAlgorithm();
                                        if (keyExchangeAlgorithm != 1) {
                                            if (keyExchangeAlgorithm == 3) {
                                                if (!(this.serverPublicKey instanceof DSAPublicKeyParameters)) {
                                                    failWithError((short) 2, AP_certificate_unknown);
                                                }
                                            } else if (keyExchangeAlgorithm == 5 || keyExchangeAlgorithm == 11) {
                                                if (!(this.serverPublicKey instanceof RSAKeyParameters)) {
                                                    failWithError((short) 2, AP_certificate_unknown);
                                                }
                                                i2 = 128;
                                            } else if (keyExchangeAlgorithm != 12) {
                                                failWithError((short) 2, AP_unsupported_certificate);
                                            } else if (!(this.serverPublicKey instanceof DSAPublicKeyParameters)) {
                                                failWithError((short) 2, AP_certificate_unknown);
                                            }
                                            if (!this.verifyer.isValid(certificate.getCerts())) {
                                                failWithError((short) 2, AP_user_canceled);
                                            }
                                        } else {
                                            if (!(this.serverPublicKey instanceof RSAKeyParameters)) {
                                                failWithError((short) 2, AP_certificate_unknown);
                                            }
                                            i2 = 32;
                                        }
                                        validateKeyUsage(x509CertificateStructure, i2);
                                        if (!this.verifyer.isValid(certificate.getCerts())) {
                                            failWithError((short) 2, AP_user_canceled);
                                        }
                                    }
                                    this.connection_state = (short) 3;
                                    i = 1;
                                    break;
                                case 12:
                                    short s = this.connection_state;
                                    if (s != 2) {
                                        if (s != 3) {
                                            failWithError((short) 2, (short) 10);
                                        }
                                        this.connection_state = (short) 4;
                                        i = 1;
                                    } else if (this.chosenCipherSuite.getKeyExchangeAlgorithm() != 10) {
                                        failWithError((short) 2, (short) 10);
                                    }
                                    short keyExchangeAlgorithm2 = this.chosenCipherSuite.getKeyExchangeAlgorithm();
                                    if (keyExchangeAlgorithm2 != 3) {
                                        if (keyExchangeAlgorithm2 != 5) {
                                            switch (keyExchangeAlgorithm2) {
                                                case 10:
                                                    processSRPKeyExchange(byteArrayInputStream2, null);
                                                    break;
                                                case 11:
                                                    tlsRSASigner = new TlsRSASigner();
                                                    processSRPKeyExchange(byteArrayInputStream2, tlsRSASigner);
                                                    break;
                                                case 12:
                                                    tlsRSASigner = new TlsDSSSigner();
                                                    processSRPKeyExchange(byteArrayInputStream2, tlsRSASigner);
                                                    break;
                                                default:
                                                    failWithError((short) 2, (short) 10);
                                                    break;
                                            }
                                        } else {
                                            tlsDSSSigner = new TlsRSASigner();
                                        }
                                        this.connection_state = (short) 4;
                                        i = 1;
                                    } else {
                                        tlsDSSSigner = new TlsDSSSigner();
                                    }
                                    processDHEKeyExchange(byteArrayInputStream2, tlsDSSSigner);
                                    this.connection_state = (short) 4;
                                    i = 1;
                                    break;
                                case 13:
                                    short s2 = this.connection_state;
                                    if (s2 != 3) {
                                        if (s2 != 4) {
                                            failWithError((short) 2, (short) 10);
                                        }
                                        this.connection_state = (short) 5;
                                        i = 1;
                                    } else if (this.chosenCipherSuite.getKeyExchangeAlgorithm() != 1) {
                                        failWithError((short) 2, (short) 10);
                                    }
                                    TlsUtils.readOpaque8(byteArrayInputStream2);
                                    TlsUtils.readOpaque8(byteArrayInputStream2);
                                    assertEmpty(byteArrayInputStream2);
                                    this.connection_state = (short) 5;
                                    i = 1;
                                    break;
                                case 14:
                                    short s3 = this.connection_state;
                                    if (s3 != 3) {
                                        if (s3 != 4 && s3 != 5) {
                                            failWithError((short) 2, AP_handshake_failure);
                                            break;
                                        }
                                    } else if (this.chosenCipherSuite.getKeyExchangeAlgorithm() != 1) {
                                        failWithError((short) 2, (short) 10);
                                    }
                                    assertEmpty(byteArrayInputStream2);
                                    boolean z = this.connection_state == 5;
                                    this.connection_state = (short) 6;
                                    if (z) {
                                        sendClientCertificate();
                                    }
                                    short keyExchangeAlgorithm3 = this.chosenCipherSuite.getKeyExchangeAlgorithm();
                                    if (keyExchangeAlgorithm3 == 1) {
                                        byte[] bArr3 = new byte[48];
                                        this.pms = bArr3;
                                        this.random.nextBytes(bArr3);
                                        byte[] bArr4 = this.pms;
                                        bArr4[0] = 3;
                                        bArr4[1] = 1;
                                        PKCS1Encoding pKCS1Encoding = new PKCS1Encoding(new RSABlindedEngine());
                                        pKCS1Encoding.init(true, new ParametersWithRandom(this.serverPublicKey, this.random));
                                        try {
                                            byte[] bArr5 = this.pms;
                                            bArrProcessBlock = pKCS1Encoding.processBlock(bArr5, 0, bArr5.length);
                                        } catch (InvalidCipherTextException unused2) {
                                            failWithError((short) 2, AP_internal_error);
                                        }
                                        sendClientKeyExchange(bArrProcessBlock);
                                        break;
                                    } else if (keyExchangeAlgorithm3 != 3 && keyExchangeAlgorithm3 != 5) {
                                        switch (keyExchangeAlgorithm3) {
                                            case 10:
                                            case 11:
                                            case 12:
                                                sendClientKeyExchange(BigIntegers.asUnsignedByteArray(this.SRP_A));
                                                break;
                                            default:
                                                failWithError((short) 2, (short) 10);
                                                break;
                                        }
                                    } else {
                                        sendClientKeyExchange(BigIntegers.asUnsignedByteArray(this.Yc));
                                    }
                                    this.connection_state = (short) 7;
                                    this.rs.writeMessage((short) 20, new byte[]{1}, 0, 1);
                                    this.connection_state = (short) 9;
                                    this.ms = new byte[48];
                                    byte[] bArr6 = this.clientRandom;
                                    byte[] bArr7 = new byte[bArr6.length + this.serverRandom.length];
                                    System.arraycopy(bArr6, 0, bArr7, 0, bArr6.length);
                                    byte[] bArr8 = this.serverRandom;
                                    System.arraycopy(bArr8, 0, bArr7, this.clientRandom.length, bArr8.length);
                                    TlsUtils.PRF(this.pms, TlsUtils.toByteArray("master secret"), bArr7, this.ms);
                                    this.rs.writeSuite = this.chosenCipherSuite;
                                    this.rs.writeSuite.init(this.ms, this.clientRandom, this.serverRandom);
                                    byte[] bArr9 = new byte[12];
                                    byte[] bArr10 = new byte[36];
                                    this.rs.hash1.doFinal(bArr10, 0);
                                    TlsUtils.PRF(this.ms, TlsUtils.toByteArray("client finished"), bArr10, bArr9);
                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    TlsUtils.writeUint8((short) 20, byteArrayOutputStream);
                                    TlsUtils.writeUint24(12, byteArrayOutputStream);
                                    byteArrayOutputStream.write(bArr9);
                                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                                    this.rs.writeMessage((short) 22, byteArray, 0, byteArray.length);
                                    this.connection_state = (short) 10;
                                    i = 1;
                                    break;
                                default:
                                    failWithError((short) 2, (short) 10);
                                    break;
                            }
                        } else if (this.connection_state != 11) {
                            failWithError((short) 2, (short) 10);
                        } else {
                            byte[] bArr11 = new byte[12];
                            TlsUtils.readFully(bArr11, byteArrayInputStream2);
                            assertEmpty(byteArrayInputStream2);
                            byte[] bArr12 = new byte[12];
                            byte[] bArr13 = new byte[36];
                            this.rs.hash2.doFinal(bArr13, 0);
                            TlsUtils.PRF(this.ms, TlsUtils.toByteArray("server finished"), bArr13, bArr12);
                            while (i < 12) {
                                if (bArr11[i] != bArr12[i]) {
                                    failWithError((short) 2, AP_handshake_failure);
                                }
                                i++;
                            }
                            this.connection_state = (short) 12;
                            this.appDataReady = true;
                            i = 1;
                        }
                    } else if (this.connection_state != 1) {
                        failWithError((short) 2, (short) 10);
                    } else {
                        TlsUtils.checkVersion(byteArrayInputStream2, this);
                        byte[] bArr14 = new byte[32];
                        this.serverRandom = bArr14;
                        TlsUtils.readFully(bArr14, byteArrayInputStream2);
                        TlsUtils.readOpaque8(byteArrayInputStream2);
                        this.chosenCipherSuite = TlsCipherSuiteManager.getCipherSuite(TlsUtils.readUint16(byteArrayInputStream2), this);
                        if (TlsUtils.readUint8(byteArrayInputStream2) != 0) {
                            failWithError((short) 2, AP_illegal_parameter);
                        }
                        if (this.extendedClientHello && byteArrayInputStream2.available() > 0) {
                            byte[] opaque16 = TlsUtils.readOpaque16(byteArrayInputStream2);
                            Hashtable hashtable = new Hashtable();
                            ByteArrayInputStream byteArrayInputStream3 = new ByteArrayInputStream(opaque16);
                            while (byteArrayInputStream3.available() > 0) {
                                hashtable.put(new Integer(TlsUtils.readUint16(byteArrayInputStream3)), TlsUtils.readOpaque16(byteArrayInputStream3));
                            }
                        }
                        assertEmpty(byteArrayInputStream2);
                        this.connection_state = (short) 2;
                        i = 1;
                    }
                }
            }
        } while (i != 0);
    }

    private void processSRPKeyExchange(ByteArrayInputStream byteArrayInputStream, Signer signer) throws IOException {
        InputStream signerInputStream;
        if (signer != null) {
            signer.init(false, this.serverPublicKey);
            byte[] bArr = this.clientRandom;
            signer.update(bArr, 0, bArr.length);
            byte[] bArr2 = this.serverRandom;
            signer.update(bArr2, 0, bArr2.length);
            signerInputStream = new SignerInputStream(byteArrayInputStream, signer);
        } else {
            signerInputStream = byteArrayInputStream;
        }
        byte[] opaque16 = TlsUtils.readOpaque16(signerInputStream);
        byte[] opaque17 = TlsUtils.readOpaque16(signerInputStream);
        byte[] opaque8 = TlsUtils.readOpaque8(signerInputStream);
        byte[] opaque18 = TlsUtils.readOpaque16(signerInputStream);
        if (signer != null && !signer.verifySignature(TlsUtils.readOpaque16(byteArrayInputStream))) {
            failWithError((short) 2, AP_bad_certificate);
        }
        assertEmpty(byteArrayInputStream);
        BigInteger bigInteger = new BigInteger(1, opaque16);
        BigInteger bigInteger2 = new BigInteger(1, opaque17);
        BigInteger bigInteger3 = new BigInteger(1, opaque18);
        SRP6Client sRP6Client = new SRP6Client();
        sRP6Client.init(bigInteger, bigInteger2, new SHA1Digest(), this.random);
        this.SRP_A = sRP6Client.generateClientCredentials(opaque8, this.SRP_identity, this.SRP_password);
        try {
            this.pms = BigIntegers.asUnsignedByteArray(sRP6Client.calculateSecret(bigInteger3));
        } catch (CryptoException unused) {
            failWithError((short) 2, AP_illegal_parameter);
        }
    }

    private void sendClientCertificate() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TlsUtils.writeUint8((short) 11, byteArrayOutputStream);
        TlsUtils.writeUint24(3, byteArrayOutputStream);
        TlsUtils.writeUint24(0, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        this.rs.writeMessage((short) 22, byteArray, 0, byteArray.length);
    }

    private void sendClientKeyExchange(byte[] bArr) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TlsUtils.writeUint8((short) 16, byteArrayOutputStream);
        TlsUtils.writeUint24(bArr.length + 2, byteArrayOutputStream);
        TlsUtils.writeOpaque16(bArr, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        this.rs.writeMessage((short) 22, byteArray, 0, byteArray.length);
    }

    private void validateKeyUsage(X509CertificateStructure x509CertificateStructure, int i) throws IOException {
        X509Extension extension;
        X509Extensions extensions = x509CertificateStructure.getTBSCertificate().getExtensions();
        if (extensions == null || (extension = extensions.getExtension(X509Extensions.KeyUsage)) == null || (KeyUsage.getInstance(extension).getBytes()[0] & UByte.MAX_VALUE & i) == i) {
            return;
        }
        failWithError((short) 2, AP_certificate_unknown);
    }

    protected void assertEmpty(ByteArrayInputStream byteArrayInputStream) throws IOException {
        if (byteArrayInputStream.available() > 0) {
            failWithError((short) 2, AP_decode_error);
        }
    }

    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        failWithError((short) 1, (short) 0);
    }

    public void connect(CertificateVerifyer certificateVerifyer) throws IOException {
        this.verifyer = certificateVerifyer;
        byte[] bArr = new byte[32];
        this.clientRandom = bArr;
        this.random.nextBytes(bArr);
        int iCurrentTimeMillis = (int) (System.currentTimeMillis() / 1000);
        byte[] bArr2 = this.clientRandom;
        bArr2[0] = (byte) (iCurrentTimeMillis >> 24);
        bArr2[1] = (byte) (iCurrentTimeMillis >> 16);
        bArr2[2] = (byte) (iCurrentTimeMillis >> 8);
        bArr2[3] = (byte) iCurrentTimeMillis;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TlsUtils.writeVersion(byteArrayOutputStream);
        byteArrayOutputStream.write(this.clientRandom);
        TlsUtils.writeUint8((short) 0, byteArrayOutputStream);
        TlsCipherSuiteManager.writeCipherSuites(byteArrayOutputStream);
        TlsUtils.writeOpaque8(new byte[]{0}, byteArrayOutputStream);
        Hashtable hashtable = new Hashtable();
        boolean z = !hashtable.isEmpty();
        this.extendedClientHello = z;
        if (z) {
            ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
            Enumeration enumerationKeys = hashtable.keys();
            while (enumerationKeys.hasMoreElements()) {
                Integer num = (Integer) enumerationKeys.nextElement();
                byte[] bArr3 = (byte[]) hashtable.get(num);
                TlsUtils.writeUint16(num.intValue(), byteArrayOutputStream2);
                TlsUtils.writeOpaque16(bArr3, byteArrayOutputStream2);
            }
            TlsUtils.writeOpaque16(byteArrayOutputStream2.toByteArray(), byteArrayOutputStream);
        }
        ByteArrayOutputStream byteArrayOutputStream3 = new ByteArrayOutputStream();
        TlsUtils.writeUint8((short) 1, byteArrayOutputStream3);
        TlsUtils.writeUint24(byteArrayOutputStream.size(), byteArrayOutputStream3);
        byteArrayOutputStream3.write(byteArrayOutputStream.toByteArray());
        byte[] byteArray = byteArrayOutputStream3.toByteArray();
        this.rs.writeMessage((short) 22, byteArray, 0, byteArray.length);
        this.connection_state = (short) 1;
        while (this.connection_state != 12) {
            this.rs.readData();
        }
        this.tlsInputStream = new TlsInputStream(this);
        this.tlsOutputStream = new TlsOuputStream(this);
    }

    protected void failWithError(short s, short s2) throws IOException {
        if (this.closed) {
            throw new IOException(TLS_ERROR_MESSAGE);
        }
        byte[] bArr = {(byte) s, (byte) s2};
        this.closed = true;
        if (s == 2) {
            this.failedWithError = true;
        }
        this.rs.writeMessage((short) 21, bArr, 0, 2);
        this.rs.close();
        if (s == 2) {
            throw new IOException(TLS_ERROR_MESSAGE);
        }
    }

    protected void flush() throws IOException {
        this.rs.flush();
    }

    public InputStream getInputStream() {
        return this.tlsInputStream;
    }

    public OutputStream getOutputStream() {
        return this.tlsOutputStream;
    }

    public TlsInputStream getTlsInputStream() {
        return this.tlsInputStream;
    }

    public TlsOuputStream getTlsOuputStream() {
        return this.tlsOutputStream;
    }

    protected void processData(short s, byte[] bArr, int i, int i2) throws IOException {
        switch (s) {
            case 20:
                this.changeCipherSpecQueue.addData(bArr, i, i2);
                processChangeCipherSpec();
                break;
            case 21:
                this.alertQueue.addData(bArr, i, i2);
                processAlert();
                break;
            case 22:
                this.handshakeQueue.addData(bArr, i, i2);
                processHandshake();
                break;
            case 23:
                if (!this.appDataReady) {
                    failWithError((short) 2, (short) 10);
                }
                this.applicationDataQueue.addData(bArr, i, i2);
                processApplicationData();
                break;
        }
    }

    protected int readApplicationData(byte[] bArr, int i, int i2) throws IOException {
        while (this.applicationDataQueue.size() == 0) {
            if (this.failedWithError) {
                throw new IOException(TLS_ERROR_MESSAGE);
            }
            if (this.closed) {
                return -1;
            }
            try {
                this.rs.readData();
            } catch (IOException e) {
                if (!this.closed) {
                    failWithError((short) 2, AP_internal_error);
                }
                throw e;
            } catch (RuntimeException e2) {
                if (!this.closed) {
                    failWithError((short) 2, AP_internal_error);
                }
                throw e2;
            }
        }
        int iMin = Math.min(i2, this.applicationDataQueue.size());
        this.applicationDataQueue.read(bArr, i, iMin, 0);
        this.applicationDataQueue.removeData(iMin);
        return iMin;
    }

    protected void writeData(byte[] bArr, int i, int i2) throws IOException {
        if (this.failedWithError) {
            throw new IOException(TLS_ERROR_MESSAGE);
        }
        if (this.closed) {
            throw new IOException("Sorry, connection has been closed, you cannot write more data");
        }
        this.rs.writeMessage((short) 23, emptybuf, 0, 0);
        do {
            int iMin = Math.min(i2, 16384);
            try {
                this.rs.writeMessage((short) 23, bArr, i, iMin);
                i += iMin;
                i2 -= iMin;
            } catch (IOException e) {
                if (!this.closed) {
                    failWithError((short) 2, AP_internal_error);
                }
                throw e;
            } catch (RuntimeException e2) {
                if (!this.closed) {
                    failWithError((short) 2, AP_internal_error);
                }
                throw e2;
            }
        } while (i2 > 0);
    }
}
