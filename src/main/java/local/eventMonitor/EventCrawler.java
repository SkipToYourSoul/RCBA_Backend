package local.eventMonitor;

import local.eventMonitor.tool.WebTool;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventCrawler
{
  WebTool webTool = new WebTool();
  
  public List<String> BaiduHotSpot(int eventCount)
  {
    List<String> events = new ArrayList();
    
    String content = this.webTool.getContent("http://top.baidu.com/buzz?b=341&fr=topbuzz_b1", "gbk");
    
    String regEx = "<a class=\"list-title\" target=\"_blank\" .*>.*?</a>";
    Pattern pattern = Pattern.compile(regEx);
    Matcher matcher = pattern.matcher(content);
    int count = 0;
    while ((matcher.find()) && (count < eventCount))
    {
      String event = matcher.group();
      event = event.split(">")[1].split("<")[0];
      event = event.replace("&quot;", "\"");
      events.add(event);
      count++;
    }
    System.out.println("Get " + count + " events from Baidu HotSpot. http://top.baidu.com/buzz.php?p=top10");
    
    return events;
  }
}
