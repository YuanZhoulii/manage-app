package com.example.app.utils;

import android.util.Base64;

import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSAUtils {
    //公钥
    private static String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgAY4ApGR9f1Z1C0Xd0bBgq5ynz+W3WPs4VUfjOCaF8bDRTZZEngSWp+Trn7c4jJlmKMIt5dLY4Fd/5h5GcuNr1udZxMnQBYMjb4txANf8BirGDi5OvHao0P3gPCyr1j5b6wE9FwVN8RDxq8TdZyVFUnkzdA2ZdZy18KU+/6d5eQIDAQAB";
    public static PublicKey publicKey;

    static {
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decode(pubKey, Base64.DEFAULT));
            KeyFactory factory = KeyFactory.getInstance("RSA");
            publicKey = factory.generatePublic(x509EncodedKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 使用 公钥加密
     *
     * @param data 被加密数据
     * @param key  秘钥
     * @return 加密后的 Base64 编码的字符串
     */
    public static String encryption(String data) {
        byte[] bytes = encryptAndDecode(data.getBytes(), publicKey, Cipher.ENCRYPT_MODE);
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }


    /**
     * 使用 公钥解密
     *
     * @param data 被解密数据
     * @param key  秘钥
     * @return 解密后的字符串
     */
    public static String decrypt(String data) {
        byte[] bytes = Base64.decode(data.getBytes(), Base64.DEFAULT);
        return new String(encryptAndDecode(bytes, publicKey, Cipher.DECRYPT_MODE));
    }

    private static byte[] encryptAndDecode(byte[] data, Key key, int mode) {
        byte[] bytes = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(mode, key);
            bytes = cipher.doFinal(data);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

}
