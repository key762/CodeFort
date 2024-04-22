package coffee.lucks.codefort.embeds.arms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StrArm {

    /**
     * 字符常量：斜杠
     */
    public static char SLASH = '/';

    /**
     * 字符常量：反斜杠
     */
    public static char BACKSLASH = '\\';

    /**
     * 被检测的字符串是否为空
     *
     * @param str 被检测的字符串
     * @return 是否为空
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * 特定分割的字符串转List
     *
     * @param str   原始字符串
     * @param regex 分割字符
     * @return 字符串数组
     */
    public static List<String> toListByRegex(String str, String regex) {
        List<String> list = new ArrayList<>();
        if (str != null && !str.isEmpty()) {
            list = Arrays.stream(str.split(regex)).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
        }
        return list;
    }

    /**
     * 是否以指定字符串结尾，忽略大小写
     *
     * @param str    被监测字符串
     * @param suffix 结尾字符串
     * @return 是否以指定字符串结尾
     */
    public static boolean endWithIgnoreCase(CharSequence str, CharSequence suffix) {
        return endWith(str, suffix, true, false);
    }

    /**
     * 是否以指定字符串结尾
     *
     * @param str          被监测字符串
     * @param suffix       结尾字符串
     * @param ignoreCase   是否忽略大小写
     * @param ignoreEquals 是否忽略字符串相等的情况
     * @return 是否以指定字符串结尾
     */
    public static boolean endWith(CharSequence str, CharSequence suffix, boolean ignoreCase, boolean ignoreEquals) {
        if (null == str || null == suffix) {
            if (ignoreEquals) {
                return false;
            }
            return null == str && null == suffix;
        }
        final int strOffset = str.length() - suffix.length();
        boolean isEndWith = str.toString().regionMatches(ignoreCase, strOffset, suffix.toString(), 0, suffix.length());
        if (isEndWith) {
            return (!ignoreEquals) || (!equals(str, suffix, ignoreCase));
        }
        return false;
    }

    /**
     * 比较两个字符串是否相等
     *
     * @param str1       要比较的字符串1
     * @param str2       要比较的字符串2
     * @param ignoreCase 是否忽略大小写
     * @return 如果两个字符串相同 true 否则 false
     */
    public static boolean equals(CharSequence str1, CharSequence str2, boolean ignoreCase) {
        if (null == str1) {
            return str2 == null;
        }
        if (null == str2) {
            return false;
        }
        if (ignoreCase) {
            return str1.toString().equalsIgnoreCase(str2.toString());
        } else {
            return str1.toString().contentEquals(str2);
        }
    }

    /**
     * 给定字符串是否以任何一个字符串结尾（忽略大小写
     * 给定字符串和数组为空都返回false
     *
     * @param str      给定字符串
     * @param suffixes 需要检测的结尾字符串
     * @return 给定字符串是否以任何一个字符串结尾
     */
    public static boolean endWithAnyIgnoreCase(CharSequence str, CharSequence... suffixes) {
        if (isEmpty(str) || suffixes == null || suffixes.length == 0) {
            return false;
        }
        for (CharSequence suffix : suffixes) {
            if (endWith(str, suffix, true)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否以指定字符串结尾
     * 如果给定的字符串和开头字符串都为null则返回true，否则任意一个值为null返回false
     *
     * @param str        被监测字符串
     * @param suffix     结尾字符串
     * @param ignoreCase 是否忽略大小写
     * @return 是否以指定字符串结尾
     */
    public static boolean endWith(CharSequence str, CharSequence suffix, boolean ignoreCase) {
        return endWith(str, suffix, ignoreCase, false);
    }

    /**
     * 是否为Windows或者Linux（Unix）文件分隔符
     * Windows平台下分隔符为\，Linux（Unix）为/
     *
     * @param c 字符
     * @return 是否为Windows或者Linux（Unix）文件分隔符
     */
    public static boolean isFileSeparator(char c) {
        return SLASH == c || BACKSLASH == c;
    }

    /**
     * 字符串是否包含集合中的任意字符
     *
     * @param str   字符串
     * @param array 字符数组
     * @return true/false
     */
    public static boolean containsArray(String str, String[] array) {
        for (String e : array) {
            if (str.contains(e)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 在字符串的某个位置插入字符串
     *
     * @param arrayStr  字符串数组
     * @param insertStr 要插入的字串
     * @param pos       位置开始标识
     * @return 插入后的字串
     */
    public static String insertStringArray(String[] arrayStr, String insertStr, String pos) {
        StringBuffer newStr = new StringBuffer();
        boolean isInsert = false;
        for (int i = 0; i < arrayStr.length; i++) {
            newStr.append(arrayStr[i]).append("\r\n");
            if (arrayStr[i].startsWith(pos)) {
                newStr.append(insertStr).append("\r\n");
                isInsert = true;
            }
        }
        if (!isInsert) {
            newStr.append(insertStr).append("\r\n");
        }
        return newStr.toString();
    }

    /**
     * 判断集合是否包含字符
     *
     * @param strings 集合
     * @param str     字符
     * @return true/false
     */
    public static boolean isMatch(List<String> strings, String str) {
        if (strings == null || strings.isEmpty()) {
            return false;
        }
        for (String s : strings) {
            if (str.startsWith(s) || str.endsWith(s) || isMatch(s, str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 通配符匹配
     *
     * @param s   匹配字符串
     * @param str 待匹配字符窜
     * @return
     */
    public static boolean isMatch(String s, String str) {
        String regex = s.replaceAll("\\?", "(.?)")
                .replaceAll("\\*+", "(.*?)");
        return Pattern.matches(regex, str);
    }

}
