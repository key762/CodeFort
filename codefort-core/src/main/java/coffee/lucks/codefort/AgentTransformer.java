package coffee.lucks.codefort;

import coffee.lucks.codefort.embeds.util.EncryptUtil;
import coffee.lucks.codefort.embeds.arms.StrArm;
import coffee.lucks.codefort.embeds.util.StringUtil;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class AgentTransformer implements ClassFileTransformer {

    private String pwd;

    public AgentTransformer(String pwd) {
        this.pwd = pwd;
    }

    /**
     * 解密加密的文件
     *
     * @param loader              the defining loader of the class to be transformed,
     *                            may be <code>null</code> if the bootstrap loader
     * @param className           the name of the class in the internal form of fully
     *                            qualified class and interface names as defined in
     *                            <i>The Java Virtual Machine Specification</i>.
     *                            For example, <code>"java/util/List"</code>.
     * @param classBeingRedefined if this is triggered by a redefine or retransform,
     *                            the class being redefined or retransformed;
     *                            if this is a class load, <code>null</code>
     * @param domain              the protection domain of the class being defined or redefined
     * @param classBuffer         the input byte buffer in class file format - must not be modified
     * @return 解密后的字节码
     */
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain domain, byte[] classBuffer) {
        if (className == null || domain == null || loader == null) return classBuffer;
        String projectPath = domain.getCodeSource().getLocation().getPath();
        projectPath = StringUtil.getRootPath(projectPath);
        if (StrArm.isEmpty(projectPath)) return classBuffer;
        className = className.replace("/", ".").replace("\\", ".");
        byte[] bytes = EncryptUtil.decryptFile(projectPath, className, this.pwd);
        // CAFE BABE,表示解密成功
        if (bytes != null && bytes[0] == -54 && bytes[1] == -2 && bytes[2] == -70 && bytes[3] == -66) {
            return bytes;
        }
        return classBuffer;
    }

}
