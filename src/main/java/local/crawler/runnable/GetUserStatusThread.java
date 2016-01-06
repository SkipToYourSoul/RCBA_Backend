package local.crawler.runnable;

import local.crawler.control.Controller;
import local.crawler.control.TweetLifeCycleEvaluator;
import myutil.MyWriter;
import myutil.StringReaderOne;
import myutil.Tool;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.WeiboException;

import java.util.List;

/**
 * get user status
 * version: store status to local files
 *
 * @author liye
 */
public class GetUserStatusThread extends Controller implements Runnable {

    //user's sleep time
    public static int sleepTime = 5000;

    //files to control pos and mid
    private StringReaderOne readUid = null;
    private StringReaderOne readPos = null;
    private StringReaderOne readMid = null;

    //crawl set
    private Paging paging = new Paging();

    //worker
    private UserStatusFunction worker = new UserStatusFunction();

    public GetUserStatusThread() {
        super("GetUserStatusThread");
        this.readUid = new StringReaderOne(configuration.getUidFile());
        this.readPos = new StringReaderOne(configuration.getUidPosFile());
    }

    //user worker
    private class UserStatusFunction extends CrawlerWorker {
        private String uid = null;

        @Override
        public void crawlerImpl() throws WeiboException {
            res = weibo.getUserStream(uid,paging);
            randomSleep(sleepTime);
        }
    }

    public void run() {
        //writer
        MyWriter writeStatus = null;

        //get user position
        int pos = 0;
        if (!this.readPos.IsOver()) {
            pos = Integer.parseInt(this.readPos.GetStrictNewID());
        }
        this.readUid.setPos(pos);

        //get user status
        while (true){
            if (!this.readUid.IsOver()) {

                //get previous user's sinceId
                String sinceMid = "1";
                String uid = this.readUid.GetStrictNewID();
                createNewFile(configuration.getUserMidDir() + uid + "_mid_tmp");
                this.readMid = new StringReaderOne(configuration.getUserMidDir() + uid + "_mid_tmp");
                if (!this.readMid.IsOver()) {
                    sinceMid = String.valueOf(this.readMid.GetStrictNewID());
                }
                Long sinceId = Long.parseLong(sinceMid);

                writeStatus = new MyWriter(timelyFile(), "utf-8");

                //get status
                if (sinceId == 1)
                    paging.setCount(2);
                else
                    paging.setCount(200);
                paging.setPage(1);
                paging.setSinceId(sinceId);
                worker.uid = String.valueOf(uid);
                List<Status> result = crawlStatus(worker);

                //handle the result
                if (result.size() > 0){
                    Long MaxMid = sinceId;
                    for (Status status : result){
                        if (status.getId() > sinceId) {
                            //handle hot tweets
                            Status oStatus = status.getRetweetedStatus() == null ? status : status.getRetweetedStatus();
                            if (oStatus != null && TweetLifeCycleEvaluator.isHotTweet(oStatus, 0)) {
                                Tool.write(configuration.getMidFile(), oStatus.getMid(), true, "utf-8");
                            }
                            //write file
                            writeStatus.Write(switchStatus(status));
                        }
                        if (status.getId() > MaxMid.longValue()) {
                            MaxMid = status.getId();
                        }
                    }
                    //update maxId
                    Tool.write(configuration.getUserMidDir() + uid + "_mid_tmp", MaxMid.toString(), false, "utf-8");
                }

                //update uid position
                this.readUid.nextPos();
                Tool.write(configuration.getUidPosFile(), String.valueOf(this.readUid.getPos()), false, "UTF8");

                writeStatus.closeWrite();

                System.out.println("--------------------------------->");
                System.out.println("GetUserStatusThread: Crawl " + uid + " at " + sinceId + " get " + result.size() + " status at "
                        + tokenPack.getCount() + " in token " + tokenPack.token);
                System.out.println("--------------------------------->");
            } else {
                this.readUid.setPos(0);
            }
        }
    }
}
