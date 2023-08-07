package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * JSON format of this record:
 * <p>
 * <code>
 * {
 *   "name": "github_username",
 *   "game-type": "SINGLE/MULTI"
 * }
 * </code>
 * </p>
 *
 * @param name github username of the client player
 * @param gametype single (against server AI) or multi (against another cliuent player)
 */
public record JoinJson(
    @JsonProperty("name") String name,
    @JsonProperty("game-type") GameType gametype) {
}
