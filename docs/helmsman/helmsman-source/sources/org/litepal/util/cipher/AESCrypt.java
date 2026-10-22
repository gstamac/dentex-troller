package org.litepal.util.cipher;

import android.util.Base64;
import android.util.Log;
import com.facebook.share.internal.ShareConstants;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import kotlin.UByte;

/* JADX INFO: loaded from: classes2.dex */
public final class AESCrypt {
    private static final String AES_MODE = "AES/CBC/PKCS7Padding";
    private static final String CHARSET = "UTF-8";
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final String TAG = "AESCrypt";
    private static final byte[] ivBytes = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public static boolean DEBUG_LOG_ENABLED = false;

    private static SecretKeySpec generateKey(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
        byte[] bytes = str.getBytes("UTF-8");
        messageDigest.update(bytes, 0, bytes.length);
        byte[] bArrDigest = messageDigest.digest();
        log("SHA-256 key ", bArrDigest);
        return new SecretKeySpec(bArrDigest, "AES");
    }

    public static String encrypt(String str, String str2) throws GeneralSecurityException {
        try {
            SecretKeySpec secretKeySpecGenerateKey = generateKey(str);
            log(ShareConstants.WEB_DIALOG_PARAM_MESSAGE, str2);
            String strEncodeToString = Base64.encodeToString(encrypt(secretKeySpecGenerateKey, ivBytes, str2.getBytes("UTF-8")), 2);
            log("Base64.NO_WRAP", strEncodeToString);
            return strEncodeToString;
        } catch (UnsupportedEncodingException e) {
            if (DEBUG_LOG_ENABLED) {
                Log.e(TAG, "UnsupportedEncodingException ", e);
            }
            throw new GeneralSecurityException(e);
        }
    }

    public static byte[] encrypt(SecretKeySpec secretKeySpec, byte[] bArr, byte[] bArr2) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(AES_MODE);
        cipher.init(1, secretKeySpec, new IvParameterSpec(bArr));
        byte[] bArrDoFinal = cipher.doFinal(bArr2);
        log("cipherText", bArrDoFinal);
        return bArrDoFinal;
    }

    public static String decrypt(String str, String str2) throws GeneralSecurityException {
        try {
            SecretKeySpec secretKeySpecGenerateKey = generateKey(str);
            log("base64EncodedCipherText", str2);
            byte[] bArrDecode = Base64.decode(str2, 2);
            log("decodedCipherText", bArrDecode);
            byte[] bArrDecrypt = decrypt(secretKeySpecGenerateKey, ivBytes, bArrDecode);
            log("decryptedBytes", bArrDecrypt);
            String str3 = new String(bArrDecrypt, "UTF-8");
            log(ShareConstants.WEB_DIALOG_PARAM_MESSAGE, str3);
            return str3;
        } catch (UnsupportedEncodingException e) {
            if (DEBUG_LOG_ENABLED) {
                Log.e(TAG, "UnsupportedEncodingException ", e);
            }
            throw new GeneralSecurityException(e);
        }
    }

    public static byte[] decrypt(SecretKeySpec secretKeySpec, byte[] bArr, byte[] bArr2) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(AES_MODE);
        cipher.init(2, secretKeySpec, new IvParameterSpec(bArr));
        byte[] bArrDoFinal = cipher.doFinal(bArr2);
        log("decryptedBytes", bArrDoFinal);
        return bArrDoFinal;
    }

    private static void log(String str, byte[] bArr) {
        if (DEBUG_LOG_ENABLED) {
            Log.d(TAG, str + "[" + bArr.length + "] [" + bytesToHex(bArr) + "]");
        }
    }

    private static void log(String str, String str2) {
        if (DEBUG_LOG_ENABLED) {
            Log.d(TAG, str + "[" + str2.length() + "] [" + str2 + "]");
        }
    }

    private static String bytesToHex(byte[] bArr) {
        char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] cArr2 = new char[bArr.length * 2];
        for (int i = 0; i < bArr.length; i++) {
            int i2 = bArr[i] & UByte.MAX_VALUE;
            int i3 = i * 2;
            cArr2[i3] = cArr[i2 >>> 4];
            cArr2[i3 + 1] = cArr[i2 & 15];
        }
        return new String(cArr2);
    }

    private AESCrypt() {
    }
}
