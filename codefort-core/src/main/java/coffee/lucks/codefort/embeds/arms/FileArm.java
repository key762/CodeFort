package coffee.lucks.codefort.embeds.arms;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class FileArm {

    /**
     * 创建文件夹
     *
     * @param filePath 文件夹路径
     * @return 创建成功时返回目录的File对象，否则返回null
     */
    public static File mkDir(String filePath) {
        File file = new File(filePath);
        if (file.exists() || file.mkdirs()) {
            return file;
        }
        return null;
    }

    /**
     * 创建文件夹
     *
     * @param file 文件夹路径
     * @return 创建成功时返回目录的File对象，否则返回null
     */
    public static File mkDir(File file) {
        if (file.exists() || file.mkdirs()) {
            return file;
        }
        return null;
    }

    /**
     * 递归查找文件，只返回文件
     *
     * @param fileList 返回的文件列表
     * @param dir      目录
     * @param endWith  文件后缀
     */
    public static void listFile(List<File> fileList, File dir, String endWith) {
        if (!dir.exists()) throw new RuntimeException("目录[" + dir.getAbsolutePath() + "]不存在");
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    listFile(fileList, f, endWith);
                } else if (f.isFile() && f.getName().endsWith(endWith)) {
                    fileList.add(f);
                }
            }
        }
    }

    /**
     * 获取系统级的临时目录
     *
     * @return 临时目录
     */
    public static String getTmpDirPath() {
        return System.getProperty("java.io.tmpdir");
    }

    /**
     * 返回文件名
     *
     * @param filePath 文件
     * @return 文件名
     */
    public static String getName(String filePath) {
        if (null == filePath) {
            return null;
        }
        int len = filePath.length();
        if (0 == len) {
            return filePath;
        }
        if (StrArm.isFileSeparator(filePath.charAt(len - 1))) {
            // 以分隔符结尾的去掉结尾分隔符
            len--;
        }
        int begin = 0;
        char c;
        for (int i = len - 1; i > -1; i--) {
            c = filePath.charAt(i);
            if (StrArm.isFileSeparator(c)) {
                // 查找最后一个路径分隔符（/或者\）
                begin = i + 1;
                break;
            }
        }
        return filePath.substring(begin, len);
    }

    /**
     * 读取文件所有数据
     *
     * @param file 文件对象
     * @return 字节码
     */
    public static byte[] readBytes(File file) {
        long len = file.length();
        if (len >= Integer.MAX_VALUE) {
            throw new RuntimeException("File is larger then max array size");
        }
        byte[] bytes = new byte[(int) len];
        FileInputStream in = null;
        int readLength;
        try {
            in = new FileInputStream(file);
            readLength = in.read(bytes);
            if (readLength < len) {
                throw new RuntimeException(String.format("File length is [%d] but read [%d]!", len, readLength));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IoArm.close(in);
        }
        return bytes;
    }

    /**
     * 删除文件或目录
     *
     * @param path
     */
    public static void del(String path) {
        File fileOrFolder = new File(path);
        if (!fileOrFolder.exists()) {
            return;
        }
        if (fileOrFolder.isDirectory()) {
            deleteDirectory(fileOrFolder);
        } else {
            fileOrFolder.delete();
        }
    }

    /**
     * 递归删除文件
     *
     * @param directory 文件目录
     */
    private static void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }

}
