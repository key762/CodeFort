package coffee.lucks.codefort.embeds.unit;

import coffee.lucks.codefort.embeds.arms.DateArm;
import coffee.lucks.codefort.embeds.arms.StrArm;

import java.util.Date;

public class FortUnit {

    /**
     * 执行文件路径
     */
    public String unitPath;

    /**
     * 密码
     */
    public String password;

    /**
     * 机器码
     */
    public String biosMark;

    /**
     * 编译时间
     */
    public String buildTime;

    /**
     * 运行开始时间
     */
    public String startTime;

    /**
     * 运行结束时间
     */
    public String endTime;

    /**
     * 可用时间
     */
    public int availableTime;

    /**
     * 依赖包信息
     */
    public String libs;

    /**
     * 加密路径信息
     */
    public String packages;

    /**
     * 加密排除信息
     */
    public String excludes;

    /**
     * Socket主机地址
     */
    public String host;


    /**
     * Socket主机端口
     */
    public int port;

    /**
     * 备注说明
     */
    public String explain;

    /**
     * 是否DEBUG
     */
    public boolean isDebug;

    /**
     * RSA公钥
     */
    public String rsaPublicKey;

    public FortUnit() {
    }

    public FortUnit(FortUnit unit) {
        this.unitPath = unit.unitPath;
        this.password = unit.password;
        this.biosMark = unit.biosMark;
        this.buildTime = unit.buildTime;
        this.startTime = unit.startTime;
        this.endTime = unit.endTime;
        this.libs = unit.libs;
        this.packages = unit.packages;
        this.excludes = unit.excludes;
        this.host = unit.host;
        this.port = unit.port;
        this.explain = unit.explain;
        this.availableTime = unit.availableTime;
        this.isDebug = unit.isDebug;
        this.rsaPublicKey = unit.rsaPublicKey;
    }

    public String getUnitPath() {
        return unitPath;
    }

    public void setUnitPath(String unitPath) {
        this.unitPath = unitPath;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (StrArm.isEmpty(password)) {
            this.password = PathConst.DEFAULT_PASSWORD;
        } else {
            this.password = password;
        }
    }

    public String getBuildTime() {
        return buildTime;
    }

    public void setBuildTime() {
        this.buildTime = DateArm.formatDateTime(new Date());
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        if (startTime == null) {
            this.startTime = "";
        } else {
            this.startTime = DateArm.formatDateTime(startTime);
        }
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        if (endTime == null) {
            this.endTime = "";
        } else {
            this.endTime = DateArm.formatDateTime(endTime);
        }
    }

    public String getLibs() {
        return libs;
    }

    public void setLibs(String libs) {
        if (StrArm.isEmpty(libs)) {
            this.libs = "";
        } else {
            this.libs = libs;
        }
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        if (StrArm.isEmpty(packages)) {
            this.packages = "";
        } else {
            this.packages = packages;
        }
    }

    public String getExcludes() {
        return excludes;
    }

    public void setExcludes(String excludes) {
        if (StrArm.isEmpty(excludes)) {
            this.excludes = "";
        } else {
            this.excludes = excludes;
        }
    }

    public String getBiosMark() {
        return biosMark;
    }

    public void setBiosMark(String biosMark) {
        if (StrArm.isEmpty(biosMark)) {
            biosMark = PathConst.DEFAULT_PASSWORD;
        } else {
            this.biosMark = biosMark;
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public int getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(int availableTime) {
        this.availableTime = availableTime;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }

    public String getRsaPublicKey() {
        return rsaPublicKey;
    }

    public void setRsaPublicKey(String rsaPublicKey) {
        this.rsaPublicKey = rsaPublicKey;
    }
}
