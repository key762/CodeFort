package coffee.lucks.codefort.embeds.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.spec.IvParameterSpec;

public class AESUtil {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     * AES加密
     *
     * @param data   源数据
     * @param secret 密钥
     * @param iv     偏移量
     * @return 字节码
     */
    public static byte[] encrypt(byte[] data, byte[] secret, byte[] iv) {
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
    public static byte[] decrypt(byte[] data, byte[] secret, byte[] iv) {
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

}