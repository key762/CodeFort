package coffee.lucks.codefort.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.crypto.symmetric.AES;

import java.io.*;
import java.util.zip.*;

public class EncryptUtil {

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
        }finally {
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

}
