import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainWolframTest extends TestCase {
  
  private MainWolfram mw;
  
  @Before
  public void setUp() throws Exception {
    mw = new MainWolfram(100,100);
  }
  
  @Test
  public void testIsBitSet() {
    // Test a few values
    assertTrue("Assert failed", MainWolfram.isBitSet(8,3));
    assertTrue("Assert failed", MainWolfram.isBitSet(9,3));
    assertTrue("Assert failed", MainWolfram.isBitSet(1,0));
    assertTrue("Assert failed", !MainWolfram.isBitSet(10,0));
  }
  
  @Test
  public void testGetWrappedValue() {
    // Test a few values after setting up the class
    mw.setWidth(100);
  }
}