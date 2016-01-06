package myutil;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class StringReaderOne
{
  ArrayList<String> ids = null;
  int pos = 0;
  
  public StringReaderOne(String path)
  {
    this.ids = new ArrayList();
    Input(path);
  }
  
  public int getIdsSize()
  {
    return this.ids.size();
  }
  
  public int getPos()
  {
    return this.pos;
  }
  
  public void setPos(int pos)
  {
    if ((this.ids != null) && (pos <= this.ids.size())) {
      this.pos = pos;
    }
  }
  
  public boolean IsOver()
  {
    return this.pos >= this.ids.size();
  }
  
  public void nextPos()
  {
    this.pos += 1;
  }
  
  public String GetStrictNewID()
  {
    if (this.pos < this.ids.size()) {
      return (String)this.ids.get(this.pos);
    }
    return null;
  }
  
  void Input(String path)
  {
    String inline = "";
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
        if ((inline.length() != 0) && 
        
          (inline.charAt(0) != '/'))
        {
          inline = inline.trim();
          if ((!inline.equals("")) && 
          
            (!this.ids.contains(inline))) {
            this.ids.add(inline);
          }
        }
      }
      reader.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  public void updateIds(String path, int pos)
  {
    String inline = "";
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
      int c = 1;
      while ((inline = reader.readLine()) != null) {
        if ((inline.length() != 0) && 
        
          (inline.charAt(0) != '/'))
        {
          inline = inline.trim();
          if (!inline.equals(""))
          {
            if ((!this.ids.contains(inline)) && (c >= pos)) {
              this.ids.add(inline);
            }
            c++;
          }
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
