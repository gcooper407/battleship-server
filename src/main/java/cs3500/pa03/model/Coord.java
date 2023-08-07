package cs3500.pa03.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an ordered pair of numbers which specify the position
 * of a point on a two-dimensional plane.
 */
public record Coord(@JsonProperty("x") int xpos, @JsonProperty("y") int ypos) {

}
