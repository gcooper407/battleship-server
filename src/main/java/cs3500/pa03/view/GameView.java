package cs3500.pa03.view;

import cs3500.pa03.model.Coord;
import cs3500.pa03.model.GameResult;
import cs3500.pa03.model.ShipType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Represents a view of a BattleSalvo game, with functionalities for displaying output to
 * the user and collecting user input to save/use (elsewhere) as data.
 */
public class GameView implements View {

  private Appendable output;
  private Scanner sc;

  /**
   * Initializes a GameView object with a given source of output to append to and a given source
   * of input to scan user input from
   *
   * @param output source of output to append messages to
   * @param input source of input to scan user input from
   */
  public GameView(Appendable output, Readable input) {
    this.output = output;
    this.sc = new Scanner(input);
  }

  /**
   * Prompts the user for input for dimensions of the game board they'd like to play with, and
   * validates the input based on given bounds.
   *
   * @param min minimum bound for user input
   * @param max maximum bound for user input
   * @return a Coordinate object representing the bottom right corner of the board (in other words,
   *         whose dimensions represent the bounds of the game board)
   */
  @Override
  public Coord displaySizeQuery(int min, int max) {
    this.addToOutput(String.format("""
        Hello! Welcome to the OOD BattleSalvo Game!
        Please enter a valid height and width below:
        ------------------------------------------------------%n"""));

    int width = -1;
    int height = -1;
    boolean needsValidInput = true;

    while (needsValidInput) {
      try {
        width = sc.nextInt();
        height = sc.nextInt();

        this.catchSizeErrors(width, height, min, max);

        needsValidInput = false; // all inputs are valid
      } catch (IllegalArgumentException e) {
        this.addToOutput(String.format("""
            ------------------------------------------------------------------------------
            Uh Oh! You've entered invalid inputs. Please remember that the height and
            width of the game must be two integers in the range (6, 15), inclusive. Try again!
            ------------------------------------------------------------------------------%n"""));
        // sc.nextLine();
      } catch (InputMismatchException e) {
        this.addToOutput(String.format("""
            ------------------------------------------------------------------------------
            Uh Oh! You've entered invalid inputs. Please remember that the height and
            width of the game must be two integers in the range (6, 15), inclusive. Try again!
            ------------------------------------------------------------------------------%n"""));
        sc.nextLine();
      }
    }

    return new Coord(width, height);
  }

  /**
   * Checks for any errors that might occur from user input regarding the Board size
   *
   * @param width width input from user
   * @param height height input from user
   * @param min minimum bound of input from user
   * @param max maximum bound of input from user
   */
  private void catchSizeErrors(int width, int height, int min, int max) {
    // if too MANY inputs, throw an error to trigger the catch block
    if (new Scanner(sc.nextLine()).hasNext()) {
      throw new IllegalArgumentException("Too many inputs");
    }
    // if inputs are not within the given bounds (provided as arguments to the method;
    // the view doesn't know what they represent, only that they bound the inputs)
    if (width < min || width > max || height < min || height > max) {
      throw new IllegalArgumentException("Inputs are not within the given range bounds");
    }
  }

  /**
   * Prompts the user to input the number of ships they'd like to play with, and validates
   * the input based on the given bound.
   *
   * @param max maximum bound on the sum of the user inputs (max number of ships total)
   * @return a map of ship type to the number of occurrences each ship should appear on the board
   */
  @Override
  public Map<ShipType, Integer> displayFleetQuery(int max) {
    this.addToOutput(String.format(
        "-------------------------------------------------------------------------------%n"));

    Map<ShipType, Integer> shipOccurrences = new EnumMap<>(ShipType.class);
    boolean needsValidInput = true;

    while (needsValidInput) {
      this.addToOutput(String.format(
          "Please enter your fleet in the order [Carrier, Battleship, Destroyer, Submarine].%n"
          + "Remember, your fleet may not exceed size " + max + ".%n"
          + "-------------------------------------------------------------------------------%n"));

      try {
        shipOccurrences.put(ShipType.CARRIER, sc.nextInt());
        shipOccurrences.put(ShipType.BATTLESHIP, sc.nextInt());
        shipOccurrences.put(ShipType.DESTROYER, sc.nextInt());
        shipOccurrences.put(ShipType.SUBMARINE, sc.nextInt());

        this.catchFleetErrors(shipOccurrences, max);

        needsValidInput = false; // all inputs are valid
      } catch (IllegalArgumentException e) {
        this.addToOutput(String.format(
            "-------------------------------------------------------------------------------%n"
            + "Uh Oh! You've entered invalid fleet sizes.%n"));
      } catch (InputMismatchException e) {
        this.addToOutput(String.format(
            "-------------------------------------------------------------------------------%n"
            + "Uh Oh! You've entered invalid inputs.%n"));
        shipOccurrences.clear();
        sc.nextLine();
      }
    }

    return shipOccurrences;
  }

