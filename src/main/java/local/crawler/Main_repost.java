package local.crawler;

import local.crawler.runnable.CheckHotRpThread;
import local.crawler.runnable.CheckNewRpThread;

/**
 * Created by liye on 16/1/5.
 * get repost status
 * store in local files
 */
public class Main_repost {
    public static void main(String[] args) {
        CheckHotRpThread checkHotRpThread = new CheckHotRpThread();
        new Thread(checkHotRpThread).start();

        CheckNewRpThread checkNewRpThread = new CheckNewRpThread();
        new Thread(checkNewRpThread).start();
    }
}
