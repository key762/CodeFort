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
    public static Date parseDateTime(String str) {
        try {
            return simpleDateFormat.parse(str);
        }catch (Exception e){
            return new Date();
        }
    }

    /**
     * 比较时间是否大于本地当前时间
     *
     * @param str 时间字符串
     * @return 如果输入时间大于当前本地时间返回true，否则返回false
     * @throws ParseException 如果时间字符串解析失败
     */
    public static boolean compareLocalTimeAfter(String str) {
        try {
            Date date = simpleDateFormat.parse(str);
            Date currentDate = new Date();
            return date.after(currentDate);
        } catch (Exception e) {
            return true;
        }
    }

    public static void main(String[] args) {
        System.out.println(compareLocalTimeAfter("2024-03-27 09:56:38"));
    }

    /**
     * 比较时间是否小于本地当前时间
     *
     * @param str 时间字符串
     * @return 如果输入时间小于当前本地时间返回true，否则返回false
     */
    public static boolean compareLocalTimeBefore(String str) {
        try {
            Date date = simpleDateFormat.parse(str);
            Date currentDate = new Date();
            return date.before(currentDate);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断当前时间是否在区间内部
     *
     * @param startTime 开始时间字符串
     * @param endTime   结束时间字符串
     * @return 如果当前时间在区间内部返回true，否则返回false
     */
    public static boolean localTimeInRegion(String startTime, String endTime) {
        try {
            Date startDate = simpleDateFormat.parse(startTime);
            Date endDate = simpleDateFormat.parse(endTime);
            Date currentDate = new Date();
            return currentDate.after(startDate) && currentDate.before(endDate);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断当前是否在可用时间内部
     *
     * @param str           编译时间
     * @param availableTime 可用时间 毫秒
     * @return 是否在范围内部 true/false
     */
    public static boolean beforeDeadline(String str, String availableTime) {
        try {
            long all = simpleDateFormat.parse(str).getTime() + Long.parseLong(availableTime);
            long current = new Date().getTime();
            return current <= all;
        } catch (Exception e) {
            return false;
        }
    }

}
