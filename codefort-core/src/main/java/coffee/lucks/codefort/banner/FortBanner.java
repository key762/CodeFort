package coffee.lucks.codefort.banner;

public class FortBanner {

    /**
     * 青色
     */
    private static final String cyanColor = "\u001B[36m";

    /**
     * 重制颜色
     */
    private static final String resetColor = "\u001B[0m";

    /**
     * 打印CodeFort的Banner
     */
    public static void banner() {
        System.out.println(
                cyanColor +
                "  _____        __    ____         __ \n" +
                " / ___/__  ___/ /__ / __/__  ____/ /_\n" +
                "/ /__/ _ \\/ _  / -_) _// _ \\/ __/ __/\n" +
                "\\___/\\___/\\_,_/\\__/_/  \\___/_/  \\__/ \n" +
                resetColor
        );
    }

}
