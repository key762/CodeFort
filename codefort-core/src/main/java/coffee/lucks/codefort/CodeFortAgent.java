package coffee.lucks.codefort;

import coffee.lucks.codefort.embeds.arms.StrArm;
import coffee.lucks.codefort.embeds.unit.*;
import coffee.lucks.codefort.embeds.util.CmdLineUtil;
import coffee.lucks.codefort.embeds.util.EncryptUtil;
import coffee.lucks.codefort.embeds.util.SecurityUtil;
import coffee.lucks.codefort.embeds.util.StringUtil;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Timer;

public class CodeFortAgent {

    public static void premain(String args, Instrumentation inst) {
        // 禁止程序被Attach
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new AttachTask(), 0, 1000);
        FortBanner.banner();
        CmdLineUtil cmdLine = new CmdLineUtil();
        cmdLine.addOption("pwd", true);
        String pwd = null;
        if (args != null) {
            cmdLine.parse(args.split(" "));
            pwd = cmdLine.getOptionValue("pwd", "");
        }
        // 先尝试使用默认密码
        byte[] encryptedFile = EncryptUtil.readEncryptedFile(new File(Objects.requireNonNull(StringUtil.getRootPath(null))), FortConst.CODE_FORT_INFO);
        boolean flag = true;
        try {
            String fileStr = new String(SecurityUtil.decrypt(encryptedFile, FortConst.DEFAULT_PASSWORD), StandardCharsets.UTF_8);
            if (!StrArm.isEmpty(fileStr) && fileStr.startsWith("{\"") && fileStr.endsWith("\"}")) {
                flag = false;
                pwd = FortConst.DEFAULT_PASSWORD;
                FortLog.info("匹配至默认密码,即将启动");
            }
        } catch (Exception ignore) {
        }
        if (flag) {
            if (StrArm.isEmpty(pwd)) {
                FortLog.info("无法在运行参数中获取密码,将会从系统参数获取");
                pwd = System.getenv("pwd");
            }
            if (StrArm.isEmpty(pwd)) {
                Console console = System.console();
                if (console != null) {
                    FortLog.info("无法在系统参数中获取密码,将会从控制台获取");
                    FortLog.info("提示: 如果未设置密码请直接回车键跳过");
                    pwd = new String(console.readPassword(FortLog.infoStr("请输入密码 :")));
                }
            }
            if (StrArm.isEmpty(pwd)) {
                pwd = FortConst.DEFAULT_PASSWORD;
            }
        }
        // 正式处理之前先去获取配置的信息
        String fileStr = "";
        try {
            fileStr = new String(SecurityUtil.decrypt(encryptedFile, pwd), StandardCharsets.UTF_8);
        } catch (Exception e) {
            FortLog.info("密码错误,请重试或联系管理员及开发者");
            try {
                Thread.sleep(1000);
            } catch (Exception ignore) {
            }
            System.exit(0);
        }
        if (StrArm.isEmpty(fileStr) && !fileStr.startsWith("{\"") && !fileStr.endsWith("\"}")) {
            FortLog.info("密码错误,请重试或联系管理员/开发者");
            System.exit(0);
        }
        timer.cancel();
        try {
            byte[] bytes = EncryptUtil.readEncryptedFile(new File(Objects.requireNonNull(StringUtil.getRootPath(null))), FortConst.AGENT_ENGINE);
            Class clazz = new FortLoader().get(SecurityUtil.decrypt(bytes, pwd));
            Method method = clazz.getMethod("premain", String.class, Instrumentation.class, String.class);
            Object instance = clazz.newInstance();
            method.invoke(instance, args, inst, pwd);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
