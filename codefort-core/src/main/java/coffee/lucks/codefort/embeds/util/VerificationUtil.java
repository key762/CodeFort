package coffee.lucks.codefort.embeds.util;

import coffee.lucks.codefort.embeds.arms.FileArm;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.security.MessageDigest;
import java.util.*;

public class VerificationUtil {

    private static final char[] alphabets = "0123456789abcdef".toCharArray();

    public static String getVerificationInfo(File targetFile) {
        List<File> classList = new ArrayList<>();
        FileArm.listFile(classList, new File(targetFile, "coffee"), ".class");
        Map<Integer, byte[]> fileMap = new HashMap<>();
        for (File file : classList) {
            fileMap.put(file.getName().hashCode(), FileArm.readBytes(file));
        }
        return getVerificationInfo(fileMap);
    }

    public static String getVerificationInfo(Map<Integer, byte[]> fileMap) {
        List<Map.Entry<Integer, byte[]>> list = new ArrayList<>(fileMap.entrySet());
        list.sort(Map.Entry.comparingByKey());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (Map.Entry<Integer, byte[]> entry : list) {
            try {
                outputStream.write(entry.getValue());
            } catch (Exception ignore) {
            }
        }
        byte[] combinedBytes = outputStream.toByteArray();
        return new String(encode(Objects.requireNonNull(digestMd5(combinedBytes))));
    }

    public static byte[] digestMd5(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data);
            byte[] digest = md.digest();
            return Base64.getEncoder().encode(digest);
        } catch (Exception e) {
            return null;
        }
    }

    public static char[] encode(byte[] data) {
        int len = data.length;
        char[] out = new char[len << 1];
        int i = 0;
        for (int j = 0; i < len; ++i) {
            out[j++] = alphabets[(240 & data[i]) >>> 4];
            out[j++] = alphabets[15 & data[i]];
        }
        return out;
    }

}
