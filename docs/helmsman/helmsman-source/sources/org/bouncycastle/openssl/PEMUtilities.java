package org.bouncycastle.openssl;

import java.io.IOException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.RC2ParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.generators.OpenSSLPBEParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;

/* JADX INFO: loaded from: classes2.dex */
final class PEMUtilities {
    PEMUtilities() {
    }

    static byte[] crypt(boolean z, String str, byte[] bArr, char[] cArr, String str2, byte[] bArr2) throws IOException {
        String str3;
        String str4;
        String str5;
        SecretKey key;
        AlgorithmParameterSpec ivParameterSpec = new IvParameterSpec(bArr2);
        String str6 = "NoPadding";
        if (str2.endsWith("-CFB")) {
            str3 = "CFB";
            str4 = "NoPadding";
        } else {
            str3 = "CBC";
            str4 = "PKCS5Padding";
        }
        if (str2.endsWith("-ECB") || "DES-EDE".equals(str2) || "DES-EDE3".equals(str2)) {
            str3 = "ECB";
            ivParameterSpec = null;
        }
        if (str2.endsWith("-OFB")) {
            str3 = "OFB";
        } else {
            str6 = str4;
        }
        int i = 1;
        if (str2.startsWith("DES-EDE")) {
            str5 = "DESede";
            key = getKey(cArr, "DESede", 24, bArr2, !str2.startsWith("DES-EDE3"));
        } else if (str2.startsWith("DES-")) {
            key = getKey(cArr, "DES", 8, bArr2);
            str5 = "DES";
        } else if (str2.startsWith("BF-")) {
            str5 = "Blowfish";
            key = getKey(cArr, "Blowfish", 16, bArr2);
        } else {
            int i2 = 128;
            if (str2.startsWith("RC2-")) {
                if (str2.startsWith("RC2-40-")) {
                    i2 = 40;
                } else if (str2.startsWith("RC2-64-")) {
                    i2 = 64;
                }
                str5 = "RC2";
                key = getKey(cArr, "RC2", i2 / 8, bArr2);
                ivParameterSpec = ivParameterSpec == null ? new RC2ParameterSpec(i2) : new RC2ParameterSpec(i2, bArr2);
            } else {
                if (!str2.startsWith("AES-")) {
                    throw new EncryptionException("unknown encryption with private key");
                }
                if (bArr2.length > 8) {
                    byte[] bArr3 = new byte[8];
                    System.arraycopy(bArr2, 0, bArr3, 0, 8);
                    bArr2 = bArr3;
                }
                if (!str2.startsWith("AES-128-")) {
                    if (str2.startsWith("AES-192-")) {
                        i2 = 192;
                    } else {
                        if (!str2.startsWith("AES-256-")) {
                            throw new EncryptionException("unknown AES encryption with private key");
                        }
                        i2 = 256;
                    }
                }
                str5 = "AES";
                key = getKey(cArr, "AES", i2 / 8, bArr2);
            }
        }
        try {
            Cipher cipher = Cipher.getInstance(str5 + "/" + str3 + "/" + str6, str);
            if (!z) {
                i = 2;
            }
            if (ivParameterSpec == null) {
                cipher.init(i, key);
            } else {
                cipher.init(i, key, ivParameterSpec);
            }
            return cipher.doFinal(bArr);
        } catch (Exception e) {
            throw new EncryptionException("exception using cipher - please check password and data.", e);
        }
    }

    private static SecretKey getKey(char[] cArr, String str, int i, byte[] bArr) {
        return getKey(cArr, str, i, bArr, false);
    }

    private static SecretKey getKey(char[] cArr, String str, int i, byte[] bArr, boolean z) {
        OpenSSLPBEParametersGenerator openSSLPBEParametersGenerator = new OpenSSLPBEParametersGenerator();
        openSSLPBEParametersGenerator.init(PBEParametersGenerator.PKCS5PasswordToBytes(cArr), bArr);
        byte[] key = ((KeyParameter) openSSLPBEParametersGenerator.generateDerivedParameters(i * 8)).getKey();
        if (z && key.length >= 24) {
            System.arraycopy(key, 0, key, 16, 8);
        }
        return new SecretKeySpec(key, str);
    }
}
