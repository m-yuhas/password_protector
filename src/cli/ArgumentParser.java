package cli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The ArgumentParser class generates an object that parses the command-line arguments.
 */
public class ArgumentParser {

  /**
   * The description of the command line argument.
   */
  private String description;

  /**
   * List of all the acceptable command line arguments.
   */
  private ArrayList<Argument> arguments;

  /**
   * Construct an instance of ArgumentParser.
   * 
   * @param description is a string that describes the program in which this object is being
   *        constructed and will be printed as part of the help menu.
   */
  public ArgumentParser(String description) {
    this.description = description;
    this.arguments = new ArrayList<Argument>();
  }

  /**
   * Add a supported argument to this parser.
   * 
   * @param shortName is as string representing the short version of the argument name.  When the
   *        command-line arguments are parsed, this object will check for a '-' followed by this
   *        string.  Spaces are not allowed in this parameter.
   * @param longName is a string representing the long version of the argument name.  When the
   *        command-line arguments are parsed, this object will check for a '--' followed by this
   *        string.  Spaces are not allowed in this parameter.
   * @param argFollows is a boolean value that determines if the command-line argument following
   *        this argument should be treated as a value for this argument or not.
   * @param help is a string containing the description of this argument.
   */
  public void addArgument(String shortName, String longName, boolean argFollows, String help) {
    this.arguments.add(new Argument(shortName, longName, argFollows, help));
  }

  /**
   * Parse the command-line arguments.
   * 
   * @param args is an array of strings for each command-line argument provided to the JVM.
   * @return a map whose keys are the parser's arguments' short names and whose values are the
   *         values provided in the input command-line arguments.  If an argument does not take a
   *         value (i.e. a flag), its presence in the map in indicates that it was provided.
   * @throws ParseException if an invalid sequence of command-line arguments is provided or if an
   *         unknown argument is provided.
   */
  public Map<String, String> parseArgs(String[] args) throws ParseException {
    Map<String, String> parsedArgs = new HashMap<String, String>();
    int state = 0;
    String next = "";
    for (String arg: args) {
      if (state == 0) {
        boolean validArg = false;
        for (Argument expected: this.arguments) {
          if (arg.equals("-" + expected.shortName) || arg.equals("--" + expected.longName)) {
            validArg = true;
            parsedArgs.put(expected.shortName, "");
            if (expected.argFollows) { 
              state = 1;
              next = expected.shortName;
            }
            break;
          }
        }
        if (!validArg) {
          throw new ParseException("Unexpected value");
        }
      } else if (state == 1) {
        if (arg.startsWith("-") || arg.startsWith("--")) {
          throw new ParseException("Argument requires a value");
        }
        parsedArgs.put(next, arg);
        state = 0;
      } else {
        throw new ParseException("Undefined state");
      }
    }
    if (state == 1) {
      throw new ParseException("Argument requires a value");
    }
    return parsedArgs;
  }

  /**
   * Print the help menu for this program.
   */
  public void printHelp() {
    System.out.println(this.description);
    int maxLength = 0;
    for (Argument arg: this.arguments) {
      maxLength = Math.max(maxLength, new String(arg.shortName + arg.longName + "  -,-- <arg> ").length());
    }
    for (Argument arg: this.arguments) {
      String line = new String("  -" + arg.shortName + ",--" + arg.longName + " ");
      if (arg.argFollows) {
        line = line.concat("<arg>");
      }
      line = line.concat(new String(new char[maxLength - line.length()]).replace('\0', ' '));
      System.out.println(line + arg.help);
    }
  }

}
