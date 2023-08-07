package cs3500.pa03.model;


/**
 * Represents a model object that manages BattleSalvo board data across two players.
 */
public class GameModel implements Model {

  public static final int MIN_BOARD_DIMENSION = 6; // represents the minimum dimension of a Board
  public static final int MAX_BOARD_DIMENSION = 15; // represents the maximum dimension of a Board
  private Board p1Board;
  private Board p2Board;

  /**
   * Creates a model object to store board data of a BattleSalvo game between two players,
   * with both players' boards initialized to null.
   */
  public GameModel() {
    this.p1Board = null;
    this.p2Board = null;
  }


  /**
   * Sets either Player 1's Board (if given boolean p1 is true) or Player 2's board (if given
   * boolean p1 is false) to the given Board
   *
   * @param board Board object to assign either Player 1's or Player 2's board to
   * @param p1 true to assign Player 1's Board to the given board, false to assign
   *           Player 2's Board to the given board
   */
  public void setBoard(Board board, boolean p1) {
    if (p1) {
      this.p1Board = board;
    } else {
      this.p2Board = board;
    }
  }


  /**
   * Determines whether the BattleSalvo game is over.
   *
   * @return true if the BattleSalvo game is over.
   */
  @Override
  public boolean isOver() {
    return this.p1Wins() || this.p2Wins() || this.isDraw();
  }

  /**
   * Determines whether Player 1 has won the BattleSalvo game, by sinking all of Player 2's ships
   * without having all of its own ships sunk
   *
   * @return true if Player 1 has won the BattleSalvo game
   */
  @Override
  public boolean p1Wins() {
    return this.p2Board.numShips() == 0 && this.p1Board.numShips() > 0;
  }

  /**
   * Determines whether Player 2 has won the BattleSalvo game, by sinking all of Player 1's ships
   * without having all of its own ships sunk
   *
   * @return true if Player 2 has won the BattleSalvo game
   */
  @Override
  public boolean p2Wins() {
    return this.p1Board.numShips() == 0 && this.p2Board.numShips() > 0;
  }

  /**
   * Determines whether the BattleSalvo game has ended in a draw, where Player 1 sank all of
   * Player 2's ships at the same time that Player 2 sank all of Player 1's ships
   *
   * @return true if the BattleSalvo game has ended in a draw
   */
  @Override
  public boolean isDraw() {
    return this.p1Board.numShips() == 0 && this.p2Board.numShips() == 0;
  }

}

