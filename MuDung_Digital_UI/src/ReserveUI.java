import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

public class ReserveUI extends JPanel {
	private static JTextField[] Tfield;

	private Date date;
	private SimpleDateFormat datetext;
	private String sql;

	private static String driver = "oracle.jdbc.driver.OracleDriver";
	private static String url = "jdbc:oracle:thin:@KJH-GE63VR7RE:1521:xe";
	private static Connection con = null;
	private static Statement pstmt = null;
	private static ResultSet rs = null;

	private static String setnum;
	private static String setgenre;
	private static String settitle;
	private static String setstate;
	private static String setage;

	public static String loginuser = null;
	public static String loginusername = null;
	public static int setbooknum = 0;

	public String userpossible = null;
	public String userreason = null;

	public ReserveUI() {
		// TODO Auto-generated constructor stub
		setLayout(null);

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, "MUDL", "1234");
			System.out.println("ReserveUI->" + setbooknum);
			pstmt = con.createStatement();
			String sql = "Select USERID, USERNAME, USERPOSSIBLE, USERREASON FROM USERDATA WHERE USERID = \'" + loginuser
					+ "\'";
			rs = pstmt.executeQuery(sql);

			while (rs.next()) {
				loginusername = rs.getString("USERNAME");
				userpossible = rs.getString("USERPOSSIBLE");
				userreason = rs.getString("USERREASON");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ReserveUI_Button();
		FixLable_JLabel();
		TextField();
	}

	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);

		Font f = new Font("Times", Font.BOLD, 15); // 글꼴 설정
		g.setFont(f);
		g.drawString("----- 사  용  자   정  보 -----", 100, 100);
		g.drawString("----- 유    의   사    항 -----", 100, 500);

		g.drawString("1. 대여기간은 1주일입니다.", 100, 530);
		g.drawString("2. 연장은 회당 1회, 1주일입니다.", 100, 560);
		g.drawString("3. 대출반납일을 넘길 경우, 불이익이 발생할 수 있습니다.", 100, 590);

		f = new Font("Times", Font.BOLD, 12);
		this.setPreferredSize(new Dimension(1200, 30));
	}

	private void ReserveUI_Button() {
		date = new Date();
		datetext = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss"); // 포맷 지정
		JButton[] Button = new JButton[3];
		Button[0] = new JButton(" 취      소 ");
		Button[1] = new JButton(" 예      약 ");
		Button[2] = new JButton(" 도서찾기 ");
		for (int i = 0; i < 3; i++) {
			Button[i].setSize(100, 40);
			add(Button[i]);
		}
		Button[0].setLocation(820, 50);
		Button[1].setLocation(950, 50);
		Button[2].setSize(100, 280);
		Button[2].setLocation(950, 150);

		Button[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				ReserveUI_Button();
				FixLable_JLabel();
				TextField();
				JOptionPane.showMessageDialog(null, "취소되었습니다");
			}
		});

		Button[1].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				if (Tfield[2].getText().equals("")) {
					JOptionPane.showMessageDialog(null, "도서를 선택해주세요");
				} else {
					JOptionPane.showMessageDialog(null, "신청되었습니다");
					try {
						createInsertSQL();
						System.exit(0);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		Button[2].addActionListener(new ActionListener() { // 예약창띄우기
			@Override
			public void actionPerformed(ActionEvent e) {
				new SetBook();
			}
		});
	}

	private void FixLable_JLabel() {
		JLabel[] FixLabel = new JLabel[10]; // 각 레이블
		String[] FixLabelName = { "회원번호", "회원명", "서적번호", "서적명", "서적분야", "대여일", "반납일", "회원상태", "서적상태", "불가사유" };
		EtchedBorder eb = new EtchedBorder(Color.BLACK, Color.GRAY);

		int FixLableIndex = 0;
		for (String name : FixLabelName) { // 레이블 배치
			FixLabel[FixLableIndex] = new JLabel(name);
			FixLabel[FixLableIndex].setHorizontalAlignment(SwingConstants.CENTER);
			FixLabel[FixLableIndex].setBorder(eb);
			FixLabel[FixLableIndex++].setSize(120, 30);
		}
		FixLabel[0].setLocation(150, 150);
		FixLabel[1].setLocation(600, 150);
		FixLabel[2].setLocation(150, 200);
		FixLabel[3].setLocation(600, 200);
		FixLabel[3].setSize(120, 80);
		FixLabel[4].setLocation(150, 250);
		FixLabel[5].setLocation(150, 300);
		FixLabel[6].setLocation(600, 300);
		FixLabel[7].setLocation(150, 350);
		FixLabel[8].setLocation(600, 350);
		FixLabel[9].setLocation(150, 400);

		for (int i = 0; i < 10; i++) {
			add(FixLabel[i]);
		}
	}

	private void TextField() {
		Tfield = new JTextField[10];

		for (int i = 0; i < 10; i++) {
			Tfield[i] = new JTextField();
			Tfield[i].setSize(200, 30);
			Tfield[i].setEnabled(false);
		}

		Tfield[0].setLocation(270, 150);
		Tfield[1].setLocation(720, 150);
		Tfield[2].setLocation(270, 200);
		Tfield[3].setLocation(270, 250);
		Tfield[4].setLocation(720, 200);
		Tfield[4].setSize(200, 80);
		Tfield[5].setLocation(270, 300);
		Tfield[6].setLocation(720, 300);
		Tfield[7].setLocation(270, 350);
		Tfield[8].setLocation(720, 350);
		Tfield[9].setLocation(270, 400);
		Tfield[9].setSize(650, 30);

		Tfield[0].setText(loginuser);
		Tfield[1].setText(loginusername);

		String userPset = "";
		if (userpossible == null) {
			userPset = "대여가능";
		} else {
			userPset = "대여불가";
		}
		Tfield[7].setText(userPset);

		Tfield[9].setText(userreason);

		Tfield[5].setText(datetext.format(date));

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(cal.DAY_OF_MONTH, 7);
		Tfield[6].setText(datetext.format(cal.getTime()));

		for (int i = 0; i < 10; i++) {
			add(Tfield[i]);
		}

	}

	private void createInsertSQL() throws SQLException {
		sql = "INSERT INTO RHISTORY VALUES(";
		sql += "'" + Tfield[0].getText() + "',";
		sql += Tfield[2].getText() + ",";
		sql += "'" + Tfield[5].getText() + "',";
		sql += "'" + Tfield[6].getText() + "',";
		sql += "'" + 0 + "',";
		sql += "'" + Tfield[4].getText() + "',";
		sql += "'N')";
		pstmt.executeUpdate(sql);
		System.out.println(sql);

		sql = "update USERDATA set USERPOSSIBLE='대여중' where USERID = '" + loginuser + "'";
		pstmt.executeUpdate(sql);
		System.out.println(sql);
		sql = "update BOOKINFO set 대여가능='대여중' where 청구번호 = '" + Tfield[2].getText() + "'";
		pstmt.executeUpdate(sql);
		System.out.println(sql);

	}

	public static void settingBook(int setbooknum) throws SQLException {
		// TODO Auto-generated method stub
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, "MUDL", "1234");
			System.out.println("ReserveUI->" + setbooknum);
			pstmt = con.createStatement();

			String sql = "SELECT 청구번호, 장르, 타이틀명, 관람등급, 대여가능 FROM BOOKINFO WHERE 청구번호 = '" + setbooknum + "'";

			rs = pstmt.executeQuery(sql);
			while (rs.next()) {
				setnum = rs.getString("청구번호");
				setgenre = rs.getString("장르");
				settitle = rs.getString("타이틀명");
				setstate = rs.getString("대여가능");
			}

			Tfield[2].setText(setnum);
			Tfield[3].setText(setgenre);
			Tfield[4].setText(settitle);
			if (setstate == null) {
				Tfield[8].setText("대여가능");
			} else {
				Tfield[8].setText("대여불가");
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}