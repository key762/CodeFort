package coffee.lucks.codefort.arms;

import java.io.File;
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
        if (files != null){
            for (File f : files) {
                if (f.isDirectory()) {
                    listFile(fileList, f, endWith);
                } else if (f.isFile() && f.getName().endsWith(endWith)) {
                    fileList.add(f);
                }
            }
        }
    }

}
