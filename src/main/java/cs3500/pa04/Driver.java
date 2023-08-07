package cs3500.pa04;

import cs3500.pa03.controller.Controller;
import cs3500.pa03.controller.GameController;
import cs3500.pa03.model.Board;
import cs3500.pa03.model.BotPlayer;
import cs3500.pa03.model.GameModel;
import cs3500.pa03.model.Model;
import cs3500.pa03.model.Player;
import cs3500.pa03.view.GameView;
import cs3500.pa03.view.View;
import cs3500.pa04.client.ProxyController;
import cs3500.pa04.json.GameType;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Random;

/**
 * This is the main driver of this project.
 */
public class Driver {

  /**
   * Project entry point
   *
   * @param args - no command line args required
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      View view = new GameView(System.out, new InputStreamReader(System.in));
      Model model = new GameModel();

      Controller controller = new GameController(view, model, new Random(1));
      controller.run();
    } else {
      String host = args[0];
      try {
        int port = Integer.parseInt(args[1]);
        Driver.runClient(host, port);
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Invalid port");
      } catch (IOException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * This method connects to the server at the given host and port, builds a proxy referee
   * to handle communication with the server, and sets up a client player for a BattleSalvo game
   * against a server player.
   *
   * @param host the server host
   * @param port the server port
   * @throws IOException if there is a communication issue with the server
   */
  private static void runClient(String host, int port) throws IOException {
    Socket socket = new Socket(host, port);
    GameType gametype = GameType.SINGLE;
    String username = "gcooper407";
    Player bot = new BotPlayer(new Board(), new Random());
    Controller proxyController = new ProxyController(socket, bot, gametype, username);
    proxyController.run();
  }
}