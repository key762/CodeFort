package coffee.lucks.codefort.agent;

import coffee.lucks.codefort.util.EncryptUtil;

import java.io.File;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class AgentTransformer implements ClassFileTransformer {

    private String[] files = null;//加密后生成的文件路径
    private String[] pwds = null;//密码

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classFileBuffer) {
        if (getFiles() == null || getFiles().length == 0) {
            return classFileBuffer;
        }
        //遍历所有的文件
        for (int i = 0; i < files.length; i++) {
            String file = files[i];
            String pwd = pwds[i];
            byte[] bytes = EncryptUtil.decryptFile(file, className.replace(File.separator, "."), pwd);
            if (bytes != null) {
                //CAFEBABE,表示解密成功
                if (bytes[0] == -54 && bytes[1] == -2 && bytes[2] == -70 && bytes[3] == -66) {
                    return bytes;
                }
            }
        }
        return classFileBuffer;

    }

    public String[] getFiles() {
        return files;
    }

    public void setFiles(String[] files) {
        this.files = files;
    }

    public String[] getPwds() {
        return pwds;
    }

    public void setPwds(String[] pwds) {
        this.pwds = pwds;
    }
}
