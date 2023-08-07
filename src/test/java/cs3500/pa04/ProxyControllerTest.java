package cs3500.pa04;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa03.model.Board;
import cs3500.pa03.model.BotPlayer;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.GameResult;
import cs3500.pa03.model.Player;
import cs3500.pa03.model.ShipType;
import cs3500.pa04.client.ProxyController;
import cs3500.pa04.json.EndGameJson;
import cs3500.pa04.json.FleetJson;
import cs3500.pa04.json.GameType;
import cs3500.pa04.json.JoinJson;
import cs3500.pa04.json.JsonUtils;
import cs3500.pa04.json.MessageJson;
import cs3500.pa04.json.SetupJson;
import cs3500.pa04.json.VolleyJson;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the constructor and methods of the ProxyController class
 */
class ProxyControllerTest {

  private ByteArrayOutputStream testLog;
  private ProxyController controller;
  private final ObjectMapper mapper = new ObjectMapper();
  private final String username = "gcooper407";
  private static final JsonNode VOID_RESPONSE =
      new ObjectMapper().getNodeFactory().textNode("void");


  /**
   * Reset the test log before each test is run.
   */
  @BeforeEach
  public void setup() {
    this.testLog = new ByteArrayOutputStream(2048);
    assertEquals("", logToString());
  }


