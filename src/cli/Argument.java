package cli;

/**
 * The Argument class represents an option for the ArgumentParser class.  This class mainly
 * mainly functions as a structure to store data associated with an option.
 */
public class Argument {

  /**
   * Single character representing the option.  Will be preceded by '-' when invoked from the
   * command-line.  Data type is string to allow for longer names to be used, but a single character
   * string is recommended.
   */
  public String shortName;

  /**
   * String representing the full name of an option.  Cannot contain spaces.  Will be preceded by
   * '--' when invoked from the command-line.
   */
  public String longName;

  /**
   * String that will be printed as the description of an option when the help menu is invoked.
   */
  public String help;

  /**
   * Boolean true is the following argument is the value for this argument.  This implies that a
   * following argument is required.
   */
  public boolean argFollows;

  /**
   * Construct an Argument object.
   * 
   * @param shortName is a string that is the short name (preceded by '-') of this argument.
   * @param longName is a string that is the long name (preceded by '--') of this argument.
   * @param argFollows is a boolean value that determines if the following command-line argument
   *        should be interpreted as the value for this option.
   * @param help is a description string that will be printed as part of the help menu.
   */
  public Argument(String shortName, String longName, boolean argFollows, String help) {
    this.shortName = shortName;
    this.longName = longName;
    this.argFollows = argFollows;
    this.help = help;
  }

}