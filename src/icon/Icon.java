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
import java.nio.file.Path;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 * Icon for password protector app.
 */
class Icon extends JComponent {
  
  /**
   * Unique ID for serialization.
   */
  private static final long serialVersionUID = 1L;

  /**
   * {@inheritDoc}
   */
  @Override
  public void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    Shape backgroundOuterCircle = new Ellipse2D.Double(0, 0, 1024, 1024);
    Shape backgroundInnerCircle = new Ellipse2D.Double(50, 50, 924, 924);
    Shape keyHandle = new Ellipse2D.Double(325, 600, 374, 374);
    int[] xcoords = new int[] {
        487, 487, 537, 537, 587, 587, 687, 687, 637, 637, 687, 687, 587, 587, 537, 537};
    int[] ycoords = new int[] {
        620, 100, 100, 200, 200, 100, 100, 150, 150, 300, 300, 350, 350, 250, 250, 620};
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

  /**
   * Save icon as an image file with the required dimensions.
   *
   * @param name is the path to the file to create.
   * @param dimensions are the dimensions of the output image.
   */
  public void saveImage(Path name, Dimension dimensions) {
      BufferedImage image = new BufferedImage(getWidth(),getHeight(), BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2 = image.createGraphics();
      paint(g2);
      
      try{
          ImageIO.write(this.resize(image, dimensions), "png", name.toFile());
      } catch (Exception e) {
          e.printStackTrace();
      }
  }

  /**
   * Resize a buffered image.
   * 
   * @param img is the input BufferedImage.
   * @param dimensions is a Dimensions object that describes the dimensions of the output image.
   * @return a BufferedImage scaled down to the dimensions parameter.
   */
  private BufferedImage resize(BufferedImage img, Dimension dimensions) { 
    Image tempImg = img.getScaledInstance(dimensions.width, dimensions.height, Image.SCALE_SMOOTH);
    BufferedImage resizedImage = new BufferedImage(
        dimensions.width,
        dimensions.height,
        BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = resizedImage.createGraphics();
    g2d.drawImage(tempImg, 0, 0, null);
    g2d.dispose();
    return resizedImage;
  }  

}