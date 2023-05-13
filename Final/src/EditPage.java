import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.time.*;
import java.io.File;
import java.util.ArrayList;
import java.sql.*;
//內容都是先上傳到資料庫，再透過資料庫讀到HomePage中顯示！

public class EditPage extends JFrame{
	private static final int FRAME_WIDTH = 800;
	private static final int FRAME_HEIGHT = 600;
	private static final int TEXTCOMP_WIDTH = 500;
	
	private JPanel addItemP, topLeftP, pictureP, topP, tagP, centerP, bottomP, overallP;
	private JLabel titleL, locationL, addItemL, noteL, tagL, pictureL, timeL, whiteL;
	private JLabel itemNameL1, itemNameL2, itemNameL3, itemNameL4, itemQL;
	private JTextField titleF, locationF timeF;
	private JTextField itemNameF1, itemNameF2, itemNameF3, itemNameF4, itemQF1, itemQF2, itemQF3, itemQF4;
	private JTextField tagF1, tagF2, tagF3, tagF4, tagF5;
	private JTextArea noteArea;
	private JButton pictureBtn, updateBtn, deleteBtn;
	
	private String postTime;
	private boolean sucess;
	private ArrayList<Item>items;
	private ArrayList<String>tags;
	private LocalDateTime currentPoint = LocalDateTime.now();
	Connection conn;
	Statement stat;
	
	public EditPage (Connection conn) throws SQLException {
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
		itemQL = new JLabel("，數量：");
		
		noteL = new JLabel("新增備註（三十字為限）：");
		tagL = new JLabel("新增標籤（五個為限）：");
		pictureL = new JLabel("上傳圖片");//給使用者上傳 可參考：https://blog.csdn.net/u010159842/article/details/52574233
		timeL = new JLabel("剩餘時間（分鐘）：");
		whiteL = new JLabel(" ");//調整排版用
	}
	
	public void creatTextField() {
		titleF = new JTextField(TEXTCOMP_WIDTH);
		locationF = new JTextField(TEXTCOMP_WIDTH);
		timeF = new JTextField(TEXTCOMP_WIDTH);
		
		itemNameF1 = new JTextField("", 300);
		itemNameF2 = new JTextField("", 300);
		itemNameF3 = new JTextField("", 300);
		itemNameF4 = new JTextField("", 300);
		itemQF1 = new JTextField("0", 100);
		itemQF2 = new JTextField("0", 100);
		itemQF3 = new JTextField("0", 100);
		itemQF4 = new JTextField("0", 100);
		
		Item item1 = new Item(itemNameF1.getText(), Integer.valueOf(itemQF1.getText()));
		Item item2 = new Item(itemNameF2.getText(), Integer.valueOf(itemQF2.getText()));
		Item item3 = new Item(itemNameF3.getText(), Integer.valueOf(itemQF3.getText()));
		Item item4 = new Item(itemNameF4.getText(), Integer.valueOf(itemQF4.getText()));
		
		items.add(item1);
		items.add(item2);
		items.add(item3);
		items.add(item4);
		
		for(Item item: items) {
			if(item.getQ() == 0) {
				items.remove(item);
			}
		}//移除沒有數量的商品
		
		tagF1 = new JTextField("", 300);
		tagF2 = new JTextField("", 300);
		tagF3 = new JTextField("", 300);
		tagF4 = new JTextField("", 300);
		tagF5 = new JTextField("", 300);
		
		tags.add(tagF1.getText());
		tags.add(tagF2.getText());
		tags.add(tagF3.getText());
		tags.add(tagF4.getText());
		tags.add(tagF5.getText());
		
		for(String tag: tags) {
			if(tag.equals("")) {
				tags.remove(tag);
			}
		}//移除空白的tag
	}
	
	public void creatTextArea() {
		noteArea = new JTextArea(2, TEXTCOMP_WIDTH);
	}
	
