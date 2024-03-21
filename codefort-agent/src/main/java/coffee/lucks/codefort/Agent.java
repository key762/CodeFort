package coffee.lucks.codefort;

import cn.hutool.core.io.FileUtil;
import coffee.lucks.codefort.agent.AgentTransformer;
import coffee.lucks.codefort.unit.FileType;
import coffee.lucks.codefort.unit.FortBanner;
import coffee.lucks.codefort.unit.PathConst;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Agent {

    public static void premain(String args, Instrumentation inst) throws Exception {
        FortBanner.banner();
        Options options = new Options();
        options.addOption("data", true, "加密后的文件(多个用,分割)");
        options.addOption("pwd", true, "密码(多个用,分割)");
        String file = null;
        String pwd = null;
        if (args != null) {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args.split(" "));
            file = cmd.getOptionValue("data");
            pwd = cmd.getOptionValue("pwd");
        }
        if (file == null || file.isEmpty() || pwd == null || pwd.isEmpty()) {
            return;
        }
        String[] files = file.split(",");
        String[] pwds = pwd.split(",");
        if (files.length != pwds.length) {
            throw new RuntimeException("加密文件和密码个数不一致");
        }
        for (int i = 0; i < files.length; i++) {
            File zip = new File(files[i]);
            try (ZipFile zipFile = new ZipFile(zip)) {
                if (FileUtil.extName(zip).equalsIgnoreCase(FileType.JAR.getType())) {
                    ZipEntry zipEntry = zipFile.getEntry(PathConst.ENCRYPT_NAME);
                    zipFile.getInputStream(zipEntry);
                    File classesDat = new File(files[i].substring(0, files[i].length() - 4) + "." + PathConst.ENCRYPT_NAME);
                    FileUtil.writeFromStream(zipFile.getInputStream(zipEntry), classesDat, false);
                    files[i] = classesDat.getAbsolutePath();
                }
                if (FileUtil.extName(zip).equalsIgnoreCase(FileType.WAR.getType())) {
                    files[i] = files[i].substring(0, files[i].length() - 4) + File.separator + "META-INF" + File.separator + PathConst.ENCRYPT_NAME;
                }
            } catch (Exception e) {
                throw new RuntimeException("Agent运行时获取加密块时异常");
            }
        }
        AgentTransformer tran = new AgentTransformer();
        tran.setFiles(files);
        tran.setPwds(pwds);
        inst.addTransformer(tran);
    }

}