import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.sql.*;
//輸入內容皆從資料庫中讀取！

public class HomePage extends JFrame{
	private static final int FRAME_WIDTH = 800;
	private static final int FRAME_HEIGHT = 600;
	private static final int TEXTCOMP_WIDTH = 500;
	
	private JButton updateButton, addPostButton;
	private JPanel buttonsP, timeAndLocationP, pictureP, postContentP, wholePostP, overallP;
	private JLabel noteLabel, pictureLabel;
	private JTextField titleF, leftTimeF, locationF, tagF;
	private JTextArea itemArea, noteArea;
	
	private EditPage editPage;
	private ArrayList<JPanel>panels;
	private boolean sucess;
	Connection conn;
	Statement stat;
	
	public HomePage (Connection conn) throws SQLException{
		this.conn = conn;
		stat = conn.createStatement();
		
		this.setTitle("惜食平臺");
		this.setSize(this.FRAME_WIDTH, this.FRAME_HEIGHT);
		
		creatButton();
		creatJLabel();
		creatTextField();
		creatTextArea();
		creatPanel();
	}
	
	public void creatButton() {
		updateButton = new JButton("更新頁面");
		addPostButton = new JButton("新增貼文");
		
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				creatPanel();
			}
		});
		
		addPostButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					editPage = new EditPage(conn);
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void creatJLabel() {
		noteLabel = new JLabel("備註：");
		pictureLabel = new JLabel(new imageIcon());//從資料庫中讀取圖片
	}
	
	public void creatTextField() {//從資料庫中讀取文字內容
		titleF = new JTextField(TEXTCOMP_WIDTH);
		titleF.setText("");
		titleF.setEditable(false);
		
		leftTimeF = new JTextField();
		leftTimeF.setText("");
		leftTimeF.setEditable(false);
		
		locationF = new JTextField();
		locationF.setText("");
		locationF.setEditable(false);
		
		tagF = new JTextField(TEXTCOMP_WIDTH);
		tagF.setText("");
		tagF.setEditable(false);
	}
	
	public void creatTextArea() {//從資料庫中讀取文字內容
		itemArea = new JTextArea(5, TEXTCOMP_WIDTH);
		itemArea.setText("");
		itemArea.setEditable(false);
		
		noteArea = new JTextArea(2, TEXTCOMP_WIDTH);
		noteArea.setText("");
		noteArea.setEditable(false);
	}
	
	public void creatPanel() {
		buttonsP = new JPanel();
		buttonsP.add(updateButton);
		buttonsP.add(addPostButton);
		
		timeAndLocationP = new JPanel();
		timeAndLocationP.add(leftTimeF);
		timeAndLocationP.add(locationF);
		
		pictureP = new JPanel();
		pictureP.add(pictureLabel);
		
		postContentP = new JPanel();
		postContentP.add(titleF);
		postContentP.add(timeAndLocationP);
		postContentP.add(itemArea);
		postContentP.add(noteLabel);
		postContentP.add(noteArea);
		postContentP.add(tagF);
		
		wholePostP = new JPanel(new GridLayout(1, 2));
		wholePostP.add(postContentP);
		wholePostP.add(pictureP);
		panels.add(wholePostP);
		
		overallP = new JPanel(new GridLayout(panels.size()+1, 1));
		overallP.add(buttonsP);
		for(JPanel p: panels) {
			overallP.add(p);
		}
		this.add(overallP);
	}
	
	public static String showResultSet(ResultSet result) throws SQLException {
		ResultSetMetaData metaData = result.getMetaData();
		int columnCount = metaData.getColumnCount();
		String output = "";
		for (int i = 1; i <= columnCount; i++) {
			output += String.format("%15s", metaData.getColumnLabel(i));
		}
		output += "\n";
		while (result.next()) {
			for (int i = 1; i <= columnCount; i++) {
				output += String.format("%15s", result.getString(i));
			}
			output += "\n";
		}
		return output;
	}//這是copy來的，要再調整
}
