package coffee.lucks.codefort.util;

public class StrUtil {

    /**
     * 被检测的字符串是否为空
     *
     * @param str 被检测的字符串
     * @return 是否为空
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

}
