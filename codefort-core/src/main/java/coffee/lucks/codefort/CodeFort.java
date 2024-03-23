package coffee.lucks.codefort;

import cn.hutool.core.io.FileUtil;
import coffee.lucks.codefort.unit.FileType;
import coffee.lucks.codefort.unit.FortUnit;
import coffee.lucks.codefort.unit.PathConst;
import coffee.lucks.codefort.util.ClassUtil;
import coffee.lucks.codefort.util.EncryptUtil;
import coffee.lucks.codefort.util.HandleUtil;
import coffee.lucks.codefort.util.StringUtil;

import java.io.File;
import java.util.List;
import java.util.Map;

public class CodeFort {

    public String doEncryptJar(FortUnit fortUnit) {
        String jarPath = fortUnit.getUnitPath();
        List<String> includeFiles = StringUtil.getList(fortUnit.getLibs());
        String packages = fortUnit.getPackages();
        String excludeClass = fortUnit.getExcludes();
        FileType fileType = StringUtil.getFileType(fortUnit.getUnitPath());
        String password = fortUnit.getPassword();
        // 获取临时目录
        String tempFilePath = StringUtil.getTmpDirPath(fortUnit.getUnitPath(), fileType);







        // 解压原执行文件
        List<String> allFilePath = HandleUtil.decompression(jarPath, tempFilePath, includeFiles);
        // 收集所有需加密的class
        Map<String, List<String>> jarClasses = HandleUtil.getEncryptClass(allFilePath, tempFilePath, packages, excludeClass);
        // 加密class类
        EncryptUtil.encryptClass(jarClasses, tempFilePath, fileType, password);
        String libPath = tempFilePath + File.separator + fileType.getMark() + File.separator + "lib" + File.separator;
        // 清理已加密的class对应的原方法体
        ClassUtil.clearClassBody(jarClasses, tempFilePath, libPath, fileType);
        // 先打包lib路径下的jar
        for (Map.Entry<String, List<String>> entry : jarClasses.entrySet()) {
            if (!"CLASSES".equals(entry.getKey()) && !"ROOT".equals(entry.getKey())) {
                HandleUtil.compress(libPath + entry.getKey() + PathConst.TEMP_DIR, libPath + entry.getKey() + FileType.JAR.getFullType());
            }
        }
        // 删除lib路径下的jar解压出的临时文件
        for (String file : includeFiles) {
            FileUtil.del(libPath + file.replace(FileType.JAR.getFullType(), PathConst.TEMP_DIR));
        }
        // 最终打包
        String result = HandleUtil.compress(tempFilePath, jarPath.replace(fileType.getFullType(), "-encrypted" + fileType.getFullType()));
        FileUtil.del(tempFilePath);
        return result;
    }

}