  /**
   * Check that the server returns my Github username with the desired game type when given a
   * request to join a BattleSalvo game
   */
  @Test
  public void testJoinForJoin() {
    // Prepare sample message
    JsonNode voidResponse = new ObjectMapper().getNodeFactory().textNode("void");
    MessageJson message = new MessageJson("join", voidResponse);
    JsonNode sampleMessage = JsonUtils.serializeRecord(message);

    // Create the client with all necessary messages
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    // Create a Controller
    try {
      this.controller = new ProxyController(socket, new BotPlayer(new Board(), new Random(1)),
          GameType.SINGLE, username);
    } catch (IOException e) {
      fail(); // fail if the controller can't be created
    }


    // run the controller and verify the response
    this.controller.run();

    String expected = "{\"method-name\":\"join\",\"arguments\":{\"name\":\"gcooper407"
        + "\",\"game-type\":\"SINGLE\"}}\n";
    assertEquals(expected, logToString());

    MessageJson msg = responseToClass(MessageJson.class);
    JsonNode args = msg.arguments();
    try {
      JsonParser jsonParser = new ObjectMapper().createParser(args.toString());
      jsonParser.readValueAs(JoinJson.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Check that the server returns a board setup when given a request for a setup
   * (with board dimensions and fleet specifications)
   */
  @Test
  public void testSetupForSetup() {
    // Prepare sample message
    Map<ShipType, Integer> map = new HashMap<>();
    map.put(ShipType.CARRIER, 1);
    map.put(ShipType.BATTLESHIP, 1);
    map.put(ShipType.DESTROYER, 1);
    map.put(ShipType.SUBMARINE, 1);
    SetupJson setupJson = new SetupJson(6, 6, map);
    JsonNode sampleMessage = createSampleMessage("setup", setupJson);

    // Create the client with all necessary messages
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    // Create a Controller
    try {
      this.controller = new ProxyController(socket, new BotPlayer(new Board(), new Random(1)),
          GameType.SINGLE, username);
    } catch (IOException e) {
      fail(); // fail if the controller can't be created
    }

    // run the controller and verify the response
    this.controller.run();

    String expected = "{\"method-name\":\"setup\",\"arguments\":{\"fleet\":[{\"coord\":{\"x\":0,\""
        + "y\":1},\"length\":6,\"direction\":\"HORIZONTAL\"},{\"coord\":{\"x\":0,\"y\":0},\"length"
        + "\":5,\"direction\":\"HORIZONTAL\"},{\"coord\":{\"x\":2,\"y\":2},\"length\":4,\""
        + "direction\":\"HORIZONTAL\"},{\"coord\":{\"x\":1,\"y\":5},\"length\":3,\"direction\":\""
        + "HORIZONTAL\"}]}}\n";
    assertEquals(expected, logToString());

    MessageJson msg = responseToClass(MessageJson.class);
    JsonNode args = msg.arguments();
    try {
      JsonParser jsonParser = new ObjectMapper().createParser(args.toString());
      jsonParser.readValueAs(FleetJson.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testTakeShotsForTakeShots() {
    // Prepare sample message
    VolleyJson volleyJson = new VolleyJson(List.of());
    JsonNode sampleMessage = createSampleMessage("take-shots", volleyJson);

    // Set up board
    Board board = new Board();
    Player bot = new BotPlayer(board, new Random(1));
    Map<ShipType, Integer> map = new HashMap<>();
    map.put(ShipType.CARRIER, 1);
    map.put(ShipType.BATTLESHIP, 1);
    map.put(ShipType.DESTROYER, 1);
    map.put(ShipType.SUBMARINE, 1);

    // Create the client with all necessary messages
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    // Create a Controller
    try {
      this.controller = new ProxyController(socket, bot, GameType.SINGLE, username);
      bot.setup(6, 6, map);
    } catch (IOException e) {
      fail(); // fail if the controller can't be created
    }


    // run the controller and verify the response
    this.controller.run();

    String expected = "{\"method-name\":\"take-shots\",\"arguments\":{\"coordinates\":"
        + "[{\"x\":2,\"y\":5},{\"x\":4,\"y\":5},{\"x\":3,\"y\":2},{\"x\":1,\"y\":4}]}}\n";
    assertEquals(expected, logToString());

    MessageJson msg = responseToClass(MessageJson.class);
    JsonNode args = msg.arguments();
    try {
      JsonParser jsonParser = new ObjectMapper().createParser(args.toString());
      jsonParser.readValueAs(VolleyJson.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testReportDamageForReportDamage() {
    // Prepare sample message
    VolleyJson volleyJson = new VolleyJson(List.of(new Coord(0, 0), new Coord(5, 5)));
    JsonNode sampleMessage = createSampleMessage("report-damage", volleyJson);

    // Set up board
    Board board = new Board();
    Player bot = new BotPlayer(board, new Random(1));
    Map<ShipType, Integer> map = new HashMap<>();
    map.put(ShipType.CARRIER, 1);
    map.put(ShipType.BATTLESHIP, 1);
    map.put(ShipType.DESTROYER, 1);
    map.put(ShipType.SUBMARINE, 1);

    // Create the client with all necessary messages
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    // Create a Controller
    try {
      this.controller = new ProxyController(socket, bot, GameType.SINGLE, username);
      bot.setup(6, 6, map);
    } catch (IOException e) {
      fail(); // fail if the controller can't be created
    }


    // run the controller and verify the response
    this.controller.run();

    String expected = "{\"method-name\":\"report-damage\",\"arguments\":"
        + "{\"coordinates\":[{\"x\":0,\"y\":0}]}}\n";
    assertEquals(expected, logToString());

    MessageJson msg = responseToClass(MessageJson.class);
    JsonNode args = msg.arguments();
    try {
      JsonParser jsonParser = new ObjectMapper().createParser(args.toString());
      jsonParser.readValueAs(VolleyJson.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testSuccessfulHitsForSuccessfulHits() {
    // Prepare sample message
    VolleyJson volleyJson = new VolleyJson(List.of(new Coord(0, 0)));
    JsonNode sampleMessage = createSampleMessage("successful-hits", volleyJson);

    // Set up board
    Board board = new Board();
    Player bot = new BotPlayer(board, new Random(1));
    Map<ShipType, Integer> map = new HashMap<>();
    map.put(ShipType.CARRIER, 1);
    map.put(ShipType.BATTLESHIP, 1);
    map.put(ShipType.DESTROYER, 1);
    map.put(ShipType.SUBMARINE, 1);

    // Create the client with all necessary messages
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    // Create a Controller
    try {
      this.controller = new ProxyController(socket, bot, GameType.SINGLE, username);
      bot.setup(6, 6, map);
    } catch (IOException e) {
      fail(); // fail if the controller can't be created
    }


    // run the controller and verify the response
    this.controller.run();

    String expected = "{\"method-name\":\"successful-hits\",\"arguments\":\"void\"}\n";
    assertEquals(expected, logToString());

    MessageJson msg = responseToClass(MessageJson.class);
    JsonNode args = msg.arguments();
    try {
      JsonParser jsonParser = new ObjectMapper().createParser(args.toString());
      jsonParser.readValueAs(JsonNode.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testEndGameWinForEndGame() {
    // Prepare sample message
    EndGameJson volleyJson = new EndGameJson(GameResult.WIN, "because I said so");
    JsonNode sampleMessage = createSampleMessage("end-game", volleyJson);

    // Set up board
    Board board = new Board();
    Player bot = new BotPlayer(board, new Random(1));
    Map<ShipType, Integer> map = new HashMap<>();
    map.put(ShipType.CARRIER, 1);
    map.put(ShipType.BATTLESHIP, 1);
    map.put(ShipType.DESTROYER, 1);
    map.put(ShipType.SUBMARINE, 1);

    // Create the client with all necessary messages
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    // Create a Controller
    try {
      this.controller = new ProxyController(socket, bot, GameType.SINGLE, username);
      bot.setup(6, 6, map);
    } catch (IOException e) {
      fail(); // fail if the controller can't be created
    }


    // run the controller and verify the response
    this.controller.run();

    String expected = "{\"method-name\":\"end-game\",\"arguments\":\"void\"}\n";
    assertEquals(expected, logToString());

    MessageJson msg = responseToClass(MessageJson.class);
    JsonNode args = msg.arguments();
    try {
      JsonParser jsonParser = new ObjectMapper().createParser(args.toString());
      jsonParser.readValueAs(JsonNode.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testEndGameLoseForEndGame() {
    // Prepare sample message
    EndGameJson volleyJson = new EndGameJson(GameResult.LOSE, "because I said so");
    JsonNode sampleMessage = createSampleMessage("end-game", volleyJson);

    // Set up board
    Board board = new Board();
    Player bot = new BotPlayer(board, new Random(1));
    Map<ShipType, Integer> map = new HashMap<>();
    map.put(ShipType.CARRIER, 1);
    map.put(ShipType.BATTLESHIP, 1);
    map.put(ShipType.DESTROYER, 1);
    map.put(ShipType.SUBMARINE, 1);

    // Create the client with all necessary messages
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    // Create a Controller
    try {
      this.controller = new ProxyController(socket, bot, GameType.SINGLE, username);
      bot.setup(6, 6, map);
    } catch (IOException e) {
      fail(); // fail if the controller can't be created
    }


    // run the controller and verify the response
    this.controller.run();

    String expected = "{\"method-name\":\"end-game\",\"arguments\":\"void\"}\n";
    assertEquals(expected, logToString());

    MessageJson msg = responseToClass(MessageJson.class);
    JsonNode args = msg.arguments();
    try {
      JsonParser jsonParser = new ObjectMapper().createParser(args.toString());
      jsonParser.readValueAs(JsonNode.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testEndGameDrawForEndGame() {
    // Prepare sample message
    EndGameJson volleyJson = new EndGameJson(GameResult.DRAW, "because I said so");
    JsonNode sampleMessage = createSampleMessage("end-game", volleyJson);

    // Set up board
    Board board = new Board();
    Player bot = new BotPlayer(board, new Random(1));
    Map<ShipType, Integer> map = new HashMap<>();
    map.put(ShipType.CARRIER, 1);
    map.put(ShipType.BATTLESHIP, 1);
    map.put(ShipType.DESTROYER, 1);
    map.put(ShipType.SUBMARINE, 1);

    // Create the client with all necessary messages
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    // Create a Controller
    try {
      this.controller = new ProxyController(socket, bot, GameType.SINGLE, username);
      bot.setup(6, 6, map);
    } catch (IOException e) {
      fail(); // fail if the controller can't be created
    }


    // run the controller and verify the response
    this.controller.run();

    String expected = "{\"method-name\":\"end-game\",\"arguments\":\"void\"}\n";
    assertEquals(expected, logToString());

    MessageJson msg = responseToClass(MessageJson.class);
    JsonNode args = msg.arguments();
    try {
      JsonParser jsonParser = new ObjectMapper().createParser(args.toString());
      jsonParser.readValueAs(JsonNode.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  @Test
  public void testMisinput() {
    // Prepare sample message
    MessageJson msg = new MessageJson("misinput", VOID_RESPONSE);
    JsonNode sampleMessage = JsonUtils.serializeRecord(msg);

    // Create the client with all necessary messages
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    // Create a Controller
    try {
      this.controller = new ProxyController(socket, new BotPlayer(new Board(), new Random(1)),
          GameType.SINGLE, username);
    } catch (IOException e) {
      fail(); // fail if the controller can't be created
    }

    assertThrows(IllegalStateException.class, () -> this.controller.run());

  }

  /**
   * Converts the ByteArrayOutputStream log to a string in UTF_8 format
   *
   * @return String representing the current log buffer
   */
  private String logToString() {
    return testLog.toString(StandardCharsets.UTF_8);
  }

  /**
   * Try converting the current test log to a string of a certain class.
   *
   * @param classRef Type to try converting the current test stream to.
   * @param <T>      Type to try converting the current test stream to.
   * @return T       Type that th current test stream has been converted to (or null if not)
   */
  private <T> T responseToClass(@SuppressWarnings("SameParameterValue") Class<T> classRef) {
    T result = null;

    try {
      JsonParser jsonParser = new ObjectMapper().createParser(logToString());
      result = jsonParser.readValueAs(classRef);
      // No error thrown when parsing to a GuessJson, test passes!
    } catch (IOException e) {
      // Could not read
      // -> exception thrown
      // -> test fails since it must have been the wrong type of response.
      fail();
    }

    return result;
  }

  /**
   * Create a MessageJson for some name and arguments.
   *
   * @param messageName name of the type of message; "hint" or "win"
   * @param messageObject object to embed in a message json
   * @return a MessageJson for the object
   */
  private JsonNode createSampleMessage(String messageName, Record messageObject) {
    MessageJson messageJson = new MessageJson(messageName,
        JsonUtils.serializeRecord(messageObject));
    return JsonUtils.serializeRecord(messageJson);
  }
}