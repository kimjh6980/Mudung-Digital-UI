import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class JoinMember extends JFrame {
	private Date date;
	private String sql;
	private String confirmID;
	private SimpleDateFormat datetext;

	private static String driver = "oracle.jdbc.driver.OracleDriver";
	private static String url = "jdbc:oracle:thin:@KJH-GE63VR7RE:1521:xe";
	private static Connection con = null;
	private static Statement pstmt = null;
	private static ResultSet rs = null;

	JLabel TTitle = new JLabel("무등도서관 디지털자료실 회원가입");
	JLabel idl = new JLabel("ID");
	JLabel pwl = new JLabel("PW");
	JLabel namel = new JLabel("Name");
	JLabel phonel = new JLabel("Phone");
	JLabel datel = new JLabel("DATE");
	JLabel ugenderl = new JLabel("gender");

	JTextField idt = new JTextField();
	JTextField pwt = new JTextField();
	JTextField namet = new JTextField();
	JTextField phonet = new JTextField();
	JTextField datet = new JTextField();
	
	private static boolean idset = false;

	String gend[] = { "남", "여" };
	JComboBox gen = new JComboBox(gend);

	public JoinMember() {
		// TODO Auto-generated constructor stub
		setLayout(null);
		init();
		Labelview();
		Buttonset();
	}

	public void init() {
		setSize(600, 600);
		setVisible(true);
	}

	public void Labelview() {
		date = new Date();
		datetext = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss"); // 포맷 지정

		TTitle.setBounds(50, 10, 500, 100);
		idl.setBounds(30, 100, 50, 30);
		pwl.setBounds(300, 100, 50, 30);
		namel.setBounds(30, 200, 50, 30);
		phonel.setBounds(300, 200, 50, 30);
		datel.setBounds(30, 300, 50, 30);
		ugenderl.setBounds(300, 300, 50, 30);

		idt.setBounds(30, 130, 200, 30);
		pwt.setBounds(300, 130, 200, 30);
		namet.setBounds(30, 230, 200, 30);
		phonet.setBounds(300, 230, 200, 30);
		datet.setBounds(30, 330, 200, 30);

		datet.setText(datetext.format(date));
		datet.setEnabled(false);

		gen.setBounds(300, 330, 200, 30);

		add(TTitle);
		add(idl);
		add(pwl);
		add(namel);
		add(phonel);
		add(datel);
		add(ugenderl);

		add(idt);
		add(pwt);
		add(namet);
		add(phonet);
		add(datet);

		add(gen);

		try {
			Class.forName(driver);
			try {
				con = DriverManager.getConnection(url, "MUDL", "1234");
				pstmt = con.createStatement();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void Buttonset() {
		JButton OK = new JButton("가      입 ");
		JButton CANCEL = new JButton("취      소 ");

		JButton IDC = new JButton("중복확인");

		OK.setBounds(30, 400, 200, 50);
		CANCEL.setBounds(300, 400, 200, 50);
		IDC.setBounds(130, 100, 100, 30);

		add(OK);
		add(CANCEL);
		add(IDC);

		IDC.addActionListener(new ActionListener()	{
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String Username = idt.getText().toString();
				Username = Username.replaceAll(" ", "");
				System.out.println("U" + Username);
				String search = "select USERPW from USERDATA where USERID = \'" + Username + "\'";
				String confirmid = null;
				
				if(idt.getText().toString().trim().length() == 0)	{	// 길이로 문자열 받기
					JOptionPane.showMessageDialog(null,  "아이디를 입력해주세요");
				}	else	{
					try {
						rs = pstmt.executeQuery(search);
						while(rs.next()) {
							confirmid = rs.getString("USERPW");
						}
						if(confirmid == null)	{
							JOptionPane.showMessageDialog(null,  Username + "\n아이디는 사용가능합니다.");
							idt.setEnabled(false);
							idset = true;
							System.out.println("idset : " + idset);
						}	else	{
							JOptionPane.showMessageDialog(null,  Username + "\n아이디는 이미 존재합니다.");
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
			}
			
		});
	


		OK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(idt.getText().toString().trim().length() == 0)	{
					JOptionPane.showMessageDialog(null,  "아이디를 입력해주세요");
				}	else if(idset == false)	{
					JOptionPane.showMessageDialog(null,  "아이디 중복확인을 해주세요");
				}	else if(pwt.getText().toString().trim().length() == 0)	{
					JOptionPane.showMessageDialog(null,  "비밀번호를 입력해주세요");
				}	else if(namet.getText().toString().trim().length() == 0)	{
					JOptionPane.showMessageDialog(null,  "이름를 입력해주세요");
				}	else	{
					try {
						createInsertSQL();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		CANCEL.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
			}
		});

	}

	private void createInsertSQL() throws SQLException {

		sql = "INSERT INTO USERDATA VALUES(";
		sql += "'" + idt.getText().toString() + "',";
		sql += "'" + pwt.getText().toString() + "',";
		sql += "'" + namet.getText().toString() + "',";
		sql += "'" + phonet.getText().toString() + "',";
		sql += "'" + datet.getText().toString() + "',";
		sql += "'','',";
		sql += "'" + gen.getSelectedItem().toString() + "')";

		pstmt.executeUpdate(sql);
		System.out.println(sql);
		dispose();
		JOptionPane.showMessageDialog(this, namet.getText().toString() + "님\n 가입을 환영합니다.");
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new JoinMember();
	}
}