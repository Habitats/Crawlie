package crawlie;

import java.util.Observable;

/**
 * Logger class. Sysouts mid code is bad!
 *
 * Used to delegate messages to the GUI etc
 *
 * @author Patrick
 */
public class Logger extends Observable {

  private int logNumber = 1;
  private int errorNumber = 1;
  private int statusNumber = 1;
  private static Logger instance;

  public static Logger getInstance() {
    if (instance == null) {
      instance = new Logger();
    }
    return instance;
  }


  public void log(String log) {
    // System.out.println(String.format("%4d: %s", logNumber++, log));
    setChanged();
    notifyObservers(new Message(log, Message.Type.LOG));
  }

  public void error(String log) {
    // System.err.println(String.format("%8d: %s", errorNumber++, log));
    setChanged();
    notifyObservers(new Message(log, Message.Type.ERROR));
  }

  public void status(String log) {
    System.out.println(String.format("%8d: %s", statusNumber++, log));
    setChanged();
    notifyObservers(new Message(log, Message.Type.STATUS));
  }
}
