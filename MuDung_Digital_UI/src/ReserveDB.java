import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReserveDB {
   public Connection conn;
   public Statement stmt;
   public ResultSet rs;
   public static String temp;
   public String url = null;
   public String id = "system";	// 관리자 ID
   public String pw = "kl59609401";// 관리자 PW

   public ReserveDB() {
      try {
         Class.forName("oracle.jdbc.driver.OracleDriver");
         System.out.println("driver load success");

         try { 
            url = "jdbc:oracle:thin:@KJH-GE63VR7RE:1521:xe";
            conn = DriverManager.getConnection(url, id, pw);
            System.out.println("db connet success");
         } catch (SQLException e) {
            System.out.println(e);
         }
      } catch (ClassNotFoundException e) {
         System.out.println(e);
      }

   }

   public void insert(String sql) {	// DB삽입
      try {
         stmt = conn.createStatement();
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      try {
         stmt.executeUpdate(sql);

      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
   public void select(String sql) {	// DB선택
      try {
         stmt = conn.createStatement();
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      try {
         stmt.executeUpdate(sql);
         rs = stmt.executeQuery(sql);
         temp="";
         while (rs.next()) {
            temp = rs.getString("Name");
            System.out.println(temp);
         }
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
   public void close() {	// 종료
      try {
         if (stmt != null)
            stmt.close();
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
      try {
         conn.close();
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

}