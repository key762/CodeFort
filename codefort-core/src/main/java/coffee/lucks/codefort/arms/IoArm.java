package coffee.lucks.codefort.arms;

import java.io.*;
import java.nio.file.Files;

public class IoArm {

    public static void writeFromStream(InputStream in, File file) {
        byte[] bytes = ByteArm.readBytes(in);
        try (OutputStream os = Files.newOutputStream(file.toPath())) {
            os.write(bytes, 0, bytes.length);
            os.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeFromByte(byte[] bytes, File file) {
        try (OutputStream os = Files.newOutputStream(file.toPath())) {
            os.write(bytes, 0, bytes.length);
            os.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从流中读取内容，读到输出流中
     *
     * @param input 输入流
     * @return 输出流
     * @throws IOException IO异常
     */
    public static byte[] readBytes(InputStream input) throws IOException {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int n = 0;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
            return output.toByteArray();
        } finally {
            close(input);
        }
    }

    /**
     * 关闭<br>
     * 关闭失败不会抛出异常
     *
     * @param closeable 被关闭的对象
     */
    public static void close(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (Exception e) {
                // 静默关闭
            }
        }
    }

}
