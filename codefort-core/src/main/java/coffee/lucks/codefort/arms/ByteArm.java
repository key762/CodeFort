package coffee.lucks.codefort.arms;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ByteArm {

    /**
     * 读取指定文件的byte[]
     *
     * @param file 文件对象
     * @return byte[]
     */
    public static byte[] readBytes(File file) {
        long len = file.length();
        if (len >= Integer.MAX_VALUE) throw new RuntimeException("文件大小超过最大限制");
        byte[] bytes = new byte[(int) len];
        try (FileInputStream in = new FileInputStream(file)) {
            int readLength = in.read(bytes);
            if (readLength < len)
                throw new RuntimeException(String.format("文件长度为[%d]但是读取到[%d]!", len, readLength));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bytes;
    }

    /**
     * 从压缩文件中获取对应路径文件的byte[]
     *
     * @param zip      压缩文件对象
     * @param fileName 需获取文件的路径
     * @return byte[]
     */
    public static byte[] getFileFromZip(File zip, String fileName) {
        if (!zip.exists()) return null;
        try (ZipFile zipFile = new ZipFile(zip)) {
            ZipEntry zipEntry = zipFile.getEntry(fileName);
            if (zipEntry == null) return null;
            return readBytes(zipFile.getInputStream(zipEntry));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从InputStream中获取byte[]
     *
     * @param in 输入InputStream
     * @return byte[]
     */
    public static byte[] readBytes(InputStream in) {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int n;
            while ((n = in.read(buffer)) != -1) {
                output.write(buffer, 0, n);
            }
            return output.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
