package coffee.lucks.codefort.compile;

import coffee.lucks.codefort.CodeFortAgent;
import coffee.lucks.codefort.embeds.arms.FileArm;
import coffee.lucks.codefort.embeds.arms.IoArm;
import coffee.lucks.codefort.embeds.arms.StrArm;
import coffee.lucks.codefort.embeds.unit.Guarder;
import coffee.lucks.codefort.embeds.unit.FileType;
import coffee.lucks.codefort.embeds.unit.PathConst;
import coffee.lucks.codefort.embeds.util.StringUtil;
import javassist.*;
import javassist.bytecode.*;
import javassist.compiler.CompileError;
import javassist.compiler.Javac;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClassCompile {

    /**
     * 将本项目打包到目标执行文件中
     *
     * @param guarder
     */
    public static void codeFortAgent(Guarder guarder) {
        List<String> thisJarPaths = new ArrayList<>();
        thisJarPaths.add(ClassCompile.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        //把本项目的class文件打包进去
        thisJarPaths.forEach(thisJar -> {
            File thisJarFile = new File(thisJar);
            if (thisJar.endsWith("/classes/")) {
                List<File> files = new ArrayList<>();
                FileArm.listFile(files, new File(thisJar));
                files.forEach(file -> {
                    String className = file.getAbsolutePath().substring(thisJarFile.getAbsolutePath().length());
                    File targetFile = FileType.JAR.equals(guarder.getType()) ? guarder.getTargetFile() : guarder.getTargetClassesDir();
                    targetFile = new File(targetFile, className);
                    if (file.isDirectory()) {
                        targetFile.mkdirs();
                    } else if (StrArm.containsArray(file.getAbsolutePath(), PathConst.CODE_FORT_FILES)) {
                        byte[] bytes = FileArm.readBytes(file);
                        IoArm.writeFromByte(bytes, targetFile);
                    }
                });
            }
        });
        //把javaagent信息加入到MANIFEST.MF
        File manifest = new File(guarder.getTargetFile(), "META-INF/MANIFEST.MF");
        String preMain = "Premain-Class: " + CodeFortAgent.class.getName();
        String[] txts = {};
        if (manifest.exists()) {
            txts = IoArm.readTxtFile(manifest).split("\r\n");
        }
        String str = StrArm.insertStringArray(txts, preMain, "Main-Class:");
        IoArm.writeTxtFile(manifest, str + "\r\n\r\n");
    }

    /**
     * 清空class文件的方法体，并保留参数信息
     *
     * @param guarder
     */
    public static void clearClassMethod(Guarder guarder) {
        ClassPool pool = ClassPool.getDefault();
        loadClassPath(pool, guarder.getTargetFile());
        List<String> classPaths = new ArrayList<>();
        guarder.getEncryptClass().forEach(classFile -> {
            String classPath = StringUtil.resolveClassPath(classFile.getAbsolutePath(), false);
            if (classPaths.contains(classPath)) return;
            try {
                pool.insertClassPath(classPath);
            } catch (NotFoundException ignored) {
            }
            classPaths.add(classPath);
        });
        //[2]修改class方法体，并保存文件
        guarder.getEncryptClass().forEach(classFile -> {
            //解析出类全名
            String className = StringUtil.resolveClassPath(classFile.getAbsolutePath(), true);
            byte[] bts = null;
            try {
                bts = rewriteMethod(pool, className);
            } catch (Exception ignored) {
            }
            if (bts != null) {
                IoArm.writeFromByte(bts, classFile);
            }
        });
    }

    /**
     * 加载指定目录下的所有依赖
     *
     * @param pool javassist池
     * @param file 加载路径
     */
    public static void loadClassPath(ClassPool pool, File file) {
        if (file == null || !file.exists()) return;
        if (file.isDirectory()) {
            List<File> jars = new ArrayList<>();
            FileArm.listFile(jars, file, ".jar");
            for (File jar : jars) {
                try {
                    pool.insertClassPath(jar.getAbsolutePath());
                } catch (NotFoundException ignored) {
                }
            }
        } else if (file.getName().endsWith(".jar")) {
            try {
                pool.insertClassPath(file.getAbsolutePath());
            } catch (Exception ignore) {
            }
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
        String name = null;
        try {
            CtClass cc = pool.getCtClass(classname);
            CtMethod[] methods = cc.getDeclaredMethods();
            for (CtMethod m : methods) {
                name = m.getName();
                if (!m.getName().contains("<") && m.getLongName().startsWith(cc.getName())) {
                    CodeAttribute ca = m.getMethodInfo().getCodeAttribute();
                    if (ca != null && ca.getCodeLength() != 1 && ca.getCode()[0] != -79) {
                        setBodyKeepParamInfos(m, null, true);
                        if ("void".equalsIgnoreCase(m.getReturnType().getName()) && m.getLongName().endsWith(".main(java.lang.String[])") && m.getMethodInfo().getAccessFlags() == 9) {
                            m.insertBefore("System.out.println(\"启动校验失败,请联系开发者或管理员处理.\");");
                        }
                    }
                }
            }
            return cc.toBytecode();
        } catch (Exception e) {
            throw new RuntimeException("处理class对象时异常(name:" + name + ")", e);
        }
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

}