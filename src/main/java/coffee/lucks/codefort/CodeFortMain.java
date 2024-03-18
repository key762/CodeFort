package coffee.lucks.codefort;

import cn.hutool.core.io.FileUtil;
import coffee.lucks.codefort.util.ClassUtil;
import coffee.lucks.codefort.util.FortUtil;
import coffee.lucks.codefort.util.custom.CustomRegister;
import coffee.lucks.codefort.util.custom.CustomUtil;
import javassist.ClassPool;
import javassist.NotFoundException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CodeFortMain {

    private static String packages = "host.skiree.springdemo,cn.hutool.setting";

    private static String excludeClass = "";

    private static String jarPath = "/Users/anorak/Documents/JavaProject/standalone/codefort/src/main/resources/demo.jar";

    private static String password = "123456";

    public static void main(String[] args) throws NotFoundException {
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

        String libpath = tempFilePath + File.separator + ("jar".equals(fileType) ? "BOOT-INF" : "WEB-INF") + File.separator + "lib" + File.separator;
        for (Map.Entry<String, List<String>> entry : jarClasses.entrySet()) {
            //初始化javassist
            ClassPool pool = ClassPool.getDefault();
            //lib目录
            ClassUtil.loadClassPath(pool, new String[]{libpath});
            //要修改的class所在的目录
            pool.insertClassPath(tempFilePath + File.separator + FortUtil.realPath(entry.getKey(), null, fileType));
            //修改class方法体，并保存文件
            for (String classname : entry.getValue()) {
                byte[] bts = ClassUtil.rewriteMethod(pool, classname);
                if (bts != null) {
                    String path = tempFilePath + File.separator + FortUtil.realPath(entry.getKey(), classname, fileType);
                    FileUtil.writeBytes(bts, path);
                }
            }
        }
    }

}
