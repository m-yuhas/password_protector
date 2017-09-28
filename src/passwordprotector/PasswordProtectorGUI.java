package passwordprotector;

import java.awt.GridLayout;

import javax.swing.JFrame;

public class PasswordProtectorGUI {
	private JFrame mainFrame;
	
	public PasswordProtectorGUI() {
		this.mainFrame = new JFrame("Password Protector Beta");
		this.mainFrame.setSize(400,400);
		this.mainFrame.setLayout(new GridLayout(3,1));
		
		this.mainFrame.setVisible(true);
	}
}
