package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.util.function.Function;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

public class PasswordEntryWindow {
  
  public char[] password;
  private JFrame mainFrame;
  
  public PasswordEntryWindow(MainWindow context, Function<char[], Void> callback) {
    this.mainFrame = new JFrame("Password Entry");
    JPanel mainPanel = new JPanel();
    JLabel explanation = new JLabel("Enter the password:");
    JPasswordField passwordField = new JPasswordField(32);
    JButton submitButton = new JButton("Submit");
    submitButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
        password = passwordField.getPassword();
        //context.openFileCallback(password);
        callback.apply(password);
      }
    });
    submitButton.addKeyListener(new KeyListener() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          submitButton.doClick();
        }
      }

      @Override
      public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        
      }

      @Override
      public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
      }
    });
    passwordField.addKeyListener(new KeyListener() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          submitButton.doClick();
        }
      }

      @Override
      public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        
      }

      @Override
      public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
      }
    });
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.add(explanation);
    mainPanel.add(passwordField);
    mainPanel.add(submitButton);
    this.mainFrame.add(mainPanel);
    this.mainFrame.pack();
  }
  
  public PasswordEntryWindow(MainWindow context, Object callback) {
    // TODO Auto-generated constructor stub
  }

  public void show() {
    this.mainFrame.setVisible(true);
  }

}
