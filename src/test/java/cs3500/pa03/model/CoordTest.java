package cs3500.pa03.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the Coord record, including constructors and getters
 */
public class CoordTest {

  Coord c1;
  Coord c2;

  @BeforeEach
  public void initData() {
    this.c1 = new Coord(1, 5);
    this.c2 = new Coord(3, 4);
  }

  /**
   * Tests the .xpos() method
   */
  @Test
  public void xpos() {
    assertEquals(1, c1.xpos());
    assertEquals(3, c2.xpos());
    assertNotEquals(c1.xpos(), c2.xpos());
  }

  /**
   * Tests the .ypos() method
   */
  @Test
  public void ypos() {
    assertEquals(5, c1.ypos());
    assertEquals(4, c2.ypos());
    assertNotEquals(c1.xpos(), c2.ypos());
  }
}