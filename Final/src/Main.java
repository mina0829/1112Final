import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		String server = "jdbc:mysql://140.119.19.73:3315/";
		String database = "111306012";
		String url = server + database + "?useSSL=false";
		String username = "111306012"; 
		String password = "25cah";
	
		try {
			Connection conn = DriverManager.getConnection(url, username, password);
			System.out.println("DB Connectd");
			HomePage frame = new HomePage(conn);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
