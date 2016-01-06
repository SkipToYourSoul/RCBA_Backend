package token;

import java.util.Date;

public class Token
  implements Comparable<Token>
{
  public String token;
  int count;
  long timestamp;
  
  Token(String at)
  {
    this.token = at;
    this.count = 0;
    this.timestamp = new Date().getTime();
  }
  
  public int getCount()
  {
    return this.count;
  }
  
  public void setCount(int count)
  {
    this.count = count;
  }
  
  public long getTimestamp()
  {
    return this.timestamp;
  }
  
  public void addCount()
  {
    this.count += 1;
  }
  
  public int compareTo(Token o)
  {
    if (this.count < o.getCount()) {
      return -1;
    }
    if (this.count > o.getCount()) {
      return 1;
    }
    if (this.timestamp < o.getTimestamp()) {
      return -1;
    }
    if (this.timestamp > o.getTimestamp()) {
      return 1;
    }
    return 0;
  }
}
