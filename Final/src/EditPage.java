import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.time.*;
import java.util.ArrayList;
//內容都是先上傳到資料庫，再透過資料庫讀到HomePage中顯示！

public class EditPage extends JFrame{
	private static final int FRAME_WIDTH = 800;
	private static final int FRAME_HEIGHT = 600;
	private static final int TEXTCOMP_WIDTH = 500;
	
	private JPanel addItemP, topLeftP, topP, tagP, centerP, bottomP, overallP;
	private JLabel titleL, locationL, addItemL, noteL, tagL, pictureL, whiteL;
	private JLabel itemNameL1, itemNameL2, itemNameL3, itemNameL4, itemQL;
	private JTextField titleF, locationF;
	private JTextField itemNameF1, itemNameF2, itemNameF3, itemNameF4, itemQF1, itemQF2, itemQF3, itemQF4;
	private JTextField tagF1, tagF2, tagF3, tagF4, tagF5;
	private JTextArea noteArea;
	private JButton updateBtn, deleteBtn;
	private String postTime;
	private ArrayList<Item>items;
	private ArrayList<String>tags;
	
	public EditPage() {
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
		pictureL = new JLabel(new imageIcon());//給使用者上傳
		whiteL = new JLabel(" ");//調整排版用
	}
	
	public void creatTextField() {
		titleF = new JTextField(TEXTCOMP_WIDTH);
		locationF = new JTextField(TEXTCOMP_WIDTH);
		
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
		updateBtn = new JButton("更新編輯");
		updateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//將現有資料上傳到資料庫
			}
		});
		
		deleteBtn = new JButton("刪除貼文");
		deleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//將這篇貼文的資料刪除
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
		
		topLeftP = new JPanel(new GridLayout(4, 2));
		topLeftP.add(titleL);
		topLeftP.add(titleF);
		topLeftP.add(locationL);
		topLeftP.add(locationF);
		topLeftP.add(addItemL);
		topLeftP.add(whiteL);
		topLeftP.add(addItemP);
		topLeftP.add(whiteL);
		
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
