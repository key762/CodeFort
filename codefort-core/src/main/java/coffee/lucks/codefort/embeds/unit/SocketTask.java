package coffee.lucks.codefort.embeds.unit;

import coffee.lucks.codefort.FortSocket;

import java.util.TimerTask;

public class SocketTask extends TimerTask {
    @Override
    public void run() {
        FortSocket.getInstance().handle();
    }
}
