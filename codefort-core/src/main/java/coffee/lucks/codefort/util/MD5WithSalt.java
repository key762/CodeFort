package coffee.lucks.codefort.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class MD5WithSalt {

    // 生成盐值
    public static byte[] createSalt() {
        byte[] bytes = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(bytes);
        return bytes;
    }

    // 生成MD5散列，并添加盐
    public static String generateHashWithSalt(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(salt);
        md.update(password.getBytes());
        byte[] digest = md.digest();
        return Base64.getEncoder().encodeToString(digest);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String password = "password123";
        byte[] salt = createSalt();
        String hashWithSalt = generateHashWithSalt(password, salt);

        // 存储盐和散列后的密码
        System.out.println("Salt: " + Base64.getEncoder().encodeToString(salt));
        System.out.println("Hash with salt: " + hashWithSalt);
    }
}
