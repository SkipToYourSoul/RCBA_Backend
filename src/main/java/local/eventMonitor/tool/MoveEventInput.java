package local.eventMonitor.tool;

import myutil.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MoveEventInput
{
  public static void main(String[] args)
    throws SQLException
  {
    String date = "2015-12-13";
    Db db1 = new Db("ocba", "root", "root");
    Connection conn = db1.createConnection();
    PreparedStatement ps = conn.prepareStatement("SELECT * FROM eventinput WHERE EventTime = '" + date + "'");
    ResultSet rs = ps.executeQuery();
    ArrayList<ArrayList<String>> list = new ArrayList();
    while (rs.next())
    {
      ArrayList<String> record = new ArrayList();
      record.add(rs.getString(2));
      record.add(rs.getString(3));
      record.add(String.valueOf(rs.getInt(4)));
      record.add(rs.getString(5));
      record.add(rs.getString(6));
      record.add(rs.getString(7));
      record.add(rs.getString(8));
      record.add(rs.getString(9));
      list.add(record);
    }
    System.out.println(list.size());
    
    Db db2 = new Db("ocba", "10.11.1.212", "root", "root");
    conn = db2.createConnection();
    ps = conn.prepareStatement("SELECT MAX(EventId) FROM eventinput");
    rs = ps.executeQuery();
    int maxEventId = 0;
    while (rs.next()) {
      maxEventId = rs.getInt(1);
    }
    ps = conn.prepareStatement("INSERT INTO eventinput VALUES (?,?,?,?,?,?,?,?,?)");
    for (int i = 0; i < list.size(); i++)
    {
      ps.setInt(1, ++maxEventId);
      ps.setString(2, (String)((ArrayList)list.get(i)).get(0));
      ps.setString(3, (String)((ArrayList)list.get(i)).get(1));
      ps.setInt(4, Integer.valueOf((String)((ArrayList)list.get(i)).get(2)).intValue());
      ps.setString(5, (String)((ArrayList)list.get(i)).get(3));
      ps.setString(6, (String)((ArrayList)list.get(i)).get(4));
      ps.setString(7, (String)((ArrayList)list.get(i)).get(5));
      ps.setString(8, (String)((ArrayList)list.get(i)).get(6));
      ps.setString(9, (String)((ArrayList)list.get(i)).get(7));
      ps.execute();
    }
  }
}
