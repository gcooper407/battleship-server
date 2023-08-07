package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.model.GameResult;
/**
 * JSON format of this record:
 * <p>
 * <code>
 * {
 *   "result": "WIN/LOSE/DRAW",
 *   "reason": "[reason for outcome]"
 * }
 * </code>
 * </p>
 *
 * @param result result of the BattleSalvo game
 * @param reason reason for the result
 */

public record EndGameJson(
    @JsonProperty("result") GameResult result,
    @JsonProperty("reason") String reason) {
}
