package org.bouncycastle.jce.provider;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.DSAParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.Hashtable;
import javax.crypto.spec.DHParameterSpec;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.DHBasicKeyPairGenerator;
import org.bouncycastle.crypto.generators.DHParametersGenerator;
import org.bouncycastle.crypto.generators.DSAKeyPairGenerator;
import org.bouncycastle.crypto.generators.DSAParametersGenerator;
import org.bouncycastle.crypto.generators.ElGamalKeyPairGenerator;
import org.bouncycastle.crypto.generators.ElGamalParametersGenerator;
import org.bouncycastle.crypto.generators.GOST3410KeyPairGenerator;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.DHKeyGenerationParameters;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.params.DHPrivateKeyParameters;
import org.bouncycastle.crypto.params.DHPublicKeyParameters;
import org.bouncycastle.crypto.params.DSAKeyGenerationParameters;
import org.bouncycastle.crypto.params.DSAParameters;
import org.bouncycastle.crypto.params.DSAPrivateKeyParameters;
import org.bouncycastle.crypto.params.DSAPublicKeyParameters;
import org.bouncycastle.crypto.params.ElGamalKeyGenerationParameters;
import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.bouncycastle.crypto.params.GOST3410KeyGenerationParameters;
import org.bouncycastle.crypto.params.GOST3410Parameters;
import org.bouncycastle.crypto.params.GOST3410PrivateKeyParameters;
import org.bouncycastle.crypto.params.GOST3410PublicKeyParameters;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.jce.spec.ElGamalParameterSpec;
import org.bouncycastle.jce.spec.GOST3410ParameterSpec;
import org.bouncycastle.jce.spec.GOST3410PublicKeyParameterSetSpec;

/* JADX INFO: loaded from: classes2.dex */
public abstract class JDKKeyPairGenerator extends KeyPairGenerator {

    public static class DH extends JDKKeyPairGenerator {
        private static Hashtable params = new Hashtable();
        int certainty;
        DHBasicKeyPairGenerator engine;
        boolean initialised;
        DHKeyGenerationParameters param;
        SecureRandom random;
        int strength;

        public DH() {
            super("DH");
            this.engine = new DHBasicKeyPairGenerator();
            this.strength = 1024;
            this.certainty = 20;
            this.random = new SecureRandom();
            this.initialised = false;
        }

        @Override // org.bouncycastle.jce.provider.JDKKeyPairGenerator, java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
        public KeyPair generateKeyPair() {
            if (!this.initialised) {
                Integer num = new Integer(this.strength);
                if (params.containsKey(num)) {
                    this.param = (DHKeyGenerationParameters) params.get(num);
                } else {
                    DHParametersGenerator dHParametersGenerator = new DHParametersGenerator();
                    dHParametersGenerator.init(this.strength, this.certainty, this.random);
                    DHKeyGenerationParameters dHKeyGenerationParameters = new DHKeyGenerationParameters(this.random, dHParametersGenerator.generateParameters());
                    this.param = dHKeyGenerationParameters;
                    params.put(num, dHKeyGenerationParameters);
                }
                this.engine.init(this.param);
                this.initialised = true;
            }
            AsymmetricCipherKeyPair asymmetricCipherKeyPairGenerateKeyPair = this.engine.generateKeyPair();
            return new KeyPair(new JCEDHPublicKey((DHPublicKeyParameters) asymmetricCipherKeyPairGenerateKeyPair.getPublic()), new JCEDHPrivateKey((DHPrivateKeyParameters) asymmetricCipherKeyPairGenerateKeyPair.getPrivate()));
        }

        @Override // org.bouncycastle.jce.provider.JDKKeyPairGenerator, java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
        public void initialize(int i, SecureRandom secureRandom) {
            this.strength = i;
            this.random = secureRandom;
        }

        @Override // java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
        public void initialize(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            if (!(algorithmParameterSpec instanceof DHParameterSpec)) {
                throw new InvalidAlgorithmParameterException("parameter object not a DHParameterSpec");
            }
            DHParameterSpec dHParameterSpec = (DHParameterSpec) algorithmParameterSpec;
            DHKeyGenerationParameters dHKeyGenerationParameters = new DHKeyGenerationParameters(secureRandom, new DHParameters(dHParameterSpec.getP(), dHParameterSpec.getG(), null, dHParameterSpec.getL()));
            this.param = dHKeyGenerationParameters;
            this.engine.init(dHKeyGenerationParameters);
            this.initialised = true;
        }
    }

    public static class DSA extends JDKKeyPairGenerator {
        int certainty;
        DSAKeyPairGenerator engine;
        boolean initialised;
        DSAKeyGenerationParameters param;
        SecureRandom random;
        int strength;

        public DSA() {
            super("DSA");
            this.engine = new DSAKeyPairGenerator();
            this.strength = 1024;
            this.certainty = 20;
            this.random = new SecureRandom();
            this.initialised = false;
        }

