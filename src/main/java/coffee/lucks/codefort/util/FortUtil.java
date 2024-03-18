package coffee.lucks.codefort.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import javassist.ClassPool;
import javassist.NotFoundException;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FortUtil {

    /**
     * jar
     */
    public static final String JAR = "jar";

    /**
     * war
     */
    public static final String WAR = "jar";

    /**
     * jar解压的目录名后缀
     */
    public static final String TEMP_DIR = "_temp";

    /**
     * class加密后的文件
     */
    public static final String ENCRYPT_NAME = "classes.dat";

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

    /**
     * 获取需要加密的class文件
     *
     * @param allFilePath  所有的文件路径
     * @param filePath     文件路径
     * @param packages     需加密包路径
     * @param excludeClass 排除的类路径
     * @return 需加密的class集合
     */
    public static Map<String, List<String>> getEncryptClass(List<String> allFilePath, String filePath, String packages, String excludeClass) {
        Map<String, List<String>> jarClasses = new HashMap<>();
        for (String file : allFilePath) {
            if (!file.endsWith(FileNameUtil.EXT_CLASS)) {
                continue;
            }
            file = file.replace(filePath, "");
            file = file.startsWith(File.separator) ? file.substring(1) : file;
            file = file.replace(File.separator, ".");
            file = file.substring(0, file.length() - 6);
            String jarName;
            String clsName;
            if ((file.contains("BOOT-INF.lib") || file.contains("WEB-INF.lib")) && file.contains(FortUtil.TEMP_DIR)) {
                file = file.replace("BOOT-INF.lib.", "").replace("WEB-INF.lib.", "");
                jarName = file.substring(0, file.indexOf(FortUtil.TEMP_DIR));
                clsName = file.substring(file.indexOf(FortUtil.TEMP_DIR) + FortUtil.TEMP_DIR.length() + 1);
            } else if (file.contains("BOOT-INF.classes") || file.contains("WEB-INF.classes")) {
                file = file.replace("BOOT-INF.classes.", "").replace("WEB-INF.classes.", "");
                jarName = "CLASSES";
                clsName = file;
            } else {
                jarName = "ROOT";
                clsName = file;
            }
            if (isPackage(packages, clsName) && (excludeClass == null || excludeClass.isEmpty() || !excludeClass.contains(clsName))) {
                jarClasses.computeIfAbsent(jarName, k -> new ArrayList<>()).add(clsName);
            }
        }
        return jarClasses;
    }

    /**
     * 判断类是否在路径下
     *
     * @param encryptPackage 包路径
     * @param className      类路径
     * @return 判断类是否在路径下
     */
    public static boolean isPackage(String encryptPackage, String className) {
        if (encryptPackage == null || encryptPackage.isEmpty()) {
            return false;
        }
        String[] packages = encryptPackage.split(",");
        for (String pkg : packages) {
            if (className.startsWith(pkg)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据类名和jar名还原真实路径
     *
     * @param key       class 类型
     * @param className 类名
     * @param fileType  原文件类型
     * @return 真实路径
     */
    public static String realPath(String key, String className, String fileType) {
        String INF = JAR.equals(fileType) ? "BOOT-INF" : "WEB-INF";
        String path;
        if ("ROOT".equals(key)) {
            path = "";
        } else if ("CLASSES".equals(key)) {
            path = INF + File.separator + "classes";
        } else {
            path = INF + File.separator + "lib" + File.separator + key + TEMP_DIR;
        }
        if (className == null || className.isEmpty()) {
            return path;
        }
        path = path + (path.isEmpty() ? "" : File.separator) + className.replace(".", File.separator) + ".class";
        return path;
    }

    /**
     * 加密class文件
     * @param jarClasses 需加密的class集合
     * @param tempFilePath 临时文件路径
     * @param fileType 文件类型
     * @param password 加密密码
     */
    public static void encryptClass(Map<String, List<String>> jarClasses, String tempFilePath, String fileType,  String password) {
        try (FileOutputStream out = new FileOutputStream(tempFilePath + File.separator + FortUtil.ENCRYPT_NAME);
             ZipOutputStream zos = new ZipOutputStream(out)) {
            for (Map.Entry<String, List<String>> entry : jarClasses.entrySet()) {
                for (String classname : entry.getValue()) {
                    String classPath = tempFilePath + File.separator + FortUtil.realPath(entry.getKey(), classname, fileType);
                    zos.putNextEntry(new ZipEntry(classname));
                    byte[] bytes = FileUtil.readBytes(classPath);
                    bytes = EncryptUtil.encrypt(bytes, password);
                    zos.write(bytes, 0, bytes.length);
                    zos.closeEntry();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
