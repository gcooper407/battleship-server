package cs3500.pa03.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the constructor and methods of the BotPlayer class
 */
public class BotPlayerTest {

  Player bot;
  Map<ShipType, Integer> map;
  Board ownBoard;
  Board oppBoard;

  /**
   * Initializes the test data (also serves to test the constructor)
   */
  @BeforeEach
  public void initData() {
    ownBoard = new Board();
    oppBoard = new Board();

    this.bot = new BotPlayer(ownBoard, oppBoard, new Random(1));

    map = new HashMap<>();
    map.put(ShipType.CARRIER, 1);
    map.put(ShipType.BATTLESHIP, 1);
    map.put(ShipType.DESTROYER, 1);
    map.put(ShipType.SUBMARINE, 1);
  }


  /**
   * Tests the .name() method
   */
  @Test
  public void name() {
    assertNull(this.bot.name());
  }

  /**
   * Tests the .setup(...) method
   */
  @Test
  public void setup() {
    // tests that all Ships are placed in random origin Coordinates and directions
    Ship carrier = new Ship(ShipType.CARRIER, new Coord(0, 1), ShipOrientation.HORIZONTAL);
    Ship battleship = new Ship(ShipType.BATTLESHIP, new Coord(0, 0), ShipOrientation.HORIZONTAL);
    Ship destroyer = new Ship(ShipType.DESTROYER, new Coord(2, 2), ShipOrientation.HORIZONTAL);
    Ship submarine = new Ship(ShipType.SUBMARINE, new Coord(1, 5), ShipOrientation.HORIZONTAL);

    List<Ship> expectedFull = List.of(carrier, battleship, destroyer, submarine);
    List<Ship> actualFull = this.bot.setup(6, 6, map);

    assertEquals(expectedFull, actualFull);

    // tests to ensure that board properties are updated properly
    assertEquals(6, this.ownBoard.getBoard().length);
    assertEquals(6, this.ownBoard.getBoard()[0].length);
    assertEquals(6, this.oppBoard.getBoard().length);
    assertEquals(6, this.oppBoard.getBoard()[0].length);

    assertEquals(4, this.ownBoard.numShips());

    // tests to ensure that Board visuals match setup
    String ownBoardVisual = """
             B B B B B -
             C C C C C C
             - - D D D D
             - - - - - -
             - - - - - -
             - S S S - -
             
        """;
    String oppBoardVisual = """
             - - - - - -
             - - - - - -
             - - - - - -
             - - - - - -
             - - - - - -
             - - - - - -
             
        """;

    assertEquals(ownBoardVisual, this.ownBoard.render(true));
    assertEquals(oppBoardVisual, this.oppBoard.render(false));
  }

  /**
   * Tests the .takeShots() method
   */
  @Test
  public void takeShots() {
    this.bot.setup(6, 6, map);

    // tests that the BotPlayer's initial shots are completely random
    List<Coord> shotsExpected = List.of(new Coord(2, 5), new Coord(4, 5),
        new Coord(3, 2), new Coord(1, 4));
    List<Coord> shotsActual = this.bot.takeShots();
    assertEquals(shotsExpected, shotsActual);

    // then, updates the BotPlayer's shotQueue based on shots it took that hit its
    // opponent's ships
    List<Coord> hits = List.of(new Coord(3, 2), new Coord(1, 4));
    this.bot.successfulHits(hits);

    // tests that the BotPlayer's second round of shots is informed by its successful
    // hits from previous rounds
    shotsExpected = List.of(new Coord(3, 1), new Coord(3, 3), new Coord(2, 2), new Coord(4, 2));
    shotsActual = this.bot.takeShots();

    assertEquals(shotsExpected, shotsActual);
  }

  /**
   * Tests the .reportDamage(...) method
   */
  @Test
  public void reportDamage() {
    this.bot.setup(6, 6, map);

    // tests that reportDamage returns the list of Coords (shots), out of the given
    // list of Coords (shots), that hit this HumanPlayer's ships
    List<Coord> shots = List.of(new Coord(0, 3), new Coord(1, 5), new Coord(2, 5), new Coord(3, 5));
    List<Coord> hits = List.of(new Coord(1, 5), new Coord(2, 5), new Coord(3, 5));
    assertEquals(hits, this.bot.reportDamage(shots));

    // tests that the number of afloat ships is updated properly
    assertEquals(3, this.ownBoard.numShips());

    // tests that this BotPlayer's board visual is updated properly
    String ownBoardVisual = """
             B B B B B -
             C C C C C C
             - - D D D D
             O - - - - -
             - - - - - -
             - X X X - -
                
        """;

    assertEquals(ownBoardVisual, this.ownBoard.render(true));
  }

  /**
   * Tests the .successfulHits(...) method
   */
  @Test
  public void successfulHits() {
    this.bot.setup(6, 6, map);

    List<Coord> hits = List.of(new Coord(0, 0), new Coord(1, 1));

    // update the BotPlayer's shotQueue based on its successful hits
    this.bot.successfulHits(hits);

    // tests that the BotPlayer's next shots are directly informed by the report of
    // its successful hits (all of these shots are directly adjacent to one or more of its
    // successful hits), but all within bounds
    List<Coord> shots = List.of(new Coord(0, 1), new Coord(1, 0), new Coord(1, 2), new Coord(2, 1));
    assertEquals(shots, this.bot.takeShots());

    // tests to ensure that shots that hit the opponent's ship get marked accordingly
    // on this BotPlayer's Board of known information about their opponent's board state
    String oppBoardVisual = """
             X - - - - -
             - X - - - -
             - - - - - -
             - - - - - -
             - - - - - -
             - - - - - -
             
        """;

    assertEquals(oppBoardVisual, this.oppBoard.render(false));

    // update the BotPlayer's shotQueue based on (selected) successful hits
    hits = List.of(new Coord(5, 5), new Coord(4, 4));
    this.bot.successfulHits(hits);

    // tests that the BotPlayer's next shots are, once again, directly informed by the report of
    // its successful hits (all of these shots are directly adjacent to one or more of its
    // successful hits), but all within bounds
    shots = List.of(new Coord(5, 4), new Coord(4, 5), new Coord(4, 3), new Coord(3, 4));
    assertEquals(shots, this.bot.takeShots());
  }

  /**
   * Tests the .endGame(...) method
   */
  @Test
  public void endGame() {
    assertDoesNotThrow(() -> this.bot.endGame(GameResult.WIN, "Win message"));
    assertDoesNotThrow(() -> this.bot.endGame(GameResult.LOSE, "Lose message"));
    assertDoesNotThrow(() -> this.bot.endGame(GameResult.DRAW, "Draw message"));
  }
}