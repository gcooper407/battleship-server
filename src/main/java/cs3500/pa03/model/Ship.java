package cs3500.pa03.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a ship in a BattleSalvo game.
 */
public class Ship {

  private final ShipType type;
  private final Coord origin;
  private final ShipOrientation dir;
  private int spacesUnhit;

  /**
   * Constructs a ship in a BattleSalvo game with an origin location on a board (the top-most or
   * left-most space it inhabits), an orientation (either vertical or horizontal), and an array
   * representing hits landed on the ship (an element is true if the corresponding space on the
   * ship has been hit).
   *
   * @param type the type of ship, which indicates its size
   * @param origin origin point of the ship (top- or left-most point, depending on orientation)
   * @param dir orientation of the ship, either vertical or horizontal
   */
  public Ship(ShipType type, Coord origin, ShipOrientation dir) {
    this.type = type;
    this.origin = origin;
    this.dir = dir;
    this.spacesUnhit = type.getSize();
  }


  /**
   * Gets the list of Coordinates that this Ship is placed on
   *
   * @return list of Coordinates that this Ship is placed on
   */
  public List<Coord> getCoords() {
    List<Coord> coords = new ArrayList<>();

    if (dir == ShipOrientation.HORIZONTAL) {
      for (int i = this.origin.xpos(); i < this.origin.xpos() + this.type.getSize(); i += 1) {
        coords.add(new Coord(i, this.origin.ypos()));
      }
    } else {
      for (int i = this.origin.ypos(); i < this.origin.ypos() + this.type.getSize(); i += 1) {
        coords.add(new Coord(this.origin.xpos(), i));
      }
    }

    return coords;
  }

  /**
   * Gets the ShipType of this Ship
   *
   * @return ShipType of this Ship
   */
  public ShipType getType() {
    return this.type;
  }

  /**
   * Gets the direction of this Ship
   *
   * @return the direction (orientation) of this Ship
   */
  public ShipOrientation getDir() {
    return this.dir;
  }

  /**
   * Processes a hit by decrementing by 1 the number of spaces unhit
   */
  public void processHit() {
    this.spacesUnhit -= 1;
  }

  /**
   * Determines whether this ship has sunk.
   *
   * @return true if this Ship has sunk.
   */
  public boolean isSunk() {
    return this.spacesUnhit == 0;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Ship s) {
      return this.type == s.type && this.origin.equals(s.origin)
          && this.dir == s.dir && this.spacesUnhit == s.spacesUnhit;
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return (this.type.getSize() * 1000000) + (this.origin.xpos() * 10000)
        + (this.origin.ypos() * 100) + (this.spacesUnhit * 10) + this.dir.ordinal();
  }

}
