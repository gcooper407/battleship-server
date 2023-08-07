package cs3500.pa03.controller;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.model.GameModel;
import cs3500.pa03.model.Model;
import cs3500.pa03.view.GameView;
import cs3500.pa03.view.View;
import java.io.StringReader;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the constructors and methods of the GameController class
 */
class GameControllerTest {

  Controller controller;
  Model model;

  View viewWin;
  Readable inputWin;
  Appendable outputWin;

  View viewLose;
  Readable inputLose;
  Appendable outputLose;

  View viewDraw;
  Readable inputDraw;
  Appendable outputDraw;

  /**
   * Initializes the testing data
   */
  @BeforeEach
  public void initData() {
    String sampleUserInputWin = """
        6 6
        1 1 1 1
        0 0 1 1 2 2 3 3
        2 1 2 3 2 4 2 5
        1 1 1 2 1 3 1 4
        1 0 1 2 1 3 1 4
        2 0 3 0 4 0 5 0
        0 2 3 2 4 2 0 4
        0 3 5 5 5 4 5 3
        5 2 5 1
        """;

    this.inputWin = new StringReader(sampleUserInputWin);
    this.outputWin = new StringBuilder();
    this.viewWin = new GameView(outputWin, inputWin);

    String sampleUserInputLose = """
        6 6
        1 1 1 1
        0 0 0 1 0 4 0 5
        1 1 1 4 1 5 2 1
        2 4 2 5 3 1 3 3
        3 4 3 5 4 1 4 3
        4 4 4 5 0 2 0 3
        1 0 2 0 3 0
        1 2
        1 3
        5 1
        
        """;

    this.inputLose = new StringReader(sampleUserInputLose);
    this.outputLose = new StringBuilder();
    this.viewLose = new GameView(outputLose, inputLose);

    String sampleUserInputDraw = """
        6 6
        1 1 1 1
        0 0 0 1 0 2 0 3
        0 4 0 5 1 0 1 1
        1 2 1 3 1 4 1 5
        5 0 5 1 5 2 5 3
        5 4 5 5 4 5 3 5
        3 0 3 1 3 2
        2 0
        4 0
        2 3
        2 2
        2 4
        2 5
        3 4
        4 2
        """;

    this.inputDraw = new StringReader(sampleUserInputDraw);
    this.outputDraw = new StringBuilder();
    this.viewDraw = new GameView(outputDraw, inputDraw);

    this.model = new GameModel();

    Controller constructorTest = new GameController(viewWin, model);
  }

  /**
   * Tests the .run() method and all related private helper methods, for
   * the instance where Player 1 wins
   */
  @Test
  public void runWin() {
    this.controller = new GameController(this.viewWin, this.model, new Random(1));

    String expected = """
        Hello! Welcome to the OOD BattleSalvo Game!
        Please enter a valid height and width below:
        ------------------------------------------------------
        -------------------------------------------------------------------------------
        Please enter your fleet in the order [Carrier, Battleship, Destroyer, Submarine].
        Remember, your fleet may not exceed size 6.
        -------------------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             - - - - - -
             - - - - - -
             - - - - - -
             - - - - - -
             - - - - - -
             - - - - - -
                
        Your Board:
             B B B B B -
             C C C C C C
             - - D D D D
             - - - - - -
             - - - - - -
             - S S S - -
                
        Please enter 4 distinct shots:
        ------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             O - - - - -
             - O - - - -
             - - X - - -
             - - - O - -
             - - - - - -
             - - - - - -
                
        Your Board:
             X B B B B -
             C C C C C C
             - - D X D D
             - - - - - -
             O - - - - O
             - S S S - -
                
        Please enter 4 distinct shots:
        ------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             O - - - - -
             - O O - - -
             - - X - - -
             - - X O - -
             - - O - - -
             - - O - - -
                
        Your Board:
             X X B B B -
             X C C X C C
             - - D X D D
             - - - O - -
             O - - - - O
             - S S S - -
                
        Please enter 4 distinct shots:
        ------------------------------------------------------------------
        ------------------------------------------------------------------
        Uh Oh! You've entered a shot at a coordinate you've already shot at.
        Please enter 4 distinct shots:
        ------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             O X - - - -
             - O O - - -
             - X X - - -
             - X X O - -
             - O O - - -
             - - O - - -
                
        Your Board:
             X X B B B -
             X X C X C C
             O - X X X D
             - - - O - -
             O - - - - O
             - S S S - -
                
        Please enter 4 distinct shots:
        ------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             O X X X X X
             - O O - - -
             - X X - - -
             - X X O - -
             - O O - - -
             - - O - - -
                
        Your Board:
             X X X X B -
             X X X X X C
             O - X X X D
             - - - O - -
             O - - - - O
             - S S S - -
                
        Please enter 4 distinct shots:
        ------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             O X X X X X
             - O O - - -
             X X X X X -
             - X X O - -
             O O O - - -
             - - O - - -
                
        Your Board:
             X X X X B -
             X X X X X C
             O O X X X D
             - - O O O -
             O - - - - O
             - S S S - -
                
        Please enter 4 distinct shots:
        ------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             O X X X X X
             - O O - - -
             X X X X X -
             X X X O - X
             O O O - - X
             - - O - - X
                
        Your Board:
             X X X X X -
             X X X X X C
             O O X X X X
             - - O O O -
             O - - - - O
             - S S S - -
                
        Please enter 2 distinct shots:
        ------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             O X X X X X
             - O O - - X
             X X X X X X
             X X X O - X
             O O O - - X
             - - O - - X
                
        Your Board:
             X X X X X -
             X X X X X X
             O O X X X X
             - - O O O -
             O - - - - O
             - S S S - -
                
        Player 1, you WIN
        """;

    this.controller.run();

    assertEquals(expected, this.outputWin.toString());

  }

