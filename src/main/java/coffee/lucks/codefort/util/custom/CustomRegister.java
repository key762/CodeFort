package coffee.lucks.codefort.util.custom;

import java.util.List;

public abstract class CustomRegister {

    public abstract String utilName();

    /**
     * 解压文件
     * @param filePath 需解压的文件路径
     * @param targetDir 需解压到的文件路径
     * @param includeFiles 需要解压的内部文件名称
     * @return 解压出的所有文件完整路径
     */
    public abstract List<String> decompression(String filePath, String targetDir, List<String> includeFiles);


    public abstract String compress(String jarDir, String targetJar);

    public void register() {
        CustomUtil.getInstance().register(this);
    }
}
