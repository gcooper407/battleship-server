package cs3500.pa03.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.view.GameView;
import cs3500.pa03.view.View;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the constructor and methods of the HumanPlayer class
 */
public class HumanPlayerTest {

  Player player;
  Appendable output;
  Readable input;
  Map<ShipType, Integer> map;
  Board ownBoard;
  Board oppBoard;

  /**
   * Initializes the test data (also serves to test the constructor)
   */
  @BeforeEach
  public void initData() {
    String userInput = """
        0 0
        1 1
        6 10
        0 0 1 1 2 s 3 3
        0 0 1 1 2 2 3 3 4
        0 0
        1 1
        1 1
        0 0 1 1 2 2 3 3
        """;

    output = new StringBuilder();
    input = new StringReader(userInput);
    ownBoard = new Board();
    oppBoard = new Board();

    View view = new GameView(output, input);
    this.player = new HumanPlayer(ownBoard, oppBoard, new Random(1), view);

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
    assertNull(this.player.name());
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
    List<Ship> actualFull = this.player.setup(6, 6, map);

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
    assertTrue(output.toString().isEmpty());

    // tests to ensure that takeShots() only takes valid input, and
    // handles multiple different kinds of invalid inputs properly until
    // receiving an input of valid shots
    String expectedOutput = """
        -------------------------------------------------------------------------------
        Opponent Board Data:
             - - - - - -
             - - - - - -
             - - - - - -
             - - - - - -
             - - - - - -
             - - - - - -
                
        Your Board:
             B B B B B -
             C C C C C C
             - - D D D D
             - - - - - -
             - - - - - -
             - S S S - -
                
        Please enter 4 distinct shots:
        ------------------------------------------------------------------
        ------------------------------------------------------------------
        Uh Oh! You've entered an invalid shot (exceeds board dimensions or too many inputs).
        Please enter 4 distinct shots:
        ------------------------------------------------------------------
        ------------------------------------------------------------------
        Uh Oh! You've entered invalid inputs (non-integer character or duplicate shot).
        Please enter 4 distinct shots:
        ------------------------------------------------------------------
        ------------------------------------------------------------------
        Uh Oh! You've entered an invalid shot (exceeds board dimensions or too many inputs).
        Please enter 4 distinct shots:
        ------------------------------------------------------------------
        ------------------------------------------------------------------
        Uh Oh! You've entered invalid inputs (non-integer character or duplicate shot).
        Please enter 4 distinct shots:
        ------------------------------------------------------------------
        """;
    List<Coord> shots = List.of(new Coord(0, 0), new Coord(1, 1), new Coord(2, 2), new Coord(3, 3));

    this.player.setup(6, 6, map);

    assertEquals(shots, this.player.takeShots());
    assertEquals(expectedOutput, output.toString());

    // tests to ensure that misses are preemptively (before being confirmed
    // by other methods) marked as misses
    String oppBoardVisual = """
             O - - - - -
             - O - - - -
             - - O - - -
             - - - O - -
             - - - - - -
             - - - - - -
             
        """;

    assertEquals(oppBoardVisual, this.oppBoard.render(false));
  }

  /**
   * Tests the .reportDamage(...) method
   */
  @Test
  public void reportDamage() {
    this.player.setup(6, 6, map);

    // tests that reportDamage returns the list of Coords (shots), out of the given
    // list of Coords (shots), that hit this HumanPlayer's ships
    List<Coord> shots = List.of(new Coord(0, 3), new Coord(1, 5), new Coord(2, 5), new Coord(3, 5));
    List<Coord> hits = List.of(new Coord(1, 5), new Coord(2, 5), new Coord(3, 5));
    assertEquals(hits, this.player.reportDamage(shots));

    // tests that the number of afloat ships is updated properly
    assertEquals(3, this.ownBoard.numShips());

    // tests that this HumanPlayer's board visual is updated properly
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
    this.player.setup(6, 6, map);

    List<Coord> hits = List.of(new Coord(0, 0), new Coord(1, 1));

    // tests to ensure that shots that hit the opponent's ship get marked accordingly
    // on this HumanPlayer's Board of known information about their opponent's board state
    this.player.successfulHits(hits);

    String oppBoardVisual = """
             X - - - - -
             - X - - - -
             - - - - - -
             - - - - - -
             - - - - - -
             - - - - - -
             
        """;

    assertEquals(oppBoardVisual, this.oppBoard.render(false));
  }

  /**
   * Tests the .endGame(...) method
   */
  @Test
  public void endGame() {
    this.player.setup(6, 6, map);

    String end = """
        -------------------------------------------------------------------------------
        Opponent Board Data:
             - - - - - -
             - - - - - -
             - - - - - -
             - - - - - -
             - - - - - -
             - - - - - -
             
        Your Board:
             B B B B B -
             C C C C C C
             - - D D D D
             - - - - - -
             - - - - - -
             - S S S - -
             
        """;

    this.player.endGame(GameResult.WIN, "");
    assertEquals(end + "Player 1, you WIN\n", this.output.toString());

    initData();
    this.player.setup(6, 6, map);

    this.player.endGame(GameResult.LOSE, "");
    assertEquals(end + "Player 1, you LOSE\n", this.output.toString());

    initData();
    this.player.setup(6, 6, map);

    this.player.endGame(GameResult.DRAW, "");
    assertEquals(end + "Player 1, you DRAW\n", this.output.toString());




  }
}