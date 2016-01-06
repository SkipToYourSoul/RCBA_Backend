package token;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TokenManage
{
  static TokensPool tokenspool = null;
  int maxCount = 1000;
  static String LogDir = "." + File.separator + "rData" + File.separator + "Logs" + File.separator;
  static SimpleDateFormat inputFormatYMD = new SimpleDateFormat("yyyy-MM-dd");
  
  public TokenManage()
  {
    tokenspool = new TokensPool();
  }
  
  public void setMaxCount(int maxCount)
  {
    this.maxCount = maxCount;
  }
  
  public Token GetToken()
  {
    Token tokenPack = null;
    tokenPack = (Token)tokenspool.getTokenList().get(0);
    return tokenPack;
  }
  
  public synchronized Token GetNextToken()
  {
    Token tokenPack = null;
    int nextPos = tokenspool.getPos() + 1;
    tokenspool.setPos(nextPos);
    if (tokenspool.IsOver()) {
      tokenspool.setPos(0);
    }
    tokenPack = (Token)tokenspool.getTokenList().get(tokenspool.getPos());
    if (tokenPack == null)
    {
      System.out.println("Token error");
      return null;
    }
    tokenPack.setCount(0);
    System.out.println("Change Token : " + tokenPack.token);
    
    return tokenPack;
  }
  
  public boolean maxTokenCount(Token token)
  {
    if (token.getCount() > this.maxCount) {
      return false;
    }
    return true;
  }
  
  public static boolean refreshToken()
  {
    GetAccessToken wc = new GetAccessToken();
    try
    {
      wc.run();
      System.out.println("refresh token finished");
      //Tool.write(LogDir + inputFormatYMD.format(Long.valueOf(new Date().getTime())) + "token", "Refresh token!", true, "utf-8");
    }
    catch (IOException e)
    {
      return false;
    }
    catch (ParseException e)
    {
      return false;
    }
    return true;
  }
}
