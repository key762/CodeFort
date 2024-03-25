package coffee.lucks.codefort.compile;

import coffee.lucks.codefort.embeds.arms.FileArm;
import coffee.lucks.codefort.embeds.unit.FortUnit;
import coffee.lucks.codefort.embeds.unit.Guarder;
import coffee.lucks.codefort.embeds.unit.FileType;
import coffee.lucks.codefort.embeds.unit.PathConst;
import coffee.lucks.codefort.embeds.util.EncryptUtil;
import coffee.lucks.codefort.embeds.util.HandleUtil;

public class FortCompile {

    public final static FortCompile fc = new FortCompile();

    /**
     * 对执行文件进行加密
     *
     * @param fortUnit 执行对象
     * @return 加密后的执行文件路径
     */
    public String doEncrypt(FortUnit fortUnit) {
        Guarder guarder = new Guarder(fortUnit);
        HandleUtil.decompression(guarder.getUnitPath(), guarder.getTargetStr(), guarder);
        HandleUtil.getEncryptClass(guarder);
        //将本项目打包进去
        ClassCompile.codeFortAgent(guarder);
        EncryptUtil.encryptClass(guarder);
        ClassCompile.clearClassMethod(guarder);
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
