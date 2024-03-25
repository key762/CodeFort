package coffee.lucks.codefort;

import coffee.lucks.codefort.arms.FileArm;
import coffee.lucks.codefort.model.FortUnit;
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
    @Parameter(property = "password", defaultValue = "000000")
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
    @Parameter(property = "biosMark", defaultValue = "1q2w3e4r")
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
        // 准备开始加密
        String res = new CodeFort().doEncryptJar(fortUnit);
        logger.info(String.format("%s 加密完成", FileArm.getName(res)));
        logger.info("\u001B[33m" + "联系QQ 2940397985" + "\u001B[0m");
    }

}
