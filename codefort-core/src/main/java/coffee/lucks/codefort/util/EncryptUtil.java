package coffee.lucks.codefort.util;

import cn.hutool.crypto.digest.MD5;
import cn.hutool.crypto.symmetric.AES;
import coffee.lucks.codefort.arms.ByteArm;
import coffee.lucks.codefort.arms.FileArm;
import coffee.lucks.codefort.arms.IoArm;
import coffee.lucks.codefort.model.Guarder;
import coffee.lucks.codefort.unit.PathConst;

import java.io.*;
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
    public static void encryptClass(Guarder guarder) {
        File metaDir = new File(guarder.getTargetStr(), "META-INF" + File.separator + PathConst.ENCRYPT_NAME);
        FileArm.mkDir(metaDir);
        try {
            for (File file : guarder.getEncryptClass()) {
                String className = file.getName();
                if (className.endsWith(".class")) {
                    className = StringUtil.resolveClassPath(file.getAbsolutePath(), true);
                }
                byte[] bytes = ByteArm.readBytes(file);
                bytes = EncryptUtil.encrypt(bytes, guarder.getPassword());
                IoArm.writeFromByte(bytes, new File(metaDir, className));
            }
        } catch (Exception e) {
            throw new RuntimeException("加密Jar/War的class文件时出现异常", e);
        }
    }

    /**
     * 根据名称解密出一个文件
     *
     * @param projectPath 项目路径
     * @param fileName    类名称
     * @param password    密码
     * @return 字节码
     */
    public static byte[] decryptFile(String projectPath, String fileName, String password) {
        File workDir = new File(projectPath);
        byte[] bytes = readEncryptedFile(workDir, fileName);
        if (bytes == null) {
            return null;
        }
        return decrypt(bytes, password);
    }

    /**
     * 在jar文件或目录中读取文件字节
     *
     * @param workDir jar文件或目录
     * @param name    文件名
     * @return 文件字节数组
     */
    public static byte[] readEncryptedFile(File workDir, String name) {
        byte[] bytes = null;
        String fileName = PathConst.ENCRYPT_PATH + name;
        if (workDir.isFile()) {
            bytes = ByteArm.getFileFromZip(workDir, fileName);
        } else {//war解压的目录
            File file = new File(workDir, fileName);
            if (file.exists()) {
                bytes = ByteArm.readBytes(file);
            }
        }
        return bytes;
    }

    /**
     * 在压缩文件中获取一个文件的字节
     *
     * @param zip      压缩文件
     * @param fileName 文件名
     * @return 文件的字节
     */
    public static byte[] getFileFromZip(File zip, String fileName) {
        if (zip.exists()) {
            try (ZipFile zipFile = new ZipFile(zip)) {
                InputStream inputStream = zipFile.getInputStream(zipFile.getEntry("META-INF" + File.separator + PathConst.ENCRYPT_NAME + File.separator + fileName));
                return IoArm.readBytes(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("在压缩文件中获取字节时异常");
            }
        }
        return null;
    }

    public static byte[] decrypt(byte[] msg, String key) {
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

}
