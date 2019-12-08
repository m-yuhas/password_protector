package passwordio;

import java.security.SecureRandom;
import java.util.ArrayList;

public class PasswordGenerator {
  
  public static final char[] NUMBERS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
  public static final char[] LOWER_CASE_LETTERS = {
      'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
      'x', 'y', 'z'
  };
  public static final char[] UPPER_CASE_LETTERS = {
      'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
      'X', 'Y', 'Z'
  };
  public static final char[] SYMBOLS = {
      '~', '`', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '-', '+', '=', '{', '}', '[', ']', '|', '\\',
      ':', ';', '"', '\'', '<', '>', ',', '.', '?', '/'
  };
  
  private SecureRandom randomNumberGenerator;
  private ArrayList<Character> allowableCharacters;

  public PasswordGenerator(char[] ... allowableCharacters) {
    this.randomNumberGenerator = new SecureRandom();
    this.allowableCharacters = new ArrayList<Character>();
    for (char[] arg: allowableCharacters) {
      for (char c: arg) {
        this.allowableCharacters.add(c);
      }
    }
  }
  
  public char[] generatePassword(int length) {
    char[] password = new char[length];
    for (int i = 0; i < password.length; i++) {
      password[i] = this.allowableCharacters.get(this.randomNumberGenerator.nextInt(this.allowableCharacters.size()));
    }
    return password;
  }
}
