import javax.swing.*;
import java.awt.*;

public class ADisplay extends JFrame {
  
  public Canvas canvas;
  
  public ADisplay(int width, int height) {
    this(new Dimension(width, height));
  }
  
  public ADisplay(Dimension window_size) {
    this.setTitle("");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false);
    
    canvas = new Canvas();
    
    canvas.setPreferredSize(window_size);
    canvas.setFocusable(false);
    
    this.add(canvas);
    this.pack();
    this.setLocation(50, 50); // Position on screen
  }
  
}
