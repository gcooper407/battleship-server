package cs3500.pa03.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the constructor and methods of the Board class
 */
public class BoardTest {

  Board board;

  /**
   * Initializes testing data (sample Board) (and thus tests the constructor)
   */
  @BeforeEach
  public void initData() {
    this.board = new Board();
  }

  /**
   * Tests the .initBoard(...) method
   */
  @Test
  public void initBoard() {
    assertNull(this.board.getBoard());
    assertEquals(0, this.board.numShips());

    this.board.initBoard(6, 6);

    assertEquals(6, this.board.getBoard().length);
    assertEquals(6, this.board.getBoard()[0].length);
    assertEquals(0, this.board.numShips());
  }

  /**
   * Tests the .getBoard() method
   */
  @Test
  public void getBoard() {
    this.board.initBoard(6, 6);

    BoardElement[][] expected = new BoardElement[6][6];
    for (BoardElement[] row : expected) {
      Arrays.fill(row, BoardElement.EMPTY);
    }

    assertArrayEquals(expected, this.board.getBoard());

    this.board.setElementAt(new Coord(0, 3), BoardElement.HIT);
    this.board.setElementAt(new Coord(1, 3), BoardElement.HIT);
    this.board.setElementAt(new Coord(2, 3), BoardElement.HIT);
    this.board.setElementAt(new Coord(3, 3), BoardElement.HIT);
    this.board.setElementAt(new Coord(4, 3), BoardElement.HIT);
    this.board.setElementAt(new Coord(5, 3), BoardElement.HIT);

    Arrays.fill(expected[3], BoardElement.HIT);

    assertArrayEquals(expected, this.board.getBoard());

  }

  /**
   * Tests the .getElementAt(...) method
   */
  @Test
  public void getElementAt() {
    this.board.initBoard(6, 6);

    Coord topLeft = new Coord(0, 0);

    assertEquals(BoardElement.EMPTY, this.board.getElementAt(topLeft));
    this.board.setElementAt(topLeft, BoardElement.HIT);
    assertEquals(BoardElement.HIT, this.board.getElementAt(topLeft));
  }

  /**
   * Tests the .setElementAt(...) method
   */
  @Test
  public void setElementAt() {
    this.board.initBoard(6, 6);

    Coord bottomRight = new Coord(5, 5);

    assertEquals(BoardElement.EMPTY, this.board.getElementAt(bottomRight));
    this.board.setElementAt(bottomRight, BoardElement.HIT);
    assertEquals(BoardElement.HIT, this.board.getElementAt(bottomRight));
  }

  /**
   * Tests the .render(...) method
   */
  @Test
  public void render() {
    this.board.initBoard(6, 6);

    this.board.setElementAt(new Coord(0, 0), BoardElement.HIT);
    this.board.setElementAt(new Coord(1, 1), BoardElement.MISS);
    this.board.setElementAt(new Coord(3, 3), BoardElement.SUBMARINE);

    String full = """
             X - - - - -
             - O - - - -
             - - - - - -
             - - - S - -
             - - - - - -
             - - - - - -
             
        """;
    String hidden = """
             X - - - - -
             - O - - - -
             - - - - - -
             - - - - - -
             - - - - - -
             - - - - - -
             
        """;

    assertEquals(full, this.board.render(true));
    assertEquals(hidden, this.board.render(false));
  }

  /**
   * Tests the .canPlace(...) method
   */
  @Test
  public void canPlace() {
    this.board.initBoard(6, 6);

    this.board.addToFleet(new Ship(ShipType.CARRIER, new Coord(0, 0), ShipOrientation.HORIZONTAL));
    this.board.addToFleet(new Ship(ShipType.DESTROYER, new Coord(2, 1), ShipOrientation.VERTICAL));

    Ship valid = new Ship(ShipType.SUBMARINE, new Coord(3, 2), ShipOrientation.VERTICAL);
    Ship invalid = new Ship(ShipType.CARRIER, new Coord(0, 4), ShipOrientation.HORIZONTAL);

    assertTrue(this.board.canPlace(valid));
    assertFalse(this.board.canPlace(invalid));
  }

