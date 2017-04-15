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
    private final Cipher cipher;

    public AES_CTR_PKCS5Padding() throws NoSuchAlgorithmException, NoSuchPaddingException 
    {
        cipher = Cipher.getInstance(AES_CTR_PKCS5Padding.CIPHER_MODE);
        SecureRandom srandom = SecureRandom.getInstance("SHA1PRNG");
        AES_CTR_PKCS5Padding.IV = new byte[cipher.getBlockSize()];
        srandom.nextBytes(AES_CTR_PKCS5Padding.IV);
    }
    
    public String encrypt(String key, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(AES_CTR_PKCS5Padding.IV);
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            
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

            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}