package cs3500.pa03.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests the BoardElement enum
 */
public class BoardElementTest {

  /**
   * Tests the .renderFull() method
   */
  @Test
  public void renderFull() {
    assertEquals('-', BoardElement.EMPTY.renderFull());
    assertEquals('X', BoardElement.HIT.renderFull());
    assertEquals('O', BoardElement.MISS.renderFull());
    assertEquals('C', BoardElement.CARRIER.renderFull());
    assertEquals('B', BoardElement.BATTLESHIP.renderFull());
    assertEquals('D', BoardElement.DESTROYER.renderFull());
    assertEquals('S', BoardElement.SUBMARINE.renderFull());
  }

  /**
   * Tests the .renderHidden() method
   */
  @Test
  public void renderHidden() {
    assertEquals('-', BoardElement.EMPTY.renderHidden());
    assertEquals('X', BoardElement.HIT.renderHidden());
    assertEquals('O', BoardElement.MISS.renderHidden());
    assertEquals('-', BoardElement.CARRIER.renderHidden());
    assertEquals('-', BoardElement.BATTLESHIP.renderHidden());
    assertEquals('-', BoardElement.DESTROYER.renderHidden());
    assertEquals('-', BoardElement.SUBMARINE.renderHidden());
  }

  /**
   * Tests the .values() method
   */
  @Test
  public void values() {
    assertArrayEquals(new BoardElement[]{BoardElement.EMPTY, BoardElement.CARRIER,
        BoardElement.BATTLESHIP, BoardElement.DESTROYER, BoardElement.SUBMARINE,
        BoardElement.HIT, BoardElement.MISS}, BoardElement.values());
  }

  /**
   * Tests the .valueOf(String) method
   */
  @Test
  public void valueOf() {
    assertEquals(BoardElement.EMPTY, BoardElement.valueOf("EMPTY"));
    assertEquals(BoardElement.HIT, BoardElement.valueOf("HIT"));
    assertEquals(BoardElement.MISS, BoardElement.valueOf("MISS"));
    assertEquals(BoardElement.CARRIER, BoardElement.valueOf("CARRIER"));
    assertEquals(BoardElement.BATTLESHIP, BoardElement.valueOf("BATTLESHIP"));
    assertEquals(BoardElement.DESTROYER, BoardElement.valueOf("DESTROYER"));
    assertEquals(BoardElement.SUBMARINE, BoardElement.valueOf("SUBMARINE"));
  }
}