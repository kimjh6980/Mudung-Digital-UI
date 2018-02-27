import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class RHistoryUI extends JPanel {

	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@KJH-GE63VR7RE:1521:xe";
	private Connection con = null;
	private Statement pstmt = null;
	private ResultSet rs = null;

	public int setbook = 0;

	String header[] = { "아이디", "책번호", "책이름", "대여일", "반납예정일", "연장여부" };

	private DefaultTableModel model = new DefaultTableModel(header, 0) {
		public boolean isCellEditable(int rowIndex, int mColIndex) {
			return false;
		}
	};

	public RHistoryUI() {

		Container contentPane;
		
		JTable table = new JTable(model);
		JScrollPane scrollpane = new JScrollPane(table);
		scrollpane.setSize(1500, 700);
		try {
			Class.forName(driver);
			try {
				con = DriverManager.getConnection(url, "MUDL", "1234");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				pstmt = con.createStatement();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		add(scrollpane);
		setVisible(true);
		settable(model);
	}

	private void settable(DefaultTableModel dt) {
		String query = "select USERNAME, BOOKNUM, BOOKTITLE, RENTDAY, RETURNDAY, EXTENSIONDAY from RHISTORY where USERNAME = \'"
				+ ReserveUI.loginuser + "\'";
		try {
			rs = pstmt.executeQuery(query);

			for (int i = 0; i < dt.getRowCount();) {
				dt.removeRow(i);
			}
			while (rs.next()) {
				model.addRow(new Object[] { rs.getString("USERNAME"), rs.getInt("BOOKNUM"), rs.getString("BOOKTITLE"),
						rs.getString("RENTDAY"), rs.getString("RETURNDAY"), rs.getString("EXTENSIONDAY") });
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
				con.close();
			} catch (Exception e) {
			}
		}
	}

}
