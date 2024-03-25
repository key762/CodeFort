package coffee.lucks.codefort.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import javax.crypto.spec.IvParameterSpec;

public class AESUtil {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    public static byte[] encrypt(byte[] data, byte[] secret, byte[] iv) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encrypted = cipher.doFinal(data);
            return Base64.getEncoder().encode(encrypted);
        }catch (Exception e){
            return null;
        }
    }

    public static byte[] decrypt(byte[] data, byte[] secret, byte[] iv) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            return cipher.doFinal(Base64.getDecoder().decode(data));
        }catch (Exception e){
            return null;
        }
    }

}