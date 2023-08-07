package cs3500.pa03.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a board in a BattleSalvo game, containing a 2D grid of board elements and
 * a list of afloat ships
 */
public class Board {

  private BoardElement[][] board;
  private List<Ship> fleet;

  /**
   * Constructs an empty board in a BattleSalvo game with an empty fleet.
   */
  public Board() {
    this.board = null;
    this.fleet = new ArrayList<>();
  }

  /**
   * Initializes the content of this board, containing the given number of rows and the given
   * number of columns of EMPTY BoardElements
   *
   * @param rows number of rows to put in this Board
   * @param cols number of columns to put in this Board
   */
  public void initBoard(int rows, int cols) {
    this.board = new BoardElement[rows][cols];

    for (BoardElement[] row : this.board) {
      Arrays.fill(row, BoardElement.EMPTY);
    }
  }

  /**
   * Gets the board contents (2D array of BoardElements) of this Board
   *
   * @return 2D array of BoardElements that represents the contents of this Board
   */
  public BoardElement[][] getBoard() {
    return this.board;
  }

  /**
   * Gets the BoardElement in this Board at the given Coordinate point
   *
   * @param point Coordinate point to get the element at
   * @return the BoardElement in this Board at the given Coordinate point
   */
  public BoardElement getElementAt(Coord point) {
    return this.board[point.ypos()][point.xpos()];
  }

  /**
   * Sets the given Coordinate point in this Board's 2D representation to the given BoardElement
   *
   * @param point Coordinate point in this Board's 2D representation
   * @param element BoardElement to set the point to
   */
  public void setElementAt(Coord point, BoardElement element) {
    this.board[point.ypos()][point.xpos()] = element;
  }

  /**
   * Displays a visual representation of this board, with the visibility of elements
   * determined by the given boolean (true to show all info -- ships, hits, misses, and
   * empty spaces), false to show limited info (only that which a user might know about their
   * opponent's board -- hits, misses, and unknown spaces).
   *
   * @param allInfo true to display all info, false to display limited info
   * @return a String representation of this Board with the proper info
   */
  public String render(boolean allInfo) {
    StringBuilder visual = new StringBuilder();

    for (int i = 0; i < this.board.length; i += 1) {
      visual.append("     ");
      for (int j = 0; j < this.board[i].length; j += 1) {
        visual.append(allInfo ? this.board[i][j].renderFull() : this.board[i][j].renderHidden());
        visual.append(String.format(j == this.board[i].length - 1 ? "%n" : " "));
      }
    }

    visual.append(String.format("%n"));

    return visual.toString();
  }

  /**
   * Determines whether the given Ship can be placed on this Board
   *
   * @param ship Ship object to check against this Board, to see if it can be placed
   * @return true if the given Ship can be placed on this Board
   */
  public boolean canPlace(Ship ship) {
    // returns true if no Coordinate that the given Ship would exist on is
    // already taken up by any Ship in this Board's fleet
    return ship.getCoords().stream().noneMatch(coord ->
        this.fleet.stream().anyMatch(s -> s.getCoords().contains(coord)));
  }

  /**
   * Adds the given Ship to this Board's fleet
   *
   * @param ship given Ship to add to this Board's fleet
   */
  public void addToFleet(Ship ship) {
    this.fleet.add(ship);
    ship.getCoords().forEach(coord -> this.setElementAt(coord, ship.getType().toElement()));
  }

  /**
   * Returns the number of ships in this Board's fleet
   *
   * @return an int representing the number of ships in this Board's fleet
   */
  public int numShips() {
    return this.fleet.size();
  }

  /**
   * Returns the number of empty spaces remaining on this Board
   *
   * @return the number of empty spaces remaining on this Board
   */
  public int numRemainingEmpty() {
    int base = 0;
    for (BoardElement[] row : this.board) {
      for (BoardElement space : row) {
        base += (space == BoardElement.EMPTY ? 1 : 0);
      }
    }

    return base;
  }

  /**
   * Determines whether the given Coordinate, representing a shot on this Board, is valid (i.e.
   * if it is within the Board's bounds and has not already been shot at)
   *
   * @param shot Coordinate object representing a shot on this Board
   * @return true if the given Coordinate "shot" on this Board is valid
   */
  public boolean isValidShot(Coord shot) {
    return (shot.xpos() >= 0 && shot.xpos() < this.board[0].length)
        && (shot.ypos() >= 0 && shot.ypos() < this.board.length)
        && this.getElementAt(shot) != BoardElement.HIT
        && this.getElementAt(shot) != BoardElement.MISS;
  }

  /**
   * Determines whether all the Coordinates in the given list, representing shots on this Board,
   * are valid (i.e. if they are within the Board's bounds and have not already been shot at)
   *
   * @param coords List of Coordinate objects representing shots on this Board
   * @return true if all Coordinate "shots" in the given list are valid
   */
  public boolean allValidShots(List<Coord> coords) {
    return coords.stream().allMatch(this::isValidShot);
  }

  /**
   * Processes the given list of Coordinate shots, updating this Board's 2D representation and
   * fleet as necessary, and returns which shots hit a ship on this Board.
   *
   * @param shots list of Coordinate shots taken on this Board
   * @return list of shots that hit a ship on this Board
   */
  public List<Coord> processShots(List<Coord> shots) {
    List<Coord> hits = new ArrayList<>();

    for (Coord c : shots) {
      boolean isHit = false;
      for (Ship s : this.fleet) {
        if (s.getCoords().contains(c)) {
          hits.add(c);
          this.setElementAt(c, BoardElement.HIT);
          s.processHit();
          isHit = true;
          break;
        }
      }
      if (!isHit) {
        this.setElementAt(c, BoardElement.MISS);
      }
    }

    this.fleet.removeIf(Ship::isSunk);

    return hits;
  }

  /**
   * Displays the list of hits on this Board (for displaying successful hits on an opponent)
   *
   * @param hits list of hits to display on this Board
   */
  public void displaySuccessfulHits(List<Coord> hits) {
    for (Coord c : hits) {
      this.setElementAt(c, BoardElement.HIT);
    }
  }

  /**
   * Marks all shots in the given list as misses
   *
   * @param shots list of Coordinate shots to mark as misses
   */
  public void markAsMiss(List<Coord> shots) {
    for (Coord shot : shots) {
      this.setElementAt(shot, BoardElement.MISS);
    }
  }
}
