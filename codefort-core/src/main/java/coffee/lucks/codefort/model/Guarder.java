package coffee.lucks.codefort.model;

import coffee.lucks.codefort.arms.FileArm;
import coffee.lucks.codefort.arms.StrArm;
import coffee.lucks.codefort.unit.FileType;
import coffee.lucks.codefort.util.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Guarder extends FortUnit {

    private FileType type;

    private String targetStr;

    private File targetFile;

    private File targetLibDir;

    private File targetClassesDir;

    private List<String> allFile;

    private List<String> libJars;

    private List<String> libJarNames;

    private List<File> encryptClass;

    private List<String> includeJars;

    public Guarder(FortUnit unit) {
        super(unit);
        this.type = StringUtil.getFileType(this.unitPath);
        this.targetStr = StringUtil.getTmpDirPath(this.unitPath, this.type);
//        this.targetStr = FileArm.getTmpDirPath();
        this.targetFile = FileArm.mkDir(this.targetStr);
        this.allFile = new ArrayList<>();
        this.libJars = new ArrayList<>();
        this.libJarNames = new ArrayList<>();
        this.encryptClass = new ArrayList<>();
        this.includeJars = StrArm.toListByRegex(this.libs, ",");
        this.targetLibDir = new File(this.targetFile, (FileType.JAR.equals(this.type) ? "BOOT-INF" : "WEB-INF")
                + File.separator + "lib");
        this.targetClassesDir = new File(this.targetFile, (FileType.JAR.equals(this.type) ? "BOOT-INF" : "WEB-INF")
                + File.separator + "classes");
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public String getTargetStr() {
        return targetStr;
    }

    public void setTargetStr(String targetStr) {
        this.targetStr = targetStr;
    }

    public File getTargetFile() {
        return targetFile;
    }

    public void setTargetFile(File targetFile) {
        this.targetFile = targetFile;
    }

    public List<String> getAllFile() {
        return allFile;
    }

    public void setAllFile(List<String> allFile) {
        this.allFile = allFile;
    }

    public List<String> getLibJars() {
        return libJars;
    }

    public void setLibJars(List<String> libJars) {
        this.libJars = libJars;
    }

    public List<File> getEncryptClass() {
        return encryptClass;
    }

    public void setEncryptClass(List<File> encryptClass) {
        this.encryptClass = encryptClass;
    }

    public List<String> getIncludeJars() {
        return includeJars;
    }

    public void setIncludeJars(List<String> includeJars) {
        this.includeJars = includeJars;
    }

    public List<String> getLibJarNames() {
        return libJarNames;
    }

    public void setLibJarNames(List<String> libJarNames) {
        this.libJarNames = libJarNames;
    }

    public File getTargetLibDir() {
        return targetLibDir;
    }

    public void setTargetLibDir(File targetLibDir) {
        this.targetLibDir = targetLibDir;
    }

    public File getTargetClassesDir() {
        return targetClassesDir;
    }

    public void setTargetClassesDir(File targetClassesDir) {
        this.targetClassesDir = targetClassesDir;
    }

}
