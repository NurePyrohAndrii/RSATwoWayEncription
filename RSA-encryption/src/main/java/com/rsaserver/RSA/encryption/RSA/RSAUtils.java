package com.rsaserver.RSA.encryption.RSA;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class RSAUtils {

    public static String decrypt(String encryptedData, Key privateKey) {
        try {
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encrypt(String data, Key key) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static PublicKey getPublicKeyFromPem(String publicKeyPem) throws Exception {
        String publicKeyPEM = publicKeyPem.substring(RSAKey.publicKeyHeader.length(), publicKeyPem.length() - RSAKey.publicKeyFooter.length());
        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        return keyFactory.generatePublic(keySpec);
    }

    public static String getKey(Key key, RSAKeyType type) {
        String publicKeyBody = Base64.getEncoder().encodeToString(key.getEncoded());
        return new RSAKey(publicKeyBody, type).getKey();
    }
}
