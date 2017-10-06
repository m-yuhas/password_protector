package passwordprotector;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class PasswordProtectorGUI {
	private JFrame mainFrame;
	
	public PasswordProtectorGUI() {
		this.mainFrame = new JFrame("Password Protector Beta");
		this.mainFrame.setSize(400,400);
		this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel mainPanel = new JPanel(new GridBagLayout());
		
		JButton openFileButton = new JButton("Open");
		JButton newButton = new JButton("New");
		JButton generateRandomStringButton = new JButton("Generate Random String");
		JButton saveButton = new JButton("Save");
				
		JScrollPane listPane = new JScrollPane(mainPanel);
		listPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		listPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		
		this.mainFrame.add(mainPanel);
		
		GridBagConstraints openConstraints = new GridBagConstraints();
		openConstraints.gridx = 0;
		openConstraints.gridy = 0;
		openConstraints.gridwidth = 1;
		mainPanel.add(openFileButton, openConstraints);
		
		GridBagConstraints newConstraints = new GridBagConstraints();
		newConstraints.gridx = 0;
		newConstraints.gridy = 1;
		newConstraints.gridwidth = 1;
		mainPanel.add(newButton, newConstraints);
		
		GridBagConstraints generateRandomStringConstraints = new GridBagConstraints();
		generateRandomStringConstraints.gridx = 0;
		generateRandomStringConstraints.gridy = 2;
		generateRandomStringConstraints.gridwidth = 1;
		mainPanel.add(generateRandomStringButton, generateRandomStringConstraints);
		
		GridBagConstraints saveConstraints = new GridBagConstraints();
		saveConstraints.gridx = 0;
		saveConstraints.gridy = 3;
		saveConstraints.gridwidth = 1;
		mainPanel.add(saveButton, saveConstraints);
		
		GridBagConstraints scrollPanelConstraints = new GridBagConstraints();
		scrollPanelConstraints.gridx = 1;
		scrollPanelConstraints.gridy = 0;
		scrollPanelConstraints.gridwidth = 1;
		mainPanel.add(listPane, scrollPanelConstraints);

		this.mainFrame.setVisible(true);
	}
}
