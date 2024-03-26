package coffee.lucks.codefort;

import coffee.lucks.codefort.compile.FortCompile;
import coffee.lucks.codefort.embeds.unit.FortUnit;
import coffee.lucks.codefort.embeds.unit.PathConst;

import java.util.Date;

/**
 * java -javaagent:demo-encrypted.jar='-pwd 123456' -jar demo-encrypted.jar
 * java -javaagent:demo-encrypted.jar -jar demo-encrypted.jar
 */
public class CodeFortMain {

    private static String packages = "host.skiree.springdemo";

    private static String excludeClass = "";

    private static String jarPath = "/Users/anorak/Documents/JavaProject/standalone/codefort/codefort-core/src/main/resources/demo.jar";

    private static String password = "123456";

    private static String includeFiles = "license-client-2.0.jar";

    public static void main(String[] args) {
        FortUnit fortUnit = new FortUnit();
        fortUnit.setUnitPath(jarPath);
        fortUnit.setPassword(password);
        fortUnit.setBuildTime();
        fortUnit.setStartTime(new Date());
        fortUnit.setEndTime(new Date());
        fortUnit.setBiosMark(PathConst.DEFAULT_PASSWORD);
        fortUnit.setLibs(includeFiles);
        fortUnit.setPackages(packages);
        fortUnit.setExcludes(excludeClass);
        String res = FortCompile.fc.doEncrypt(fortUnit);
        System.out.println(res);
    }

}
