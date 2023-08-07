package cs3500.pa03.model;

import cs3500.pa03.view.View;
import java.util.List;
import java.util.Random;

/**
 * Represents a Player controlled by a human user.
 */
public class HumanPlayer extends AbstractPlayer {

  private final View view;

  /**
   * Creates a HumanPlayer with the first given board as its own board and the second given board
   * as its opponent's board (to contain only information known to this HumanPlayer about their
   * opponent's board state), a View object (enables getting direct input from the Player), a
   * given Random object, and 0 hits left to win
   *
   * @param own this HumanPlayer's board
   * @param opp this HumanPlayer's opponent's board (to contain only information this
   *            HumanPlayer knows about its opponent's board state)
   * @param rand Random object to store in this HumanPlayer
   * @param view View object that allows for direct collection of user input
   */
  public HumanPlayer(Board own, Board opp, Random rand, View view) {
    super(own, opp, rand);
    this.view = view;
  }

  /**
   * Creates a HumanPlayer with its own board, an opponent's board (to contain only information
   * known to this HumanPlayer about their opponent's own board), a View object (enables
   * getting direct input), a given Random object, and 0 hits left to win
   *
   * @param own this HumanPlayer's board
   * @param rand Random object to store in this HumanPlayer
   * @param view View object that allows for direct collection of user input
   */
  public HumanPlayer(Board own, Random rand, View view) {
    this(own, new Board(), rand,  view);
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
    this.view.displayBoards(this.ownBoard.render(true), this.oppBoard.render(false));

    int xmax = this.oppBoard.getBoard()[0].length;
    int ymax = this.oppBoard.getBoard().length;

    int numShots = Math.min(this.oppBoard.numRemainingEmpty(), this.ownBoard.numShips());

    List<Coord> shots = this.view.displaySalvoQuery(true, numShots, xmax, ymax);

    while (!this.oppBoard.allValidShots(shots)) {
      shots = this.view.displaySalvoQuery(false, numShots, xmax, ymax);
    }

    this.oppBoard.markAsMiss(shots);

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
    // this.hitsLeftToWin -= shotsThatHitOpponentShips.size();
  }

  /**
   * Notifies the player that the game is over.
   * Win, lose, and draw should all be supported
   *
   * @param result if the player has won, lost, or forced a draw
   * @param reason the reason for the game ending
   */
  @Override
  public void endGame(GameResult result, String reason) {
    this.view.displayBoards(this.ownBoard.render(true), this.oppBoard.render(false));
    this.view.displayEndMessage(result);
  }

}
