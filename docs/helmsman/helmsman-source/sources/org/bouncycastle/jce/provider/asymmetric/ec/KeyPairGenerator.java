package org.bouncycastle.jce.provider.asymmetric.ec;

import com.MAVLink.enums.MAV_CMD;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;
import java.util.Hashtable;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.cryptopro.ECGOST3410NamedCurves;
import org.bouncycastle.asn1.nist.NISTNamedCurves;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.teletrust.TeleTrusTNamedCurves;
import org.bouncycastle.asn1.x9.X962NamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jce.provider.JCEECPrivateKey;
import org.bouncycastle.jce.provider.JCEECPublicKey;
import org.bouncycastle.jce.provider.JDKKeyPairGenerator;
import org.bouncycastle.jce.provider.ProviderUtil;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECCurve;

/* JADX INFO: loaded from: classes2.dex */
public abstract class KeyPairGenerator extends JDKKeyPairGenerator {

    public static class EC extends KeyPairGenerator {
        private static Hashtable ecParameters;
        String algorithm;
        int certainty;
        Object ecParams;
        ECKeyPairGenerator engine;
        boolean initialised;
        ECKeyGenerationParameters param;
        SecureRandom random;
        int strength;

        static {
            Hashtable hashtable = new Hashtable();
            ecParameters = hashtable;
            hashtable.put(new Integer(192), new ECGenParameterSpec("prime192v1"));
            ecParameters.put(new Integer(239), new ECGenParameterSpec("prime239v1"));
            ecParameters.put(new Integer(256), new ECGenParameterSpec("prime256v1"));
            ecParameters.put(new Integer(224), new ECGenParameterSpec("P-224"));
            ecParameters.put(new Integer(384), new ECGenParameterSpec("P-384"));
            ecParameters.put(new Integer(MAV_CMD.MAV_CMD_REQUEST_CAMERA_INFORMATION), new ECGenParameterSpec("P-521"));
        }

        public EC() {
            super("EC");
            this.engine = new ECKeyPairGenerator();
            this.ecParams = null;
            this.strength = 239;
            this.certainty = 50;
            this.random = new SecureRandom();
            this.initialised = false;
            this.algorithm = "EC";
        }

        public EC(String str) {
            super(str);
            this.engine = new ECKeyPairGenerator();
            this.ecParams = null;
            this.strength = 239;
            this.certainty = 50;
            this.random = new SecureRandom();
            this.initialised = false;
            this.algorithm = str;
        }

        @Override // org.bouncycastle.jce.provider.JDKKeyPairGenerator, java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
        public KeyPair generateKeyPair() {
            if (!this.initialised) {
                throw new IllegalStateException("EC Key Pair Generator not initialised");
            }
            AsymmetricCipherKeyPair asymmetricCipherKeyPairGenerateKeyPair = this.engine.generateKeyPair();
            ECPublicKeyParameters eCPublicKeyParameters = (ECPublicKeyParameters) asymmetricCipherKeyPairGenerateKeyPair.getPublic();
            ECPrivateKeyParameters eCPrivateKeyParameters = (ECPrivateKeyParameters) asymmetricCipherKeyPairGenerateKeyPair.getPrivate();
            Object obj = this.ecParams;
            if (obj instanceof ECParameterSpec) {
                ECParameterSpec eCParameterSpec = (ECParameterSpec) obj;
                JCEECPublicKey jCEECPublicKey = new JCEECPublicKey(this.algorithm, eCPublicKeyParameters, eCParameterSpec);
                return new KeyPair(jCEECPublicKey, new JCEECPrivateKey(this.algorithm, eCPrivateKeyParameters, jCEECPublicKey, eCParameterSpec));
            }
            if (obj == null) {
                return new KeyPair(new JCEECPublicKey(this.algorithm, eCPublicKeyParameters), new JCEECPrivateKey(this.algorithm, eCPrivateKeyParameters));
            }
            java.security.spec.ECParameterSpec eCParameterSpec2 = (java.security.spec.ECParameterSpec) obj;
            JCEECPublicKey jCEECPublicKey2 = new JCEECPublicKey(this.algorithm, eCPublicKeyParameters, eCParameterSpec2);
            return new KeyPair(jCEECPublicKey2, new JCEECPrivateKey(this.algorithm, eCPrivateKeyParameters, jCEECPublicKey2, eCParameterSpec2));
        }

        @Override // org.bouncycastle.jce.provider.JDKKeyPairGenerator, java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
        public void initialize(int i, SecureRandom secureRandom) {
            this.strength = i;
            this.random = secureRandom;
            Object obj = ecParameters.get(new Integer(i));
            this.ecParams = obj;
            if (obj == null) {
                throw new InvalidParameterException("unknown key size.");
            }
            try {
                initialize((ECGenParameterSpec) obj, secureRandom);
            } catch (InvalidAlgorithmParameterException unused) {
                throw new InvalidParameterException("key size not configurable.");
            }
        }

