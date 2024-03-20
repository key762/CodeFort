package coffee.lucks.codefort.util;

import cn.hutool.core.io.FileUtil;
import javassist.ClassPool;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ClearUtil {

    public static void clear(Map<String, List<String>> jarClasses, String tempFilePath, String libPath, String fileType) {
        try {
            for (Map.Entry<String, List<String>> entry : jarClasses.entrySet()) {
                //初始化javassist
                ClassPool pool = ClassPool.getDefault();
                //lib目录
                ClassUtil.loadClassPath(pool, new String[]{libPath});
                //要修改的class所在的目录
                pool.insertClassPath(tempFilePath + File.separator + FortUtil.realPath(entry.getKey(), null, fileType));
                //修改class方法体，并保存文件
                for (String classname : entry.getValue()) {
                    byte[] bts = ClassUtil.rewriteMethod(pool, classname);
                    if (bts != null) {
                        String path = tempFilePath + File.separator + FortUtil.realPath(entry.getKey(), classname, fileType);
                        FileUtil.writeBytes(bts, path);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
