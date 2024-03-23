package coffee.lucks.codefort;

import cn.hutool.core.io.FileUtil;
import coffee.lucks.codefort.model.DecFile;
import coffee.lucks.codefort.unit.FileType;
import coffee.lucks.codefort.unit.FortUnit;
import coffee.lucks.codefort.unit.PathConst;
import coffee.lucks.codefort.util.ClassUtil;
import coffee.lucks.codefort.util.EncryptUtil;
import coffee.lucks.codefort.util.HandleUtil;
import coffee.lucks.codefort.util.StringUtil;

import java.io.File;
import java.util.List;

public class CodeFort {

    public String doEncryptJar(FortUnit fortUnit) {
        FileUtil.del(StringUtil.getTmpDirPath(fortUnit.getUnitPath(), FileType.JAR));
        String jarPath = fortUnit.getUnitPath();
        List<String> includeFiles = StringUtil.getList(fortUnit.getLibs());
        String packages = fortUnit.getPackages();
        String excludeClass = fortUnit.getExcludes();
        FileType fileType = StringUtil.getFileType(fortUnit.getUnitPath());
        String password = fortUnit.getPassword();
        // 获取临时目录
        String tempFilePath = StringUtil.getTmpDirPath(fortUnit.getUnitPath(), fileType);
        DecFile decFile = new DecFile();
        // 解压原执行文件
        DecFile allFilePath = HandleUtil.decompression(jarPath, tempFilePath, includeFiles, decFile);
        // 收集所有需加密的class
        List<File> encryptFile = HandleUtil.getEncryptClass(allFilePath.getAllCls(), tempFilePath, packages, excludeClass);
//        for (File file : encryptFile) {
//            System.out.println(file.getAbsoluteFile());
//        }
        // 加密class类
        EncryptUtil.encryptClass(encryptFile, tempFilePath, fileType, password);
        // 清空class方法体并保存
        ClassUtil.clearClassMethod(encryptFile, tempFilePath);
        // 先打包lib路径下的jar
        for (String file : decFile.getLibJars()) {
            HandleUtil.compress(file.replace(FileType.JAR.getFullType(), PathConst.TEMP_DIR), file);
        }
        // 删除lib路径下的jar解压出的临时文件
        for (String file : decFile.getLibJars()) {
            FileUtil.del(file.replace(FileType.JAR.getFullType(), PathConst.TEMP_DIR));
        }
        // 最终打包
        String result = HandleUtil.compress(tempFilePath, jarPath.replace(fileType.getFullType(), "-encrypted" + fileType.getFullType()));
//        FileUtil.del(tempFilePath);
        return result;
    }

}
