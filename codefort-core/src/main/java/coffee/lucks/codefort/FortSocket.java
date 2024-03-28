package coffee.lucks.codefort;

import coffee.lucks.codefort.embeds.unit.FortLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class FortSocket {

    /**
     * Socket内容
     */
    public static String SOCKET_CONTACT = "";

    /**
     * Socket主机地址
     */
    public static String SOCKET_HOST = "";

    /**
     * Socket主机端口
     */
    public static int SOCKET_PORT = 0;

    private static FortSocket instance;

    public static FortSocket getInstance() {
        if (instance == null) {
            synchronized (FortSocket.class) {
                if (instance == null) {
                    instance = new FortSocket();
                }
            }
        }
        return instance;
    }

    public Socket socket = null;

    public boolean create() {
        try {
            FortLog.debug("Socket服务器IP: "+SOCKET_HOST+",端口: "+SOCKET_PORT);
            this.socket = new Socket(SOCKET_HOST, SOCKET_PORT);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void handle() {
        if (FortSocket.getInstance().getSocket() == null) {
            boolean flag = FortSocket.getInstance().create();
            if (flag) {
                FortLog.debug("连接成功");
            } else {
                FortSocket.getInstance().setSocket(null);
                FortLog.debug("连接失败");
            }
        } else {
            try {
                // 尝试读取数据
                Socket socket = FortSocket.getInstance().getSocket();
                int data = socket.getInputStream().read();
                if (data == -1 || socket.isClosed() || !socket.isConnected()) {
                    FortLog.debug("服务端已关闭");
                    FortSocket.getInstance().setSocket(null);
                } else {
                    BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                    String sr = socketReader.readLine();
                    FortLog.debug("与服务端连接通畅, 接收到消息("+sr.length()+") : "+sr);
                }
            } catch (IOException e) {
                FortSocket.getInstance().setSocket(null);
                FortLog.debug("连接异常关闭");
            }
        }
    }

}
