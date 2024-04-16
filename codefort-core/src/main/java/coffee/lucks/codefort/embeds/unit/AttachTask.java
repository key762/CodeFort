package coffee.lucks.codefort.embeds.unit;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.TimerTask;

public class AttachTask extends TimerTask {
    @Override
    public void run() {
        attachCheck();
    }

    public static void attachCheck(){
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] threadIds = threadMXBean.getAllThreadIds();
        for (long threadId : threadIds) {
            ThreadInfo threadInfo = threadMXBean.getThreadInfo(threadId);
            if (threadInfo != null && threadInfo.getThreadName().contains("Attach Listener")) {
                FortLog.info("程序被Attach,程序即将关闭");
                System.exit(0);
            }
        }
    }

}
