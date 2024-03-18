package coffee.lucks.codefort.util;

import cn.hutool.crypto.digest.MD5;
import cn.hutool.crypto.symmetric.AES;

import java.io.ByteArrayOutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class EncryptUtil {
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


}
