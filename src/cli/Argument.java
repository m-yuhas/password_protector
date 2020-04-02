package cli;

public class Argument {
  
  public String shortName;
  public String longName;
  public String help;
  public boolean argFollows;
  
  public Argument(String shortName, String longName, boolean argFollows, String help) {
    this.shortName = shortName;
    this.longName = longName;
    this.argFollows = argFollows;
    this.help = help;
  }

}
