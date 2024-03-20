package coffee.lucks.codefort.util;

import cn.hutool.core.util.StrUtil;
import coffee.lucks.codefort.consts.PathConst;
import coffee.lucks.codefort.enums.FileType;

import java.io.File;

public class StringUtil {

    /**
     * 校验文件路径并返回文件类型
     *
     * @param filePath 文件路径
     * @return 文件类型枚举
     */
    public static FileType getFileType(String filePath) {
        if (StrUtil.isEmpty(filePath)) {
            throw new RuntimeException("文件格式错误(仅支持Jar文件和War文件)");
        }
        if (StrUtil.endWithIgnoreCase(filePath, FileType.JAR.getFullType())) {
            return FileType.JAR;
        } else if (StrUtil.endWithIgnoreCase(filePath, FileType.WAR.getFullType())) {
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
        return StrUtil.isEmpty(str) ? def : str;
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
        if (StrUtil.isEmpty(packageName)) {
            return false;
        }
        String[] packages = packageName.split(",");
        if (StrUtil.startWithAny(className, packages)) {
            return StrUtil.isEmpty(excludeClass) || !excludeClass.contains(className);
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
        if (StrUtil.isEmpty(className)) {
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
        return StrUtil.endWithAnyIgnoreCase(filePath, ".DS_Store", "Thumbs.db");
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

}
