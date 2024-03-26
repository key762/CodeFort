package coffee.lucks.codefort;

import java.net.Socket;

public class FortSocket {

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
            this.socket = new Socket("localhost", 7007);
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
}
