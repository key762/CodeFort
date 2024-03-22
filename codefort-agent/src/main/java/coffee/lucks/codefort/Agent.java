package coffee.lucks.codefort;

import coffee.lucks.codefort.agent.AgentTransformer;
import coffee.lucks.codefort.unit.FortBanner;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import java.lang.instrument.Instrumentation;

public class Agent {

    public static String GLOBAL_VAR = "";

    public static void premain(String args, Instrumentation inst) throws Exception {
        FortBanner.banner();
        Options options = new Options();
        options.addOption("pwd", true, "密码");
        String pwd = null;
        if (args != null) {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args.split(" "));
            pwd = cmd.getOptionValue("pwd");
        }
        GLOBAL_VAR = "成功设置了数据";
        AgentTransformer tran = new AgentTransformer(pwd);
        inst.addTransformer(tran);
    }

}