package coffee.lucks.codefort.unit;

import lombok.Data;

import java.util.Date;

@Data
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

}
