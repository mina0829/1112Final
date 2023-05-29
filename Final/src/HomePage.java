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
	private JPanel buttonsP, postsP, overallP;
	private JTextField keyWordF;
	private JTextArea postsArea;
	
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
		creatTextArea();
		creatPanel();
	}
	
	public void creatButton() {
		updateButton = new JButton("更新頁面");
		addPostButton = new JButton("新增貼文");
		
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String query = "SELECT * FROM `posts` WHERE state = 1";
				try{
					sucess = stat.execute(query);
					if(sucess) {
						ResultSet result = stat.getResultSet();
						postsArea.setText(showResultSet(result));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				//這邊要修改，改成只update結果顯示區，在顯示區做時間到了就不顯示的功能
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
	
	public void creatTextArea() {
		postsArea = new JTextArea();
		postsArea.setText("");
		postsArea.setEditable(false);
	}
	
	public void creatPanel() {
		postsP = new JPanel();
		postsP.add(postsArea);
		
		overallP = new JPanel(new GridLayout(2, 1));
		overallP.add(buttonsP);
		overallP.add(postsP);
		this.add(overallP);
	}
	
	public void keywordSearch(){
		 String keyword = keyWordF.getText();
		 String[] columns = {
				 "title", "location", 
				 "itemName1", "itemName2", "itemName3",  "itemName3", "itenName4",
				 "note", "tag1", "tag2", "tag3", "tag4", "tag5"
		 };
		 for(String column : columns) {
			 String query = "SELECT * FROM posts WHERE "+column+" LIKE " + keyword;
			 try {
		            PreparedStatement statement = conn.prepareStatement(query);
		            statement.setString(1, "%" + keyword + "%");
		            ResultSet resultSet = statement.executeQuery();
	
		            // 處理搜索结果
		            while (resultSet.next()) {
		                // 處理每一行數據
		                String result = resultSet.getString(column);
		                System.out.println(result);
		            }
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
	   }
	 }
	 //設定button, textField

	public static String showResultSet(ResultSet result) throws SQLException {
		ResultSetMetaData metaData = result.getMetaData();
		int columnCount = metaData.getColumnCount();
		String output = "";
		ArrayList<String> items = new ArrayList<String>();
		ArrayList<String>tags =  new ArrayList<String>();
		
		while (result.next()) {
			String title = result.getString(1);
			String location = result.getString(2);
			
			for(int i = 1; i < 5; i++) {
				String itemName = "itemName" + (i);
				if(result.getString(itemName) != null) {
					items.add(String.format("%s，剩餘%d份", result.getString(itemName), result.getInt((i+1)*2)));
				}
			}
			
			String note = result.getString(9);
			for(int i = 1; i < 6; i++) {
				String tag = "tag" + i;
				if(result.getString(tag) != null){
					tags.add(String.format("#%s", result.getString(tag)));
				}
			}
			
			//圖片顯示
			
			String endTime = result.getString(19);
			
			output += String.format("%s\n%s．%s\n", title, endTime, location);
			
			for(String item: items) {
				output += String.format("%s\n", item);
			}
			
			output += String.format("備註：%s\n", note);
			
			for(String tag: tags) {
				output += tag;
			}
			output += "\n";
		}
		return output;
	}
}
