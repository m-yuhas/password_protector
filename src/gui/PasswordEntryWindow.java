package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

public class PasswordEntryWindow extends JDialog {
  
  /**
   * 
   */
  private static final long serialVersionUID = 5118665644770822849L;
  private JButton submitButton;
  private JPanel mainPanel;
  private JLabel message;
  private JLabel password1Label;
  private JLabel password2Label;
  private JPasswordField password1Field;
  private JPasswordField password2Field;

  public PasswordEntryWindow(String message) {
    this.setModal(true);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.setSize(new Dimension(500, 150));
    this.setTitle("Password Entry");

    this.mainPanel = new JPanel();
    this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));
    
    JPanel messagePanel = new JPanel();
    this.message = new JLabel(message + "\n");
    messagePanel.add(this.message);
    this.mainPanel.add(messagePanel);

    JPanel password1Panel = new JPanel();
    this.password1Label = new JLabel("Password 1:");
    password1Panel.add(this.password1Label);

    this.password1Field = new JPasswordField(32);
    password1Panel.add(this.password1Field);
    this.mainPanel.add(password1Panel);
    
    JPanel password2Panel = new JPanel();
    this.password2Label = new JLabel("Password 2:");
    password2Panel.add(this.password2Label);

    this.password2Field = new JPasswordField(32);
    password2Panel.add(this.password2Field);
    this.mainPanel.add(password2Panel);

    this.submitButton = new JButton("Submit");
    ListenForButton listener = new ListenForButton();

    this.submitButton.addActionListener(listener);
    this.mainPanel.add(this.submitButton);
    
    this.add(mainPanel);
    this.setVisible(true);

  }

  private class ListenForButton implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      dispose();
    }
  }

  public char[][] getPasswords() {
    return new char[][] {this.password1Field.getPassword(), this.password2Field.getPassword()};
  }

}