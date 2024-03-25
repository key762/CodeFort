package coffee.lucks.codefort.embeds.util;

import coffee.lucks.codefort.embeds.arms.ByteArm;
import coffee.lucks.codefort.embeds.arms.FileArm;
import coffee.lucks.codefort.embeds.arms.IoArm;
import coffee.lucks.codefort.embeds.unit.Guarder;
import coffee.lucks.codefort.embeds.unit.PathConst;

import java.io.*;
import java.util.zip.*;

public class EncryptUtil {

    /**
     * 加密class文件
     *
     * @param guarder 执行对象
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

    public static byte[] decrypt(byte[] msg, String key) {
        return AESUtil.decrypt(decompressByte(msg), MD5Util.digest(key), MD5Util.digest(key));
    }

    public static byte[] encrypt(byte[] bytes, String password) {
        return compressByte(AESUtil.encrypt(bytes, MD5Util.digest(password), MD5Util.digest(password)));
    }

    /**
     * 字节码压缩
     *
     * @param inputBytes 源字节码
     * @return 字节码
     */
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

    /**
     * 字节码解压
     *
     * @param compressedData 源字节码
     * @return 字节码
     */
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
