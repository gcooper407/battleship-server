package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.Ship;
import cs3500.pa03.model.ShipOrientation;

/**
 * Adapts a Ship object (from PA03) into an object compatible with the PA04 server (containing an
 * origin coordinate (top-left), a length, and a direction
 */
public class ShipAdapter {

  @JsonProperty("coord") Coord coord;
  @JsonProperty("length") int length;
  @JsonProperty("direction") ShipOrientation direction;

  /**
   * Constructs a ShipAdapter object given a Coord, a length, and a direction
   *
   * @param coord Coord representing the top-left point of a ship
   * @param length integer length of a ship
   * @param direction direction of a ship from its origin coordinate, either horizontal or vertical
   */
  @JsonCreator
  public ShipAdapter(@JsonProperty("coord") Coord coord,
                     @JsonProperty("length") int length,
                     @JsonProperty("direction") ShipOrientation direction) {
    this.coord = coord;
    this.length = length;
    this.direction = direction;
  }

  /**
   * Constructs a ShipAdapter object given a Ship object
   *
   * @param myShip Ship object to adapt
   */
  public ShipAdapter(Ship myShip) {
    this(myShip.getCoords().get(0), myShip.getType().getSize(), myShip.getDir());
  }
}