	public void creatButton() {
		pictureBtn = new JButton("上傳圖片");
		pictureBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                    public boolean accept(File file) {
                        return file.getName().toLowerCase().endsWith(".jpg")
                                || file.getName().toLowerCase().endsWith(".jpeg")
                                || file.getName().toLowerCase().endsWith(".png")
                                || file.isDirectory();
                    }

                    public String getDescription() {
                        return "圖片文件 (*.jpg, *.jpeg, *.png)";
                    }
                });

                int result = fileChooser.showOpenDialog(new JFrame());
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    ImageIcon icon = new ImageIcon(selectedFile.getPath());
                    pictureL.setIcon(icon);;
                }
            }
        });
		
		updateBtn = new JButton("更新編輯");
		updateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//將現有資料上傳到資料庫
				String columns = "`title`, `location`";
				for(int i = 0; i < items.size(); i++) {
					columns += ", `itemName`" + i+1 + ", `itemQ`" + i+1;
				}
				columns += ", `note`";
				for(int i = 0; i< tags.size(); i++) {
					columns += ", `tag`" + i+1;
				}
				columns += ", `picture`, `postTime`";
				
				String values = titleF.getText() + locationF.getText();
				for(Item item: items) {
					values += ", " + item.getName() + ", " + item.getQ();
				}
				columns += noteArea.getText();
				for(String tag:tags) {
					values += ", " + tag;
				}
				values += ", " + pictureL.getsomething:) + ", " + currentPoint;//圖片上傳到資料庫，我也不知道怎麼寫，請隨意修改
				//組合上傳資料庫的指令
				try {
					String query = "INSERT INTO `posts`(" + columns + ")VALUES(" + values + ");";
					sucess = stat.execute(query);
				}catch(SQLException e) {
					e.printStackTrace();
				}
				//用使用者輸入的分鐘數，用SQL的select curtime()指令新增目前時間&add()加使用者輸入的分鐘數，算出endTime
			}
		});
		
		deleteBtn = new JButton("刪除貼文");
		deleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//將這篇貼文的資料刪除
				try {
					String query = "DELETE FROM `posts` WHERE `title` = " + titleF.getText() + ";";
					sucess = stat.execute(query);
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void creatPanel() {
		addItemP = new JPanel(new GridLayout(4, 4));
		addItemP.add(itemNameL1);
		addItemP.add(itemNameF1);
		addItemP.add(itemQL);
		addItemP.add(itemQF1);
		
		addItemP.add(itemNameL2);
		addItemP.add(itemNameF2);
		addItemP.add(itemQL);
		addItemP.add(itemQF2);
		
		addItemP.add(itemNameL3);
		addItemP.add(itemNameF3);
		addItemP.add(itemQL);
		addItemP.add(itemQF3);
		
		addItemP.add(itemNameL4);
		addItemP.add(itemNameF4);
		addItemP.add(itemQL);
		addItemP.add(itemQF4);
		
		topLeftP = new JPanel(new GridLayout(5, 2));
		topLeftP.add(titleL);
		topLeftP.add(titleF);
		topLeftP.add(locationL);
		topLeftP.add(locationF);
		topLeftP.add(addItemL);
		topLeftP.add(whiteL);
		topLeftP.add(addItemP);
		topLeftP.add(whiteL);
		topLeftP.add(timeL);
		topLeftP.add(timeF);
		
		topP = new JPanel(new GridLayout(1, 2));
		topP.add(topLeftP);
		topP.add(pictureL);
		
		tagP = new JPanel(new GridLayout(1, 5));
		tagP.add(tagF1);
		tagP.add(tagF2);
		tagP.add(tagF3);
		tagP.add(tagF4);
		tagP.add(tagF5);
		
		centerP = new JPanel(new GridLayout(4, 1));
		centerP.add(noteL);
		centerP.add(noteArea);
		centerP.add(tagL);
		centerP.add(tagP);
		
		bottomP = new JPanel(new GridLayout(1, 2));
		bottomP.add(updateBtn);
		bottomP.add(deleteBtn);
		
		overallP = new JPanel(new GridLayout(3, 1));
		overallP.add(topP);
		overallP.add(centerP);
		overallP.add(bottomP);
		this.add(overallP);
	}
	
	public String getPostTime() {
		return postTime;//時間功能，還沒寫
	}
}
