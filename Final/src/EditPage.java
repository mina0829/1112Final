import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.*;
import java.io.File;
import java.util.Date;
import java.util.ArrayList;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
//內容都是先上傳到資料庫，再透過資料庫讀到HomePage中顯示！

public class EditPage extends JFrame{
	private static final int FRAME_WIDTH = 800;
	private static final int FRAME_HEIGHT = 420;
	
	private JPanel addItemP1, addItemP2, addItemP3, addItemP4, topP, tagP, centerP, bottomP, overallP, itemsP;
	private JLabel titleL, locationL, addItemL, noteL, tagL, timeL, whiteL;
	private JLabel itemNameL1, itemNameL2, itemNameL3, itemNameL4, itemQL1, itemQL2, itemQL3, itemQL4;
	private JTextField titleF, locationF, timeF;
	private JTextField itemNameF1, itemNameF2, itemNameF3, itemNameF4, itemQF1, itemQF2, itemQF3, itemQF4;
	private JTextField tagF1, tagF2, tagF3, tagF4, tagF5;
	private JTextArea noteArea;
	private JButton pictureBtn, updateBtn, deleteBtn;
	
	private String postTime;
	private Border border;
	private boolean sucess;
	private int i = 0;
	private ArrayList<Item>items;
	private ArrayList<String>tags;
	private Date date = new Date();
	private SimpleDateFormat ft = new SimpleDateFormat ("MM-dd HH:mm");
	private Calendar calendar = Calendar.getInstance();
	Connection conn;
	Statement stat;
	
	public EditPage (Connection conn) throws SQLException{
		this.conn = conn;
		stat = conn.createStatement();
		
		this.setTitle("編輯貼文");
		this.setSize(this.FRAME_WIDTH, this.FRAME_HEIGHT);
		
		items = new ArrayList<Item>();
		tags = new ArrayList<String>();
		
		creatLabel();
		creatTextField();
		creatTextArea();
		creatButton();
		creatPanel();
	}
	
	public void creatLabel() {
		titleL = new JLabel("標題：");
		locationL = new JLabel("所在地：");
		
		addItemL = new JLabel("新增品項：");
		itemNameL1 = new JLabel("品項一：");
		itemNameL2 = new JLabel("品項二：");
		itemNameL3 = new JLabel("品項三：");
		itemNameL4 = new JLabel("品項四：");
		itemQL1 = new JLabel("，數量：");
		itemQL2 = new JLabel("，數量：");
		itemQL3 = new JLabel("，數量：");
		itemQL4 = new JLabel("，數量：");
		
		noteL = new JLabel("新增備註（三十字為限）：");
		tagL = new JLabel("新增標籤（五個為限）：");
		timeL = new JLabel("剩餘時間（分鐘）：");
		whiteL = new JLabel(" ");
	}
	
	public void creatTextField() {
		titleF = new JTextField(20);
		locationF = new JTextField(10);
		timeF = new JTextField(10);
		
		itemNameF1 = new JTextField(null, 10);
		itemNameF2 = new JTextField(null, 10);
		itemNameF3 = new JTextField(null, 10);
		itemNameF4 = new JTextField(null, 10);
		itemQF1 = new JTextField("0", 5);
		itemQF2 = new JTextField("0", 5);
		itemQF3 = new JTextField("0", 5);
		itemQF4 = new JTextField("0", 5);
		
		Item item1 = new Item(itemNameF1.getText(), Integer.valueOf(itemQF1.getText()));
		Item item2 = new Item(itemNameF2.getText(), Integer.valueOf(itemQF2.getText()));
		Item item3 = new Item(itemNameF3.getText(), Integer.valueOf(itemQF3.getText()));
		Item item4 = new Item(itemNameF4.getText(), Integer.valueOf(itemQF4.getText()));
		
		items.add(item1);
		items.add(item2);
		items.add(item3);
		items.add(item4);
		
		
		tagF1 = new JTextField("", 10);
		tagF2 = new JTextField("", 10);
		tagF3 = new JTextField("", 10);
		tagF4 = new JTextField("", 10);
		tagF5 = new JTextField("", 10);
		
		tags.add(tagF1.getText());
		tags.add(tagF2.getText());
		tags.add(tagF3.getText());
		tags.add(tagF4.getText());
		tags.add(tagF5.getText());
	}
	
	public void creatTextArea() {
		border = BorderFactory.createLineBorder(Color.GRAY, 1);
		noteArea = new JTextArea(2, 40);
		noteArea.setBorder(border);
	}
	
