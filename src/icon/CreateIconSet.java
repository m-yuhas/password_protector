package icon;

import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CreateIconSet {
  
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

  public static void main(String[] args) {
    if (args.length > 1 && args[0] == "-h" || args[0] == "--help" || args.length > 2) {
      System.out.println(
          "Create icon set for password protector's Mac OSX bundle.  Usage:\n" +
          "java icon.CreateIconSet <path to final .icns file>");
    }
    File temp = new File(args[0] + "PasswordProtector.iconset");
    boolean bool = temp.mkdir();
    if(bool){
       System.out.println("Directory created successfully");
    }else{
       System.out.println("Sorry couldn’t create specified directory");
    }
    Icon p = new Icon();
    p.setSize(1024, 1024);
    p.setBackground(new Color(0, 0, 0, 255));
    for (Entry<String, Dimension> entry: CreateIconSet.iconFiles.entrySet()) {
      p.saveImage(args[0] + "PasswordProtector.iconset/" + entry.getKey(), entry.getValue());
    }
    ProcessBuilder processBuilder = new ProcessBuilder();
    processBuilder.command("bash", "-c", "iconutil -c icns " + args[0] + "PasswordProtector.iconset");
    try {
      Process process = processBuilder.start();
      StringBuilder output = new StringBuilder();
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line + "\n");
      }
      int exitVal = process.waitFor();
      if (exitVal == 0) {
        System.out.println("Success!");
        System.out.println(output);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
