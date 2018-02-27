import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

public class TabbedUI extends JFrame {
   public static Container TabPannel;		// 탭구성함수

   public static boolean close = false;
   
   public static String user = null;
   
   public TabbedUI() {
      // TODO Auto-generated constructor stub
      init();
   }
   JTabbedPane pannel = new JTabbedPane();    
   
   JTabbedPane createTabbedPane() {	// 탭 구성
      pannel.addTab("예 약 하 기", new ReserveUI());
      pannel.addTab("예 약 내 역", new RHistoryUI());
      pannel.addTab("사용자정보", new USERDUI()); 
      return pannel;
   }

   public void init() {
      TabPannel = this.getContentPane();
      TabPannel.setLayout(new BorderLayout(30, 30)); 
      setTitle("무등도서관 디지털자료실 예약시스템");	// 타이틀
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         
      JTabbedPane jtabbedpane = createTabbedPane();

      TabPannel.add(new Top(), BorderLayout.NORTH);
      TabPannel.add(jtabbedpane, BorderLayout.CENTER);
      setLocationByPlatform(true);
      
      setSize(1200, 800);
      setVisible(true);
   }
 
   public static void main(String[] args) {
      // TODO Auto-generated method stub
      new TabbedUI();   
   }	
}