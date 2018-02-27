import java.awt.Font;
import java.awt.Graphics;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JPanel;

class Top extends JPanel{
   
	String Uname;
	public void getUsername(String Username) {
		   Uname = Username;
	   }
   
   @Override
   protected void paintComponent(Graphics g) {
      // TODO Auto-generated method stub
      super.paintComponent(g);
      
      Date date = new Date();
      SimpleDateFormat text = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");	// 현재 날짜

      Font f = new Font("Times", Font.BOLD, 15);
      g.setFont(f);
      
      g.drawString("현재 접속자 정보 : "+ ReserveUI.loginusername  , 100, 25);
      g.drawString("접속 일시 : " + text.format(date), 800, 25);	// 날짜 출력
      this.setSize(1550, 30);
   }
   
   

}