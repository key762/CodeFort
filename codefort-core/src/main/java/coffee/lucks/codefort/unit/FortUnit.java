package coffee.lucks.codefort.unit;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Date;

public class FortUnit {

    /**
     * 执行文件路径
     */
    private String unitPath;

    /**
     * 密码
     */
    private String password;

    /**
     * 机器码
     */
    private String biosMark;

    /**
     * 编译时间
     */
    private String buildTime;

    /**
     * 运行开始时间
     */
    private String startTime;

    /**
     * 运行结束时间
     */
    private String endTime;

    /**
     * 依赖包信息
     */
    private String libs;

    /**
     * 加密路径信息
     */
    private String packages;

    /**
     * 加密排除信息
     */
    private String excludes;

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
        if (StrUtil.isEmpty(password)) {
            this.password = "000000";
        } else {
            this.password = password;
        }
    }

    public String getBuildTime() {
        return buildTime;
    }

    public void setBuildTime() {
        this.buildTime = DateUtil.formatDateTime(new Date());
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        if (startTime == null) {
            this.startTime = "";
        } else {
            this.startTime = DateUtil.formatDateTime(startTime);
        }
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        if (endTime == null) {
            this.endTime = "";
        } else {
            this.endTime = DateUtil.formatDateTime(endTime);
        }
    }

    public String getLibs() {
        return libs;
    }

    public void setLibs(String libs) {
        if (StrUtil.isEmpty(libs)) {
            this.libs = "";
        } else {
            this.libs = libs;
        }
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        if (StrUtil.isEmpty(packages)) {
            this.packages = "";
        } else {
            this.packages = packages;
        }
    }

    public String getExcludes() {
        return excludes;
    }

    public void setExcludes(String excludes) {
        if (StrUtil.isEmpty(excludes)) {
            this.excludes = "";
        } else {
            this.excludes = excludes;
        }
    }

    public String getBiosMark() {
        return biosMark;
    }

    public void setBiosMark(String biosMark) {
        if (StrUtil.isEmpty(biosMark)) {
            biosMark = "1q2w3e4r";
        } else {
            this.biosMark = biosMark;
        }
    }

}
