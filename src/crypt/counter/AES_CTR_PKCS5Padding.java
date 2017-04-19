package crypt.counter;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class AES_CTR_PKCS5Padding 
{
    public static final String CIPHER_ALGORITHM = "AES/CTR/PKCS5PADDING";
    public static Cipher CIPHER = null;
    public static byte[] IV;

    public static void init() throws NoSuchAlgorithmException, NoSuchPaddingException
    {
        assertCIPHER();
        SecureRandom srandom = SecureRandom.getInstance("SHA1PRNG");
        byte[] value = new byte[CIPHER.getBlockSize()];
        AES_CTR_PKCS5Padding.init(value);
        srandom.nextBytes(value);
    }
    
    public static void init(byte[] IV) throws NoSuchAlgorithmException, NoSuchPaddingException {
        assertCIPHER();
        AES_CTR_PKCS5Padding.IV = IV;
    }
    
    private static void assertCIPHER() throws NoSuchAlgorithmException, NoSuchPaddingException 
    {
        if(CIPHER == null)
            CIPHER = Cipher.getInstance(AES_CTR_PKCS5Padding.CIPHER_ALGORITHM);
    }
    
    public static String encrypt(String key, String value) throws Exception
    {
        assertCIPHER();
        IvParameterSpec iv = new IvParameterSpec(AES_CTR_PKCS5Padding.IV);
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        CIPHER.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = CIPHER.doFinal(value.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String key, String encrypted) throws Exception
    {
        assertCIPHER();
        IvParameterSpec iv = new IvParameterSpec(AES_CTR_PKCS5Padding.IV);
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        CIPHER.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] original = CIPHER.doFinal(Base64.getDecoder().decode(encrypted));
        return new String(original);
    }
}