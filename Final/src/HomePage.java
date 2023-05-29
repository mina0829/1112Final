import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//輸入內容皆從資料庫中讀取！

public class HomePage extends JFrame{
	private static final int FRAME_WIDTH = 800;
	private static final int FRAME_HEIGHT = 600;
	
	private JLabel keyWordL;
	private JButton updateButton, addPostButton, searchButton;
	private JPanel buttonsP, postsP, overallP, keyWordP;
	private JTextField keyWordF;
	private JTextArea postsArea;
	
	private EditPage editPage;
	private Date date = new Date();
	private boolean sucess;
	Connection conn;
	Statement stat;
	
	public HomePage (Connection conn) throws SQLException{
		this.conn = conn;
		stat = conn.createStatement();
		
		this.setTitle("惜食平臺");
		this.setSize(this.FRAME_WIDTH, this.FRAME_HEIGHT);
		
		creatTextArea();
		creatButton();
		creatLabel();
		creatTextField();
		creatPanel();
	}
	
	public void creatLabel() {
		keyWordL = new JLabel("關鍵字：");
	}
	
	public void creatButton() {
		updateButton = new JButton("更新頁面");
		addPostButton = new JButton("新增貼文");
		searchButton = new JButton("搜尋");
		
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String query = "SELECT * FROM `posts` WHERE state = 1";
				try{
					sucess = stat.execute(query);
					if(sucess) {
						ResultSet result = stat.getResultSet();
						System.out.println("check");
						postsArea.repaint();
						postsArea.setText(showResultSet(result, stat));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					System.out.println("error");
				}
			}
		});
		
		addPostButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					editPage = new EditPage(conn);
					editPage.setVisible(true);
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
		});
		
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				keywordSearch();
			}
		});
	}
	
	public void creatTextField() {
		keyWordF = new JTextField(5);
	}
	
	public void creatTextArea() {
		postsArea = new JTextArea(40, 70);
		postsArea.setText("");
	}
	
	public void creatPanel() {
		buttonsP = new JPanel();
		buttonsP.add(updateButton);
		buttonsP.add(addPostButton);
		
		keyWordP = new JPanel();
		keyWordP.add(keyWordL);
		keyWordP.add(keyWordF);
		keyWordP.add(searchButton);
		
		postsP = new JPanel();
		postsP.add(postsArea);
		
		overallP = new JPanel();
		overallP.add(buttonsP);
		overallP.add(keyWordP);
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
	
		            while (resultSet.next()) {
		               postsArea.setText(showResultSet(resultSet, stat));
		            }
		    
		       } catch (SQLException e) {
		            e.printStackTrace();
		       } catch (ParseException e) {
					System.out.println("error");
		       }
	   }
	 }
	 //設定button, textField
	
	public boolean checkTime() { 
		boolean finish = false;
		 String query = "SELECT * FROM posts WHERE endTime";
		return finish;
	}

	public static String showResultSet(ResultSet result, Statement stat) throws SQLException, ParseException{
		ResultSetMetaData metaData = result.getMetaData();
		int columnCount = metaData.getColumnCount();
		String output = "";
		ArrayList<String> items = new ArrayList<String>();
		ArrayList<String>tags =  new ArrayList<String>();
		
		while (result.next()) {
			String title = result.getString(1);
			String location = result.getString(2);
			
			for(int i = 1; i < 5; i++) {
				if(result.getInt((i+1)*2) != 0) {
						items.add(String.format("%s，剩餘%d份", result.getString((i*2)+1), result.getInt((i+1)*2)));
				}
			}//數字為0時不顯示
			
			String note = result.getString(11);
			
			for(int i = 1; i < 6; i++) {
				tags.add(result.getString(i+11) + " ");
			}
			
			String endTime = result.getString(18);
			
			/*Calendar calendar = Calendar.getInstance();
			Date date = new Date();
			calendar.setTime(date);
			
			Date endDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2023-"+endTime);
			if(calendar.getTime().compareTo(endDate) > 0) {
				String query = "UPDATE `posts` SET state = '1' WHERE title = '" + title + "';";
				stat.execute(query);
			}*/
			
			output += String.format("%s\n%s．%s\n", title, endTime, location);
			
			for(String item: items) {
				output += String.format("%s\n", item);
			}
			
			output += String.format("備註：%s\n", note);
			
			output += "#標籤：";
			for(String tag: tags) {
				output += tag;
			}
			output +="\n" + "-".repeat(40) + "\n";
			
			items.clear();
			tags.clear();
		}
		return output;
	}
}
