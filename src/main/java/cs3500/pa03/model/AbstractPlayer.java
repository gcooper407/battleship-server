package cs3500.pa03.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Represents an abstract Player, for abstracting duplicate code across implementations of
 * the Player interface.
 */
public abstract class AbstractPlayer implements Player {

  protected final Board ownBoard;
  protected final Board oppBoard;
  protected final Random rand;

  /**
   * Creates an abstract Player with the first given Board as its own Board and the second given
   * Board as its opponent's Board, and well as the given Random object, and 0 hits left to win
   *
   * @param own this AbstractPlayer's own board
   * @param opp this AbstractPlayer's opponent's board (to contain only information this
   *            AbstractPlayer knows about its opponent's board state)
   * @param rand Random object to store in this AbstractPlayer
   */
  public AbstractPlayer(Board own, Board opp, Random rand) {
    this.ownBoard = own;
    this.oppBoard = opp;
    this.rand = rand;
  }

  /**
   * Creates an abstract Player with the given Board as its own Board and an empty Board as its
   * opponent's Board, and well as the given Random object, and 0 hits left to win
   *
   * @param own this AbstractPlayer's own board
   * @param rand Random object to store in this AbstractPlayer
   */
  public AbstractPlayer(Board own, Random rand) {
    this(own, new Board(), rand);
  }


  /**
   * Get the player's name.
   *
   * @return the player's name
   */
  @Override
  public String name() {
    return null;
  }

  /**
   * Given the specifications for a BattleSalvo board, return a list of ships with their locations
   * on the board.
   *
   * @param height         the height of the board, range: [6, 15] inclusive
   * @param width          the width of the board, range: [6, 15] inclusive
   * @param specifications a map of ship type to the number of occurrences each ship should
   *                       appear on the board
   * @return the placements of each ship on the board
   */
  @Override
  public List<Ship> setup(int height, int width, Map<ShipType, Integer> specifications) {
    List<Ship> placements = new ArrayList<>();
    this.ownBoard.initBoard(height, width);
    this.oppBoard.initBoard(height, width);

    // get array of ShipTypes (already ordered by size (largest to smallest))
    ShipType[] ships = ShipType.values();

    for (ShipType s : ships) { // for each type of ship...
      int shipsPlaced = 0; // the initial number of ships (of this ShipType) is 0

      // while not all ships of this type have been placed...
      while (shipsPlaced < specifications.get(s)) {
        // pick a random orientation
        ShipOrientation dir = rand.nextBoolean()
            ? ShipOrientation.HORIZONTAL : ShipOrientation.VERTICAL;

        // based on the orientation, confine the bounds for placement of origin point
        int xmax = dir == ShipOrientation.HORIZONTAL ? (width - s.getSize() + 1) : width;
        int ymax = dir == ShipOrientation.VERTICAL ? (height - s.getSize() + 1) : height;

        // pick a random point in the bounds
        Coord point = new Coord(rand.nextInt(xmax), rand.nextInt(ymax));

        // create a new Ship object of the type s with random origin point and random orientation
        Ship ship = new Ship(s, point, dir);

        // if the ship can't be placed on the board (due to overlap) re-loop to find new placement
        if (!this.ownBoard.canPlace(ship)) {
          continue;
        }

        // add this ship to the board
        this.ownBoard.addToFleet(ship);
        placements.add(ship);
        shipsPlaced += 1;
      }
    }

    return placements;
  }

  /**
   * Returns this player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public abstract List<Coord> takeShots();

  /**
   * Given the list of shots the opponent has fired on this player's board, report which
   * shots hit a ship on this player's board.
   *
   * @param opponentShotsOnBoard the opponent's shots on this player's board
   * @return a filtered list of the given shots that contain all locations of shots that hit a
   *         ship on this board
   */
  @Override
  public List<Coord> reportDamage(List<Coord> opponentShotsOnBoard) {
    return this.ownBoard.processShots(opponentShotsOnBoard);
  }

  /**
   * Reports to this player what shots in their previous volley returned from takeShots()
   * successfully hit an opponent's ship.
   *
   * @param shotsThatHitOpponentShips the list of shots that successfully hit the opponent's ships
   */
  @Override
  public abstract void successfulHits(List<Coord> shotsThatHitOpponentShips);

  /**
   * Notifies the player that the game is over.
   * Win, lose, and draw should all be supported
   *
   * @param result if the player has won, lost, or forced a draw
   * @param reason the reason for the game ending
   */
  @Override
  public void endGame(GameResult result, String reason) {
    System.out.println(result.toString() + reason);
  }

}
