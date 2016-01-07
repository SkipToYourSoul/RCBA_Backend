package local.eventMonitor;

import myutil.Db;

import java.util.ArrayList;
import java.util.List;

public class Main {
    EventCrawler ec = null;
    EventInformationGetter ig = null;

    //UpdateDB updateDB = null;

    public Main() {
        this.ec = new EventCrawler();
        this.ig = new EventInformationGetter();

        //this.updateDB = new UpdateDB();
    }

    public static void main(String[] args) throws InterruptedException {
        Main main = new Main();
        int count = 10;

        List<String> events = new ArrayList();

        //use for update DataBase
        /*List<String> intro = new ArrayList();
        List<String> news = new ArrayList();
        List<String> videos = new ArrayList();
        List<String> pic = new ArrayList();*/

        //get events from EventCrawler
        events = main.ec.BaiduHotSpot(count);


        for (String event : events) {
            System.out.println(event);
            System.out.println(main.ig.getEventIntroduction(event));
            System.out.println(main.ig.getEventNewsUrl(event));
            System.out.println(main.ig.getEventVideoUrl(event));
            System.out.println(main.ig.getEventPicture(event));
            Thread.sleep(1000L);

            //use for update DataBase
            /*String s1 = main.ig.getEventIntroduction(event);;
            String s2 = main.ig.getEventNewsUrl(event);;
            String s3 = main.ig.getEventVideoUrl(event);;
            String s4 = main.ig.getEventPicture(event);;
            if ((s1 != null) && (s2 != null)) {
                if (((s3 != null ? 1 : 0) & (s4 != null ? 1 : 0)) != 0) {
                    intro.add(s1);
                    news.add(s2);
                    videos.add(s3);
                    pic.add(s4);
                }
            }*/
        }

        //main.updateDB.update(events, intro, pic, news, videos);

        System.out.println("-------END-------");
    }
}
