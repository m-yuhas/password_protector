package icon;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Create an icon set for the password protector application.
 */
public class CreateIconSet {

  /**
   * Map of of the required file names and their dimensions for a Mac OSX icon set.
   */
  private final static Map<String, Dimension> iconFiles;
  static {
    Map<String, Dimension> map = new HashMap<String, Dimension>();
    map.put("icon_512x512@2x.png", new Dimension(1024, 1024));
    map.put("icon_512x512.png", new Dimension(512, 512));
    map.put("icon_256x256@2x.png", new Dimension(512, 512));
    map.put("icon_256x256.png", new Dimension(256, 256));
    map.put("icon_128x128@2x.png", new Dimension(256, 256));
    map.put("icon_128x128.png", new Dimension(128, 128));
    map.put("icon_32x32@2x.png", new Dimension(64, 64));
    map.put("icon_32x32.png", new Dimension(32, 32));
    map.put("icon_16x16@2x.png", new Dimension(32, 32));
    map.put("icon_16x16.png", new Dimension(16, 16));
    iconFiles = Collections.unmodifiableMap(map);
  }

  /**
   * Entry point for execution.  Steps are to create a temporary folder at the specified path, then
   * create each of the required png images in that folder.  Lastly use Mac OS's 'iconutil' command
   * to create the icns file and then clean up temporary resources.
   *
   * @param args is the array of strings representing command line inputs.
   */
  public static void main(String[] args) {
    if (args.length > 1 && args[0] == "-h" || args[0] == "--help" || args.length == 0) {
      System.out.println(
          "Create icon set for password protector's Mac OSX bundle.  Usage:\n" +
          "java icon.CreateIconSet <path to final .icns file>");
      return;
    }
    File tempPath = new File(args[0] + "/PasswordProtector.iconset/");
    if (!tempPath.mkdirs()) {
       System.out.println("Sorry couldn’t create specified directory.");
       return;
    }
    Icon icon = new Icon();
    icon.setSize(1024, 1024);
    icon.setBackground(new Color(0, 0, 0, 255));
    for (Entry<String, Dimension> entry: CreateIconSet.iconFiles.entrySet()) {
      icon.saveImage(Paths.get(tempPath.getPath(), entry.getKey()), entry.getValue());
    }
    try {
      ProcessBuilder processBuilder = new ProcessBuilder();
      processBuilder.command("bash", "-c", "iconutil -c icns " + tempPath.getPath());
      Process process = processBuilder.start();
      if (process.waitFor() != 0) {
        System.out.println("An error occurred while running iconutil.");
      }
    } catch (IOException | InterruptedException e) {
      System.out.println("An error occurred while running iconutil.");
      e.printStackTrace();
    }
    String[] tempFile = tempPath.list();
    for(String file: tempFile){
        new File(tempPath.getPath(), file).delete();
    }
    tempPath.delete();
  }

}
