package coffee.lucks.codefort.util;

import coffee.lucks.codefort.arms.StrArm;
import coffee.lucks.codefort.unit.PathConst;
import coffee.lucks.codefort.unit.FileType;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;

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
     * 校验字符串并异常时返回默认值
     *
     * @param str 字符串
     * @param def 默认值
     * @return 字符串/默认值
     */
    public static String checkStrWithDef(String str, String def) {
        return StrArm.isEmpty(str) ? def : str;
    }

    /**
     * 判断class是否需要加密
     *
     * @param packageName  包路径
     * @param className    类路径
     * @param excludeClass 排除的类信息
     * @return 是否加密
     */
    public static boolean needEncrypt(String packageName, String className, String excludeClass) {
        if (StrArm.isEmpty(packageName)) {
            return false;
        }
        String[] packages = packageName.split(",");
        if (StrArm.startWithAny(className, packages)) {
            return StrArm.isEmpty(excludeClass) || !excludeClass.contains(className);
        }
        return false;
    }

    /**
     * 还原真实路径
     *
     * @param key       class类型
     * @param className 类名
     * @param fileType  文件类型
     * @return 真实路径
     */
    public static String getRealPath(String key, String className, FileType fileType) {
        String INF = fileType.getMark();
        String path;
        if ("ROOT".equals(key)) {
            path = "";
        } else if ("CLASSES".equals(key)) {
            path = INF + File.separator + "classes";
        } else {
            path = INF + File.separator + "lib" + File.separator + key + PathConst.TEMP_DIR;
        }
        if (StrArm.isEmpty(className)) {
            return path;
        }
        path = path + (path.isEmpty() ? "" : File.separator) + className.replace(".", File.separator) + PathConst.EXT_CLASS;
        return path;
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
        return filePath.replace(fileType.getFullType(), PathConst.TEMP_DIR);
    }

    /**
     * 获取字符分割集合
     *
     * @param libs 字符串
     * @return 集合
     */
    public static List<String> getList(String libs) {
        if (StrArm.isEmpty(libs)) {
            return new ArrayList<>();
        }
        return Arrays.asList(libs.split(","));
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
     * @param fileName    class绝对路径
     * @param classOrPath true|false
     * @return class名称|包所在的路径
     */
    public static String resolveClassPath(String fileName, boolean classOrPath) {
        String K_LIB = File.separator + "lib" + File.separator;
        String K_CLASSES = File.separator + "classes" + File.separator;
        String file = fileName.substring(0, fileName.length() - 6);
        String clsPath;
        String clsName;
        if (file.contains(K_LIB)) {
            clsName = file.substring(file.indexOf(PathConst.TEMP_DIR, file.indexOf(K_LIB)) + PathConst.TEMP_DIR.length() + 1);
            clsPath = file.substring(0, file.length() - clsName.length() - 1);
        } else if (file.contains(K_CLASSES)) {
            clsName = file.substring(file.indexOf(K_CLASSES) + K_CLASSES.length());
            clsPath = file.substring(0, file.length() - clsName.length() - 1);
        } else {
            clsName = file.substring(file.indexOf(PathConst.TEMP_DIR) + PathConst.TEMP_DIR.length() + 1);
            clsPath = file.substring(0, file.length() - clsName.length() - 1);
        }
        return classOrPath ? clsName.replace(File.separator, ".") : clsPath;
    }


}
