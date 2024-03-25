package coffee.lucks.codefort;

import coffee.lucks.codefort.embeds.arms.StrArm;
import coffee.lucks.codefort.embeds.unit.FortBanner;
import coffee.lucks.codefort.embeds.unit.FortLog;
import coffee.lucks.codefort.embeds.unit.PathConst;
import coffee.lucks.codefort.embeds.util.CmdLine;

import java.io.Console;
import java.lang.instrument.Instrumentation;

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
        CmdLine cmdLine = new CmdLine();
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
        AgentTransformer tran = new AgentTransformer(pwd);
        inst.addTransformer(tran);
    }

}