  /**
   * Tests the .addToFleet(...) method
   */
  @Test
  public void addToFleet() {
    this.board.initBoard(6, 6);

    String visual = """
             - - - - - -
             - - - - - -
             - - - - - -
             - - - - - -
             - - - - - -
             - - - - - -
             
        """;

    assertEquals(visual, this.board.render(true));
    assertEquals(0, this.board.numShips());

    this.board.addToFleet(new Ship(ShipType.CARRIER, new Coord(0, 0), ShipOrientation.HORIZONTAL));
    this.board.addToFleet(new Ship(ShipType.DESTROYER, new Coord(2, 1), ShipOrientation.VERTICAL));

    assertEquals(2, this.board.numShips());

    visual = """
             C C C C C C
             - - D - - -
             - - D - - -
             - - D - - -
             - - D - - -
             - - - - - -
             
        """;

    assertEquals(visual, this.board.render(true));

  }

  /**
   * Tests the .numShips() method
   */
  @Test
  public void numShips() {
    this.board.initBoard(6, 6);

    assertEquals(0, this.board.numShips());

    // adding 2 ships to the fleet increases numShips by 2
    this.board.addToFleet(new Ship(ShipType.CARRIER, new Coord(0, 0), ShipOrientation.HORIZONTAL));
    this.board.addToFleet(new Ship(ShipType.DESTROYER, new Coord(2, 1), ShipOrientation.VERTICAL));

    assertEquals(2, this.board.numShips());

    // sinking a ship in the fleet decreases numShips by 1, down to 1
    List<Coord> shots = List.of(new Coord(2, 1), new Coord(2, 2), new Coord(2, 3), new Coord(2, 4));
    this.board.processShots(shots);

    assertEquals(1, this.board.numShips());
  }

  /**
   * Tests the .numRemainingEmpty() method
   */
  @Test
  public void numRemainingEmpty() {
    this.board.initBoard(6, 6);

    // 6 xpos 6 board initially has 36 empty spaces
    assertEquals(36, this.board.numRemainingEmpty());

    // adding two ships to the Board, sizes 6 and 4 respectively, means
    // there are 10 fewer empty spaces
    this.board.addToFleet(new Ship(ShipType.CARRIER, new Coord(0, 0), ShipOrientation.HORIZONTAL));
    this.board.addToFleet(new Ship(ShipType.DESTROYER, new Coord(2, 1), ShipOrientation.VERTICAL));

    assertEquals(26, this.board.numRemainingEmpty());

    // one hit and 3 misses adds 3 new spots to the Board, meaning 3 fewer empty spaces
    List<Coord> shots = List.of(new Coord(2, 1), new Coord(5, 5), new Coord(4, 5), new Coord(3, 5));
    this.board.processShots(shots);

    assertEquals(23, this.board.numRemainingEmpty());
  }

  /**
   * Tests the .isValidShot(...) method
   */
  @Test
  public void isValidShot() {
    this.board.initBoard(6, 6);

    this.board.addToFleet(new Ship(ShipType.CARRIER, new Coord(0, 0), ShipOrientation.HORIZONTAL));

    // before any hits or misses, all shots (within bounds) are valid
    assertTrue(this.board.isValidShot(new Coord(0, 0)));
    assertTrue(this.board.isValidShot(new Coord(1, 1)));

    List<Coord> shots = List.of(new Coord(0, 0), new Coord(1, 1));
    this.board.processShots(shots);

    // after shots, those Coordinates with hits and misses are no
    // longer valid shots
    assertFalse(this.board.isValidShot(new Coord(0, 0)));
    assertFalse(this.board.isValidShot(new Coord(1, 1)));

    // shots outside bounds are not valid
    assertFalse(this.board.isValidShot(new Coord(10, 0)));
    assertFalse(this.board.isValidShot(new Coord(0, 10)));
    assertFalse(this.board.isValidShot(new Coord(-10, 0)));
    assertFalse(this.board.isValidShot(new Coord(0, -10)));
  }

