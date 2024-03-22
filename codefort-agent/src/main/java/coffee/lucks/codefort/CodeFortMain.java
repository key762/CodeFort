package coffee.lucks.codefort;

import coffee.lucks.codefort.unit.FortUnit;

import java.util.Date;

/**
 * java -javaagent:codefort-agent-1.0.0.jar='-pwd 123456' -jar demo-encrypted.jar
 */
public class CodeFortMain {

    private static String packages = "host.skiree.springdemo";

    private static String excludeClass = "";

    private static String jarPath = "/Users/anorak/Documents/JavaProject/standalone/codefort/codefort-core/src/main/resources/demo.jar";

    private static String password = "123456";

    private static String includeFiles = "hutool-setting-5.8.26.jar";

    public static void main(String[] args) {
        FortUnit fortUnit = new FortUnit();
        fortUnit.setUnitPath(jarPath);
        fortUnit.setPassword(password);
        fortUnit.setBuildTime();
        fortUnit.setStartTime(new Date());
        fortUnit.setEndTime(new Date());
        fortUnit.setBiosMark("1q2w3e4R");
        fortUnit.setLibs(includeFiles);
        fortUnit.setPackages(packages);
        fortUnit.setExcludes(excludeClass);
        String res = new CodeFort().doEncryptJar(fortUnit);
        System.out.println(res);
    }

}
