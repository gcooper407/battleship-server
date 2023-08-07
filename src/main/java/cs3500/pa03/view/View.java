package cs3500.pa03.view;

import cs3500.pa03.model.Coord;
import cs3500.pa03.model.GameResult;
import cs3500.pa03.model.ShipType;
import java.util.List;
import java.util.Map;

/**
 * An interface for a view of a BattleSalvo game, outlining basic expectations regarding
 * functionalities for displaying output to the user and collecting user input.
 */
public interface View {

  /**
   * Prompts the user for input for dimensions of the game board they'd like to play with, and
   * validates the input based on given bounds.
   *
   * @param min minimum bound for user input
   * @param max maximum bound for user input
   * @return a Coordinate object representing the bottom right corner of the board (in other words,
   *         whose dimensions represent the bounds of the game board)
   */
  Coord displaySizeQuery(int min, int max);

  /**
   * Prompts the user to input the number of ships they'd like to play with, and validates
   * the input based on the given bound.
   *
   * @param max maximum bound on the sum of the user inputs (max number of ships total)
   * @return a map of ship type to the number of occurrences each ship should appear on the board
   */
  Map<ShipType, Integer> displayFleetQuery(int max);

  /**
   * Displays two boards (the user's own board and their opponent's board) given their
   * String representations.
   *
   * @param own String representation of the user's own Board
   * @param opp String representation of the user's opponent's Board
   */
  void displayBoards(String own, String opp);

  /**
   * Prompts the user to input as many shots as is defined by the given value.
   *
   * @param firstTry false if the user is being prompted for re-input, after previously
   *                 providing a shot they've already taken; true if the user is prompted
   *                 for the first time (this round) for shots
   * @param numShots the number of shots the user must enter
   * @param xmax the maximum bound on the first user input, which corresponds to the xpos-value
   *             of a desired shot
   * @param ymax the maximum bound on the second user input, which corresponds to the y-value
   *             of a desired shot
   * @return a list of Coordinate objects representing the location of the user's shots
   */
  List<Coord> displaySalvoQuery(boolean firstTry, int numShots, int xmax, int ymax);

  /**
   * Displays an end message to the user based on the outcome of the BattleSalvo game
   *
   * @param result GameResult representing the outcome of the game
   */
  void displayEndMessage(GameResult result);

  /**
   * Adds the given message to this View's source of output
   *
   * @param msg String message to add to the output
   */
  void addToOutput(String msg);

}
