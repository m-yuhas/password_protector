package gui;

import java.awt.EventQueue;

public class Gui {

  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      
      @Override
      public void run() {
        MainWindow mainWindow = new MainWindow();
        mainWindow.show();
      }
      
    });
  }

}