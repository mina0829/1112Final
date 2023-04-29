import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
//輸入內容皆從資料庫中讀取！

public class HomePage extends JFrame{
	private static final int FRAME_WIDTH = 800;
	private static final int FRAME_HEIGHT = 600;
	private static final int TEXTCOMP_WIDTH = 500;
	
	private JButton addPostButton;
	private JPanel titleAndConP, timeAndLocationP, pictureP, postContentP, wholePostP, overallP;
	private JLabel noteLabel, conLabel, pictureLabel;
	private JTextField titleF, postTimeF, locationF, tagF;
	private JTextArea itemArea, noteArea;
	private ArrayList<JPanel>panels;
	
	public HomePage() {
		this.setTitle("惜食平臺");
		this.setSize(this.FRAME_WIDTH, this.FRAME_HEIGHT);
		
		creatButton();
		creatJLabel();
		creatTextField();
		creatTextArea();
		creatPanel();
	}
	
	public void creatButton() {
		addPostButton = new JButton("新增貼文");
		addPostButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//跳到編輯視窗
			}
		});
	}
	
	public void creatJLabel() {
		String condition = new String();
		//判斷是否還在繼續發送，是的話顯示發送中的標籤，否則顯示發送結束標籤
		noteLabel = new JLabel("備註：");
		conLabel = new JLabel(condition);
		pictureLabel = new JLabel(new imageIcon());//從資料庫中讀取圖片
	}
	
	public void creatTextField() {//從資料庫中讀取文字內容
		titleF = new JTextField(TEXTCOMP_WIDTH);
		titleF.setText("");
		titleF.setEditable(false);
		
		postTimeF = new JTextField();
		postTimeF.setText("");
		postTimeF.setEditable(false);
		
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
		titleAndConP = new JPanel();
		titleAndConP.add(titleF);
		titleAndConP.add(conLabel);
		
		timeAndLocationP = new JPanel();
		timeAndLocationP.add(postTimeF);
		timeAndLocationP.add(locationF);
		
		pictureP = new JPanel();
		pictureP.add(pictureLabel);
		
		postContentP = new JPanel();
		postContentP.add(titleAndConP);
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
		overallP.add(addPostButton);
		for(JPanel p: panels) {
			overallP.add(p);
		}
		this.add(overallP);
	}
	
	
}
