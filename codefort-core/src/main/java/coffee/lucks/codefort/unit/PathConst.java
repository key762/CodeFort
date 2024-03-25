package coffee.lucks.codefort.unit;

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


    public static final String[] CODE_FORT_FILES = {"CoreAgent.class", "InputForm.class", "InputForm$1.class",
            "JarDecryptor.class", "AgentTransformer.class", "Const.class", "CmdLineOption.class",
            "EncryptUtils.class", "IoUtils.class", "JarUtils.class", "Log.class", "StrUtils.class",
            "SysUtils.class"};

}
