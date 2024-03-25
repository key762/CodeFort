package coffee.lucks.codefort.embeds.arms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateArm {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 时间字符串化
     *
     * @param date 时间对象
     * @return 时间字符串
     */
    public static String formatDateTime(Date date) {
        return simpleDateFormat.format(date);
    }

    /**
     * 解析字符串时间对象
     *
     * @param str 时间字符串
     * @return 时间对象
     * @throws ParseException
     */
    public static Date parseDateTime(String str) throws ParseException {
        return simpleDateFormat.parse(str);
    }

}
