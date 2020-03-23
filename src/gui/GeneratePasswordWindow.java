package gui;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import passwordio.PasswordGenerator;

public class GeneratePasswordWindow {

  private JFrame mainFrame;
  private JTextField generatedPasswordField;
  private Map<JCheckBox, char[]> characterChoices;
  private JTextField passwordSizeField;
  
  public GeneratePasswordWindow() {
    this.mainFrame = new JFrame("Generate Password");
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    //mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    this.characterChoices = Map.ofEntries(
        new AbstractMap.SimpleEntry<JCheckBox, char[]>(new JCheckBox("Numbers"), passwordio.PasswordGenerator.NUMBERS),
        new AbstractMap.SimpleEntry<JCheckBox, char[]>(new JCheckBox("Lower Case Letters"), passwordio.PasswordGenerator.LOWER_CASE_LETTERS),
        new AbstractMap.SimpleEntry<JCheckBox, char[]>(new JCheckBox("Upper Case Letters"), passwordio.PasswordGenerator.UPPER_CASE_LETTERS),
        new AbstractMap.SimpleEntry<JCheckBox, char[]>(new JCheckBox("Symbols"), passwordio.PasswordGenerator.SYMBOLS));
    Iterator<Entry<JCheckBox, char[]>> iterator = this.characterChoices.entrySet().iterator();
    while (iterator.hasNext()) {
      mainPanel.add(iterator.next().getKey());
    }
    JPanel passwordSizePanel = new JPanel();
    JLabel passwordSizeLabel = new JLabel("Length (Characters):");
    passwordSizePanel.add(passwordSizeLabel);
    //passwordSizePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    this.passwordSizeField = new JTextField(2);
    passwordSizePanel.add(this.passwordSizeField);
    mainPanel.add(passwordSizePanel);
    JButton generateButton = new JButton("Generate Passord");
    generateButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        generate();
      }
    });
    mainPanel.add(generateButton);
    //JPanel generatedPanel = new JPanel();
    this.generatedPasswordField = new JTextField(16);
    this.generatedPasswordField.setEditable(false);
    mainPanel.add(generatedPasswordField);
    JButton copyButton = new JButton("Copy to Clipboard");
    copyButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        copyToClipboard();
      }
    });
    //copyButton.setAlignmentX(Component.LEFT_ALIGNMENT);
    mainPanel.add(copyButton);
    this.mainFrame.add(mainPanel);
    this.mainFrame.pack();
    this.mainFrame.setVisible(true);
  }
  
  private void generate() {
    ArrayList<Character> acceptableCharacters = new ArrayList<Character>();
    for (JCheckBox choice: this.characterChoices.keySet()) {
      if (choice.isSelected()) {
        for (char c: this.characterChoices.get(choice)) {
          acceptableCharacters.add(c);
        }
      }
    }
    char[] acceptableCharArray = new char[acceptableCharacters.size()];
    for (int i = 0; i < acceptableCharArray.length; i++) {
      acceptableCharArray[i] = acceptableCharacters.get(i);
    }
    passwordio.PasswordGenerator passwordGenerator = new PasswordGenerator(acceptableCharArray);
    try {
      int passwordLength = Integer.parseInt(this.passwordSizeField.getText());
      this.generatedPasswordField.setText(new String(passwordGenerator.generatePassword(passwordLength)));
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(this.mainFrame,
          "Please enter a valid integer for password size.",
          "NumberFormatException",
          JOptionPane.ERROR_MESSAGE);
    }
  }
  
  private void copyToClipboard() {
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    clipboard.setContents(new StringSelection(this.generatedPasswordField.getText()), null);
  }
}
