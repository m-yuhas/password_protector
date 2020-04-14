package cli;

/**
 * This class is an exception that can be thrown if an error occurs while parsing the command-line
 * arguments.
 */
public class ParseException extends Exception {

  private static final long serialVersionUID = 7583932604952895463L;

  /**
   * Constructs an instance of ParseException.
   * 
   * @param errorMessage is a string containing the error message relevant to this instance of
   *        ParseException.
   */
  public ParseException(String errorMessage) {
    super(errorMessage);
  }
}