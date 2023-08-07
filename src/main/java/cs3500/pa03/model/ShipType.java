package cs3500.pa03.model;

/**
 * Represents a type of Ship, whether it is a carrier, a battleship, a destroyer, or a submarine.
 */
public enum ShipType {
  CARRIER(6),
  BATTLESHIP(5),
  DESTROYER(4),
  SUBMARINE(3);

  private final int size;

  /**
   * Constructs a ShipType of the given size
   *
   * @param size int size of this ShipType
   */
  private ShipType(int size) {
    this.size = size;
  }

  /**
   * Gets the size of this ShipType
   *
   * @return an int representing the size of this ShipType
   */
  public int getSize() {
    return this.size;
  }

  /**
   * Gets the representation of this ShipType as a BoardElement to display on a Board
   *
   * @return BoardElement representation of this ShipType
   */
  public BoardElement toElement() {
    return BoardElement.valueOf(this.toString());
  }
}
