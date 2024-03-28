package coffee.lucks.codefort;

import coffee.lucks.codefort.compile.FortCompile;
import coffee.lucks.codefort.embeds.arms.DateArm;
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
//        fortUnit.setStartTime(DateArm.parseDateTime("2024-04-03 12:00:00"));
//        fortUnit.setEndTime(DateArm.parseDateTime("2024-05-01 12:00:00"));
//        fortUnit.setAvailableTime(1000);
        fortUnit.setBiosMark(PathConst.DEFAULT_PASSWORD);
        fortUnit.setLibs(includeFiles);
        fortUnit.setPackages(packages);
        fortUnit.setHost("127.0.0.1");
        fortUnit.setPort(7007);
        fortUnit.setDebug(true);
        fortUnit.setExcludes(excludeClass);
//        fortUnit.setBiosMark("123123");
        String res = FortCompile.fc.doEncrypt(fortUnit);
        System.out.println(res);
    }

}
