package icon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

class Icon extends JComponent {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Override
  public void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    Shape backgroundOuterCircle = new Ellipse2D.Double(0, 0, 1024, 1024);
    Shape backgroundInnerCircle = new Ellipse2D.Double(50, 50, 924, 924);
    Shape keyHandle = new Ellipse2D.Double(325, 600, 374, 374);
    int[] xcoords = new int[] {487, 487, 537, 537, 587, 587, 687, 687, 637, 637, 687, 687, 587, 587, 537, 537};
    int[] ycoords = new int[] {620, 100, 100, 200, 200, 100, 100, 150, 150, 300, 300, 350, 350, 250, 250, 620};
    Shape keyBody = new Polygon(xcoords, ycoords, 16);
    Shape keyHandleHole = new Ellipse2D.Double(375, 650, 274, 274);
    g2.setColor(new Color(0, 0, 128));
    g2.fill(backgroundOuterCircle);
    g2.draw(backgroundOuterCircle);
    g2.setColor(new Color(0, 128, 128));
    g2.fill(backgroundInnerCircle);
    g2.draw(backgroundInnerCircle);
    g2.setColor(new Color(255, 255, 0));
    g2.fill(keyHandle);
    g2.draw(keyHandle);
    g2.fill(keyBody);
    g2.draw(keyBody);
    g2.setColor(new Color(0, 128, 128));
    g2.fill(keyHandleHole);
    g2.draw(keyHandleHole);
  }

  public void saveImage(String name, Dimension dimensions) {
      BufferedImage image = new BufferedImage(getWidth(),getHeight(), BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2 = image.createGraphics();
      paint(g2);
      
      try{
          ImageIO.write(this.resize(image, dimensions), "png", new File(name));
      } catch (Exception e) {
          e.printStackTrace();
      }
  }
  
  private BufferedImage resize(BufferedImage img, Dimension dimensions) { 
    Image temp = img.getScaledInstance(dimensions.width, dimensions.height, Image.SCALE_SMOOTH);
    BufferedImage resizedImage = new BufferedImage(dimensions.width, dimensions.height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = resizedImage.createGraphics();
    g2d.drawImage(temp, 0, 0, null);
    g2d.dispose();
    return resizedImage;
  }  

}