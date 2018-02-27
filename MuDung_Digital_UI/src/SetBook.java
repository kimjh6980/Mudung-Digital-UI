import java.awt.Button;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;


public class SetBook extends JFrame {

	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@KJH-GE63VR7RE:1521:xe";
	private Connection con = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	public int setbook = 0;

	private String select;
	private String query;
	
	JLabel csi = new JLabel("분야검색");
	JLabel nsi = new JLabel("이름검색");
	JTextField searchbook = new JTextField();
	JButton nsb = new JButton("검색");
	
	String header[] = {"청구번호","장르","타이틀명","관람등급", "대여가능"};
	
	private DefaultTableModel model = new DefaultTableModel(header, 0)	{
		public boolean isCellEditable(int rowIndex, int mColIndex) {
			return false;
		}
	};
	
	int num;
	String genre;
	String title;
	String age;
	
	Container contentPane;
	String[] cate = {"서적선택","-----------------------------","스릴러","액션","드라마","공포","다큐멘터리","어린이","가족","SF","교육","코미디","뮤지컬","어드밴처","스포츠","전쟁","범죄","로맨스","무협","서부극","KBS역사 스페셜","내셔널지오그래픽","사이언스21","미스터리"};
	
	JTable table = new JTable(model);
	JScrollPane scrollpane = new JScrollPane(table);
	
	SetBook()	{
		setTitle("서적 선택하기");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = getContentPane();
		contentPane.setLayout(null);
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.addMouseListener(new MyMouseListener());
		
		JComboBox strCombo = new JComboBox(cate);
		strCombo.setBounds(150, 45, 330, 30);
		
		csi.setBounds(42, 43, 70, 30);
		nsi.setBounds(42, 10, 70, 30);
		
		searchbook.setBounds(150, 10, 250, 30);
		scrollpane.setBounds(5, 90, 470, 400);
		
		nsb.setBounds(405, 10, 73, 30);
		
		strCombo.addActionListener(new ActionListener()	{
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox)e.getSource();
				
				select = (String) cb.getSelectedItem();

				settable(model, select, false);
				//System.out.println(select);
				// 선택된거 찾았으니깐 리스트 띄워야지
			}
		});
		contentPane.add(strCombo);
		contentPane.add(scrollpane);
		contentPane.add(csi);
		contentPane.add(nsi);
		contentPane.add(searchbook);
		contentPane.add(nsb);
		setSize(500,550);
		setVisible(true);
		
		nsb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				select = searchbook.getText().toString().trim();
				settable(model, select , true);
			}
			
		});
	}
	
	private void settable(DefaultTableModel dt, String select, Boolean nm) {
		if(nm == false)	{
			if(select.toString().equals("서적선택"))	{
				query = "select 청구번호, 장르, 타이틀명, 관람등급 , 대여가능 from BOOKINFO order by 청구번호";
			}	else	{
				query = "select 청구번호, 장르, 타이틀명, 관람등급 , 대여가능 from BOOKINFO where 장르 LIKE '%" +select + "%' order by 청구번호";
			}
		}	else	{
			query = "select 청구번호, 장르, 타이틀명, 관람등급 , 대여가능 from BOOKINFO where 타이틀명 LIKE '%" +select + "%' order by 청구번호";
		}
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, "MUDL", "1234");
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			
			for(int i=0; i<dt.getRowCount();) {
				dt.removeRow(i);
			}
			
			while(rs.next())	//끝날떄까지 돌리기
			{
				model.addRow(new Object[] {rs.getInt("청구번호"),rs.getString("장르"), rs.getString("타이틀명"),rs.getString("관람등급"),rs.getString("대여가능")});
			}
		}	catch(Exception e) {
			System.out.println(e.getMessage());
		}	finally	{
			try	{
				rs.close();
				pstmt.close();
				con.close();
			}	catch (Exception e2) {}
		}
	}

	private class MyMouseListener extends MouseAdapter	{
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2)	{	//더블클릭
				int row = table.getSelectedRow();
				
				if(table.getValueAt(row, 4) != null)	{
					JOptionPane.showMessageDialog(null, "해당 서적은 이미 대여중입니다.");	}
					//NoDialog();
				else	{
					String setting = "서적번호 : " + table.getValueAt(row, 0) + "\n장르 : " + table.getValueAt(row, 1) + "\n타이틀명 : " + table.getValueAt(row, 2) + "\n시청연령 : " + table.getValueAt(row, 3);
					int result = JOptionPane.showConfirmDialog(null,  setting + "\n 선택사항이 맞습니까?", "선택 서적 확인", JOptionPane.OK_CANCEL_OPTION);

					if(result == 0)// ok = 0, cancel = 2
					{
						ReserveUI.setbooknum = (int) table.getValueAt(row, 0);
						System.out.println(ReserveUI.setbooknum + "OK");
						try {
							ReserveUI.settingBook(ReserveUI.setbooknum);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						dispose();
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		new SetBook();
	   	}
}
