package local.crawler.runnable;

import local.crawler.control.Controller;
import local.crawler.control.TweetLifeCycleEvaluator;
import myutil.MyWriter;
import myutil.Tool;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.WeiboException;

import java.util.List;

/**
 * get repost status
 * version: store status to local files
 *
 * @author liye
 */
public class GetRepostStatusThread extends Controller implements Runnable {
    public static int sleepTime = 4000;

    private String mid = null;
    private String sinceMid = null;
    private Paging paging = null;

    private RepostCrawlFunction worker = new RepostCrawlFunction();

    public GetRepostStatusThread(String mid, String sinceMid) {
        super("GetRepostStatusThread");
        this.mid = mid;
        this.sinceMid = sinceMid;
    }

    private class RepostCrawlFunction extends CrawlerWorker{
        @Override
        public void crawlerImpl() throws WeiboException {
            res = weibo.getRepostStream(mid,paging);
            randomSleep(sleepTime);
        }
    }

    public void run() {
        MyWriter writeStatus = new MyWriter(timelyFile(), "utf-8");

        int totalCount = 0;
        long minTime = Long.MAX_VALUE;
        long maxTime = 0;
        Long maxId = Long.parseLong(sinceMid);
        Status oStatus = null;

        //set parameters
        paging = new Paging();
        paging.setCount(200);
        paging.setSinceId(Long.parseLong(this.sinceMid));

        for (int page = 1; page <= 10; page++) {
            //get status
            paging.setPage(page);
            List<Status> result = crawlStatus(worker);

            if (result.size() > 0) {
                totalCount += result.size();

                //record the minTime and maxTime
                minTime = result.get(result.size()-1).getCreatedAt().getTime();
                maxTime = result.get(0).getCreatedAt().getTime();

                //get maxId
                maxId = result.get(0).getId();

                for (Status status : result) {
                    //get origin status
                    if (oStatus == null && status.getRetweetedStatus() != null
                            && status.getRetweetedStatus().getCreatedAt() != null) {
                        oStatus = status.getRetweetedStatus();
                    }

                    //write file
                    writeStatus.Write(switchStatus(status));
                }
            } else {
                System.out.println("GetRepostStatusThread: break at page " + page);
                break;
            }
            System.out.println("--------------------------------->");
            System.out.println("GetRepostStatusThread: get " + totalCount + " status of " + this.mid + " at " + tokenPack.getCount()
                    + " in token " + tokenPack.token);
            System.out.println("--------------------------------->");
        }


        float vec = ((float) totalCount) / ((maxTime - minTime) / (1000 * 3600) + 1);
        if (oStatus != null && TweetLifeCycleEvaluator.isHotTweet(oStatus, vec)) {
            Tool.write(configuration.getRepostMidDir() + this.mid + "_mid_tmp", maxId.toString(), false, "utf-8");
        } else {
            Tool.write(configuration.getExpiredMidFile(), this.mid, true, "utf-8");
        }

        writeStatus.closeWrite();

        System.out.println("GetRepostStatusThread: " + this.mid + " complete!");
    }
}
