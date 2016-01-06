package local.crawler.control;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import token.Token;
import token.TokenManage;
import myutil.Tool;
import weibo4j.Weibo;
import weibo4j.model.WeiboException;
import weibo4j.http.Response;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.User;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Controller {

    private String taskName;

    public Controller(String taskName) {
        this.taskName = taskName;
        System.out.println(this.taskName + " start!");
    }

    public Controller(){}

    //retry number
    private static final int RETRY_NUM = 10;

    //logger
    private static final Logger logger = Logger.getLogger(Controller.class);

    //weibo and token
    protected static Weibo weibo = new Weibo();
    protected static TokenManage tm = new TokenManage();
    protected static Token tokenPack = tm.GetToken();
    protected static Configuration configuration = new Configuration();

    //time format
    protected SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    protected SimpleDateFormat inputFormatYMD = new SimpleDateFormat("yyyy-MM-dd");
    protected SimpleDateFormat inputFormatYMDH = new SimpleDateFormat("yyyy-MM-dd-HH");

    //separator
    private static String separator = "|#|";

    static {
        //log
        PropertyConfigurator
                .configure(new File(System.getProperty("basedir", "./"), "config/log4j.properties").getAbsolutePath());

        //configuration
        configuration.load(".//config/RC.conf");

        //set token
        tm.setMaxCount(configuration.getMaxTokenCount());
        weibo.setToken(tokenPack.token);

        //create files and file dirs
        createFileDir(configuration.getLogDir());
        createFileDir(configuration.getStatusDir());
        createFileDir(configuration.getRepostMidDir());
        createFileDir(configuration.getUserMidDir());
        createFileDir(configuration.getFileDir());

        createNewFile(configuration.getUidPosFile());
        createNewFile(configuration.getMidFile());
        createNewFile(configuration.getMidPosFile());
        createNewFile(configuration.getExpiredMidFile());
    }

    //------------- crawler -----------------
    public abstract class CrawlerWorker {
        public Response res = null;

        public abstract void crawlerImpl() throws weibo4j.model.WeiboException;

        public Response getRes() {
            return res;
        }

    }

    //get crawl result and construct
    public List<Status> crawlStatus(CrawlerWorker worker) {
        List<Status> statusList = new ArrayList<Status>();

        StatusWapper wapper = null;
        if (crawl(worker)) {
            try {
                Response res = worker.getRes();
                // 解析
                if (!res.toString().equals("[]") && res != null) {
                    wapper = Status.constructWapperStatus(res.toString());
                    if (wapper == null) {
                        return statusList;
                    }
                    statusList = wapper.getStatuses();
                }
                if (res.toString().startsWith("[")) {
                    statusList = Status.constructStatuses(Tool.removeEol(res.toString()));
                }
            } catch (WeiboException e) {
                logger.error(e.getMessage());
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }

        return statusList;
    }

    //do the crawl
    public boolean crawl(CrawlerWorker worker) {
        boolean ret = false;
        for (int i = 0; i < RETRY_NUM; i++) {
            try {
                worker.crawlerImpl();
                addTokenCount();
                ret = true;
                break;
            }  catch (WeiboException e) {
                logger.error(e.getMessage());
                // 获取时报的异常
                if (e.getStatusCode() == -1) {
                    // 网络连接超时
                    i = 0;
                } else if (e.getStatusCode() == 401) {
                    setNextToken();
                } else if (e.getStatusCode() == 403) {
                    if (e.getMessage().contains("invalid_access_token")) {
                        setNextToken();
                    }
                    randomSleep(4000);
                } else if (e.getStatusCode() == 400) {
                    // This is the status code will be returned during rate
                    // limiting.error:expired_token
                    // error_code:21327/2/statuses/repost_timeline.json
                    setNextToken();
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        return ret;
    }

    //set token = token + 1
    protected void addTokenCount() {
        tokenPack.addCount();
        if (!tm.maxTokenCount(tokenPack)) {
            tokenPack = tm.GetNextToken();
            weibo.setToken(tokenPack.token);
        }
    }

    //set next token
    protected void setNextToken() {
        tokenPack = tm.GetNextToken();
        weibo.setToken(tokenPack.token);
    }

    //download new token from web
    protected void changeNewToken(){
        TokenManage.refreshToken();
        tm = new TokenManage();
        tokenPack = tm.GetNextToken();
        tm.setMaxCount(configuration.getMaxTokenCount());
        weibo.setToken(tokenPack.token);
    }

    //switch status to string
    protected String switchStatus(Status status) {
        String result = null;
        String time = null;
        String text = null;
        String location = null;
        User user = null;

        //result = mid|#|rtMid|#|time|#|text|#|repostCount|#|uid|#|uname|#|gender|#|isV|#|location|#|source
        user = status.getUser();
        if ((status.getUser() != null) && (status.getCreatedAt() != null) && (user != null)) {
            time = this.inputFormat.format(status.getCreatedAt());
            text = status.getText();
            if (status.getRetweetedStatus() != null) {
                Status rtStatus = status.getRetweetedStatus();
                result = status.getMid() + separator + rtStatus.getMid() + separator + time + separator + text + separator + status.getRepostsCount();
            } else {
                result = status.getMid() + separator + "" + separator + time + separator + text + separator + status.getRepostsCount();
            }
            result = result + separator + user.getId() + separator + user.getName() + separator + user.getGender() + separator + user.isVerified();

            location = user.getLocation();
            if (location == null) {
                location = "";
            }
            result = result + separator + location;
            if (status.getSource() != null) {
                result = result + separator + status.getSource().toString().split(">")[1].split("<")[0];
            } else {
                result = result + separator + "";
            }
        }

        return result;
    }
    //-------------end crawler-----------------

    //-------------common function-------------
    //create file
    protected static void createNewFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //create file dir
    protected static void createFileDir(String path) {
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
    }

    //random sleep
    protected void randomSleep(int sleeping) {
        Random ra = new Random();
        int random = ra.nextInt(2000) - 1000;
        System.out.println(this.taskName + " sleep " + (sleeping + random) + "ms!");
        try {
            Thread.sleep(sleeping + random);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //create file dir according the time
    protected String timelyFile(){
        String path = configuration.getStatusDir() + this.inputFormatYMD.format(Long.valueOf(new Date().getTime())) + File.separator;
        createFileDir(path);
        return path + this.inputFormatYMDH.format(Long.valueOf(new Date().getTime()));
    }
    //-------------common function-------------
}
