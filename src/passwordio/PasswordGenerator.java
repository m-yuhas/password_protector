package passwordio;

import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * This class generates passwords based on a secure pseudorandom seed and a set of allowable characters.
 */
public class PasswordGenerator {

  /**
   * ASCII numerals 0-9
   */
  public static final char[] NUMBERS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

  /**
   * ASCII lower case characters a-z
   */
  public static final char[] LOWER_CASE_LETTERS = {
      'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
      'x', 'y', 'z'
  };

  /**
   * ASCII upper case characters A-Z
   */
  public static final char[] UPPER_CASE_LETTERS = {
      'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
      'X', 'Y', 'Z'
  };

  /**
   * ASCII characters that can be typed on a US keyboard excluding a-z, A-Z, and 0-9
   */
  public static final char[] SYMBOLS = {
      '~', '`', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '-', '+', '=', '{', '}', '[', ']', '|', '\\',
      ':', ';', '"', '\'', '<', '>', ',', '.', '?', '/'
  };

  private SecureRandom randomNumberGenerator;
  private ArrayList<Character> allowableCharacters;

  /**
   * Constructs an instance of PasswordGenerator.
   * 
   * @param allowableCharacters is an array of character arrays that represent all the valid characters that can appear
   *        in a generated password.
   */
  public PasswordGenerator(char[] ... allowableCharacters) {
    this.randomNumberGenerator = new SecureRandom();
    this.allowableCharacters = new ArrayList<Character>();
    for (char[] arg: allowableCharacters) {
      for (char c: arg) {
        this.allowableCharacters.add(c);
      }
    }
  }

  /**
   * Generate a password using the characters allowed by this instance of PasswordGenerator.
   * 
   * @param length is an integer of the desired output password length in characters.
   * @return the generated password in the form of a character array.
   */
  public char[] generatePassword(int length) {
    char[] password = new char[length];
    for (int i = 0; i < password.length; i++) {
      password[i] = this.allowableCharacters.get(this.randomNumberGenerator.nextInt(this.allowableCharacters.size()));
    }
    return password;
  }
}