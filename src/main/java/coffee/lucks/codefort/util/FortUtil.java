package coffee.lucks.codefort.util;

import java.io.File;
import java.util.Locale;

public class FortUtil {

    /**
     * jar解压的目录名后缀
     */
    public static final String TEMP_DIR = "_temp";

    /**
     * 校验文件类型
     *
     * @param filePath 文件绝对路径
     */
    public static void checkFileType(String filePath) {
        filePath = filePath.toLowerCase(Locale.ROOT);
        if (!filePath.endsWith(".jar") && !filePath.endsWith(".war")) {
            throw new RuntimeException("文件格式错误(仅支持Jar文件和War文件)");
        }
    }

    /**
     * 校验密码
     *
     * @param password 密码
     */
    public static void checkPassWord(String password) {
        if (password == null || password.isEmpty()) {
            throw new RuntimeException("密码格式错误(密码不能为空)");
        }
    }

    /**
     * 获取文件类型
     *
     * @param filePath 文件绝对路径
     * @return 文件类型
     */
    public static String getFileType(String filePath) {
        return filePath.substring(filePath.lastIndexOf(".") + 1);
    }

    /**
     * 获取临时文件目录
     *
     * @param filePath 文件绝对路径
     * @param fileType 文件类型
     * @return 临时文件目录
     */
    public static String getTempFilePath(String filePath, String fileType) {
        return filePath.replace("." + fileType, TEMP_DIR);
    }

    /**
     * 去除文件最后的/号
     *
     * @param filePath 文件绝对路径
     * @return 处理后的文件路径
     */
    public static String convertPath(String filePath) {
        return filePath.endsWith(File.separator) ? filePath.substring(0, filePath.length() - 1) : filePath;
    }

}
