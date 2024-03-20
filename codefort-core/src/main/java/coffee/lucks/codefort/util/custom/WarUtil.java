package coffee.lucks.codefort.util.custom;

import java.util.List;

public class WarUtil extends CustomRegister {
    @Override
    public String utilName() {
        return "war";
    }

    @Override
    public List<String> decompression(String filePath, String targetDir, List<String> includeFiles) {
        return null;
    }

    @Override
    public String compress(String jarDir, String targetJar) {
        return null;
    }
}
