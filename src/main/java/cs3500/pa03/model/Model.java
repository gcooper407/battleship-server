package cs3500.pa03.model;

/**
 * An interface for a model that stores BattleSalvo board game data.
 */
public interface Model {

  /**
   * Sets either Player 1's Board (if given boolean p1 is true) or Player 2's board (if given
   * boolean p1 is false) to the given Board
   *
   * @param board Board object to assign either Player 1's or Player 2's board to
   * @param p1 true to assign Player 1's Board to the given board, false to assign
   *           Player 2's Board to the given board
   */
  void setBoard(Board board, boolean p1);

  /**
   * Determines whether the BattleSalvo game is over.
   *
   * @return true if the BattleSalvo game is over.
   */
  boolean isOver();

  /**
   * Determines whether Player 1 has won the BattleSalvo game, by sinking all of Player 2's ships
   * without having all of its own ships sunk
   *
   * @return true if Player 1 has won the BattleSalvo game
   */
  boolean p1Wins();

  /**
   * Determines whether Player 2 has won the BattleSalvo game, by sinking all of Player 1's ships
   * without having all of its own ships sunk
   *
   * @return true if Player 2 has won the BattleSalvo game
   */
  boolean p2Wins();

  /**
   * Determines whether the BattleSalvo game has ended in a draw, where Player 1 sank all of
   * Player 2's ships at the same time that Player 2 sank all of Player 1's ships
   *
   * @return true if the BattleSalvo game has ended in a draw
   */
  boolean isDraw();

}

