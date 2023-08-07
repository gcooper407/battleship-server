package cs3500.pa04.client;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa03.controller.Controller;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.GameResult;
import cs3500.pa03.model.Player;
import cs3500.pa03.model.Ship;
import cs3500.pa03.model.ShipType;
import cs3500.pa04.json.EndGameJson;
import cs3500.pa04.json.FleetJson;
import cs3500.pa04.json.GameType;
import cs3500.pa04.json.JoinJson;
import cs3500.pa04.json.JsonUtils;
import cs3500.pa04.json.MessageJson;
import cs3500.pa04.json.SetupJson;
import cs3500.pa04.json.ShipAdapter;
import cs3500.pa04.json.VolleyJson;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;

/**
 * This class uses the Proxy Pattern to talk to the Server and dispatch methods to the Player.
 */
public class ProxyController implements Controller {

  private final Socket server;
  private final InputStream in;
  private final PrintStream out;
  private final Player player;
  private final String username;
  private final GameType gametype;
  private final ObjectMapper mapper = new ObjectMapper();
  private static final JsonNode VOID_RESPONSE =
      new ObjectMapper().getNodeFactory().textNode("void");

  /**
   * Construct an instance of a ProxyController.
   *
   * @param server the socket connection to the server
   * @param player the instance of the player
   * @throws IOException if unable to get input and/or output stream(s) from the server
   */
  public ProxyController(Socket server, Player player, GameType type, String username)
      throws IOException {
    this.server = server;
    this.in = server.getInputStream();
    this.out = new PrintStream(server.getOutputStream());
    this.player = player;
    this.gametype = type;
    this.username = username;
  }

  /**
   * Listens for messages from the server as JSON in the format of a MessageJSON. When a complete
   * message is sent by the server, the message is parsed and then delegated to the corresponding
   * helper method for each message. This method stops when the connection to the server is closed
   * or an IOException is thrown from parsing malformed JSON.
   */
  public void run() {
    try {
      JsonParser parser = this.mapper.getFactory().createParser(this.in);

      while (!this.server.isClosed()) {
        MessageJson message = parser.readValueAs(MessageJson.class);
        delegateMessage(message);
      }
    } catch (IOException e) {
      // Disconnected from server or parsing exception
    }
  }

  /**
   * Determines the type of request the server has sent ("guess" or "win") and delegates to the
   * corresponding helper method with the message arguments.
   *
   * @param message the MessageJSON used to determine what the server has sent
   */
  private void delegateMessage(MessageJson message) {
    String name = message.methodName();
    JsonNode arguments = message.arguments();

    switch (name) {
      case "join" -> handleJoin();
      case "setup" -> handleSetup(arguments);
      case "take-shots" -> handleTakeShots();
      case "report-damage" -> handleReportDamage(arguments);
      case "successful-hits" -> handleSuccessfulHits(arguments);
      case "end-game" -> handleEndGame(arguments);
      default -> throw new IllegalStateException("Invalid message name");
    }
  }


  /**
   * Handles the joining of a BattleSalvo game in the server, sending my github username and desired
   * game type (either "SINGLE" or "MULTI")
   */
  private void handleJoin() {
    JoinJson join = new JoinJson(this.username, this.gametype);
    JsonNode joinNode = JsonUtils.serializeRecord(join);
    MessageJson msg = new MessageJson("join", joinNode);
    JsonNode jsonResponse = JsonUtils.serializeRecord(msg);

    this.out.println(jsonResponse);
  }


  /**
   * Handles communicating to the client Player the request for setting up a BattleSalvo game,
   * based on information from the server about board width, height, and fleet specifications,
   * and sending back to the server an array of ships representing the client's fleet
   *
   * @param arguments JsonNode containing information about the board width and height as well
   *                  as fleet specifications
   */
  private void handleSetup(JsonNode arguments) {
    SetupJson setupArgs = this.mapper.convertValue(arguments, SetupJson.class);

    List<Ship> ships = makeFleet(setupArgs);
    List<ShipAdapter> adaptedShips = ships.stream().map(ShipAdapter::new).toList();

    FleetJson fleetJson = new FleetJson(adaptedShips);
    JsonNode fleet = JsonUtils.serializeRecord(fleetJson);

    MessageJson msg = new MessageJson("setup", fleet);
    JsonNode jsonResponse = JsonUtils.serializeRecord(msg);

    this.out.println(jsonResponse);
  }

  /**
   * Handles communicating to the client Player the request for making of a fleet of ships based on
   * given information (from the server) about the board dimension and fleet specifications.
   *
   * @param setupArgs SetupJson containing information about the board dimensions and fleet
   *                  specifications
   * @return a list of Ship representing a player's fleet
   */
  private List<Ship> makeFleet(SetupJson setupArgs) {
    int height = setupArgs.height();
    int width = setupArgs.width();
    Map<ShipType, Integer> fleetSpecs = setupArgs.fleetSpec();

    return this.player.setup(height, width, fleetSpecs);
  }


  /**
   * Handles communicating to the client Player a request to take shots on the opponent and
   * communicating the coordinates of these shots back to the server
   */
  private void handleTakeShots() {
    VolleyJson volley = new VolleyJson(this.player.takeShots());

    JsonNode jsonVolley = JsonUtils.serializeRecord(volley);

    MessageJson msg = new MessageJson("take-shots", jsonVolley);
    JsonNode jsonResponse = JsonUtils.serializeRecord(msg);

    this.out.println(jsonResponse);
  }


  /**
   * Handles communicating to the client Player incoming shots from the server and reporting back to
   * the server which incoming shots inflicted damage on the client's ships
   *
   * @param arguments JsonNode containing an array of coordinate shots taken on the client's board
   */
  private void handleReportDamage(JsonNode arguments) {
    VolleyJson volley = this.mapper.convertValue(arguments, VolleyJson.class);

    List<Coord> hits = this.player.reportDamage(volley.coordinates());
    VolleyJson damage = new VolleyJson(hits);
    JsonNode jsonVolley = JsonUtils.serializeRecord(damage);

    MessageJson msg = new MessageJson("report-damage", jsonVolley);

    JsonNode jsonResponse = JsonUtils.serializeRecord(msg);

    this.out.println(jsonResponse);
  }


  /**
   * Handles communicating to the client Player which of their shots successfully hit the opponent's
   * ships
   *
   * @param arguments JsonNode containing an array of coordinate shots that hit opponent ships
   */
  private void handleSuccessfulHits(JsonNode arguments) {
    VolleyJson volley = this.mapper.convertValue(arguments, VolleyJson.class);

    this.player.successfulHits(volley.coordinates());

    MessageJson msg = new MessageJson("successful-hits", VOID_RESPONSE);
    JsonNode jsonResponse = JsonUtils.serializeRecord(msg);

    this.out.println(jsonResponse);
  }


  /**
   * Handles communicating to the client Player the ending of the BattleSalvo game based on a
   * game result and reason provided by the server
   *
   * @param arguments JsonNode containing information about the result of the game and the
   *                  reason for that result
   */
  private void handleEndGame(JsonNode arguments) {
    EndGameJson endgame = this.mapper.convertValue(arguments, EndGameJson.class);

    GameResult result = endgame.result();
    String reason = endgame.reason();

    this.player.endGame(result, reason);

    MessageJson msg = new MessageJson("end-game", VOID_RESPONSE);
    JsonNode jsonResponse = JsonUtils.serializeRecord(msg);

    this.out.println(jsonResponse);
  }



}
