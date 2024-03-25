package coffee.lucks.codefort.embeds.arms;

import java.io.*;
import java.nio.file.Files;

public class IoArm {

    /**
     * 将输入流写入到文件
     *
     * @param in   输入流
     * @param file 文件
     */
    public static void writeFromStream(InputStream in, File file) {
        byte[] bytes = ByteArm.readBytes(in);
        try (OutputStream os = Files.newOutputStream(file.toPath())) {
            os.write(bytes, 0, bytes.length);
            os.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将字节码写入到文件
     *
     * @param bytes 字节码
     * @param file  文件
     */
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

    /**
     * 从文件中读取文本
     *
     * @param file 文件
     * @return 文本
     */
    public static String readTxtFile(File file) {
        StringBuffer txt = new StringBuffer("");
        try (InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
             BufferedReader bufferedReader = new BufferedReader(read)) {
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                txt.append(lineTxt).append("\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return txt.toString();
    }

    /**
     * 写文件
     *
     * @param file 文件
     * @param txt  内容
     */
    public static void writeTxtFile(File file, String txt) {
        BufferedWriter out = null;
        try {
            if (!file.exists()) {
                file.mkdirs();
                file.delete();
                file.createNewFile();
            }
            out = new BufferedWriter(new FileWriter(file));
            out.write(txt);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(out);
        }
    }

}
