package coffee.lucks.codefort.start;

import coffee.lucks.codefort.embeds.unit.FortLog;
import coffee.lucks.codefort.unit.SocketServer;
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

    public void run(ApplicationArguments args) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            FortLog.info("Socket服务已启动,端口: " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                FortLog.info("客户端连接成功,地址: " + socket.getRemoteSocketAddress());
                SocketServer fortServer = new SocketServer(socket);
                SocketServer.serverCodeFortMap.put(fortServer, null);
                fortServer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}