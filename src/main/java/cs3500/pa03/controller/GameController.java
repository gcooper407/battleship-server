package cs3500.pa03.controller;

import cs3500.pa03.model.Board;
import cs3500.pa03.model.BotPlayer;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.GameModel;
import cs3500.pa03.model.GameResult;
import cs3500.pa03.model.HumanPlayer;
import cs3500.pa03.model.Model;
import cs3500.pa03.model.Player;
import cs3500.pa03.model.ShipType;
import cs3500.pa03.view.View;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * Represents a controller of a BattleSalvo game with a View object to display game data and gather
 * user input, as well as two Players of this game.
 */
public class GameController implements Controller {

  private final View view;
  private final Model model;
  private final Player p1;
  private final Player p2;

  /**
   * Creates a controller of a BattleSalvo game that is connected to the given view
   * object to display game content and gather user input, and is played by a HumanPlayer
   * and a BotPlayer
   *
   * @param view View object for displaying game elements
   * @param model Model object for storing game (board) state
   * @param rand Random object for randomly generating ship placements and bot shots
   */
  public GameController(View view, Model model, Random rand) {
    this.view = Objects.requireNonNull(view);
    this.model = Objects.requireNonNull(model);
    Board p1Board = new Board();
    Board p2Board = new Board();

    this.p1 = new HumanPlayer(p1Board, rand, view);
    this.p2 = new BotPlayer(p2Board, rand);

    this.model.setBoard(p1Board, true);
    this.model.setBoard(p2Board, false);
  }

  /**
   * Creates a controller of a BattleSalvo game that is connected to the given view
   * object to display game content and gather user input, and is played by the two given Players
   *
   * @param view View object for displaying game elements
   * @param model Model object for storing game (board) state
   */
  public GameController(View view, Model model) {
    this(view, model, new Random());
  }




  /**
   * Runs this program controller, which runs the BattleSalvo game.
   */
  @Override
  public void run() {
    this.initData();
    this.operateGame();
  }

  /**
   * Initializes the data of the BattleSalvo game (setup of board and fleet).
   */
  private void initData() {
    Coord dimensions = this.view.displaySizeQuery(GameModel.MIN_BOARD_DIMENSION,
        GameModel.MAX_BOARD_DIMENSION);

    int minDim = Math.min(dimensions.xpos(), dimensions.ypos());

    Map<ShipType, Integer> specs = this.view.displayFleetQuery(minDim);

    this.p1.setup(dimensions.ypos(), dimensions.xpos(), specs);
    this.p2.setup(dimensions.ypos(), dimensions.xpos(), specs);
  }

  /**
   * Runs the BattleSalvo game, exchanging shots between players until one (or both)
   * has no more ships (all have been sunk).
   */
  private void operateGame() {
    List<Coord> p1Shots;
    List<Coord> p2Shots;

    while (!this.model.isOver()) {
      p1Shots = this.p1.takeShots();
      p2Shots = this.p2.takeShots();

      this.p1.successfulHits(this.p2.reportDamage(p1Shots));
      this.p2.successfulHits(this.p1.reportDamage(p2Shots));
    }

    if (this.model.p1Wins()) {
      this.p1.endGame(GameResult.WIN, "");
    } else if (this.model.p2Wins()) {
      this.p1.endGame(GameResult.LOSE, "");
    } else {
      this.p1.endGame(GameResult.DRAW, "");
    }
  }


}
