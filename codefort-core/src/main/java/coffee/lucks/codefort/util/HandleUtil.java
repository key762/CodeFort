package coffee.lucks.codefort.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import coffee.lucks.codefort.unit.PathConst;
import coffee.lucks.codefort.unit.FileType;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

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
        try (ZipFile zipFile = new ZipFile(new File(filePath))) {
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String entryName = entry.getName();
                String fullPath = targetDir + File.separator + entryName;
                if (entryName.endsWith(FileType.JAR.getFullType())) {
                    FileUtil.writeFromStream(zipFile.getInputStream(entry), new File(fullPath), false);
                    if (includeFiles != null && includeFiles.contains(FileUtil.getName(entryName))) {
                        list.addAll(decompression(fullPath, fullPath.replace(FileType.JAR.getFullType(), PathConst.TEMP_DIR), includeFiles));
                    }
                } else if (entry.isDirectory()) {
                    FileUtil.mkdir(fullPath);
                } else {
                    FileUtil.writeFromStream(zipFile.getInputStream(entry), new File(fullPath), false);
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

    /**
     * 指定目录打包Jar/War文件
     *
     * @param jarDir    目录
     * @param targetJar 目标文件
     * @return 打包后的路径
     */
    public static String compress(String jarDir, String targetJar) {
        List<File> files = new ArrayList<>();
        listFile(files, new File(jarDir));
        FileUtil.del(targetJar);
        try (OutputStream out = Files.newOutputStream(new File(targetJar).toPath());
             ZipOutputStream zos = new ZipOutputStream(out);
        ) {
            for (File file : files) {
                if (StringUtil.isDel(file.getAbsolutePath())) {
                    continue;
                }
                String fileName = file.getAbsolutePath().substring(jarDir.length());
                fileName = fileName.startsWith(File.separator) ? fileName.substring(1) : fileName;
                if (file.isDirectory()) {
                    ZipEntry ze = new ZipEntry(fileName + File.separator);
                    ze.setTime(System.currentTimeMillis());
                    zos.putNextEntry(ze);
                    zos.closeEntry();
                } else if (StrUtil.endWithAnyIgnoreCase(fileName, ".zip", ".jar")) {
                    byte[] bytes = FileUtil.readBytes(file);
                    ZipEntry ze = new ZipEntry(fileName);
                    ze.setMethod(ZipEntry.STORED);
                    ze.setSize(bytes.length);
                    ze.setTime(System.currentTimeMillis());
                    ze.setCrc(EncryptUtil.crc32(bytes));
                    zos.putNextEntry(ze);
                    zos.write(bytes);
                    zos.closeEntry();
                } else {
                    ZipEntry ze = new ZipEntry(fileName);
                    ze.setTime(System.currentTimeMillis());
                    zos.putNextEntry(ze);
                    byte[] bytes = FileUtil.readBytes(file);
                    zos.write(bytes);
                    zos.closeEntry();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("打包Jar/War文件时出现异常", e);
        }
        return targetJar;
    }

    /**
     * 递归收集文件夹信息
     *
     * @param fileList 文件集合
     * @param dir      目录地址
     */
    public static void listFile(List<File> fileList, File dir) {
        File[] files = dir.listFiles();
        for (File f : files) {
            fileList.add(f);
            if (f.isDirectory()) {
                listFile(fileList, f);
            }
        }
    }

}
