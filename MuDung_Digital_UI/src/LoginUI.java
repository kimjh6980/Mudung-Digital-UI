
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginUI extends JFrame {

	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@KJH-GE63VR7RE:1521:xe";
	private Connection con = null;
	private Statement pstmt = null;
	private ResultSet rs = null;

	JTextField jt1 = new JTextField();
	JPasswordField jt2 = new JPasswordField();
	JLabel idl = new JLabel("ID : ");
	JLabel pwl = new JLabel("PW : ");

	JButton loginB = new JButton("Login");
	JButton joinB = new JButton("회원가입");

	private FlowLayout fl = new FlowLayout();
	
	ImageIcon mdimg = new ImageIcon("src/image/md.jpg");
	ImageIcon Devimg = new ImageIcon("src/image/Devimg.jpg");

	Image omdimg = mdimg.getImage();
	Image cmdimg = omdimg.getScaledInstance(300, 550, Image.SCALE_SMOOTH);
	ImageIcon smdimg = new ImageIcon(cmdimg);
	
	Image oDevimg = Devimg.getImage();
	Image cDevimg = oDevimg.getScaledInstance(200, 300, Image.SCALE_SMOOTH);
	ImageIcon sDevimg = new ImageIcon(cDevimg);
	
	JLabel imageLabel = new JLabel();
	JLabel devimgLabel = new JLabel();
	
	String Username;
	String UserPW;
	
	JLabel Developer = new JLabel("제작자 : 20134833 김진혁");

	public LoginUI() {
		setTitle("무등도서관 디지털자료실 예약시스템");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setLayout(null);
		JPanel panel = new JPanel();

		// 이미지 받기
		imageLabel.setIcon(smdimg);
		devimgLabel.setIcon(sDevimg);
		
		idl.setBounds(400, 30, 100, 100);
		pwl.setBounds(400, 80, 100, 100);
		jt1.setBounds(500, 60, 200, 30);
		jt2.setBounds(500, 110, 200, 30);

		loginB.setBounds(450, 160, 200, 50);
		joinB.setBounds(450, 220, 200, 50);
		
		imageLabel.setBounds(10, 10, 300, 550);
		devimgLabel.setBounds(550, 280, 200, 300);
		
		
		Developer.setBounds(350, 480, 150, 100);
		
		add(idl);
		add(pwl);
		add(jt1);
		add(jt2);
		add(loginB);
		add(joinB);
		add(imageLabel);
		add(devimgLabel);
		add(Developer);
		setVisible(true);
		loginB.addActionListener(new OKListener(this));
		joinB.addActionListener(new JoinListener(this));
	}

	public class JoinListener implements ActionListener {
		public JoinListener(JFrame f) {
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			new JoinMember();
		}

	}

	public class OKListener implements ActionListener {
		JFrame loginDialog;

		public OKListener(JFrame f) {
			// TODO Auto-generated constructor stub
			loginDialog = f;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			try {
				System.out.println("DB Connect~~!!!");
				Class.forName(driver);
				con = DriverManager.getConnection(url, "MUDL", "1234");
				pstmt = con.createStatement();

				System.out.println(arg0.getActionCommand());
				Username = jt1.getText();
				UserPW = jt2.getText();
				System.out.println(Username + "+" + UserPW);
				if (Username != null && UserPW != null) {
					String searchPW = "select USERPW from USERDATA where USERID = \'" + Username + "\'";
					String userstate = "select USERPOSSIBLE from USERDATA where USERID = '" + Username + "'";
					String confirmstate = null;
					String confirmPW = null;
					System.out.println(searchPW);
					rs = pstmt.executeQuery(searchPW);

					try {
						while (rs.next()) {
							confirmPW = rs.getString("USERPW");
							System.out.println("UPW :" + UserPW + "+" + confirmPW);

						}
						if (jt2.getText().equals(confirmPW)) {
							// if(UserPW == confirmPW) {
							// 여기 밑은 로그인 성공 때 넘어가는거임
							JOptionPane.showMessageDialog(loginDialog, Username + "님 환영합니다.");
							ReserveUI.loginuser = Username;
							dispose();
							// 여기서 예약중일때랑 대기중일떄 나눠야됨

							rs = pstmt.executeQuery(userstate);
							while (rs.next()) {
								confirmstate = rs.getString("USERPOSSIBLE");
							}

							System.out.println(confirmstate);
							if (confirmstate != null) {
								new ReturnUI();
							} else {
								new TabbedUI();
							}
						} else {
							JOptionPane.showMessageDialog(null, "로그인에 실패하였습니다.", "로그인 실패", JOptionPane.WARNING_MESSAGE);
						}

					} catch (Exception e) {
						System.out.println("db Connect fail");
						// e.printStackTrace();
					} finally {
						try {
							if (rs != null)
								rs.close();
							if (pstmt != null)
								pstmt.close();
							if (con != null)
								con.close();
						} catch (Exception e) {
							System.out.println(e.getMessage());
						}
					}

				}
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}

	public static void main(String[] args) {
		new LoginUI();
	}

}
