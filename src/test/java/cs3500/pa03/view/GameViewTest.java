package cs3500.pa03.view;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.model.Coord;
import cs3500.pa03.model.GameModel;
import cs3500.pa03.model.GameResult;
import cs3500.pa03.model.ShipType;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the constructor and methods of the GameView class
 */
public class GameViewTest {

  View view;
  Appendable output;
  Readable noInput;

  /**
   * Initializes the test data (also serves to test the constructor)
   */
  @BeforeEach
  public void initData() {
    output = new StringBuilder();
    noInput = new StringReader("");

    view = new GameView(output, noInput);
  }

  /**
   * Tests the .displaySizeQuery(...) method
   */
  @Test
  public void displaySizeQuery() {
    String userSizeInput = """
        6 s
        10 20
        6 5 4
        6
        6
        """;

    Readable sizeInput = new StringReader(userSizeInput);
    view = new GameView(output, sizeInput);

    assertTrue(output.toString().isEmpty());
    assertEquals(new Coord(6, 6), this.view.displaySizeQuery(GameModel.MIN_BOARD_DIMENSION,
        GameModel.MAX_BOARD_DIMENSION));
    assertFalse(output.toString().isEmpty());

    String expected = """
        Hello! Welcome to the OOD BattleSalvo Game!
        Please enter a valid height and width below:
        ------------------------------------------------------
        ------------------------------------------------------------------------------
        Uh Oh! You've entered invalid inputs. Please remember that the height and
        width of the game must be two integers in the range (6, 15), inclusive. Try again!
        ------------------------------------------------------------------------------
        ------------------------------------------------------------------------------
        Uh Oh! You've entered invalid inputs. Please remember that the height and
        width of the game must be two integers in the range (6, 15), inclusive. Try again!
        ------------------------------------------------------------------------------
        ------------------------------------------------------------------------------
        Uh Oh! You've entered invalid inputs. Please remember that the height and
        width of the game must be two integers in the range (6, 15), inclusive. Try again!
        ------------------------------------------------------------------------------
        """;

    assertEquals(expected, output.toString());
  }

  /**
   * Tests the .displayFleetQuery(...) method
   */
  @Test
  public void displayFleetQuery() {
    String userFleetInput = """
        1 2 0 2
        1 2 3 4
        1
        3
        s
        1 1 1 1 4
        1 1 1 2
        """;

    Readable fleetInput = new StringReader(userFleetInput);
    view = new GameView(output, fleetInput);



    Map<ShipType, Integer> map = Map.of(ShipType.CARRIER, 1,
        ShipType.BATTLESHIP, 1, ShipType.DESTROYER, 1, ShipType.SUBMARINE, 2);

    assertTrue(output.toString().isEmpty());
    assertEquals(map, this.view.displayFleetQuery(6));
    assertFalse(output.toString().isEmpty());

    String expected = """
        -------------------------------------------------------------------------------
        Please enter your fleet in the order [Carrier, Battleship, Destroyer, Submarine].
        Remember, your fleet may not exceed size 6.
        -------------------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Uh Oh! You've entered invalid fleet sizes.
        Please enter your fleet in the order [Carrier, Battleship, Destroyer, Submarine].
        Remember, your fleet may not exceed size 6.
        -------------------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Uh Oh! You've entered invalid fleet sizes.
        Please enter your fleet in the order [Carrier, Battleship, Destroyer, Submarine].
        Remember, your fleet may not exceed size 6.
        -------------------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Uh Oh! You've entered invalid inputs.
        Please enter your fleet in the order [Carrier, Battleship, Destroyer, Submarine].
        Remember, your fleet may not exceed size 6.
        -------------------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Uh Oh! You've entered invalid fleet sizes.
        Please enter your fleet in the order [Carrier, Battleship, Destroyer, Submarine].
        Remember, your fleet may not exceed size 6.
        -------------------------------------------------------------------------------
        """;

    assertEquals(expected, output.toString());
  }

  /**
   * Tests the .displayBoard(...) method
   */
  @Test
  public void displayBoards() {
    String expected = """
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

    String opp = """
             - - - - - -
             - - - - - -
             - - - - - -
             - - - - - -
             - - - - - -
             - - - - - -
        
        """;

    String own = """
             B B B B B -
             C C C C C C
             - - D D D D
             - - - - - -
             - - - - - -
             - S S S - -
        
        """;

    this.view.displayBoards(own, opp);

    assertEquals(expected, output.toString());
  }

  /**
   * Tests the .displaySalvoQuery(...) method
   */
  @Test
  public void displaySalvoQuery() {
    String userShotInput = """
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

    Readable shotInput = new StringReader(userShotInput);
    view = new GameView(output, shotInput);

    List<Coord> shots = List.of(new Coord(0, 0), new Coord(1, 1), new Coord(2, 2), new Coord(3, 3));

    assertTrue(output.toString().isEmpty());
    assertEquals(shots, this.view.displaySalvoQuery(true, 4, 6, 6));
    assertFalse(output.toString().isEmpty());

    // tests to ensure that takeShots() only takes valid input, and
    // handles multiple different kinds of invalid inputs properly until
    // receiving an input of valid shots
    String expected = """
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

    assertEquals(expected, output.toString());
  }

  /**
   * Tests the .displayEndMessage(...) method
   */
  @Test
  public void displayEndMessage() {
    assertTrue(this.output.toString().isEmpty());
    this.view.displayEndMessage(GameResult.WIN);
    this.view.displayEndMessage(GameResult.LOSE);
    this.view.displayEndMessage(GameResult.DRAW);

    String expected = """
        Player 1, you WIN
        Player 1, you LOSE
        Player 1, you DRAW
        """;

    assertEquals(expected, this.output.toString());
  }

  /**
   * Tests the .addToOutput(...) method
   */
  @Test
  public void addToOutput() {
    assertTrue(this.output.toString().isEmpty());
    this.view.addToOutput("something");
    assertEquals("something", this.output.toString());

    Appendable mock = new MockAppendable();
    View viewMock = new GameView(mock, noInput);
    assertThrows(RuntimeException.class, () -> viewMock.addToOutput("anything"));
  }
}