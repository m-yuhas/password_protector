package gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;


/**
 * Class of button that performs its action when selected and and the user presses enter.
 */
public class KeyableButton extends JButton {

  /**
   * Unique ID for serialization.
   */
  private static final long serialVersionUID = -1294042084540205908L;

  public KeyableButton(String buttonText) {
    super(buttonText);
    this.addKeyListener(new KeyAdapter() {

      @Override
      public void keyTyped(KeyEvent e) {
        if (e.getKeyCode() == 0) {
          doClick();
        }
      }

    });
  }
  
}
