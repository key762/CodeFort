package coffee.lucks.codefort.util.custom;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import coffee.lucks.codefort.util.ClassUtil;
import coffee.lucks.codefort.util.FortUtil;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;

public class JarUtil extends CustomRegister {
    @Override
    public String utilName() {
        return "jar";
    }

    @Override
    public List<String> decompression(String filePath, String targetDir, List<String> includeFiles) {
        List<String> list = new ArrayList<>();
        targetDir = FortUtil.convertPath(targetDir);
        FileUtil.mkdir(targetDir);
        try (FileInputStream fin = new FileInputStream(filePath);
             JarArchiveInputStream jis = new JarArchiveInputStream(fin)) {
            JarArchiveEntry jarEntry;
            while ((jarEntry = jis.getNextEntry()) != null) {
                String entryName = jarEntry.getName();
                String fullPath = targetDir + File.separator + entryName;
                list.add(fullPath);
                if (entryName.endsWith(FileNameUtil.EXT_JAR)) {
                    ByteArrayOutputStream jos0 = new ByteArrayOutputStream();
                    IOUtils.copy(jis, jos0);
                    byte[] bytes = jos0.toByteArray();
                    FileUtil.writeBytes(bytes, fullPath);
                    if (includeFiles != null && includeFiles.contains(FileUtil.getName(entryName))) {
                        List<String> list0 = decompression(fullPath, fullPath.replace(FileNameUtil.EXT_JAR, FortUtil.TEMP_DIR), includeFiles);
                        list.addAll(list0);
                    }
                } else if (jarEntry.isDirectory()) {
                    FileUtil.mkdir(fullPath);
                } else {
                    ByteArrayOutputStream jos0 = new ByteArrayOutputStream();
                    IOUtils.copy(jis, jos0);
                    byte[] bytes = jos0.toByteArray();
                    FileUtil.writeBytes(bytes, fullPath);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public String compress(String jarDir, String targetJar) {
        List<File> files = new ArrayList<>();
        listFile(files, new File(jarDir));
        JarArchiveOutputStream jos = null;
        OutputStream out = null;

        try {
            File jar = new File(targetJar);
            if (jar.exists()) {
                jar.delete();
            }

            out = new FileOutputStream(jar);
            jos = new JarArchiveOutputStream(out);

            for (File file : files) {
                if (ClassUtil.isDel(file)) {
                    continue;
                }
                String fileName = file.getAbsolutePath().substring(jarDir.length());
                fileName = fileName.startsWith(File.separator) ? fileName.substring(1) : fileName;

                if (file.isDirectory()) {
                    JarArchiveEntry je = new JarArchiveEntry(fileName + File.separator);
                    je.setTime(System.currentTimeMillis());
                    jos.putArchiveEntry(je);
                    jos.closeArchiveEntry();
                } else if (fileName.endsWith(".jar") || fileName.endsWith(".zip")) {
                    byte[] bytes = FileUtil.readBytes(file);
                    JarArchiveEntry je = new JarArchiveEntry(fileName);
                    je.setMethod(JarArchiveEntry.STORED);
                    je.setSize(bytes.length);
                    je.setTime(System.currentTimeMillis());
                    je.setCrc(crc32(bytes));
                    jos.putArchiveEntry(je);
                    jos.write(bytes);
                    jos.closeArchiveEntry();

                } else {
                    JarArchiveEntry je = new JarArchiveEntry(fileName);
                    je.setTime(System.currentTimeMillis());
                    jos.putArchiveEntry(je);
                    byte[] bytes = FileUtil.readBytes(file);
                    jos.write(bytes);
                    jos.closeArchiveEntry();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                IOUtils.close(jos);
            }catch (Exception ignore){}
        }
        return targetJar;
    }

    public static long crc32(byte[] bytes) {
        CRC32 crc = new CRC32();
        crc.update(bytes);
        return crc.getValue();
    }


    public static void listFile(List<File> filess, File dir) {
        if (!dir.exists()) {
            throw new IllegalArgumentException("目录[" + dir.getAbsolutePath() + "]不存在");
        }
        File[] files = dir.listFiles();
        for (File f : files) {
            filess.add(f);
            if (f.isDirectory()) {
                listFile(filess, f);
            }
        }
    }

}