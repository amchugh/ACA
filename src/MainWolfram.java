import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

public class MainWolfram {
  
  private static final int DEFAULT_WIDTH = 1920/2;
  private static final int DEFAULT_HEIGHT = 1080/2;
  
  private static final int WIN_WIDTH = 1920/2;
  private static final int WIN_HEIGHT = 1080/2;
  private static final int UPDATE_SPEED = 90;
  private static final boolean INSTANT = false;
  
  public static void main(String[] args) {
    new MainWolfram(DEFAULT_WIDTH, DEFAULT_HEIGHT).run();
  }
  
  private boolean DOSCROLL = true;
  private boolean DOWRAP = false;
  
  private ArrayList<Generation> gens;
  private BufferedImage image;
  private ADisplay display;
  
  private double delayTime;
  private int neighborhoodsize = 5;
  private int ruleset = 6845; // should be less than 2^(2^neighborhood size)
  
  private int width, height;
  
  public MainWolfram(int width, int height) {
    // store values
    this.width = width;
    this.height = height;
    
    // Setup the generation
  
    gens = new ArrayList<>();
    gens.add(new Generation(width));
    
    // Set the update speed
    changeSpeed(UPDATE_SPEED); // We'll do 6 updates per second
    
    // Draw to the screen
    display = new ADisplay(WIN_WIDTH, WIN_HEIGHT);
    // Create an image object
    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    
    // The simulation is not allowed to be both instant and scrolling
    if (INSTANT) {
      DOSCROLL = false;
      // Handle the instant
      for (int y = 1; y < height; y++) {
        getNextGeneration();
      }
    }
  }
  
  public void run() {
    // Draw to the window for the first time
    draw();
    // Make the window visible
    display.setVisible(true);
    long pastUpdateTime = System.nanoTime();
    
    while (true) {
      
      if (System.nanoTime() - pastUpdateTime > delayTime) {
        pastUpdateTime += delayTime;
        if (!INSTANT)
          getNextGeneration();
        draw();
      }
      
    }
  }
  
  public void changeSpeed(int newUPS) {
    if (newUPS > 0) {
      delayTime = Math.pow(10, 9) / newUPS;
    }
  }
  
  private void getNextGeneration() {
    int[] n = new int[width];
    int[] p = gens.get(gens.size()-1).values;
    for (int x = 0; x < width; x++) {
      int pattern_index = 0;
      for (int x1 = 0; x1 < neighborhoodsize; x1++) {
        int s_index = x1 - neighborhoodsize/2 + x;
        if (getWrappedValue(s_index, p) == 1) {
          pattern_index += Math.pow(2, neighborhoodsize-x1-1);
        }
      }
      if (isBitSet(ruleset, pattern_index)) {
        n[x] = 1;
      } else {
        n[x] = 0;
      }
    }
    gens.add(new Generation(n));
  }
  
  private class Generation {
    private int[] values;
    // Create the default generation
    private Generation(int width) {
      values = new int[width];
      values[width/2] = 1;
    }
    // Create a generation object from an array
    private Generation(int[] values) {
      this.values = values;
    }
  }
  
  public static boolean isBitSet(int number, int bitIndex) {
    int temp = 1 << (bitIndex);
    if ((number & temp) > 0) {
      return true;
    }
    return false;
  }
  
  public int getWrappedValue(int n, int[] values) {
    if (DOWRAP) {
      return values[getWrappedInt(n, width)];
    } else {
      if (n < 0 || n >= width) {
        return 0;
      }
      return values[n];
    }
  }
  
  public int getWrappedInt(int n, int max) {
    return (n + max) % max;
  }
  
  private void draw() {
  
    BufferStrategy b = display.canvas.getBufferStrategy();
    if (b == null) {
      display.canvas.createBufferStrategy(3);
      b = display.canvas.getBufferStrategy();
    }
  
    Graphics g = b.getDrawGraphics();
    
    int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
    
    // Start drawing
    int c = 0;
    int start_val = 0;
    if (DOSCROLL) {
      start_val = gens.size() - height;
      if (start_val < 0)
        start_val = 0;
    }
    // Fill with the generation data
    for (int index = start_val; index < gens.size(); index++) {
      Generation gen = gens.get(index);
      for (int i = 0; i < gen.values.length; i++) {
        if (gen.values[i] == 1 ) {
          pixels[i + c*width] = 0x0;
        } else {
          pixels[i + c*width] = 0xffffff;
        }
      }
      c++;
      if (c >= height) {
        break;
      }
    }
    
    // fill the rest of the screen with white
    for (; c < height; c++ ) {
      for (int x = 0; x < width; x++) {
        pixels[x + c*height] = 0xffffff;
      }
    }
  
    g.drawImage(image, 0, 0, WIN_WIDTH, WIN_HEIGHT, null);
    g.dispose();
    b.show();
    
  }
  
  public void setWidth(int w) {
    this.width = w;
  }
  
}
