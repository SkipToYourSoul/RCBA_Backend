package token;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TokensPool
{
  int pos = 0;
  ArrayList<Token> tokenList = null;
  String fPath = "." + File.separator + "Tokens" + File.separator + "tokens.txt";
  
  public TokensPool()
  {
    mainFunction();
  }
  
  public TokensPool(String path)
  {
    setFPath(path);
    mainFunction();
  }
  
  public void setFPath(String fPath)
  {
    this.fPath = fPath;
  }
  
  public int getPos()
  {
    return this.pos;
  }
  
  public void setPos(int pos)
  {
    this.pos = pos;
  }
  
  public ArrayList<Token> getTokenList()
  {
    return this.tokenList;
  }
  
  public void ResetPos()
  {
    this.pos = 0;
  }
  
  public boolean IsOver()
  {
    return this.pos >= this.tokenList.size();
  }
  
  public void mainFunction()
  {
    this.tokenList = new ArrayList();
    String inline = "";
    String path = this.fPath;
    BufferedReader reader = null;
    try
    {
      reader = new BufferedReader(new FileReader(path));
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    try
    {
      int c = 0;
      while ((inline = reader.readLine()) != null) {
        if (!inline.equals("")) {
          this.tokenList.add(new Token(inline));
        }
      }
      reader.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