  /**
   * Tests the .allValidShots(...) method
   */
  @Test
  public void allValidShots() {
    this.board.initBoard(6, 6);

    this.board.addToFleet(new Ship(ShipType.CARRIER, new Coord(0, 0), ShipOrientation.HORIZONTAL));

    List<Coord> shots = List.of(new Coord(0, 0), new Coord(1, 1));

    // shots on spots without hits/misses are valid
    assertTrue(this.board.allValidShots(shots));
    this.board.processShots(shots);
    // shots on spots with hits/misses are invalid
    assertFalse(this.board.allValidShots(shots));

    // for a list of shots where one is inValid, not all shots
    // in the list are valid
    List<Coord> oneInvalid = List.of(new Coord(0, 0), new Coord(2, 2), new Coord(3, 3));
    assertFalse(this.board.allValidShots(oneInvalid));
  }

  /**
   * Tests the .processShots(...) method
   */
  @Test
  public void processShots() {
    this.board.initBoard(6, 6);

    Coord origin = new Coord(0, 0);
    Coord oneOne = new Coord(1, 1);

    this.board.addToFleet(new Ship(ShipType.CARRIER, origin, ShipOrientation.HORIZONTAL));

    // before the following two shots are processed, they are valid
    assertTrue(this.board.isValidShot(origin));
    assertTrue(this.board.isValidShot(oneOne));

    assertEquals(BoardElement.CARRIER, this.board.getElementAt(origin));
    assertEquals(BoardElement.EMPTY, this.board.getElementAt(oneOne));

    // tests to ensure that .processShots(List<Coord>) returns a List<Coord>
    // containing all the Coords of shots that hit ships
    List<Coord> shots = List.of(origin, oneOne);
    List<Coord> hits = List.of(origin);
    assertEquals(hits, this.board.processShots(shots));

    // tests that the visual representation of the Board is properly updated
    // after processing shots
    assertEquals(BoardElement.HIT, this.board.getElementAt(origin));
    assertEquals(BoardElement.MISS, this.board.getElementAt(oneOne));

    // after processing, the same shots are no longer valid
    // as they are in spots containing hits/misses
    assertFalse(this.board.isValidShot(origin));
    assertFalse(this.board.isValidShot(oneOne));
  }

  /**
   * Tests the .displaySuccessfulHits(...) method
   */
  @Test
  public void displaySuccessfulHits() {
    this.board.initBoard(6, 6);

    Coord origin = new Coord(0, 0);
    Coord oneOne = new Coord(1, 1);

    this.board.addToFleet(new Ship(ShipType.CARRIER, origin, ShipOrientation.HORIZONTAL));

    assertEquals(BoardElement.CARRIER, this.board.getElementAt(origin));
    assertEquals(BoardElement.EMPTY, this.board.getElementAt(oneOne));

    List<Coord> hits = List.of(origin, oneOne);
    this.board.displaySuccessfulHits(hits);

    assertEquals(BoardElement.HIT, this.board.getElementAt(origin));
    assertEquals(BoardElement.HIT, this.board.getElementAt(oneOne));
  }

  /**
   * Tests the .markAsMiss(...) method
   */
  @Test
  public void markAsMiss() {
    this.board.initBoard(6, 6);

    this.board.initBoard(6, 6);

    Coord origin = new Coord(0, 0);
    Coord oneOne = new Coord(1, 1);

    this.board.addToFleet(new Ship(ShipType.CARRIER, origin, ShipOrientation.HORIZONTAL));

    assertEquals(BoardElement.CARRIER, this.board.getElementAt(origin));
    assertEquals(BoardElement.EMPTY, this.board.getElementAt(oneOne));

    List<Coord> misses = List.of(origin, oneOne);
    this.board.markAsMiss(misses);

    assertEquals(BoardElement.MISS, this.board.getElementAt(origin));
    assertEquals(BoardElement.MISS, this.board.getElementAt(oneOne));
  }
}