	public void creatButton() {
		updateBtn = new JButton("更新編輯");
		updateBtn.setForeground(Color.WHITE);
		updateBtn.setBackground(new Color(70, 130, 180));
		updateBtn.setBorderPainted(false); 
		
		updateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//將現有資料上傳到資料庫
				try {
					if(i == 0) {
						String query = "INSERT INTO `posts`(title, location, itemName1, itemQ1, itemName2, itemQ2, itemName3, itemQ3, itemName4, itemQ4, note, tag1, tag2, tag3, tag4, tag5, leftTime, endTime, state)"
								+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
						PreparedStatement stat = conn.prepareStatement(query);
					
						stat.setString(1, titleF.getText());
						stat.setString(2, locationF.getText());
						stat.setString(3, itemNameF1.getText());
						stat.setInt(4, Integer.valueOf(itemQF1.getText()));
						stat.setString(5, itemNameF2.getText());
						stat.setInt(6, Integer.valueOf(itemQF2.getText()));
						stat.setString(7, itemNameF3.getText());
						stat.setInt(8, Integer.valueOf(itemQF3.getText()));
						stat.setString(9, itemNameF4.getText());
						stat.setInt(10, Integer.valueOf(itemQF4.getText()));
						stat.setString(11, noteArea.getText());
						stat.setString(12, tagF1.getText());
						stat.setString(13, tagF2.getText());
						stat.setString(14, tagF3.getText());
						stat.setString(15, tagF4.getText());
						stat.setString(16, tagF5.getText());
						stat.setString(17, ft.format(date));
						stat.setInt(19, 1);
					
						calendar.setTime(date);
						calendar.add(Calendar.MINUTE, Integer.valueOf(timeF.getText()));
						Date endDate = calendar.getTime();
						stat.setString(18, ft.format(endDate));
					
						sucess = stat.execute();
						i++;
					}else{
						String query = "UPDATE `posts` SET  itemName1 = ?, itemQ1 = ?, itemName2 = ?, itemQ2 = ?, itemName3 = ?, itemQ3 = ?, itemName4 = ?, itemQ4 = ?"
								+ "WHERE title = ?;";
						PreparedStatement stat = conn.prepareStatement(query);
					
						stat.setString(1, itemNameF1.getText());
						stat.setString(2, itemQF1.getText());
						stat.setString(3, itemNameF2.getText());
						stat.setString(4, itemQF2.getText());
						stat.setString(5, itemNameF3.getText());
						stat.setString(6, itemQF3.getText());
						stat.setString(7, itemNameF4.getText());
						stat.setString(8, itemQF4.getText());
						stat.setString(9, titleF.getText());
					
						sucess = stat.execute();
					}
					
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
		});
		
		deleteBtn = new JButton("刪除貼文");
		deleteBtn.setForeground(Color.WHITE);
		deleteBtn.setBackground(new Color(178, 34, 34));
		deleteBtn.setBorderPainted(false);
		
		deleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					String query = "UPDATE `posts` SET state = '0' WHERE `title` = '" + titleF.getText() + "';";
					sucess = stat.execute(query);
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void creatPanel() {
		addItemP1 = new JPanel(new GridLayout(1, 4));
		addItemP1.add(itemNameL1);
		addItemP1.add(itemNameF1);
		addItemP1.add(itemQL1);
		addItemP1.add(itemQF1);
		addItemP1.setOpaque(false);
		
		addItemP2 = new JPanel(new GridLayout(1, 4));
		addItemP2.add(itemNameL2);
		addItemP2.add(itemNameF2);
		addItemP2.add(itemQL2);
		addItemP2.add(itemQF2);
		addItemP2.setOpaque(false);
		
		addItemP3 = new JPanel(new GridLayout(1, 4));
		addItemP3.add(itemNameL3);
		addItemP3.add(itemNameF3);
		addItemP3.add(itemQL3);
		addItemP3.add(itemQF3);
		addItemP3.setOpaque(false);
		
		addItemP4 = new JPanel(new GridLayout(1, 4));
		addItemP4.add(itemNameL4);
		addItemP4.add(itemNameF4);
		addItemP4.add(itemQL4);
		addItemP4.add(itemQF4);
		addItemP4.setOpaque(false);
		
		itemsP = new JPanel(new GridLayout(5, 1));
		itemsP.add(addItemL);
		itemsP.add(addItemP1);
		itemsP.add(addItemP2);
		itemsP.add(addItemP3);
		itemsP.add(addItemP4);
		itemsP.setOpaque(false);
		
		topP = new JPanel(new GridLayout(3, 2));
		topP.add(titleL);
		topP.add(titleF);
		topP.add(locationL);
		topP.add(locationF);
		topP.add(timeL);
		topP.add(timeF);
		topP.setOpaque(false);
		
		tagP = new JPanel(new GridLayout(1, 5));
		tagP.add(tagF1);
		tagP.add(tagF2);
		tagP.add(tagF3);
		tagP.add(tagF4);
		tagP.add(tagF5);
		tagP.setOpaque(false);
		
		centerP = new JPanel(new GridLayout(4, 1));
		centerP.add(noteL);
		centerP.add(noteArea);
		centerP.add(tagL);
		centerP.add(tagP);
		centerP.setOpaque(false);
		
		bottomP = new JPanel();
		bottomP.add(updateBtn);
		bottomP.add(deleteBtn);
		bottomP.setOpaque(false);
		
		overallP = new JPanel();
		overallP.add(topP);
		overallP.add(itemsP);
		overallP.add(centerP);
		overallP.add(whiteL);
		overallP.add(bottomP);
		overallP.setOpaque(false);
		
		this.add(overallP);
		this.getContentPane().setBackground(new Color(255, 255, 240));
	}
}

