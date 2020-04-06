package cli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ArgumentParser {
  
  private String description;
  private ArrayList<Argument> arguments;
  private Map<String, String> parsedArgs;

  public ArgumentParser(String description) {
    this.description = description;
    this.arguments = new ArrayList<Argument>();
  }
  
  public void addArgument(String shortName, String longName, boolean argFollows, String help) {
    this.arguments.add(new Argument(shortName, longName, argFollows, help));
  }
  
  public Map<String, String> parseArgs(String[] args) throws ParseException {
    parsedArgs = new HashMap<String, String>();
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
