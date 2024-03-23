package coffee.lucks.codefort.model;

import java.util.ArrayList;
import java.util.List;

public class DecFile {

    private List<String> allCls;


    private List<String> libJars;


    public DecFile() {
        this.allCls = new ArrayList<>();
        this.libJars = new ArrayList<>();
    }

    public List<String> getAllCls() {
        return allCls;
    }

    public void setAllCls(List<String> allCls) {
        this.allCls = allCls;
    }

    public List<String> getLibJars() {
        return libJars;
    }

    public void setLibJars(List<String> libJars) {
        this.libJars = libJars;
    }
}
