package coffee.lucks.codefort.arms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateArm {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String formatDateTime(Date date) {
        return simpleDateFormat.format(date);
    }

    public static Date parseDateTime(String str) throws ParseException {
        return simpleDateFormat.parse(str);
    }

}
