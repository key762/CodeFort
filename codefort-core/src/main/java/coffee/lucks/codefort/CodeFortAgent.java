package coffee.lucks.codefort;

import coffee.lucks.codefort.embeds.arms.MapArm;
import coffee.lucks.codefort.embeds.arms.StrArm;
import coffee.lucks.codefort.embeds.arms.SysArm;
import coffee.lucks.codefort.embeds.unit.FortBanner;
import coffee.lucks.codefort.embeds.unit.FortLog;
import coffee.lucks.codefort.embeds.unit.PathConst;
import coffee.lucks.codefort.embeds.util.CmdLineUtil;
import coffee.lucks.codefort.embeds.util.EncryptUtil;
import coffee.lucks.codefort.embeds.util.SecurityUtil;
import coffee.lucks.codefort.embeds.util.StringUtil;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class CodeFortAgent {

    public static String GLOBAL_VAR = "";

    /**
     * 启动前置信息处理
     *
     * @param args 参数
     * @param inst 实例
     */
    public static void premain(String args, Instrumentation inst) {
        FortBanner.banner();
        PathConst.DEBUG = true;
        CmdLineUtil cmdLine = new CmdLineUtil();
        cmdLine.addOption("pwd", true);
        String pwd = null;
        if (args != null) {
            cmdLine.parse(args.split(" "));
            pwd = cmdLine.getOptionValue("pwd", "");
        }
        if (StrArm.isEmpty(pwd)) {
            FortLog.info("无法在运行参数中获取密码,将会从系统参数获取.");
            pwd = System.getenv("pwd");
        }
        if (StrArm.isEmpty(pwd)) {
            Console console = System.console();
            if (console != null) {
                FortLog.info("无法在系统参数中获取密码,将会从控制台获取.");
                pwd = new String(console.readPassword(FortLog.infoStr("请输入密码 :")));
            }
        }
        FortLog.info("获取到了密码: " + pwd);
        GLOBAL_VAR = "成功设置了数据";
        // 正式处理之前先去获取配置的信息
        byte[] encryptedFile = EncryptUtil.readEncryptedFile(new File(Objects.requireNonNull(StringUtil.getRootPath(null))), PathConst.CODE_FORT_INFO);
        String fileStr = "";
        try {
            fileStr = new String(SecurityUtil.decrypt(encryptedFile, pwd), StandardCharsets.UTF_8);
        }catch (Exception e){
            FortLog.info("密码错误,请重试或联系管理员及开发者");
            try {
                Thread.sleep(1000);
            }catch (Exception ignore){}
            System.exit(0);
        }
        if (StrArm.isEmpty(fileStr) && !fileStr.startsWith("{\"") && !fileStr.endsWith("\"}")){
            FortLog.info("密码错误,请重试或联系管理员/开发者");
            System.exit(0);
        }
        System.out.println(fileStr);
        Map<String, Object> objectMap = MapArm.toMap(fileStr);
        if (objectMap.get("needBiosMark").toString().equals("true")){
            if (!SysArm.getCPUSerialNumber().equalsIgnoreCase(objectMap.get("biosMark").toString())){
                FortLog.info("请在指定机器上运行此服务,即将退出");
                System.exit(0);
            }
        }
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (FortSocket.getInstance().getSocket() == null){
                    boolean flag = FortSocket.getInstance().create();
                    if (flag){
                        FortLog.info("连接成功");
                    }else {
                        FortSocket.getInstance().setSocket(null);
                        FortLog.info("连接失败");
                    }
                }else {
                    try {
                        // 尝试读取数据
                        Socket socket = FortSocket.getInstance().getSocket();
                        int data = socket.getInputStream().read();
                        if (data == -1 || socket.isClosed() || !socket.isConnected()) {
                            FortLog.info("服务端已关闭");
                            FortSocket.getInstance().setSocket(null);
                        } else {
                            FortLog.info("与服务端连接通畅");
                        }
                    } catch (IOException e) {
                        FortSocket.getInstance().setSocket(null);
                        FortLog.info("连接异常关闭");
                    }
                }
            }
        }, 1000, 10000); // 10秒后开始，每隔10秒执行一次
        AgentTransformer tran = new AgentTransformer(pwd);
        inst.addTransformer(tran);
    }

}
