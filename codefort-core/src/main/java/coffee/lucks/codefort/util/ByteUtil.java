package coffee.lucks.codefort.util;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ByteUtil {

    public static byte[] readBytes(File file) {
        long len = file.length();
        if (len >= Integer.MAX_VALUE) {
            throw new RuntimeException("文件大小超过最大限制");
        }
        byte[] bytes = new byte[(int) len];
        try (FileInputStream in = new FileInputStream(file);) {
            int readLength = in.read(bytes);
            if (readLength < len) {
                throw new RuntimeException(String.format("文件长度为[%d]但是读取到[%d]!", len, readLength));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bytes;
    }

    public static byte[] getFileFromJar(File zip, String fileName) {
        if (zip.exists()) {
            try (ZipFile zipFile = new ZipFile(zip)) {
                ZipEntry zipEntry = zipFile.getEntry(fileName);
                if (zipEntry == null) {
                    return null;
                }
                InputStream is = zipFile.getInputStream(zipEntry);
                return readBytes(is);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public static byte[] readBytes(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[4096];
            int n = 0;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
            return output.toByteArray();
        } finally {
            close(output, input);
        }
    }

    public static void close(Closeable... outs) {
        if (outs != null) {
            for (Closeable out : outs) {
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

}
