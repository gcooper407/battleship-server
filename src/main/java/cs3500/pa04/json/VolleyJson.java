package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.model.Coord;
import java.util.List;
/**
 * JSON format of this record:
 * <p>
 * <code>
 * {
 *   "coordinates": [
 *     {"x": 4, "y": 2},
 *     {"x": 7, "y": 1}
 *   ]
 * }
 * </code>
 * </p>
 *
 * @param coordinates list of Coords representing a list of shots fired by a player
 */

public record VolleyJson(
    @JsonProperty("coordinates") List<Coord> coordinates) {
}
