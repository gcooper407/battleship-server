package cs3500.pa04;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests the Driver class with args (i.e. running the client)
 */
class DriverTest {

  @Test
  public void testMainWithArgs() {
    String[] args = new String[]{"0.0.0.0", "35001"};
    String[] wrongArgs = new String[]{"0.0.0.0", "3500asd"};
    assertDoesNotThrow(() -> Driver.main(args));
    assertThrows(IllegalArgumentException.class, () -> Driver.main(wrongArgs));
  }

}