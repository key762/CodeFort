package coffee.lucks.codefort.embeds.util;

import java.security.MessageDigest;
import java.util.Base64;

public class MD5Util {

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
    public static byte[] digest(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(convertChar(data).getBytes());
            byte[] digest = md.digest();
            return Base64.getEncoder().encode(digest);
        } catch (Exception e) {
            return null;
        }
    }

}
