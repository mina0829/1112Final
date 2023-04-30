import javax.swing.JFrame;
public class Main {

	public static void main(String[] args) {
		//這邊調整成可以連資料庫的格式
		HomePage frame = new HomePage();//打開主介面
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
