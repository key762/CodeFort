package coffee.lucks.codefort;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

public class RSAManualKeys {
    public static void main(String[] args) throws Exception {
        // 公钥和私钥（Base64编码的字符串）
        String publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsC8XjlV8kaxg8mVdYfuJ\n" +
                "uMNx7leWMloj/PGhx84FVbaIFFsJei2+/hVAdKkxMH/NpqeW6bDg2W9spfMqx1uq\n" +
                "AtRKr0aIKyBM/V4SC8/9XmIpws0kLAEDGZa28LnUgl40gGuxjhFWvDmkUiv+r1Sr\n" +
                "QYBMFPptGrrSgncLyJBTma5vWoxZrfPmyfuX4vkaqeTkqbIdsw6er8LD9u5TStNp\n" +
                "zrGAP/OnqhhX0I+KbeIPzg+P4kV0m2wodHlG9VQ61zlwG6UGm5JkkXn6vrgbD/Tx\n" +
                "akVW0bqkEtbd/sO01DwUUM3/RPrIwSAjeOoEQPUMAc2EN4B687ZVDKPE2NUXsbAI\n" +
                "eQIDAQAB";
        String privateKeyStr = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCwLxeOVXyRrGDy\n" +
                "ZV1h+4m4w3HuV5YyWiP88aHHzgVVtogUWwl6Lb7+FUB0qTEwf82mp5bpsODZb2yl\n" +
                "8yrHW6oC1EqvRogrIEz9XhILz/1eYinCzSQsAQMZlrbwudSCXjSAa7GOEVa8OaRS\n" +
                "K/6vVKtBgEwU+m0autKCdwvIkFOZrm9ajFmt8+bJ+5fi+Rqp5OSpsh2zDp6vwsP2\n" +
                "7lNK02nOsYA/86eqGFfQj4pt4g/OD4/iRXSbbCh0eUb1VDrXOXAbpQabkmSRefq+\n" +
                "uBsP9PFqRVbRuqQS1t3+w7TUPBRQzf9E+sjBICN46gRA9QwBzYQ3gHrztlUMo8TY\n" +
                "1RexsAh5AgMBAAECggEABSI/fSNFOk1xPxRFu1QuuCvhxwCAXOk+1ouym3WHtu6s\n" +
                "Wm4orDRnC1T9MtO3SOLV0D8JPmKsD/SXkDeyDcBrB3gTe7YCGRebhrUrW3LCaZyn\n" +
                "UN4qE/+CN/L/U6u74yycOl2+UmseQY/VwkXhPvn9n4P93jf0iUHbs8zaPWGPPhFz\n" +
                "G/egqRDD7i23HKo8250B8UAvXtUjznI7rl4yF/2MjXrhUFOCA7dLj6VK57x0agTl\n" +
                "B33wDub9Px3ITneuXooJR3szU4KL5GDX7du6JBiHoaDw/UZVASE9HEA3r+XiENwP\n" +
                "hruVmbdNskF3OkTAmOzQ0RLTPaER83zH3cTpa5FWcQKBgQDWCNMImkDWU8wIheNk\n" +
                "Fs61OFZoF3GBNAhN0IFq2nItmzm0noGPka7XzFcBBgqXIJgPIGLWRY+FWxhynym2\n" +
                "M5rnAT0VcardRU+21XJpQnJbtzlTEhgiSLihDmJRm6n7ViOwfPKNaC0ZFs7B0o5T\n" +
                "TIez4hNLIOESvdi4Ow8xjQ7YDQKBgQDSumlKjKYyEn8a3RrfCM4ItgWbjd8pyFAj\n" +
                "lMTgGkZw1gS29WNNa05LYrRUXFqlbt3TMaWMwEAV+E64VI8gM9pCTCpY6o7ouIRP\n" +
                "E+cvnVbUgVM/u0HcWUGNN4UCgd3uyVDuSiPNbUs877D6hi0j/sTMXyBgPu3NeKd6\n" +
                "MwzsM10LHQKBgQDS2oCkAeqL+qOPZe8c5EchSm39P9Mm+M6Rci58yNUgzsHriReU\n" +
                "C24W1AyGSqBWP9rtU/dqpb59HsAX82rRP0eD+blmjcNJFwYv1VlQDxC//+HuT24N\n" +
                "IF0a9SOwx8yOeU7RiFYfLpj3FXv8f+SMdWFeugJNygdRQkvlOvF93DvaZQKBgAue\n" +
                "BUH74QmvpVw4xkt3c8xdJI//0Ua/aVOc1wG30RxVYCsp+hCBku5rAaAI+2JVa1tC\n" +
                "SGsrCh1r9AMLflx7H1Q6WLdQLxK7YWfuo/cnCGtsuccwrp/UN93uKqIJwM9yP8jz\n" +
                "Q2gG13gDitE05nYujHDr6aAEuB10wl7lQ5gd+MotAoGBAIlxXU8KFfKgvw2FqO/b\n" +
                "+A+GOXBOxKYTQtLl00BQ6V3VN1t7JJp1PY6GrSmBYr7mY5BUszb+3+LXXRCtXgx2\n" +
                "9cilXXj6wPq7dXc7ToOzS7PhNpXAYbqVYBH8sDdNFXxY5X92vkctsx3Npr1DEXSj\n" +
                "fs1YH+FYqTpUs07RXl3APabF";

        // 解码公钥和私钥
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyStr.replace("\n", ""));
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyStr.replace("\n", ""));

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        // 将字节转换为公钥对象
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        // 将字节转换为私钥对象
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        // 初始化Cipher对象进行加密
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        // 要加密的数据
        String plainText = "Hello, RSA!";

        // 加密
        byte[] cipherText = encryptCipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        System.out.println("Encrypted Text: " + Base64.getEncoder().encodeToString(cipherText));

        // 初始化Cipher对象进行解密
        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);

        // 解密
        byte[] decryptedTextBytes = decryptCipher.doFinal(cipherText);
        String decryptedText = new String(decryptedTextBytes, StandardCharsets.UTF_8);
        System.out.println("Decrypted Text: " + decryptedText);
    }
}