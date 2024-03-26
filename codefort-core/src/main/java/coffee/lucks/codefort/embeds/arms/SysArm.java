package coffee.lucks.codefort.embeds.arms;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Scanner;

public class SysArm {

    /**
     * 内网IP
     */
    public static String INTRANET_IP = getIntranetIp();

    /**
     * 外网IP
     */
    public static String INTERNET_IP = getInternetIp();

    /**
     * 获得内网IP
     *
     * @return 内网IP
     */
    public static String getIntranetIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获得外网IP
     *
     * @return 外网IP
     */
    public static String getInternetIp() {
        try {
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            Enumeration<InetAddress> addrs;
            while (networks.hasMoreElements()) {
                addrs = networks.nextElement().getInetAddresses();
                while (addrs.hasMoreElements()) {
                    ip = addrs.nextElement();
                    if (ip != null
                            && ip instanceof Inet4Address
                            && ip.isSiteLocalAddress()
                            && !ip.getHostAddress().equals(INTRANET_IP)) {
                        return ip.getHostAddress();
                    }
                }
            }
            // 如果没有外网IP，就返回内网IP
            return INTRANET_IP;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 运行命令
     *
     * @param cmd  命令
     * @param line 返回第几行结果，0返回所有
     * @return 结果
     */
    public static String runCmd(String cmd, int line) {
        Process process;
        Scanner sc = null;
        StringBuffer sb = new StringBuffer();
        try {
            process = Runtime.getRuntime().exec(cmd);
            process.getOutputStream().close();
            sc = new Scanner(process.getInputStream());
            int i = 0;
            while (sc.hasNextLine()) {
                i++;
                String str = sc.nextLine();
                if (line <= 0) {
                    sb.append(str).append("\r\n");
                } else if (i == line) {
                    return str.trim();
                }
            }
            sc.close();
        } catch (Exception e) {


        } finally {
            try {
                sc.close();
            } catch (Exception ignore) {
            }
        }
        return sb.toString();
    }

    /**
     * 运行cmd命令
     *
     * @param cmd    命令
     * @param substr 关键字
     * @return 包含关键字的行数
     */
    public static String runCmd(String cmd, String substr) {
        Process process;
        Scanner sc = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
            process.getOutputStream().close();
            sc = new Scanner(process.getInputStream());
            while (sc.hasNextLine()) {
                String str = sc.nextLine();
                if (str != null && str.contains(substr)) {
                    return str.trim();
                }
            }
            sc.close();
        } catch (Exception e) {

        } finally {
            try {
                sc.close();
            } catch (Exception ignore) {
            }
        }
        return null;
    }

    /**
     * 获取cpu序列号
     *
     * @return 序列号
     */
    public static String getCPUSerialNumber() {
        String sysName = System.getProperty("os.name");
        if (sysName.contains("Windows")) {//win
            String str = runCmd("wmic cpu get ProcessorId", 2);
            return str;
        } else if (sysName.contains("Linux")) {
            String str = runCmd("dmidecode |grep -A16 \"Processor Information$\"", "ID");
            if (str != null) {
                return str.substring(str.indexOf(":")).trim();
            }
        } else if (sysName.contains("Mac")) {
            String str = runCmd("system_profiler SPHardwareDataType", "Serial Number");
            if (str != null) {
                return str.substring(str.indexOf(":") + 1).trim();
            }
        }
        return "";
    }

}
