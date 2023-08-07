package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.model.ShipType;
import java.util.HashMap;
import java.util.Map;

/**
 * JSON format of this record:
 * <p>
 * <code>
 * {
 *   "width": 10,
 *   "height": 10,
 *   "fleet-spec": {
 *     "CARRIER": 2,
 *     "BATTLESHIP": 4,
 *     "DESTROYER": 1,
 *     "SUBMARINE": 3
 *   }
 * }
 * </code>
 * </p>
 *
 * @param width the width of the BattleSalvo board
 * @param height the height of the BattleSalvo board
 * @param fleetSpec the fleet specifications (number of each ship type) for a BattleSalvo game
 */
public record SetupJson(
    @JsonProperty("width") int width,
    @JsonProperty("height") int height,
    @JsonProperty("fleet-spec") Map<ShipType, Integer> fleetSpec) {
}
