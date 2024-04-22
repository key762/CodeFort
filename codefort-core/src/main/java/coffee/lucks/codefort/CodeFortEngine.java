package coffee.lucks.codefort;

import coffee.lucks.codefort.embeds.arms.DateArm;
import coffee.lucks.codefort.embeds.arms.MapArm;
import coffee.lucks.codefort.embeds.arms.SysArm;
import coffee.lucks.codefort.embeds.unit.*;
import coffee.lucks.codefort.embeds.util.EncryptUtil;
import coffee.lucks.codefort.embeds.util.SecurityUtil;
import coffee.lucks.codefort.embeds.util.StringUtil;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;

public class CodeFortEngine {

    public static void premain(String args, Instrumentation inst, String pwd) {
        new Timer().scheduleAtFixedRate(new AttachTask(), 0, 1000);
        byte[] encryptedFile = EncryptUtil.readEncryptedFile(new File(Objects.requireNonNull(StringUtil.getRootPath(null))), FortConst.CODE_FORT_INFO);
        String fileStr = new String(SecurityUtil.decrypt(encryptedFile, pwd), StandardCharsets.UTF_8);
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
        AgentTransformer tran = new AgentTransformer(pwd);
        inst.addTransformer(tran);
    }

}
