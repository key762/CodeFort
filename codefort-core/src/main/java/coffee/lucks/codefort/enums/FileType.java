package coffee.lucks.codefort.enums;

/**
 * 文件类型枚举类
 */
public enum FileType {

    JAR("jar", ".jar","BOOT-INF"),
    WAR("war", ".war","WEB-INF");

    FileType(String type, String fullType, String mark) {
        this.type = type;
        this.fullType = fullType;
        this.mark = mark;
    }

    private String type;

    private String fullType;

    private String mark;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFullType() {
        return fullType;
    }

    public void setFullType(String fullType) {
        this.fullType = fullType;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
