package gui;

import java.awt.EventQueue;

public class PasswordProtector {

  public static void main(String[] args) { 
    EventQueue.invokeLater(new Runnable() {
      
      @Override
      public void run() {
        new MainWindow();
      }
      
    });
  }

}