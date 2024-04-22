package coffee.lucks.codefort;

import coffee.lucks.codefort.compile.FortCompile;
import coffee.lucks.codefort.embeds.arms.FileArm;
import coffee.lucks.codefort.embeds.unit.FortUnit;
import coffee.lucks.codefort.embeds.unit.FortConst;
import org.apache.maven.model.Build;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.Date;

@Mojo(name = "CodeFort", defaultPhase = LifecyclePhase.PACKAGE)
public class CodeFortPlugin extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    /**
     * 密码
     */
    @Parameter(property = "password", defaultValue = FortConst.DEFAULT_PASSWORD)
    private String password;

    /**
     * 开始时间
     */
    @Parameter(property = "startTime", defaultValue = "")
    private Date startTime;

    /**
     * 结束时间
     */
    @Parameter(property = "endTime", defaultValue = "")
    private Date endTime;

    /**
     * 密码
     */
    @Parameter(property = "biosMark", defaultValue = FortConst.DEFAULT_PASSWORD)
    private String biosMark;

    /**
     * 可用时间(单位毫秒)
     */
    @Parameter(property = "availableTime", defaultValue = "0")
    private int availableTime;

    /**
     * lib下需加密包
     */
    @Parameter(property = "libs")
    private String libs;

    /**
     * 加密路径
     */
    @Parameter(property = "packages")
    private String packages;

    /**
     * 排除路径
     */
    @Parameter(property = "excludes")
    private String excludes;

    /**
     * Socket主机地址
     */
    @Parameter(property = "host")
    public String host;


    /**
     * Socket主机端口
     */
    @Parameter(property = "port")
    public int port;

    /**
     * 备注说明
     */
    @Parameter(property = "explain")
    public String explain;

    /**
     * 是否DEBUG
     */
    @Parameter(property = "isDebug", defaultValue = "false")
    private boolean isDebug;

    /**
     * RSA公钥
     */
    @Parameter(property = "rsaPublicKey", defaultValue = FortConst.RSA_PUBLIC_KEY_FINAL)
    private String rsaPublicKey;

    /**
     * 依赖外部lib路径
     */
    @Parameter(property = "relyLibPath", defaultValue = "")
    private String relyLibPath;

    public void execute() {
        Log logger = getLog();
        logger.info("\u001B[34m" + "CodeFort 致力于保卫您的代码安全" + "\u001B[0m");
        Build build = project.getBuild();
        String targetJar = build.getFinalName() + "." + project.getPackaging();
        logger.info(String.format("%s 编译完成", targetJar));
        targetJar = build.getDirectory() + File.separator + targetJar;
        FortUnit fortUnit = new FortUnit();
        fortUnit.setUnitPath(targetJar);
        fortUnit.setPassword(password);
        fortUnit.setBuildTime();
        fortUnit.setStartTime(startTime);
        fortUnit.setEndTime(endTime);
        fortUnit.setBiosMark(biosMark);
        fortUnit.setLibs(libs);
        fortUnit.setPackages(packages);
        fortUnit.setExcludes(excludes);
        fortUnit.setAvailableTime(availableTime);
        fortUnit.setDebug(isDebug);
        fortUnit.setExplain(explain);
        fortUnit.setRsaPublicKey(rsaPublicKey);
        fortUnit.setHost(host);
        fortUnit.setPort(port);
        fortUnit.setRelyLibPath(relyLibPath);
        // 准备开始加密
        String res = FortCompile.fc.doEncrypt(fortUnit);
        logger.info(String.format("%s 加密完成", FileArm.getName(res)));
        logger.info("\u001B[33m" + "联系微信号: Anroak" + "\u001B[0m");
    }

}
