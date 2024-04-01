package coffee.lucks.codefort.unit;

import coffee.lucks.codefort.embeds.arms.SysArm;
import coffee.lucks.codefort.embeds.unit.FortLog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.net.ServerSocket;
import java.net.Socket;

@Component
public class StartListener implements ApplicationRunner {

    @Value("${codefort.port}")
    private int port;


    @Value("${server.port}")
    private int webPort;

    public void run(ApplicationArguments args) {
        FortLog.info("CodeFortWeb地址: http://"+ SysArm.getInternetIp() +":"+webPort+"/");
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            FortLog.info("CodeFortSocket服务已启动,端口: " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                String remoteSocketAddress = socket.getRemoteSocketAddress().toString();
                remoteSocketAddress = remoteSocketAddress.startsWith("/") ? remoteSocketAddress.replaceFirst("/","") : remoteSocketAddress;
                FortLog.info("客户端连接成功,地址: " + remoteSocketAddress);
                SocketServer fortServer = new SocketServer(socket);
                SocketServer.serverCodeFortMap.put(fortServer, null);
                fortServer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}