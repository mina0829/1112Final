import java.util.ArrayList;

public class Item {
	private String name;
	private int quantity;
	
	public Item(String name, int quantity) {
		this.name = name;
		this.quantity = quantity;
	}
	
	public String getName() {
		return name;
	}
	
	public int getQ() {
		return quantity;
	}
}