  /**
   * Tests the .run() method and all related private helper methods, for
   * the instance where Player 1 loses
   */
  @Test
  public void runLose() {
    this.controller = new GameController(this.viewLose, this.model, new Random(1));

    String expected = """
        Hello! Welcome to the OOD BattleSalvo Game!
        Please enter a valid height and width below:
        ------------------------------------------------------
        -------------------------------------------------------------------------------
        Please enter your fleet in the order [Carrier, Battleship, Destroyer, Submarine].
        Remember, your fleet may not exceed size 6.
        -------------------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             - - - - - -
             - - - - - -
             - - - - - -
             - - - - - -
             - - - - - -
             - - - - - -
                
        Your Board:
             B B B B B -
             C C C C C C
             - - D D D D
             - - - - - -
             - - - - - -
             - S S S - -
                
        Please enter 4 distinct shots:
        ------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             O - - - - -
             O - - - - -
             - - - - - -
             - - - - - -
             O - - - - -
             O - - - - -
                
        Your Board:
             X B B B B -
             C C C C C C
             - - D X D D
             - - - - - -
             O - - - - O
             - S S S - -
                
        Please enter 4 distinct shots:
        ------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             O - - - - -
             O O O - - -
             - - - - - -
             - - - - - -
             O O - - - -
             O O - - - -
                
        Your Board:
             X X B B B -
             X C C X C C
             - - D X D D
             - - - O - -
             O - - - - O
             - S S S - -
                
        Please enter 4 distinct shots:
        ------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             O - - - - -
             O O O O - -
             - - - - - -
             - - - O - -
             O O O - - -
             O O O - - -
                
        Your Board:
             X X B B B -
             X X C X C C
             O - X X X D
             - - - O - -
             O - - - - O
             - S S S - -
                
        Please enter 4 distinct shots:
        ------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             O - - - - -
             O O O O O -
             - - - - - -
             - - - O O -
             O O O O - -
             O O O O - -
                
        Your Board:
             X X X X B -
             X X X X X C
             O - X X X D
             - - - O - -
             O - - - - O
             - S S S - -
                
        Please enter 4 distinct shots:
        ------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             O - - - - -
             O O O O O -
             X - - - - -
             X - - O O -
             O O O O O -
             O O O O O -
                
        Your Board:
             X X X X B -
             X X X X X C
             O O X X X X
             - - O O O -
             O - - - - O
             - S S S - -
                
        Please enter 3 distinct shots:
        ------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             O X X X - -
             O O O O O -
             X - - - - -
             X - - O O -
             O O O O O -
             O O O O O -
                
        Your Board:
             X X X X X -
             X X X X X X
             O O X X X X
             - - O O O O
             O O - - - O
             - S S S - -
                
        Please enter 1 distinct shots:
        ------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             O X X X - -
             O O O O O -
             X X - - - -
             X - - O O -
             O O O O O -
             O O O O O -
                
        Your Board:
             X X X X X O
             X X X X X X
             O O X X X X
             - O O O O O
             O O - O - O
             - S S S O -
                
        Please enter 1 distinct shots:
        ------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             O X X X - -
             O O O O O -
             X X - - - -
             X X - O O -
             O O O O O -
             O O O O O -
                
        Your Board:
             X X X X X O
             X X X X X X
             O O X X X X
             O O O O O O
             O O - O O O
             O S S S O O
                
        Please enter 1 distinct shots:
        ------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             O X X X - -
             O O O O O X
             X X - - - -
             X X - O O -
             O O O O O -
             O O O O O -
                
        Your Board:
             X X X X X O
             X X X X X X
             O O X X X X
             O O O O O O
             O O O O O O
             O X X X O O
                
        Player 1, you LOSE
        """;

    this.controller.run();

    assertEquals(expected, this.outputLose.toString());

  }

