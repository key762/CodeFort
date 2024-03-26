package coffee.lucks.codefort.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SocketTest {
    private static List<Handler> list=new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket=new ServerSocket(7007);
        System.out.println("服务器已启动!");
        while (true){
            Socket socket=serverSocket.accept();
            System.out.println(String.format("客户端连接已经创建，客户端地址："+socket.getRemoteSocketAddress()));
            Handler talkThread=new Handler(socket);
            list.add(talkThread);
            talkThread.start();
        }
    }

    static class Handler extends Thread{
        Socket socket;
        public Handler(Socket socket){
            this.socket=socket;
        }
        public synchronized void sendTalk(List<Handler> list,String tellInfo) throws IOException {   //将信息全发送给所有socket连接
            for (Handler v:list){
                PrintWriter printStream=new PrintWriter(v.socket.getOutputStream(),true);
                printStream.println(v.socket.getRemoteSocketAddress()+":"+tellInfo);
            }
        }
        public synchronized void removeSocket(List<Handler> list){   //移除socket连接
            synchronized (list){//
                list.remove(this);
            }
        }

        @Override
        public void run() {
            try {
                InputStream is=this.socket.getInputStream();
                OutputStream os=this.socket.getOutputStream();
                handle(is,os);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                removeSocket(list);//线程执行完毕，移除集合
            }
            System.out.println(socket.getRemoteSocketAddress()+"客户端连接已断开");
        }

        private void handle(InputStream inputStream,OutputStream outputStream) throws IOException {
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            BufferedReader br=new BufferedReader(new InputStreamReader(inputStream,StandardCharsets.UTF_8));
            bw.write("连接成功\n");
            bw.flush();
            while (true){
                String message=br.readLine();  //读取客户端发送过来的消息
                System.out.println(String.format("客户%s：%s",socket.getRemoteSocketAddress(),message));
                sendTalk(list,message);
                if (message.equals("end")){ //用户结束通话，线程执行完毕
                    System.out.println(socket.getRemoteSocketAddress()+"退出");
                    br.close();
                    bw.close();
                    break;
                }
            }
        }
    }

}
