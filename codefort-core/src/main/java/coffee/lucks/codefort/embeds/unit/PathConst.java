package coffee.lucks.codefort.embeds.unit;

public class PathConst {
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


    public static final String[] CODE_FORT_FILES = {
            "AgentTransformer.class", "CodeFortAgent.class", "RSAManualKeys.class",
            "ByteArm.class", "DateArm.class", "FileArm.class", "IoArm.class", "StrArm.class",
            "FileType.class", "FortBanner.class", "FortLog.class", "FortUnit.class", "Guarder.class", "PathConst.class",
            "AESUtil.class", "CmdLine.class", "EncryptUtil.class", "HandleUtil.class", "MD5Util.class", "StringUtil.class"};

}
