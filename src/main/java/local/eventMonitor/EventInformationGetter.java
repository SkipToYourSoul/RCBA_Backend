package local.eventMonitor;

import local.eventMonitor.tool.WebTool;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventInformationGetter
{
  WebTool webTool = new WebTool();
  
  public String getEventIntroduction(String event)
  {
    String result = null;
    String newsUrl = null;
    try
    {
      newsUrl = "http://news.baidu.com/ns?tn=news&from=news&cl=2&rn=20&ct=1&word=" + URLEncoder.encode(event, "utf-8");
    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    String news = this.webTool.getContent(newsUrl, "utf-8");
    String regEx = "<div class=\"c-span18 c-span-last\">.*?<span class=";
    Pattern pattern = Pattern.compile(regEx);
    Matcher matcher = pattern.matcher(news);
    if (matcher.find())
    {
      result = matcher.group();
      result = result.split("</p>")[1].substring(0, result.split("</p>")[1].length() - 13);
      result = result.replace("<em>", "");
      result = result.replace("</em>", "");
    }
    return result;
  }
  
  public String getEventPicture(String event)
  {
    String result = null;
    String picUrl = null;
    try
    {
      picUrl = "http://image.baidu.com/search/index?tn=baiduimage&lm=-1&ct=201326592&cl=2&word=" + URLEncoder.encode(event, "gbk") + "&ie=gbk";
    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    String picContent = this.webTool.getContent(picUrl, "gbk");
    String regExp = "\"objURL\":\".*?\"";
    Pattern patternP = Pattern.compile(regExp);
    Matcher matcherP = patternP.matcher(picContent);
    if (matcherP.find()) {
      result = matcherP.group().split("\"")[3];
    }
    return result;
  }
  
  public String getEventNewsUrl(String event)
  {
    String result = null;
    try
    {
      result = "http://news.baidu.com/ns?tn=news&from=news&cl=2&rn=20&ct=1&word=" + URLEncoder.encode(event, "gbk");
    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    return result;
  }
  
  public String getEventVideoUrl(String event)
  {
    String result = null;
    try
    {
      result = "http://v.baidu.com/v?word=" + URLEncoder.encode(event, "gbk") + "&ct=301989888&rn=20&pn=0&db=0&s=0&fbl=800&ie=utf-8";
    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    return result;
  }
}
