package coffee.lucks.codefort.agent;

import coffee.lucks.codefort.util.EncryptUtil;
import coffee.lucks.codefort.unit.FortLog;
import coffee.lucks.codefort.arms.StrArm;
import coffee.lucks.codefort.util.StringUtil;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class AgentTransformer implements ClassFileTransformer {

    private String pwd;

    public AgentTransformer(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain domain, byte[] classBuffer) {
        if (className == null || domain == null || loader == null) return classBuffer;
        String projectPath = domain.getCodeSource().getLocation().getPath();
        FortLog.info("项目运行路径 : " + projectPath);
        projectPath = StringUtil.getRootPath(projectPath);
        FortLog.info("项目运行真实路径 : " + projectPath);
        if (StrArm.isEmpty(projectPath)) return classBuffer;
        className = className.replace("/", ".").replace("\\", ".");
        FortLog.info("当前还原的class名称 : " + className);
        byte[] bytes = EncryptUtil.decryptFile(projectPath, className, this.pwd);
        // CAFE BABE,表示解密成功
        if (bytes != null && bytes[0] == -54 && bytes[1] == -2 && bytes[2] == -70 && bytes[3] == -66) {
            return bytes;
        }
        return classBuffer;
    }

}
