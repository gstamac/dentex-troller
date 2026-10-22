package im.helmsman.helmsmanandroid.utils;

import android.util.Base64;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import javax.crypto.Cipher;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure;

/* JADX INFO: loaded from: classes2.dex */
public class RSAUtils {
    private static String RSA = "RSA";

    public static byte[] decryptData(byte[] bArr, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(2, privateKey);
            return cipher.doFinal(bArr);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("出错了", "出错了");
            return null;
        }
    }

    public static PrivateKey loadPrivateKey(String str) throws Exception {
        try {
            RSAPrivateKeyStructure rSAPrivateKeyStructure = new RSAPrivateKeyStructure((ASN1Sequence) ASN1Sequence.fromByteArray(Base64.decode(str, 0)));
            return (RSAPrivateKey) KeyFactory.getInstance(RSA).generatePrivate(new RSAPrivateKeySpec(rSAPrivateKeyStructure.getModulus(), rSAPrivateKeyStructure.getPrivateExponent()));
        } catch (NullPointerException unused) {
            throw new Exception("私钥数据为空");
        } catch (NoSuchAlgorithmException unused2) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException unused3) {
            throw new Exception("私钥非法");
        }
    }

    public static PrivateKey loadPrivateKey(InputStream inputStream) throws Exception {
        try {
            return loadPrivateKey(readKey(inputStream));
        } catch (IOException unused) {
            throw new Exception("私钥数据读取错误");
        } catch (NullPointerException unused2) {
            throw new Exception("私钥输入流为空");
        }
    }

    private static String readKey(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        while (true) {
            String line = bufferedReader.readLine();
            if (line != null) {
                if (line.charAt(0) != '-') {
                    sb.append(line);
                    sb.append('\r');
                }
            } else {
                return sb.toString();
            }
        }
    }
}
