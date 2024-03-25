package coffee.lucks.codefort;

import coffee.lucks.codefort.arms.FileArm;
import coffee.lucks.codefort.model.FortUnit;
import coffee.lucks.codefort.model.Guarder;
import coffee.lucks.codefort.unit.FileType;
import coffee.lucks.codefort.unit.PathConst;
import coffee.lucks.codefort.util.ClassUtil;
import coffee.lucks.codefort.util.EncryptUtil;
import coffee.lucks.codefort.util.HandleUtil;

public class CodeFort {

    public String doEncryptJar(FortUnit fortUnit) {
        Guarder guarder = new Guarder(fortUnit);
        HandleUtil.decompression(guarder.getUnitPath(), guarder.getTargetStr(), guarder);
        HandleUtil.getEncryptClass(guarder);
        EncryptUtil.encryptClass(guarder);
        ClassUtil.clearClassMethod(guarder);
        // 先打包lib路径下的jar
        for (String file : guarder.getLibJars()) {
            HandleUtil.compress(file.replace(FileType.JAR.getFullType(), PathConst.TEMP_DIR), file);
        }
        // 删除lib路径下的jar解压出的临时文件
        for (String file : guarder.getLibJars()) {
            FileArm.del(file.replace(FileType.JAR.getFullType(), PathConst.TEMP_DIR));
        }
        // 最终打包
        String result = HandleUtil.compress(guarder.getTargetStr(), guarder.getUnitPath().replace(guarder.getType().getFullType(), "-encrypted" + guarder.getType().getFullType()));
        FileArm.del(guarder.getTargetStr());
        return result;
    }

}
