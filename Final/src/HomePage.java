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
	private JScrollPane scrollPane;
	
	private EditPage editPage;
	private Date date = new Date();
	private boolean sucess;
	Connection conn;
	Statement stat;
	ResultSet resultSet;
	
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
		checkTime();
		updateTextArea();
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
				checkTime();
				updateTextArea();
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
	
	public void updateTextArea() {
		String query = "SELECT * FROM `posts` WHERE state = 1";
		try{
			sucess = stat.execute(query);
			if(sucess) {
				ResultSet result = stat.getResultSet();
				System.out.println("check");
				postsArea.repaint();
				postsArea.setText(showResultSet(result));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("error");
		}
	}
	
	public void creatTextField() {
		keyWordF = new JTextField(5);
	}
	
	public void creatTextArea() {
	    postsArea = new JTextArea(30, 75);
	    postsArea.setText("");

	    scrollPane = new JScrollPane(postsArea);
	    scrollPane.setPreferredSize(new Dimension(650, 450)); // Adjust the preferred size of the scroll pane

	    Font font = new Font("Microsoft JhengHei", Font.PLAIN, 10);
	    postsArea.setFont(font);

	    // Adjust the font size without affecting the JTextArea's size
	    Font currentFont = postsArea.getFont();
	    Font newFont = currentFont.deriveFont(20f); // Set font size to 20
	    postsArea.setFont(newFont);
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
	    postsP.add(scrollPane); 

	    overallP = new JPanel();
	    overallP.add(buttonsP);
	    overallP.add(keyWordP);
	    overallP.add(postsP);
	    this.add(overallP);
	}
	
	public void keywordSearch() {
	    String keyword = keyWordF.getText();
	    String[] columns = {
	        "title", "location",
	        "itemName1", "itemName2", "itemName3", "itemName4",
	        "note", "tag1", "tag2", "tag3", "tag4", "tag5"
	    };

	    StringBuilder queryBuilder = new StringBuilder();
	    queryBuilder.append("SELECT * FROM posts WHERE state = 1 AND (");
	    for (int i = 0; i < columns.length; i++) {
	        queryBuilder.append(columns[i]).append(" LIKE ?");
	        if (i < columns.length - 1) {
	            queryBuilder.append(" OR ");
	        }
	    }
	    queryBuilder.append(")");

	    String query = queryBuilder.toString();
	    try {
	        PreparedStatement statement = conn.prepareStatement(query);
	        for (int i = 1; i <= columns.length; i++) {
	            statement.setString(i, "%" + keyword + "%");
	        }

	        ResultSet resultSet = statement.executeQuery();

	        while (resultSet.next()) {
	            PreparedStatement subStatement = conn.prepareStatement(query);
		        for (int i = 1; i <= columns.length; i++) {
		        	subStatement.setString(i, "%" + keyword + "%");
		        }

		        ResultSet subResultSet = subStatement.executeQuery();
	            postsArea.repaint();
	            postsArea.setText(showResultSet(subResultSet));
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	}
	 
	
	public boolean checkTime() {
	    boolean finish = false;
	    String query = "SELECT * FROM posts WHERE state = 1";
	    try {
	        ResultSet resultSet = stat.executeQuery(query);
	        while (resultSet.next()) {
	            String title = resultSet.getString(1);
	            String endTimeString = "2023-" + resultSet.getString(18); // 從資料庫中取得 endTime 的字串形式
	            
	            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	            Date endTime = dateFormat.parse(endTimeString); // 將 endTime 字串轉換為日期物件
	            
	            Date currentDate = new Date();

	            if (currentDate.after(endTime)) {
	            	System.out.println(currentDate.toString() + " after " + endTime.toString());
	                String updateQuery = "UPDATE posts SET state = '0' WHERE title = ?";
	                PreparedStatement updateStatement = conn.prepareStatement(updateQuery);
	                updateStatement.setString(1, title);
	                updateStatement.executeUpdate();
	            }
	        }
	        finish = true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	    return finish;
	}

	public static String showResultSet(ResultSet result) throws SQLException, ParseException {
	    ResultSetMetaData metaData = result.getMetaData();
	    StringBuilder output = new StringBuilder();
	    ArrayList<String> items = new ArrayList<String>();
	    ArrayList<String> tags = new ArrayList<String>();

	    while (result.next()) {
	        String title = result.getString(1);
	        String location = result.getString(2);
	        
	        for (int i = 1; i < 5; i++) {
	            if (result.getInt((i + 1) * 2) != 0) {
	                items.add(String.format("%s，剩餘%d份", result.getString((i * 2) + 1), result.getInt((i + 1) * 2)));
	            }
	        } //數字為0時不顯示

	        String note = result.getString(11);

	        for (int i = 1; i < 6; i++) {
	            tags.add(result.getString(i + 11) + " ");
	        }

	        String endTime = result.getString(18);

	        output.append(String.format("%s\n%s．%s\n", title, endTime, location));

	        for (String item : items) {
	            output.append(String.format("%s\n", item));
	        }

	        output.append(String.format("備註：%s\n", note));

	        output.append("#標籤：");
	        for (String tag : tags) {
	            output.append(tag);
	        }
	        output.append("\n").append("-".repeat(40)).append("\n");

	        items.clear();
	        tags.clear();
	    }
	    return output.toString();
	}
}
