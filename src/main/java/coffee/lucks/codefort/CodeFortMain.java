package coffee.lucks.codefort;

import coffee.lucks.codefort.banner.FortBanner;
import coffee.lucks.codefort.util.FortUtil;
import coffee.lucks.codefort.util.custom.CustomRegister;
import coffee.lucks.codefort.util.custom.CustomUtil;

import java.util.ArrayList;
import java.util.List;

public class CodeFortMain {

    private static String packages = "host.skiree.springdemo";

    private static String jarPath = "/Users/anorak/Documents/JavaProject/standalone/codefort/src/main/resources/demo.jar";

    private static String password = "123456";

    public static void main(String[] args) {
        FortBanner.banner();
        FortUtil.checkFileType(jarPath);
        FortUtil.checkPassWord(password);
        String fileType = FortUtil.getFileType(jarPath);
        String tempFilePath = FortUtil.getTempFilePath(jarPath, fileType);
        System.out.println(tempFilePath);
        CustomRegister customRegister = CustomUtil.getInstance().get(fileType);
        List<String> allFilePath = customRegister.decompression(jarPath, tempFilePath, new ArrayList<>());
        System.out.println(allFilePath);
    }

}
