package coffee.lucks.codefort.agent;

import cn.hutool.core.io.FileUtil;
import coffee.lucks.codefort.consts.PathConst;
import coffee.lucks.codefort.util.EncryptUtil;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Agent {

    public static void premain(String args, Instrumentation inst) throws Exception {
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
            //解压出classes.dat
            if (files[i].endsWith(".jar") || files[i].endsWith(".war")) {
                ZipFile zipFile = null;
                try {
                    File zip = new File(files[i]);
                    if (!zip.exists()) {
                        continue;
                    }
                    zipFile = new ZipFile(zip);
                    ZipEntry zipEntry = zipFile.getEntry(PathConst.ENCRYPT_NAME);
                    if (zipEntry == null) {
                        continue;
                    }
                    InputStream is = zipFile.getInputStream(zipEntry);
                    File classesDat = new File(files[i].substring(0, files[i].length() - 4) + "." + PathConst.ENCRYPT_NAME);
                    FileUtil.writeBytes(EncryptUtil.toByteArray(is), classesDat);
                    files[i] = classesDat.getAbsolutePath();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    EncryptUtil.close(zipFile);
                }
            }
        }
        AgentTransformer tran = new AgentTransformer();
        tran.setFiles(files);
        tran.setPwds(pwds);
        inst.addTransformer(tran);
    }

}