package icon;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
    Icon p = new Icon();
    p.setSize(1024, 1024);
    p.setBackground(new Color(0, 0, 0, 255));
    p.saveImage("foo", new Dimension(512, 512));
  }

}
