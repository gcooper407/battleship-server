package cs3500.pa03.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
 * Tests the methods and constructor of the GameModel class
 */
public class GameModelTest {

  GameModel model;
  Board p1Board;
  Board p2Board;

  /**
   * Initializes the test data (which serves to also test the constructor)
   */
  @BeforeEach
  public void initData() {
    this.model = new GameModel();

    this.p1Board = new Board();
    this.p2Board = new Board();
    this.p1Board.initBoard(6, 6);
    this.p2Board.initBoard(6, 6);
  }

  /**
   * Tests the .setBoard() method
   */
  @Test
  public void setBoard() {
    // initially, both boards in model are set to null
    assertThrows(NullPointerException.class, () -> this.model.p1Wins());
    assertThrows(NullPointerException.class, () -> this.model.p2Wins());
    assertThrows(NullPointerException.class, () -> this.model.isDraw());
    // set one to a non-null board
    this.model.setBoard(this.p2Board, false);

    // however, the other board is still set to null
    assertThrows(NullPointerException.class, () -> this.model.p1Wins());
    assertThrows(NullPointerException.class, () -> this.model.p2Wins());
    assertThrows(NullPointerException.class, () -> this.model.isDraw());

    // set the other board to a non-null board
    this.model.setBoard(this.p1Board, true);
    // now, that board is no longer null as well
    assertDoesNotThrow(() -> this.model.p1Wins());
    assertDoesNotThrow(() -> this.model.p2Wins());
    assertDoesNotThrow(() -> this.model.isDraw());
  }

  /**
   * Tests the .isOver() method
   */
  @Test
  public void isOver() {
    // sets each Board in the model to a non-null Board
    this.model.setBoard(this.p1Board, true);
    this.model.setBoard(this.p2Board, false);

    // initially, both Boards have no ships in their fleet -- when this is the case,
    // the game would be over, ending in a draw.
    assertTrue(this.model.isOver());

    // add a ship to the fleet of Player 1's board
    this.p1Board.addToFleet(new Ship(ShipType.SUBMARINE, new Coord(0, 0),
        ShipOrientation.VERTICAL));

    // now, Player 2's Board has no ships in its fleet while Player 1's has one ship in its
    // fleet -- since one Board has an empty fleet, the game would still be over
    assertTrue(this.model.isOver());

    // add a ship to the fleet of Player 2's board
    this.p2Board.addToFleet(new Ship(ShipType.DESTROYER, new Coord(1, 4),
        ShipOrientation.HORIZONTAL));

    // now, both Boards have at least one ship in their fleets -- in this case,
    // the game would NOT be over
    assertFalse(this.model.isOver());

    // take shots such that Player 1's fleet is reduced to empty again
    this.p1Board.processShots(List.of(new Coord(0, 0), new Coord(0, 1), new Coord(0, 2)));

    // now, Player 1's Board has an empty fleet and Player 2's Board has a non-empty fleet -- in
    // this case, the game would be over
    assertTrue(this.model.isOver());
  }

  /**
   * Tests the .p1Wins() method
   */
  @Test
  public void p1Wins() {
    // sets each Board in the model to a non-null Board
    this.model.setBoard(this.p1Board, true);
    this.model.setBoard(this.p2Board, false);

    // initially, both Boards have no ships in their fleet -- so, player 1 does not win
    assertFalse(this.model.p1Wins());

    // add a ship to the fleet of Player 1's board
    this.p1Board.addToFleet(new Ship(ShipType.SUBMARINE, new Coord(0, 0),
        ShipOrientation.VERTICAL));

    // now, since Player 2's Board has an empty fleet and Player 1's Board has a non-empty fleet,
    // Player 1 would win
    assertTrue(this.model.p1Wins());

    // add a ship to the fleet of Player 2's board
    this.p2Board.addToFleet(new Ship(ShipType.DESTROYER, new Coord(1, 4),
        ShipOrientation.HORIZONTAL));

    // now, since both Player's Boards have at least one ship in their fleets,
    // Player 1 would not win
    assertFalse(this.model.p1Wins());
  }

  /**
   * Tests the .p2Wins() method
   */
  @Test
  public void p2Wins() {
    // sets each Board in the model to a non-null Board
    this.model.setBoard(this.p1Board, true);
    this.model.setBoard(this.p2Board, false);

    // initially, both Boards have no ships in their fleet -- so, player 2 does not win
    assertFalse(this.model.p2Wins());

    // add a ship to the fleet of Player 2's board
    this.p2Board.addToFleet(new Ship(ShipType.SUBMARINE, new Coord(0, 0),
        ShipOrientation.VERTICAL));

    // now, since Player 1's Board has an empty fleet and Player 2's Board has a non-empty fleet,
    // Player 2 would win
    assertTrue(this.model.p2Wins());

    // add a ship to the fleet of Player 1's board
    this.p1Board.addToFleet(new Ship(ShipType.DESTROYER, new Coord(1, 4),
        ShipOrientation.HORIZONTAL));

    // now, since both Player's Boards have at least one ship in their fleets,
    // Player 2 would not win
    assertFalse(this.model.p2Wins());
  }

  /**
   * Tests the .isDraw() method
   */
  @Test
  public void isDraw() {
    // sets each Board in the model to a non-null Board
    this.model.setBoard(this.p1Board, true);
    this.model.setBoard(this.p2Board, false);

    // initially, both Boards have no ships in their fleet -- so, there is a draw
    assertTrue(this.model.isDraw());

    // add a ship to the fleet of Player 1's board
    this.p1Board.addToFleet(new Ship(ShipType.SUBMARINE, new Coord(0, 0),
        ShipOrientation.VERTICAL));

    // now, since Player 2's Board has an empty fleet and Player 1's Board has a non-empty fleet,
    // there would be no draw
    assertFalse(this.model.isDraw());

    // add a ship to the fleet of Player 2's board
    this.p2Board.addToFleet(new Ship(ShipType.DESTROYER, new Coord(1, 4),
        ShipOrientation.HORIZONTAL));

    // now, since both Player's Boards have at least one ship in their fleets,
    // there would still be no draw
    assertFalse(this.model.isDraw());

    // take shots such that Player 1's fleet is reduced to empty again
    this.p1Board.processShots(List.of(new Coord(0, 0), new Coord(0, 1), new Coord(0, 2)));

    // now, since Player 1's Board has an empty fleet and Player 2's Board has a non-empty fleet,
    // there would still be no draw
    assertFalse(this.model.isDraw());
  }
}