        @Override // java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
        public void initialize(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            ECKeyGenerationParameters eCKeyGenerationParameters;
            ECNamedCurveSpec eCNamedCurveSpec;
            ECKeyGenerationParameters eCKeyGenerationParameters2;
            if (!(algorithmParameterSpec instanceof ECParameterSpec)) {
                if (algorithmParameterSpec instanceof java.security.spec.ECParameterSpec) {
                    java.security.spec.ECParameterSpec eCParameterSpec = (java.security.spec.ECParameterSpec) algorithmParameterSpec;
                    this.ecParams = algorithmParameterSpec;
                    ECCurve eCCurveConvertCurve = EC5Util.convertCurve(eCParameterSpec.getCurve());
                    eCKeyGenerationParameters2 = new ECKeyGenerationParameters(new ECDomainParameters(eCCurveConvertCurve, EC5Util.convertPoint(eCCurveConvertCurve, eCParameterSpec.getGenerator(), false), eCParameterSpec.getOrder(), BigInteger.valueOf(eCParameterSpec.getCofactor())), secureRandom);
                } else if (algorithmParameterSpec instanceof ECGenParameterSpec) {
                    String name = ((ECGenParameterSpec) algorithmParameterSpec).getName();
                    if (this.algorithm.equals("ECGOST3410")) {
                        ECDomainParameters byName = ECGOST3410NamedCurves.getByName(name);
                        if (byName == null) {
                            throw new InvalidAlgorithmParameterException("unknown curve name: " + name);
                        }
                        eCNamedCurveSpec = new ECNamedCurveSpec(name, byName.getCurve(), byName.getG(), byName.getN(), byName.getH(), byName.getSeed());
                    } else {
                        X9ECParameters byName2 = X962NamedCurves.getByName(name);
                        if (byName2 == null) {
                            byName2 = SECNamedCurves.getByName(name);
                            if (byName2 == null) {
                                byName2 = NISTNamedCurves.getByName(name);
                            }
                            if (byName2 == null) {
                                byName2 = TeleTrusTNamedCurves.getByName(name);
                            }
                            if (byName2 == null) {
                                try {
                                    DERObjectIdentifier dERObjectIdentifier = new DERObjectIdentifier(name);
                                    X9ECParameters byOID = X962NamedCurves.getByOID(dERObjectIdentifier);
                                    if (byOID == null) {
                                        byOID = SECNamedCurves.getByOID(dERObjectIdentifier);
                                    }
                                    if (byOID == null) {
                                        byOID = NISTNamedCurves.getByOID(dERObjectIdentifier);
                                    }
                                    byName2 = byOID == null ? TeleTrusTNamedCurves.getByOID(dERObjectIdentifier) : byOID;
                                    if (byName2 == null) {
                                        throw new InvalidAlgorithmParameterException("unknown curve OID: " + name);
                                    }
                                } catch (IllegalArgumentException unused) {
                                    throw new InvalidAlgorithmParameterException("unknown curve name: " + name);
                                }
                            }
                        }
                        eCNamedCurveSpec = new ECNamedCurveSpec(name, byName2.getCurve(), byName2.getG(), byName2.getN(), byName2.getH(), null);
                    }
                    this.ecParams = eCNamedCurveSpec;
                    java.security.spec.ECParameterSpec eCParameterSpec2 = (java.security.spec.ECParameterSpec) this.ecParams;
                    ECCurve eCCurveConvertCurve2 = EC5Util.convertCurve(eCParameterSpec2.getCurve());
                    eCKeyGenerationParameters2 = new ECKeyGenerationParameters(new ECDomainParameters(eCCurveConvertCurve2, EC5Util.convertPoint(eCCurveConvertCurve2, eCParameterSpec2.getGenerator(), false), eCParameterSpec2.getOrder(), BigInteger.valueOf(eCParameterSpec2.getCofactor())), secureRandom);
                } else {
                    if (algorithmParameterSpec != null || ProviderUtil.getEcImplicitlyCa() == null) {
                        if (algorithmParameterSpec != null || ProviderUtil.getEcImplicitlyCa() != null) {
                            throw new InvalidAlgorithmParameterException("parameter object not a ECParameterSpec");
                        }
                        throw new InvalidAlgorithmParameterException("null parameter passed but no implicitCA set");
                    }
                    ECParameterSpec ecImplicitlyCa = ProviderUtil.getEcImplicitlyCa();
                    this.ecParams = algorithmParameterSpec;
                    eCKeyGenerationParameters = new ECKeyGenerationParameters(new ECDomainParameters(ecImplicitlyCa.getCurve(), ecImplicitlyCa.getG(), ecImplicitlyCa.getN()), secureRandom);
                }
                this.param = eCKeyGenerationParameters2;
                this.engine.init(eCKeyGenerationParameters2);
                this.initialised = true;
            }
            ECParameterSpec eCParameterSpec3 = (ECParameterSpec) algorithmParameterSpec;
            this.ecParams = algorithmParameterSpec;
            eCKeyGenerationParameters = new ECKeyGenerationParameters(new ECDomainParameters(eCParameterSpec3.getCurve(), eCParameterSpec3.getG(), eCParameterSpec3.getN()), secureRandom);
            this.param = eCKeyGenerationParameters;
            this.engine.init(eCKeyGenerationParameters);
            this.initialised = true;
        }
    }

    public static class ECDH extends EC {
        public ECDH() {
            super("ECDH");
        }
    }

    public static class ECDHC extends EC {
        public ECDHC() {
            super("ECDHC");
        }
    }

    public static class ECDSA extends EC {
        public ECDSA() {
            super("ECDSA");
        }
    }

    public static class ECGOST3410 extends EC {
        public ECGOST3410() {
            super("ECGOST3410");
        }
    }

    public static class ECMQV extends EC {
        public ECMQV() {
            super("ECMQV");
        }
    }

    public KeyPairGenerator(String str) {
        super(str);
    }
}
