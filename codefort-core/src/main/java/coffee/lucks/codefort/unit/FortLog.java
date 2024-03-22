package coffee.lucks.codefort.unit;

import coffee.lucks.codefort.unit.FortBanner;
import coffee.lucks.codefort.unit.PathConst;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FortLog {

    public static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("HH:mm:ss");

    /**
     * 输出debug信息
     *
     * @param msg 信息
     */
    public static void info(Object msg) {
        String def = datetimeFormat.format(new Date());
        if (PathConst.DEBUG) {
            String log = "[" + FortBanner.cyanColor + def + FortBanner.resetColor + "-" + FortBanner.blueColor + "INFO" + FortBanner.resetColor + "] " + msg;
            System.out.println(log);
        }
    }

}
