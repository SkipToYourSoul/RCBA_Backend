package local.crawler.control;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration
{
  Properties props = new Properties();
  
  public void load(String path)
  {
    try
    {
      this.props.load(new FileInputStream(path));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public String getTokenFile(){
    return this.props.getProperty("tokenFile");
  }

  //uid
  public String getUidFile()
  {
    return this.props.getProperty("uidFile");
  }

  //uid pos
  public String getUidPosFile()
  {
    return this.props.getProperty("uidPosFile");
  }

  //user's sinceId
  public String getUserMidDir()
  {
    return this.props.getProperty("userMidDir");
  }

  //files
  public String getFileDir(){
    return this.props.getProperty("fileDir");
  }

  //log
  public String getLogDir()
  {
    return this.props.getProperty("logDir");
  }

  //status
  public String getStatusDir()
  {
    return this.props.getProperty("statusDir");
  }

  //repost mid
  public String getMidFile()
  {
    return this.props.getProperty("midFile");
  }

  //repost mid pos
  public String getMidPosFile()
  {
    return this.props.getProperty("midPosFile");
  }

  //repost's sinceId
  public String getRepostMidDir()
  {
    return this.props.getProperty("repostMidDir");
  }

  //expired mid
  public String getExpiredMidFile()
  {
    return this.props.getProperty("expiredFile");
  }
  
  public int getMaxTokenCount()
  {
    return Integer.valueOf(this.props.getProperty("maxTokenCount", "200")).intValue();
  }
}
