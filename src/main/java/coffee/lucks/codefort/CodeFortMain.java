package coffee.lucks.codefort;

import cn.hutool.core.io.FileUtil;
import coffee.lucks.codefort.util.ClearUtil;
import coffee.lucks.codefort.util.FortUtil;
import coffee.lucks.codefort.util.custom.CustomRegister;
import coffee.lucks.codefort.util.custom.CustomUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * java -javaagent:codefort-1.0-SNAPSHOT.jar='-data demo-encrypted.jar -pwd 123456' -jar demo-encrypted.jar
 */
public class CodeFortMain {

    private static String packages = "host.skiree.springdemo,cn.hutool.setting";

    private static String excludeClass = "";

    private static String jarPath = "/Users/anorak/Documents/JavaProject/standalone/codefort/src/main/resources/demo.jar";

    private static String password = "123456";

    public static void main(String[] args) {
//        FortBanner.banner();
        FortUtil.checkFileType(jarPath);
        FortUtil.checkPassWord(password);
        String fileType = FortUtil.getFileType(jarPath);
        String tempFilePath = FortUtil.getTempFilePath(jarPath, fileType);
        System.out.println(tempFilePath);
        CustomRegister customRegister = CustomUtil.getInstance().get(fileType);
        List<String> includeFiles = new ArrayList<>();
        includeFiles.add("hutool-setting-5.8.26.jar");
        List<String> allFilePath = customRegister.decompression(jarPath, tempFilePath, includeFiles);
        System.out.println(allFilePath);
        Map<String, List<String>> jarClasses = FortUtil.getEncryptClass(allFilePath, tempFilePath, packages, excludeClass);
        System.out.println(jarClasses);
        FortUtil.encryptClass(jarClasses, tempFilePath, fileType, password);
        String libPath = tempFilePath + File.separator + ("jar".equals(fileType) ? "BOOT-INF" : "WEB-INF") + File.separator + "lib" + File.separator;
        ClearUtil.clear(jarClasses, tempFilePath, libPath,fileType);

        for (Map.Entry<String, List<String>> entry : jarClasses.entrySet()) {
            if (!"CLASSES".equals(entry.getKey()) && !"ROOT".equals(entry.getKey())) {
                customRegister.compress(libPath + entry.getKey() + FortUtil.TEMP_DIR, libPath + entry.getKey() + ".jar");
            }
        }
        for (String file : includeFiles) {
            FileUtil.del(libPath + file.replace(".jar", FortUtil.TEMP_DIR));
        }
        String result = customRegister.compress(tempFilePath, jarPath.replace("." + fileType, "-encrypted." + fileType));
        FileUtil.del(tempFilePath);
        System.out.println(result);

    }

}
