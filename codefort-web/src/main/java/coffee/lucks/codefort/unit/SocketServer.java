package coffee.lucks.codefort.unit;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import coffee.lucks.codefort.embeds.unit.FortLog;
import coffee.lucks.codefort.embeds.util.SecurityUtil;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SocketServer extends Thread {

    public static Map<SocketServer, CodeFort> serverCodeFortMap = new HashMap<>();

    private Socket socket;

    public SocketServer(Socket socket) {
        this.socket = socket;
    }

    public synchronized void removeSocket(Map<SocketServer,CodeFort> serverCodeFortMap) {
        synchronized (serverCodeFortMap) {
            serverCodeFortMap.remove(this);
        }
    }

    @Override
    public void run() {
        try {
            server(this.socket.getInputStream());
        } catch (IOException ignored) {
        } finally {
            removeSocket(serverCodeFortMap);
        }
        FortLog.info("客户端连接已断开,地址: " + socket.getRemoteSocketAddress());
    }

    private void server(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        PrintWriter printStream = new PrintWriter(this.socket.getOutputStream(), true);
        Map<String,String> res = new HashMap<>();
        res.put("type", "info");
        res.put("msg", "CodeFort 致力保卫您的代码安全, 联系QQ 2940397985");
        printStream.println(" "+JSONUtil.toJsonStr(res));
        while (true) {
            String message = br.readLine();
            if (StrUtil.isNotEmpty(message)){
                byte[] keyBytes = Base64.getDecoder().decode(message);
                String data = SecurityUtil.decryptRsa(ServerConst.rsaKey,keyBytes);
                CodeFort codeFort = JSONUtil.toBean(data, CodeFort.class);
                serverCodeFortMap.put(this, codeFort);
            }
        }
    }

    public void outLine() {
        try {
            Map<String,String> res = new HashMap<>();
            res.put("type", "exit");
            res.put("msg", "此服务已被强制下线！如有疑问请联系管理员或开发者");
            PrintWriter printStream = new PrintWriter(this.socket.getOutputStream(), true);
            printStream.println(" "+JSONUtil.toJsonStr(res));
            removeSocket(serverCodeFortMap);
        }catch (Exception ignore){}
    }

    public void info(String data) {
        try {
            Map<String,String> res = new HashMap<>();
            res.put("type", "info");
            res.put("msg", data);
            PrintWriter printStream = new PrintWriter(this.socket.getOutputStream(), true);
            printStream.println(" "+JSONUtil.toJsonStr(res));
        }catch (Exception ignore){}
    }

}
