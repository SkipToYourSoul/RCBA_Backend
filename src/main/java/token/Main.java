package token;

public class Main
{
  public static void main(String[] args)
  {
    TokenManage tm = new TokenManage();
    Token tokenPack = null;
    tokenPack = tm.GetToken();
    for (int i = 0; i < 1200; i++) {
      if (tm.maxTokenCount(tokenPack))
      {
        System.out.println(tokenPack.token + " " + tokenPack.count);
        tokenPack.addCount();
      }
      else
      {
        tokenPack = tm.GetNextToken();
      }
    }
  }
}
