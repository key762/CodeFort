package coffee.lucks.codefort.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import coffee.lucks.codefort.unit.PathConst;
import coffee.lucks.codefort.unit.FileType;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HandleUtil {

    /**
     * 解压Jar/War执行文件
     *
     * @param filePath     文件路径
     * @param targetDir    目标路径
     * @param includeFiles 文件lib解压名称
     * @return 所有解压文件绝对路径
     */
    public static List<String> decompression(String filePath, String targetDir, List<String> includeFiles) {
        List<String> list = new ArrayList<>();
        try (FileInputStream fin = new FileInputStream(filePath);
             JarArchiveInputStream jis = new JarArchiveInputStream(fin)) {
            JarArchiveEntry jarEntry;
            while ((jarEntry = jis.getNextEntry()) != null) {
                String entryName = jarEntry.getName();
                String fullPath = targetDir + File.separator + entryName;
                list.add(fullPath);
                if (entryName.endsWith(FileType.JAR.getFullType())) {
                    FileUtil.writeFromStream(jis, new File(fullPath), false);
                    if (includeFiles != null && includeFiles.contains(FileUtil.getName(entryName))) {
                        list.addAll(decompression(fullPath, fullPath.replace(FileType.JAR.getFullType(), PathConst.TEMP_DIR), includeFiles));
                    }
                } else if (jarEntry.isDirectory()) {
                    FileUtil.mkdir(fullPath);
                } else {
                    FileUtil.writeFromStream(jis, new File(fullPath), false);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("解压Jar/War执行文件时出现异常", e);
        }
        return list;
    }

    /**
     * 收集需加密class
     *
     * @param allFilePath  临时文件路径集合
     * @param filePath     临时文件路径
     * @param packages     需加密包路径
     * @param excludeClass 排除的类路径
     * @return 需加密class集合
     */
    public static Map<String, List<String>> getEncryptClass(List<String> allFilePath, String filePath, String packages, String excludeClass) {
        Map<String, List<String>> jarClasses = new HashMap<>();
        List<String> classFilePath = allFilePath.stream()
                .filter(x -> x.endsWith(PathConst.EXT_CLASS))
                .map(x -> x.replace(filePath + File.separator, "")
                        .replace(PathConst.EXT_CLASS, "")
                        .replace(File.separator, ".")
                )
                .collect(Collectors.toList());
        for (String file : classFilePath) {
            String jarName;
            String clsName;
            if (StrUtil.containsAny(file, "BOOT-INF.lib.", "WEB-INF.lib.") && file.contains(PathConst.TEMP_DIR)) {
                file = StrUtil.replace(file, "BOOT-INF.lib.", "")
                        .replace("WEB-INF.lib.", "");
                jarName = file.substring(0, file.indexOf(PathConst.TEMP_DIR));
                clsName = file.substring(file.indexOf(PathConst.TEMP_DIR) + PathConst.TEMP_DIR.length() + 1);
            } else if (StrUtil.containsAny(file, "BOOT-INF.classes.", "WEB-INF.classes.")) {
                file = StrUtil.replace(file, "BOOT-INF.classes.", "")
                        .replace("WEB-INF.classes.", "");
                jarName = "CLASSES";
                clsName = file;
            } else {
                jarName = "ROOT";
                clsName = file;
            }
            if (StringUtil.needEncrypt(packages, clsName, excludeClass)) {
                jarClasses.computeIfAbsent(jarName, k -> new ArrayList<>()).add(clsName);
            }
        }
        return jarClasses;
    }

}
