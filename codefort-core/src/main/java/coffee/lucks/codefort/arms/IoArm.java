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

}
