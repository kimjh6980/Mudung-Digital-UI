import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class ReturnUI extends JFrame {
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@KJH-GE63VR7RE:1521:xe";
	private Connection con = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	private String sql;
	private String sql2;
	private String sql3;
	private String sql4;
	
	JTextField unt = new JTextField();
	JTextField bnt = new JTextField();
	JTextField btt = new JTextField();
	JTextField rentdt = new JTextField();
	JTextField returndt = new JTextField();
	JTextField extt = new JTextField();

	public String unf;
	public String bnf;
	public String btf;
	public String rentdf;
	public String returndf;
	public String extf;

	JButton RE = new JButton("반납");
	JButton EX = new JButton("연장");
	JButton cl = new JButton("취소");
	
	private Date date;
	private SimpleDateFormat datetext;
	
	public ReturnUI() {
		// TODO Auto-generated constructor stub
		date = new Date();
		datetext = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss"); // 포맷 지정
		
		init();

		labelUI();
		TfieldUI();
		searchR();
		
		RE.setBounds(100, 480, 100, 80);
		EX.setBounds(300,  480,  100,  80);
		cl.setBounds(500,  480,  100,  80);
		add(RE);
		add(EX);
		add(cl);
		
		RE.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	            // TODO Auto-generated method stub
	            String bookS = bnt.getText().toString();
	            int bookn = Integer.parseInt(bookS);
	            returndt.setText(datetext.format(date));
	            System.out.println("num : "+bookn);
	            JOptionPane.showMessageDialog(null, "반납되었습니다");
	            sql ="update USERDATA set USERPOSSIBLE='' where USERID = '" + ReserveUI.loginuser +"'";
	            sql2 ="update BOOKINFO set 대여가능='' where 청구번호 = '" + bookn +"'";
	            sql3 ="update RHISTORY set RETURNOK='Y' where USERNAME = '" + ReserveUI.loginuser + "' and RETURNOK = 'N'";
	            sql4 ="update RHISTORY set RETURNDAY='" + returndt.getText().toString() + "' where USERNAME = '" + ReserveUI.loginuser + "' and RETURNOK = 'N'";
	            try {
					pstmt.executeUpdate(sql);
					pstmt.executeUpdate(sql2);
					pstmt.executeUpdate(sql3);
					pstmt.executeUpdate(sql4);
					dispose();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	            
	         }
	      });
		
		EX.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	            // TODO Auto-generated method stub
	        	 SimpleDateFormat transFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
	        	 try {
					Date ed = transFormat.parse(returndf);
					Calendar cal = Calendar.getInstance();
		        	cal.setTime(ed);
		        	cal.add(Calendar.DAY_OF_MONTH, 7);
		        	
		        	returndf = datetext.format(cal.getTime()).toString();
				} catch (ParseException e2) {
					// TODO Auto-generated catch block
					System.out.println("날짜수정못함");
					e2.printStackTrace();
				}
	        	 
	        	 System.out.println("date : " + returndf);
	        	 returndt.setText(returndf);
	        	 
	        	 int temp = Integer.parseInt(extf);
	        	 temp += 1;
	        	 extf = Integer.toString(temp);
	        	 
	            JOptionPane.showMessageDialog(null, returndf + "까지 연장되었습니다.");
	            sql ="update RHISTORY set RETURNDAY = '" + returndf + "' where USERNAME = '" + unf + "' and RETURNOK = 'N'";
	            sql2 ="update RHISTORY set EXTENSIONDAY = '" + extf + "' where USERNAME = '" + unf + "' and RETURNOK = 'N'";
	            try {
					pstmt.executeUpdate(sql);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	            
	         }
	      });
		cl.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
			}
			
		});
	}

	public void init() {
		System.out.println("RETURNUI income");
		setLayout(null);
		setSize(700, 700);
		setVisible(true);
	}

	public void labelUI() {
		JLabel un = new JLabel("ID");
		JLabel bn = new JLabel("서적번호");
		JLabel bt = new JLabel("서적이름");
		JLabel rentd = new JLabel("대여일");
		JLabel returnd = new JLabel("반납예정일");
		JLabel ext = new JLabel("연장여부");

		un.setBounds(120, 100, 100, 30);
		bn.setBounds(100, 170, 100, 30);
		bt.setBounds(400, 100, 100, 130);
		rentd.setBounds(100, 300, 100, 30);
		returnd.setBounds(400, 300, 100, 30);
		ext.setBounds(100, 400, 100, 30);

		add(un);
		add(bn);
		add(bt);
		add(rentd);
		add(returnd);
		add(ext);
	}

	public void TfieldUI() {
		unt.setBounds(170, 100, 150, 30);
		bnt.setBounds(170, 170, 150, 30);
		btt.setBounds(470, 100, 150, 100);
		rentdt.setBounds(170, 300, 150, 30);
		returndt.setBounds(470, 300, 150, 30);
		extt.setBounds(170, 400, 150, 30);

		unt.setEnabled(false);
		bnt.setEnabled(false);
		btt.setEnabled(false);
		rentdt.setEnabled(false);
		returndt.setEnabled(false);
		extt.setEnabled(false);
		
		add(unt);
		add(bnt);
		add(btt);
		add(rentdt);
		add(returndt);
		add(extt);
	}

	public void searchR() {
		String query = "select USERNAME, BOOKNUM, RENTDAY, RETURNDAY, EXTENSIONDAY, BOOKTITLE from RHISTORY where USERNAME = '"
				+ ReserveUI.loginuser + "' and RETURNOK = 'N'";
		System.out.println("query : " + query);

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, "MUDL", "1234");
			pstmt = con.prepareStatement(query);
			System.out.println("pstmt");
			rs = pstmt.executeQuery();
			while (rs.next()) {

				unf = rs.getString("USERNAME");
				bnf = rs.getString("BOOKNUM");
				btf = rs.getString("BOOKTITLE");
				rentdf = rs.getString("RENTDAY");
				returndf = rs.getString("RETURNDAY");
				extf = rs.getString("EXTENSIONDAY");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(unf);

		unt.setText(unf);
		bnt.setText(bnf);
		btt.setText(btf);
		rentdt.setText(rentdf);
		returndt.setText(returndf);
		extt.setText(extf);

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ReturnUI();
	}
}