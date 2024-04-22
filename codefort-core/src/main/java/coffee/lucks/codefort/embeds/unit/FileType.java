package coffee.lucks.codefort.embeds.unit;

/**
 * 文件类型枚举类
 */
public enum FileType {

    JAR(".jar"),
    WAR(".war");

    FileType(String fullType) {
        this.fullType = fullType;
    }

    private final String fullType;

    public String getFullType() {
        return fullType;
    }

}
