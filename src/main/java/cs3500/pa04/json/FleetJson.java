package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * JSON format of this record:
 * <p>
 * <code>
 * {
 *   "fleet": [
 *    {
 *      "coord": {"x": 0, "y": 0},
 *      "length": 6,
 *      "direction": "VERTICAL"
 *    },
 *    {
 *      "coord": {"x": 1, "y": 0},
 *      "length": 5,
 *      "direction": "HORIZONTAL"
 *    }
 *   ]
 * }
 * </code>
 * </p>
 *
 * @param fleet list of Ships representing a player's fleet in a BattleSalvo game
 */
public record FleetJson(
    @JsonProperty("fleet") List<ShipAdapter> fleet) {
}
