package coffee.lucks.codefort;

import coffee.lucks.codefort.embeds.arms.DateArm;
import coffee.lucks.codefort.embeds.arms.MapArm;
import coffee.lucks.codefort.embeds.arms.StrArm;
import coffee.lucks.codefort.embeds.arms.SysArm;
import coffee.lucks.codefort.embeds.unit.*;
import coffee.lucks.codefort.embeds.util.CmdLineUtil;
import coffee.lucks.codefort.embeds.util.EncryptUtil;
import coffee.lucks.codefort.embeds.util.SecurityUtil;
import coffee.lucks.codefort.embeds.util.StringUtil;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;

public class CodeFortAgent {

    /**
     * 启动前置信息处理
     *
     * @param args 参数
     * @param inst 实例
     */
    public static void premain(String args, Instrumentation inst) {
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
        Map<String, Object> objectMap = MapArm.toMap(fileStr);
        FortConst.DEBUG = Boolean.parseBoolean(objectMap.get("isDebug").toString());
        FortConst.RSA_PUBLIC_KEY = objectMap.get("rsaPublicKey").toString();
        for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
            FortLog.debug(entry.getKey() + " : " + entry.getValue());
        }
        // 先检查agent程序是否被更改
        String sign = EncryptUtil.verificationClass(new File(Objects.requireNonNull(StringUtil.getRootPath(null))));
        if (!objectMap.get("verificationInfo").toString().equals(sign)) {
            FortLog.info("Agent校验失败,即将退出");
            System.exit(0);
        }
        FortLog.info("Agent校验成功,即将启动");
        // 时间区域
        if (!objectMap.get("timeJudge").toString().equals("no")) {
            // 先检查本地时间
            if (DateArm.compareLocalTimeAfter(objectMap.get("buildTime").toString())) {
                FortLog.info("请先同步本机时间,即将退出");
                System.exit(0);
            }
            String timeJudge = objectMap.get("timeJudge").toString();
            if (timeJudge.equals("region")) {
                String startTime = objectMap.get("startTime").toString();
                String endTime = objectMap.get("endTime").toString();
                if (!DateArm.localTimeInRegion(startTime, endTime)) {
                    FortLog.info("请在时间段 " + startTime + " 至 " + endTime + " 运行此服务,即将退出");
                    System.exit(0);
                }
            } else if (timeJudge.equals("deadline")) {
                if (!DateArm.beforeDeadline(objectMap.get("buildTime").toString(), timeJudge)) {
                    FortLog.info("此服务可用时间已不足,即将退出");
                    System.exit(0);
                }
            }
        }
        // 机器码
        if (objectMap.get("needBiosMark").toString().equals("true")) {
            if (!SysArm.getCPUSerialNumber().equalsIgnoreCase(objectMap.get("biosMark").toString())) {
                FortLog.info("请在指定机器上运行此服务,即将退出");
                System.exit(0);
            }
        }
        // Socket实时
        if (objectMap.get("needSocket").toString().equals("true")) {
            FortSocket.SOCKET_CONTACT = objectMap.get("contact").toString();
            FortSocket.SOCKET_HOST = objectMap.get("host").toString();
            try {
                FortSocket.SOCKET_PORT = Integer.parseInt(objectMap.get("port").toString());
            } catch (Exception ignore) {
            }
            // 1秒后开始，每隔10秒执行一次
            new Timer().scheduleAtFixedRate(new SocketTask(), 1000, 10000);
        }
        // 禁止程序被Attach
        new Timer().scheduleAtFixedRate(new AttachTask(), 1000, 1000);
        AgentTransformer tran = new AgentTransformer(pwd);
        inst.addTransformer(tran);
    }

}
