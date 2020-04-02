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
    for (String s: args) {
      System.out.println(s);
    }
    parsedArgs = new HashMap<String, String>();
    int state = 0;
    String next = "";
    for (String arg: args) {
      if (state == 0) {
        for (Argument expected: this.arguments) {
          if (arg.equals("-" + expected.shortName) || arg.equals("--" + expected.longName)) {
            parsedArgs.put(expected.shortName, "");
            if (expected.argFollows) { 
              state = 1;
              next = expected.shortName;
              break;
            }
          }
        }
        if (state == 0) {
          System.out.println("Here");
          throw new ParseException("Unexpected value");
        }
      } else if (state == 1) {
        if (arg.startsWith("-") || arg.startsWith("--")) {
          System.out.println("HERE");
          throw new ParseException("Argument requires a value");
        }
        parsedArgs.put(next, arg);
        state = 0;
      } else {
        System.out.println("HeRe");
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
    for (Argument arg: this.arguments) {
      System.out.print("  -" + arg.shortName + ",--" + arg.longName + " ");
      if (arg.argFollows) {
        System.out.print("<arg>");
      }
      System.out.print("\t\t\t" + arg.help + "\n");
    }
  }

}