        @Override // org.bouncycastle.jce.provider.JDKKeyPairGenerator, java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
        public KeyPair generateKeyPair() {
            if (!this.initialised) {
                DSAParametersGenerator dSAParametersGenerator = new DSAParametersGenerator();
                dSAParametersGenerator.init(this.strength, this.certainty, this.random);
                DSAKeyGenerationParameters dSAKeyGenerationParameters = new DSAKeyGenerationParameters(this.random, dSAParametersGenerator.generateParameters());
                this.param = dSAKeyGenerationParameters;
                this.engine.init(dSAKeyGenerationParameters);
                this.initialised = true;
            }
            AsymmetricCipherKeyPair asymmetricCipherKeyPairGenerateKeyPair = this.engine.generateKeyPair();
            return new KeyPair(new JDKDSAPublicKey((DSAPublicKeyParameters) asymmetricCipherKeyPairGenerateKeyPair.getPublic()), new JDKDSAPrivateKey((DSAPrivateKeyParameters) asymmetricCipherKeyPairGenerateKeyPair.getPrivate()));
        }

        @Override // org.bouncycastle.jce.provider.JDKKeyPairGenerator, java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
        public void initialize(int i, SecureRandom secureRandom) {
            if (i < 512 || i > 1024 || i % 64 != 0) {
                throw new InvalidParameterException("strength must be from 512 - 1024 and a multiple of 64");
            }
            this.strength = i;
            this.random = secureRandom;
        }

        @Override // java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
        public void initialize(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            if (!(algorithmParameterSpec instanceof DSAParameterSpec)) {
                throw new InvalidAlgorithmParameterException("parameter object not a DSAParameterSpec");
            }
            DSAParameterSpec dSAParameterSpec = (DSAParameterSpec) algorithmParameterSpec;
            DSAKeyGenerationParameters dSAKeyGenerationParameters = new DSAKeyGenerationParameters(secureRandom, new DSAParameters(dSAParameterSpec.getP(), dSAParameterSpec.getQ(), dSAParameterSpec.getG()));
            this.param = dSAKeyGenerationParameters;
            this.engine.init(dSAKeyGenerationParameters);
            this.initialised = true;
        }
    }

    public static class ElGamal extends JDKKeyPairGenerator {
        int certainty;
        ElGamalKeyPairGenerator engine;
        boolean initialised;
        ElGamalKeyGenerationParameters param;
        SecureRandom random;
        int strength;

        public ElGamal() {
            super("ElGamal");
            this.engine = new ElGamalKeyPairGenerator();
            this.strength = 1024;
            this.certainty = 20;
            this.random = new SecureRandom();
            this.initialised = false;
        }

        @Override // org.bouncycastle.jce.provider.JDKKeyPairGenerator, java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
        public KeyPair generateKeyPair() {
            if (!this.initialised) {
                ElGamalParametersGenerator elGamalParametersGenerator = new ElGamalParametersGenerator();
                elGamalParametersGenerator.init(this.strength, this.certainty, this.random);
                ElGamalKeyGenerationParameters elGamalKeyGenerationParameters = new ElGamalKeyGenerationParameters(this.random, elGamalParametersGenerator.generateParameters());
                this.param = elGamalKeyGenerationParameters;
                this.engine.init(elGamalKeyGenerationParameters);
                this.initialised = true;
            }
            AsymmetricCipherKeyPair asymmetricCipherKeyPairGenerateKeyPair = this.engine.generateKeyPair();
            return new KeyPair(new JCEElGamalPublicKey((ElGamalPublicKeyParameters) asymmetricCipherKeyPairGenerateKeyPair.getPublic()), new JCEElGamalPrivateKey((ElGamalPrivateKeyParameters) asymmetricCipherKeyPairGenerateKeyPair.getPrivate()));
        }

        @Override // org.bouncycastle.jce.provider.JDKKeyPairGenerator, java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
        public void initialize(int i, SecureRandom secureRandom) {
            this.strength = i;
            this.random = secureRandom;
        }

        @Override // java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
        public void initialize(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            ElGamalKeyGenerationParameters elGamalKeyGenerationParameters;
            boolean z = algorithmParameterSpec instanceof ElGamalParameterSpec;
            if (!z && !(algorithmParameterSpec instanceof DHParameterSpec)) {
                throw new InvalidAlgorithmParameterException("parameter object not a DHParameterSpec or an ElGamalParameterSpec");
            }
            if (z) {
                ElGamalParameterSpec elGamalParameterSpec = (ElGamalParameterSpec) algorithmParameterSpec;
                elGamalKeyGenerationParameters = new ElGamalKeyGenerationParameters(secureRandom, new ElGamalParameters(elGamalParameterSpec.getP(), elGamalParameterSpec.getG()));
            } else {
                DHParameterSpec dHParameterSpec = (DHParameterSpec) algorithmParameterSpec;
                elGamalKeyGenerationParameters = new ElGamalKeyGenerationParameters(secureRandom, new ElGamalParameters(dHParameterSpec.getP(), dHParameterSpec.getG(), dHParameterSpec.getL()));
            }
            this.param = elGamalKeyGenerationParameters;
            this.engine.init(this.param);
            this.initialised = true;
        }
    }

