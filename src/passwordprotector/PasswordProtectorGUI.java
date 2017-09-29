package passwordprotector;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;

public class PasswordProtectorGUI {
	private JFrame mainFrame;
	
	public PasswordProtectorGUI() {
		this.mainFrame = new JFrame("Password Protector Beta");
		this.mainFrame.setSize(400,400);
		this.mainFrame.setLayout(new GridLayout(3,1));
		
		JButton openFileButton = new JButton("Open");
		JButton newButton = new JButton("New");
		JButton generateRandomStringButton = new JButton("Generate Random String");
		
		mainFrame.add(openFileButton);
		mainFrame.add(newButton);
		mainFrame.add(generateRandomStringButton);
		
		
		
		this.mainFrame.setVisible(true);
	}
}
