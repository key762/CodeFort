package coffee.lucks.codefort.embeds.unit;

import coffee.lucks.codefort.embeds.arms.FileArm;
import coffee.lucks.codefort.embeds.arms.MapArm;
import coffee.lucks.codefort.embeds.arms.StrArm;
import coffee.lucks.codefort.embeds.arms.SysArm;
import coffee.lucks.codefort.embeds.util.SecurityUtil;
import coffee.lucks.codefort.embeds.util.StringUtil;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public byte[] getNecessaryInfo() {
        Map<String, Object> res = new HashMap<>();
        res.put("needSocket", "false");
        res.put("needBiosMark", "false");
        res.put("isDebug", this.isDebug);
        res.put("rsaPublicKey", this.rsaPublicKey);
        // 设置
        res.put("timeJudge", "no");
        if (!StrArm.isEmpty(this.startTime) && !StrArm.isEmpty(this.endTime)) {
            res.put("buildTime", this.buildTime);
            res.put("startTime", this.startTime);
            res.put("endTime", this.endTime);
            res.put("timeJudge", "region");
        } else if (this.availableTime != 0) {
            res.put("buildTime", this.buildTime);
            res.put("availableTime", this.availableTime);
            res.put("timeJudge", "deadline");
        }
        // 判断是否需要socket检查
        if (!StrArm.isEmpty(this.host) && this.port != 0) {
            res.put("needSocket", "true");
            res.put("host", this.host);
            res.put("port", this.port);
        }
        // 判断是否需要绑定机器码
        if (!FortConst.DEFAULT_PASSWORD.equalsIgnoreCase(this.biosMark)) {
            res.put("needBiosMark", "true");
            res.put("biosMark", this.biosMark);
        }
        // 设置其他信息
        Map<String, Object> otherInfo = new HashMap<>();
        otherInfo.put("password", this.password);
        otherInfo.put("buildTime", this.buildTime);
        otherInfo.put("startTime", this.startTime);
        otherInfo.put("endTime", this.endTime);
        otherInfo.put("availableTime", this.availableTime);
        otherInfo.put("explain", this.explain);
        otherInfo.put("version", FortConst.CODE_FORT_VERSION);
        otherInfo.put("cpuSerial", SysArm.getCPUSerialNumber());
        otherInfo.put("ipInfo", SysArm.getInternetIp());
        // 合并
        res.put("contact", SecurityUtil.encryptRsa(this.rsaPublicKey, MapArm.toString(otherInfo)));
        // 综合加密
        return SecurityUtil.encrypt(MapArm.toString(res).getBytes(StandardCharsets.UTF_8), this.password);
    }

}
