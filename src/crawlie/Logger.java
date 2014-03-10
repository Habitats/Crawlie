package crawlie;

import java.util.Observable;

/**
 * Logger class. Sysouts mid code is bad!
 * 
 * @author Patrick
 * 
 */
public class Logger extends Observable {
  private static int logNumber = 1;
  private static int errorNumber = 1;
  private static Logger instance;

  public static Logger getInstance() {
    if (instance == null)
      instance = new Logger();
    return instance;
  }

  public void log(String log) {
    System.out.println(String.format("%4d: %s", logNumber++, log));
    setChanged();
    notifyObservers(log);
  }

  public void error(String log) {
    System.err.println(String.format("%8d: %s", errorNumber++, log));
    setChanged();
    notifyObservers(log);
  }
}
