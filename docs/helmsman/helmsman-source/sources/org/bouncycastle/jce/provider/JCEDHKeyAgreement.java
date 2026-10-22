package org.bouncycastle.jce.provider;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Hashtable;
import javax.crypto.KeyAgreementSpi;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.crypto.params.DESParameters;
import org.bouncycastle.util.Strings;

/* JADX INFO: loaded from: classes2.dex */
public class JCEDHKeyAgreement extends KeyAgreementSpi {
    private static final Hashtable algorithms;
    private BigInteger g;
    private BigInteger p;
    private SecureRandom random;
    private BigInteger result;
    private BigInteger x;

    static {
        Hashtable hashtable = new Hashtable();
        algorithms = hashtable;
        Integer num = new Integer(64);
        Integer num2 = new Integer(192);
        Integer num3 = new Integer(128);
        hashtable.put("DES", num);
        hashtable.put("DESEDE", num2);
        hashtable.put("BLOWFISH", num3);
    }

    private byte[] bigIntToBytes(BigInteger bigInteger) {
        byte[] byteArray = bigInteger.toByteArray();
        if (byteArray[0] != 0) {
            return byteArray;
        }
        int length = byteArray.length - 1;
        byte[] bArr = new byte[length];
        System.arraycopy(byteArray, 1, bArr, 0, length);
        return bArr;
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected Key engineDoPhase(Key key, boolean z) throws IllegalStateException, InvalidKeyException {
        if (this.x == null) {
            throw new IllegalStateException("Diffie-Hellman not initialised.");
        }
        if (!(key instanceof DHPublicKey)) {
            throw new InvalidKeyException("DHKeyAgreement doPhase requires DHPublicKey");
        }
        DHPublicKey dHPublicKey = (DHPublicKey) key;
        if (!dHPublicKey.getParams().getG().equals(this.g) || !dHPublicKey.getParams().getP().equals(this.p)) {
            throw new InvalidKeyException("DHPublicKey not for this KeyAgreement!");
        }
        if (z) {
            this.result = dHPublicKey.getY().modPow(this.x, this.p);
            return null;
        }
        this.result = dHPublicKey.getY().modPow(this.x, this.p);
        return new JCEDHPublicKey(this.result, dHPublicKey.getParams());
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected int engineGenerateSecret(byte[] bArr, int i) throws IllegalStateException, ShortBufferException {
        if (this.x == null) {
            throw new IllegalStateException("Diffie-Hellman not initialised.");
        }
        byte[] bArrBigIntToBytes = bigIntToBytes(this.result);
        if (bArr.length - i < bArrBigIntToBytes.length) {
            throw new ShortBufferException("DHKeyAgreement - buffer too short");
        }
        System.arraycopy(bArrBigIntToBytes, 0, bArr, i, bArrBigIntToBytes.length);
        return bArrBigIntToBytes.length;
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected SecretKey engineGenerateSecret(String str) {
        if (this.x == null) {
            throw new IllegalStateException("Diffie-Hellman not initialised.");
        }
        String upperCase = Strings.toUpperCase(str);
        byte[] bArrBigIntToBytes = bigIntToBytes(this.result);
        Hashtable hashtable = algorithms;
        if (!hashtable.containsKey(upperCase)) {
            return new SecretKeySpec(bArrBigIntToBytes, str);
        }
        int iIntValue = ((Integer) hashtable.get(upperCase)).intValue() / 8;
        byte[] bArr = new byte[iIntValue];
        System.arraycopy(bArrBigIntToBytes, 0, bArr, 0, iIntValue);
        if (upperCase.startsWith("DES")) {
            DESParameters.setOddParity(bArr);
        }
        return new SecretKeySpec(bArr, str);
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected byte[] engineGenerateSecret() throws IllegalStateException {
        if (this.x != null) {
            return bigIntToBytes(this.result);
        }
        throw new IllegalStateException("Diffie-Hellman not initialised.");
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected void engineInit(Key key, SecureRandom secureRandom) throws InvalidKeyException {
        if (!(key instanceof DHPrivateKey)) {
            throw new InvalidKeyException("DHKeyAgreement requires DHPrivateKey");
        }
        DHPrivateKey dHPrivateKey = (DHPrivateKey) key;
        this.random = secureRandom;
        this.p = dHPrivateKey.getParams().getP();
        this.g = dHPrivateKey.getParams().getG();
        BigInteger x = dHPrivateKey.getX();
        this.result = x;
        this.x = x;
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected void engineInit(Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        DHParameterSpec params;
        if (!(key instanceof DHPrivateKey)) {
            throw new InvalidKeyException("DHKeyAgreement requires DHPrivateKey for initialisation");
        }
        DHPrivateKey dHPrivateKey = (DHPrivateKey) key;
        this.random = secureRandom;
        if (algorithmParameterSpec == null) {
            this.p = dHPrivateKey.getParams().getP();
            params = dHPrivateKey.getParams();
        } else {
            if (!(algorithmParameterSpec instanceof DHParameterSpec)) {
                throw new InvalidAlgorithmParameterException("DHKeyAgreement only accepts DHParameterSpec");
            }
            params = (DHParameterSpec) algorithmParameterSpec;
            this.p = params.getP();
        }
        this.g = params.getG();
        BigInteger x = dHPrivateKey.getX();
        this.result = x;
        this.x = x;
    }
}
