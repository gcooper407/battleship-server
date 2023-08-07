package cs3500.pa03.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the constructors and method on the Ship class
 */
public class ShipTest {

  Ship carrier;
  Ship battleship;
  Ship destroyer;
  Ship submarine;

  /**
   * Initializes sample Ships (and thus tests the constructor)
   */
  @BeforeEach
  public void initData() {
    this.carrier = new Ship(ShipType.CARRIER, new Coord(0, 0), ShipOrientation.HORIZONTAL);
    this.battleship = new Ship(ShipType.BATTLESHIP, new Coord(3, 2), ShipOrientation.VERTICAL);
    this.destroyer = new Ship(ShipType.DESTROYER, new Coord(10, 6), ShipOrientation.VERTICAL);
    this.submarine = new Ship(ShipType.SUBMARINE, new Coord(7, 12), ShipOrientation.HORIZONTAL);
  }

  /**
   * Tests the .getCoords() method (on each type of ship and on both orientations)
   */
  @Test
  public void getCoords() {
    List<Coord> carrierCoords = new ArrayList<>();
    carrierCoords.add(new Coord(0, 0));
    carrierCoords.add(new Coord(1, 0));
    carrierCoords.add(new Coord(2, 0));
    carrierCoords.add(new Coord(3, 0));
    carrierCoords.add(new Coord(4, 0));
    carrierCoords.add(new Coord(5, 0));

    assertEquals(carrierCoords, this.carrier.getCoords());

    List<Coord> battleshipCoords = new ArrayList<>();
    battleshipCoords.add(new Coord(3, 2));
    battleshipCoords.add(new Coord(3, 3));
    battleshipCoords.add(new Coord(3, 4));
    battleshipCoords.add(new Coord(3, 5));
    battleshipCoords.add(new Coord(3, 6));

    assertEquals(battleshipCoords, this.battleship.getCoords());

    List<Coord> destroyerCoords = new ArrayList<>();
    destroyerCoords.add(new Coord(10, 6));
    destroyerCoords.add(new Coord(10, 7));
    destroyerCoords.add(new Coord(10, 8));
    destroyerCoords.add(new Coord(10, 9));

    assertEquals(destroyerCoords, this.destroyer.getCoords());

    List<Coord> submarineCoords = new ArrayList<>();
    submarineCoords.add(new Coord(7, 12));
    submarineCoords.add(new Coord(8, 12));
    submarineCoords.add(new Coord(9, 12));

    assertEquals(submarineCoords, this.submarine.getCoords());
  }

  /**
   * Tests the .getType() method (on each type of ship)
   */
  @Test
  public void getType() {
    assertEquals(ShipType.CARRIER, this.carrier.getType());
    assertEquals(ShipType.BATTLESHIP, this.battleship.getType());
    assertEquals(ShipType.DESTROYER, this.destroyer.getType());
    assertEquals(ShipType.SUBMARINE, this.submarine.getType());
  }

  /**
   * Tests both the .processHit() method and the .isSunk() methods -- the functionalities
   * of both methods involve the private "spacesUnhit" field, so using each method serves
   * to test the functionality of the other.
   */
  @Test
  public void processHitAndIsSunk() {
    // tests that with each call to .processHit(), spacesUnhit gets decremented
    // by 1, and a Ship is only sunk when spacesUnhit reaches 0
    assertFalse(this.submarine.isSunk()); // spacesUnhit = 3
    this.submarine.processHit(); // spacesUnhit = 3 --> 2
    assertFalse(this.submarine.isSunk()); // spacesUnhit = 2
    this.submarine.processHit(); // spacesUnhit = 2 --> 1
    assertFalse(this.submarine.isSunk()); // spacesUnhit = 1
    this.submarine.processHit(); // spacesUnhit = 1 --> 0
    assertTrue(this.submarine.isSunk()); // spacesUnhit = 0
  }

  /**
   * Tests the .equals(...) overridden method
   */
  @Test
  public void equalsTest() {
    // same as itself and same as other Ship object with exact same elements
    assertEquals(this.carrier, this.carrier);
    assertEquals(this.carrier, new Ship(ShipType.CARRIER, new Coord(0, 0),
        ShipOrientation.HORIZONTAL));

    // different origin
    assertNotEquals(this.carrier, new Ship(ShipType.CARRIER, new Coord(1, 0),
        ShipOrientation.HORIZONTAL));
    // different orientation
    assertNotEquals(this.carrier, new Ship(ShipType.CARRIER, new Coord(0, 0),
        ShipOrientation.VERTICAL));
    // different type
    assertNotEquals(this.carrier, new Ship(ShipType.BATTLESHIP, new Coord(0, 0),
        ShipOrientation.HORIZONTAL));

    Ship hitShip = new Ship(ShipType.CARRIER, new Coord(0, 0),
        ShipOrientation.HORIZONTAL);
    hitShip.processHit();

    // different number of spaces unhit
    assertNotEquals(this.carrier, hitShip);

    // completely different ships and even different objects entirely
    assertNotEquals(this.carrier, this.battleship);
    assertNotEquals(this.carrier, new Coord(5, 5));
  }

  /**
   * Tests the .hashCode() overridden method
   */
  @Test
  public void hashCodeTest() {
    assertEquals(6000060, this.carrier.hashCode());
    assertEquals(5030251, this.battleship.hashCode());
    assertEquals(4100641, this.destroyer.hashCode());
    assertEquals(3071230, this.submarine.hashCode());
  }
}