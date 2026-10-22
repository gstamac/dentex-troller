package org.bouncycastle.crypto.signers;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DSA;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECKeyParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECConstants;

/* JADX INFO: loaded from: classes2.dex */
public class ECNRSigner implements DSA {
    private boolean forSigning;
    private ECKeyParameters key;
    private SecureRandom random;

    @Override // org.bouncycastle.crypto.DSA
    public BigInteger[] generateSignature(byte[] bArr) {
        AsymmetricCipherKeyPair asymmetricCipherKeyPairGenerateKeyPair;
        BigInteger bigIntegerMod;
        if (!this.forSigning) {
            throw new IllegalStateException("not initialised for signing");
        }
        BigInteger n = ((ECPrivateKeyParameters) this.key).getParameters().getN();
        int iBitLength = n.bitLength();
        BigInteger bigInteger = new BigInteger(1, bArr);
        int iBitLength2 = bigInteger.bitLength();
        ECPrivateKeyParameters eCPrivateKeyParameters = (ECPrivateKeyParameters) this.key;
        if (iBitLength2 > iBitLength) {
            throw new DataLengthException("input too large for ECNR key.");
        }
        do {
            ECKeyPairGenerator eCKeyPairGenerator = new ECKeyPairGenerator();
            eCKeyPairGenerator.init(new ECKeyGenerationParameters(eCPrivateKeyParameters.getParameters(), this.random));
            asymmetricCipherKeyPairGenerateKeyPair = eCKeyPairGenerator.generateKeyPair();
            bigIntegerMod = ((ECPublicKeyParameters) asymmetricCipherKeyPairGenerateKeyPair.getPublic()).getQ().getX().toBigInteger().add(bigInteger).mod(n);
        } while (bigIntegerMod.equals(ECConstants.ZERO));
        return new BigInteger[]{bigIntegerMod, ((ECPrivateKeyParameters) asymmetricCipherKeyPairGenerateKeyPair.getPrivate()).getD().subtract(bigIntegerMod.multiply(eCPrivateKeyParameters.getD())).mod(n)};
    }

    @Override // org.bouncycastle.crypto.DSA
    public void init(boolean z, CipherParameters cipherParameters) {
        ECKeyParameters eCKeyParameters;
        this.forSigning = z;
        if (!z) {
            eCKeyParameters = (ECPublicKeyParameters) cipherParameters;
        } else {
            if (cipherParameters instanceof ParametersWithRandom) {
                ParametersWithRandom parametersWithRandom = (ParametersWithRandom) cipherParameters;
                this.random = parametersWithRandom.getRandom();
                this.key = (ECPrivateKeyParameters) parametersWithRandom.getParameters();
                return;
            }
            this.random = new SecureRandom();
            eCKeyParameters = (ECPrivateKeyParameters) cipherParameters;
        }
        this.key = eCKeyParameters;
    }

    @Override // org.bouncycastle.crypto.DSA
    public boolean verifySignature(byte[] bArr, BigInteger bigInteger, BigInteger bigInteger2) {
        if (this.forSigning) {
            throw new IllegalStateException("not initialised for verifying");
        }
        ECPublicKeyParameters eCPublicKeyParameters = (ECPublicKeyParameters) this.key;
        BigInteger n = eCPublicKeyParameters.getParameters().getN();
        int iBitLength = n.bitLength();
        BigInteger bigInteger3 = new BigInteger(1, bArr);
        if (bigInteger3.bitLength() > iBitLength) {
            throw new DataLengthException("input too large for ECNR key.");
        }
        if (bigInteger.compareTo(ECConstants.ONE) < 0 || bigInteger.compareTo(n) >= 0 || bigInteger2.compareTo(ECConstants.ZERO) < 0 || bigInteger2.compareTo(n) >= 0) {
            return false;
        }
        return bigInteger.subtract(ECAlgorithms.sumOfTwoMultiplies(eCPublicKeyParameters.getParameters().getG(), bigInteger2, eCPublicKeyParameters.getQ(), bigInteger).getX().toBigInteger()).mod(n).equals(bigInteger3);
    }
}
