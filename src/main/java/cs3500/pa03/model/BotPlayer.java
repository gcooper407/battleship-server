package cs3500.pa03.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Represents a Player controller by a bot or AI.
 */
public class BotPlayer extends AbstractPlayer {

  private final List<Coord> shotQueue;
  private final List<Coord> shotHistory;
  private Map<ShipType, Integer> specs;

  /**
   * Creates a BotPlayer with its own (given) board, an (given) opponent's board (to contain only
   * information known to this BotPlayer about their opponent's own board), a given Random object,
   * and an empty shot queue to take shots from
   *
   * @param own this BotPlayer's board
   * @param opp this BotPlayer's opponent's board (to contain only information known to this
   *            BotPlayer about their opponent's board)
   * @param rand Random object to store in this BotPlayer
   */
  public BotPlayer(Board own, Board opp, Random rand) {
    super(own, opp, rand);
    this.shotQueue = new ArrayList<>();
    this.shotHistory = new ArrayList<>();
  }

  /**
   * Creates a BotPlayer with its own given board, an empty opponent's board (to contain only
   * information known to this BotPlayer about their opponent's board), a given Random object, and
   * an empty shot queue to take shots from
   *
   * @param own this BotPlayer's board
   * @param rand Random object to store in this BotPlayer
   */
  public BotPlayer(Board own, Random rand) {
    this(own, new Board(), rand);
  }


  /**
   * Returns this player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk OR the number of empty
   * coordinates remaining on the opponent's board, whichever is smaller.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public List<Coord> takeShots() {

    int numShots = Math.min(this.oppBoard.numRemainingEmpty(), this.ownBoard.numShips());

    int xmax = this.oppBoard.getBoard()[0].length;
    int ymax = this.oppBoard.getBoard().length;

    List<Coord> shots = new ArrayList<>();

    while (!this.shotQueue.isEmpty() && numShots != 0) {
      shots.add(this.shotQueue.remove(0));
      numShots -= 1;
    }

    while (numShots != 0) {
      Coord c = new Coord(rand.nextInt(xmax), rand.nextInt(ymax));
      if (!this.shotHistory.contains(c) && !this.shotQueue.contains(c) && !shots.contains(c)) {
        shots.add(c);
        numShots -= 1;
      }
    }

    this.shotHistory.addAll(shots);

    return shots;
  }


  /**
   * Reports to this player what shots in their previous volley returned from takeShots()
   * successfully hit an opponent's ship.
   *
   * @param shotsThatHitOpponentShips the list of shots that successfully hit the opponent's ships
   */
  @Override
  public void successfulHits(List<Coord> shotsThatHitOpponentShips) {
    this.oppBoard.displaySuccessfulHits(shotsThatHitOpponentShips);

    for (Coord shot : shotsThatHitOpponentShips) {
      Coord above = new Coord(shot.xpos(), shot.ypos() - 1);
      this.addToQueueIfValid(above);

      Coord below = new Coord(shot.xpos(), shot.ypos() + 1);
      this.addToQueueIfValid(below);

      Coord left = new Coord(shot.xpos() - 1, shot.ypos());
      this.addToQueueIfValid(left);

      Coord right = new Coord(shot.xpos() + 1, shot.ypos());
      this.addToQueueIfValid(right);
    }
  }

  /**
   * Adds the given Coordinate to this BotPlayer's shot queue if the given Coord
   * is a valid shot
   *
   * @param coord Coordinate shot to add to the shot queue if valid
   */
  private void addToQueueIfValid(Coord coord) {
    if (this.oppBoard.isValidShot(coord) && !this.shotQueue.contains(coord)
        && !this.shotHistory.contains(coord)) {
      this.shotQueue.add(coord);
    }
  }

}
