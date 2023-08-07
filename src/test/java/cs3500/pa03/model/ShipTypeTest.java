package cs3500.pa03.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests the ShipType enum
 */
public class ShipTypeTest {

  /**
   * Tests the .getSize() method
   */
  @Test
  public void getSize() {
    assertEquals(6, ShipType.CARRIER.getSize());
    assertEquals(5, ShipType.BATTLESHIP.getSize());
    assertEquals(4, ShipType.DESTROYER.getSize());
    assertEquals(3, ShipType.SUBMARINE.getSize());
  }

  /**
   * Tests the .toElement() method
   */
  @Test
  public void toElement() {
    assertEquals(BoardElement.CARRIER, ShipType.CARRIER.toElement());
    assertEquals(BoardElement.BATTLESHIP, ShipType.BATTLESHIP.toElement());
    assertEquals(BoardElement.DESTROYER, ShipType.DESTROYER.toElement());
    assertEquals(BoardElement.SUBMARINE, ShipType.SUBMARINE.toElement());
  }

  /**
   * Tests the .values() method
   */
  @Test
  public void values() {
    assertArrayEquals(new ShipType[]{ShipType.CARRIER, ShipType.BATTLESHIP, ShipType.DESTROYER,
        ShipType.SUBMARINE}, ShipType.values());
  }

  /**
   * Tests the .valueOf(String) method
   */
  @Test
  public void valueOf() {
    assertEquals(ShipType.CARRIER, ShipType.valueOf("CARRIER"));
    assertEquals(ShipType.BATTLESHIP, ShipType.valueOf("BATTLESHIP"));
    assertEquals(ShipType.DESTROYER, ShipType.valueOf("DESTROYER"));
    assertEquals(ShipType.SUBMARINE, ShipType.valueOf("SUBMARINE"));
  }
}