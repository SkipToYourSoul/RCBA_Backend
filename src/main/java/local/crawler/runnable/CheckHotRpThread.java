package local.crawler.runnable;


import local.crawler.control.Controller;
import myutil.StringReaderOne;
import myutil.Tool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * check if there are hot status and get it's retweets
 * version: store status to local files
 *
 * @author liye
 */
public class CheckHotRpThread extends Controller implements Runnable {

    //thread pool
    //pool size : 3
    ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 3, 200L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());

    private StringReaderOne readMid = null;
    private StringReaderOne readMidPos = null;

    public CheckHotRpThread() {
        this.readMid = new StringReaderOne(configuration.getMidFile());
        this.readMidPos = new StringReaderOne(configuration.getMidPosFile());
    }

    public void run() {
        //get mid pos
        int pos = 0;
        if (!this.readMidPos.IsOver()) {
            pos = Integer.parseInt(this.readMidPos.GetStrictNewID());
        }
        this.readMid.setPos(pos);

        for (; ; ) {
            //sleep
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //adjust the sleep time
            if (this.executor.getQueue().size() > 5) {
                GetRepostStatusThread.sleepTime = 4000;
            } else {
                GetRepostStatusThread.sleepTime = 5000;
            }

            if (this.executor.getQueue().size() == 0) {
                //update the mid
                this.readMid.updateIds(configuration.getMidFile(), this.readMid.getPos());

                while (!this.readMid.IsOver()) {
                    String mid = String.valueOf(this.readMid.GetStrictNewID());
                    System.out.println("CheckHotRpThread: start " + mid);

                    //get status
                    GetRepostStatusThread repostThread = new GetRepostStatusThread(mid,"1");
                    this.executor.execute(repostThread);

                    this.readMid.nextPos();

                    //update mid pos
                    Tool.write(configuration.getMidPosFile(), String.valueOf(this.readMid.getPos()), false, "UTF8");
                }
            }
            System.out.println("checkHotRpThread: " +
                    "current pool size:" + this.executor.getPoolSize() +                       //线程池中线程数目
                    ", active thread count：" + this.executor.getActiveCount() +               //线程池中正在运行的线程数目
                    ", wait list size：" + this.executor.getQueue().size() +                   //队列中等待执行的任务数目
                    ", complete thread count：" + this.executor.getCompletedTaskCount());      //已执行完别的任务数目
        }
    }
}
