package local.crawler.runnable;


import local.crawler.control.Controller;
import myutil.StringReaderOne;
import myutil.Tool;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * check if there are new repost of the retweets
 * version: store status to local files
 *
 * @author liye
 */
public class CheckNewRpThread extends Controller implements Runnable {
    private StringReaderOne readMid = null;

    //thread pool
    //pool size : 3
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 3, 200L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());

    //20 days to changeToken
    private int changeToken = 0;

    public void run() {
        String path = configuration.getRepostMidDir();
        for (; ; ) {
            try {
                Thread.sleep(1000*60*60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //token change
            changeToken ++;
            if (changeToken == 24*20){
                changeToken = 0;
                changeNewToken();
            }

            //adjust the sleep time
            if (this.executor.getQueue().size() > 5) {
                GetRepostStatusThread.sleepTime = 4000;
            } else {
                GetRepostStatusThread.sleepTime = 5000;
            }

            //pool size is empty
            if ((this.executor.getQueue().size() == 0) && (this.executor.getActiveCount() == 0)) {
                List<String> mid = getNeedMid();
                for (String m : mid) {
                    //get sinceId
                    String needGetMid = path + m + "_mid_tmp";
                    File file = new File(needGetMid);
                    if (file.exists()) {
                        this.readMid = new StringReaderOne(needGetMid);
                        if (!this.readMid.IsOver()) {
                            System.out.println("checkNewRpThread: start " + m);

                            //get status
                            GetRepostStatusThread repostStatusThread = new GetRepostStatusThread(m, this.readMid.GetStrictNewID());
                            this.executor.execute(repostStatusThread);
                        }
                    }
                }
            }
            System.out.println("checkNewRpThread: " +
                    "current pool size:" + this.executor.getPoolSize() +                       //线程池中线程数目
                    ", active thread count：" + this.executor.getActiveCount() +               //线程池中正在运行的线程数目
                    ", wait list size：" + this.executor.getQueue().size() +                   //队列中等待执行的任务数目
                    ", complete thread count：" + this.executor.getCompletedTaskCount());      //已执行完别的任务数目
        }
    }

    //get mid that not expired
    private List<String> getNeedMid() {
        List<String> mid = Tool.readFile(configuration.getMidFile(), "utf-8");
        List<String> eMid = Tool.readFile(configuration.getExpiredMidFile(), "utf-8");
        Set<String> midSet = new HashSet();
        for (String e : eMid) {
            midSet.add(e);
        }
        List<String> need = new ArrayList();
        for (String m : mid) {
            if (!midSet.contains(m)) {
                need.add(m);
            }
        }
        return need;
    }
}
