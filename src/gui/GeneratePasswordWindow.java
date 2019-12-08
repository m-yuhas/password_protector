package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GeneratePasswordWindow {

  private JFrame mainFrame;
  
  public GeneratePasswordWindow() {
    this.mainFrame = new JFrame("Generate Password");
    JPanel mainPanel = new JPanel();
    
    this.mainFrame.add(mainPanel);
    this.mainFrame.pack();
  }
  
}
