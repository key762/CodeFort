package coffee.lucks.codefort.unit;

import java.util.Date;

public class CodeFort {

    /**
     * 说明
     */
    private String explain;

    /**
     * 密码
     */
    private String password;

    /**
     * 可用毫秒数
     */
    private long availableTime;

    /**
     * 编译时间
     */
    private Date buildTime;


    /**
     * CPU序列号
     */
    private String cpuSerial;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * IP信息
     */
    private String ipInfo;

    /**
     * CodeFort版本
     */
    private String version;


    public CodeFort() {
    }

    public CodeFort(String explain, String password, long availableTime, Date buildTime, String cpuSerial, Date startTime, Date endTime, String ipInfo, String version) {
        this.explain = explain;
        this.password = password;
        this.availableTime = availableTime;
        this.buildTime = buildTime;
        this.cpuSerial = cpuSerial;
        this.startTime = startTime;
        this.endTime = endTime;
        this.ipInfo = ipInfo;
        this.version = version;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(long availableTime) {
        this.availableTime = availableTime;
    }

    public Date getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(Date buildTime) {
        this.buildTime = buildTime;
    }

    public String getCpuSerial() {
        return cpuSerial;
    }

    public void setCpuSerial(String cpuSerial) {
        this.cpuSerial = cpuSerial;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getIpInfo() {
        return ipInfo;
    }

    public void setIpInfo(String ipInfo) {
        this.ipInfo = ipInfo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "CodeFort{" +
                "explain='" + explain + '\'' +
                ", password='" + password + '\'' +
                ", availableTime=" + availableTime +
                ", buildTime=" + buildTime +
                ", cpuSerial='" + cpuSerial + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", ipInfo='" + ipInfo + '\'' +
                ", version='" + version + '\'' +
                '}';
    }

}
