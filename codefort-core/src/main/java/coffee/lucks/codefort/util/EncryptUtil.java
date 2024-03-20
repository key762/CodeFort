package coffee.lucks.codefort.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.crypto.symmetric.AES;
import coffee.lucks.codefort.consts.PathConst;
import coffee.lucks.codefort.enums.FileType;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.*;

public class EncryptUtil {

    /**
     * 加密class文件
     *
     * @param jarClasses   需加密的class集合
     * @param tempFilePath 临时文件路径
     * @param fileType     文件类型
     * @param password     加密密码
     */
    public static void encryptClass(Map<String, List<String>> jarClasses, String tempFilePath, FileType fileType, String password) {
        try (FileOutputStream out = new FileOutputStream(tempFilePath + File.separator + PathConst.ENCRYPT_NAME);
             ZipOutputStream zos = new ZipOutputStream(out)) {
            for (Map.Entry<String, List<String>> entry : jarClasses.entrySet()) {
                for (String classname : entry.getValue()) {
                    String classPath = tempFilePath + File.separator + StringUtil.getRealPath(entry.getKey(), classname, fileType);
                    zos.putNextEntry(new ZipEntry(classname));
                    byte[] bytes = FileUtil.readBytes(classPath);
                    bytes = EncryptUtil.encrypt(bytes, password);
                    zos.write(bytes, 0, bytes.length);
                    zos.closeEntry();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("加密Jar/War的class文件时出现异常", e);
        }
    }


    /**
     * 根据名称解密出一个文件
     *
     * @param encryptPath classes目录
     * @param fileName    加密生成的zip
     * @param password    密码
     * @param isZip       是否压缩
     * @return
     */
    public static byte[] decryptFile(String encryptPath, String fileName, String password, boolean isZip) {
        if (!isZip) {
            File file = new File(encryptPath + File.separator + fileName + ".clazz");
            if (!file.exists()) {
                return null;
            }
            byte[] bytes = FileUtil.readBytes(file);
            bytes = de(bytes, password);
            return bytes;
        }
        ZipFile zipFile = null;
        try {
            File zip = new File(encryptPath);
            if (!zip.exists()) {
                return null;
            }
            zipFile = new ZipFile(zip);
            ZipEntry zipEntry = zipFile.getEntry(fileName);
            if (zipEntry == null) {
                return null;
            }
            InputStream is = zipFile.getInputStream(zipEntry);
            byte[] bytes = toByteArray(is);
            bytes = de(bytes, password);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            EncryptUtil.close(zipFile);
        }
        return null;
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[4096];
            int n = 0;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
            return output.toByteArray();
        } finally {
            output.close();
            input.close();
        }
    }


    /**
     * 根据名称解密出一个文件
     *
     * @param encryptPath classes目录
     * @param fileName    加密生成的zip
     * @param password    密码
     * @return
     */
    public static byte[] decryptFile(String encryptPath, String fileName, String password) {
        return decryptFile(encryptPath, fileName, password, true);
    }

    public static byte[] de(byte[] msg, String key) {
        AES aes = new AES(
                "CBC",
                "PKCS7Padding",
                MD5.create().digest(key),
                MD5.create().digest(key)
        );
        return aes.decrypt(decompressByte(msg));
    }

    public static byte[] encrypt(byte[] bytes, String password) {
        AES aes = new AES(
                "CBC",
                "PKCS7Padding",
                MD5.create().digest(password),
                MD5.create().digest(password)
        );
        return compressByte(aes.encrypt(bytes));
    }

    private static byte[] compressByte(byte[] inputBytes) {
        Deflater deflater = new Deflater();
        deflater.setInput(inputBytes);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(inputBytes.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        deflater.end();
        return outputStream.toByteArray();
    }

    private static byte[] decompressByte(byte[] compressedData) {
        Inflater inflater = new Inflater();
        inflater.setInput(compressedData);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(compressedData.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
        } catch (DataFormatException e) {
            e.printStackTrace();
        }
        inflater.end();
        return outputStream.toByteArray();
    }


    public static void close(Closeable... outs) {
        if (outs != null) {
            for (Closeable out : outs) {
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public static String compress(String jarDir, String targetJar) {
        List<File> files = new ArrayList<>();
        listFile(files, new File(jarDir));
        FileUtil.del(targetJar);
        try (OutputStream out = Files.newOutputStream(new File(targetJar).toPath());
             JarArchiveOutputStream jos = new JarArchiveOutputStream(out);
        ) {
            for (File file : files) {
                if (StringUtil.isDel(file.getAbsolutePath())) {
                    continue;
                }
                String fileName = file.getAbsolutePath().substring(jarDir.length());
                fileName = fileName.startsWith(File.separator) ? fileName.substring(1) : fileName;
                if (file.isDirectory()) {
                    JarArchiveEntry je = new JarArchiveEntry(fileName + File.separator);
                    je.setTime(System.currentTimeMillis());
                    jos.putArchiveEntry(je);
                    jos.closeArchiveEntry();
                } else if (StrUtil.endWithAnyIgnoreCase(fileName, ".zip", ".jar")) {
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
            throw new RuntimeException("打包Jar/War文件时出现异常", e);
        }
        return targetJar;
    }

    /**
     * crc32算法
     *
     * @param bytes 字节码
     * @return 加密值
     */
    public static long crc32(byte[] bytes) {
        CRC32 crc = new CRC32();
        crc.update(bytes);
        return crc.getValue();
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
