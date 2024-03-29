package coffee.lucks.codefort.embeds.unit;

public class FortConst {

    /**
     * 版本
     */
    public static final String CODE_FORT_VERSION = "1.0.0";

    /**
     * RSA公钥
     */
    public static String RSA_PUBLIC_KEY = "";

    /**
     * RSA公钥静态-编译默认
     */
    public static final String RSA_PUBLIC_KEY_FINAL = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsC8XjlV8kaxg8mVdYfuJ\n" +
            "uMNx7leWMloj/PGhx84FVbaIFFsJei2+/hVAdKkxMH/NpqeW6bDg2W9spfMqx1uq\n" +
            "AtRKr0aIKyBM/V4SC8/9XmIpws0kLAEDGZa28LnUgl40gGuxjhFWvDmkUiv+r1Sr\n" +
            "QYBMFPptGrrSgncLyJBTma5vWoxZrfPmyfuX4vkaqeTkqbIdsw6er8LD9u5TStNp\n" +
            "zrGAP/OnqhhX0I+KbeIPzg+P4kV0m2wodHlG9VQ61zlwG6UGm5JkkXn6vrgbD/Tx\n" +
            "akVW0bqkEtbd/sO01DwUUM3/RPrIwSAjeOoEQPUMAc2EN4B687ZVDKPE2NUXsbAI\n" +
            "eQIDAQAB";

    /**
     * class文件拓展名
     */
    public static final String EXT_CLASS = ".class";

    /**
     * jar解压的目录名后缀
     */
    public static final String TEMP_DIR = "_temp";

    /**
     * class加密后的文件
     */
    public static final String ENCRYPT_NAME = ".classes";

    /**
     * 加密后文件存放位置
     */
    public static final String ENCRYPT_PATH = "META-INF/" + ENCRYPT_NAME + "/";

    /**
     * 环境模式
     */
    public static boolean DEBUG = false;

    /**
     * 编译信息
     */
    public static final String CODE_FORT_INFO = "CodeFortAlwaysSupportYou";

    /**
     * 默认密码 - CodeFortAlwaysSupportYou
     */
    public static final String DEFAULT_PASSWORD = "cfasy";

    /**
     * 需要嵌入的字节码文件
     */
    public static final String[] CODE_FORT_FILES = {
            "CodeFortAgent$1.class","FortSocket$1.class",

            "AgentTransformer.class", "CodeFortAgent.class", "FortSocket.class", "RSAManualKeys.class",
            "ByteArm.class", "DateArm.class", "FileArm.class", "IoArm.class", "MapArm.class", "StrArm.class", "SysArm.class",
            "FileType.class", "FortBanner.class", "FortLog.class", "FortUnit.class", "Guarder.class", "FortConst.class",
            "CmdLineUtil.class", "EncryptUtil.class", "HandleUtil.class", "SecurityUtil.class", "StringUtil.class"};

}
