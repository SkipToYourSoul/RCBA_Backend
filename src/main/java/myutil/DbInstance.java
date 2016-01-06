package myutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by 叶 on 2015/5/13.
 *
 */
public class DbInstance {
    private static final String DRIVERCLASS = "com.mysql.jdbc.Driver";
    static String URL = "jdbc:mysql://localhost:3306/industrydult";

    public static String getURL()
    {
        return URL;
    }
    public static void setURL(String uRL)
    {
        URL = uRL;
    }

    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private static final ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();
    private static Connection conn = null;// 数据库连接

    static {
        // 通过静态方法加载数据库驱动
        try {
            Class.forName(DRIVERCLASS).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 创建数据库连接的方法
    public static Connection getConnection() {
        conn = threadLocal.get(); // 从线程中获得数据库连接
        if (conn == null) {
        // 没有可用的数据库连接
            try {
                conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);// 创建新的数据库连接
                threadLocal.set(conn); // 将数据库连接保存到线程中
            }
            catch (Exception e) {
                String[] infos = { "未能成功连接数据库！", "请确认本软件是否已经运行！" };
                System.out.println(infos);
                System.exit(0);// 关闭系统
                e.printStackTrace();
            }
        }
        return conn;
    }

    public static boolean closeConnection() { // 关闭数据库连接的方法
        boolean isClosed = true; // 默认关闭成功
        conn = threadLocal.get(); // 从线程中获得数据库连接
        threadLocal.set(null); // 清空线程中的数据库连接
        if (conn != null) { // 数据库连接可用
            try {
                conn.close(); // 关闭数据库连接
            }
            catch (SQLException e) {
                isClosed = false; // 关闭失败
                e.printStackTrace();
            }
        }
        return isClosed;
    }
}
