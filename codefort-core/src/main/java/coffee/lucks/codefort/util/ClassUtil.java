package coffee.lucks.codefort.util;

import cn.hutool.core.io.FileUtil;
import coffee.lucks.codefort.unit.FileType;
import javassist.*;
import javassist.bytecode.*;
import javassist.compiler.CompileError;
import javassist.compiler.Javac;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClassUtil {

    /**
     * 清理加密class方法体
     *
     * @param jarClasses 加密class集合
     * @param tempPath   临时地址
     * @param libPath    lib目录
     * @param fileType   文件类型
     */
    public static void clearClassBody(Map<String, List<String>> jarClasses, String tempPath, String libPath, FileType fileType) {
        try {
            for (Map.Entry<String, List<String>> entry : jarClasses.entrySet()) {
                ClassPool pool = ClassPool.getDefault();
                ClassUtil.loadClassPath(pool, new String[]{libPath});
                pool.insertClassPath(tempPath + File.separator + StringUtil.getRealPath(entry.getKey(), null, fileType));
                for (String classname : entry.getValue()) {
                    byte[] bts = ClassUtil.rewriteMethod(pool, classname);
                    if (bts != null) {
                        String path = tempPath + File.separator + StringUtil.getRealPath(entry.getKey(), classname, fileType);
                        FileUtil.writeBytes(bts, path);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清空方法
     *
     * @param pool      javassist池
     * @param classname class全类名
     * @return 字节码
     */
    public static byte[] rewriteMethod(ClassPool pool, String classname) {
        String name = "";
        try {
            CtClass cc = pool.getCtClass(classname);
            CtMethod[] methods = cc.getDeclaredMethods();
            for (CtMethod m : methods) {
                name = m.getName();
                if (!m.getName().contains("<") && m.getLongName().startsWith(cc.getName())) {
                    CodeAttribute ca = m.getMethodInfo().getCodeAttribute();
                    if (ca != null && ca.getCodeLength() != 1 && ca.getCode()[0] != -79) {
                        ClassUtil.setBodyKeepParamInfos(m, null, true);
                        if ("void".equalsIgnoreCase(m.getReturnType().getName()) && m.getLongName().endsWith(".main(java.lang.String[])") && m.getMethodInfo().getAccessFlags() == 9) {
                            m.insertBefore("System.out.println(\"\\n启动💰校验失败,请联系开发者或管理员处理.\\n\");");
                        }
                    }

                }
            }
            return cc.toBytecode();
        } catch (Exception e) {
            System.out.println("处理class对象时异常(name:" + name + ")");
        }
        return null;
    }

    /**
     * 清空方法体信息但保留参数信息
     *
     * @param m       方法对象
     * @param src     方法对象
     * @param rebuild 是否重构对象
     * @throws CannotCompileException 不能编译异常
     */
    public static void setBodyKeepParamInfos(CtMethod m, String src, boolean rebuild) throws CannotCompileException {
        CtClass cc = m.getDeclaringClass();
        if (cc.isFrozen()) {
            throw new RuntimeException(cc.getName() + " 类被冻结");
        }
        CodeAttribute ca = m.getMethodInfo().getCodeAttribute();
        if (ca == null) {
            throw new RuntimeException("没有方法体");
        } else {
            CodeIterator iterator = ca.iterator();
            Javac jv = new Javac(cc);
            try {
                int nvars = jv.recordParams(m.getParameterTypes(), Modifier.isStatic(m.getModifiers()));
                jv.recordParamNames(ca, nvars);
                jv.recordLocalVariables(ca, 0);
                jv.recordReturnType(Descriptor.getReturnType(m.getMethodInfo().getDescriptor(), cc.getClassPool()), false);
                Bytecode b = jv.compileBody(m, src);
                int stack = b.getMaxStack();
                int locals = b.getMaxLocals();
                if (stack > ca.getMaxStack()) {
                    ca.setMaxStack(stack);
                }
                if (locals > ca.getMaxLocals()) {
                    ca.setMaxLocals(locals);
                }
                int pos = iterator.insertEx(b.get());
                iterator.insert(b.getExceptionTable(), pos);
                if (rebuild) {
                    m.getMethodInfo().rebuildStackMapIf6(cc.getClassPool(), cc.getClassFile2());
                }
            } catch (NotFoundException var12) {
                throw new CannotCompileException(var12);
            } catch (CompileError var13) {
                throw new CannotCompileException(var13);
            } catch (BadBytecode var14) {
                throw new CannotCompileException(var14);
            }
        }
    }

    /**
     * 加载指定目录下的所有Jar
     *
     * @param pool  javassist池
     * @param paths lib路径
     */
    public static void loadClassPath(ClassPool pool, String[] paths) {
        for (String path : paths) {
            List<File> jars = FileUtil.loopFiles(path).stream()
                    .filter(File::isFile)
                    .filter(x -> FileUtil.extName(x).equalsIgnoreCase(FileType.JAR.getType()))
                    .collect(Collectors.toList());
            for (File jar : jars) {
                try {
                    pool.insertClassPath(jar.getAbsolutePath());
                } catch (Exception e) {
                    throw new RuntimeException("加载Jar时异常(file:" + jar.getAbsolutePath() + ")");
                }
            }
        }
    }
}
