package local.eventMonitor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import myutil.Db;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.util.FilterModifWord;

public class UpdateDB
{
  private Db db;
  Connection conn = null;
  PreparedStatement ps = null;
  ResultSet rs = null;
  private String today = null;
  
  UpdateDB(Db db)
  {
    this.db = db;
    this.conn = db.createConnection();
    this.today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
  }
  
  public void update(List<String> title, List<String> info, List<String> pic, List<String> news, List<String> videos)
  {
    Set<String> existEventSet = new HashSet();
    int maxId = 0;
    try
    {
      this.ps = this.conn.prepareStatement("SELECT EventName FROM eventinput");
      this.rs = this.ps.executeQuery();
      while (this.rs.next()) {
        if (!existEventSet.contains(this.rs.getString(1))) {
          existEventSet.add(this.rs.getString(1));
        }
      }
      this.ps = this.conn.prepareStatement("SELECT max(EventId) FROM eventinput");
      this.rs = this.ps.executeQuery();
      if (this.rs.next()) {
        maxId = this.rs.getInt(1);
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    int updateCount = 0;
    int insertCount = 0;
    for (int i = 0; i < title.size(); i++) {
      if (existEventSet.contains(title.get(i)))
      {
        try
        {
          this.ps = this.conn.prepareStatement("UPDATE eventinput SET isSearch = 1 WHERE EventName = '" + (String)title.get(i) + "'");
          this.ps.execute();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
        updateCount++;
      }
      else
      {
        String reg = getEventReg((String)title.get(i));
        String sql = "INSERT INTO eventinput (EventId,EventName,EventReg,isSearch,EventInfo,EventPic,EventUrl,EventVUrl,EventTime) VALUES (?,?,?,?,?,?,?,?,?)";
        try
        {
          this.ps = this.conn.prepareStatement(sql);
          this.ps.setInt(1, ++maxId);
          this.ps.setString(2, (String)title.get(i));
          this.ps.setString(3, reg);
          this.ps.setInt(4, 1);
          this.ps.setString(5, (String)info.get(i));
          this.ps.setString(6, (String)pic.get(i));
          this.ps.setString(7, (String)news.get(i));
          this.ps.setString(8, (String)videos.get(i));
          this.ps.setString(9, this.today);
          this.ps.execute();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
        insertCount++;
      }
    }
    try
    {
      if (this.ps != null) {
        this.ps.close();
      }
      this.rs.close();
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    System.out.println("Update DB: update " + updateCount + ", insert " + insertCount + "!");
  }
  
  private String getEventReg(String name)
  {
    String reg = "";
    List<Term> parse = NlpAnalysis.parse(name);
    FilterModifWord.insertStopNatures(new String[] { "w", "m", "null" });
    parse = FilterModifWord.modifResult(parse);
    for (int i = 0; i < parse.size(); i++) {
      if (((Term)parse.get(i)).getName().length() > 1) {
        reg = reg + ((Term)parse.get(i)).getName() + ".*";
      }
    }
    reg = reg.substring(0, reg.length() - 2);
    
    return reg;
  }
}
