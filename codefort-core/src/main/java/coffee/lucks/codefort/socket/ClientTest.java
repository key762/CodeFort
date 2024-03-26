package coffee.lucks.codefort.socket;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientTest {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 7007);
        ClientHandler clientHandler = new ClientHandler(socket);
        clientHandler.start();
    }
}

class ClientHandler {
    Socket socket = null;
    boolean flag = true;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    Thread sendMessage = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Scanner scanner = new Scanner(System.in);
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                while (flag) {
                    String message = scanner.nextLine();
                    if (message.equals("")) continue;//发送内容为空，则不发送
                    printWriter.println(message);
                    if (message.equals("end") || message == null) {
                        flag = false;//如果发送内容为end，则表示结束通信
                        break;
                    }
                }
            } catch (Exception ignore) {
            }
        }
    });
    Thread getMessage = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                while (flag) {
                    String sr = socketReader.readLine();
                    System.out.println(sr);
                }
            } catch (Exception ignore) {
            }
        }
    });

    public void start() {
        sendMessage.start();
        getMessage.start();
    }
}
