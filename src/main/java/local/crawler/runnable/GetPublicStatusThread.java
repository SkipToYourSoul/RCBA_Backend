package local.crawler.runnable;

import local.crawler.control.Controller;
import local.crawler.control.TweetLifeCycleEvaluator;
import myutil.MyWriter;
import myutil.Tool;
import weibo4j.model.Status;
import weibo4j.model.WeiboException;

import java.util.List;

/**
 * get public status
 * version: store status to local files
 *
 * @author liye
 */
public class GetPublicStatusThread extends Controller implements Runnable {

    public static int sleepTime = 5000;

    private PublicCrawlFunction worker = new PublicCrawlFunction();

    public GetPublicStatusThread() {
        super("GetPublicStatusThread");
    }

    private class PublicCrawlFunction extends CrawlerWorker{

        @Override
        public void crawlerImpl() throws WeiboException {
            res = weibo.getGlobalStream(200);
            randomSleep(sleepTime);
        }
    }

    public void run() {
        int emptyCount = 0;
        MyWriter writeStatus = null;
        while (true) {
            //get status
            List<Status> result = crawlStatus(worker);

            //judge if token is invalid to get status
            if (result.size() == 0 || result == null){
                emptyCount ++;
                break;
            }
            if (emptyCount == 3){
                emptyCount = 0;
                setNextToken();
            }

            //create file dir according the time
            writeStatus = new MyWriter(timelyFile(), "utf-8");

            for (Status status : result){
                //handle hot tweets
                Status oStatus = status.getRetweetedStatus() == null ? status : status.getRetweetedStatus();
                if (oStatus != null && TweetLifeCycleEvaluator.isHotTweet(oStatus, 0)) {
                    Tool.write(configuration.getMidFile(), oStatus.getMid(), true, "utf-8");
                }
                //write file
                writeStatus.Write(switchStatus(status));
            }

            writeStatus.closeWrite();

            System.out.println("--------------------------------->");
            System.out.println("Thread_public: get " + result.size() + " status at " + tokenPack.getCount()
                    + " in token " + tokenPack.token);
            System.out.println("--------------------------------->");
        }
    }
}
