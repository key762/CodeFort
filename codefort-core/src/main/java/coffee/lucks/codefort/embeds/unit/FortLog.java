package coffee.lucks.codefort.embeds.unit;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FortLog {

    public static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("HH:mm:ss.SSS");

    /**
     * 输出debug信息
     *
     * @param msg 信息
     */
    public static void debug(Object msg) {
        String def = datetimeFormat.format(new Date());
        if (PathConst.DEBUG) {
            String log = "[" + FortBanner.cyanColor + def + FortBanner.resetColor + "-" + FortBanner.blueColor + "DEBUG" + FortBanner.resetColor + "] " + msg;
            System.out.println(log);
        }
    }


    /**
     * 输出info信息
     *
     * @param msg 信息
     */
    public static void info(Object msg) {
        System.out.println(infoStr(msg));
    }

    /**
     * 获取info信息
     *
     * @param msg 信息
     */
    public static String infoStr(Object msg) {
        String def = datetimeFormat.format(new Date());
        return "[" + FortBanner.cyanColor + def + FortBanner.resetColor + "-" + FortBanner.blueColor + "INFO" + FortBanner.resetColor + "] " + msg;
    }

}
