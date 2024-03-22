package coffee.lucks.codefort;

import coffee.lucks.codefort.agent.AgentTransformer;
import coffee.lucks.codefort.unit.FortBanner;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import java.lang.instrument.Instrumentation;

public class Agent {

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
//        for (int i = 0; i < files.length; i++) {
//            File zip = new File(files[i]);
//            try (ZipFile zipFile = new ZipFile(zip)) {
//                if (FileUtil.extName(zip).equalsIgnoreCase(FileType.JAR.getType())) {
//                    ZipEntry zipEntry = zipFile.getEntry(PathConst.ENCRYPT_NAME);
//                    zipFile.getInputStream(zipEntry);
//                    File classesDat = new File(files[i].substring(0, files[i].length() - 4) + "." + PathConst.ENCRYPT_NAME);
//                    FileUtil.writeFromStream(zipFile.getInputStream(zipEntry), classesDat, false);
//                    files[i] = classesDat.getAbsolutePath();
//                }
//                if (FileUtil.extName(zip).equalsIgnoreCase(FileType.WAR.getType())) {
//                    files[i] = files[i].substring(0, files[i].length() - 4) + File.separator + "META-INF" + File.separator + PathConst.ENCRYPT_NAME;
//                }
//            } catch (Exception e) {
//                throw new RuntimeException("Agent运行时获取加密块时异常");
//            }
//        }
        AgentTransformer tran = new AgentTransformer(pwd);
        inst.addTransformer(tran);
    }

}