  /**
   * Tests the .run() method and all related private helper methods, for
   * the instance where Player 1 draws with Player 2
   */
  @Test
  public void runDraw() {
    this.controller = new GameController(this.viewDraw, this.model, new Random(1));

    String expected = """
        Hello! Welcome to the OOD BattleSalvo Game!
        Please enter a valid height and width below:
        ------------------------------------------------------
        -------------------------------------------------------------------------------
        Please enter your fleet in the order [Carrier, Battleship, Destroyer, Submarine].
        Remember, your fleet may not exceed size 6.
        -------------------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             - - - - - -
             - - - - - -
             - - - - - -
             - - - - - -
             - - - - - -
             - - - - - -
                
        Your Board:
             B B B B B -
             C C C C C C
             - - D D D D
             - - - - - -
             - - - - - -
             - S S S - -
                
        Please enter 4 distinct shots:
        ------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             O - - - - -
             O - - - - -
             X - - - - -
             X - - - - -
             - - - - - -
             - - - - - -
                
        Your Board:
             X B B B B -
             C C C C C C
             - - D X D D
             - - - - - -
             O - - - - O
             - S S S - -
                
        Please enter 4 distinct shots:
        ------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             O X - - - -
             O O - - - -
             X - - - - -
             X - - - - -
             O - - - - -
             O - - - - -
                
        Your Board:
             X X B B B -
             X C C X C C
             - - D X D D
             - - - O - -
             O - - - - O
             - S S S - -
                
        Please enter 4 distinct shots:
        ------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             O X - - - -
             O O - - - -
             X X - - - -
             X X - - - -
             O O - - - -
             O O - - - -
                
        Your Board:
             X X B B B -
             X X C X C C
             O - X X X D
             - - - O - -
             O - - - - O
             - S S S - -
                
        Please enter 4 distinct shots:
        ------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             O X - - - X
             O O - - - X
             X X - - - X
             X X - - - X
             O O - - - -
             O O - - - -
                
        Your Board:
             X X X X B -
             X X X X X C
             O - X X X D
             - - - O - -
             O - - - - O
             - S S S - -
                
        Please enter 4 distinct shots:
        ------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             O X - - - X
             O O - - - X
             X X - - - X
             X X - - - X
             O O - - - X
             O O - O O X
                
        Your Board:
             X X X X B -
             X X X X X C
             O O X X X X
             - - O O O -
             O - - - - O
             - S S S - -
                
        Please enter 3 distinct shots:
        ------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             O X - X - X
             O O - O - X
             X X - X - X
             X X - - - X
             O O - - - X
             O O - O O X
                
        Your Board:
             X X X X X -
             X X X X X X
             O O X X X X
             - - O O O O
             O - - - - O
             - S S S - -
                
        Please enter 1 distinct shots:
        ------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             O X X X - X
             O O - O - X
             X X - X - X
             X X - - - X
             O O - - - X
             O O - O O X
                
        Your Board:
             X X X X X O
             X X X X X X
             O O X X X X
             - - O O O O
             O O - O - O
             - S S S - -
                
        Please enter 1 distinct shots:
        ------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             O X X X X X
             O O - O - X
             X X - X - X
             X X - - - X
             O O - - - X
             O O - O O X
                
        Your Board:
             X X X X X O
             X X X X X X
             O O X X X X
             - O O O O O
             O O - O - O
             O S S S O -
                
        Please enter 1 distinct shots:
        ------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             O X X X X X
             O O - O - X
             X X - X - X
             X X X - - X
             O O - - - X
             O O - O O X
                
        Your Board:
             X X X X X O
             X X X X X X
             O O X X X X
             O O O O O O
             O O - O - O
             O S S S O O
                
        Please enter 1 distinct shots:
        ------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             O X X X X X
             O O - O - X
             X X X X - X
             X X X - - X
             O O - - - X
             O O - O O X
                
        Your Board:
             X X X X X O
             X X X X X X
             O O X X X X
             O O O O O O
             O O - O O O
             O S S S O O
                
        Please enter 1 distinct shots:
        ------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             O X X X X X
             O O - O - X
             X X X X - X
             X X X - - X
             O O O - - X
             O O - O O X
                
        Your Board:
             X X X X X O
             X X X X X X
             O O X X X X
             O O O O O O
             O O - O O O
             O S X S O O
                
        Please enter 1 distinct shots:
        ------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             O X X X X X
             O O - O - X
             X X X X - X
             X X X - - X
             O O O - - X
             O O O O O X
                
        Your Board:
             X X X X X O
             X X X X X X
             O O X X X X
             O O O O O O
             O O O O O O
             O S X S O O
                
        Please enter 1 distinct shots:
        ------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             O X X X X X
             O O - O - X
             X X X X - X
             X X X - - X
             O O O O - X
             O O O O O X
                
        Your Board:
             X X X X X O
             X X X X X X
             O O X X X X
             O O O O O O
             O O O O O O
             O X X S O O
                
        Please enter 1 distinct shots:
        ------------------------------------------------------------------
        -------------------------------------------------------------------------------
        Opponent Board Data:
             O X X X X X
             O O - O - X
             X X X X X X
             X X X - - X
             O O O O - X
             O O O O O X
                
        Your Board:
             X X X X X O
             X X X X X X
             O O X X X X
             O O O O O O
             O O O O O O
             O X X X O O
                
        Player 1, you DRAW
        """;

    this.controller.run();

    assertEquals(expected, this.outputDraw.toString());
  }
}