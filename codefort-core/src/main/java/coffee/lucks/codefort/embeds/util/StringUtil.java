package coffee.lucks.codefort.embeds.util;

import coffee.lucks.codefort.embeds.arms.StrArm;
import coffee.lucks.codefort.embeds.unit.Guarder;
import coffee.lucks.codefort.embeds.unit.FortConst;
import coffee.lucks.codefort.embeds.unit.FileType;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

public class StringUtil {

    /**
     * 校验文件路径并返回文件类型
     *
     * @param filePath 文件路径
     * @return 文件类型枚举
     */
    public static FileType getFileType(String filePath) {
        if (StrArm.isEmpty(filePath)) {
            throw new RuntimeException("文件格式错误(仅支持Jar文件和War文件)");
        }
        if (StrArm.endWithIgnoreCase(filePath, FileType.JAR.getFullType())) {
            return FileType.JAR;
        } else if (StrArm.endWithIgnoreCase(filePath, FileType.WAR.getFullType())) {
            return FileType.WAR;
        } else {
            throw new RuntimeException("文件格式错误(仅支持Jar文件和War文件)");
        }
    }

    /**
     * 判断class是否需要加密
     *
     * @param file    文件路径
     * @param guarder 执行对象
     * @return true/false
     */
    public static boolean needEncrypt(String file, Guarder guarder) {
        if (StrArm.isEmpty(guarder.getPackages()) && guarder.getLibJarNames().isEmpty()) {
            return false;
        }
        String clsName = StringUtil.resolveClassPath(file, true);
        List<String> packages = Arrays.asList(guarder.getPackages().split(","));
        List<String> libJarName = guarder.getLibJarNames();
        if (StrArm.isMatch(packages, clsName) || StrArm.isMatch(libJarName, clsName)) {
            return StrArm.isEmpty(guarder.getExcludes()) || !guarder.getExcludes().contains(clsName);
        }
        return false;
    }

    /**
     * 判断class是否需要加密(依赖包)
     *
     * @param file    文件路径
     * @param guarder 执行对象
     * @return true/false
     */
    public static boolean needEncryptLib(String file, Guarder guarder) {
        if (guarder.getLibJarNames().isEmpty()) {
            return false;
        }
        for (String libJarName : guarder.getLibJarNames()) {
            if (file.contains(libJarName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否无用文件
     *
     * @param filePath 文件路径
     * @return 是否
     */
    public static boolean isDel(String filePath) {
        return StrArm.endWithAnyIgnoreCase(filePath, ".DS_Store", "Thumbs.db");
    }

    /**
     * 获取临时文件目录
     *
     * @param filePath 文件绝对路径
     * @param fileType 文件类型
     * @return 临时文件目录
     */
    public static String getTmpDirPath(String filePath, FileType fileType) {
        return filePath.replace(fileType.getFullType(), FortConst.TEMP_DIR);
    }

    /**
     * 获取class运行的classes目录或所在的jar包目录
     *
     * @return 路径字符串
     */
    public static String getRootPath(String path) {
        if (path == null) {
            path = StringUtil.class.getResource("").getPath();
        }
        try {
            path = java.net.URLDecoder.decode(path, "utf-8");
        } catch (UnsupportedEncodingException ignored) {
        }
        if (path.startsWith("jar:") || path.startsWith("war:")) {
            path = path.substring(4);
        }
        if (path.startsWith("file:")) {
            path = path.substring(5);
        }
        if (path.contains("*")) {
            return path.substring(0, path.indexOf("*"));
        } else if (path.contains("WEB-INF")) {
            return path.substring(0, path.indexOf("WEB-INF"));
        } else if (path.contains("!")) {
            return path.substring(0, path.indexOf("!"));
        } else if (path.endsWith(".jar") || path.endsWith(".war")) {
            return path;
        } else if (path.contains("/classes/")) {
            return path.substring(0, path.indexOf("/classes/") + 9);
        }
        return null;
    }

    /**
     * 根据class的绝对路径解析出class名称或class包所在的路径
     *
     * @param fileName class绝对路径
     * @param flag     true|false
     * @return class名称|包所在的路径
     */
    public static String resolveClassPath(String fileName, boolean flag) {
        String K_LIB = File.separator + "lib" + File.separator;
        String K_CLASSES = File.separator + "classes" + File.separator;
        String file = fileName.substring(0, fileName.length() - 6);
        String clsPath;
        String clsName;
        if (file.contains(K_LIB)) {
            clsName = file.substring(file.indexOf(FortConst.TEMP_DIR, file.indexOf(K_LIB)) + FortConst.TEMP_DIR.length() + 1);
            clsPath = file.substring(0, file.length() - clsName.length() - 1);
        } else if (file.contains(K_CLASSES)) {
            clsName = file.substring(file.indexOf(K_CLASSES) + K_CLASSES.length());
            clsPath = file.substring(0, file.length() - clsName.length() - 1);
        } else {
            clsName = file.substring(file.indexOf(FortConst.TEMP_DIR) + FortConst.TEMP_DIR.length() + 1);
            clsPath = file.substring(0, file.length() - clsName.length() - 1);
        }
        return flag ? clsName.replace(File.separator, ".") : clsPath;
    }

}
