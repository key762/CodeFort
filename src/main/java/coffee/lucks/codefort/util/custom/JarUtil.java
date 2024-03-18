package coffee.lucks.codefort.util.custom;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import coffee.lucks.codefort.util.FortUtil;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class JarUtil extends CustomRegister {
    @Override
    public String utilName() {
        return "jar";
    }

    @Override
    public List<String> decompression(String filePath, String targetDir, List<String> includeFiles) {
        List<String> list = new ArrayList<>();
        targetDir = FortUtil.convertPath(targetDir);
        FileUtil.mkdir(targetDir);
        try (FileInputStream fin = new FileInputStream(filePath);
             JarArchiveInputStream jis = new JarArchiveInputStream(fin)) {
            JarArchiveEntry jarEntry;
            while ((jarEntry = jis.getNextEntry()) != null) {
                String entryName = jarEntry.getName();
                String fullPath = targetDir + File.separator + entryName;
                list.add(fullPath);
                if (entryName.endsWith(FileNameUtil.EXT_JAR)) {
                    ByteArrayOutputStream jos0 = new ByteArrayOutputStream();
                    IOUtils.copy(jis, jos0);
                    byte[] bytes = jos0.toByteArray();
                    FileUtil.writeBytes(bytes, fullPath);
                    if (includeFiles != null && includeFiles.contains(FileUtil.getName(entryName))) {
                        List<String> list0 = decompression(fullPath, fullPath.replace(FileNameUtil.EXT_JAR, FortUtil.TEMP_DIR), includeFiles);
                        list.addAll(list0);
                    }
                } else if (jarEntry.isDirectory()) {
                    FileUtil.mkdir(fullPath);
                } else {
                    ByteArrayOutputStream jos0 = new ByteArrayOutputStream();
                    IOUtils.copy(jis, jos0);
                    byte[] bytes = jos0.toByteArray();
                    FileUtil.writeBytes(bytes, fullPath);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}