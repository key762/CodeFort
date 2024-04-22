package coffee.lucks.codefort.embeds.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.zip.CRC32;

public class SecurityUtil {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     * RSA加密
     *
     * @param key  公钥
     * @param data 源数据
     * @return 加密字节码
     */
    public static String encryptRsa(String key, String data) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(key.replace("\n", ""));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(keyBytes);
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            Cipher encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Base64.getEncoder().encodeToString(encryptCipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * RSA解密
     *
     * @param key  私钥
     * @param data 加密的字节码
     * @return 解密数据
     */
    public static String decryptRsa(String key, byte[] data) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(key.replace("\n", ""));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(keyBytes);
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedTextBytes = decryptCipher.doFinal(data);
            return new String(decryptedTextBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 字符偏移转换
     *
     * @param str 源字符
     * @return 转换后字符
     */
    public static String convertChar(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            sb.append((i % 2 == 0) ? Character.toUpperCase(c) : Character.toLowerCase(c));
        }
        return sb.toString();
    }

    /**
     * MD5加密
     *
     * @param data 源数据
     * @return 加密后字符
     */
    public static byte[] digestMd5(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(convertChar(data).getBytes());
            byte[] digest = md.digest();
            return Base64.getEncoder().encode(digest);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * AES加密
     *
     * @param data   源数据
     * @param secret 密钥
     * @param iv     偏移量
     * @return 字节码
     */
    public static byte[] encryptAes(byte[] data, byte[] secret, byte[] iv) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(Arrays.copyOf(iv, 16));
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encrypted = cipher.doFinal(data);
            return Base64.getEncoder().encode(encrypted);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * AES解密
     *
     * @param data   源数据
     * @param secret 密钥
     * @param iv     偏移量
     * @return 字节码
     */
    public static byte[] decryptAes(byte[] data, byte[] secret, byte[] iv) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(Arrays.copyOf(iv, 16));
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            return cipher.doFinal(Base64.getDecoder().decode(data));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * crc32算法
     *
     * @param bytes 字节码
     * @return 加密值
     */
    public static long crc32(byte[] bytes) {
        CRC32 crc = new CRC32();
        crc.update(bytes);
        return crc.getValue();
    }

    /**
     * 解密
     * @param msg 字节码
     * @param key 密码
     * @return 字节码
     */
    public static byte[] decrypt(byte[] msg, String key) {
        return decryptAes(msg, digestMd5(key), digestMd5(key));
    }

    /**
     * 加密
     * @param bytes 字节码
     * @param password 密码
     * @return 字节码
     */
    public static byte[] encrypt(byte[] bytes, String password) {
        return encryptAes(bytes, digestMd5(password), digestMd5(password));
    }

}