  /**
   * Catches error that might be thrown by issues encountered in processing user input
   * regarding fleet size
   *
   * @param shipOccurrences map of ship type to the number of occurrences each
   *                        ship should appear on the board, as input by the user
   * @param max maximum bound on the sum of the user inputs (max number of ships total)
   */
  private void catchFleetErrors(Map<ShipType, Integer> shipOccurrences, int max) {
    // if there are too MANY inputs, throw an error to trigger the catch block and re-loop
    if (new Scanner(sc.nextLine()).hasNext()) {
      throw new IllegalArgumentException("Too many inputs");
    }

    // if any of the inputs are 0 (or negative), throw an exception
    if (shipOccurrences.values().stream().anyMatch(num -> num <= 0)) {
      throw new IllegalArgumentException("Missing at least one of some boat type");
    }
    // if the total number of ships is greater than the max fleet size, throw an exception
    if (shipOccurrences.values().stream().reduce(0, Integer::sum) >= max) {
      throw new IllegalArgumentException("Fleet size too large");
    }
  }


  /**
   * Displays two boards (the user's own board and their opponent's board) given their
   * String representations.
   *
   * @param own String representation of the user's own Board
   * @param opp String representation of the user's opponent's Board
   */
  @Override
  public void displayBoards(String own, String opp) {
    this.addToOutput(String.format(
        "-------------------------------------------------------------------------------%n"));
    this.addToOutput(String.format("Opponent Board Data:%n" + opp));
    this.addToOutput(String.format("Your Board:%n" + own));
  }

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
  @Override
  public List<Coord> displaySalvoQuery(boolean firstTry, int numShots, int xmax, int ymax) {
    if (!firstTry) {
      this.addToOutput(String.format(
          "------------------------------------------------------------------%n"
              + "Uh Oh! You've entered a shot at a coordinate you've already shot at.%n"));
    }

    List<Coord> shotList = new ArrayList<>();
    boolean needsValidInput = true;

    while (needsValidInput) {
      this.addToOutput(String.format(
          "Please enter " + numShots + " distinct shots:%n"
          + "------------------------------------------------------------------%n"));

      try {
        shotList = this.processSalvoInput(numShots, xmax, ymax);

      } catch (IllegalArgumentException e) {
        this.addToOutput(String.format(
            "------------------------------------------------------------------%n"
            + "Uh Oh! You've entered an invalid shot "
            + "(exceeds board dimensions or too many inputs).%n"));
        continue;
      } catch (InputMismatchException e) {
        this.addToOutput(String.format(
            "------------------------------------------------------------------%n"
            + "Uh Oh! You've entered invalid inputs (non-integer character or duplicate shot).%n"));
        sc.nextLine();
        continue;
      }

      needsValidInput = false;
    }

    return shotList;
  }

  /**
   * Processes user input pertaining to salvo shots, determining whether the input is valid
   * and returning the input as a list of Coordinate shots
   *
   * @param numShots the number of shots the user must enter
   * @param xmax the maximum bound on the first user input, which corresponds to the xpos-value
   *             of a desired shot
   * @param ymax the maximum bound on the second user input, which corresponds to the y-value
   *             of a desired shot
   * @return a list of Coordinate objects representing the location of the user's shots
   */
  private List<Coord> processSalvoInput(int numShots, int xmax, int ymax) {
    List<Coord> shotList = new ArrayList<>();

    for (int i = 0; i < numShots; i += 1) {
      int x = sc.nextInt();
      int y = sc.nextInt();

      Coord shot = new Coord(x, y);

      if (x < 0 || x >= xmax || y < 0 || y >= ymax) {
        throw new IllegalArgumentException("Inputs are not within the given bound values");
      }

      if (shotList.contains(shot)) {
        throw new InputMismatchException("Duplicate shot");
      }

      shotList.add(shot);
    }

    if (new Scanner(sc.nextLine()).hasNext()) {
      throw new IllegalArgumentException("Too many inputs");
    }

    return shotList;
  }


  /**
   * Displays an end message to the user based on the outcome of the BattleSalvo game
   *
   * @param result GameResult representing the outcome of the game
   */
  @Override
  public void displayEndMessage(GameResult result) {
    this.addToOutput(String.format("Player 1, you " + result.toString() + "%n"));
  }

  /**
   * Adds the given message to this View's source of output
   *
   * @param msg String message to add to the output
   */
  @Override
  public void addToOutput(String msg) {
    try {
      output.append(msg);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
