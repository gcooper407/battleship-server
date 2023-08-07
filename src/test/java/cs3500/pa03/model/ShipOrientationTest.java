package cs3500.pa03.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests the ShipOrientation enum
 */
public class ShipOrientationTest {

  /**
   * Tests the .values() method
   */
  @Test
  public void values() {
    assertArrayEquals(new ShipOrientation[]{ShipOrientation.HORIZONTAL, ShipOrientation.VERTICAL},
        ShipOrientation.values());
  }

  /**
   * Tests the .valuesOf(String) method
   */
  @Test
  public void valueOf() {
    assertEquals(ShipOrientation.HORIZONTAL, ShipOrientation.valueOf("HORIZONTAL"));
    assertEquals(ShipOrientation.VERTICAL, ShipOrientation.valueOf("VERTICAL"));
  }
}