import java.awt.Component;
import java.awt.Frame;
import java.awt.TextField;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class USERDUI extends JPanel {
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@KJH-GE63VR7RE:1521:xe";
	private Connection con = null;
	private Statement pstmt = null;
	private ResultSet rs = null;

	TextField idt = new TextField();
	TextField pwt = new TextField();
	TextField namet = new TextField();
	TextField pnt = new TextField();
	TextField datet = new TextField();
	TextField gendert = new TextField();
	
	String pnt2 = null;
	
	JButton OKB = new JButton("변경");
	JButton CLB = new JButton("취소");
	JButton DEL = new JButton("계정 삭제");
	
	String confirm1pw = null;
	String confirm2pw = null;
	
	public USERDUI()	{
		setLayout(null);
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, "MUDL", "1234");
			pstmt = con.createStatement();
			String sql = "Select USERID, USERNAME, USERPN, USERDATE, USERGENDER FROM USERDATA WHERE USERID = \'" + ReserveUI.loginuser + "\'";
			rs = pstmt.executeQuery(sql);
			
			Labelset();
			TFset();
			Bset();
			setVisible(true);
			
			while(rs.next()) {
				idt.setText(rs.getString("USERID"));
				namet.setText(rs.getString("USERNAME"));
				pnt2 = rs.getString("USERPN");
				pnt.setText(pnt2);
				datet.setText(rs.getString("USERDATE"));
				gendert.setText(rs.getString("USERGENDER"));
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		idt.setEnabled(false);
		namet.setEnabled(false);
		datet.setEnabled(false);
		gendert.setEnabled(false);
	}
	
	private void Labelset()	{
		JLabel idl = new JLabel("아이디");
		JLabel pwl = new JLabel("비밀번호");
		JLabel namel = new JLabel("이름");
		JLabel pnl = new JLabel("전화번호");
		JLabel datel = new JLabel("가입일자");
		JLabel genderl = new JLabel("성별");
		
		idl.setBounds(100, 100, 200, 30);
		pwl.setBounds(600, 100, 200, 30);
		namel.setBounds(100, 200, 200, 30);
		pnl.setBounds(600, 200, 200, 30);
		datel.setBounds(100, 300, 200, 30);
		genderl.setBounds(600, 300, 200, 30);
		
		add(idl);
		add(pwl);
		add(namel);
		add(pnl);
		add(datel);
		add(genderl);
		
	}
	private void TFset()	{
		
		idt.setBounds(100, 130, 200, 30);
		pwt.setBounds(600, 130, 200, 30);
		namet.setBounds(100, 230, 200, 30);
		pnt.setBounds(600, 230, 200, 30);
		datet.setBounds(100, 330, 200, 30);
		gendert.setBounds(600, 330, 200, 30);
		
		add(idt);
		add(pwt);
		add(namet);
		add(pnt);
		add(datet);
		add(gendert);
		
	}
	private void Bset()	{
		OKB.setBounds(300, 400, 150, 50);
		OKB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(pwt.getText().toString().trim().length() == 0) {
					JOptionPane.showMessageDialog(null, "비밀번호를 입력해주세요");
				}	else	{
					confirm1pw = pwt.getText().toString();
					confirm2pw = JOptionPane.showInputDialog("비밀번호를 다시 한번 입력해주세요").toString();
					//confirm2pw = confirm2pw.trim().toString();
					if(pwt.getText().toString().equals(confirm2pw)) {
						JOptionPane.showMessageDialog(null, "변경되었습니다. 다시 로그인해주세요");
						updateUserSQL();
					}	else	{
						JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.");
					}
				}
			}
			
		});
		
		CLB.setBounds(150, 400, 150, 50);
		CLB.addActionListener(new ActionListener()	{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				pnt.setText(pnt2);
			}
			
		});
		
		DEL.setBounds(650, 400, 150, 50);
		DEL.addActionListener(new ActionListener()	{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int result = JOptionPane.showConfirmDialog(null,  "정말로 삭제하겠습니까?", "계정 삭제??", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				if(result == JOptionPane.YES_OPTION)	{
					String searchPW = "select USERPW from USERDATA where USERID = \'" + ReserveUI.loginuser + "\'";
					String confirmPW = null;
					try {
						rs = pstmt.executeQuery(searchPW);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					try {
						while(rs.next())	{
							confirmPW = rs.getString("USERPW");
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					confirm2pw = JOptionPane.showInputDialog("비밀번호를 다시 한번 입력해주세요").toString();
					if(confirmPW.toString().equals(confirm2pw)) {
						DeleteUserSQL();
					}	else	{	JOptionPane.showMessageDialog(null, "비밀번호를 틀리셨습니다.");	}
				}	else	{
					
				}
			}
			
		});
		
		add(OKB);
		add(CLB);
		add(DEL);
	}

	protected void DeleteUserSQL() {
		// TODO Auto-generated method stub
		String sql = "delete from USERDATA where USERID = '" + ReserveUI.loginuser + "'";
		String sql2 = "delete from RHISTORY where USERNAME = '" + ReserveUI.loginuser + "'";
		try {
			rs = pstmt.executeQuery(sql);
			rs = pstmt.executeQuery(sql2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("삭제 실패");
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "계정이 삭제되었습니다.");
		System.exit(0);
		
	}

	protected void updateUserSQL() {
		// TODO Auto-generated method stub
		String sql = "update USERDATA set USERPW = '" + confirm2pw + "' where USERID = '" + ReserveUI.loginuser + "'";
		String sql2 = "update USERDATA set USERPN = '" + pnt.getText().toString().trim() + "' where USERID = '" + ReserveUI.loginuser + "'";
		try {
			rs = pstmt.executeQuery(sql);
			rs = pstmt.executeQuery(sql2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
