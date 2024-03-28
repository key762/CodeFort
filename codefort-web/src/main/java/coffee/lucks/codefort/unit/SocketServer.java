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

    private static final String rsaKey =
            "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCwLxeOVXyRrGDy\n" +
            "ZV1h+4m4w3HuV5YyWiP88aHHzgVVtogUWwl6Lb7+FUB0qTEwf82mp5bpsODZb2yl\n" +
            "8yrHW6oC1EqvRogrIEz9XhILz/1eYinCzSQsAQMZlrbwudSCXjSAa7GOEVa8OaRS\n" +
            "K/6vVKtBgEwU+m0autKCdwvIkFOZrm9ajFmt8+bJ+5fi+Rqp5OSpsh2zDp6vwsP2\n" +
            "7lNK02nOsYA/86eqGFfQj4pt4g/OD4/iRXSbbCh0eUb1VDrXOXAbpQabkmSRefq+\n" +
            "uBsP9PFqRVbRuqQS1t3+w7TUPBRQzf9E+sjBICN46gRA9QwBzYQ3gHrztlUMo8TY\n" +
            "1RexsAh5AgMBAAECggEABSI/fSNFOk1xPxRFu1QuuCvhxwCAXOk+1ouym3WHtu6s\n" +
            "Wm4orDRnC1T9MtO3SOLV0D8JPmKsD/SXkDeyDcBrB3gTe7YCGRebhrUrW3LCaZyn\n" +
            "UN4qE/+CN/L/U6u74yycOl2+UmseQY/VwkXhPvn9n4P93jf0iUHbs8zaPWGPPhFz\n" +
            "G/egqRDD7i23HKo8250B8UAvXtUjznI7rl4yF/2MjXrhUFOCA7dLj6VK57x0agTl\n" +
            "B33wDub9Px3ITneuXooJR3szU4KL5GDX7du6JBiHoaDw/UZVASE9HEA3r+XiENwP\n" +
            "hruVmbdNskF3OkTAmOzQ0RLTPaER83zH3cTpa5FWcQKBgQDWCNMImkDWU8wIheNk\n" +
            "Fs61OFZoF3GBNAhN0IFq2nItmzm0noGPka7XzFcBBgqXIJgPIGLWRY+FWxhynym2\n" +
            "M5rnAT0VcardRU+21XJpQnJbtzlTEhgiSLihDmJRm6n7ViOwfPKNaC0ZFs7B0o5T\n" +
            "TIez4hNLIOESvdi4Ow8xjQ7YDQKBgQDSumlKjKYyEn8a3RrfCM4ItgWbjd8pyFAj\n" +
            "lMTgGkZw1gS29WNNa05LYrRUXFqlbt3TMaWMwEAV+E64VI8gM9pCTCpY6o7ouIRP\n" +
            "E+cvnVbUgVM/u0HcWUGNN4UCgd3uyVDuSiPNbUs877D6hi0j/sTMXyBgPu3NeKd6\n" +
            "MwzsM10LHQKBgQDS2oCkAeqL+qOPZe8c5EchSm39P9Mm+M6Rci58yNUgzsHriReU\n" +
            "C24W1AyGSqBWP9rtU/dqpb59HsAX82rRP0eD+blmjcNJFwYv1VlQDxC//+HuT24N\n" +
            "IF0a9SOwx8yOeU7RiFYfLpj3FXv8f+SMdWFeugJNygdRQkvlOvF93DvaZQKBgAue\n" +
            "BUH74QmvpVw4xkt3c8xdJI//0Ua/aVOc1wG30RxVYCsp+hCBku5rAaAI+2JVa1tC\n" +
            "SGsrCh1r9AMLflx7H1Q6WLdQLxK7YWfuo/cnCGtsuccwrp/UN93uKqIJwM9yP8jz\n" +
            "Q2gG13gDitE05nYujHDr6aAEuB10wl7lQ5gd+MotAoGBAIlxXU8KFfKgvw2FqO/b\n" +
            "+A+GOXBOxKYTQtLl00BQ6V3VN1t7JJp1PY6GrSmBYr7mY5BUszb+3+LXXRCtXgx2\n" +
            "9cilXXj6wPq7dXc7ToOzS7PhNpXAYbqVYBH8sDdNFXxY5X92vkctsx3Npr1DEXSj\n" +
            "fs1YH+FYqTpUs07RXl3APabF";

    public static List<SocketServer> list = new ArrayList<>();

    private Socket socket;


    private Timer timer;

    public SocketServer(Socket socket) {
        this.socket = socket;
        this.timer = new Timer();
    }

    public synchronized void removeSocket(List<SocketServer> list) {
        synchronized (list) {
            list.remove(this);
        }
    }

    @Override
    public void run() {
        try {
            InputStream is = this.socket.getInputStream();
            OutputStream os = this.socket.getOutputStream();
            server(is, os);
        } catch (IOException ignored) {
        } finally {
            removeSocket(list);
        }
        FortLog.info("客户端连接已断开,地址: " + socket.getRemoteSocketAddress());
    }

    public void sendPeriodicMessage() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    PrintWriter printStream = new PrintWriter(socket.getOutputStream(), true);
                    printStream.println("This is a periodic message from the server.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 10000); // 每10秒发送一次消息
    }

    private void server(InputStream inputStream, OutputStream outputStream) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        bw.write("连接成功\n");
        bw.flush();
//        sendPeriodicMessage();
        while (true) {
            String message = br.readLine();  //读取客户端发送过来的消息
            if (StrUtil.isNotEmpty(message)){
                byte[] keyBytes = Base64.getDecoder().decode(message);
                String data = SecurityUtil.decryptRsa(rsaKey,keyBytes);
                CodeFort codeFort = JSONUtil.toBean(data, CodeFort.class);
                System.out.println(codeFort);
            }
        }
    }

}
