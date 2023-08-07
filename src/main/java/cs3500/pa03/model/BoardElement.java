package cs3500.pa03.model;

/**
 * Represents an element on a BattleSalvo game board (empty space, ship space,
 * hit space, miss space).
 */
public enum BoardElement {
  EMPTY,
  CARRIER,
  BATTLESHIP,
  DESTROYER,
  SUBMARINE,
  HIT,
  MISS;


  /**
   * Renders this BoardElement as a char to display on a board.
   *
   * @return a char representation of this BoardElement
   */
  public char renderFull() {
    if (this == EMPTY) {
      return '-';
    } else if (this == HIT) {
      return 'X';
    } else if (this == MISS) {
      return 'O';
    } else {
      return this.name().charAt(0);
    }
  }


  /**
   * Renders this BoardElement based on only what can be known by an opponent (hit, miss,
   * or unknown).
   *
   * @return a char representing this BoardElement as it might be known by an opponent
   */
  public char renderHidden() {
    if (this == HIT) {
      return 'X';
    } else if (this == MISS) {
      return 'O';
    } else {
      return '-';
    }
  }
}