    public static class GOST3410 extends JDKKeyPairGenerator {
        GOST3410KeyPairGenerator engine;
        GOST3410ParameterSpec gost3410Params;
        boolean initialised;
        GOST3410KeyGenerationParameters param;
        SecureRandom random;
        int strength;

        public GOST3410() {
            super("GOST3410");
            this.engine = new GOST3410KeyPairGenerator();
            this.strength = 1024;
            this.random = null;
            this.initialised = false;
        }

        private void init(GOST3410ParameterSpec gOST3410ParameterSpec, SecureRandom secureRandom) {
            GOST3410PublicKeyParameterSetSpec publicKeyParameters = gOST3410ParameterSpec.getPublicKeyParameters();
            GOST3410KeyGenerationParameters gOST3410KeyGenerationParameters = new GOST3410KeyGenerationParameters(secureRandom, new GOST3410Parameters(publicKeyParameters.getP(), publicKeyParameters.getQ(), publicKeyParameters.getA()));
            this.param = gOST3410KeyGenerationParameters;
            this.engine.init(gOST3410KeyGenerationParameters);
            this.initialised = true;
            this.gost3410Params = gOST3410ParameterSpec;
        }

        @Override // org.bouncycastle.jce.provider.JDKKeyPairGenerator, java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
        public KeyPair generateKeyPair() {
            if (!this.initialised) {
                init(new GOST3410ParameterSpec(CryptoProObjectIdentifiers.gostR3410_94_CryptoPro_A.getId()), new SecureRandom());
            }
            AsymmetricCipherKeyPair asymmetricCipherKeyPairGenerateKeyPair = this.engine.generateKeyPair();
            return new KeyPair(new JDKGOST3410PublicKey((GOST3410PublicKeyParameters) asymmetricCipherKeyPairGenerateKeyPair.getPublic(), this.gost3410Params), new JDKGOST3410PrivateKey((GOST3410PrivateKeyParameters) asymmetricCipherKeyPairGenerateKeyPair.getPrivate(), this.gost3410Params));
        }

        @Override // org.bouncycastle.jce.provider.JDKKeyPairGenerator, java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
        public void initialize(int i, SecureRandom secureRandom) {
            this.strength = i;
            this.random = secureRandom;
        }

        @Override // java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
        public void initialize(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            if (!(algorithmParameterSpec instanceof GOST3410ParameterSpec)) {
                throw new InvalidAlgorithmParameterException("parameter object not a GOST3410ParameterSpec");
            }
            init((GOST3410ParameterSpec) algorithmParameterSpec, secureRandom);
        }
    }

    public static class RSA extends JDKKeyPairGenerator {
        static final BigInteger defaultPublicExponent = BigInteger.valueOf(65537);
        static final int defaultTests = 12;
        RSAKeyPairGenerator engine;
        RSAKeyGenerationParameters param;

        public RSA() {
            super("RSA");
            this.engine = new RSAKeyPairGenerator();
            RSAKeyGenerationParameters rSAKeyGenerationParameters = new RSAKeyGenerationParameters(defaultPublicExponent, new SecureRandom(), 2048, 12);
            this.param = rSAKeyGenerationParameters;
            this.engine.init(rSAKeyGenerationParameters);
        }

        @Override // org.bouncycastle.jce.provider.JDKKeyPairGenerator, java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
        public KeyPair generateKeyPair() {
            AsymmetricCipherKeyPair asymmetricCipherKeyPairGenerateKeyPair = this.engine.generateKeyPair();
            return new KeyPair(new JCERSAPublicKey((RSAKeyParameters) asymmetricCipherKeyPairGenerateKeyPair.getPublic()), new JCERSAPrivateCrtKey((RSAPrivateCrtKeyParameters) asymmetricCipherKeyPairGenerateKeyPair.getPrivate()));
        }

        @Override // org.bouncycastle.jce.provider.JDKKeyPairGenerator, java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
        public void initialize(int i, SecureRandom secureRandom) {
            RSAKeyGenerationParameters rSAKeyGenerationParameters = new RSAKeyGenerationParameters(defaultPublicExponent, secureRandom, i, 12);
            this.param = rSAKeyGenerationParameters;
            this.engine.init(rSAKeyGenerationParameters);
        }

        @Override // java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
        public void initialize(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            if (!(algorithmParameterSpec instanceof RSAKeyGenParameterSpec)) {
                throw new InvalidAlgorithmParameterException("parameter object not a RSAKeyGenParameterSpec");
            }
            RSAKeyGenParameterSpec rSAKeyGenParameterSpec = (RSAKeyGenParameterSpec) algorithmParameterSpec;
            RSAKeyGenerationParameters rSAKeyGenerationParameters = new RSAKeyGenerationParameters(rSAKeyGenParameterSpec.getPublicExponent(), secureRandom, rSAKeyGenParameterSpec.getKeysize(), 12);
            this.param = rSAKeyGenerationParameters;
            this.engine.init(rSAKeyGenerationParameters);
        }
    }

    public JDKKeyPairGenerator(String str) {
        super(str);
    }

    @Override // java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
    public abstract KeyPair generateKeyPair();

    @Override // java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
    public abstract void initialize(int i, SecureRandom secureRandom);
}
