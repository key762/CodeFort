package coffee.lucks.codefort;

import cn.hutool.core.io.FileUtil;
import coffee.lucks.codefort.banner.FortBanner;
import coffee.lucks.codefort.consts.PathConst;
import coffee.lucks.codefort.enums.FileType;
import coffee.lucks.codefort.util.ClassUtil;
import coffee.lucks.codefort.util.EncryptUtil;
import coffee.lucks.codefort.util.HandleUtil;
import coffee.lucks.codefort.util.StringUtil;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * java -javaagent:codefort-agent-1.0.0.jar='-data demo-encrypted.jar -pwd 123456' -jar demo-encrypted.jar
 */
public class CodeFortMain {

    private static final String packages = "host.skiree.springdemo,cn.hutool.setting";

    private static final String excludeClass = "";

    private static final String jarPath = "/Users/anorak/Documents/JavaProject/standalone/codefort/codefort-core/src/main/resources/demo.jar";

    private static String password = "123456";

    private static final List<String> includeFiles = Arrays.asList("hutool-setting-5.8.26.jar");

    public static void main(String[] args) {
        FortBanner.banner();
        FileType fileType = StringUtil.getFileType(jarPath);
        password = StringUtil.checkStrWithDef(password, "000000");
        // 获取临时目录
        String tempFilePath = StringUtil.getTmpDirPath(jarPath, fileType);
        // 解压原执行文件
        List<String> allFilePath = HandleUtil.decompression(jarPath, tempFilePath, includeFiles);
        // 收集所有需加密的class
        Map<String, List<String>> jarClasses = HandleUtil.getEncryptClass(allFilePath, tempFilePath, packages, excludeClass);
        // 加密class类
        EncryptUtil.encryptClass(jarClasses, tempFilePath, fileType, password);
        String libPath = tempFilePath + File.separator + fileType.getMark() + File.separator + "lib" + File.separator;
        // 清理已加密的class对应的原方法体
        ClassUtil.clearClassBody(jarClasses, tempFilePath, libPath, fileType);
        // 先打包lib路径下的jar
        for (Map.Entry<String, List<String>> entry : jarClasses.entrySet()) {
            if (!"CLASSES".equals(entry.getKey()) && !"ROOT".equals(entry.getKey())) {
                EncryptUtil.compress(libPath + entry.getKey() + PathConst.TEMP_DIR, libPath + entry.getKey() + FileType.JAR.getFullType());
            }
        }
        // 删除lib路径下的jar解压出的临时文件
        for (String file : includeFiles) {
            FileUtil.del(libPath + file.replace(FileType.JAR.getFullType(), PathConst.TEMP_DIR));
        }
        // 最终打包
        String result = EncryptUtil.compress(tempFilePath, jarPath.replace(fileType.getFullType(), "-encrypted" + fileType.getFullType()));
        FileUtil.del(tempFilePath);
        System.out.println(result);
    }

}
