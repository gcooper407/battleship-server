package cs3500.pa03.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests the GameResult enum
 */
public class GameResultTest {

  /**
   * Tests the .values() method
   */
  @Test
  public void values() {
    assertArrayEquals(new GameResult[]{GameResult.WIN, GameResult.LOSE, GameResult.DRAW},
        GameResult.values());
  }

  /**
   * Tests the .valueOf(String) method
   */
  @Test
  public void valueOf() {
    assertEquals(GameResult.WIN, GameResult.valueOf("WIN"));
    assertEquals(GameResult.LOSE, GameResult.valueOf("LOSE"));
    assertEquals(GameResult.DRAW, GameResult.valueOf("DRAW"));
  }
}