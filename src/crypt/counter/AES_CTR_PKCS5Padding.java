package crypt.counter;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class AES_CTR_PKCS5Padding {

    public static final String CIPHER_MODE = "AES/CTR/PKCS5PADDING";
    public static byte[] IV;

    public AES_CTR_PKCS5Padding() throws NoSuchAlgorithmException, NoSuchPaddingException {
        SecureRandom srandom = SecureRandom.getInstance("SHA1PRNG");
        AES_CTR_PKCS5Padding.IV = new byte[chiper().getBlockSize()];
        srandom.nextBytes(AES_CTR_PKCS5Padding.IV);
    }
    
    public Cipher chiper() throws NoSuchAlgorithmException, NoSuchPaddingException {
        return Cipher.getInstance(AES_CTR_PKCS5Padding.CIPHER_MODE);
    }
    
    public String encrypt(String key, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(AES_CTR_PKCS5Padding.IV);
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            chiper().init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = chiper().doFinal(value.getBytes());
            
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public String decrypt(String key, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(AES_CTR_PKCS5Padding.IV);
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            chiper().init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = chiper().doFinal(Base64.getDecoder().decode(encrypted));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}