package coffee.lucks.codefort.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import coffee.lucks.codefort.arms.FileArm;
import coffee.lucks.codefort.arms.IoArm;
import coffee.lucks.codefort.model.Guarder;
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
     * @param filePath  文件路径
     * @param targetDir 目标路径
     * @param guarder   执行对象
     * @return 所有解压文件绝对路径
     */
    public static Guarder decompression(String filePath, String targetDir, Guarder guarder) {
        FileArm.mkDir(targetDir);
        try (ZipFile zipFile = new ZipFile(new File(filePath))) {
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String entryName = entry.getName();
                String fullPath = targetDir + File.separator + entryName;
                if (entryName.endsWith(FileType.JAR.getFullType())) {
                    IoArm.writeFromStream(zipFile.getInputStream(entry), new File(fullPath));
                    if (guarder.getIncludeJars() != null && guarder.getIncludeJars().contains(FileUtil.getName(entryName))) {
                        guarder.getLibJars().add(fullPath);
                        guarder.getLibJarNames().add(entryName.replace(FileType.JAR.getFullType(), PathConst.TEMP_DIR));
                        guarder.getAllFile().addAll(decompression(fullPath, fullPath.replace(FileType.JAR.getFullType(), PathConst.TEMP_DIR), guarder).getAllFile());
                    }
                } else if (entry.isDirectory()) {
                    FileArm.mkDir(fullPath);
                } else {
                    IoArm.writeFromStream(zipFile.getInputStream(entry), new File(fullPath));
                    guarder.getAllFile().add(fullPath);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("解压Jar/War执行文件时出现异常", e);
        }
        return guarder;
    }

    /**
     * 收集需加密class
     *
     * @param guarder 执行对象
     * @return 加密class集合
     */
    public static Guarder getEncryptClass(Guarder guarder) {
        List<File> files = new ArrayList<>();
        List<String> stringList = guarder.getAllFile().stream()
                .filter(x -> x.endsWith(PathConst.EXT_CLASS))
                .collect(Collectors.toList());
        for (String file : stringList) {
            if (StringUtil.needEncrypt(file, guarder)) {
                files.add(new File(file));
            }
        }
        guarder.getEncryptClass().addAll(files);
        return guarder;
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
                fileName = fileName.replace(File.separator, "/");
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
