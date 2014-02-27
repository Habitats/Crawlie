package crawlie;

public class Logger {
  private static int logNumber = 1;
  private static int errorNumber = 1;

  public static void log(String log) {
    System.out.println(String.format("%4d: %s", logNumber++, log));
  }

  public static void error(String string) {
    System.err.println(String.format("%8d: %s", errorNumber++, string));
  }

}
